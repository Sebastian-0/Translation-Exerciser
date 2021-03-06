/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package backend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.Word;

public class Session {
	public enum Result {
		CorrectTranslationsLeft,
		CorrectWordDone,
		Incorrect,
		AlreadyAssigned,
		TranslationsRevealed
	}
		
	private Faults faults;

	private boolean isTraining;
	
	private Map<String, Word> originalWords;
	private Map<String, Word> wordsLeft;
	
	private Statistics stats;
	private Set<Word> incorrect;
	private Set<Word> revealed;
	
	public Session(Faults faults, boolean isTraining, Map<Integer, Word> wordsToPractise, int[] wordListIds, boolean includeZeroDecay) {
		this.faults = faults;
		this.isTraining = isTraining;

		stats = new Statistics();
		stats.wordListIds = wordListIds;
		stats.isTraining = isTraining;
		stats.includeZeroDecay = includeZeroDecay;
		
		incorrect = new HashSet<>();
		revealed = new HashSet<>();
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
			stats.translationsCorrect += 1;
			if (w.translations.isEmpty()) {
				faults.wordCorrect(w);
				return Result.CorrectWordDone;
			}
			return Result.CorrectTranslationsLeft;
		}
		
		if (originalWords.get(word).translations.contains(translation)) {
			return Result.AlreadyAssigned;
		}
		
		if (!isTraining) {
			faults.wordIncorrect(w, originalWords.get(word).translations.size() - w.translations.size());
		}
		stats.translationsIncorrect += 1;
		incorrect.add(w);
		return Result.Incorrect;
	}


	public List<String> revealTranslations(String word) {
		Word w = originalWords.get(word);
		
		if (wordsLeft.containsKey(word)) {
			Word wordLeft = wordsLeft.remove(word);			
			revealed.add(wordLeft);
		}
		
		return w.translations;
	}
	
	public boolean allTranslated() {
		for (Word word : wordsLeft.values()) {
			if (!word.translations.isEmpty())
				return false;
		}
		
		return true;
	}
	
	public Statistics end() {
		stats.amountOfWords = originalWords.size();
		for (Word word : wordsLeft.values()) {
			int maximumTranslations = originalWords.get(word.word).translations.size();
			int missedTranslations = word.translations.size();
			if (missedTranslations > 0) {
				if (missedTranslations == maximumTranslations) {
					stats.wordsIncorrect += 1;
					if (!incorrect.contains(word))
						stats.wordsNoAnswer += 1;
				} else {
					stats.wordsPartiallyCorrect += 1;
				}
				
				if (!isTraining) {
					faults.wordIncorrect(word, maximumTranslations - missedTranslations);
				}
			} else {
				if (incorrect.contains(word))
					stats.wordsCorrect += 1;
				else
					stats.wordsCorrectNoErrors += 1;
			}
			
			stats.translationsNotAnswered += missedTranslations;
			stats.translationsIncorrect += missedTranslations;
		}
		
		for (Word word : revealed) {
			int maximumTranslations = originalWords.get(word.word).translations.size();
			int missedTranslations = word.translations.size();
			if (!isTraining) {
				faults.wordIncorrect(word, maximumTranslations - missedTranslations);
			}
			
			stats.translationsRevealed += missedTranslations;
			stats.translationsIncorrect += missedTranslations;
			stats.wordsIncorrect += 1;
		}
		
		stats.amountOfTranslations = stats.translationsCorrect + stats.translationsIncorrect;
		
		stats.timestamp = System.currentTimeMillis();
		return stats;
	}
	
	public void saveFaults() throws IOException {
		faults.save();
	}
}
