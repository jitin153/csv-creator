package com.example.csvcreator.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.csvcreator.exception.DocumentCreatorException;
import com.example.csvcreator.model.DocumentRequest;
import com.example.csvcreator.util.Constants;
import com.example.csvcreator.util.FileNameGenerator;

@Service("documentGeneratorService")
public class DocumentCreatorService {

	@Autowired
	private FreemarkerTemplateProcessor freemarkerTemplateProcessor;

	@Autowired
	private CsvWriterService csvWriterService;

	public byte[] getDocumentAsByteArray(DocumentRequest documentRequest) {
		if (null != documentRequest) {
			String processedText = freemarkerTemplateProcessor.getProcessedText(documentRequest);
			if (null != processedText && processedText != "") {
				return csvWriterService.getDocument(processedText);
			} else {
				throw new DocumentCreatorException("Processed text cannot be blank.");
			}
		} else {
			throw new DocumentCreatorException("Document request cannot be null.");
		}
	}

	public void writeDocumentToDisk(String outputDirectory, String fileName, DocumentRequest documentRequest) {
		if (null != documentRequest) {
			if (null != fileName && fileName != "") {
				fileName = FileNameGenerator.generateFileName(fileName);
			} else {
				fileName = FileNameGenerator.generateFileName(Constants.FILE_NAME_PREFIX);
			}
			if (null != outputDirectory && outputDirectory != "") {
				byte[] document = getDocumentAsByteArray(documentRequest);
				StringBuilder file = new StringBuilder(outputDirectory).append("/").append(fileName);
				try {
					Files.write(new File(file.toString()).toPath(), document);
				} catch (IOException e) {
					System.out.println("Error occurred : " + e);
				}
			} else {
				throw new DocumentCreatorException("Output directory cannot be null or empty!");
			}
		} else {
			throw new DocumentCreatorException("DocumentRequest cannot be null.");
		}
	}
}
