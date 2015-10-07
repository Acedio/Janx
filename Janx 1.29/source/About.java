import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class About extends JFrame {	
	public About() {
		super("Options");
		setResizable(false);
		go();
		requestFocus();
		pack();
		setVisible(true);
	}
	
	public void go() {
		
		this.add(BorderLayout.NORTH, new JLabel("Created By Josh Simmons"));
		this.add(BorderLayout.CENTER, new JLabel("josh@joshsimmons.net"));
		this.add(BorderLayout.SOUTH, new JLabel("GPL'd, yo."));
	}
}