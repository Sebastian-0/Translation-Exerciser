package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordList {
	public int id;
	public String name;
	
	public List<WordListSection> sections;
	
	public WordList() {
		sections = new ArrayList<>();
	}

	public void collectWords(Map<Integer, Word> allWords) {
		for (WordListSection section : sections)
			section.collectWords(allWords);
	}
}
