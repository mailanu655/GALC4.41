package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceDataInput;

/**
 * 
 * <h3>PlcDataString</h3>
 * <h3> This class encapsulate data send from GALC to PLC  </h3>
 * <h4> Description </h4>
 * <p> PlcDataString description </p>
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
 * Mar. 5, 2019
 *
 */
public class PlcDataString implements IDeviceDataInput, Serializable{
	private static final long serialVersionUID = 1L;
	
	private String plcDataString;

	public PlcDataString() {
		super();
	}
	
	public PlcDataString(String plcDataString) {
		super();
		this.plcDataString = plcDataString;
	}

	public String getPlcDataString() {
		return plcDataString;
	}

	public void setPlcDataString(String plcDataString) {
		this.plcDataString = plcDataString;
	}

}
