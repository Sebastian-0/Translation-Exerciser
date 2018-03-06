package database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordList {
	public int id;
	public String name;
	
	public File listPath;

	public List<Word> words;
	
	public WordList() {
		words = new ArrayList<>();
	}

	public void collectWords(Map<Integer, Word> target) {
		for (Word word : words) {
			if (target.containsKey(word.id)) {
				if (!word.equals(target.get(word.id)))
					throw new IllegalStateException("There are two words with the same ID (" + word.id + "): " +
						word.word + ", " + target.get(word.id).word);
			} else {
				target.put(word.id, word);
			}
		}
	}
}
