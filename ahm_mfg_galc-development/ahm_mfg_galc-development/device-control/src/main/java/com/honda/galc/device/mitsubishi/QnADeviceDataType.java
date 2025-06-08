package com.honda.galc.device.mitsubishi;

/**
 * 
 * <h3>QnADeviceDataType</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QnADeviceDataType description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Nov 5, 2010
 *
 */
public enum QnADeviceDataType {
	BOOLEAN(1, "Boolean"),
	SHORT(16, "Short"),
	INTEGER(32, "Integer"),
	FLOAT(32,"Float"),
	/*DOUBLE(64),*/
	STRING(-1, "String");
	
	private int bits;
	private String type;
	private QnADeviceDataType(int bits, String type) {
		this.bits = bits;
		this.type = type;
	}
	
	// Getters & Setters
	public int getBits() {
		return bits;
	}
	
	public void setBits(int bits) {
		this.bits = bits;
	}
	
	public String getType(){
		return type;
	}
	
	
	
	
	
}
