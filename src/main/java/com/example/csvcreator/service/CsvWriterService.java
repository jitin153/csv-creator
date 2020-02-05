package com.example.csvcreator.service;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.csvcreator.exception.DocumentCreatorException;
import com.example.csvcreator.util.Constants;

@Component("csvWriterService")
public class CsvWriterService{
	
	public byte[] getDocument(String processedText) {
		
		Document document = Jsoup.parse(processedText);
		Elements tables = document.getElementsByTag(Constants.HTML_TABLE);
		Writer writer = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			writer = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
			for (Element table : tables) {
				Elements rows = table.select(Constants.HTML_TABLE_ROW);
				if (!rows.isEmpty()) {
					int i = 0;
					Elements tableHeders = rows.select(Constants.HTML_TABLE_HEADER);
					if (!tableHeders.isEmpty()) {
						writeData(writer, tableHeders);
						i = 1;
					}
					for (; i < rows.size(); i++) {
						Elements cells = rows.get(i).select(Constants.HTML_TABLE_DATA);
						writeData(writer, cells);
					}
				}
				writer.write("\n");
			}
		} catch (Exception e) {
			System.out.println("Error occurred : " + e);
			throw new DocumentCreatorException("Error while creating csv!");
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				System.out.println("Error occurred : " + e);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	private void writeData(Writer writer, Elements cells) {
		try {
			for (Element cell : cells) {
				//writer.write(cell.text().concat(", ")); //--Text without double quotes.
				writer.write("\""+cell.text()+"\"".concat(", ")); //--Enclose text within double quotes.
			}
			writer.write("\n");
		} catch (Exception e) {
			System.out.println("Error occurred : " + e);
			throw new DocumentCreatorException("Error while creating csv!");
		}
	}
}
