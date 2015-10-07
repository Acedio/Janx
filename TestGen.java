import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestGen {
	JFrame frame;
	TerraMap map;
	
	public static void main(String[] args) {
		TestGen gen = new TestGen();
		gen.go();
	}
	
	public void go() {
		map = new TerraMap(3, 6);
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DrawPanel panel = new DrawPanel(map);
		
		JButton redo = new JButton("Redo");
		redo.addActionListener(new RedoListener());
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, redo);
		
		frame.setSize(800,600);
		frame.setVisible(true);
	}
	
	class RedoListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.repaint();
		}
	}
}