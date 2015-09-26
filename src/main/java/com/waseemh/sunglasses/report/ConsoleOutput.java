package com.waseemh.sunglasses.report;

import java.io.PrintStream;
import java.text.DecimalFormat;

import com.waseemh.sunglasses.exec.ComparisonResult;

public class ConsoleOutput extends PrintOutput{
	
	public ConsoleOutput(PrintStream stream) {
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
		print(FormattedProperty.ON_SUCCESS, result.getCaptureName(),new DecimalFormat("#0.##").format(result.getMissmatch()));	
	}

	public void onFail(ComparisonResult result) {
		print(FormattedProperty.ON_FAIL, result.getCaptureName(),new DecimalFormat("#0.##").format(result.getMissmatch()));
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