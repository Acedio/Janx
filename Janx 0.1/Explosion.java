public class Explosion {
	private int x, y, rad;
	
	public Explosion(int xPos, int yPos, int r) {
		x = xPos;
		y = yPos;
		rad = r;
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public int getRad() { return rad; }
}