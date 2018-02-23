package gui.model;

import java.awt.Graphics2D;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import backend.Session;

public class WordEngine {

	private List<WordBlock> wordsToTranslate;
	private List<AnswerBlock> targets;
	private List<WordBlock> alternatives;
	
	public WordEngine() {
		wordsToTranslate = new ArrayList<>();
		targets = new ArrayList<>();
		alternatives = new ArrayList<>();
	}

	public void start(Session session) {
		wordsToTranslate.clear();
		alternatives.clear();
		
		List<String> words = session.getWords();
		List<String> translations = session.getTranslations();
				
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
		Collections.shuffle(words);
		translations.sort(collator);
		
		int x = 100;
		int y = 50;
		for (String word : words) {
			wordsToTranslate.add(new WordBlock(word, x, y, true));
			targets.add(new AnswerBlock(x + AbstractBlock.HEIGHT/2, y, false));
			y += 1.5 * AbstractBlock.HEIGHT;
		}
		
		y = 50;
		x += 250;
		for (String word : translations) {
			alternatives.add(new WordBlock(word, x, y, false));
			y += 1.5 * AbstractBlock.HEIGHT;
		}
	}

	public void render(Graphics2D g2d) {
		for (WordBlock wordBlock : wordsToTranslate) {
			wordBlock.render(g2d);
		}
		for (AnswerBlock answerBlock : targets) {
			answerBlock.render(g2d);
		}
		for (WordBlock wordBlock : alternatives) {
			wordBlock.render(g2d);
		}
	}

}
