/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.model;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backend.Session;
import backend.Statistics;
import backend.Session.Result;

public class WordEngine {
	private Session session;

	private List<WordBlock> wordsToTranslate;
	private List<AnswerBlock> targets;
	private List<WordBlock> alternatives;
	private List<RevealBlock> revealButtons;
	
	private WordBlock movingWord;

	private int offsetAlternatives;
	private int offsetTargets;
	
	public WordEngine() {
		wordsToTranslate = new ArrayList<>();
		targets = new ArrayList<>();
		alternatives = new ArrayList<>();
		revealButtons = new ArrayList<>();
	}

	public void start(Session session) {
		this.session = session; 
		wordsToTranslate.clear();
		alternatives.clear();
		targets.clear();
		revealButtons.clear();
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

		x = -widest - offset - AbstractBlock.HEIGHT/2 - RevealBlock.SIZE;
		y = offset;
		for (String word : words) {
			WordBlock newWord = new WordBlock(word, x, y, true);
			AnswerBlock answerBlock = new AnswerBlock(newWord, x + AbstractBlock.HEIGHT/2, y, false);
			RevealBlock revealButton = new RevealBlock(answerBlock, x + AbstractBlock.HEIGHT + widest, y + AbstractBlock.HEIGHT/2 - RevealBlock.SIZE/2, false);
			wordsToTranslate.add(newWord);
			targets.add(answerBlock);
			revealButtons.add(revealButton);
			y += 1.5 * AbstractBlock.HEIGHT;
			answerBlock.setWidth(widest);
		}
	}

	public Statistics stop() {
		return session.end();
	}

	public void saveFaults() throws IOException {
		session.saveFaults();
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
		for (RevealBlock revealButton : revealButtons) {
			revealButton.render(g2d);
		}
		g2d.translate(0, -offsetTargets);
		
		if (movingWord != null) {
			movingWord.render(g2d);
		}
	}
	
	public boolean areAllAnswered() {
		return session.allTranslated();
	}
	

	public boolean mousePressed(MouseEvent e, int viewportX, int viewportY) {
		if (isLeftMouseButton(e)) {
			int x = e.getX() + viewportX;
			int y = e.getY() + viewportY;
			for (WordBlock word : alternatives) {
				if (word.contains(x, y - offsetAlternatives)) {
					movingWord = new WordBlock(word);
					movingWord.setCenter(x, y);
					word.setHighlighted(false);
					return true;
				}
			}
			
			for (RevealBlock revealBlock : revealButtons) {
				if (revealBlock.contains(x, y - offsetTargets) && revealBlock.markTriggered()) {
					AnswerBlock block = revealBlock.getConnectedWord();
					List<String> translations = session.revealTranslations(block.getConnectedWord());
					block.updateWithResult(Result.TranslationsRevealed, translations);
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
			
			for (int i = 0; i < targets.size(); i++) {
				AnswerBlock target = targets.get(i);
				if (target.contains(x, y - offsetTargets) && !target.isDone()) {
					target.setHighlighted(false);
					Result result = session.tryTranslate(target.getConnectedWord(), movingWord.toString());
					target.updateWithResult(result, movingWord.toString());
					
					if (target.isDone()) {
						revealButtons.get(i).markTriggered();
					}
				}
			}
			
			movingWord = null;
			return true;
		}
		return false;
	}
	
	public boolean mouseMoved(MouseEvent e, boolean wasDragged, int viewportX, int viewportY) {
		int x = e.getX() + viewportX;
		int y = e.getY() + viewportY;
		
		updateHighlight(x, y);
		
		if (wasDragged && movingWord != null) {
			movingWord.setCenter(x, y);
			
			return true;
		}
		return false;
	}

	private void updateHighlight(int x, int y) {
		if (movingWord != null) {
			for (AnswerBlock target : targets) {
				if (target.contains(x, y - offsetTargets)) {
					target.setHighlighted(true);
				} else {
					target.setHighlighted(false);
				}
			}
		} else {
			for (RevealBlock revealButton : revealButtons) {
				if (revealButton.contains(x, y - offsetTargets)) {
					revealButton.setHighlighted(true);
				} else {
					revealButton.setHighlighted(false);
				}
			}
			for (WordBlock target : alternatives) {
				if (target.contains(x, y - offsetAlternatives)) {
					target.setHighlighted(true);
				} else {
					target.setHighlighted(false);
				}
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
