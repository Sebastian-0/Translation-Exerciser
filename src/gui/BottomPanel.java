package gui;

import javax.swing.JPanel;

import gui.control.ProgramUI;

public class BottomPanel extends JPanel {
	private FinishSessionButton finishSessionButton;

	public BottomPanel(ProgramUI ui) {
		finishSessionButton = new FinishSessionButton(ui);
		add(finishSessionButton);
	}

	public void sessionStarted() {
		finishSessionButton.setEnabled(true);
	}

	public void sessionEnded() {
		finishSessionButton.setEnabled(false);
	}
}
