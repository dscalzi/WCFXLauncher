package com.westeroscraft.core.mojang;

import java.io.File;

public enum DefaultSettings {

	/**
	 * Default Value is a String
	 */
	MIN_MEMORY("minimumMemory", "1G"),
	/**
	 * Default Value is a String
	 */
	MAX_MEMORY("maximumMemory","4G"),
	/**
	 * Default value is a File.
	 */
	JAVA_PATH("javaPath", null),
	/**
	 * Default value is a String.
	 */
	JVM_OPTIONS("jvmOptions", "-Xmx1G -XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy -Xmn128M"),
	/**
	 * Default value is an Integer
	 */
	RES_WIDTH("resWidth", 854),
	/**
	 * Default value is an Integer
	 */
	RES_HEIGHT("resHeight", 480),
	/**
	 * Default value is a Boolean
	 */
	FULLSCREEN("fullscreen", false),
	/**
	 * Default value is a Boolean
	 */
	AUTO_CONNECT("autoConnect", false),
	/**
	 * Default value is a Boolean
	 */
	MINIMIZE("minimizeOnLaunch", true);
	
	private String key;
	private Object defaultValue;
	
	private DefaultSettings(String key, Object defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public Object getDefaultValue(){
		if(defaultValue == null)
			defaultValue = dynamicResolver(this);
		return defaultValue;
	}
	
	public static Object dynamicResolver(DefaultSettings s){
		switch(s){
		case JAVA_PATH:
			File f = new File(System.getProperty("java.home"));
			f = new File(f, "bin" + File.separator + "javaw.exe");
			return f;
		default:
			return null;
		}
	}
	
}
