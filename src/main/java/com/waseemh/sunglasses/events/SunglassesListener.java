package com.waseemh.sunglasses.events;

import com.waseemh.sunglasses.exec.ComparisonResult;

public interface SunglassesListener {
	
	public void beforeCapture(String captureName);
	
	public void afterCapture(String captureName);
	
	public void beforeCompare(String captureName);
	
	public void afterCompare(String captureName);
	
	public void onSuccess(ComparisonResult result);
	
	public void onFail(ComparisonResult result);

}