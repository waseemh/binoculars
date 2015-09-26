package com.waseemh.sunglasses.configuration;

import java.awt.Color;

import com.waseemh.sunglasses.engines.ImageComparisonEngine;
import com.waseemh.sunglasses.exec.ComparisonResultHandler;
import com.waseemh.sunglasses.exec.ComparisonTasksExecutor;
import com.waseemh.sunglasses.report.Reporter;
import com.waseemh.sunglasses.webdriver.WebDriverWaiter;

public abstract class Configuration {
	
	private String rootFolder;
	
	private String failuresFolder; //folder for storing failed screenshots
	
	private String screenshotsFolder; //folder for storing captured screenshots
	
	private String baselineFolder; //folder for storing/loading baseline screenshots
	
	private String reportFolder; //folder for report output
	
	private boolean baselineMode; //force baseline
	
	private boolean compareUponCapture;
	
	private ImageComparisonEngine engine;
	
	private double missmatchThreshold;
	
	private String baselineExtension;
	
	private String captureExtension;
	
	private String diffExtension;
	
	private String failExtension;
	
	private Color diffColor;
	
	private ResourceManager resourceManager;
	
	private ComparisonTasksExecutor executor;
	
	private ComparisonResultHandler handler;
	
	private boolean absorbFailures;
	
	private Reporter reporter;
	
	private WebDriverWaiter waiter;
	
	public boolean isCompareUponCapture() {
		return compareUponCapture;
	}
	public void setCompareUponCapture(boolean compareUponCapture) {
		this.compareUponCapture = compareUponCapture;
	}
	public String getRootFolder() {
		return rootFolder;
	}
	public void setRootFolder(String rootFolder) {
		this.rootFolder = rootFolder;
	}
	public String getFailuresFolder() {
		return failuresFolder;
	}
	public void setFailuresFolder(String failuresFolder) {
		this.failuresFolder = failuresFolder;
	}
	public String getScreenshotsFolder() {
		return screenshotsFolder;
	}
	public void setScreenshotsFolder(String screenshotsFolder) {
		this.screenshotsFolder = screenshotsFolder;
	}
	public boolean isBaselineMode() {
		return baselineMode;
	}
	public void setBaselineMode(boolean baselineMode) {
		this.baselineMode = baselineMode;
	}
	public ImageComparisonEngine getEngine() {
		return engine;
	}
	public void setEngine(ImageComparisonEngine engine) {
		this.engine = engine;
	}
	public String getBaselineFolder() {
		return baselineFolder;
	}
	public void setBaselineFolder(String baselineFolder) {
		this.baselineFolder = baselineFolder;
	}
	public String getReportFolder() {
		return reportFolder;
	}
	public void setReportFolder(String reportFolder) {
		this.reportFolder = reportFolder;
	}
	public double getMissmatchThreshold() {
		return missmatchThreshold;
	}
	public void setMissmatchThreshold(double missmatchThreshold) {
		this.missmatchThreshold = missmatchThreshold;
	}
	public String getBaselineExtension() {
		return baselineExtension;
	}
	public void setBaselineExtension(String baselineExtension) {
		this.baselineExtension = baselineExtension;
	}
	public String getCaptureExtension() {
		return captureExtension;
	}
	public void setCaptureExtension(String captureExtension) {
		this.captureExtension = captureExtension;
	}
	public Color getDiffColor() {
		return diffColor;
	}
	public void setDiffColor(Color diffColor) {
		this.diffColor = diffColor;
	}
	public String getFailExtension() {
		return failExtension;
	}
	public void setFailExtension(String failExtension) {
		this.failExtension = failExtension;
	}
	public ResourceManager getResourceManager() {
		return resourceManager;
	}
	public void setResourceManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}
	public String getDiffExtension() {
		return diffExtension;
	}
	public void setDiffExtension(String diffExtension) {
		this.diffExtension = diffExtension;
	}
	public ComparisonTasksExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(ComparisonTasksExecutor executor) {
		this.executor = executor;
	}
	public ComparisonResultHandler getHandler() {
		return handler;
	}
	public void setHandler(ComparisonResultHandler handler) {
		this.handler = handler;
	}
	public boolean isAbsorbFailures() {
		return absorbFailures;
	}
	public void setAbsorbFailures(boolean absorbFailures) {
		this.absorbFailures = absorbFailures;
	}
	public Reporter getReporter() {
		return reporter;
	}
	public void setReporter(Reporter reporter) {
		this.reporter = reporter;
	}
	
	public WebDriverWaiter getWaiter() {
		return waiter;
	}
	public void setWaiter(WebDriverWaiter waiter) {
		this.waiter = waiter;
	}

}