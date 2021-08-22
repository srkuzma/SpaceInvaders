package spaceInvaders;

import java.awt.Image;

public class Monster extends Figure {
	Image i;
	int x,y, w, h;
	boolean dead = false;
	enum BulletType{
		REGULAR, TRIPLE;
	}
	BulletType bulletType;
	public Monster(Image i, int x, int y, BulletType bt, int w, int h) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		bulletType = bt;
	}
}
