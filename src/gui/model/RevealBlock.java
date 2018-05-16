/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class RevealBlock extends AbstractBlock {
	
	public static final int SIZE = 20;
		
	private AnswerBlock connectedWord;
	private boolean isTriggered;
	
	public RevealBlock(AnswerBlock connectedWord, int x, int y, boolean rightCentered) {
		super("R", x, y, rightCentered);
		this.connectedWord = connectedWord;
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
	
	@Override
	protected Color getColor(boolean isHighlighted) {
		if (!isTriggered)
			return Color.YELLOW.darker();
		else
			return Color.YELLOW.darker().darker();
	}
	
	@Override
	protected int getHighlightSizeOffset() {
		return 1;
	}
	
	@Override
	public boolean isDone() {
		return isTriggered;
	}
	
	public boolean markTriggered() {
		if (!isTriggered) {
			isTriggered = true;
			return true;
		}
		return false;
	}
}
