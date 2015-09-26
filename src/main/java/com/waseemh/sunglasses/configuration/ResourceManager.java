package com.waseemh.sunglasses.configuration;

import java.io.File;


public class ResourceManager {

	protected ClassLoader classLoader;
	
	private Configuration configuration;

	protected ResourceManager() {
		classLoader = Thread.currentThread().getContextClassLoader();
	}
	
	public ResourceManager(Configuration configuration) {
		this.configuration = configuration;
	}


	public File getBaselineImage(String captureName) {
		File baselineImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getBaselineExtension()+".png");
		return baselineImage;
	}
	
	public File getCaptureImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getCaptureExtension()+".png");
		return captureImage;
	}
	
	public File getDiffImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getDiffExtension()+".png");
		return captureImage;
	}
	
	public File getFailImage(String captureName) {
		File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getFailExtension()+".png");
		return captureImage;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	

}
