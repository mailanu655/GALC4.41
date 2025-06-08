package com.honda.galc.client.product.pane;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.ApplicationMainPane;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.event.ProductCancelledEvent;
import com.honda.galc.client.ui.event.ProductFinishedEvent;
import com.honda.galc.client.ui.event.ProductStartedEvent;


/**
 * 
 * <h3>AbstractProcessView Class description</h3>
 * <p> AbstractProcessView description </p>
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
 * Feb 24, 2014
 *
 *
 */

public abstract class AbstractWidget extends ApplicationMainPane{
	
	private ProductController productController;
	
	private ViewId viewId;
	
	public AbstractWidget(ViewId viewId,ProductController productController) {
		super(productController.getView().getMainWindow());
		this.productController = productController;
		this.viewId = viewId;
		EventBusUtil.register(this);
		initComponents();
	}
	

	public ProductController getProductController() {
		return productController;
	}
	
	public ViewId getViewId() {
		return viewId;
	}
	
	public String getViewLabel(){
		return viewId == null ? "" : viewId.getViewLabel();
	}
	
	@Subscribe
	public void received(ProductStartedEvent event) {
		processProductStarted(event.getProductModel());
	}
	
	@Subscribe
	public void received(ProductFinishedEvent event) {
		processProductFinished(event.getProductModel());
	}
	
	@Subscribe
	public void received(ProductCancelledEvent event) {
		processProductCancelled(event.getProductModel());
	}

	protected abstract void initComponents();

	protected abstract void processProductStarted(ProductModel productModel);

	protected abstract void processProductCancelled(ProductModel productModel);
	
	protected abstract void processProductFinished(ProductModel productModel);
	
}
