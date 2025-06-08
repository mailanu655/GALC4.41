package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.observer.ILotControlDbManager;
import com.honda.galc.client.datacollection.observer.LotControlAudioManager;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.net.Request;


public class PreviousProductButtonAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 8394239778741676739L;
	protected boolean canceled;

	public PreviousProductButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public PreviousProductButtonAction(ClientContext context, String name,
			boolean afterPopup) {
		super(context, name);

		if (LotControlAudioManager.isExist())
			LotControlAudioManager.getInstance().stopRepeatedSound();

		doPrevVIN();
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);

		doPrevVIN();
	}

	public void doPrevVIN() {
		if (getProperty().isNeedAuthorizedUserToSkipProduct() && !login())
			return;

		logInfo();

		if (context.isOnLine())
			doSkipProduct();


		if (!isCanceled())
			runInSeparateThread(new Request("skipProduct"));
	}

	protected void doSkipProduct() {
		String productId = getState().getProductId();
		if(productId == null) productId = getState().getExpectedProductId();
		
		if(productId != null) {
			String previousToResetSeq = context.getDbManager().getExpectedProductManger().findPreviousProductId(productId);
	
			if (previousToResetSeq == null) {
				if (context.getCurrentViewManager() == null)
					context.createViewManager();
				context.getCurrentViewManager().setErrorMessage("Unable to find previous product for " + productId);
			}else {
				context.getDbManager().saveExpectedProductId(previousToResetSeq,previousToResetSeq);
				getState().setExpectedProductId(previousToResetSeq);
				Logger.getLogger().info("Reset Expected Product Sequence to: " + previousToResetSeq);
			}
		}
	}
	
	public boolean isCanceled() {
		return canceled;
	}

	protected ILotControlDbManager getDbManager() {
		return context.getDbManager();
	}
	
	protected DataCollectionState getState() {
		return DataCollectionController.getInstance(context.getAppContext().getApplicationId()).getState();
	}
	
}
