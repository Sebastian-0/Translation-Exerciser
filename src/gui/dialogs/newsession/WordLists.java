package gui.dialogs.newsession;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import backend.WordExerciser;
import database.WordList;
import gui.control.ProgramUI;

public class WordLists extends JList<String> {
	private WordExerciser exerciser;
	private List<WordList> lists;
	
	private StartButton startButton;
	
	public WordLists(ProgramUI ui, StartButton startButton) {
		this.startButton = startButton;
		this.exerciser = ui.getWordExerciser();
		
		lists = new ArrayList<>(exerciser.getLists()); // TODO WordLists; Use more advanced sorting (Collator)
		lists.sort(new Comparator<WordList>() {
			@Override
			public int compare(WordList o1, WordList o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		
		List<String> names = new ArrayList<>();
		for (WordList wordList : lists) {
			names.add(wordList.name);
		}
		
		setListData(names.toArray(new String[0]));
		
		setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(3, 3, 3, 3)));
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		setSelectedIndex(0);
		
		addListSelectionListener(listener);
	}
	
	
	public int[] getWordLists() {
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
