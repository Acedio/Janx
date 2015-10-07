import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Tank {
	private int x, y;
	private String name;
	private TerraMap map;
	private int health;
	
	public ArrayList<Projectile> projectiles;
	
	public Tank(String n, int xPos, TerraMap m) {
		name = n;
		map = m;
		x = xPos;
		y = map.map[x] + 1;
		health = 100;
		projectiles = new ArrayList<Projectile>();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.red);
		
		g.fillOval(x - 10, DrawPanel.FRAMEY - (y + 10), 20, 20);
		
		g.setColor(Color.black);
		
		g.fillRect(x - 15, DrawPanel.FRAMEY - (y + 20), 30, 6);
		
		g.setColor(Color.yellow);
		
		g.fillRect(x - 14, DrawPanel.FRAMEY - (y + 19), (int)(28 * (((double)health)/100)), 4);
	}
	
	public void fire(int angle, int power) {
		float bX = (float)x;
		float bY = (float)y + 20;
		
		if(angle > 360 || angle < 0 || power > 100 || power < 0 || health <= 0) {
			return; //new Explosion(0,0,0);
		}
		
		double rads = ((double)angle/180)*Math.PI;
		
		float xVel = ((float)power / 5) * (float)Math.cos(rads);
		float yVel = ((float)power / 5) * (float)Math.sin(rads);
		
		//System.out.println("Next.");
		
		projectiles.add(new Projectile (bX, bY, xVel, yVel));
		
		/*while(bX >= 0 && bX < DrawPanel.FRAMEX && (int)bY > map.map[(int)bX]) {
			//System.out.println("bX = " + bX + " bY = " + bY + " y at map[bX] = " + map.map[(int)bX]);
			yVel -= (0.5/10);
			bX += (xVel/10);
			bY += (yVel/10);
		}
		map.destroy((int)bX,(int)bY,20);
		return new Explosion((int)bX,(int)bY,20);*/
	}
	
	public Explosion drawProjectiles(Graphics g) {
		//System.out.println("drawProjectiles");
		for(int i = 0; i < projectiles.size(); i++)
		{
			g.fillOval((int)projectiles.get(i).getX() - 2, DrawPanel.FRAMEY - (int)(projectiles.get(i).getY() + 2), 4, 4);
			Explosion ex = projectiles.get(i).update();
			if(ex != null) {
				map.destroy(ex.getX(), ex.getY(), ex.getRad());
				projectiles.remove(i);
			}
			return ex;
		}
		return null;
	}
	
	public void checkExplosion(Explosion ex) {
		double distance = Math.sqrt(Math.pow((double)x-(double)ex.getX(), 2) + Math.pow((double)y-(double)ex.getY(), 2));
		//System.out.println(distance);
		if(distance < (double)ex.getRad()) {
			health -= (int)((((double)ex.getRad() - distance)/((double)ex.getRad()))*(double)ex.getRad());
		}
	}
	
	public void drop() {
		y = map.map[x] + 1;
	}
	
	public boolean isDead() {
		if(health > 0) return false;
		return true;
	}
	
	public class Projectile {
		private float bX, bY, yVel, xVel;
		
		public Projectile(float x, float y, float xV, float yV) {
			bX = x;
			bY = y;
			xVel = xV;
			yVel = yV;
		}
		
		public Explosion update() {
			yVel -= 0.5;
			bX += xVel;
			bY += yVel;
			if((int)bX >= DrawPanel.FRAMEX || (int)bX < 0) {
				return new Explosion(0,0,0);
			}else if((int)bY < map.map[(int)bX]) {
				return new Explosion((int)bX,(int)bY,20);
			}
			return null;
		}
		
		public float getX() {
			return bX;
		}
		
		public float getY() {
			return bY;
		}
		
		public float getXVel() {
			return xVel;
		}
		
		public float getYVel() {
			return yVel;
		}
	}
}