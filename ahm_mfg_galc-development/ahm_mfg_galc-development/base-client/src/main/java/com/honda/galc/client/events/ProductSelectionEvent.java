package com.honda.galc.client.events;

import com.honda.galc.data.ProductType;


/**
 * <h3>Class description</h3>
 * The event is used to publish selection of a product.
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>May 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130522</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class ProductSelectionEvent {
	
	private ProductType productType;
	private String productId = "";
	
	public ProductSelectionEvent(String productId) {
		this.productId = productId;
	}
	
	public ProductSelectionEvent(ProductType productType, String productId) {
		this.productType = productType;
		this.productId = productId;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
