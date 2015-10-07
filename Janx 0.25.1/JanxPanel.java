import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanxPanel extends JPanel implements Runnable{
	private static final int PWIDTH = 800;
	private static final int PHEIGHT = 600;
	
	private static final int NO_DELAYS_PER_YIELD = 16;
	
	private static final int MAX_FRAME_SKIPS = 5;
	
	private Thread animator;
	
	private volatile boolean running = false;
	private volatile boolean gameOver = false;
	private volatile boolean isPaused = false;
	
	private Graphics dbg;
	private Image dbImage = null;
	
	private long period;
	
	private TestJanx janxTop;
	
	public JanxPanel(TestJanx janx, long period) {
		janxTop = janx;
		this.period = period;
		
		setBackground(Color.black);
		setPreferredSize(new Dimension(PWIDTH, PHEIGHT));
		
		setFocusable(true);
		requestFocus();
		readyForTermination();
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
		}
	}
	
	private void gameRender(){
		if(dbImage == null){
			dbImage = createImage(PWIDTH,PHEIGHT);
			if(dbImage == null){
				System.out.println("dbImage is null");
				return;
			} else {
				dbg = dbImage.getGraphics();
			}
			
			dbg.setColor(Color.black);
			dbg.fillRect(0,0,PWIDTH,PHEIGHT);
			
			
			
			if(gameOver){
			}
		}
	}
	
	public void paintScreen(){
		Graphics g;
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
	
	private void readyForTermination(){
		addKeyListener(new KeyAdapter() {
			public void KeyPressed(KeyEvent e){
				int keyCode = e.getKeyCode();
				if((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) || (keyCode == KeyEvent.VK_END) || ((keyCode == KeyEvent.VK_C) && e.isControlDown()) ) {
					running = false;
				}
			}
		});
	}
	
	public void pauseGame(){
		isPaused = true;
	}
	
	public void resumeGame(){
		isPaused = false;
	}
}