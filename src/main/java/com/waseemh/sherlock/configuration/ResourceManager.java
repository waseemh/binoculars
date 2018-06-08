package com.waseemh.sherlock.configuration;


import java.io.File;
import java.io.IOException;

import static com.google.common.io.Files.copy;

/**
 * Manages and loads all captured screenshots
 */

public class ResourceManager {

    private Configuration configuration;

    public ResourceManager(Configuration configuration) {
        this.configuration = configuration;
    }

    public File getBaselineImage(String captureName) {
        File baselineImage = new File(configuration.getRootFolder() + File.separator + configuration.getBaselineFolder() + File.separator + captureName + "." + configuration.getBaselineExtension() + ".png");
        return baselineImage;
    }

    public boolean isBaselineImageExists(String captureName) {
        File baseline = getBaselineImage(captureName);
        return baseline.exists();
    }

    public void writeBaseline(File newBaselineImage, String captureName) {
        try {
            copy(newBaselineImage, getBaselineImage(captureName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getCaptureImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder() + File.separator + configuration.getScreenshotsFolder() + File.separator + captureName + "." + configuration.getCaptureExtension() + ".png");
        return captureImage;
    }

    public void writeCaptureImage(File newCaptureImage, String captureName) {
        try {
            copy(newCaptureImage, getCaptureImage(captureName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getDiffImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder() + File.separator + configuration.getScreenshotsFolder() + File.separator + captureName + "." + configuration.getDiffExtension() + ".png");
        return captureImage;
    }

    public void writeDiffImage(File diffImage, String captureName) {

    }

    public File getFailImage(String captureName) {
        File captureImage = new File(configuration.getRootFolder() + File.separator + configuration.getFailuresFolder() + File.separator + captureName + "." + configuration.getFailExtension() + ".png");
        return captureImage;
    }

    public void writeFailImage(File failImage, String captureName) {

    }

}
