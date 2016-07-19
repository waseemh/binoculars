package com.waseemh.sherlock.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.io.FileUtils;

import com.waseemh.sherlock.configuration.Configuration;
import com.waseemh.sherlock.exceptions.BinocularsWrappedException;


public class OutputFactory {

	Configuration configuration;

	public enum OutputType {
		TXT,HTML,CONSOLE;
	}

	public OutputFactory(Configuration configuration) {
		this.configuration = configuration;
	}

	public PrintOutput create(OutputType type) {
		PrintOutput output;
		PrintStream stream = null;
		switch (type) {
		case CONSOLE:
		default:
			stream = System.out;
			output = new ConsoleOutput(stream);
			break;
		case HTML:
			try {
				File reportFile = new File(configuration.getRootFolder()+File.separator+configuration.getReportFolder()+File.separator+"report.html");
				if(reportFile.exists()) {
					reportFile.delete();
				}
				FileOutputStream fos = FileUtils.openOutputStream(reportFile, true);
				stream = new PrintStream(fos);
			} catch (Exception e) {
				throw new BinocularsWrappedException(e);
			}
			output = new HtmlOutput(stream);
			break;
		}
		return output;
	}
}