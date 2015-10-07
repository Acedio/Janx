import java.awt.*;
import javax.swing.*;

public class Tank {
	private int x, y, health;
	
	private static final int HEALTH = 100;
	
	private TerraMap map;
	
	private static final int size = 20;
	
	private int ang, pow;
	
	public Tank(int px, TerraMap m) {
		map = m;
		x = px;
		y = map.heightAt(x);
		health = HEALTH;
		ang = 90;
		pow = 50;
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		
		g.fillOval(x - (size/2), map.getH() - (y + (size/2)), size, size);
		
		g.setColor(Color.black);
		
		g.fillRect(x - 15, map.getH() - (y + 20), 30, 6);
		
		g.setColor(Color.yellow);
		
		g.fillRect(x - 14, map.getH() - (y + 19), (int)(28 * (((double)health)/100)), 4);
	}
	
	public void drop() {
		y = map.heightAt(x) + 1;
	}
	
	public boolean isDead() {
		if(health > 0) return false;
		return true;
	}
	
	public Projectile fire() {
		float bX = (float)x;
		float bY = (float)y + (size/2);
		
		double rads = ((double)ang/180)*Math.PI;
		
		float xVel = ((float)pow / 5) * (float)Math.cos(rads);
		float yVel = ((float)pow / 5) * (float)Math.sin(rads);
		
		return new Projectile((int)bX, (int)bY, xVel, yVel, map);
	}
	
	public void checkExplosion(Explosion exp) {
		double distance = Math.sqrt(Math.pow((double)x-(double)exp.getX(), 2) + Math.pow((double)y-(double)exp.getY(), 2));
		if(distance < (double)exp.getRad()) {
			health -= 2*(int)((((double)exp.getRad() - distance)/((double)exp.getRad()))*(double)exp.getRad());
		}
	}
	
	public void changeAngle(int d) {
		ang += d;
		while(ang >= 360) {
			ang -= 360;
		}
		while(ang < 0) {
			ang += 360;
		}
		//System.out.println("Angle: " + ang);
	}
	
	public void changePower(int d) {
		pow += d;
		if(pow > 100) {
			pow = 100;
		} else if(pow < 0) {
			pow = 0;
		}
		//System.out.println("Power: " + pow);
	}
	
	public void drawTurret(Graphics g) {
		double rads = ((double)ang/180)*Math.PI;
		
		g.setColor(Color.red);
		
		g.drawLine(x, map.getH() - y, x + (int)(Math.cos(rads)*size), map.getH() - (y + (int)(Math.sin(rads)*size)));
		
		g.setColor(Color.gray);
		
		g.fillRect(18, 18, 104, 24);
		
		g.setColor(Color.black);
		
		g.fillRect(20,20,100,20);
		
		g.setColor(Color.red);
		
		g.fillRect(20, 20, (int)(100*((float)pow/100)), 20);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getAng(){
		return ang;
	}
}