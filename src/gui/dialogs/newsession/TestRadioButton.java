package gui.dialogs.newsession;

import javax.swing.JRadioButton;

import config.Table;

public class TestRadioButton extends JRadioButton {
	public TestRadioButton() {
		super(Table.get("new_session_test"));
	}
}
