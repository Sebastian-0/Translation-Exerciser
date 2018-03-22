package database.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import backend.Statistics;

public class StatisticsIO {
	
	public List<Statistics> read(File input) throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		List<Statistics> result = new ArrayList<>();
		while (true) {
			String wordLine = in.readLine();
			if (wordLine == null)
				break;
			
			if (!wordLine.trim().isEmpty()) {
				Statistics stats = new Statistics();
				stats.fromString(wordLine.trim());
				result.add(stats);
			}
		}
		
		in.close();
		
		return result;
	}
	
	
	public void write(File output, Statistics stats) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(output, true));
		writer.write(stats.toString() + "\n");
		writer.close();
	}


	public static void main(String[] args) throws IOException {
		new StatisticsIO().read(new File("wordlists/list-1.txt"));
	}
}
