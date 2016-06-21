package com.waseemh.sherlock.events;

import com.waseemh.sherlock.exec.ComparisonResult;

public interface EventListener {
	
	public void beforeCapture(String captureName);
	
	public void afterCapture(String captureName);
	
	public void beforeCompare(String captureName);
	
	public void afterCompare(String captureName);
	
	public void onSuccess(ComparisonResult result);
	
	public void onFail(ComparisonResult result);

}