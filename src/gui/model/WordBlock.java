package gui.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class WordBlock extends AbstractBlock {
	public static final int HEIGHT = 32;
	
	private String word;
	private Font font;
	private int width;
	
	public WordBlock(String word, int x, int y, boolean rightCentered) {
		super (x, y, rightCentered);
		this.word = word;
	}

	public void render(Graphics2D g2d) {
		if (font == null) {
			font = g2d.getFont().deriveFont(Font.BOLD);
		}
		g2d.setFont(font);
		
		FontMetrics metrics = g2d.getFontMetrics();
		width = metrics.stringWidth(word) + HEIGHT/2;
		

		g2d.setColor(Color.GRAY);
		super.render(g2d);
		g2d.setColor(Color.BLACK);
		g2d.drawString(word, getCenterX() - metrics.stringWidth(word)/2, getCenterY() - metrics.getHeight() / 2 + metrics.getAscent());
	}
	
	@Override
	public int getWidth() {
		return width;
	}
}
