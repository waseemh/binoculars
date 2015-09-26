package com.waseemh.sunglasses.exec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.waseemh.sunglasses.configuration.Configuration;
import com.waseemh.sunglasses.configuration.ResourceManager;
import com.waseemh.sunglasses.exceptions.SunglassesWrappedException;
import com.waseemh.sunglasses.exceptions.UIRegressionException;

public class ComparisonResultHandler {

	Configuration configuration;

	public ComparisonResultHandler(Configuration configuration) {
		this.configuration = configuration;
	}

	public void handle(List<ComparisonResult> results) {
		
		ResourceManager resourceManager = configuration.getResourceManager();
		Error e = null;
		
		for(ComparisonResult result: results) {
			String captureName = result.getCaptureName();
			if(result.getMissmatch() > configuration.getMissmatchThreshold()) 
			{
				//report failure
				configuration.getReporter().reportOnFail(result);
				
				//copy failure image
				File failureImage = resourceManager.getFailImage(captureName);
				File diffImage = resourceManager.getDiffImage(captureName);
				try {
					FileUtils.copyFile(diffImage, failureImage);
				} catch (IOException eio) {
					throw new SunglassesWrappedException(eio);
				}
				
				e = new UIRegressionException("UI regression found in capture: '" + captureName + "' with missmatch: " + result.getMissmatch()+"%");
			}
			else {
				//report success
				configuration.getReporter().reportOnSuccess(result);
			}
		}
		
		if((e!=null) && !configuration.isAbsorbFailures()) {
			throw e;
		}
	}
}