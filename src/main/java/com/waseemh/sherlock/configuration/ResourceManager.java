package com.waseemh.sherlock.configuration;

import java.io.File;

/**
 *
 * Manages and loads all captured screenshots
 *
 */

public class ResourceManager {

	private Configuration configuration;
	
	public ResourceManager(Configuration configuration) {
		this.configuration = configuration;
	}

	public File getBaselineImage(String captureName) {
		File baselineImage = new File(configuration.getRootFolder()+File.separator+configuration.getBaselineFolder()+File.separator+captureName+"."+configuration.getBaselineExtension()+".png");
		return baselineImage;
	}

	public boolean isBaselineImageExists(String captureName) {
		File baseline = getBaselineImage(captureName);
		return baseline.exists();
	}

	public void writeBaseline(File newBaselineImage, String captureName) {

	}

	public File getCaptureImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getCaptureExtension()+".png");
		return captureImage;
	}

	public void writeCaptureImage(File newBaselineImage, String captureName) {

	}

	
	public File getDiffImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getDiffExtension()+".png");
		return captureImage;
	}
	
	public File getFailImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getFailuresFolder()+File.separator+captureName+"."+configuration.getFailExtension()+".png");
		return captureImage;
	}

}
