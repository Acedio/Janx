import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class JanxPanel extends JPanel implements Runnable{
	private static final int PWIDTH = 800; //width of window
	private static final int PHEIGHT = 600; //height of window
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	private static final int MAX_FRAME_SKIPS = 5;
	
	private Thread animator;
	
	private volatile boolean running = false;
	private volatile boolean gameOver = false;
	private volatile boolean isPaused = false;
	private volatile boolean rendPaused = false; //pause drawing (for menu access)
	
	private boolean isFiring = false;
	
	private Graphics dbg;
	private Image dbImage = null; //img for double buffering
	
	private long period; // ms between draws
	
	private TestJanx janxTop; // the reference to the frame
	
	private TerraMap map; //our random map
	
	private ArrayList<Tank> tanks; //all our tanks
	
	private ArrayList<Projectile> projectiles; //projectiles
		
	private ArrayList<Explosion> explosions; //explosions
	
	private int turn; //whose turn?
	
	private Font font; //font for stuff
	private FontMetrics metrics; //Not sure, actually. Probably has to do with font size
	
	private volatile int players = 2; //standard players
		
	private boolean windy = true; //standard wind
	
	public JanxPanel(TestJanx janx, long period, int p) { //constructor
		janxTop = janx;
		this.period = period;
		
		players = p;
		
		map = new TerraMap(PWIDTH, PHEIGHT, 3, 6);
		
		tanks = new ArrayList<Tank>();
		
		projectiles = new ArrayList<Projectile>();
		
		explosions = new ArrayList<Explosion>();
		
		for(int i = 0; i < players; i++) {
			tanks.add(new Tank((PWIDTH/(players+1))*(i+1), map));
		}
		
		tanks.get(turn).setWind(windy);//wind is handled per tank
		
		font = new Font("SansSerif", Font.BOLD, 18);
		metrics = this.getFontMetrics(font);
		
		setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		setFocusable(true);
		requestFocus();
		addKeyListener( new KeyAdapter() {
		public void keyPressed(KeyEvent e)
			{ processKey(e); }
		});
	}
	
	public void remake() { //incase we want to restart
		map = new TerraMap(PWIDTH, PHEIGHT, 3, 6);
		
		tanks = new ArrayList<Tank>();
		
		projectiles = new ArrayList<Projectile>();
		
		explosions = new ArrayList<Explosion>();
		
		for(int i = 0; i < players; i++) {
			tanks.add(new Tank((PWIDTH/(players+1))*(i+1), map));
		}
		
		tanks.get(turn).setWind(windy); //wind is handled per tank
		
		running = true; // basically reset all variables to initial posisitions
		isPaused = false;
		rendPaused = false;
		gameOver = false;
		isFiring = false;
		
		turn = 0;
	}
	
	public void addNotify(){ //notifies monitors
		super.addNotify();
		startGame();
	}
	
	private void startGame(){ // everything is go!
		if(animator == null || !running){
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void stopGame(){ //end game
		running = false;
	}
	
	public void run(){ //main thread, handles FPS
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		
		beforeTime = System.nanoTime();
		
		running = true;
		while(running){
			gameUpdate();
			gameRender();
			paintScreen();
			
			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;
			
			if(sleepTime > 0) {
				try{
					Thread.sleep(sleepTime/1000000L);
				} catch(InterruptedException ex) {
				}
				
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else {
				excess -= sleepTime;
				overSleepTime = 0L;
				
				if(++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield();
					noDelays = 0;
				}
			}
			
			beforeTime = System.nanoTime();
			
			int skips = 0;
			while((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate();
				skips++;
			}
		}
		System.exit(0);
	}
	
	private void gameUpdate(){ //updats positions, etc
		if(!isPaused && !gameOver){
			for(int i = 0; i < projectiles.size(); i++) {
				projectiles.get(i).move();
				Explosion exp = projectiles.get(i).checkCollision();
				if(exp != null){
					map.destroy(exp);
					for(int j = 0; j < tanks.size(); j++) {
						if(!tanks.get(j).isDead()){
							tanks.get(j).checkExplosion(exp);
						}
					}
					projectiles.remove(i);
					explosions.add(exp);
					turn++;
					if(turn >= players) turn = 0;
					while(tanks.get(turn).isDead()) {
						turn++;
						if(turn >= players) turn = 0;
					}
					tanks.get(turn).setWind(windy);
					isFiring = false;
				} else {
					isFiring = true;
				}
					
			}
			for(int i = 0; i < tanks.size(); i++){
				tanks.get(i).drop();
			}
			if(tanks.size() == 0) {
				running = false;
			}
		}
	}
	
	private void gameRender(){ //renders to buffer
		if(rendPaused == false){
			if(dbImage == null){
				dbImage = createImage(PWIDTH,PHEIGHT);
				if(dbImage == null){
					System.out.println("dbImage is null");
					return;
				} else {
					dbg = dbImage.getGraphics();
				}
			}
			
			dbg.setColor(Color.black);
			dbg.fillRect(0,0,PWIDTH,PHEIGHT);
			
			map.draw(dbg);
			
			int left = 0;
			
			for(int i = 0; i < tanks.size(); i++) {
				if(!tanks.get(i).isDead()){
					tanks.get(i).draw(dbg);
					left++;
				}
			}
			
			if(left == 1) gameOver = true;
			
			if(tanks.size() != 0) {
				tanks.get(turn).drawTurret(dbg);
			}
			
			for(int i = 0; i < projectiles.size(); i++) {
				projectiles.get(i).draw(dbg);
			}
			
			for(int i = 0; i < explosions.size(); i++) {
				if(explosions.get(i).draw(dbg) == false) {
					explosions.remove(i);
				}
			}
			
			dbg.setColor(Color.white);
			dbg.setFont(font);
			
			dbg.drawString("Player " + (turn + 1), 34, 35);
			
			dbg.drawString("Angle: " + tanks.get(turn).getAng() + "\u00B0", 20, 60);
			
			if(gameOver){
				for(int i = 0; i < tanks.size(); i++){
					if(!tanks.get(i).isDead()){
						dbg.drawString("Player " + (i+1) + " wins!", 320, 170);
					}
				}
			}
		}
	}
	
	public void paintScreen(){ //paints buffer to screen
		Graphics g;
		if(rendPaused == false){
			try{
				g = this.getGraphics();
				if((g != null) && (dbImage != null)){
					g.drawImage(dbImage,0,0,null);
				}
				Toolkit.getDefaultToolkit().sync();
				g.dispose();
			} catch(Exception e) {
				System.out.println("Graphics context error: " + e);
			}
		}
	}
	
	private void processKey(KeyEvent e){ //handles input
		int keyCode = e.getKeyCode();
		if((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown()) ) {
			running = false;
		}
		if(!isPaused && !gameOver) {
			if(tanks.size() != 0) {
				if(keyCode == KeyEvent.VK_RIGHT) {
					tanks.get(turn).changeAngle(-1);
				} else if(keyCode == KeyEvent.VK_LEFT) {
					tanks.get(turn).changeAngle(1);
				} else if(keyCode == KeyEvent.VK_UP) {
					tanks.get(turn).changePower(1);
				} else if(keyCode == KeyEvent.VK_DOWN) {
					tanks.get(turn).changePower(-1);
				} else if(keyCode == KeyEvent.VK_SPACE) {
					if(isFiring == false) {
						projectiles.add(tanks.get(turn).fire());
					}
				}
			}
		}
	}
	
	public void setPlayers(int p){ //change player ammount
		if(players != p) {
			players = p;
			remake();
		}
	}
	
	public int getPlayers(){ // how many players?
		return players;
	}
	
	public void pauseGame(){ // pause it!
		isPaused = true;
	}
	
	public void resumeGame(){ //resuuume
		isPaused = false;
	}
	
	public void pauseRender(){ //pause rendering for menu
		isPaused = true;
		rendPaused = true;
	}
	
	public void resumeRender(){ //resume
		isPaused = false;
		rendPaused = false;
	}
	
	public boolean isWindy(){
		return windy;
	}
	
	public void setWindy(boolean w){ //set windy
		if(windy != w){
			windy = w;
			remake();
		}
	}
}