package com.waseemh.sherlock.report;

import java.util.ArrayList;
import java.util.List;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.events.EventListener;
import com.waseemh.sherlock.exceptions.SherlockWrappedException;
import com.waseemh.sherlock.exec.ComparisonResult;
import com.waseemh.sherlock.report.OutputFactory.OutputType;

public class Reporter {

	private List<EventListener> listeners = new ArrayList<EventListener>();
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
			EventListener listener = factory.create(type);
			listeners.add(listener);
		}
	}
	
	public void addOutput(OutputType type) {
		listeners.add(factory.create(type));
	}

	public void reportBeforeCapture(String captureName) {
		for(EventListener listener : listeners) {
			listener.beforeCapture(captureName);
		}
	}

	public void reportAfterCapture(String captureName) {
		for(EventListener listener : listeners) {
			listener.afterCapture(captureName);
		}
	}

	public void reportBeforeCompare(String captureName) {
		for(EventListener listener : listeners) {
			listener.beforeCompare(captureName);
		}
	}

	public void reportAfterCompare(String captureName) {
		for(EventListener listener : listeners) {
			listener.afterCompare(captureName);
		}
	}

	public void reportOnSuccess(ComparisonResult result) {
		reportData.add(new ReportModel(result,true));
		for(EventListener listener : listeners) {
			listener.onSuccess(result);
		}
	}

	public void reportOnFail(ComparisonResult result) {
		reportData.add(new ReportModel(result,false));
		for(EventListener listener : listeners) {
			listener.onFail(result);
		}
	}
	
	public void generateOverview() {
		try {
			OverviewGenerator.generate(configuration,reportData);
		} catch (Exception e) {
			throw new SherlockWrappedException(e);
		}
	}


}