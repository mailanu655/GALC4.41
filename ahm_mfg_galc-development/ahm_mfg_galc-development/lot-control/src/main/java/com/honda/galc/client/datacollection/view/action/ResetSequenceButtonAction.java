package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.control.DataCollectionController;
import com.honda.galc.client.datacollection.state.DataCollectionState;
import com.honda.galc.client.datacollection.state.ProductBean;
import com.honda.galc.client.ui.component.ManualProductEntryDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.entity.product.SkippedProduct;
import com.honda.galc.entity.product.SkippedProductId;
import com.honda.galc.net.Request;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ResetSequenceButtonAction extends BaseDataCollectionAction {

	private static final long serialVersionUID = 8988655179488534900L;
	private boolean cancelled;

	public ResetSequenceButtonAction(ClientContext context, String name) {
		super(context, name);
	}

	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		doResetSeq();
	}

	private void doResetSeq() {
		if (getProperty().isNeedAuthorizedUserToSkipProduct() && !login()) return;

		logInfo();
		if (context.isOnLine()) {
			resetExpectedProduct();
		}
		if (!isCancelled()) {
			runInSeparateThread(new Request("skipProduct"));
		}
	}

	private void resetExpectedProduct() {
		String resetProductId = resetProduct();
		if (this.cancelled) {
			Logger.getLogger().info("Reset Sequence has been cancelled");
		}
		if (!StringUtils.isEmpty(resetProductId)) {
			// It's possible to skip multiple products, so get skipped range
			EventBus.publish(new SkippedProduct(new SkippedProductId(resetProductId, context.getProcessPointId())));
		}
	}

	private String resetProduct() {
		ManualProductEntryDialog manualProductEntryDialog = new ManualProductEntryDialog(context.getFrame(), context.getProperty().getProductType(), "Reset SEQ");
		manualProductEntryDialog.setModal(true);
		manualProductEntryDialog.setVisible(true);
		String productId = manualProductEntryDialog.getResultProductId();
		this.cancelled = StringUtils.isEmpty(productId);
		if (!isCancelled()) { 
			String previousToResetSeq = context.getDbManager().getExpectedProductManger().findPreviousProductId(productId);
			if (previousToResetSeq == null) {
				if (context.getCurrentViewManager() == null)
					context.createViewManager();
				context.getCurrentViewManager().setErrorMessage("Unable to find previous product for " + productId);
				this.cancelled = true;
				return null;
			}
					
			if(isUseExpectedAsScan()) {
				if(isValidateLineOnReset()) {
					String selectedProductLineId = "";
					if(getProperty().isProcessEmptyEnabled()) {
						 String selectedProcessPointId = ServiceFactory.getDao(ProductCarrierDao.class).findByProductId(productId).getProcessPointId();
						 selectedProductLineId = ServiceFactory.getDao(ProcessPointDao.class).findById(selectedProcessPointId).getLineId();				 		
					}
					else {
						selectedProductLineId = ServiceFactory.getDao(InProcessProductDao.class).findByKey(productId).getLineId();
					 }
					String resetLineId = expectedResetLineId();
					String expectedLineId; 
					if(StringUtils.isNotBlank(resetLineId)) {
						expectedLineId =  resetLineId;
					} else {
						expectedLineId =ServiceFactory.getDao(ProcessPointDao.class).findById(context.getProcessPointId()).getLineId();
					}					
					if(!StringUtils.equalsIgnoreCase(selectedProductLineId, expectedLineId)) {						 
						context.getCurrentViewManager().setErrorMessage("Line Id : "+selectedProductLineId+" for Product Id : "+productId+" doesn't match the expected line id : "+expectedLineId);
						this.cancelled = true;
						return null;
					}				 
				}
			} 
			 				
			DataCollectionState currentState = DataCollectionController.getInstance().getState();
			currentState.getStateBean().setSkipBroadcast(true);
			String resetSeqToProductPointer = (context.getProperty().isSaveNextProductAsExpectedProduct() ? productId : previousToResetSeq);	
			String resetSeqProducts = currentState.getExpectedProductId();
			if (resetSeqProducts != null) {
				if(previousToResetSeq.length() == context.getProperty().getMaxProductSnLength())
				resetSeqProducts = (resetSeqProducts + "-" + previousToResetSeq.substring(context.getProperty().getMaxProductSnLength() - 3));
			}
			
			//Save skipped to/next expected to database
			context.getDbManager().saveExpectedProductId(resetSeqToProductPointer, currentState.getProductId());
			currentState.setProduct(makeProductBean(previousToResetSeq));
			currentState.setExpectedProductId(productId);
			currentState.getStateBean().setResetSequence(true);
			Logger.getLogger().info("Reset Expected Product Sequence to: " + productId + "; Skipping product range " + resetSeqProducts);
			return resetSeqProducts;
		}
		return null;
	}

	private ProductBean makeProductBean(String productId) {
		ProductBean productBean = new ProductBean();
		productBean.setProductId(productId);
		productBean.setValidProductId(true);
		return productBean;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}
		
	public boolean isUseExpectedAsScan() {
	 return PropertyService.getPropertyBoolean(context.getProcessPointId(), "USE_EXPECTED_AS_SCAN", false);
	}
	
	public boolean isValidateLineOnReset() {	
		 return PropertyService.getPropertyBoolean(context.getProcessPointId(), "VALIDATE_LINE_ON_RESET", false);
	}
		
	public String expectedResetLineId() {
			return  PropertyService.getProperty(context.getProcessPointId(), "EXPECTED_RESET_LINE_ID", "");
	}

}
