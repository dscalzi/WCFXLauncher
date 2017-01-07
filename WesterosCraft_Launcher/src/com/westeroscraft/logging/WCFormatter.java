package com.westeroscraft.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Formatter which is intended for use in formatting
 * console output.
 * 
 * @author Daniel D. Scalzi
 *
 */
public class WCFormatter extends Formatter{

	private static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
	
	@Override
	public String format(LogRecord record) {
		StringBuilder builder = new StringBuilder(1000);
		builder.append("[").append(df.format(new Date(record.getMillis()))).append("] ");
		builder.append("[").append(record.getLevel()).append("] - ");
		builder.append(formatMessage(record));
		builder.append("\n");
		return builder.toString();
    }
    
    @Override
    public String getHead(Handler h){
    	return super.getHead(h);
    }
    
    @Override
    public String getTail(Handler h){
    	return super.getTail(h);
    }
	
}
