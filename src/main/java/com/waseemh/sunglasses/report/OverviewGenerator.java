package com.waseemh.sunglasses.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class OverviewGenerator {

	public static void generate(com.waseemh.sunglasses.configuration.Configuration configuration, List<ReportModel> reportData) throws TemplateException, IOException {

		Map<String, Object> root = new HashMap<String, Object>();
		//put builds list to template
		root.put("data",reportData);
		root.put("resourceManager", configuration.getResourceManager());
		//put template util static class to template
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
//		TemplateHashModel staticModels = wrapper.getStaticModels();
//		TemplateHashModel templateUtil = (TemplateHashModel) staticModels.get("com.");  
//		root.put("TemplateUtil", templateUtil);

		//process template
		FileWriter out = new FileWriter(configuration.getRootFolder() + File.separator + configuration.getReportFolder()+File.separator+"overview.html");
		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(OverviewGenerator.class, "templates");
		Template template = cfg.getTemplate("overview.ftl");
		template.process(root,out);
		out.flush();

	}

}
