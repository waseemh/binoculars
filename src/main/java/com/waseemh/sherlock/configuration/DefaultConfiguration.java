package com.waseemh.sherlock.configuration;

import java.awt.Color;

import com.waseemh.sherlock.engines.ResembleJsEngine;
import com.waseemh.sherlock.exec.ComparisonResultHandler;
import com.waseemh.sherlock.exec.concurrent.ComparisonMultiThreadedExecutor;
import com.waseemh.sherlock.report.Reporter;

public class DefaultConfiguration extends Configuration {
	
	public DefaultConfiguration() {
		setRootFolder("binoculars");
		setScreenshotsFolder("screenshots");
		setFailuresFolder("failures");
		setBaselineFolder("baseline");
		setReportFolder("report");
		setEngine(new ResembleJsEngine(this));
		setBaselineExtension("baseline");
		setCaptureExtension("capture");
		setDiffExtension("diff");
		setFailExtension("fail");
		setMismatchThreshold(0.5);
		setCompareUponCapture(false);
		setDiffColor(Color.RED);
		setResourceManager(new ResourceManager(this));
		setExecutor(new ComparisonMultiThreadedExecutor(this));
		setHandler(new ComparisonResultHandler(this));
		setReporter((new Reporter(this)));
		setWaitDuration(30);
	}

}