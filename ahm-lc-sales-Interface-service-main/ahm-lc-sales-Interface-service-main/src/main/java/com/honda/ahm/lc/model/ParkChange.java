package com.honda.ahm.lc.model;

public class ParkChange extends AuditEntry{

	private static final long serialVersionUID = 1L;
	
	private String vin;
	
	private String date;
	
	private String time;
	
	private String sendLocation;
	
	private String transactiontype;
	
	private String parkingLocation;
	
	private String parkControlNumber;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
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

	public String getSendLocation() {
		return sendLocation;
	}

	public void setSendLocation(String sendLocation) {
		this.sendLocation = sendLocation;
	}

	public String getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	public String getParkingLocation() {
		return parkingLocation;
	}

	public void setParkingLocation(String parkingLocation) {
		this.parkingLocation = parkingLocation;
	}

	public String getParkControlNumber() {
		return parkControlNumber;
	}

	public void setParkControlNumber(String parkControlNumber) {
		this.parkControlNumber = parkControlNumber;
	}
	
	
	
}
