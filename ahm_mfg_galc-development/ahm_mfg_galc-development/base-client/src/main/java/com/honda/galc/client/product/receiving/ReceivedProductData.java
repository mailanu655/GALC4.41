package com.honda.galc.client.product.receiving;

import com.honda.galc.data.ProductType;

/**
 * <h3>Class description</h3>
 * Information related to received products.
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
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class ReceivedProductData {

	private String originalId;
	private ProductType productType;
	private String newModelCode;
	private String conversionId;
	
	public String getOriginalId() {
		return originalId;
	}
	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public String getNewModelCode() {
		return newModelCode;
	}
	public void setNewModelCode(String newModelCode) {
		this.newModelCode = newModelCode;
	}
	public String getConversionId() {
		return conversionId;
	}
	public void setConversionId(String conversionId) {
		this.conversionId = conversionId;
	}
}
