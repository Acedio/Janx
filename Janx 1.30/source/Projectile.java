import java.awt.*;
import javax.swing.*;

public class Projectile{ //what tanks fire
	private double x, y;
	private double xVel, yVel;
	private double wind; //wind ammount
	
	private TerraMap map;
	
	private static final double grav = 0.15; //gravity ammount
	
	private static final int expSize = 20; //big explosion!
	
	private static final int size = 6; //size in px
	
	public Projectile(int px, int py, double xV, double yV, TerraMap m, double w) { //constructor
		x = (double)px;
		y = (double)py;
		xVel = xV;
		yVel = yV;
		wind = w;
		
		map = m;
	}
	
	public void move(){ //update it
		yVel -= grav;
		xVel += wind;
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