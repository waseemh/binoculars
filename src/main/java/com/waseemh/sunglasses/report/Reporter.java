package com.waseemh.sunglasses.report;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.waseemh.sunglasses.configuration.Configuration;
import com.waseemh.sunglasses.events.SunglassesListener;
import com.waseemh.sunglasses.exceptions.SunglassesWrappedException;
import com.waseemh.sunglasses.exec.ComparisonResult;
import com.waseemh.sunglasses.report.OutputFactory.OutputType;

import freemarker.template.TemplateException;

public class Reporter {

	private List<SunglassesListener> listeners = new ArrayList<SunglassesListener>();
	private OutputFactory factory;
	private List<ReportModel> reportData = new ArrayList<ReportModel>();
	private Configuration configuration;
	
	public Reporter(Configuration configuration) {
		this.factory = new OutputFactory(configuration);
		addOutput(OutputType.CONSOLE); //default output: console
		this.configuration = configuration;
	}

	public void useOutput(OutputType... types) {
		listeners.clear();
		for(OutputType type : types) {
			SunglassesListener listener = factory.create(type);
			listeners.add(listener);
		}
	}
	
	public void addOutput(OutputType type) {
		listeners.add(factory.create(type));
	}

	public void reportBeforeCapture(String captureName) {
		for(SunglassesListener listener : listeners) {
			listener.beforeCapture(captureName);
		}
	}

	public void reportAfterCapture(String captureName) {
		for(SunglassesListener listener : listeners) {
			listener.afterCapture(captureName);
		}
	}

	public void reportBeforeCompare(String captureName) {
		for(SunglassesListener listener : listeners) {
			listener.beforeCompare(captureName);
		}
	}

	public void reportAfterCompare(String captureName) {
		for(SunglassesListener listener : listeners) {
			listener.afterCompare(captureName);
		}
	}

	public void reportOnSuccess(ComparisonResult result) {
		reportData.add(new ReportModel(result,true));
		for(SunglassesListener listener : listeners) {
			listener.onSuccess(result);
		}
	}

	public void reportOnFail(ComparisonResult result) {
		reportData.add(new ReportModel(result,false));
		for(SunglassesListener listener : listeners) {
			listener.onFail(result);
		}
	}
	
	public void generateOverview() {
		try {
			OverviewGenerator.generate(configuration,reportData);
		} catch (Exception e) {
			throw new SunglassesWrappedException(e);
		}
	}


}