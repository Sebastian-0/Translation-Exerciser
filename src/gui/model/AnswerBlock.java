package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;

import backend.Session.Result;

public class AnswerBlock extends AbstractBlock {
	
	enum State {
		WordsLeft(new Color(45, 180, 255)),
		Done(new Color(65, 160, 30)),
		Incorrect(new Color(200, 35, 35));
		
		Color color;
		State(Color color) {
			this.color = color;
		}
	}
	
	private WordBlock connectedWord;
	private boolean isHighlighted;
	private State state;
	
	public AnswerBlock(WordBlock connectedWord, int x, int y, boolean rightCentered) {
		super(x, y, rightCentered);
		this.connectedWord = connectedWord;
		state = State.WordsLeft;
		word = "?";
	}
	
	@Override
	protected void drawBackground(Graphics2D g2d) {
		int offset = 0;
		if (!isHighlighted || state == State.Done) {
			g2d.setColor(state.color);
		} else {
			g2d.setColor(state.color.darker().darker());
			offset = 3;
		}

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
		return 60;
	}
	
	public String getConnectedWord() {
		return connectedWord.toString();
	}
	
	public boolean isDone() {
		return state == State.Done;
	}

	public void setHighlighted(boolean isHighlighted) {
		this.isHighlighted = isHighlighted;
	}
	
	public void updateWithResult(Result result, String word) {
		this.word = word;
		
		switch (result) {
		case CorrectTranslationsLeft:
			state = State.WordsLeft;
			break;
		case CorrectWordDone:
			state = State.Done;
			break;
		case Incorrect:
			state = State.Incorrect;
			break;

		default:
			break;
		}
	}
}
