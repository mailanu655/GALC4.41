package com.honda.galc.device.dataformat;

import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.Tag;
/**
 * 
 * 
 * <h3>ResetCommand Class description</h3>
 * <p> ResetCommand description </p>
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
 * Feb 1, 2016
 *
 *
 */
public class ResetCommand implements IDeviceData {

	@Tag(name="RESET")
	private String resetFlag;
	
	
	public ResetCommand() {
	}
	
	public ResetCommand(String parameterData) {
		super();
		this.resetFlag = parameterData;
	}

	// Getters & Setters
	public String getResetFlag() {
		return resetFlag;
	}
	
	public void setResetFlag(String resetFlag) {
		this.resetFlag = resetFlag;
	}

}
