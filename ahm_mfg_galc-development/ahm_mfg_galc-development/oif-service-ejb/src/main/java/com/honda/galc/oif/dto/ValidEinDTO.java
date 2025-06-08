package com.honda.galc.oif.dto;

import java.sql.Timestamp;

import com.honda.galc.util.OutputData;

public class ValidEinDTO implements IOutputFormat {

	@OutputData("ENGINE_ID")
	private String engineId;
	@OutputData("MODEL_YEAR")
	private String modelYear;
	@OutputData("MODEL_CODE")
	private String modelCode;
	@OutputData("MODEL_TYPE_CODE")
	private String modelTypeCode;
	@OutputData("MODEL_OPTION_CODE")
	private String modelOptionCode;
	@OutputData("TIMESTAMP")
	private Timestamp timestamp;
	@OutputData("PREFIX")
	private String prefix;

	public ValidEinDTO() {
		super();
	}

	public String getEngineId() {
		return engineId;
	}

	public void setEngineId(String engineId) {
		this.engineId = engineId;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setModelYear(String modelYear) {
		this.modelYear = modelYear;
	}

	public String getModelYear() {
		return modelYear;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("ValidEinDTO:");
		result.append("\nengineId: " + engineId);
		result.append("\nmodelYear: " + modelYear);
		result.append("\nmodelCode: " + modelCode);
		result.append("\nmodelTypeCode: " + modelTypeCode);
		result.append("\nmodelOptionCode: " + modelOptionCode);
		result.append("\nprefix: " + prefix);
		result.append("\ntimestamp: " + timestamp);
		return result.toString();
	}

}