package com.honda.galc.client.product.process.model;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.entity.product.BaseProduct;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProcessModel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public class ProcessModel {

	private ApplicationContext applicationContext;

	private BaseProduct product;

	public ProcessModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	// === get/set === //
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public BaseProduct getProduct() {
		return product;
	}

	public void setProduct(BaseProduct product) {
		this.product = product;
	}
}
