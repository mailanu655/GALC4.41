package com.honda.galc.client.product.pane;

import javafx.scene.layout.BorderPane;

import com.honda.galc.client.product.mvc.ProductController;

/**
 * 
 * 
 * <h3>AbstractProductIdlePane Class description</h3>
 * <p> AbstractProductIdlePane description </p>
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
 * @author Shweta Kadav<br>
 * Oct 6, 2016
 *
 *
 */
public abstract class AbstractProductIdlePane extends BorderPane{
	
	private ProductController productController;
	
	public AbstractProductIdlePane(ProductController productController) {
		this.productController = productController;
		initComponents();
	}
	
	
	public ProductController getProductController() {
		return productController;
	}


	public void setProductController(ProductController productController) {
		this.productController = productController;
	}


	protected abstract void initComponents();
	
	public abstract  void toIdle();
	
	public abstract String getName();
	
}
