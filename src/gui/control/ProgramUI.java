/*
 * Copyright (C) 2018 Sebastian Hjelm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License.
 */

package gui.control;

import backend.WordExerciser;

public interface ProgramUI {
	public void shutdown();
	
	public WordExerciser getWordExerciser();
	
	public void startExercising(int[] listIds, boolean isPractising);
	public void startExercisingFaulty(boolean includeZeroDecay);

	public void stopExercising();
}
