package database;

import java.util.ArrayList;
import java.util.List;

public class Word {
	public int id;
	public String word;
	public List<String> translations;
	
	public Word() {
		translations = new ArrayList<>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Word) {
			Word other = (Word) obj;
			return id == other.id && word.equals(other.word);
		}
		
		return false;
	}
}
