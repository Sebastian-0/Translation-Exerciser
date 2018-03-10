package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backend.Session;
import backend.Session.Result;

public class WordEngine {
	private Session session;

	private List<WordBlock> wordsToTranslate;
	private List<AnswerBlock> targets;
	private List<WordBlock> alternatives;
	
	private WordBlock movingWord;

	private int offsetAlternatives;
	private int offsetTargets;
	
	public WordEngine() {
		wordsToTranslate = new ArrayList<>();
		targets = new ArrayList<>();
		alternatives = new ArrayList<>();
	}

	public void start(Session session) {
		this.session = session; 
		wordsToTranslate.clear();
		alternatives.clear();
		targets.clear();
		movingWord = null;
		offsetAlternatives = 0;
		offsetTargets = 0;
		
		List<String> words = session.getWords();
		List<String> translations = session.getTranslations();
				
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
		Collections.shuffle(words);
		translations.sort(collator);
		
		final int offset = 50;
				
		int y = offset;
		int x = offset;
		int widest = 0;
		for (String word : translations) {
			WordBlock wordBlock = new WordBlock(word, x, y, false);
			alternatives.add(wordBlock);
			y += 1.5 * AbstractBlock.HEIGHT;
			widest = Math.max(widest, wordBlock.getWidth());
		}

		x = -widest - offset - AbstractBlock.HEIGHT/2;
		y = offset;
		for (String word : words) {
			WordBlock newWord = new WordBlock(word, x, y, true);
			AnswerBlock answerBlock = new AnswerBlock(newWord, x + AbstractBlock.HEIGHT/2, y, false);
			wordsToTranslate.add(newWord);
			targets.add(answerBlock);
			y += 1.5 * AbstractBlock.HEIGHT;
			answerBlock.setWidth(widest);
		}
	}

	public void render(Graphics2D g2d) {
		g2d.translate(0, offsetAlternatives);
		for (WordBlock wordBlock : alternatives) {
			wordBlock.render(g2d);
		}

		g2d.translate(0, offsetTargets - offsetAlternatives);
		for (WordBlock wordBlock : wordsToTranslate) {
			wordBlock.render(g2d);
		}
		for (AnswerBlock answerBlock : targets) {
			answerBlock.render(g2d);
		}
		g2d.translate(0, -offsetTargets);
		
		if (movingWord != null) {
			movingWord.render(g2d);
		}
	}
	

	public boolean mousePressed(MouseEvent e, int viewportX, int viewportY) {
		if (isLeftMouseButton(e)) {
			for (WordBlock word : alternatives) {
				int x = e.getX() + viewportX;
				int y = e.getY() + viewportY;
				if (word.contains(x, y - offsetAlternatives)) {
					movingWord = new WordBlock(word);
					movingWord.setCenter(x, y);
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean mouseReleased(MouseEvent e, int viewportX, int viewportY) {
		if (isLeftMouseButton(e) && movingWord != null) {
			int x = e.getX() + viewportX;
			int y = e.getY() + viewportY;
			for (AnswerBlock target : targets) {
				if (target.contains(x, y - offsetTargets) && !target.isDone()) {
					target.setHighlighted(false);
					Result result = session.tryTranslate(target.getConnectedWord(), movingWord.toString());
					target.updateWithResult(result, movingWord.toString());
				}
			}
			
			movingWord = null;
			return true;
		}
		return false;
	}
	
	public boolean mouseMoved(MouseEvent e, boolean wasDragged, int viewportX, int viewportY) {
		if (wasDragged && movingWord != null) {
			int x = e.getX() + viewportX;
			int y = e.getY() + viewportY;
			movingWord.setCenter(x, y);
			
			updateHighlight(x, y);
			
			return true;
		}
		return false;
	}

	private void updateHighlight(int x, int y) {
		for (AnswerBlock target : targets) {
			if (target.contains(x, y - offsetTargets)) {
				target.setHighlighted(true);
			} else {
				target.setHighlighted(false);
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e, float scale, int viewportX, int viewportY) {
		int x = e.getX() + viewportX;
		int y = e.getY() + viewportY;
		
		final int scrollStep = (int) (30 * 1/scale);
		if (x > 0) {
			offsetAlternatives -= e.getWheelRotation()*scrollStep;
		} else {
			offsetTargets -= e.getWheelRotation()*scrollStep;
		}

		// TODO Stop scrolling when all are visible
//		offsetAlternatives = Math.min(0, offsetAlternatives);
//		offsetTargets = Math.min(0, offsetTargets);
		if (movingWord != null)
			updateHighlight(x, y);
	}

	
	private boolean isLeftMouseButton(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON1;
	}

	private boolean isMiddleMouseButton(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON2;
	}

	private boolean isRightMouseButton(MouseEvent e) {
		return e.getButton() == MouseEvent.BUTTON3;
	}
}
