/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package database.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import database.Word;
import database.WordList;

public class WordListIO {
	
	public WordList read(File input) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		WordList list = new WordList();
		list.id = Integer.parseInt(in.readLine());
		list.name = new String(in.readLine().getBytes(), "UTF8");
		list.listPath = input;
		
		while (true) {
			String wordLine = in.readLine();
			if (wordLine == null)
				break;
			wordLine = new String(wordLine.getBytes(), "UTF8");
			
			if (!wordLine.isEmpty()) {
				String[] tokens = wordLine.split("\\s*=\\s*");
				
				String[] wordTokens = tokens[0].split("\\s*;\\s*");
				
				Word word = new Word();
				word.id = Integer.parseInt(wordTokens[0]);
				word.word = wordTokens[1];
				word.translations.addAll(Arrays.asList(tokens[1].split("\\s*;\\s*")));
				
				list.words.add(word);
			}
		}
		
		in.close();
		
		return list;
	}
	
	
	public void write(WordList list) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(list.listPath));

		writer.write(list.id + "\n");
		writer.write(list.name + "\n");
		for (Word word : list.words) {
			String translations = "";
			for (String translation : word.translations) {
				translations += translation + "; ";
			}
			writer.write(word.id + "; " + word.word + " = " + translations.substring(0, translations.length() - 2) + "\n");
		}
		
		writer.close();
	}


	public static void main(String[] args) throws IOException {
		new WordListIO().read(new File("wordlists/list-1.txt"));
	}
}
