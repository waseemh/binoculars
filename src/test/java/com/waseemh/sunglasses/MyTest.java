package com.waseemh.sunglasses;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.waseemh.sunglasses.engines.ResembleJsEngine;
import com.waseemh.sunglasses.engines.ResembleJsEngine.ComparisonMode;
import com.waseemh.sunglasses.report.OutputFactory.OutputType;
import com.waseemh.sunglasses.webdriver.WebDriverWaiter.VisibilityWebDriverWaiter;

public class MyTest {

	static WebDriver driver;
	static Sunglasses sg;

	@BeforeClass
	public static void before() {
		driver = new FirefoxDriver();
		sg = new Sunglasses(driver);
		sg.getConfiguration().setRootFolder("C:\\sunglasses");
		sg.getConfiguration().getReporter().useOutput(OutputType.CONSOLE,OutputType.HTML);
		sg.getConfiguration().setWaiter(new VisibilityWebDriverWaiter(new WebDriverWait(driver,10)));
		((ResembleJsEngine) sg.getConfiguration().getEngine()).setMode(ComparisonMode.IgnoreAntialiasing);
	
		driver.get("http://yahoo.com");
	}

	@Test
	public void test1() {
		sg.capture("logo", By.id("yucs-logo-ani"));
		sg.compare("logo");
	}
	
	@Test
	public void test2() {
		sg.capture("top bar",By.className("view_default"));
		sg.compare("top bar");
	}
	
	@AfterClass
	public static void teardown() {
		sg.getConfiguration().getReporter().generateOverview();
		sg.close();
		driver.quit();
	}

}