import java.awt.*;
import javax.swing.*;

public class Ball{
	private double x, y;
	private double xVel, yVel;
	
	private int width, height;
	
	private static final double grav = 0.05;
	
	public Ball(int px, int py, double xV, double yV, int width, int height)
	{
		x = (double)px;
		y = (double)py;
		xVel = xV;
		yVel = yV;
	}
	
	public void move(){		
		yVel -= grav;
		x += xVel;
		y += yVel;
		
		if((int)x >= width){
			x = width - 1;
			xVel = -xVel;
		} else if((int)x < 0){
			x = 0;
			xVel = -xVel;
		}
		
		if((int)y < 0){
			y = 0;
			yVel = -yVel;
		}
	}
	
	public void draw(Graphics g){
		g.setColor(Color.red);
		g.fillOval((int)x - 10, height - ((int)y + 10), 20, 20);
	}
}