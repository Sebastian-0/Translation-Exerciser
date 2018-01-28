package database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordListSection {
	public String name;
	public List<Word> words;
	
	public WordListSection() {
		words = new ArrayList<>();
	}

	public void collectWords(Map<Integer, Word> allWords) {
		for (Word word : words) {
			if (allWords.containsKey(word.id)) {
				if (!word.equals(allWords.get(word.id)))
					throw new IllegalStateException("There are two words with the same ID (" + word.id + "): " +
						word.word + ", " + allWords.get(word.id).word);
			} else {
				allWords.put(word.id, word);
			}
		}
	}
}
