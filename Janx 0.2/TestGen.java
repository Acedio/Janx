import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TestGen {
	JFrame frame;
	TerraMap map;
	DrawPanel panel;
	JTextField ang, pow;
	
	public static void main(String[] args) {
		TestGen gen = new TestGen();
		gen.go();
	}
	
	public void go() {
		map = new TerraMap(3, 6);
		frame = new JFrame("Janx");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new DrawPanel(map);
		
		JPanel angPowPan = new JPanel();
		
		ang = new JTextField(4);
		pow = new JTextField(4);
		
		JButton redo = new JButton("Redo");
		redo.addActionListener(new RedoListener());
		
		JButton destroy = new JButton("Destroy");
		destroy.addActionListener(new DestroyListener());
		
		angPowPan.add(ang);
		angPowPan.add(pow);
		angPowPan.add(destroy);
		
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.getContentPane().add(BorderLayout.SOUTH, redo);
		frame.getContentPane().add(BorderLayout.NORTH, angPowPan);
		
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	class DestroyListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if(ang.getText() != "" && pow.getText() != ""){
				panel.destroy(Integer.valueOf(ang.getText()).intValue(),Integer.valueOf(pow.getText()).intValue());
			}
			frame.repaint();
		}
	}
	
	class RedoListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			map = new TerraMap(3, 6);
			panel.remake(map);
			frame.repaint();
		}
	}
}