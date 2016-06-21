package com.waseemh.sherlock.report;

import com.waseemh.sherlock.exec.ComparisonResult;

public class ReportModel {
	
	private ComparisonResult result;
	
	private boolean isPass;
	
	public ReportModel(ComparisonResult result, boolean isPass) {
		this.result = result;
		this.isPass = isPass;
	}

	public ComparisonResult getResult() {
		return result;
	}

	public void setResult(ComparisonResult result) {
		this.result = result;
	}

	public boolean isPass() {
		return isPass;
	}

	public void setPass(boolean isPass) {
		this.isPass = isPass;
	}
	
	

}
