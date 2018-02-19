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
	private WordExerciser exerciser;
	
	private StartButton startButton;
	
	public WordLists(ProgramUI ui, StartButton startButton) {
		this.startButton = startButton;
		this.exerciser = ui.getWordExerciser();
		Collection<WordList> lists = exerciser.getLists();
		
		List<String> names = new ArrayList<>();
		for (WordList wordList : lists) {
			names.add(wordList.name);
		}
		
//		names.add(Table.get("new_session_previous_errors"));
		
		setListData(names.toArray(new String[0]));
		
		setBorder(new LineBorder(Color.DARK_GRAY));
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setSelectedIndex(0);
		
		addListSelectionListener(listener);
	}
	
	
	public int[] getWordLists() {
		List<WordList> lists = new ArrayList<>(exerciser.getLists());
		List<Integer> result = new ArrayList<>();
		for (int index : getSelectedIndices()) {
			result.add(lists.get(index).id);
		}
		return result.stream().mapToInt(i->i).toArray();
	}
	
	
	private ListSelectionListener listener = new ListSelectionListener() {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			startButton.setEnabled(getSelectedIndex() >= 0);
		}
	};
}
