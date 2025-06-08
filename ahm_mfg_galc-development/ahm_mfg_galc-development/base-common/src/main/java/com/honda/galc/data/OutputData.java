package com.honda.galc.data;

public class OutputData {
	private Boolean checkResult;
	private String message;
	
	public OutputData(Boolean checkResult, String message) {
		super();
		this.checkResult = checkResult;
		this.message = message;
	}

	public OutputData() {
		super();		
	}

	public Boolean getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(Boolean checkResult) {
		this.checkResult = checkResult;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "OutputData [checkResult=" + checkResult + ", message=" + message + "]";
	}
	
	
}
