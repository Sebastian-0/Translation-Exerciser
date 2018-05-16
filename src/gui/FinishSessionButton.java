/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */


package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import config.Table;
import gui.control.ProgramUI;

public class FinishSessionButton extends JButton {
	private ProgramUI ui;

	public FinishSessionButton(ProgramUI ui) {
		super(Table.get("bottom_finish_session"));
		this.ui = ui;
		addActionListener(actionListener);
		setEnabled(false);
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ui.stopExercising();
		}
	};
}
