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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.FaultyWord;

public class FaultListIO {
	
	public List<FaultyWord> read(File input) throws IOException {
		List<FaultyWord> list = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(input))) {
			while (true) {
				String wordLine = in.readLine();
				if (wordLine == null)
					break;
				
				if (!wordLine.isEmpty()) {
					String[] wordTokens = wordLine.split(";");
					
					FaultyWord word = new FaultyWord();
					word.id = Integer.parseInt(wordTokens[0]);
					word.failureStreak = Integer.parseInt(wordTokens[1]);
					word.decay = Float.parseFloat(wordTokens[2]);
					
					list.add(word);
				}
			}
			
			in.close();
		} catch (FileNotFoundException e) {
			// Ignore, the file will be created automatically when saved
		}
		
		return list;
	}
	
	public void write(File output, List<FaultyWord> list) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(output));
		
		for (FaultyWord faultyWord : list) {
			out.write(faultyWord.id + ";" + faultyWord.failureStreak + ";" + faultyWord.decay + "\r\n");
		}
		
		out.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		List<FaultyWord> list = new FaultListIO().read(new File("data/faults.txt"));
		new FaultListIO().write(new File("data/temp.txt"), list);
	}
}
