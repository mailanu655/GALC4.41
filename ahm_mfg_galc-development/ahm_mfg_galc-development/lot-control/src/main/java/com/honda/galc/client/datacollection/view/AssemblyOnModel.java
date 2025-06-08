package com.honda.galc.client.datacollection.view;

public class AssemblyOnModel {

	private String sequence;
	private String vin;
	private String mtoci;
	private String kdLot;
	private String comments;
	
	public String getSequence() {
		return sequence;
	}
	public String getVin() {
		return vin;
	}
	public String getMtoci() {
		return mtoci;
	}
	public String getKdLot() {
		return kdLot;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public void setMtoci(String mtoci) {
		this.mtoci = mtoci;
	}
	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
