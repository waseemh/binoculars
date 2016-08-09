package com.waseemh.sherlock.report;

import java.io.PrintStream;
import java.text.DecimalFormat;

import com.waseemh.sherlock.exec.ComparisonResult;

public class ConsoleOutput extends PrintOutput{
	
	public ConsoleOutput(PrintStream stream) {
		super(stream);
	}

	@Override
	public void formatOutput() {
		properties.setProperty(FormattedProperty.BEFORE_CAPTURE.toString(), "Capturing {0}");
		properties.setProperty(FormattedProperty.AFTER_CAPTURE.toString(), "Captured {0}");
		properties.setProperty(FormattedProperty.BEFORE_COMPARE.toString(), "Comparing {0}");
		properties.setProperty(FormattedProperty.AFTER_COMPARE.toString(), "Compared {0}");
		properties.setProperty(FormattedProperty.ON_FAIL.toString(), "Comparison failed for capture {0} with missmatch {1}%");
		properties.setProperty(FormattedProperty.ON_SUCCESS.toString(), "Comparison succedeed for capture {0} with missmatch {1}%");
	}

}