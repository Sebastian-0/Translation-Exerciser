package gui.model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
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
		
		List<String> words = session.getWords();
		List<String> translations = session.getTranslations();
				
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
		Collections.shuffle(words);
		translations.sort(collator);
				
		int y = 50;
		int x = 350;
		for (String word : translations) {
			WordBlock wordBlock = new WordBlock(word, x, y, false);
			alternatives.add(wordBlock);
			y += 1.5 * AbstractBlock.HEIGHT;
		}

		x = 100;
		y = 50;
		for (String word : words) {
			WordBlock newWord = new WordBlock(word, x, y, true);
			wordsToTranslate.add(newWord);
			targets.add(new AnswerBlock(newWord, x + AbstractBlock.HEIGHT/2, y, false));
			y += 1.5 * AbstractBlock.HEIGHT;
		}
	}

	public void render(Graphics2D g2d) {
		for (WordBlock wordBlock : wordsToTranslate) {
			wordBlock.render(g2d);
		}
		int widest = 0;
		for (WordBlock wordBlock : alternatives) {
			widest = Math.max(widest, wordBlock.getWidth());
			wordBlock.render(g2d);
		}
		for (AnswerBlock answerBlock : targets) {
			answerBlock.setWidth(widest);
			answerBlock.render(g2d);
		}
		if (movingWord != null) {
			movingWord.render(g2d);
		}
	}
	

	public boolean mousePressed(MouseEvent e, int viewportX, int viewportY) {
		if (isLeftMouseButton(e)) {
			for (WordBlock word : alternatives) {
				int x = e.getX() + viewportX;
				int y = e.getY() + viewportY;
				if (word.contains(x, y)) {
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
				if (target.contains(x, y) && !target.isDone()) {
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
			
			for (AnswerBlock target : targets) {
				if (target.contains(x, y)) {
					target.setHighlighted(true);
				} else {
					target.setHighlighted(false);
				}
			}
			
			return true;
		}
		return false;
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
