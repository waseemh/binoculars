package com.waseemh.sherlock.events;

import com.waseemh.sherlock.exec.ComparisonResult;

public interface EventListener {
	
	void beforeCapture(String captureName);
	
	void afterCapture(String captureName);
	
	void beforeCompare(String captureName);
	
	void afterCompare(String captureName);
	
	void onSuccess(ComparisonResult result);
	
	void onFail(ComparisonResult result);

}