package com.honda.galc.common.exception;

public class InputValidationException extends TaskException {
	
	private static final long serialVersionUID = 1L;
	
	public InputValidationException() {
		super(null);
	}
	
	public InputValidationException(String message) {
		super(message);
	}
	
	public InputValidationException(String message, String taskName) {
		super(message, taskName);
	}
	
	public InputValidationException(String message, Throwable detail) {
		super(message, detail);
	}
	
	public InputValidationException(String message, Throwable detail, String taskName) {
		super(message, taskName, detail);
	}
}
