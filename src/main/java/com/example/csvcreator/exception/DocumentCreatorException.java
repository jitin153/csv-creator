package com.example.csvcreator.exception;

public class DocumentCreatorException extends RuntimeException {

	public DocumentCreatorException(String message, Throwable cause) {
		super(message);
	}

	public DocumentCreatorException(String message) {
		super(message);
	}
}
