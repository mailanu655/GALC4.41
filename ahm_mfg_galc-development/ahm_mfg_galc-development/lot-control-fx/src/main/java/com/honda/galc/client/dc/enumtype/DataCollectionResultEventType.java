package com.honda.galc.client.dc.enumtype;

import com.honda.galc.enumtype.IdEnum;

/**
 * @author Subu Kathiresan
 * @date Jun 18, 2014
 */
public enum DataCollectionResultEventType implements IdEnum <DataCollectionResultEventType> {

	VALID_PART_SCAN_RECEIVED		(1, "VALID_PART_SCAN_RECEIVED"),
	INVALID_PART_SCAN_RECEIVED		(2, "INVALID_PART_SCAN_RECEIVED"),
	VALID_MEASUREMENT_RECEIVED		(3, "VALID_MEASUREMENT_RECEIVED"),
	INVALID_MEASUREMENT_RECEIVED	(4, "INVALID_MEASUREMENT_RECEIVED"),
	SKIP_PART_SCAN_RECEIVED			(5, "SKP_PART_SCAN_RECEIVED"),
	SKIP_MEASUREMENT_RECEIVED		(6, "SKIP_MEASUREMENT_RECEIVED"),
	REJECT_PART_SCAN_RECEIVED		(7, "REJECT_PART_SCAN_RECEIVED"),
	REJECT_MEASUREMENT_RECEIVED		(8, "REJECT_MEASUREMENT_RECEIVED"),
	DC_COMPLETED_FOR_PART			(9, "DC_COMPLETED_FOR_PART"),
	DC_REJECTED_FOR_PART			(10, "DC_REJECTED_FOR_PART"),
	DC_ERROR_REPORTED				(11, "DC_ERROR_REPORTED");;

	private int id = 1;
	private String name = "";
	private String message = "";

	private DataCollectionResultEventType(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	private DataCollectionResultEventType(int id, String name, String message){
		this.id = id;
		this.name = name;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
