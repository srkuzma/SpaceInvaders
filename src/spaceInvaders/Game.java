package spaceInvaders;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game extends JFrame {
	
	
	JPanelWithBackground background;
	public Game() {
		background = new JPanelWithBackground("blacksky.jpg", "spaceCraft1.png", "slimeMonster.png", "monster2.png", "health.png", "smoke (3).png", "missile (2).png");
		
		setTitle("Space invaders");
		setBounds(100,0,400,400);
		this.getContentPane().add(background);
		setResizable(false);
		
		
		if (0> 1);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new Game();
	}
}
