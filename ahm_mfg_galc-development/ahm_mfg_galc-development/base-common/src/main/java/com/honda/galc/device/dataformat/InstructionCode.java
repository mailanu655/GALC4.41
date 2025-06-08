package com.honda.galc.device.dataformat;

import com.honda.galc.device.IDeviceData;

/**
 * 
 * <h3>TorqueControllerEiJob</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> TorqueControllerEiJob description </p>
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
 * Jun 8, 2010
 *
 */
public class InstructionCode implements IDeviceData{
	private String productId;
	private String parameterSet;
	
	public InstructionCode(String productId, String parameterSet) {
		super();
		this.productId = productId;
		this.parameterSet = parameterSet;
	}
	
	
	public InstructionCode() {
	}


	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getParameterSet() {
		return parameterSet;
	}
	public void setParameterSet(String parameterSet) {
		this.parameterSet = parameterSet;
	}
	
	
	
}
