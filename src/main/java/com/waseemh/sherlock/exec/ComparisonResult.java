package com.waseemh.sherlock.exec;

/**
 * POJO for storing comparison result tasks
 */

public class ComparisonResult {
	
	private String captureName;
	
	private double mismatch;

	public String getCaptureName() {
		return captureName;
	}

	public void setCaptureName(String captureName) {
		this.captureName = captureName;
	}

	public double getMismatch() {
		return mismatch;
	}

	public void setMismatch(double mismatch) {
		this.mismatch = mismatch;
	}

}
