package database.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import database.Word;
import database.WordList;

public class WordListIO {
	
	public WordList read(File input) throws IOException {
		WordList list = new WordList();
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		list.id = Integer.parseInt(in.readLine());
		list.name = in.readLine();
		
		while (true) {
			String wordLine = in.readLine();
			if (wordLine == null)
				break;
			
			if (!wordLine.isEmpty()) {
				String[] tokens = wordLine.split("\\s+=\\s+");
				
				String[] wordTokens = tokens[0].split(";");
				
				Word word = new Word();
				word.id = Integer.parseInt(wordTokens[0]);
				word.word = wordTokens[1];
				word.translations.addAll(Arrays.asList(tokens[1].split(";")));
				
				list.words.add(word);
			}
		}
		
		in.close();
		
		return list;
	}
	
	
	public static void main(String[] args) throws IOException {
		new WordListIO().read(new File("wordlists/test.txt"));
	}
}
