package com.waseemh.sherlock;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.How;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.configuration.DefaultConfiguration;
import com.waseemh.sherlock.configuration.ScreenshotManager;
import com.waseemh.sherlock.exceptions.BinocularsWrappedException;
import com.waseemh.sherlock.exec.ComparisonTask;
import com.waseemh.sherlock.exec.ComparisonTaskManager;
import com.waseemh.sherlock.util.WebDriverUtil;


/**
 *
 * Facade for capturing elements and comparing screenshots.
 *
 */

public class Binoculars {

	/**
	 * WebDriver instance for capturing elements
	 */
	private WebDriver driver;

	/**
	 * Configuration
	 */
	private Configuration configuration;

	/**
	 * Screenshots manager
	 */
	private ScreenshotManager screenshotManager;

	/**
	 * Comparison task manager
	 */
	private ComparisonTaskManager comparisonTaskManager;

	public Binoculars(WebDriver driver) {
		this.driver = driver;
		this.configuration = new DefaultConfiguration();
		this.comparisonTaskManager = new ComparisonTaskManager(configuration);
		this.screenshotManager = new ScreenshotManager(driver,configuration);
	}

	public void capture(String screenshotName, String cssSelector) throws IOException {
		capture(screenshotName,By.cssSelector(cssSelector));
	}

	public void capture(String screenshotName, How howSelector, String selectorValue) throws IOException {
		capture(screenshotName,WebDriverUtil.bySelector(howSelector, selectorValue));
	}

	/**
	 *
	 * Capture an element in view and save screenshot.
	 * If flag Configuration.isCompareUponCapture is set to true, comparison will be done upon capture.
	 * Otherwise, comparison will be done on demand by invoking any comparison operation (compare, compareAll)
	 *
	 * @param screenshotName
	 * @param bySelector
     */
	public void capture(String screenshotName, By bySelector) {
		
		configuration.getReporter().reportBeforeCapture(screenshotName);
		
		boolean doCompare;
		try {
			doCompare = screenshotManager.takeScreenshot(bySelector, screenshotName);
		} catch (IOException e) {
			throw new BinocularsWrappedException(e);
		}
		configuration.getReporter().reportAfterCapture(screenshotName);

		//if this is a baseline image, then no comparison required
		if(!doCompare) {
			return;
		}

		//add comparison task to queue
		ComparisonTask task = new ComparisonTask(screenshotName,configuration);
		comparisonTaskManager.addTask(task);

		//if flag set to true, compare screenshot immediately
		if(configuration.isCompareUponCapture()) {
			compare(screenshotName);
		}

	}

	public void compareAll() {
		try{
			comparisonTaskManager.executeAllTasks();
		} 
		finally {
			//configuration.getReporter().generateReports();
		}
	}

	public void compare(String screenshotName) {
		try {
			comparisonTaskManager.executeTasks(screenshotName);
		} 
		finally {
			//configuration.getReporter().generateReports(null);
		}

	}

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}