
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import config.Table;
import gui.control.ProgramUI;

public class FinishSessionButton extends JButton {
	private ProgramUI ui;

	public FinishSessionButton(ProgramUI ui) {
		super(Table.get("bottom_finish_session"));
		this.ui = ui;
		addActionListener(actionListener);
	}

	private ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			ui.stopExercising();
		}
	};
}
