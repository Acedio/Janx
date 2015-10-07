import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Options extends JFrame {
	JanxPanel jp;
	
	JTextField playerField;
	
	JCheckBox windBox;
	
	public Options(JanxPanel j) {
		super("Options");
		jp = j;
		setResizable(false);
		go();
		pack();
		setVisible(true);
	}
	
	public void go() {
		JPanel playerPanel = new JPanel();
		JLabel label = new JLabel("Players:");
		playerField = new JTextField("" + jp.getPlayers(), 3);
		JButton button = new JButton("OK!");
		windBox = new JCheckBox("Wind", jp.isWindy());
		
		button.addActionListener(new ButtonListener());
		
		playerPanel.add(label);
		playerPanel.add(playerField);
		playerPanel.add(button);
		
		this.add(BorderLayout.NORTH, playerPanel);
		this.add(BorderLayout.CENTER, windBox);
		this.add(BorderLayout.SOUTH, button);
		
		playerField.requestFocus();
		playerField.selectAll();
	}
	
	public class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			int i = Integer.valueOf(playerField.getText());
			if(i >= 2 && i <= 16){
				jp.setPlayers(i);
				jp.setWindy(windBox.isSelected());
				setVisible(false);
			} else {
				playerField.setText("Nope");
				playerField.requestFocus();
				playerField.selectAll();
			}
		}
	}
}