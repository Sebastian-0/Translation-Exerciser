/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package backend;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Statistics {
	public int[] wordListIds;
	public boolean isTraining;
	public boolean includeZeroDecay;
	
	public int amountOfWords;
	public int amountOfTranslations;
	
	/* Words with no answer attempts */
	public int wordsNoAnswer;
	/* Words with no correct answer (includes words with "no answer") */
	public int wordsIncorrect;
	/* Words with at least one correct answer and at least one translation left */
	public int wordsPartiallyCorrect;
	/* Words that were correct but there were error(s) along the way */
	public int wordsCorrect;
	/* Words that were correct with no errors */
	public int wordsCorrectNoErrors;

	/* Amount of translations that got revealed */
	public int translationsRevealed;
	/* Amount of translations that got no answer (both unanswered and those with only incorrect answers) */
	public int translationsNotAnswered;
	/* Amount of incorrect translation attempts, also includes one for each "not answered" and "revealed" */
	public int translationsIncorrect;
	/* Amount of correct translations, there may have been errors along the way */
	public int translationsCorrect;
	
	public long timestamp;
	
	
	@Override
	public String toString() {
		String ids = Arrays.stream(wordListIds).mapToObj(Integer::toString).collect(Collectors.joining(":"));
		
		return ids + "; " + amountOfWords + ":" + amountOfTranslations + ":" + isTraining + ":" + includeZeroDecay + "; " +
				wordsNoAnswer + ":" + wordsIncorrect + ":" + wordsPartiallyCorrect + ":" + wordsCorrect + ":" +
					wordsCorrectNoErrors + "; " + 
				translationsRevealed + ":" + translationsNotAnswered + ":" + translationsIncorrect + ":" + translationsCorrect + "; " + timestamp;
	}
	
	public void fromString(String in) {
		String[] tokens = in.split("; ", -1);
		
		String[] wordListIds = tokens[0].split(":");
		this.wordListIds = new int[wordListIds.length];
		for (int i = 0; i < wordListIds.length; i++) {
			this.wordListIds[i] = Integer.parseInt(wordListIds[i]);
		}
		
		String[] generalStats = tokens[1].split(":");
		amountOfWords = Integer.parseInt(generalStats[0]);
		amountOfTranslations = Integer.parseInt(generalStats[1]);
		isTraining = Boolean.parseBoolean(generalStats[2]);
		includeZeroDecay = Boolean.parseBoolean(generalStats[3]);
		
		String[] wordStats = tokens[2].split(":");
		wordsNoAnswer = Integer.parseInt(wordStats[0]);
		wordsIncorrect = Integer.parseInt(wordStats[1]);
		wordsPartiallyCorrect = Integer.parseInt(wordStats[2]);
		wordsCorrect = Integer.parseInt(wordStats[3]);
		wordsCorrectNoErrors = Integer.parseInt(wordStats[4]);
		
		String[] translationStats = tokens[3].split(":");
		translationsRevealed = Integer.parseInt(translationStats[0]);
		translationsNotAnswered = Integer.parseInt(translationStats[1]);
		translationsIncorrect = Integer.parseInt(translationStats[2]);
		translationsCorrect = Integer.parseInt(translationStats[3]);
		
		timestamp = Long.parseLong(tokens[4]);
	}
}
