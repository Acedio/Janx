import java.awt.*;
import javax.swing.*;

public class Tank {
	private int x, y, health;
	
	private static final int HEALTH = 100;
	
	private TerraMap map;
	
	public Tank(int px, TerraMap m) {
		map = m;
		x = px;
		y = map.heightAt(x);
		health = HEALTH;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		
		g.fillOval(x - 10, map.getH() - (y + 10), 20, 20);
		
		g.setColor(Color.black);
		
		g.fillRect(x - 15, map.getH() - (y + 20), 30, 6);
		
		g.setColor(Color.yellow);
		
		g.fillRect(x - 14, map.getH() - (y + 19), (int)(28 * (((double)health)/100)), 4);
	}
	
	public void drop() {
		y = map.getH() + 1;
	}
	
	public boolean isDead() {
		if(health > 0) return false;
		return true;
	}
}