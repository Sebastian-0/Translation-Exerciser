package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Word;

public class Session {
	public enum Result {
		CorrectTranslationsLeft,
		CorrectWordDone,
		Incorrect,
		AlreadyAssigned
	}
		
	private boolean isTraining;
	
	private Map<String, Word> originalWords;
	private Map<String, Word> wordsLeft;
		
	public Session(boolean isTraining, Map<Integer, Word> wordsToPractise) {
		this.isTraining = isTraining;

		originalWords = new HashMap<>();
		wordsLeft = new HashMap<>();
		for (Word word : wordsToPractise.values()) {
			originalWords.put(word.word, word);
			wordsLeft.put(word.word, new Word(word));
		}
	}
	
	
	public List<String> getWords() {
		List<String> words = new ArrayList<>();
		for (Word word : this.originalWords.values()) {
			words.add(word.word);
		}
		return words;
	}
	
	public List<String> getTranslations() {
		List<String> translations = new ArrayList<>();
		for (Word word : originalWords.values()) {
			translations.addAll(word.translations);
		}
		return translations;
	}
	
	
	public Result tryTranslate(String word, String translation) {
		Word w = wordsLeft.get(word);
		if (w.translations.remove(translation)) {
			if (w.translations.isEmpty())
				return Result.CorrectWordDone;
			return Result.CorrectTranslationsLeft;
		}
		
		if (originalWords.get(word).translations.contains(translation)) {
			return Result.AlreadyAssigned;
		}
		
		if (!isTraining) {
			// TODO Add penalty for error
		}
		return Result.Incorrect;
	}
	
	public boolean allTranslated() {
		for (Word word : wordsLeft.values()) {
			if (!word.translations.isEmpty())
				return false;
		}
		
		return true;
	}
}
