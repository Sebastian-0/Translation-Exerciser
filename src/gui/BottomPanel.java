package gui;

import javax.swing.JPanel;

import gui.control.ProgramUI;

public class BottomPanel extends JPanel {
	private FinishSessionButton finishSessionButton;

	public BottomPanel(ProgramUI ui) {
		finishSessionButton = new FinishSessionButton(ui);
		add(finishSessionButton);
	}
}
