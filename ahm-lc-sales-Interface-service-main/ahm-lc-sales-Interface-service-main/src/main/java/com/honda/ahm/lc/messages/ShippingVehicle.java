package com.honda.ahm.lc.messages;

import java.util.List;

public class ShippingVehicle extends Vehicle  {

	String vin;

	String model_id;

	String model_type;

	String model_option;

	String color_code;

	String engine_number;

	String key_number;

	String issue_date;

	String adc_process_code;

	String product_lot_number;

	String kd_lot_number;

	String price;

	String assembly_off_date;

	String print_loc;

	String ccc_number;

	String parts_installed;

	String purchase_contract_number;

	List<String> fif_codes;

	String timestamp;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getModel_id() {
		return model_id;
	}

	public void setModel_id(String model_id) {
		this.model_id = model_id;
	}

	public String getModel_type() {
		return model_type;
	}

	public void setModel_type(String model_type) {
		this.model_type = model_type;
	}

	public String getModel_option() {
		return model_option;
	}

	public void setModel_option(String model_option) {
		this.model_option = model_option;
	}

	public String getColor_code() {
		return color_code;
	}

	public void setColor_code(String color_code) {
		this.color_code = color_code;
	}

	public String getEngine_number() {
		return engine_number;
	}

	public void setEngine_number(String engine_number) {
		this.engine_number = engine_number;
	}

	public String getKey_number() {
		return key_number;
	}

	public void setKey_number(String key_number) {
		this.key_number = key_number;
	}

	public String getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}

	public String getAdc_process_code() {
		return adc_process_code;
	}

	public void setAdc_process_code(String adc_process_code) {
		this.adc_process_code = adc_process_code;
	}

	public String getProduct_lot_number() {
		return product_lot_number;
	}

	public void setProduct_lot_number(String product_lot_number) {
		this.product_lot_number = product_lot_number;
	}

	public String getKd_lot_number() {
		return kd_lot_number;
	}

	public void setKd_lot_number(String kd_lot_number) {
		this.kd_lot_number = kd_lot_number;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAssembly_off_date() {
		return assembly_off_date;
	}

	public void setAssembly_off_date(String assembly_off_date) {
		this.assembly_off_date = assembly_off_date;
	}

	public String getPrint_loc() {
		return print_loc;
	}

	public void setPrint_loc(String print_loc) {
		this.print_loc = print_loc;
	}

	public String getCcc_number() {
		return ccc_number;
	}

	public void setCcc_number(String ccc_number) {
		this.ccc_number = ccc_number;
	}

	public String getParts_installed() {
		return parts_installed;
	}

	public void setParts_installed(String parts_installed) {
		this.parts_installed = parts_installed;
	}

	public String getPurchase_contract_number() {
		return purchase_contract_number;
	}

	public void setPurchase_contract_number(String purchase_contract_number) {
		this.purchase_contract_number = purchase_contract_number;
	}

	public List<String> getFif_codes() {
		return fif_codes;
	}

	public void setFif_codes(List<String> fif_codes) {
		this.fif_codes = fif_codes;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ShippingVehicle [vin=" + vin + ", model_id=" + model_id + ", model_type=" + model_type
				+ ", model_option=" + model_option + ", color_code=" + color_code + ", engine_number=" + engine_number
				+ ", key_number=" + key_number + ", issue_date=" + issue_date + ", adc_process_code=" + adc_process_code
				+ ", product_lot_number=" + product_lot_number + ", kd_lot_number=" + kd_lot_number + ", price=" + price
				+ ", assembly_off_date=" + assembly_off_date + ", print_loc=" + print_loc + ", ccc_number=" + ccc_number
				+ ", parts_installed=" + parts_installed + ", purchase_contract_number=" + purchase_contract_number
				+ ", fif_codes=" + fif_codes + ", timestamp=" + timestamp + "]";
	}
	
	
}
