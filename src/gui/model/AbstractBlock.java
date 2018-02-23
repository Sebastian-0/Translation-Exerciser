package gui.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public abstract class AbstractBlock {
	public static final int HEIGHT = 32;
	
	private boolean rightCentered;
	
	private int x;
	private int y;
	
	public AbstractBlock(int x, int y, boolean rightCentered) {
		this.x = x;
		this.y = y;
		this.rightCentered = rightCentered;
	}

	public void render(Graphics2D g2d) {
		g2d.fillRoundRect(getCenterX() - getWidth()/2, getCenterY() - getHeight()/2, getWidth(), getHeight(), 10, 10);
	}
	
	public abstract int getWidth();
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getCenterX() {
		if (rightCentered) {
			return x - getWidth()/2;			
		}
		return x + getWidth()/2;
	}
	
	public int getCenterY() {
		return y + getHeight()/2;
	}
}
