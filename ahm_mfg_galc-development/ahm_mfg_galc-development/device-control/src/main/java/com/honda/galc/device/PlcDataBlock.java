package com.honda.galc.device;


/**
 * 
 * <h3>PlcDataBlock</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcDataBlock description </p>
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
public class PlcDataBlock implements IPlcData{
	private String address;
	private byte[] data;

	public PlcDataBlock() {
		super();
	}

	public PlcDataBlock(String address, byte[] data) {
		super();
		this.address = address;
		this.data = data;
	}
	
	//Getters && Setters
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
