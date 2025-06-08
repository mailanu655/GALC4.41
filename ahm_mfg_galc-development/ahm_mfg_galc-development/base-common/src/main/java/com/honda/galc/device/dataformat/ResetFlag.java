package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.Tag;

/**
 * 
 * 
 * <h3>ResetFlag Class description</h3>
 * <p> ResetFlag description </p>
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
 * May 23, 2014
 *
 *
 */
public class ResetFlag extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Tag(name="RESET")
	private String reset;
	
	public ResetFlag() {
		super();
	}

	// Getters & Setters
	public String getResetFlag() {
		return reset;
	}
	
	public void setResetFlag(String reset) {
		this.reset = reset;
	}

	
	
}
