import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Options extends JFrame {
	JanxPanel jp;
	
	JTextField field;
	
	public Options(JanxPanel j) {
		super("Options");
		jp = j;
		setResizable(false);
		go();
		pack();
		setVisible(true);
	}
	
	public void go() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel("Players:");
		field = new JTextField(3);
		JButton button = new JButton("GO!");
		
		button.addActionListener(new ButtonListener());
		
		panel.add(label);
		panel.add(field);
		panel.add(button);
		
		this.add(panel);
		
		field.requestFocus();
	}
	
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			int i = Integer.valueOf(field.getText());
			if(i > 1 && i < 16){
				jp.setPlayers(i);
				setVisible(false);
			} else {
				field.setText("err");
			}
		}
	}
}