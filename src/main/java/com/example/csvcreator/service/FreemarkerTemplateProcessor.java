package com.example.csvcreator.service;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.csvcreator.exception.DocumentCreatorException;
import com.example.csvcreator.model.DocumentRequest;
import com.example.csvcreator.util.Constants;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

@Component("freemarkerTemplateProcessor")
public class FreemarkerTemplateProcessor {

	public String getProcessedText(DocumentRequest documentRequest) {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
		configuration.setDefaultEncoding(Constants.DEFAULT_ENCODING);
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(false);
		if (null != documentRequest.getTemplateName() && documentRequest.getTemplateName() != "") {
			this.processTemplateFromFile(configuration, Constants.TEMPLATE_DIRECTORY, documentRequest.getTemplateName());
		} else {
			throw new DocumentCreatorException("Template name cannot be blank.");
		}
		String processedText = "";
		Template template;
		try {
			template = configuration.getTemplate(documentRequest.getTemplateName());
			processedText = processTemplate(template, Constants.CONTEXT, documentRequest.getData());
		} catch (TemplateNotFoundException e) {
			System.out.println("Error occurred : " + e);
		} catch (MalformedTemplateNameException e) {
			System.out.println("Error occurred : " + e);
		} catch (ParseException e) {
			System.out.println("Error occurred : " + e);
		} catch (IOException e) {
			System.out.println("Error occurred : " + e);
		}
		return processedText;
	}

	private void processTemplateFromString(Configuration configuration, String templateName, String templateText) {
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate(templateName, templateText);
		configuration.setTemplateLoader(stringTemplateLoader);
	}

	private void processTemplateFromFile(Configuration configuration, String templateDirectory, String templateName) {
		try {
			configuration.setDirectoryForTemplateLoading(new File(templateDirectory + "/"));
		} catch (IOException e) {
			System.out.println("Error occurred : " + e);
		}
	}

	private String processTemplate(Template template, String contextName, Object data) {
		String processedText = "";
		try {
			Map<String, Object> root = new HashMap<String, Object>();
			root.put(contextName, data);
			Writer writer = new StringWriter();
			try {
				template.process(root, writer);
				processedText = processTemplateIntoString(template, root);
			} catch (TemplateException e) {
				System.out.println("Error occurred : " + e);
			}
		} catch (IOException e) {
			System.out.println("Error occurred : " + e);
		}
		return processedText;
	}

	private static String processTemplateIntoString(Template template, Object model)
			throws IOException, TemplateException {
		StringWriter result = new StringWriter();
		template.process(model, result);
		return result.toString();
	}
}
