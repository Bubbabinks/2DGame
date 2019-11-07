package main;

import java.util.ArrayList;

import javax.swing.JLabel;

public class EnemyDrawer {
	
	private int width;

	private ArrayList<EnemyManager> enemys = new ArrayList<EnemyManager>();
	
	public EnemyDrawer(int width) {
		this.width = width;
	}
	
	public JLabel[] drawEnemys(JLabel[] labels) {
		for (EnemyManager enemyManager: enemys) {
			labels[width*enemyManager.getY()+enemyManager.getX()].setBackground(enemyManager.getColor());
		}
		return labels;
	}
	
	public void addEnemyManager(EnemyManager enemyMananger) {
		enemys.add(enemyMananger);
	}
	
	public void removeEnemyManager(EnemyManager enemyManager) {
		enemyManager.interrupt();
		enemys.remove(enemyManager);
	}
	
	public ArrayList<EnemyManager> getEnemys() {
		return this.enemys;
	}
	
	public void removeAllEnemys() {
		this.enemys.removeAll(this.enemys);
	}
	
}
