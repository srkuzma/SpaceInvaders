package spaceInvaders;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import javax.imageio.ImageIO;
import javax.swing.*;

import spaceInvaders.Figure.Bullet;
import spaceInvaders.Monster.BulletType;




public class JPanelWithBackground extends JPanel {
	
	Image background;
	Image rocketImage;
	ArrayList<Monster> monsters = new ArrayList<>();
	ArrayList<Smoke> smokes = new ArrayList<>();
	ArrayList<HealthBooster> healthBoosters = new ArrayList<>();
	Rocket rocket = null;
	Missile missile = null;
	Image monsterImage;
	Image monster2Image;
	Image healthImage;
	Image smokeImage;
	Image missileImage;
	Timer timer1, timer2, timer3, timer4;
	
	boolean enabledKeyboard = true;
	int opacityChange = 0;
	
	int BULLET_HEIGHT = 12;
	int BULLET_WIDTH = 3;
	int ROCKET_WIDTH = 50;
	int ROCKET_HEIGHT = 50;
	int MONSTER_WIDTH = 40;
	int MONSTER_HEIGHT = 40;
	int MISSILE_WIDTH = 40;
	int MISSILE_HEIGHT = 40;
	int MONSTER2_WIDTH = 40;
	int MONSTER2_HEIGHT = 40;
	int SMOKE_WIDTH = 40;
	int SMOKE_HEIGHT = 40;
	int PROGRESS_BAR_HEIGHT = 8;
	int HEALTH_HEIGHT;
	int HEALTH_WIDTH;
	int OFFSET = 90;
	
	
	boolean flagLeft = false;
	boolean flagRight = false;
	boolean flagUp = false;
	boolean flagDown = false;
	boolean flagSpace = false;
	
	
	int score = 0;
	int highScore = 0;
	int lives = 5;
	int health = 100;
	Game owner;
	int decreaseBlocked = 0;
	boolean gameOver = false;
	
