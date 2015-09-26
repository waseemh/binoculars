package com.waseemh.sunglasses.util;

import org.openqa.selenium.By;
import org.openqa.selenium.support.How;

public class WebDriverUtil {
	
	public static By bySelector(How how, String value) {
		switch(how) {
		case CLASS_NAME:
			return By.className(value);
		case CSS:
			return By.cssSelector(value);
		case ID:
			return By.id(value);
		case ID_OR_NAME:
			return null;
		case LINK_TEXT:
			return By.linkText(value);
		case NAME:
			return By.name(value);
		case PARTIAL_LINK_TEXT:
			return By.partialLinkText(value);
		case TAG_NAME:
			return By.tagName(value);
		case XPATH:
			return By.xpath(value);
		default:
			return null;
		}
	}

}
