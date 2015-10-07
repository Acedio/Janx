import java.awt.*;
import javax.swing.*;

class DrawPanel extends JPanel {
	private TerraMap map;
	private Tank[] tanks;
	private int turn;
	
	public static final int PLAYERS = 30;
	
	public static final int FRAMEX = 800;
	public static final int FRAMEY = 600;
	
	public DrawPanel() {
		this(new TerraMap(0,0));
	}
	
	public DrawPanel(TerraMap m) {
		setPreferredSize( new Dimension(FRAMEX, FRAMEY));
		map = m;
		turn = 0;
		tanks = new Tank[PLAYERS];
		for(int i = 0; i < tanks.length; i++) {
			tanks[i] = new Tank("Player " + (i + 1), (DrawPanel.FRAMEX/(tanks.length + 1))*(i + 1), map);
		}
	}
	
	public void remake(TerraMap m) {
		map = m;
		turn = 0;
		tanks = new Tank[PLAYERS];
		for(int i = 0; i < tanks.length; i++) {
			tanks[i] = new Tank("Player " + (i + 1), (DrawPanel.FRAMEX/(tanks.length + 1))*(i + 1), map);
		}
	}
	
	public void paintComponent(Graphics g) {
		map.draw(g);
		for(int i = 0; i < tanks.length; i++) {
			if(!tanks[i].isDead()) {
				tanks[i].draw(g);
				if(!tanks[i].projectiles.isEmpty()) {
					Explosion ex = tanks[i].drawProjectiles(g);
					if(ex != null) {
						for(int j = 0; j < tanks.length; j++) {
							if(!tanks[j].isDead()) {
								tanks[j].checkExplosion(ex);
								tanks[j].drop();
							}
						}
					}
				}
			}
		}
		repaint();
	}
	
	public void destroy(int ang, int pow) {
		//System.out.println(turn + 1);
		tanks[turn].fire(ang, pow);
		while(tanks[turn++].isDead()) {}
		if(turn >= tanks.length) turn = 0;
	}
}