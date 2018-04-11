package gui.model;

import java.awt.Color;

public class WordBlock extends AbstractBlock {	
	public WordBlock(String word, int x, int y, boolean rightCentered) {
		super (word, x, y, rightCentered);
	}
	
	public WordBlock(WordBlock other) {
		super (other);
	}
	
	@Override
	public String toString() {
		return word;
	}
	
	@Override
	protected Color getColor(boolean isHighlighted) {
		Color color = super.getColor(isHighlighted);
		if (isHighlighted)
			return color.darker();
		else
			return color;
	}
}
