import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class JanxPanel extends JPanel implements Runnable{
	private static final int PWIDTH = 800;
	private static final int PHEIGHT = 600;
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	private static final int MAX_FRAME_SKIPS = 5;
	
	private Thread animator;
	
	private volatile boolean running = false;
	private volatile boolean gameOver = false;
	private volatile boolean isPaused = false;
	private volatile boolean rendPaused = false;
	
	private boolean isFiring = false;
	
	private Graphics dbg;
	private Image dbImage = null;
	
	private long period;
	
	private TestJanx janxTop;
	
	private TerraMap map;
	
	private ArrayList<Tank> tanks;
	
	private ArrayList<Projectile> projectiles;
		
	private ArrayList<Explosion> explosions;
	
	private int turn;
	
	private Font font;
	private FontMetrics metrics;
	
	private int players = 3;
	
	public JanxPanel(TestJanx janx, long period, int p) {
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
	
	public void remake(int p) {
		players = p;
		
		map = new TerraMap(PWIDTH, PHEIGHT, 3, 6);
		
		tanks = new ArrayList<Tank>();
		
		projectiles = new ArrayList<Projectile>();
		
		explosions = new ArrayList<Explosion>();
		
		for(int i = 0; i < players; i++) {
			tanks.add(new Tank((PWIDTH/(players+1))*(i+1), map));
		}
	}
	
	public void addNotify(){
		super.addNotify();
		startGame();
	}
	
	private void startGame(){
		if(animator == null || !running){
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void stopGame(){
		running = false;
	}
	
	public void run(){
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
	
	private void gameUpdate(){
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
	
	private void gameRender(){
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
			
			for(int i = 0; i < tanks.size(); i++) {
				if(!tanks.get(i).isDead()){
					tanks.get(i).draw(dbg);
				}
			}
			
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
			}
		}
	}
	
	public void paintScreen(){
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
	
	private void processKey(KeyEvent e){
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
	
	public void pauseGame(){
		isPaused = true;
	}
	
	public void resumeGame(){
		isPaused = false;
	}
	
	public void pauseRender(){
		isPaused = true;
		rendPaused = true;
	}
	
	public void resumeRender(){
		isPaused = false;
		rendPaused = false;
	}
}