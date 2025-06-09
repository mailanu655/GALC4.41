package com.honda.ahm.lc.messages;

public class ShippingMessage {

	Transaction transaction;
	
	Vehicle vehicle;

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

	@Override
	public String toString() {
		return "ShippingMessage [transaction=" + transaction + ", vehicle=" + vehicle + "]";
	}
	
		
}