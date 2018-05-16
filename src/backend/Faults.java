/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.FaultyWord;
import database.Word;
import database.io.FaultListIO;

public class Faults {
	private Map<Integer, Word> allWords;
	
	private Set<Word> usedWords;
	private Map<Integer, FaultyWord> faultyWords;

	private String faultyWordFilePath;
	
	public Faults(Map<Integer, Word> allWords) {
		this.allWords = allWords;
		usedWords = new HashSet<>();
		faultyWords = new HashMap<>();
	}
	
	public void load(String faultyWordFile) throws IOException {
		this.faultyWordFilePath = faultyWordFile;
		List<FaultyWord> words = new FaultListIO().read(new File(faultyWordFile));
		for (FaultyWord word : words) {
			faultyWords.put(word.id, word);
		}
	}
	
	public void save() throws IOException {
		if (faultyWordFilePath == null)
			throw new IllegalStateException("load() must be invoked before save()");
		
		new FaultListIO().write(new File(faultyWordFilePath), new ArrayList<>(faultyWords.values()));
	}
	
	
	public void wordCorrect(Word word) {
		if (!usedWords.contains(word)) {
			usedWords.add(word);
			if (faultyWords.containsKey(word.id)) {
				FaultyWord fword = faultyWords.get(word.id);
				fword.decay = Math.max(0, fword.decay - 1);
				fword.failureStreak = 0;
			}
		}
	}
	
	public void wordIncorrect(Word word, int partialCorrect) {
		if (!usedWords.contains(word)) {
			usedWords.add(word);
			FaultyWord fword = faultyWords.getOrDefault(word.id, new FaultyWord());
			fword.id = word.id;
			fword.decay += getDecayIncreaseFor(fword, partialCorrect);
			fword.failureStreak += 1;
			faultyWords.put(fword.id, fword);
		}
	}
	
	private float getDecayIncreaseFor(FaultyWord word, int partialCorrect) {
		float decayIncrease = 2;
		if (word.failureStreak > 1) {
			decayIncrease = 3;
		}
		
		if (partialCorrect > 0)
			return decayIncrease/2;
		return decayIncrease;
	}
	
	
	public void collectWords(Map<Integer, Word> target, boolean includeZeroDecay) {
		for (Integer id : faultyWords.keySet()) {
			boolean shouldSkip = !includeZeroDecay && faultyWords.get(id).decay <= 0;
			if (!target.containsKey(id) && !shouldSkip) {
				target.put(id, allWords.get(id));
			}
		}
	}
}
