package com.honda.galc.client.teamleader.qi.productRecovery;

import com.honda.galc.client.ui.IEvent;
import com.honda.galc.data.ProductType;
/**
 * 
 * <h3>ShipScrapEvent</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ShipScrapEvent description </p>
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
 * @author L&T Infotech
 * Aug 28, 2017
 *
 */


public class ShipScrapEvent implements IEvent{
	private ProductType productType;
	private boolean isShipped;
	
	public ShipScrapEvent(ProductType productType, boolean isShipped) {
		super();
		this.productType = productType;
		this.isShipped = isShipped;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public boolean isShipped() {
		return isShipped;
	}

	public void setShipped(boolean isShipped) {
		this.isShipped = isShipped;
	}
}
