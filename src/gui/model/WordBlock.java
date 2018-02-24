package gui.model;

import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class WordBlock extends AbstractBlock {
	public static final int HEIGHT = 32;
	
	private int width;
	
	public WordBlock(String word, int x, int y, boolean rightCentered) {
		super (x, y, rightCentered);
		this.word = word;
	}
	
	public WordBlock(WordBlock other) {
		super (other);
		word = other.word;
		width = other.width;
		font = other.font;
	}

	public void render(Graphics2D g2d) {
		FontMetrics metrics = g2d.getFontMetrics();
		width = metrics.stringWidth(word) + HEIGHT/2;
		super.render(g2d);
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public String toString() {
		return word;
	}
}
