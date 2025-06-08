package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.ILotControlDbManager;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.view.ExpectedProductSelectView;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.net.Request;

/**
 * <h3>SkipEngineButtonAction</h3>
 * <h4>
 * Defines action for Skip Engine button.
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="0" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Aug.19, 2009</TD>
 * <TD>0.1</TD>
 * <TD>Initial Version</TD>
 * <TD></TD>
 * </TR>  
 * </TABLE>
 * @see 
 * @ver 0.1
 * @author Paul Chou
 */
/**
 * * *
 * 
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class SkipProductButtonAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 8394239778741676739L;
	protected List<String> incomingProducts;
	protected ExpectedProductSelectView expProductIdSelectView;
	protected boolean canceled;

	public SkipProductButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public SkipProductButtonAction(ClientContext context, String name,
			boolean afterPopup) {
		super(context, name);

		if (LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().stopRepeatedSound();

		doNextVIN();
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		doNextVIN();
	}

	public void doNextVIN() {
		if (getProperty().isNeedAuthorizedUserToSkipProduct() && !login())
			return;

		logInfo();

		if (context.isOnLine())
			doSkipProduct();

		if (!isCheckExpectedAndSkipMultipleProducts()
				|| isCheckExpectedAndSkipMultipleProducts()
				&& !context.isOnLine())
			EventBus.publish(new SkippedProduct(new SkippedProductId(
					getSkippedProductId(), context.getProcessPointId())));

		if (!isCanceled())
			runInSeparateThread(new Request("skipProduct"));
	}

	protected boolean isCheckExpectedAndSkipMultipleProducts() {
		return context.isCheckExpectedProductId()
				&& context.getProperty().isSkipMultipleProducts();
	}

	protected void doSkipProduct() {
		if (context.isCheckExpectedProductId()) {
			if (context.getProperty().isSkipMultipleProducts()) {
				skipMultipleProducts();
			} else {
				skipSingleProduct();
			}
		}
	}
	
	protected void skipMultipleProducts() {
		populateIncomingProducts();
		getExpProductIdSelectView().showDialog(incomingProducts);
		
		canceled = expProductIdSelectView.isCanceled();
		String skippedProducts = expProductIdSelectView.getSkippedToProducts();
		Logger.getLogger().info("Skip Expected Product to:", skippedProducts);
		
		notifySkippedProduct(skippedProducts);
	}
	
	protected void skipSingleProduct() {
		String productId = getState().getProductId();
		String expectedProductId = getState().getExpectedProductId();

		String skippedProductId = productId;
		if (context.getProperty().isSaveNextProductAsExpectedProduct()) {
			expectedProductId = getDbManager().getNextExpectedProductId(expectedProductId);
		}

		getState().setExpectedProductId(expectedProductId);
		getDbManager().saveExpectedProductId(expectedProductId, productId);

		notifySkippedProduct(skippedProductId);
	}

	private void notifySkippedProduct(String skippedProductId) {
		if (!StringUtils.isEmpty(skippedProductId)) {
			// It's possible to skip multiple engines, so get skipped range
			EventBus.publish(new SkippedProduct(new SkippedProductId(skippedProductId, context.getProcessPointId())));
		}
	}

	protected void populateIncomingProducts() {
		
		try {
			incomingProducts = getDbManager().getIncomingProducts(getState());
		} catch (Exception e) {
			Logger.getLogger().warn("Exception to get incoming products:", e.getMessage());
		}
		
		
		if (incomingProducts == null) {
			incomingProducts = new ArrayList<String>();
		}
		
		Logger.getLogger().info("Incomming Products:", incomingProducts.toString());	
	}
	
	public ExpectedProductSelectView getExpProductIdSelectView() {
		if (expProductIdSelectView == null) {
			expProductIdSelectView = new ExpectedProductSelectView(context);
		}
		return expProductIdSelectView;
	}

	public boolean isCanceled() {
		return canceled;
	}

	protected String getSkippedProductId() {
		if (context.getProperty().isCheckExpectedProductId())
			return getState().getExpectedProductId();
		else
			return getState().getProductId();
	}
		
	protected ILotControlDbManager getDbManager() {
		return context.getDbManager();
	}
	
	protected DataCollectionState getState() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState();
	}
}
