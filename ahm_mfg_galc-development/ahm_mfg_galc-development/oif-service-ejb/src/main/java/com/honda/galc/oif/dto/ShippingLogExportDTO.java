package com.honda.galc.oif.dto;

import com.honda.galc.util.OutputData;

public class ShippingLogExportDTO implements IOutputFormat{

	
	@OutputData(value="TRANS_CODE")
	private String transCode;
	
	@OutputData(value="DATE")
	private String date;
	
	@OutputData(value="TIME")
	private String time;
	
	@OutputData(value="LOC")
	private String loc;
	
	@OutputData(value="TRANS_TYPE")
	private String transType;
	
	@OutputData(value="VIN_PREFIX")
	private String vinPrefix;
	
	@OutputData(value="VIN_SERIAL")
	private String vinSerial;
	
	@OutputData(value="DATA")
	private String data;
	
	

	public String getTransCode() {
		return transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getVinPrefix() {
		return vinPrefix;
	}

	public void setVinPrefix(String vinPrefix) {
		this.vinPrefix = vinPrefix;
	}

	public String getVinSerial() {
		return vinSerial;
	}

	public void setVinSerial(String vinSerial) {
		this.vinSerial = vinSerial;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public String getProductId() {
		return this.vinPrefix +this.vinSerial;
	}

	@Override
	public String toString() {
		return "ShippingLogExportDTO [transCode=" + transCode + ", date=" + date + ", time=" + time + ", loc=" + loc
				+ ", transType=" + transType + ", vinPrefix=" + vinPrefix + ", vinSerial=" + vinSerial + ", data="
				+ data + "]";
	}
	
	
}
