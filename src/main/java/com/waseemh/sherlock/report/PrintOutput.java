package com.waseemh.sherlock.report;

import java.io.PrintStream;
import java.util.Properties;

import com.waseemh.sherlock.events.EventListener;

public abstract class PrintOutput implements EventListener {
	
	PrintStream printStream;
	
	protected Properties properties = new Properties();
	
	public PrintOutput(PrintStream printStream) {
		this.printStream = printStream;
		formatOutput();
	}

	public void print(FormattedProperty key, String... values) {
		//replace placeholders before print
		String outputTemplate = properties.getProperty(key.toString());
		for(int i=0; i<values.length;i++) {
			outputTemplate=outputTemplate.replace("{"+i+"}", values[i]);
		}
		printStream.println(outputTemplate);
	}
	
	public PrintStream getPrintStream() {
		return printStream;
	}

	public void setPrintStream(PrintStream printStream) {
		this.printStream = printStream;
	}

	public abstract void formatOutput();
	
	protected enum FormattedProperty {
		
		BEFORE_CAPTURE,AFTER_CAPTURE,BEFORE_COMPARE,AFTER_COMPARE,ON_FAIL,ON_SUCCESS;

	}

}