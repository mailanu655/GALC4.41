package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;

/**
 * 
 * 
 * <h3>MCNubmer Class description</h3>
 * <p> MCNubmer description </p>
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
 * @author Jeffray Huang<br>
 * May 16, 2014
 *
 *
 */
public class MCNumber implements IDeviceData, Serializable{
	private static final long serialVersionUID = 1L;
	
	@Tag(name="ALMCB_NUMBER")
	private String mcNumber;

	public MCNumber(){
	}
	
	public MCNumber(String mcNumber){
		this.mcNumber = mcNumber;
	}
	
	public String getMcNumber() {
		return mcNumber;
	}

	public void setMcNumber(String mcNumber) {
		this.mcNumber = mcNumber;
	}
	
}
