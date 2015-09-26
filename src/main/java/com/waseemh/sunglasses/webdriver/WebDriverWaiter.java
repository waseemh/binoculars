package com.waseemh.sunglasses.webdriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;

public abstract class WebDriverWaiter {

	Wait<WebDriver> waiter;

	public WebDriverWaiter(Wait<WebDriver> waiter) {
		this.waiter = waiter;
	}

	public abstract WebElement waitForElement(By byLocator);

	public static class VisibilityWebDriverWaiter extends WebDriverWaiter{

		public VisibilityWebDriverWaiter(Wait<WebDriver> waiter) {
			super(waiter);
		}

		@Override
		public WebElement waitForElement(final By byLocator) {
			WebElement element = waiter.until((ExpectedConditions.visibilityOfElementLocated(byLocator)));
			return element;
		}
	}

}
