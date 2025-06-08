package com.honda.galc.client.ui;

public class ElevatedLoginResult {

	private boolean isSuccessful;
	private String userId;
	private String message;

	public ElevatedLoginResult(boolean isSuccessful, String userId, String message) {
		this.isSuccessful = isSuccessful;
		this.userId = userId;
		this.message = message;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
