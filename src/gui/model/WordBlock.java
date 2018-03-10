package gui.model;

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
}
