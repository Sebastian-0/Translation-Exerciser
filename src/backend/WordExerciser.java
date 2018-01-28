package backend;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Word;
import database.WordList;
import database.io.WordListLoader;

public class WordExerciser {
	private List<WordList> wordLists;
	private Map<Integer, Word> allWords; // TODO Maybe remove this? In that case keep some way of checking for duplicates on load
	
	public WordExerciser() {
		wordLists = new ArrayList<>();
		allWords = new HashMap<>();
	}
	
	public List<WordList> loadLists(File folder) throws IOException {
		loadListsInternal(folder);
		return wordLists;
	}
	private void loadListsInternal(File folder) throws IOException {
		if (!folder.exists())
			folder.mkdirs();
		
		WordListLoader loader = new WordListLoader();
		for (File file : folder.listFiles()) {
			if (file.isDirectory())
				loadListsInternal(file);
			else {
				WordList list = loader.read(file);
				list.collectWords(allWords);
				wordLists.add(list);
			}
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		WordExerciser ex = new WordExerciser();
		ex.loadLists(new File("wordlists"));
	}
}
