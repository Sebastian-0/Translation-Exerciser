package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class AnswerBlock extends AbstractBlock {
	
	public AnswerBlock(int x, int y, boolean rightCentered) {
		super(x, y, rightCentered);
	}

	@Override
	public void render(Graphics2D g2d) {
		g2d.setColor(new Color(45, 180, 255, 200));
		super.render(g2d);
		
		
	}

	@Override
	public int getWidth() {
		return 60;
	}

}
