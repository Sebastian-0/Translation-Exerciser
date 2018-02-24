package gui.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public abstract class AbstractBlock {
	public static final int HEIGHT = 32;
	
	protected String word;
	protected Font font;
	
	private boolean rightCentered;
	
	private int x;
	private int y;
	
	public AbstractBlock(int x, int y, boolean rightCentered) {
		this.x = x;
		this.y = y;
		this.rightCentered = rightCentered;
	}
	
	public AbstractBlock(AbstractBlock other) {
		x = other.x;
		y = other.y;
		rightCentered = other.rightCentered;
	}

	public void render(Graphics2D g2d) {
		if (font == null) {
			font = g2d.getFont().deriveFont(Font.BOLD);
		}
		g2d.setFont(font);
		
		g2d.setColor(Color.GRAY);
		drawBackground(g2d);
		g2d.setColor(Color.BLACK);
		drawWord(g2d);
	}

	protected void drawBackground(Graphics2D g2d) {
		g2d.fillRoundRect(getCenterX() - getWidth()/2, getCenterY() - getHeight()/2, getWidth(), getHeight(), 10, 10);
	}
	
	protected void drawWord(Graphics2D g2d) {
		FontMetrics metrics = g2d.getFontMetrics();
		g2d.drawString(word, getCenterX() - metrics.stringWidth(word)/2, getCenterY() - metrics.getHeight() / 2 + metrics.getAscent());
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

	public void setCenter(int x, int y) {
		this.x += x - getCenterX();
		this.y += y - getCenterY();
	}

	public boolean contains(int x, int y) {
		int x1 = getCenterX() - getWidth()/2;
		int y1 = getCenterY() - getHeight()/2;
		int x2 = getCenterX() + getWidth()/2;
		int y2 = getCenterY() + getHeight()/2;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
}
