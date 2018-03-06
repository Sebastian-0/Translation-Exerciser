/*
* Copyright (C) 2016 Sebastian Hjelm
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*/

package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import sutilities.Debugger;

public class Config
{
	private static final String CONFIG_FILE = "config.ini";
	public static final String FAULTS_FILE = "faults.txt";

	public static final String[] WORDLIST_SOURCE_EXTENSIONS = new String[] { "txt", "lst" };
	public static final String WORDLIST_EXTENSION = "txt";
	public static final String STRING_TABLE_EXTENSION = "lang";
	
	public static final String LANGUAGE = "language";

	public static final String LAST_WINDOW_WIDTH = "lastWindowWidth";
	public static final String LAST_WINDOW_HEIGHT = "lastWindowHeight";
	public static final String LAST_EXTENDED_STATE = "lastExtendedState";
	
	public static final String DATA_FOLDER_PATH = "dataFolderPath";
	public static final String WORDLIST_FOLDER_PATH = "worldlistFolderPath";
	public static final String LANGUAGE_FOLDER_PATH = "languageFolderPath";
	
	public static final String USE_DEBUG_MODE = "useDebugMode";

	
	private static Properties properties;
	
	static
	{
		properties = new Properties();
		loadConfig();
	}
	
	public static String get(String configKey)
	{
		return properties.getProperty(configKey, "");
	}
	
	public static String get(String configKey, String defaultValue)
	{
		return properties.getProperty(configKey, defaultValue);
	}
	
	public static void put(String configKey, String value)
	{
		properties.put(configKey, value);
		saveConfig();
	}


	private static void loadConfig()
	{
		generateDefaultValues();
		
		File config = new File(CONFIG_FILE);
		if (config.exists())
		{
			try
			{
				FileInputStream in = new FileInputStream(config);
				properties.load(in);
				in.close();
			} catch (IOException e)
			{
				Debugger.error("Config <static>", "Failed to load config", e);
			}
		}
		else
		{
			// Save default settings
			saveConfig();
		}
	}
	
	private static void generateDefaultValues()
	{
		properties.put(LANGUAGE_FOLDER_PATH, "languages");
		properties.put(DATA_FOLDER_PATH, "data");
		properties.put(WORDLIST_FOLDER_PATH, "wordlists");
		
		properties.put(USE_DEBUG_MODE, "false");
	}

	private static void saveConfig()
	{
		try
		{
			FileOutputStream out = new FileOutputStream(new File(CONFIG_FILE));
			properties.store(out, "Logic Sim ini-file. Do not change manually\n");
			out.close();
		} catch (IOException e)
		{
			Debugger.error("Config <static>", "Failed to save config", e);
		}
	}
}
