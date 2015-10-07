import java.awt.*;
import javax.swing.*;

public class Ball{
	private double x, y;
	private double xVel, yVel;
	
	private int width, height;
	
	private static final double grav = 0.15;
	
	public Ball(int px, int py, double xV, double yV, int w, int h)
	{
		x = (double)px;
		y = (double)py;
		xVel = xV;
		yVel = yV;
		
		width = w;
		height = h;
	}
	
	public void move(){		
		yVel -= grav;
		x += xVel;
		y += yVel;
		
		if((int)x >= width){
			x = width - 1;
			xVel = (-xVel*.9);
		} else if((int)x < 0){
			x = 0;
			xVel = (-xVel*.9);
		}
		
		if((int)y < 0){
			y = 0;
			yVel = (-yVel*.9);
			xVel = (xVel*.95);
			if(Math.abs(xVel) < .05) xVel = 0;
			if(yVel < .6) yVel = 0;
		}
	}
	
	public void draw(Graphics g){
		g.setColor(Color.yellow);
		g.fillOval((int)x - 10, (height - ((int)y + 10)), 20, 20);
	}
}