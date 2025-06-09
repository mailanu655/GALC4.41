package com.honda.ahm.lc.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataContainer {

	@JsonProperty("PLANT_ID")
	private String plant_Id;
	
	@JsonProperty("LINE_ID")
	private String line_Id;
	
	@JsonProperty("PRODUCT_ID")
	private String product_Id;
	
	@JsonProperty("PRODUCT_SPEC_CODE")
	private String product_Spec_Code;
	
	@JsonProperty("PROCESS_POINT_ID")
	private String process_Point_Id;
	
	@JsonProperty("KD_LOT")
	private String kd_lot;
	
	@JsonProperty("PRODUCTION_LOT")
	private String production_lot;
	
	@JsonProperty("PURCHASE_CONTRACT_NUMBER")
	private String purchase_contract_number;
	
	@JsonProperty("PRINT_LOCATION")
	private String print_location;
	
	@JsonProperty("DELIMITER")
	private String delimiter;
	
	@JsonProperty("STATUS_TYPE")
	private String statusType;

	public String getProduct_Id() {
		return product_Id;
	}

	public void setProduct_Id(String productId) {
		this.product_Id = productId;
	}

	public String getProcess_Point_Id() {
		return process_Point_Id;
	}

	public void setProcessPointId(String processPointId) {
		this.process_Point_Id = processPointId;
	}

	public String getProduct_Spec_Code() {
		return product_Spec_Code;
	}

	public void setProduct_Spec_Code(String productSpecCode) {
		this.product_Spec_Code = productSpecCode;
	}

	public String getLine_Id() {
		return line_Id;
	}

	public void setLineId(String lineId) {
		this.line_Id = lineId;
	}

	public String getPlant_Id() {
		return plant_Id;
	}

	public void setPlant_Id(String siteId) {
		this.plant_Id = siteId;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getKd_lot() {
		return kd_lot;
	}

	public void setKd_lot(String kd_lot) {
		this.kd_lot = kd_lot;
	}

	public String getProduction_lot() {
		return production_lot;
	}

	public void setProduction_lot(String production_lot) {
		this.production_lot = production_lot;
	}

	public String getPurchase_contract_number() {
		return purchase_contract_number;
	}

	public void setPurchase_contract_number(String purchase_contract_number) {
		this.purchase_contract_number = purchase_contract_number;
	}

	public String getPrint_location() {
		return print_location;
	}

	public void setPrint_location(String print_location) {
		this.print_location = print_location;
	}

	public String getStatusType() {
		return statusType;
	}

	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	public void setLine_Id(String line_Id) {
		this.line_Id = line_Id;
	}

	public void setProcess_Point_Id(String process_Point_Id) {
		this.process_Point_Id = process_Point_Id;
	}

	@Override
	public String toString() {
		return "DataContainer [plant_Id=" + plant_Id + ", line_Id=" + line_Id + ", product_Id=" + product_Id
				+ ", product_Spec_Code=" + product_Spec_Code + ", process_Point_Id=" + process_Point_Id + ", kd_lot="
				+ kd_lot + ", production_lot=" + production_lot + ", purchase_contract_number="
				+ purchase_contract_number + ", print_location=" + print_location + ", delimiter=" + delimiter
				+ ", statusType=" + statusType + "]";
	}
	
	
}
