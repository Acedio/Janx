import java.awt.*;
import javax.swing.*;

public class Projectile{
	private double x, y;
	private double xVel, yVel;
	
	private TerraMap map;
	
	private static final double grav = 0.15;
	
	private static final int expSize = 20;
	
	private static final int size = 6;
	
	public Projectile(int px, int py, double xV, double yV, TerraMap m)
	{
		x = (double)px;
		y = (double)py;
		xVel = xV;
		yVel = yV;
		
		map = m;
	}
	
	public void move(){
		yVel -= grav;
		x += xVel;
		y += yVel;
	}
	
	public void draw(Graphics g){
		g.setColor(Color.yellow);
		g.fillOval((int)x - (size/2), (map.getH() - ((int)y + (size/2))), size, size);
	}
	
	public Explosion checkCollision() {
		if(((int)x > map.getW() - 1) || ((int)x < 0)) {
			return new Explosion(0,0,0, map);
		}
		if((int)y <= map.heightAt((int)x)) {
			return new Explosion((int)x,(int)y,expSize, map);
		}
		return null;
	}
}