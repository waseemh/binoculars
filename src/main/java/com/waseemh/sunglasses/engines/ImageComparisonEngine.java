package com.waseemh.sunglasses.engines;

import java.io.File;

import com.waseemh.sunglasses.exec.ComparisonResult;

public interface ImageComparisonEngine {
	
	ComparisonResult compare(String captureName, File baseline, File imageUnderTest);

}