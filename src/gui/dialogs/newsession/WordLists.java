package gui.dialogs.newsession;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.WordExerciser;
import database.WordList;
import gui.control.ProgramUI;

public class WordLists extends JList<String> {
	private StartButton startButton;
	
	public WordLists(ProgramUI ui, StartButton startButton) {
		this.startButton = startButton;
		
		WordExerciser exerciser = ui.getWordExerciser();
		Collection<WordList> lists = exerciser.getLists();
		
		List<String> names = new ArrayList<>();
		for (WordList wordList : lists) {
			names.add(wordList.name);
		}
		
		setListData(names.toArray(new String[0]));
		
		setBorder(new LineBorder(Color.DARK_GRAY));
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setSelectedIndex(0);
		
		addListSelectionListener(listener);
	}
	
	
	private ListSelectionListener listener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			startButton.setEnabled(getSelectedIndex() >= 0);
		}
	};
}
