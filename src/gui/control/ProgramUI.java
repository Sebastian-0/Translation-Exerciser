package gui.control;

import backend.WordExerciser;

public interface ProgramUI {
	public void shutdown();
	
	public WordExerciser getWordExerciser();
	
	public void startExercising(int[] listIds, boolean isPractising);
	public void startExercisingFaulty(boolean isPractising);

	public void stopExercising();
}
