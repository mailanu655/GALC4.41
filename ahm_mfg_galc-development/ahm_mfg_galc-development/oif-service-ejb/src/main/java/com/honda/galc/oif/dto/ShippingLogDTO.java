package com.honda.galc.oif.dto;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.enumtype.ShippingStatusEnum;
import com.honda.galc.util.GPCSData;

public class ShippingLogDTO {

	
	@GPCSData("TRANS_CODE")
	private String transCode;
	
	@GPCSData("DATE")
	private String date;
	
	@GPCSData("TIME")
	private String time;
	
	@GPCSData("LOC")
	private String loc;
	
	@GPCSData("TRANS_TYPE")
	private String transType;
	
	@GPCSData("VIN_PREFIX")
	private String vinPrefix;
	
	@GPCSData("VIN_SERIAL")
	private String vinSerial;
	
	@GPCSData("DATA")
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

	public Date getDateTime() {
		try {
			String dateTime = date+time;
			if(StringUtils.isNotEmpty(dateTime.trim())) {
				Date shippingDate=new SimpleDateFormat("yyMMddHHmmss").parse(dateTime.trim());
				return shippingDate;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public int getShippingStatus() {
		ShippingStatusEnum status = ShippingStatusEnum.valueOf(transType);
		return status != null?status.getStatus():0;
	}

	@Override
	public String toString() {
		return "ShippingLogDTO [transCode=" + transCode + ", date=" + date + ", time=" + time + ", loc=" + loc
				+ ", transType=" + transType + ", vinPrefix=" + vinPrefix + ", vinSerial=" + vinSerial + ", data="
				+ data + "]";
	}
	
	
}
