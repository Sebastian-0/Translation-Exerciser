package gui.dialogs.newsession;

import javax.swing.JCheckBox;

import config.Table;

public class IncludeAllCheckbox extends JCheckBox {
	public IncludeAllCheckbox() {
		super(Table.get("new_session_include_all"));
	}
}
