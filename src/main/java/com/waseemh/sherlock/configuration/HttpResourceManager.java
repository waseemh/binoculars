package com.waseemh.sherlock.configuration;

import com.waseemh.sherlock.exceptions.BinocularsWrappedException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by waseemh on 7/20/16.
 */
public class HttpResourceManager extends ResourceManager {

    public HttpResourceManager(Configuration configuration, URL url) {
        super(configuration);
        this.url = url;
    }

    private Configuration configuration;

    private URL url;

    public File getBaselineImage(String captureName) {
        String fullImageUrl = url.toExternalForm()
                + "/"
                + configuration.getRootFolder()
                + "/"
                + configuration.getBaselineFolder()
                + "/"
                + captureName + "."
                + configuration.getBaselineExtension() + ".png";
        try {
            URL imageUrl = new URL(fullImageUrl);
            BufferedImage img = ImageIO.read(imageUrl);
            File baselineImage = File.createTempFile("binoculars-tmp-screenshot",".png");
            ImageIO.write(img, "png", baselineImage);
            return baselineImage;
        } catch (Exception e) {
            throw new BinocularsWrappedException("Error while reading image file",e);
        }

    }

    public File getCaptureImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getCaptureExtension()+".png");
        return captureImage;
    }

    public File getDiffImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getScreenshotsFolder()+File.separator+captureName+"."+configuration.getDiffExtension()+".png");
        return captureImage;
    }

    public File getFailImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder()+File.separator+configuration.getFailuresFolder()+File.separator+captureName+"."+configuration.getFailExtension()+".png");
        return captureImage;
    }
}
