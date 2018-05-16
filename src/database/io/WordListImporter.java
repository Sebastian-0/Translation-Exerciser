/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package database.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import database.Word;
import database.WordList;

public class WordListImporter {
	
	public WordList read(File input) throws IOException {
		WordList list = new WordList();
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		list.name = input.getName().substring(0, input.getName().lastIndexOf('.'));
		
		while (true) {
			String wordLine = in.readLine();
			if (wordLine == null)
				break;
			wordLine = new String(wordLine.getBytes(), "UTF8");
			
			if (!wordLine.isEmpty()) {
				String[] tokens = wordLine.split("\\s*=\\s*");
				
				Word word = new Word();
				word.word = tokens[0];
				word.translations.addAll(Arrays.asList(tokens[1].split("\\s*;\\s*")));
				
				list.words.add(word);
			}
		}
		
		in.close();
		
		return list;
	}
}
