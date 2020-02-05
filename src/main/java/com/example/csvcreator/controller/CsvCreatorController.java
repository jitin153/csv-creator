package com.example.csvcreator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.csvcreator.model.DocumentRequest;
import com.example.csvcreator.service.DocumentCreatorService;
import com.example.csvcreator.util.Constants;

@RestController
public class CsvCreatorController {
	
	@Autowired
	private DocumentCreatorService documentGeneratorService;
	
	@PostMapping("/createcsv")
	public void createCSV(@RequestBody DocumentRequest documentRequest) {
		try {
			documentGeneratorService.writeDocumentToDisk(Constants.OUTPUT_DIRECTORY, Constants.DEFAULT_FILE_NAME, documentRequest);
			System.out.println("File has been created...");
		}catch(Exception e) {
			System.out.println("Error occurred : "+e);
		}
	}
}
