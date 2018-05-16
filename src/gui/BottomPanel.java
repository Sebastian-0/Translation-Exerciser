/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui;

import javax.swing.JPanel;

import gui.control.ProgramUI;

public class BottomPanel extends JPanel {
	private FinishSessionButton finishSessionButton;

	public BottomPanel(ProgramUI ui) {
		finishSessionButton = new FinishSessionButton(ui);
		add(finishSessionButton);
	}

	public void sessionStarted() {
		finishSessionButton.setEnabled(true);
	}

	public void sessionEnded() {
		finishSessionButton.setEnabled(false);
	}
}
