package spaceInvaders;

import java.awt.Image;

public class HealthBooster {
	int x,y;
	Image i;
	final int BOOST = 20;
	public HealthBooster(Image i, int x, int y) {
		this.i = i;
		this.x = x;
		this.y = y;
	}
}
