package com.honda.galc.client.product.process;

import com.honda.galc.client.mvc.AbstractView;
import com.honda.galc.client.product.mvc.ProductModel;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.MainWindow;


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

public abstract class AbstractProcessView<M extends AbstractProcessModel,C extends AbstractProcessController<?,?>> extends AbstractView<M,C> implements IProcessView<M,C>{

	public AbstractProcessView(ViewId viewId,MainWindow window) {
		super(viewId,window);
	}
	
	public C getController() {
		return super.getController();
	}
	
	public ProductModel getProductModel() {
		return getModel().getProductModel();
	}
}
