import java.awt.*;
import javax.swing.*;

public class TerraMap {
	private int[] map;
	
	private float jag; //steepness of hills
	
	private int width, height;
	
	private static final int DEFAULT_HEIGHT = 200;
	
	public TerraMap(int w, int h, int j, int s) {
		do {
			width = w;
			height = h;
			
			map = new int[width];
			
			for(int x = 0; x < map.length; x++) {
				map[x] = DEFAULT_HEIGHT;
			}
			
			jag = (10/((float)j));
			
			gen(0,width - 1);
			
			smooth(s);
		} while (isTooSteep());
	}
	
	public boolean isTooSteep() {
		int sum = 0;
		for(int x : map) {
			sum += x;
			if(x <= 1) return true;
		}
		sum = (int)(sum/map.length);
		if(sum > 400) return true;
		return false;
	}
	
	public void gen(int start, int end) {
		if(end - start <= 1) {
			return;
		}
		
		int change = (int)((Math.random() * 2 - 1)*((end - start)/jag));
		int mid = (int)((start + end)/2);
		
		map[mid] = (map[start] + map[end])/2;
		
		map[mid] += change;
		
		if(map[mid] < 0) map[mid] = 0;
		
		gen(start, mid);
		gen(mid, end);
	}
	
	public void smooth(int s) {
		for(int i = 0; i < s; i++)
		{
			for(int x = 1; x < map.length - 1; x++) {
				map[x] = (int)((map[x-1]+map[x+1])/2);
			}
		}
	}
	
	public void destroy(Explosion exp) {
		for(int xPos = -exp.getRad(); xPos <= exp.getRad(); xPos++) {
			double yPos = -(Math.sqrt(Math.pow((double)exp.getRad(),2) - Math.pow((double)xPos,2)));
			//System.out.println(yPos);
			if((exp.getX() + xPos >= 0) && (exp.getX() + xPos < width) && (exp.getY() + (int)yPos < map[exp.getX() + xPos])) {
				if(exp.getY() - (int)yPos <= map[exp.getX() + xPos]) {
					//System.out.println(xPos);
					map[exp.getX() + xPos] += (int)yPos*2;
					if(map[exp.getX() + xPos] < 0) map[exp.getX() + xPos] = 0;
				} else {
					map[exp.getX() + xPos] = exp.getY() + (int)yPos;
					if(map[exp.getX() + xPos] < 0) map[exp.getX() + xPos] = 0;
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		//g.setColor(Color.black);
		
		//g.fillRect(0,0,width,height);
		
		g.setColor(Color.green);
		
		for(int x = 0; x < map.length; x++) {
			g.drawLine(x+1,height,x+1,height - map[x]);
		}
	}
	
	public int heightAt(int i) {
		return map[i];
	}
	
	public int getH() {
		return height;
	}
	
	public int getW() {
		return width;
	}
}