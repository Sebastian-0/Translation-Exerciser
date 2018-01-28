package backend;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import database.Word;
import database.WordList;
import database.io.WordListIO;

public class WordExerciser {
	private Map<Integer, WordList> wordLists;
	private Map<Integer, Word> allWords; // TODO Maybe remove this? In that case keep some way of checking for duplicates on load
	
	public WordExerciser() {
		wordLists = new HashMap<>();
		allWords = new HashMap<>();
	}
	
	
	public void load(File wordlistFolder) throws IOException {
		loadListsInternal(wordlistFolder);
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
				wordLists.put(list.id, list);
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
		
		Session session = new Session(isTraining, wordsToPractise);
		return session;
	}
	
	
	public static void main(String[] args) throws IOException {
		WordExerciser ex = new WordExerciser();
		ex.load(new File("wordlists"));
		System.out.println(ex.allWords);
	}
}
