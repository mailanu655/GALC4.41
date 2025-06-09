package com.honda.ahm.lc.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusMessage {
	
	@JsonProperty("TRANSACTION")
	Transaction transaction;
	
	@JsonProperty("VEHICLE")
	Vehicle vehicle;
	
	public StatusMessage() {
		
	}
	
	public StatusMessage(Transaction transaction, Vehicle vehicle) {
		super();
		this.transaction = transaction;
		this.vehicle = vehicle;
	}
		
	public Transaction getTransaction() {
		return transaction;
	}
	
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public Vehicle createVehicle() {
		return new StatusVehicle();
	}

	@Override
	public String toString() {
		return "StatusMessage [transaction=" + transaction + ", vehicle=" + vehicle + "]";
	}
	
	
}

