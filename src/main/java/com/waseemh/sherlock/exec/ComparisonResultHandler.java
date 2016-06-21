package com.waseemh.sherlock.exec;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.configuration.ResourceManager;
import com.waseemh.sherlock.exceptions.SherlockWrappedException;
import com.waseemh.sherlock.exceptions.UIRegressionException;

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
			if(result.getMismatch() > configuration.getMismatchThreshold())
			{
				//report failure
				configuration.getReporter().reportOnFail(result);
				
				//copy failure image
				File failureImage = resourceManager.getFailImage(captureName);
				File diffImage = resourceManager.getDiffImage(captureName);
				try {
					FileUtils.copyFile(diffImage, failureImage);
				} catch (IOException eio) {
					throw new SherlockWrappedException(eio);
				}
				
				e = new UIRegressionException("UI regression found in capture: '" + captureName + "' with missmatch: " + result.getMismatch()+"%");
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