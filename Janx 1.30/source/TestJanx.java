import javax.swing.*; //imports and the like
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class TestJanx extends JFrame implements WindowListener{
	private static int DEFAULT_FPS = 80; //80 fps
	
	private JanxPanel jp; //our game panel
	
	public TestJanx(long period){ //constructor
		super("Janx");
		makeGUI(period);
		addWindowListener( this );
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	private void makeGUI(long period){ //make our window and menu bar
		Container c = getContentPane();
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem newGameMenuItem = new JMenuItem("New Game");
		JMenuItem optionsMenuItem = new JMenuItem("Options");
		JMenuItem aboutMenuItem = new JMenuItem("About");
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		
		newGameMenuItem.addActionListener(new NewGameMenuListener());
		optionsMenuItem.addActionListener(new OptionsMenuListener());
		aboutMenuItem.addActionListener(new AboutMenuListener());
		exitMenuItem.addActionListener(new ExitMenuListener());
		
		fileMenu.addMenuListener(new FileMenuListener());
		
		fileMenu.add(newGameMenuItem);
		fileMenu.add(optionsMenuItem);
		fileMenu.add(aboutMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		
		jp = new JanxPanel(this, period, 2);
		c.add(jp, BorderLayout.CENTER);
	}
	
	public static void main(String[] args){ //main, handles fps -> period
		int fps = DEFAULT_FPS;
		if(args.length != 0){
			fps = Integer.parseInt(args[0]); //we can specify an FPS
		}
		
		long period = (long) 1000.0/fps;
		//System.out.println("fps: " + fps + "; period: " + period + " ms");
		
		new TestJanx(period*1000000L); // ns -> ms
	}
	
	public void windowActivated(WindowEvent e) { //all our window event listeners
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
	
	public class NewGameMenuListener implements ActionListener { //menu event listeners
		public void actionPerformed(ActionEvent ev) {
			jp.remake();
		}
	}
	
	public class OptionsMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			new Options(jp);
		}
	}
	
	public class ExitMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			jp.stopGame();
		}
	}
	
	public class AboutMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			new About();
		}
	}
	
	public class FileMenuListener implements MenuListener { // make sure we can see our menus
		public void menuSelected(MenuEvent ev) {
			jp.pauseRender();
		}
		
		public void menuDeselected(MenuEvent ev) {
			jp.resumeRender();
		}
		
		public void menuCanceled(MenuEvent ev) {
			jp.resumeRender();
		}
	}
}