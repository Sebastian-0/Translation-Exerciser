package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class RevealBlock extends AbstractBlock {
	
	public static final int SIZE = 20;
		
	private AnswerBlock connectedWord;
	private boolean isHighlighted;
	private boolean isTriggered;
	
	public RevealBlock(AnswerBlock connectedWord, int x, int y, boolean rightCentered) {
		super("R", x, y, rightCentered);
		this.connectedWord = connectedWord;
	}
	
	@Override
	protected void drawBackground(Graphics2D g2d) {
		int offset = 0;
		if (!isHighlighted) {
		} else {
			offset = 1;
		}

		if (!isTriggered)
			g2d.setColor(Color.YELLOW.darker());
		else
			g2d.setColor(Color.YELLOW.darker().darker());
		g2d.fillRoundRect(getCenterX() - getWidth()/2 - offset,
				getCenterY() - getHeight()/2 - offset,
				getWidth() + offset*2,
				getHeight() + offset*2,
				10, 10);
	}
	
	@Override
	protected void drawWord(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 150));
		super.drawWord(g2d);
	}
	
	@Override
	public int getWidth() {
		return SIZE;
	}
	
	@Override
	public int getHeight() {
		return SIZE;
	}
	
	public AnswerBlock getConnectedWord() {
		return connectedWord;
	}

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted && !isTriggered;
	}
	
	public boolean markTriggered() {
		if (!isTriggered) {
			isTriggered = true;
			isHighlighted = false;
			return true;
		}
		return false;
	}
}
