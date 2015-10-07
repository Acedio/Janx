import java.awt.*;
import javax.swing.*;

public class Explosion {
	private int x, y, rad, times;
		
	private TerraMap map;
		
	public Explosion(int px, int py, int r, TerraMap m) {
		x = px;
		y = py;
		rad = r;
		times = r;
		map = m;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getRad() {
		return rad;
	}
	
	public boolean draw(Graphics g) {
		if(times > 0){
			g.setColor(new Color(255,(int)(Math.random()*255),0));
			g.fillOval(x - times, (map.getH() - (y + times)), times*2, times*2);
			times--;
			return true;
		} else {
			return false;
		}
	}
}