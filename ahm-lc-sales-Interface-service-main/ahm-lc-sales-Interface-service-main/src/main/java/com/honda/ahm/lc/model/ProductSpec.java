package com.honda.ahm.lc.model;

public abstract class ProductSpec extends AuditEntry {
    private static final long serialVersionUID = 1L;

    private String modelYearCode;

    private String modelCode;

    private String modelTypeCode;

    private String modelOptionCode;

    public String getModelYearCode() {
        return modelYearCode;
    }

    public void setModelYearCode(String modelYearCode) {
        this.modelYearCode = modelYearCode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelTypeCode() {
        return modelTypeCode;
    }

    public void setModelTypeCode(String modelTypeCode) {
        this.modelTypeCode = modelTypeCode;
    }

    public String getModelOptionCode() {
        return modelOptionCode;
    }

    public void setModelOptionCode(String modelOptionCode) {
        this.modelOptionCode = modelOptionCode;
    }
}

