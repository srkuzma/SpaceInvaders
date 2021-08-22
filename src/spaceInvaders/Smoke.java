package spaceInvaders;

import java.awt.Image;

import spaceInvaders.Monster.BulletType;

public class Smoke {
	Image i;
	int x,y, w, h;
	double opacity;
	
	public Smoke(Image i, int x, int y, int w, int h, double op) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.opacity = op;
		
	}
}
