package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import backend.Statistics;
import backend.WordExerciser;
import config.Config;
import config.Table;
import gui.control.ProgramUI;
import gui.dialogs.sessionresults.SessionResultsDialog;
import gui.menubar.MenuBar;
import sutilities.Debugger;
import util.SimpleGridBagLayout;
import util.TextureStorage;

public class TranslationExerciser extends JFrame implements ProgramUI {
	
	private ExercisingPanel exercisingPanel;
	private BottomPanel bottomPanel;

	private WordExerciser exerciser;

	public TranslationExerciser() {
		initLanguages();
		if (initExerciser()) {
			initGui();
		}
	}

	private void initLanguages() {
		try
		{
			Table.loadLanguages(new File(Config.get(Config.LANGUAGE_FOLDER_PATH)), Config.STRING_TABLE_EXTENSION);
			
			Table table = Table.getLanguage(Config.get(Config.LANGUAGE));
			if (table == null) {
				for (Table stringTable : Table.getLanguages()) {
					String tableLocale = stringTable.getTableString("java_locale_language") + "_" + stringTable.getTableString("java_locale_country");
					
					if (tableLocale.equalsIgnoreCase(Locale.getDefault().toString().substring(0, 5))) {
						table = stringTable;
						break;
					}
				}
			}
			
			if (table == null)
				table = Table.getLanguage("English");
			
			if (table == null && !Table.getLanguages().isEmpty())
				table = Table.getLanguages().get(0);
			
			if (table != null) {
				Config.put(Config.LANGUAGE, table.getName());
				Table.setLanguage(table);
			} else {
				JOptionPane.showMessageDialog(
						null,
						"Failed to load language files, no languages are installed!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(
					null,
					"Failed to load language files, their folder doesn't exist!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean initExerciser() {
		exerciser = new WordExerciser();
		try {
			exerciser.load();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					null,
					MessageFormat.format(Table.get("init_exerciser_failed_io"), e.getMessage()),
					Table.get("init_exerciser_failed_title"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IllegalStateException e) {
			JOptionPane.showMessageDialog(
					null,
					MessageFormat.format(Table.get("init_exerciser_failed_duplicates"), e.getMessage()),
					Table.get("init_exerciser_failed_title"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (Throwable e) {
			JOptionPane.showMessageDialog(
					null,
					MessageFormat.format(Table.get("init_exerciser_failed_unknown"), e.getMessage()),
					Table.get("init_exerciser_failed_title"),
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void initGui() {
		setTitle(Table.get("window_title"));
		loadProgramIcon();
		
		setJMenuBar(new MenuBar(this));
		
		SimpleGridBagLayout layout = new SimpleGridBagLayout(this);
		exercisingPanel = new ExercisingPanel();
		bottomPanel = new BottomPanel(this);
		
		layout.addToGrid(exercisingPanel, 0, 0, 1, 1, GridBagConstraints.BOTH, 1, 1);
		layout.addToGrid(bottomPanel, 0, 1, 1, 1, GridBagConstraints.HORIZONTAL, 1, 0);
		
		pack(); // Pack two times to set minimum size before resizing to the preferred size
		setMinimumSize(getSize());
		setPreferredSize(getPreviousSize());
		setExtendedState(getPreviousExtendedState());
		pack();
		
		addWindowListener(windowListener);
		addWindowStateListener(windowListener);
		addComponentListener(componentListener);
		
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	private Dimension getPreviousSize() {
		return new Dimension(
				Integer.parseInt(Config.get(Config.LAST_WINDOW_WIDTH, "10")),
				Integer.parseInt(Config.get(Config.LAST_WINDOW_HEIGHT, "10")));
	}
	
	private int getPreviousExtendedState() {
		return Integer.parseInt(Config.get(Config.LAST_EXTENDED_STATE, "" + NORMAL));
	}

	private void loadProgramIcon() {
		Image image = TextureStorage.instance().getTexture("exerciser_icon");
		Image image16 = TextureStorage.instance().getTexture("exerciser_icon16");
		
		List<Image> images = new ArrayList<Image>();
		images.add(image);
		images.add(image16);
		setIconImages(images);
	}
	
	
	@Override
	public void shutdown() {
		dispose();
	}
	
	
	@Override
	public WordExerciser getWordExerciser() {
		return exerciser;
	}
	
	
	@Override
	public void startExercising(int[] listIds, boolean isPractising) {
		exercisingPanel.start(exerciser.startExercising(isPractising, listIds));
		bottomPanel.sessionStarted();
	}
	
	
	@Override
	public void startExercisingFaulty(boolean isPractising) {
		exercisingPanel.start(exerciser.startExercisingFaults(isPractising, false)); // TODO Proper pass of "includeZeroDecay"
		bottomPanel.sessionStarted();
	}
	
	
	@Override
	public void stopExercising() {
		Statistics stats = exercisingPanel.stop();
		if (stats != null) {
			bottomPanel.sessionEnded();
			SessionResultsDialog dialog = new SessionResultsDialog(this, stats);
			dialog.setVisible(true);
		}
	}
	
	
	
	private WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosing(WindowEvent e) {
			shutdown();
		}
		
		@Override
		public void windowStateChanged(WindowEvent e) {
			int previousMaximizedState = (e.getOldState() & MAXIMIZED_BOTH);
			int currentMaximizedState = (e.getNewState() & MAXIMIZED_BOTH);
			if (previousMaximizedState != currentMaximizedState) {
				Config.put(Config.LAST_EXTENDED_STATE, "" + currentMaximizedState);
			}
		}
	};
		
	private ComponentAdapter componentListener = new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent e) {
			if ((getExtendedState() & MAXIMIZED_HORIZ) == 0)
				Config.put(Config.LAST_WINDOW_WIDTH, "" + getWidth());
			if ((getExtendedState() & MAXIMIZED_VERT) == 0)
				Config.put(Config.LAST_WINDOW_HEIGHT, "" + getHeight());
		}
	};

	public static void main(String[] args)
	{
		Debugger.setIsInDebugMode(true);
		try {
			new TranslationExerciser();
		} catch (Throwable e) {
			Debugger.fatal(TranslationExerciser.class.getSimpleName() + ": main()", "Failed to start program!", e);
			JOptionPane.showMessageDialog(null, "Failed to start program, see log for details: " + e.getMessage(), "Fatal error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}
}
