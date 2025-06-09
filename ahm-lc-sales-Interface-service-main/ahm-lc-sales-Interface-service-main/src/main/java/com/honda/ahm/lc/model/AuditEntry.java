package com.honda.ahm.lc.model;

public abstract class AuditEntry {

	private static final long serialVersionUID = 1L;
	
	
	private String createTimestamp;

	private String updateTimestamp;
	
	public String getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(String createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

}