	public JPanelWithBackground(String bck, String rck, String mns, String mns2, String hlt, String smk, String mis) {
		try {
			//background = new ImageIcon(bck).getImage();
			background = new ImageIcon(getClass().getClassLoader().getResource(bck)).getImage();
			rocketImage = new ImageIcon(getClass().getClassLoader().getResource(rck)).getImage();//.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
			monsterImage = new ImageIcon(getClass().getClassLoader().getResource(mns)).getImage();//.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
			monster2Image = new ImageIcon(getClass().getClassLoader().getResource(mns2)).getImage();//.getScaledInstance(80, 80, Image.SCALE_DEFAULT);
			healthImage = new ImageIcon(getClass().getClassLoader().getResource(hlt)).getImage();
			smokeImage = new ImageIcon(getClass().getClassLoader().getResource(smk)).getImage();
			missileImage = new ImageIcon(getClass().getClassLoader().getResource(mis)).getImage();
			
		    ROCKET_WIDTH = rocketImage.getWidth(null);
			ROCKET_HEIGHT = rocketImage.getHeight(null);
			MONSTER_WIDTH = monsterImage.getWidth(null);
			MONSTER_HEIGHT = monsterImage.getHeight(null);
			MONSTER2_WIDTH = monster2Image.getWidth(null);
			MONSTER2_HEIGHT = monster2Image.getHeight(null);
			HEALTH_HEIGHT = healthImage.getHeight(null);
			HEALTH_WIDTH = healthImage.getWidth(null);
			SMOKE_HEIGHT = smokeImage.getHeight(null);
			SMOKE_WIDTH = smokeImage.getWidth(null);
			MISSILE_HEIGHT = missileImage.getHeight(null);
			MISSILE_WIDTH = missileImage.getWidth(null);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		timer1 = new Timer(40, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//System.out.println(monsters.size());
				for(Monster m:monsters) {
					m.y += 5;
				}
				for(HealthBooster h:healthBoosters) {
					h.y += 5;
				}
				if (missile != null) {
					missile.y += 10;
					missile.x -= (int)(10 * missile.rotation);
					if (missile.x + MISSILE_WIDTH/2 < rocket.x + ROCKET_WIDTH/2 && missile.rotation > Math.toRadians(-35)) {
						missile.rotation -= 0.05;
					}
					else if (missile.x + MISSILE_WIDTH/2 > rocket.x + ROCKET_WIDTH/2 && missile.rotation < Math.toRadians(35)) {
						missile.rotation += 0.05;
					}
					if (missile.y > getHeight())
						missile = null;
					
					
				}
				int i = 0;
				while (i < monsters.size()) {
					if (monsters.get(i).y > getHeight()) {
						monsters.remove(i);
					}
					else {
						i++;
					}
				}
				while (i < healthBoosters.size()) {
					if (healthBoosters.get(i).y > getHeight()) {
						healthBoosters.remove(i);
					}
					else {
						i++;
					}
				}
				
				if (rocket != null) {
					for(Figure.Bullet p: rocket.bullets) {
						p.y-= 6;
					}
					i = 0;
					while (i < rocket.bullets.size()) {
						if (rocket.bullets.get(i).y + BULLET_HEIGHT < 0) {
							rocket.bullets.remove(i);
						}
						else {
							i++;
						}
					}
				}
				
				i = 0;
				while (i < monsters.size()) {
					Monster m = monsters.get(i);
					if (m.bulletType == BulletType.REGULAR) {
						for(Bullet b:m.bullets) {
							b.y += 15;
						}
					}
					else {
						for(Bullet b:m.bullets) {
							b.y += BULLET_HEIGHT;
							if (b.x < m.x + m.w/2) {
								b.x--;
							}
							else if (b.x > m.x + m.w/2) {
								b.x++;
							}
						
								
						}
					}
					
					
					int j = 0;
					while (j < m.bullets.size()) {
						if (m.bullets.get(j).y > getHeight()) {
							m.bullets.remove(j);
						}
						else {
							j++;
						}
					}
					if (m.bullets.size() == 0 && m.dead) {
						monsters.remove(m);
					}
					else {
						i++;
					}
					
				}
				checkCollisions();
				handleEvents();
				repaint();
				
			}

			private void checkCollisions() {
				int i = 0;
				while (i < monsters.size()) {
					boolean flag = false;
					Monster m = monsters.get(i);
					for(Bullet b: rocket.bullets) {
						if (!m.dead && b.y > 10 && b.x + BULLET_WIDTH> m.x && b.x < m.x + m.w && b.y < m.y + m.h && b.y > m.y) {
							m.dead = true;
							rocket.bullets.remove(b);
							smokes.add(new Smoke(smokeImage, m.x, m.y, SMOKE_WIDTH, SMOKE_HEIGHT, 0.8));
							if (!gameOver) {
								if (m.bulletType == BulletType.REGULAR)
									score += 100;
								else {
									score += 250;
								}
							}
								
							if (score > highScore) {
								
								highScore = score;
							}
							
							flag = true;
							break;
						}	
					}
					i++;
					
				}
				for(HealthBooster h:healthBoosters) {
					if (((h.x >= rocket.x && h.x <= rocket.x + ROCKET_WIDTH) || (h.x + HEALTH_WIDTH >= rocket.x && h.x + HEALTH_WIDTH <= rocket.x + ROCKET_WIDTH)) &&
						((h.y >= rocket.y && h.y <= rocket.y + ROCKET_HEIGHT) || (h.y + HEALTH_HEIGHT >= rocket.y && h.y + HEALTH_HEIGHT <= rocket.y + ROCKET_HEIGHT))) {
						if (!gameOver) {
							health = Math.min(health + 15, 100);
							healthBoosters.remove(h);
						}
						break;
					}
					
				}
				if (missile != null && missile.x >= rocket.x && missile.x <= rocket.x + ROCKET_WIDTH && missile.y >= rocket.y && missile.y <= rocket.y + ROCKET_HEIGHT) {
					health = Math.max(0, health - 20);
					if (health <= 0)
						gameOver = true;
					if (!gameOver)
						opacityChange = 10;
					missile = null;
				}
				
				int j = 0;
				while (j < monsters.size()) {
					for(Bullet b: monsters.get(j).bullets) {
						if (b.x >= rocket.x && b.x <= rocket.x + ROCKET_WIDTH && b.y >= rocket.y && b.y <= rocket.y + ROCKET_HEIGHT) {
							Monster m = monsters.get(j);
							m.bullets.remove(b);
							if (!gameOver) {
								health = Math.max(0, health - 10);
								opacityChange = 5;
							}
							
							if (health <= 0)
								gameOver = true;
							if (m.bullets.isEmpty() && m.dead) {
								monsters.remove(j);
								
							}
							break;
						}
					}
					j++;
				}
				if (decreaseBlocked > 0)
					decreaseBlocked--;
				for(Monster m:monsters) {
					if (((m.x >= rocket.x && m.x <= rocket.x + ROCKET_WIDTH) || (m.x + m.w >= rocket.x && m.x + m.w <= rocket.x + ROCKET_WIDTH)) &&
						((m.y >= rocket.y && m.y <= rocket.y + ROCKET_HEIGHT) || (m.y + m.h >= rocket.y && m.y + m.h <= rocket.y + ROCKET_HEIGHT))) {
						if (decreaseBlocked == 0 && !gameOver) {
							health = Math.max(0, health - 10);
							opacityChange = 5;
							decreaseBlocked = 20;
						}
						
						if (health <= 0)
							gameOver = true;
					}
					
				}
			}
		});
		timer2 = new Timer(1500, new ActionListener() {
			
			@Override 
			public void actionPerformed(ActionEvent e) {
				
				double rand = Math.random();
				if (rand < 0.7) {
					monsters.add(new Monster(monsterImage, (int)(Math.random() * (getWidth()-MONSTER_WIDTH)), -OFFSET, BulletType.REGULAR, MONSTER_WIDTH, MONSTER_HEIGHT));
				}
				else if (rand < 0.8) {
					healthBoosters.add(new HealthBooster(healthImage, (int)(Math.random() * (getWidth()-HEALTH_WIDTH)), -OFFSET));
				}
				else if (missile != null || rand < 0.95) {
					monsters.add(new Monster(monster2Image, (int)(Math.random() * (getWidth()-MONSTER2_WIDTH)), -OFFSET, BulletType.TRIPLE, MONSTER2_WIDTH, MONSTER2_HEIGHT));
				}
				else {
					missile = new Missile(missileImage, (int)(Math.random() * (getWidth()-MISSILE_WIDTH)), -OFFSET, MISSILE_WIDTH, MISSILE_HEIGHT);
				}
				timer2.setDelay(Math.max(400, (int)(0.98* timer2.getDelay())));
				repaint();
				
			}
		});
		timer3 = new Timer(800, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(Monster m:monsters) {
					if (m.dead)
						continue;
					if (m.bulletType.equals(BulletType.REGULAR))
						m.bullets.add(new Bullet(m.x + MONSTER_WIDTH/2 - BULLET_WIDTH/2, m.y + MONSTER_HEIGHT - 10));
					else {
						m.bullets.add(new Bullet(m.x + MONSTER2_WIDTH/2 - 4, m.y + MONSTER2_HEIGHT ));
						m.bullets.add(new Bullet(m.x + MONSTER2_WIDTH/2 + 4, m.y + MONSTER2_HEIGHT));
						m.bullets.add(new Bullet(m.x + MONSTER2_WIDTH/2, m.y + MONSTER2_HEIGHT));
					}
				}
				repaint();
				
			}
		});
		//monsters.add(new Monster(monsterImage, (int)(Math.random() * (getWidth()-70))+70 ,0));
		
		setFocusable(true);
		timer1.start();
		timer2.start();
		timer3.start();
		
		addListeners();
	}
	private void handleEvents() {
		if (flagLeft) {
			if (rocket.x > 5)
				rocket.x -= 5;
		}
		if (flagRight) {
			if (rocket.x < getWidth() - ROCKET_WIDTH-5)
				rocket.x += 5;
		}
		if (flagUp) {
			if (rocket.y > 5)
				rocket.y -= 5;
		}
		if (flagDown) {
			if (rocket.y < getHeight() - ROCKET_HEIGHT - PROGRESS_BAR_HEIGHT -5)
				rocket.y += 5;
		}
		if (flagSpace) {
			if (gameOver || !enabledKeyboard) {
				return;
			}
			enabledKeyboard = false;
			timer4 = new Timer(300, (ae) -> {
				enabledKeyboard = true;
				//timer4 = null;
				timer4.stop();
			});
			timer4.start();
			rocket.bullets.add(new Bullet(rocket.x + ROCKET_WIDTH/2 - BULLET_WIDTH, rocket.y - BULLET_HEIGHT));
		}
	}
	private void addListeners() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (gameOver ) return;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					flagLeft = true;
					break;
				case KeyEvent.VK_RIGHT:
					flagRight = true;
					break;
				case KeyEvent.VK_UP:
					flagUp = true;
					break;
				case KeyEvent.VK_DOWN:
					flagDown = true;
					break;
				case KeyEvent.VK_SPACE:
					flagSpace = true;

				default:
					break;
				}
						
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					flagLeft = false;
					break;
				case KeyEvent.VK_RIGHT:
					flagRight = false;
					break;
				case KeyEvent.VK_UP:
					flagUp = false;
					break;
				case KeyEvent.VK_DOWN:
					flagDown = false;
					break;
				case KeyEvent.VK_SPACE:
					flagSpace = false;
					break;
				case KeyEvent.VK_ENTER:
					if (gameOver) {
						restartGame();
					}
					break;
					
				default:
					break;
				}
				
			}
		});
		
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		if (rocket == null)
			rocket = new Rocket(getWidth()/2 - ROCKET_WIDTH/2,getHeight()-ROCKET_HEIGHT - PROGRESS_BAR_HEIGHT - 10);
		Graphics2D g2D = (Graphics2D) g; 
		
		
		g2D.drawImage(background, 0,0, null);
		
		int i = 0; 
		//System.out.println(smokes.size());
		while (i< smokes.size()){
			Smoke s = smokes.get(i);
			g2D.setComposite(AlphaComposite.getInstance(
	                AlphaComposite.SRC_OVER, (float)s.opacity));
			//System.out.println(s.x + " " + s.y);
			g2D.drawImage(s.i, s.x + 2 , s.y - 4, null);
			s.opacity -= 0.1;
			if (s.opacity <= 0) {
				smokes.remove(s);
			}
			else {
				i++;
			}
		}
		
		g2D.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER));
		
		
		
		g2D.setColor(new Color(127, 0, 255));
		g2D.setStroke(new BasicStroke(2));
		for(Monster m:monsters) {
			if (!m.dead)
				g2D.drawImage(m.i, m.x, m.y, null);
			if (m.bulletType == BulletType.REGULAR) {
				for(Figure.Bullet p:m.bullets) {
					g2D.fillRect(p.x, p.y, BULLET_WIDTH, BULLET_HEIGHT);
				}
			}
			else {
				for(Figure.Bullet p:m.bullets) {
					if (p.x < m.x + m.w/2)
						g2D.drawLine(p.x, p.y, p.x - BULLET_WIDTH, p.y + BULLET_HEIGHT);
					else if (p.x > m.x + m.w/2)
						g2D.drawLine(p.x, p.y, p.x + BULLET_WIDTH, p.y + BULLET_HEIGHT);
					else 
						g2D.drawLine(p.x, p.y, p.x, p.y + BULLET_HEIGHT);
				}
			}
				
		
		}
		for(HealthBooster h:healthBoosters) {
			g2D.drawImage(h.i, h.x, h.y, null);
		}
		
		
		if (missile != null) {
			//System.out.println(missile.x + " " + missile.y);
			double locationX = MISSILE_WIDTH / 2;
			double locationY = MISSILE_HEIGHT/ 2;
			AffineTransform tx = new AffineTransform();
			tx.translate(missile.x, missile.y);
			//AffineTransform tx = AffineTransform.getRotateInstance(missile.rotation, locationX, locationY);
			//
			tx.rotate(missile.rotation);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			//g2D.drawImage(op.filter((BufferedImage) missileImage, null), missile.x, missile.y, null);
			g2D.drawImage(missileImage, tx,  null);
		}
		
		if (opacityChange > 0) {
			g2D.setComposite(AlphaComposite.getInstance(
	                AlphaComposite.SRC_OVER, (float)0.5));
			opacityChange--;
		}
		g2D.drawImage(rocketImage, rocket.x, rocket.y, null);
		
		g2D.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER));
		
		g2D.setColor(Color.RED);
		g2D.fillRect(rocket.x, rocket.y + ROCKET_HEIGHT + 4, ROCKET_WIDTH, PROGRESS_BAR_HEIGHT);
		g2D.setColor(Color.GREEN);
		g2D.fillRect(rocket.x , rocket.y + ROCKET_HEIGHT + 4, ROCKET_WIDTH - (100-health) * ROCKET_WIDTH / 100, PROGRESS_BAR_HEIGHT);
		
		
		g2D.setColor(Color.RED);
		for(Figure.Bullet p:rocket.bullets) {
			g2D.fillRect(p.x, p.y, BULLET_WIDTH, BULLET_HEIGHT);
		}
		g2D.setColor(Color.WHITE);
		g2D.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		g2D.drawString("Score: " + score, 10, 20);
		g2D.drawString("Highscore: " + highScore, 10, 40);
		
		if (gameOver) {
			g2D.setFont(new Font("Comic Sans MS", Font.BOLD, 30));
			FontMetrics metrics = g.getFontMetrics(g.getFont());
			String toDraw = "Game Over";
			g2D.drawString(toDraw, getWidth()/2 - metrics.stringWidth(toDraw)/2, getHeight()/2 - metrics.getAscent());
			toDraw = "Your score is: " + score;
			g2D.drawString(toDraw, getWidth()/2 - metrics.stringWidth(toDraw)/2, getHeight()/2 );
			toDraw = "Press Enter";
			g2D.drawString(toDraw, getWidth()/2 - metrics.stringWidth(toDraw)/2, getHeight()/2  + metrics.getAscent());
		}
		
		setOpaque(false);
	}
	
	private void restartGame() {
		score = 0;
		monsters = new ArrayList<>();
		rocket = null;
		missile = null;
		health = 100;
		timer2.setDelay(1500);
		gameOver = false;
		repaint();
	}
	
	
}
