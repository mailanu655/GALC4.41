package com.honda.galc.let.enums;

public enum LetStatus {
	
	Fail(0),
	Pass(1),
	Abort(2),
	Terminate(3),
	Untested(4);
	
	private int status;
	
	private LetStatus(int letStatus) {
		status = letStatus;
	}

	public int getStatus() {
		return status;
	}
}
