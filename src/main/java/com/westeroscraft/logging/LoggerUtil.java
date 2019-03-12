package com.westeroscraft.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class which allows retrieval of pre-formatted loggers.
 * 
 * @author Daniel D. Scalzi
 *
 */
public class LoggerUtil {

	private final static Map<String, Logger> cache;
	
	private static WCFormatter formatter;
	private static ConsoleHandler handler;
	
	static {
		cache = new HashMap<String, Logger>();
		formatter = new WCFormatter();
		handler = new ConsoleHandler();
		handler.setFormatter(formatter);
		handler.setLevel(Level.ALL);
	}
	
	/**
	 * Find or create a logger for a named subsystem. If a logger has
	 * already been created with the given name it is returned. The logger
	 * will be pre-formatted so that the output to the launcher log is
	 * marked with a timestamp and level.
	 * 
	 * @param name A name for the logger.
	 * @return a suitable Logger
	 */
	public static Logger getLogger(String name){
		
		if(cache.get(name) != null) return cache.get(name);
		
		Logger logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
		
		cache.put(name, logger);
		
		return logger;
	}
	
}
