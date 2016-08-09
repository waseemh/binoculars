package com.waseemh.sherlock.report;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Properties;

import com.waseemh.sherlock.events.EventListener;
import com.waseemh.sherlock.exec.ComparisonResult;

public abstract class PrintOutput implements EventListener {
	
	private PrintStream printStream;
	
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

	public void beforeCapture(String captureName) {
		print(FormattedProperty.BEFORE_CAPTURE, captureName);
	}

	public void afterCapture(String captureName) {
		print(FormattedProperty.AFTER_CAPTURE, captureName);
	}

	public void beforeCompare(String captureName) {
		print(FormattedProperty.BEFORE_COMPARE, captureName);
	}

	public void afterCompare(String captureName) {
		print(FormattedProperty.AFTER_COMPARE, captureName);
	}

	public void onSuccess(ComparisonResult result) {
		print(FormattedProperty.ON_SUCCESS, result.getCaptureName(),new DecimalFormat("#0.##").format(result.getMismatch()));
	}

	public void onFail(ComparisonResult result) {
		print(FormattedProperty.ON_FAIL, result.getCaptureName(),new DecimalFormat("#0.##").format(result.getMismatch()));
	}
	
	protected enum FormattedProperty {
		
		BEFORE_CAPTURE,AFTER_CAPTURE,BEFORE_COMPARE,AFTER_COMPARE,ON_FAIL,ON_SUCCESS;

	}

}