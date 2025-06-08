package com.honda.galc.constant;

public enum RevisionStatus {
	MAPPING("MAPPING"),
	DEVELOPING("DEVELOPING"),
	APPROVED("APPROVED"),
	INVALID("INVALID"),
	BATCH_PENDING("BATCH_PENDING");
	
	private String revStatus;
	
	private RevisionStatus(String revStatus) {
		this.setRevStatus(revStatus);
	}

	public String getRevStatus() {
		return revStatus;
	}

	public void setRevStatus(String revStatus) {
		this.revStatus = revStatus;
	}
}
