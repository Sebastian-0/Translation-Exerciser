/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.dialogs.newsession;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import config.Table;

public class TestRadioButton extends JRadioButton {
	private WordLists wordList;
	private IncludeAllCheckbox includeAllCheckbox;
	
	public TestRadioButton(WordLists list, IncludeAllCheckbox includeAllCheckbox) {
		super(Table.get("new_session_test"));
		this.wordList = list;
		this.includeAllCheckbox = includeAllCheckbox;
		addActionListener(listener);
	}
	
	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				wordList.setEnabled(true);
				includeAllCheckbox.setEnabled(false);
			}
		}
	};
}
