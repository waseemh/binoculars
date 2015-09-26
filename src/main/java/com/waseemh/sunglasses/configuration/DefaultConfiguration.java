package com.waseemh.sunglasses.configuration;

import java.awt.Color;

import com.waseemh.sunglasses.engines.ResembleJsEngine;
import com.waseemh.sunglasses.exec.ComparisonResultHandler;
import com.waseemh.sunglasses.exec.concurrent.ComparisonMultiThreadedExecutor;
import com.waseemh.sunglasses.report.Reporter;

public class DefaultConfiguration extends Configuration {
	
	public DefaultConfiguration() {
		setRootFolder("sunglasses");
		setScreenshotsFolder("screenshots");
		setFailuresFolder("failures");
		setReportFolder("report");
		setEngine(new ResembleJsEngine(this));
		setBaselineExtension("baseline");
		setCaptureExtension("capture");
		setDiffExtension("diff");
		setFailExtension("fail");
		setMissmatchThreshold(0.5);
		setCompareUponCapture(false);
		setDiffColor(Color.RED);
		setResourceManager(new ResourceManager(this));
		setExecutor(new ComparisonMultiThreadedExecutor(this));
		setHandler(new ComparisonResultHandler(this));
		setReporter((new Reporter(this)));
	}

}