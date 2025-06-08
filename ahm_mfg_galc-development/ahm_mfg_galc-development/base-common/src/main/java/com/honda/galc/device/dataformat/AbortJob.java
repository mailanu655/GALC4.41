package com.honda.galc.device.dataformat;

import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>AbortJob</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AbortJob description </p>
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
 * Jun 16, 2010
 *
 */
public class AbortJob implements IDeviceData {

private String parameterData;
	
	
	public AbortJob() {
	}
	
	public AbortJob(String parameterData) {
		super();
		this.parameterData = parameterData;
	}

	public String getParameterData() {
		return parameterData;
	}

	public void setParameterData(String parameterData) {
		this.parameterData = parameterData;
	}

}
