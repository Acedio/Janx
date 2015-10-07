import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TestJanx extends JFrame implements WindowListener{
	private static int DEFAULT_FPS = 80;
	
	private JanxPanel jp;
	
	public TestJanx(long period){
		super("Janx");
		makeGUI(period);
		addWindowListener( this );
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	private void makeGUI(long period){
		Container c = getContentPane();
		
		jp = new JanxPanel(this, period);
		c.add(jp, BorderLayout.CENTER);
	}
	
	public static void main(String[] args){
		int fps = DEFAULT_FPS;
		if(args.length != 0){
			fps = Integer.parseInt(args[0]);
		}
		
		long period = (long) 1000.0/fps;
		System.out.println("fps: " + fps + "; period: " + period + " ms");
		
		new TestJanx(period*1000000L);
	}
	
	public void windowActivated(WindowEvent e) {
		jp.resumeGame();
	}

	public void windowDeactivated(WindowEvent e) {
		jp.pauseGame();
	}

	public void windowDeiconified(WindowEvent e) {
		jp.resumeGame();
	}

	public void windowIconified(WindowEvent e) {
		jp.pauseGame();
	}

	public void windowClosing(WindowEvent e) {
		jp.stopGame();
	}

	public void windowClosed(WindowEvent e) {
	}
		
	public void windowOpened(WindowEvent e) {
	}
}