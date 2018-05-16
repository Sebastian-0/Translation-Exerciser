/*
 * Copyright (C) 2016 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

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
	
	public Word(Word oth) {
		this();
		id = oth.id;
		word = oth.word;
		translations.addAll(oth.translations);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Word) {
			Word other = (Word) obj;
			return id == other.id && word.equals(other.word);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return word + "->" + translations.toString();
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}
}
