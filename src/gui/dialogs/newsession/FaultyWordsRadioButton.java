/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.dialogs.newsession;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import config.Table;

public class FaultyWordsRadioButton extends JRadioButton {
	private WordLists wordList;
	private IncludeAllCheckbox includeAllCheckbox;
	
	public FaultyWordsRadioButton(WordLists list, IncludeAllCheckbox includeAllCheckbox) {
		super(Table.get("new_session_faulty_words"));
		this.wordList = list;
		this.includeAllCheckbox = includeAllCheckbox;
		addActionListener(listener);
	}
	
	private ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (isSelected()) {
				wordList.setEnabled(false);
				includeAllCheckbox.setEnabled(true);
			}
		}
	};
}
