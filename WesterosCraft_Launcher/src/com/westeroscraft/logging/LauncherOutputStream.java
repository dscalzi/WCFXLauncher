package com.westeroscraft.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 * Outputstream which redirects output onto a TextArea
 * which is intended for use as the launcher log.
 * 
 * @author Daniel D. Scalzi
 *
 */
public class LauncherOutputStream extends OutputStream{

	private TextArea node;
	private Formatter formatter;
	
	/**
	 * Create a new LauncherOutputStream which will print output
	 * to the given TextArea object.
	 * 
	 * @param textArea The TextArea that this OutputStream will print to.
	 */
	public LauncherOutputStream(TextArea textArea){
		this.node = textArea;
		node.getStyleClass().add("launcher-log");
	}
	
	/**
	 * Create a new LauncherOutputStream which will print output
	 * to the given TextArea object formatted provided by
	 * the formatter.
	 * 
	 * It is recommended to use a Logger object retrieved from
	 * LoggerUtil rather than using a formatter directly on this
	 * OutputStream.
	 * 
	 * @param textArea The TextArea that this OutputStream will print to.
	 * @param formatter A formatter object which will be used to format output.
	 */
	public LauncherOutputStream(TextArea textArea, Formatter formatter){
		this(textArea);
		this.formatter = formatter;
	}
	
	@Override
	public void write(int i) throws IOException {
		synchronized(this){
			Platform.runLater(() -> {
				node.appendText(String.valueOf((char) i));
			});
		}
		
	}
	
	@Override
	public void write(byte[] bytes, int offset, int length) {
		String s = new String(bytes, offset, length);
		if(formatter != null){
			LogRecord r = new LogRecord(Level.INFO, s);
			node.appendText(formatter.format(r));
		} else {
			node.appendText(s);
		}
	}
	
	/**
	 * Set a formatter for this LauncherOutputStream.
	 * The output of this stream will be formatted using
	 * the formatter provided.
	 * 
	 * It is recommended to use a Logger object retrieved from
	 * LoggerUtil rather than using a formatter directly on this
	 * OutputStream.
	 * 
	 * @param formatter A formatter object which will be used to format output.
	 */
	public void setFormatter(Formatter formatter){
		this.formatter = formatter;
	}
	
}
