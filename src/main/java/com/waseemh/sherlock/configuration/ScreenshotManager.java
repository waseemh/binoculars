package com.waseemh.sherlock.configuration;

import com.waseemh.sherlock.exceptions.BinocularsWrappedException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ScreenshotManager {

    private WebDriver driver;
    private Configuration configuration;

    public ScreenshotManager(WebDriver driver, Configuration configuration) {
        this.driver = driver;
        this.configuration = configuration;
    }

    private boolean takeScreenshot(WebElement element, String screenshotName) throws IOException {

        //flag for indicating if screenshot comparison should be done
        boolean doCompare;

        //Get the location of element on the page
        Point point = element.getLocation();

        //scroll to the view before taking the screenshot of the view
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("javascript:window.scrollTo(" + String.format("%d,%d", point.x, point.y) + ")");

        //read view screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = ImageIO.read(screenshot);


        //Get element dimension
        int eleWidth = element.getSize().getWidth();
        int eleHeight = element.getSize().getHeight();

        //Crop the entire view screenshot to get only element screenshot
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);

        //check if baseline image doesn't exist (no comparison needed)
        //or we are in baseline mode
        if (!configuration.getResourceManager().isBaselineImageExists(screenshotName) || configuration.isBaselineMode()) {
            configuration.getResourceManager().writeBaseline(screenshot, screenshotName);
            doCompare = false;
        } else {
            configuration.getResourceManager().writeCaptureImage(screenshot, screenshotName);
            doCompare = true;
        }

        return doCompare;
    }

    public boolean takeScreenshot(By by, String screenshotName) throws IOException {

        FluentWait<WebDriver> waiter = new FluentWait<WebDriver>(driver);
        waiter.withTimeout(configuration.getWaitDuration(), TimeUnit.SECONDS);
        try {
            WebElement element = waiter.until((ExpectedConditions.visibilityOfElementLocated(by)));
            return takeScreenshot(element, screenshotName);
        } catch (TimeoutException e) {
            throw new BinocularsWrappedException("Unable to find element using locator: " + by, e);
        }

    }

}