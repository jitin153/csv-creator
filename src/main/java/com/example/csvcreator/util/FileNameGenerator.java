package com.example.csvcreator.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FileNameGenerator {
	private FileNameGenerator() {

	}

	public static String generateFileName(String prefix) {
		return new StringBuilder(prefix).append("_")
				.append(new SimpleDateFormat(Constants.TIMESTAMP).format(new Date())).append(Constants.FILE_EXTENSION).toString();
	}
}
