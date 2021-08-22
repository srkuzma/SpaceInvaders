package spaceInvaders;

import java.util.ArrayList;


public abstract class Figure {
	
	static class Bullet{
		public Bullet(int x, int y) {
			this.x = x;
			this.y = y;
		}
		int x;
		int y;
	}
	ArrayList<Bullet> bullets = new ArrayList<>();
}
