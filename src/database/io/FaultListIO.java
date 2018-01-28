package database.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.FaultyWord;

public class FaultListIO {
	
	public List<FaultyWord> read(File input) throws IOException {
		List<FaultyWord> list = new ArrayList<>();
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		while (true) {
			String wordLine = in.readLine();
			if (wordLine == null)
				break;
			
			if (!wordLine.isEmpty()) {
				String[] wordTokens = wordLine.split(";");
				
				FaultyWord word = new FaultyWord();
				word.id = Integer.parseInt(wordTokens[0]);
				word.decay = Integer.parseInt(wordTokens[1]);
				
				list.add(word);
			}
		}
		
		in.close();
		
		return list;
	}
	
	public void write(File input, List<FaultyWord> list) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(input));
		
		for (FaultyWord faultyWord : list) {
			out.write(faultyWord.id + ";" + faultyWord.decay + "\r\n");
		}
		
		out.close();
	}
	
	
	public static void main(String[] args) throws IOException {
		List<FaultyWord> list = new FaultListIO().read(new File("data/faults.txt"));
		new FaultListIO().write(new File("data/temp.txt"), list);
	}
}
