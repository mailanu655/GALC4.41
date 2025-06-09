package com.honda.ahm.lc.model;

public class QiDefectResult extends AuditEntry {

    private long defectResultId;
    private String defectTypeName;

    public long getDefectResultId() {
        return defectResultId;
    }

    public String getDefectTypeName() {
        return defectTypeName;
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }

    public QiDefectResult() {
    }

    public void setDefectResultId(long defectResultId) {
        this.defectResultId = defectResultId;
    }
}
