package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class GUI implements KeyListener, PlayerListener {
	
	private int width = 50;
	private int height = 50;
	private int pixelScaler = 20;
	
	private int score = 0;
	
	private boolean gameRunning = true;
	
	private PlayerManager player;
	
	private EnemyDrawer enemyDrawer;
	
	private JFrame frame = new JFrame("2D Game");
	
	private JLabel[] labels = new JLabel[width*height];
	
	private GUI() {
		player = new PlayerManager(width/2, height/2, Color.BLACK);
		enemyDrawer = new EnemyDrawer(width);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(width*pixelScaler, height*pixelScaler));
		frame.setLocationRelativeTo(null);
		frame.setLayout(new GridLayout(width, height));
		frame.setResizable(false);
		frame.addKeyListener(this);
		
		for (int i=0;i<width*height;i++) {
			labels[i] = new JLabel();
			labels[i].setOpaque(true);
			labels[i].setBackground(Color.WHITE);
			frame.add(labels[i]);
		}
		
		initEnemies();
		redrawBoard();
		
		frame.setVisible(true);
	}
	
	private void initEnemies() {
		drawTimer.start();
		enemySpawnTimer.start();
	}
	
	private Timer drawTimer = new Timer(10, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			collisionDetection();
			redrawBoard();
		}
	});
	
	private Timer enemySpawnTimer = new Timer(5000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			spawnEnemy();
		}
	});
	
	private Timer moveUpTimer = new Timer(100, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (player.getY() > 0) {
				player.setY(player.getY() - 1);
				redrawBoard();
			}
		}
	});
	
	private Timer moveDownTimer = new Timer(100, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (player.getY() < height-1) {
				player.setY(player.getY() + 1);
				redrawBoard();
			}
		}
	});
	
	private Timer moveRightTimer = new Timer(100, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (player.getX() < width-1) {
				player.setX(player.getX() + 1);
				redrawBoard();
			}
		}
	});
	
	private Timer moveLeftTimer = new Timer(100, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (player.getX() > 0) {
				player.setX(player.getX() - 1);
				redrawBoard();
			}
		}
	});
	
	private void spawnEnemy() {
		if (enemySpawnTimer.getDelay() > 500) {
			enemySpawnTimer.setDelay(enemySpawnTimer.getDelay()-100);
		}
		Random random = new Random();
		int randomX;
		int randomY;
		do {
			randomX = random.nextInt(width);
			randomY = random.nextInt(height);
		} while (Math.sqrt((Math.pow(randomX-player.getX(), 2)+(Math.pow(randomX-player.getX(), 2)))) < 10d);
		
		EnemyManager enemyManager = new EnemyManager(randomX, randomY, new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)),false);
		enemyManager.addPlayerListener(this);
		enemyManager.setSpeedUpTime(1000);
		enemyManager.startAI();
		enemyDrawer.addEnemyManager(enemyManager);
	}
	
	private void collisionDetection() {
		ArrayList<EnemyManager> enemys = enemyDrawer.getEnemys();
		ArrayList<EnemyManager> remove = new ArrayList<EnemyManager>();
		for (EnemyManager enemy1: enemys) {
			for (EnemyManager enemy2: enemys) {
				if (!enemy1.equals(enemy2)) {
					if (enemy1.getX() == enemy2.getX() && enemy1.getY() == enemy2.getY()) {
						try {
							remove.add(enemy1);
							remove.add(enemy2);
						}catch (Exception e) {}
					}
				}
			}
			if (enemy1.getX() == player.getX() && enemy1.getY() == player.getY()) {
				endGame();
			}
		}
		int addScore = 0;
		for (EnemyManager enemy3: remove) {
			enemyDrawer.removeEnemyManager(enemy3);
			addScore++;
		}
		score += addScore/2;
	}
	
	private void endGame() {
		System.out.println("Ending Game");
		enemySpawnTimer.stop();
		drawTimer.stop();
		endGameTimer.start();
	}
	
	private Timer endGameTimer = new Timer(250,new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			enemyDrawer.removeAllEnemys();
			redrawBoard();
			labels[(height/2)*width+(width/2)-2].setText("GA");
			labels[(height/2)*width+(width/2)-1].setText("ME");
			labels[(height/2)*width+(width/2)+1].setText("OV");
			labels[(height/2)*width+(width/2)+2].setText("ER");
			labels[(height/2+1)*width+(width/2)-1].setText("SC");
			labels[(height/2+1)*width+(width/2)].setText("OR");
			labels[(height/2+1)*width+(width/2)+1].setText("E:");
			labels[(height/2+1)*width+(width/2)+2].setText(score+"");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {}
			gameRunning = false;
			endGameTimer.stop();
		}
	});
	
	private void redrawBoard() {
		for (int i=0;i<width*height;i++) {
			labels[i].setBackground(Color.WHITE);
		}
		labels[width+1].setText(""+score);
		labels[width*player.getY()+player.getX()].setBackground(player.getColor());
		labels = enemyDrawer.drawEnemys(labels);
	}

	public void keyPressed(KeyEvent e) {
		if (gameRunning) {
			if (KeyEvent.VK_W == e.getKeyCode() && !moveUpTimer.isRunning()) {
				moveUpTimer.getActionListeners()[0].actionPerformed(null);
				moveUpTimer.restart();
			}
			if (KeyEvent.VK_S == e.getKeyCode() && !moveDownTimer.isRunning()) {
				moveDownTimer.getActionListeners()[0].actionPerformed(null);
				moveDownTimer.restart();
			}
			if (KeyEvent.VK_A == e.getKeyCode() && !moveLeftTimer.isRunning()) {
				moveLeftTimer.getActionListeners()[0].actionPerformed(null);
				moveLeftTimer.restart();
			}
			if (KeyEvent.VK_D == e.getKeyCode() && !moveRightTimer.isRunning()) {
				moveRightTimer.getActionListeners()[0].actionPerformed(null);
				moveRightTimer.restart();
			}
		} else {
			frame.dispose();
			new GUI();
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		if (gameRunning) {
			if (KeyEvent.VK_W == e.getKeyCode()) {
				moveUpTimer.stop();
			}
			if (KeyEvent.VK_S == e.getKeyCode()) {
				moveDownTimer.stop();
			}
			if (KeyEvent.VK_A == e.getKeyCode()) {
				moveLeftTimer.stop();
			}
			if (KeyEvent.VK_D == e.getKeyCode()) {
				moveRightTimer.stop();
			}
		}
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public static void main(String[] args) {
		new GUI();
	}

	public int[] getPlayerPosition() {
		return new int[] {player.getX(), player.getY()};
	}
}
