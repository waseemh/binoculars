package com.waseemh.sherlock.engines;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.engines.external.geotools.ImageComparator;
import com.waseemh.sherlock.exceptions.BinocularsWrappedException;
import com.waseemh.sherlock.exec.ComparisonResult;

public class ResembleJsEngine implements ImageComparisonEngine {

	private ImageComparator comparator;

	private Configuration config;
	
	private ComparisonMode mode = ComparisonMode.IgnoreAntialiasing;

	public ResembleJsEngine(Configuration config) {
		this.config=config;
	}

	public void setMode(ComparisonMode mode) {
		this.mode = mode;
	}
	
	public ComparisonMode getMode() {
		return mode;
	}

	public ComparisonResult compare(String captureName, File baseline, File imageUnderTest) {

		comparator = new ImageComparator();
		ComparisonResult result = new ComparisonResult();
		result.setCaptureName(captureName);

		RenderedImage image1;
		RenderedImage image2;
		try {
			image1 = ImageIO.read(baseline);
			image2 = ImageIO.read(imageUnderTest);
			comparator.compare(mode, image1, image2);
			generateDiffImage(captureName,imageUnderTest);
		}
		catch (Exception e) {
			throw new BinocularsWrappedException(e);
		}

		result.setMismatch(comparator.getMismatchPercent());
		return result;
	}

	private void generateDiffImage(String captureName, File imageFile) throws IOException {
		BufferedImage img = ImageIO.read(imageFile);
		for(Point point : comparator.getDiffPoints()) {
			img.setRGB((int) point.getX(), (int) point.getY(), config.getDiffColor().getRGB());
		}
		File outputfile = config.getResourceManager().getDiffImage(captureName);
		ImageIO.write(img, "png", outputfile);
	}

	public enum ComparisonMode{
		/**
		 * Checks if the images are equal taking into account the full color and all pixels. Some
		 * light difference between the two images is still being tolerated
		 */
		IgnoreNothing,
		/**
		 * Same as above, but if a pixel is found to be anti-aliased, only brightness will be
		 * compared, instead of the full color component
		 */
		IgnoreAntialiasing,
		/**
		 * Ignores the colors and compares only the brightness
		 */
		IgnoreColors
	}


}
