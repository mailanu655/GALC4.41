package com.honda.galc.client.product.pane;

import javafx.scene.control.Label;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.utils.UiFactory;

/**
 * 
 * 
 * <h3>ProductIdlePane Class description</h3>
 * <p> ProductIdlePane description </p>
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
 * Mar 7, 2014
 *
 *
 */
public  class ProductIdlePane extends AbstractProductIdlePane{
	
	
	public ProductIdlePane(ProductController productController) {
		super(productController);
	}
	

	protected void initComponents() {
		Label text = UiFactory.createLabel("ready", "Ready...", UiFactory.getIdle().getLabelFont());
		setCenter(text);
	}

	@Override
	public void toIdle() {
	}
	
	public String getName() {
		return "Default Idle Pane";
	}
}
