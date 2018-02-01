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
	
	public Faults(Map<Integer, Word> allWords) {
		this.allWords = allWords;
		usedWords = new HashSet<>();
		faultyWords = new HashMap<>();
	}
	
	public void load(File faultyWordFile) throws IOException {
		List<FaultyWord> words = new FaultListIO().read(faultyWordFile);
		for (FaultyWord word : words) {
			faultyWords.put(word.id, word);
		}
	}
	
	public void save(File faultyWordFile) throws IOException {
		new FaultListIO().write(faultyWordFile, new ArrayList<>(faultyWords.values()));
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
