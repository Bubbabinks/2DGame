package main;

import java.awt.Color;
import java.util.ArrayList;

public class EnemyManager extends Thread {

	private Color color;
	private int x = 0;
	private int y = 0;
	private int speed = 500;
	private int miniumSpeed = 250;
	
	private int timeAlive = 0;
	private int speedUpTime = 0;
	private int speedUpRate = 10;
	
	private ArrayList<PlayerListener> playerListeners = new ArrayList<PlayerListener>();
	
	public EnemyManager(int x, int y, Color color, boolean startAI) {
		this.x = x;
		this.y = y;
		this.color = color;
		if (startAI) {
			this.start();
		}
	}
	
	public void startAI() {
		if (!this.isAlive()) {
			this.start();
		}
	}
	
	public void addPlayerListener(PlayerListener playerListener) {
		playerListeners.add(playerListener);
	}
	
	public void run() {
		while (true) {
			timeAlive = timeAlive + speed;
			int[] playerCoords = playerListeners.get(0).getPlayerPosition();
			if (x > playerCoords[0]) {
				x--;
			}
			if (x < playerCoords[0]) {
				x++;
			}
			if (y > playerCoords[1]) {
				y--;
			}
			if (y < playerCoords[1]) {
				y++;
			}
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {}
			speedUp();
		}
	}
	
	private void speedUp() {
		if (speedUpTime == 0) {
			return;
		}
		if (timeAlive >= speedUpTime && speed > miniumSpeed) {
			speed = speed - speedUpRate;
			timeAlive = 0;
		}
	}
	
	public void setMiniumSpeed(int miniumSpeed) {
		this.miniumSpeed = miniumSpeed;
	}
	
	public void setSpeedUpRate(int speedUpRate) {
		this.speedUpRate = speedUpRate;
	}
	
	public void setSpeedUpTime(int speedUpTime) {
		this.speedUpTime = speedUpTime;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getMiniumSpeed() {
		return this.miniumSpeed;
	}
	
	public int getSpeedUpTime() {
		return this.speedUpTime;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
	}
	
}
