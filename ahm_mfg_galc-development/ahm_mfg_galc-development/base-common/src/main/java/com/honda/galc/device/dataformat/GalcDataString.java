package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.IDeviceDataInput;

/**
 * 
 * <h3>GalcDataString</h3>
 * <h3> This class encapsulate data send from GALC to PLC  </h3>
 * <h4> Description </h4>
 * <p> GalcDataString description </p>
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
public class GalcDataString implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	private String galcDataString;
	
	public GalcDataString(String galsDataString) {
		super();
		this.galcDataString = galsDataString;
	}
	
	public GalcDataString() {
		super();
	}
	
	public String getGalcDataString() {
		return galcDataString;
	}

	public void setGalcDataString(String plcDataString) {
		this.galcDataString = plcDataString;
	}

}
