import java.awt.*;
import javax.swing.*;

public class Tank {
	private int x, y;
	private String name;
	private TerraMap map;
	private int health;
	
	public Tank(String n, int xPos, TerraMap m) {
		name = n;
		map = m;
		x = xPos;
		y = DrawPanel.FRAMEY - (map.map[x] + 1);
		health = 100;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		
		g.fillOval(x - 10, y - 10, 20, 20);
		
		g.setColor(Color.black);
		
		g.fillRect(x - 15, y - 20, 30, 6);
		
		g.setColor(Color.yellow);
		
		g.fillRect(x - 14, y - 19, (int)(28 * (((double)health)/100)), 4);
	}
	
	public Explosion fire(int angle, int power) {
		double bX = (double)x;
		double bY = (double)y;
		
		if(angle > 360 || angle < 0 || power > 100 || power < 0) {
			return new Explosion(0,0,0);
		}
		
		double rads = ((double)angle/180)*Math.PI;
		
		double xVel = ((double)power / 5.0) * Math.cos(rads);
		double yVel = ((double)power / 5.0) * Math.sin(rads);
		
		while(bX >= 0 && bX < DrawPanel.FRAMEX && (int)bY > map.map[(int)bX]) {
			yVel -= (0.5/10);
			bX += (xVel/10);
			bY += (yVel/10);
		}
		map.destroy((int)bX,(int)bY,20);
		return new Explosion((int)bX,(int)bY,40);
	}
	
	public void checkExplosion(Explosion ex) {
		double distance = Math.sqrt(Math.pow((double)x-(double)ex.getX(), 2) + Math.pow((double)y-(double)ex.getY(), 2));
		System.out.println(distance);
		if(distance < (double)ex.getRad()) {
			health -= (int)((((double)ex.getRad() - distance)/((double)ex.getRad()))*20);
		}
	}
	
	public void drop() {
		y = 600 - (map.map[x] + 1);
	}
}