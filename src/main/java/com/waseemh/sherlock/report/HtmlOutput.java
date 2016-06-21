package com.waseemh.sherlock.report;

import java.io.PrintStream;
import java.text.DecimalFormat;

import com.waseemh.sherlock.exec.ComparisonResult;

public class HtmlOutput extends PrintOutput{

	public HtmlOutput(PrintStream stream) {
		super(stream);
	}

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

	@Override
	public void formatOutput() {
		properties.setProperty(FormattedProperty.BEFORE_CAPTURE.toString(), "<p>Capturing {0}</p>");
		properties.setProperty(FormattedProperty.AFTER_CAPTURE.toString(), "<p>Captured {0}</p>");
		properties.setProperty(FormattedProperty.BEFORE_COMPARE.toString(), "<p>Comparing {0}</p>");
		properties.setProperty(FormattedProperty.AFTER_COMPARE.toString(), "<p>Compared {0}</p>");
		properties.setProperty(FormattedProperty.ON_FAIL.toString(), "<p>Comparison failed for capture {0} with missmatch {1}%</p>");
		properties.setProperty(FormattedProperty.ON_SUCCESS.toString(), "<p>Comparison succedeed for capture {0} with missmatch {1}%</p>");
	}

}
