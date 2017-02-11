package com.westeroscraft.core.mojang;

public enum OperatingSystem {

	LINUX("linux", "linux", "unix"), 
	WINDOWS("windows", "win"), 
	OSX("osx", "mac"), 
	UNKNOWN("unknown");

	private final String name;
	private final String[] aliases;

	private OperatingSystem(String name, String... aliases) {
		this.name = name;
		this.aliases = (aliases == null ? new String[0] : aliases);
	}

	public String getName() { return this.name; }
	public String[] getAliases() { return this.aliases; }
	public boolean isSupported() { return this != UNKNOWN; }
	
	public static OperatingSystem getCurrentPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();

		for (OperatingSystem os : values()) {
			for (String alias : os.getAliases()) {
				if (osName.contains(alias)) return os;
			}
		}

		return UNKNOWN;
	}
	
}
