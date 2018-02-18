package gui.dialogs.newsession;

import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import config.Table;
import gui.control.ProgramUI;
import gui.dialogs.CancelButton;
import util.SimpleGridBagLayout;

public class NewSessionDialog extends JDialog {

	private PracticeRadioButton practiceRadioButton;
	private TestRadioButton testRadioButton;
	private WordLists wordLists;
	
	public NewSessionDialog(ProgramUI ui) {
		super ((Frame) ui, Table.get("new_session_title"));
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		practiceRadioButton = new PracticeRadioButton();
		testRadioButton = new TestRadioButton();
		StartButton startButton = new StartButton(this);
		wordLists = new WordLists(ui, startButton);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new TitledBorder(Table.get("new_session_exercise_mode")));
		panel.add(practiceRadioButton);
		panel.add(testRadioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(practiceRadioButton);
		group.add(testRadioButton);
		testRadioButton.setSelected(true);
		
		JScrollPane scroll = new JScrollPane(wordLists);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Temporary to get extra width for the component
		
		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);

		layout.addToGrid(new JLabel(Table.get("new_session_word_lists")), 0, 0, 1, 1);
		layout.addToGrid(scroll, 0, 1, 1, 2, GridBagConstraints.BOTH, 1, 1);

		layout.addToGrid(panel, 1, 1, 2, 2, GridBagConstraints.HORIZONTAL, 0, 0, GridBagConstraints.NORTH);

		layout.addToGrid(new CancelButton(this), 2, 2, 1, 1, GridBagConstraints.NONE, 0, 1, GridBagConstraints.SOUTH);
		layout.addToGrid(startButton, 1, 2, 1, 1, GridBagConstraints.NONE, 0, 1, GridBagConstraints.SOUTHEAST);
		
		pack();
		setMinimumSize(getSize());

		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		setLocationRelativeTo((Component) ui);
	}
	
	
	public void createSession() {
		
	}
}