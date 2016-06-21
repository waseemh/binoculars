package com.waseemh.sherlock.exec;

import java.io.File;
import java.util.concurrent.Callable;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.configuration.ResourceManager;

public final class ComparisonTask implements Callable<ComparisonResult>{

	private String captureName;

	private Configuration configuration;

	public ComparisonTask(String name, Configuration configuration) {
		this.captureName = name;
		this.configuration = configuration;
	}

	public ComparisonResult call() throws Exception {

		ResourceManager resourceManager = configuration.getResourceManager();
		File baselineImage = resourceManager.getBaselineImage(captureName);
		File captureImage = resourceManager.getCaptureImage(captureName);
		configuration.getReporter().reportBeforeCompare(captureName);
		ComparisonResult result;
		result = configuration.getEngine().compare(captureName,baselineImage, captureImage);
		configuration.getReporter().reportAfterCompare(captureName);
		return result;
	}

	public String getCaptureName() {
		return captureName;
	}

}