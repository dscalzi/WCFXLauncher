package com.westeroscraft.core.mojang;

import java.io.File;
import java.io.IOException;

import com.westeroscraft.logging.LoggerUtil;

public final class SettingsManager {

	private static SettingsManager instance;
	
	public static void initialize(){
		if(instance == null){
			instance = new SettingsManager();
		} else {
			throw new IllegalStateException("Settings Manager already initialized!");
		}
	}
	
	public static SettingsManager getInstance(){
		return instance;
	}
	
	private static File settingsFile;
	
	private SettingsManager(){
		loadJson();
	}
	
	private void loadJson(){
		try {
			this.verifyFile();
			LoggerUtil.getLogger("Launcher").info("Loading settings..");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean verifyFile() throws IOException{
		if(!settingsFile.exists()) return settingsFile.createNewFile();
		return true;
	}
	
}
