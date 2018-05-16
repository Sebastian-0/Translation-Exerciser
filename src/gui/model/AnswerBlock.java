/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import backend.Session.Result;

public class AnswerBlock extends AbstractBlock {
	
	enum State {
		WordsLeft(new Color(45, 180, 255)),
		Done(new Color(65, 160, 30)),
		Incorrect(new Color(200, 35, 35)),
		Revealed(new Color(240, 160, 10));
		
		Color color;
		State(Color color) {
			this.color = color;
		}
	}
	
	private WordBlock connectedWord;
	private State state;
	
	public AnswerBlock(WordBlock connectedWord, int x, int y, boolean rightCentered) {
		super("?", x, y, rightCentered);
		this.connectedWord = connectedWord;
		state = State.WordsLeft;
	}
	
	@Override
	protected void drawWord(Graphics2D g2d) {
		g2d.setColor(new Color(0, 0, 0, 150));
		super.drawWord(g2d);
	}

	public String getConnectedWord() {
		return connectedWord.toString();
	}
	
	@Override
	public boolean isDone() {
		return state == State.Done || state == State.Revealed;
	}
	
	@Override
	protected Color getColor(boolean isHighlighted) {
		if (isHighlighted && !isDone()) {
			return state.color.darker().darker();
		} else {
			return state.color;
		}
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void updateWithResult(Result result, List<String> words) { // TODO AnswerBlock; Must store and show all translations in some way
		updateWithResult(result, words.get(0));
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
		case TranslationsRevealed:
			state = State.Revealed;
			break;

		default:
			break;
		}
	}
}
