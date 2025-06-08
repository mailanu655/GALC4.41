package com.honda.galc.device.dataformat;

import java.io.Serializable;

/**
 * 
 * 
 * <h3>ProductIdReset Class description</h3>
 * <p> ProductIdReset description </p>
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
 * Mar 13, 2014
 *
 *
 */
public class ProductIdReset extends InputData implements Serializable{
	private static final long serialVersionUID = 1L;
	private boolean productIdReset;
	
	public ProductIdReset() {
		super();
	}

	public ProductIdReset(boolean isReset) {
		super();
		this.productIdReset = isReset;
	}

	public boolean isProductIdRefresh() {
		return productIdReset;
	}

	public void setProductIdReset(boolean isReset) {
		this.productIdReset = isReset;
	}
    
}
