package com.honda.galc.device.dataformat;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>CarrierId</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CarrierId description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Nov 1, 2018</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Nov 1, 2018
 */
public class CarrierId extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	private String carrierId;
	
	public CarrierId() {
		super();
	}

	public CarrierId(String carrierId) {
		super();
		this.carrierId = StringUtils.trim(carrierId);
	}

	// Getters & Setters
	public String getCarrierId() {
		return StringUtils.trim(carrierId);
	}

	public void setCarrierId(String carrierId) {
		this.carrierId = carrierId;
	}

	@Override
	public String toString() {
		return this.carrierId;
	}
	

}
