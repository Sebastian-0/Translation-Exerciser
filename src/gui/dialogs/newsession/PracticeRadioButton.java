package gui.dialogs.newsession;

import javax.swing.JRadioButton;

import config.Table;

public class PracticeRadioButton extends JRadioButton {
	public PracticeRadioButton() {
		super(Table.get("new_session_practice"));
	}
}
