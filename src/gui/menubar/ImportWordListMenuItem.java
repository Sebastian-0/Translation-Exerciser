/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */


package gui.menubar;

import java.awt.Component;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import config.Config;
import config.Table;
import database.Word;
import gui.control.ProgramUI;

public class ImportWordListMenuItem extends AbstractMenuItem {
	private JFileChooser fileChooser;

	public ImportWordListMenuItem(ProgramUI window) {
		super(Table.get("menubar_import_list"), window);

		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
				Table.get("file_description_list_source") + " (" + extensionsToString() + ")",
				Config.WORDLIST_SOURCE_EXTENSIONS));
		fileChooser.setAcceptAllFileFilterUsed(true);
		fileChooser.setMultiSelectionEnabled(true);
	}
	
	private String extensionsToString() {
		String result = "";
		for (String ext : Config.WORDLIST_SOURCE_EXTENSIONS) {
			result += "*." + ext + ", ";
		}
		return result.substring(0, result.length() - 2);
	}

	@Override
	protected void doAction(ProgramUI program) {
		int result = fileChooser.showOpenDialog((Component) program);
		if (result == JFileChooser.APPROVE_OPTION) {
			File[] targets = fileChooser.getSelectedFiles();
			
			Set<Word> existingWords = new HashSet<>();
			boolean targetNotFile = false;
			for (File target : targets) {
				if (target.isFile()) {
					try {
						List<Word> words = program.getWordExerciser().importList(target);
						existingWords.addAll(words);
					} catch (Throwable e) {
						JOptionPane.showMessageDialog((Component) program,
								Table.get("import_failed_bad_format"),
								Table.get("import_failed_title"),
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					targetNotFile = true;
				}
			}
			
			if (targetNotFile) {
				JOptionPane.showMessageDialog((Component) program,
						Table.get("import_warning_folder"),
						Table.get("import_warning_title"),
						JOptionPane.WARNING_MESSAGE);
			}
			if (!existingWords.isEmpty()) { // TODO Replace message with a proper dialog! We want to change the name too!
				String wordText = "";
				for (Word word : existingWords) {
					wordText += word.word + "\n";
				}
				JOptionPane.showMessageDialog((Component) program,
						"The following words already existed and were merged:\n" + wordText.trim(),
						Table.get("import_warning_title"),
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
