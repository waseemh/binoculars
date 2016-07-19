package com.waseemh.sherlock.configuration;

import com.waseemh.sherlock.engines.ImageComparisonEngine;
import com.waseemh.sherlock.exec.ComparisonResultHandler;
import com.waseemh.sherlock.exec.ComparisonTasksExecutor;
import com.waseemh.sherlock.report.Reporter;

import java.awt.*;

/**
 * Application configuration model.
 *
 * Defines all configuration parameters related to application behavior.
 */

public abstract class Configuration {
	
	private String rootFolder;

	/**
	 * folder for storing failed screenshots
	 */
	private String failuresFolder;

	/**
	 * folder for storing captured screenshots
	 */
	private String screenshotsFolder;

	/**
	 * folder for storing/loading baseline screenshots
	 */
	private String baselineFolder;

	/**
	 * folder for report output
	 */
	private String reportFolder;

	/**
	 * Force to run in baseline mode, overriding any previous captured screenshots.
	 */
	private boolean baselineMode;

	/**
	 * If set to true, screenshot comparison will be immediately after capturing.
	 */
	private boolean compareUponCapture;

	/**
	 * Image comparison engine
	 */
	private ImageComparisonEngine engine;

	/**
	 * Set threshold for image mismatch percentage.
	 * If a comparison result is above mismatch, comparison will be marked as failed.
	 *
	 */
	private double mismatchThreshold;

	/**
	 * Suffix for baseline screenshots filenames.
	 */
	private String baselineExtension;

	/**
	 * Suffix for captured screenshots filenames.
	 */
	private String captureExtension;

	/**
	 * Suffix for diff images filenames.
	 */
	private String diffExtension;

	/**
	 * Suffix for fail screenshots filenames.
	 */
	private String failExtension;

	/**
	 * Color to use for highlighting mismatch in diff images.
	 */
	private Color diffColor;

	/**
	 * Screenshots resource manager
	 */
	private ResourceManager resourceManager;

	/**
	 * Comparison task executor
	 */
	private ComparisonTasksExecutor executor;

	/**
	 * Comparison result handler
	 */
	private ComparisonResultHandler handler;

	/**
	 * If set to true, comparison mismatch failures will not throw an AssertionError.
	 */
	private boolean absorbFailures;

	/**
	 * Reporter
	 */
	private Reporter reporter;

	/**
	 * Wait timeout in seconds when locating elements using WebDriver
	 */
	private long waitDuration;

	public long getWaitDuration() {
		return waitDuration;
	}

	public void setWaitDuration(long waitDuration) {
		this.waitDuration = waitDuration;
	}

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
	public double getMismatchThreshold() {
		return mismatchThreshold;
	}
	public void setMismatchThreshold(double mismatchThreshold) {
		this.mismatchThreshold = mismatchThreshold;
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
}