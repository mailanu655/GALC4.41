package com.honda.ahm.lc.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusVehicle extends Vehicle {
	
	@JsonProperty("FLAG")
    String flag;

	@JsonProperty("VIN")
    String vin;

	@JsonProperty("SHIP_DATE")
    String ship_date;

	@JsonProperty("PARKING_BAY")
    String parking_bay;

	@JsonProperty("PARKING_SPACE")
    String parking_space;

	@JsonProperty("CONTROL_NUMBER")
    String control_number;

	@JsonProperty("DEALER_NUMBER")
    String dealer_number;

	@JsonProperty("SHIP_TYPE")
    String ship_type;

	//@JsonProperty("ODC_CODE")
   // String odc_code;

	@JsonProperty("TIMESTAMP")
    String timestamp;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVin() {
        return vin.toUpperCase();
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }

    public String getParking_bay() {
        return parking_bay;
    }

    public void setParking_bay(String parking_bay) {
        this.parking_bay = parking_bay;
    }

    public String getParking_space() {
        return parking_space;
    }

    public void setParking_space(String parking_space) {
        this.parking_space = parking_space;
    }

    public String getControl_number() {
        return control_number;
    }

    public void setControl_number(String control_number) {
        this.control_number = control_number;
    }

    public String getDealer_number() {
        return dealer_number;
    }

    public void setDealer_number(String dealer_number) {
        this.dealer_number = dealer_number;
    }

    public String getShip_type() {
        return ship_type;
    }

    public void setShip_type(String ship_type) {
        this.ship_type = ship_type;
    }

    /*public String getOdc_code() {
        return odc_code;
    }

    public void setOdc_code(String odc_code) {
        this.odc_code = odc_code;
    }*/

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "StatusVehicle [flag=" + flag + ", vin=" + vin + ", ship_date=" + ship_date + ", parking_bay="
				+ parking_bay + ", parking_space=" + parking_space + ", control_number=" + control_number
				+ ", dealer_number=" + dealer_number + ", ship_type=" + ship_type + ", odc_code=" //+ odc_code
				+ ", timestamp=" + timestamp + "]";
	}

    
}
