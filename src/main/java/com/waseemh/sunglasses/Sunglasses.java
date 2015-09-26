package com.waseemh.sunglasses;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.How;

import com.waseemh.sunglasses.configuration.Configuration;
import com.waseemh.sunglasses.configuration.DefaultConfiguration;
import com.waseemh.sunglasses.configuration.ScreenshotManager;
import com.waseemh.sunglasses.exceptions.SunglassesWrappedException;
import com.waseemh.sunglasses.exec.ComparisonTask;
import com.waseemh.sunglasses.exec.ComparisonTaskManager;
import com.waseemh.sunglasses.util.WebDriverUtil;


public class Sunglasses{

	private WebDriver driver;

	private Configuration configuration;

	private ScreenshotManager screenshotManager;

	private ComparisonTaskManager comparisonTaskManager;

	public Sunglasses(WebDriver driver) {
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

	public void capture(String screenshotName, By bySelector) {
		
		configuration.getReporter().reportBeforeCapture(screenshotName);
		
		boolean doCompare;
		try {
			doCompare = screenshotManager.takeScreenshot(bySelector, screenshotName);
		} catch (IOException e) {
			throw new SunglassesWrappedException(e);
		}
		configuration.getReporter().reportAfterCapture(screenshotName);
		
		if(!doCompare) //if this is a baseline image, then no comparison required
			return;
		
		ComparisonTask task = new ComparisonTask(screenshotName,configuration);
		comparisonTaskManager.addTask(task);
		
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