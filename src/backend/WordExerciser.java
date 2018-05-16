/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.Config;
import database.Word;
import database.WordList;
import database.io.WordListIO;
import database.io.WordListImporter;

public class WordExerciser {
	private Map<Integer, WordList> wordLists;
	private Map<Integer, Word> allWords;

	private String wordListFolderPath;
	
	private int nextWordListId;
	private int nextWordId;
	
	private Faults faults;
	
	public WordExerciser() {
		wordLists = new HashMap<>();
		allWords = new HashMap<>();
		faults = new Faults(allWords);
	}
	
	
	public void load() throws IOException {
		load(Config.get(Config.WORDLIST_FOLDER_PATH),
			Config.get(Config.DATA_FOLDER_PATH) + File.separator + Config.FAULTS_FILE);
	}
	public void load(String wordListFolderPath, String faultyWordFilePath) throws IOException {
		this.wordListFolderPath = wordListFolderPath;
		loadListsInternal(new File(wordListFolderPath));
		faults.load(faultyWordFilePath);
		for (Word word: allWords.values()) {
			nextWordId = Math.max(nextWordId, word.id + 1);
		}
	}
	
	private void loadListsInternal(File folder) throws IOException {
		if (!folder.exists())
			folder.mkdirs();
		
		WordListIO loader = new WordListIO();
		for (File file : folder.listFiles()) {
			if (file.isDirectory())
				loadListsInternal(file);
			else {
				WordList list = loader.read(file);
				list.collectWords(allWords);
				if (wordLists.containsKey(list.id))
					throw new IllegalStateException("" + list.id);
				wordLists.put(list.id, list);
				nextWordListId = Math.max(nextWordListId, list.id + 1);
			}
		}
	}
	
	public void save() throws IOException {
		if (wordListFolderPath == null)
			throw new IllegalStateException("load() must be invoked before save()");
		
		WordListIO io = new WordListIO();
		for (WordList list : wordLists.values()) {
			io.write(list);
		}
	}
	
	public List<Word> importList(File sourceFile) throws IOException {
		WordListImporter importer = new WordListImporter();
		WordList importedWords = importer.read(sourceFile);
		
		WordList newList = new WordList();
		newList.id = nextWordListId++;
		newList.name = importedWords.name;
		
		List<Word> existingWords = mergeWords(importedWords, newList);
		
		wordLists.put(newList.id, newList);
		
		int listNumber = 1;
		String baseName = "list-";
		while (true) {
			newList.listPath = new File(wordListFolderPath + File.separator + baseName + listNumber + "." +
					Config.WORDLIST_EXTENSION);
			if (!newList.listPath.exists())
				break;
			listNumber++;
		}
		
		save(); // Save all to make sure all word merges are stored correctly
		return existingWords;
	}

	private List<Word> mergeWords(WordList newWordList, WordList targetList) {
		List<Word> existingWords = new ArrayList<>();
		for (Word word : newWordList.words) {
			boolean existed = false;
			for (Word existingWord : allWords.values()) {
				if (existingWord.word.equalsIgnoreCase(word.word)) {
					mergeTranslations(word, existingWord);
					targetList.words.add(existingWord);
					existingWords.add(existingWord);
					existed = true;
					break;
				}
			}
			if (!existed) {
				word.id = nextWordId++;
				targetList.words.add(word);
				allWords.put(word.id, word);
			}
		}
		return existingWords;
	}

	private void mergeTranslations(Word word, Word targetWord) {
		for (String translation : word.translations) {
			boolean existed = false;
			for (String existingTranslation : targetWord.translations) {
				if (existingTranslation.equalsIgnoreCase(translation)) {
					existed = true;
					break;
				}
			}
			if (!existed) {
				targetWord.translations.add(translation);
			}
		}
	}
	
	
	public Collection<WordList> getLists() {
		return wordLists.values();
	}
	
	
	public Session startExercising(boolean isTraining, int... listIds) {
		Map<Integer, Word> wordsToPractise = new HashMap<>();
		for (int id : listIds) {
			wordLists.get(id).collectWords(wordsToPractise);
		}
		
		Session session = new Session(faults, isTraining, wordsToPractise, listIds, false);
		return session;
	}
	
	public Session startExercisingFaults(boolean isTraining, boolean includeZeroDecay) {
		Map<Integer, Word> wordsToPractise = new HashMap<>();
		faults.collectWords(wordsToPractise, includeZeroDecay);
		
		Session session = new Session(faults, isTraining, wordsToPractise, new int[0], includeZeroDecay);
		return session;
	}
	
	
	public static void main(String[] args) throws IOException {
		WordExerciser ex = new WordExerciser();
		ex.load("wordlists", "data/faults.txt");
		System.out.println(ex.allWords);
		Map<Integer, Word> wordsToPractise = new HashMap<>();
		ex.faults.collectWords(wordsToPractise, false);
		System.out.println(wordsToPractise);
		ex.startExercisingFaults(false, true);
	}
}
