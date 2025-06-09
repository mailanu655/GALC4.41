package com.honda.ahm.lc.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

	@JsonProperty("DESTINATION_ENVIRONMENT")
	String destination_environment;

	@JsonProperty("DESTINATION_SITE")
	String destination_site;

	@JsonProperty("PLANT_ID")
	String plant_id;

	@JsonProperty("LINE_ID")
	String line_id;

	@JsonProperty("TRANSACTION_CODE")
	String transaction_code;

	@JsonProperty("DESCRIPTION")
	String description;

	@JsonProperty("TRANSACTION_TIMESTAMP")
	String transaction_timestamp;

	public String getDestination_environment() {
		return destination_environment.toUpperCase();
	}

	public void setDestination_environment(String destination_environment) {
		this.destination_environment = destination_environment;
	}

	public String getDestination_site() {
		return destination_site.toUpperCase();
	}

	public void setDestination_site(String destination_site) {
		this.destination_site = destination_site;
	}

	public String getPlant_id() {
		return plant_id.toUpperCase();
	}

	public void setPlant_id(String plant_id) {
		this.plant_id = plant_id;
	}

	public String getLine_id() {
		return line_id;
	}

	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}

	public String getTransaction_code() {
		return transaction_code.toUpperCase();
	}

	public void setTransaction_code(String transaction_code) {
		this.transaction_code = transaction_code;
	}

	public String getDescription() {
		return description.toUpperCase();
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransaction_timestamp() {
		return transaction_timestamp;
	}

	public void setTransaction_timestamp(String transaction_timestamp) {
		this.transaction_timestamp = transaction_timestamp;
	}

	@Override
	public String toString() {
		return "Transaction [destination_environment=" + destination_environment + ", destination_site="
				+ destination_site + ", plant_id=" + plant_id + ", line_id=" + line_id + ", transaction_code="
				+ transaction_code + ", description=" + description + ", transaction_timestamp=" + transaction_timestamp
				+ "]";
	}
	
	
}
