package com.waseemh.sherlock;

import com.waseemh.sherlock.engines.ResembleJsEngine;
import com.waseemh.sherlock.engines.ResembleJsEngine.ComparisonMode;
import com.waseemh.sherlock.report.OutputFactory.OutputType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MyTest {

	static WebDriver driver;
	static Binoculars binoculars;

	@BeforeClass
	public static void before() {
		driver = new FirefoxDriver();
		binoculars = new Binoculars(driver);
		binoculars.getConfiguration().setRootFolder("binoculars");
		binoculars.getConfiguration().getReporter().useOutput(OutputType.CONSOLE,OutputType.HTML);
		((ResembleJsEngine) binoculars.getConfiguration().getEngine()).setMode(ComparisonMode.IgnoreAntialiasing);
	
		driver.get("http://yahoo.com");
	}

	@Test
	public void test1() {
		binoculars.capture("logo", By.id("uh-logo"));
		binoculars.compare("logo");
	}
	
	@Test
	public void test2() {
		binoculars.capture("top bar",By.className("view_default"));
		binoculars.compare("top bar");
	}
	
	@AfterClass
	public static void teardown() {
		binoculars.getConfiguration().getReporter().generateOverview();
		binoculars.close();
		driver.quit();
	}

}