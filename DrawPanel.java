import java.awt.*;
import javax.swing.*;

class DrawPanel extends JPanel {
	TerraMap map;
	
	public DrawPanel() {}
	
	public DrawPanel(TerraMap m) {
		map = m;
	}
	
	public void paintComponent(Graphics g) {		
		int i = (int)(Math.random()*600)+100;
		
		map.destroy(i,map.map[i],20);
		
		Graphics2D g2d = (Graphics2D) g;
		
		//g.setColor(Color.green);
		
		g2d.setColor(Color.black);
		
		g2d.fillRect(0,0,800,600);
		
		g2d.setPaint(new GradientPaint(0,300,new Color(0,100,30),0,600,new Color(150,150,20)));
		
		//g2d.fillOval(100,100,300,300);
		
		for(int x = 0; x < map.map.length; x++) {
			g2d.drawLine(x+1,600,x+1,600 - map.map[x]);
		}
	}
}