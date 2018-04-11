package gui.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import util.FontUtils;

public abstract class AbstractBlock {
	public static final int HEIGHT = 32;
	
	protected String word;
	protected Font font;
	
	private boolean rightCentered;
	
	private int x;
	private int y;
	
	protected int width;

	private boolean isHighlighted;
	
	public AbstractBlock(String text, int x, int y, boolean rightCentered) {
		this.word = text;
		this.x = x;
		this.y = y;
		this.rightCentered = rightCentered;
		font = FontUtils.getDefaultFont().deriveFont(Font.BOLD);
		width = FontUtils.getFontMetrics(font).stringWidth(word) + HEIGHT/2;
	}
	
	public AbstractBlock(AbstractBlock other) {
		word = other.word;
		font = other.font;
		x = other.x;
		y = other.y;
		width = other.width;
		rightCentered = other.rightCentered;
	}

	public void render(Graphics2D g2d) {
		g2d.setFont(font);
		
		g2d.setColor(getColor(isHighlighted));
		drawBackground(g2d);
		g2d.setColor(Color.BLACK);
		drawWord(g2d);
	}

	protected void drawBackground(Graphics2D g2d) {
		int offset = 0;
		if (isHighlighted && !isDone()) {
			offset = getHighlightSizeOffset();
		}

		g2d.fillRoundRect(getCenterX() - getWidth()/2 - offset,
				getCenterY() - getHeight()/2 - offset,
				getWidth() + offset*2,
				getHeight() + offset*2,
				10, 10);
	}
	
	protected void drawWord(Graphics2D g2d) {
		FontMetrics metrics = g2d.getFontMetrics();
		g2d.drawString(word, getCenterX() - metrics.stringWidth(word)/2, getCenterY() - metrics.getHeight() / 2 + metrics.getAscent());
	}

	public int getWidth() {
		return width;
	}
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

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}

	public boolean contains(int x, int y) {
		int x1 = getCenterX() - getWidth()/2;
		int y1 = getCenterY() - getHeight()/2;
		int x2 = getCenterX() + getWidth()/2;
		int y2 = getCenterY() + getHeight()/2;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
	
	public boolean isDone() {
		return false;
	}
	
	protected Color getColor(boolean isHighlighted) {
		return Color.GRAY;
	}
	
	protected int getHighlightSizeOffset() {
		return 3;
	}
}
