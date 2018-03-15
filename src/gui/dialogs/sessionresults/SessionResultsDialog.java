package gui.dialogs.sessionresults;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.text.MessageFormat;

import javax.swing.JDialog;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import backend.Statistics;
import config.Table;
import gui.control.ProgramUI;
import util.SimpleGridBagLayout;

public class SessionResultsDialog extends JDialog {
	
	public SessionResultsDialog(ProgramUI ui, Statistics stats) {
		super ((Frame) ui, Table.get("session_results_title"));
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		
		CloseButton startButton = new CloseButton(this);
		JTextArea resultsText = new JTextArea(getStatisticsMessage(stats));
		resultsText.setEditable(false);
		resultsText.setBorder(new CompoundBorder(new LineBorder(Color.BLACK), new EmptyBorder(5, 5, 5, 5)));
		
		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);

		layout.addToGrid(resultsText, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 1);
		layout.addToGrid(startButton, 0, 1, 1, 1, GridBagConstraints.NONE, 0, 0, GridBagConstraints.CENTER);
		
		pack();
		setMinimumSize(getSize());
		setLocationRelativeTo((Component) ui);
		
		getRootPane().setDefaultButton(startButton);
	}

	private String getStatisticsMessage(Statistics stats) {
		return MessageFormat.format(Table.get("session_results_message"),
				stats.amountOfWords,
				stats.wordsCorrectNoErrors, String.format("%.1f", stats.wordsCorrectNoErrors/(float)stats.amountOfWords*100),
				stats.wordsCorrect, String.format("%.1f", stats.wordsCorrect/(float)stats.amountOfWords*100),
				stats.wordsPartiallyCorrect, String.format("%.1f", stats.wordsPartiallyCorrect/(float)stats.amountOfWords*100),
				stats.wordsIncorrect, String.format("%.1f", stats.wordsIncorrect/(float)stats.amountOfWords*100),
				stats.wordsNoAnswer, String.format("%.1f", stats.wordsNoAnswer/(float)stats.wordsIncorrect*100),
				stats.amountOfTranslations,
				stats.translationsCorrect, String.format("%.1f", stats.translationsCorrect/(float)stats.amountOfTranslations*100),
				stats.translationsIncorrect, String.format("%.1f", stats.translationsIncorrect/(float)stats.amountOfTranslations*100),
				stats.translationsNotAnswered, String.format("%.1f", stats.translationsNotAnswered/(float)stats.translationsIncorrect*100));
	}
}
