package com.honda.galc.qics.mobile.client.events;

public class DataAccessErrorEventData {
	private String message = null;
	private Throwable exception = null;
	
	public DataAccessErrorEventData(String message, Throwable exception) {
		this.message = message;
		this.exception = exception;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Throwable getException() {
		return exception;
	}
	
	public void setException(Throwable exception) {
		this.exception = exception;
	}

}
