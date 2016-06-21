package com.waseemh.sherlock;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.waseemh.sherlock.engines.ResembleJsEngine;
import com.waseemh.sherlock.engines.ResembleJsEngine.ComparisonMode;
import com.waseemh.sherlock.report.OutputFactory.OutputType;
import com.waseemh.sherlock.webdriver.WebDriverWaiter.VisibilityWebDriverWaiter;

public class MyTest {

	static WebDriver driver;
	static Sherlock sherlock;

	@BeforeClass
	public static void before() {
		driver = new FirefoxDriver();
		sherlock = new Sherlock(driver);
		sherlock.getConfiguration().setRootFolder("sherlock");
		sherlock.getConfiguration().getReporter().useOutput(OutputType.CONSOLE,OutputType.HTML);
		sherlock.getConfiguration().setWaiter(new VisibilityWebDriverWaiter(new WebDriverWait(driver,10)));
		((ResembleJsEngine) sherlock.getConfiguration().getEngine()).setMode(ComparisonMode.IgnoreAntialiasing);
	
		driver.get("http://yahoo.com");
	}

	@Test
	public void test1() {
		sherlock.capture("logo", By.id("uh-logo"));
		sherlock.compare("logo");
	}
	
	@Test
	public void test2() {
		sherlock.capture("top bar",By.className("view_default"));
		sherlock.compare("top bar");
	}
	
	@AfterClass
	public static void teardown() {
		sherlock.getConfiguration().getReporter().generateOverview();
		sherlock.close();
		driver.quit();
	}

}