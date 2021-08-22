package spaceInvaders;

import java.awt.Image;

public class Missile {
	Image i;
	int x,y, w, h;
	double rotation  = 0;
	public Missile(Image i, int x, int y, int w, int h) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}
