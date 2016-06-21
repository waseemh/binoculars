package com.waseemh.sherlock.engines;

import java.io.File;

import com.waseemh.sherlock.exec.ComparisonResult;

public interface ImageComparisonEngine {
	
	ComparisonResult compare(String captureName, File baseline, File imageUnderTest);

}