package com.honda.galc.client.qics.view.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.screen.DunnagePanel;
import com.honda.galc.client.qics.view.screen.MainPanel;
import com.honda.galc.client.qics.view.screen.QicsPanel;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>SubmitWithDunnageAction</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 7, 2009</TD>
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
public class SubmitWithDunnageAction extends SubmitOffAction {

	private static final long serialVersionUID = 1L;

	public SubmitWithDunnageAction(QicsPanel qicsPanel) {
		super(qicsPanel);
	}

	@Override
	protected void execute(ActionEvent e) {
		if (getDunnagePanel().isProductAlreadyOnPallet()) {
			super.execute(e);
			return;
		} 
		
		boolean printDunnage = getQicsController().getClientModel().isPrintDunnage();
		int dunnageCapacity = getQicsController().getDunnageCartQuantity();
		BaseProduct product = getQicsController().getProductModel().getProduct();	
					
		if(!getQicsController().doDunnageUpdate()){
			if(printDunnage){
				List<? extends BaseProduct> products = ProductTypeUtil.getProductDao(product.getProductType()).findAllByDunnage(product.getDunnage());
				int dunnageCount = products.size();
				if (dunnageCount >= dunnageCapacity) {
					getQicsController().printDunnage(product.getDunnage(), products);
				}					
			}
			super.execute(e);
			return;
		}
		
		if (getQicsFrame().displayDelayedMessage()){
			return;
		}
		
		int dunnageUpdatedRowCount =  getQicsController().submitDunnage();
		if (dunnageUpdatedRowCount == 0) {
			getDunnagePanel().loadDunnageProducts(getQicsController().getClientModel().getDunnageNumber());
			String msg = "Dunnage completed by another process. Start new Dunnage";
			getQicsFrame().setMessage(msg);
			return;
		} 
		
		if (dunnageUpdatedRowCount > 0 && printDunnage) {
			int dunnageCount = (int) getQicsController().selectCountByDunnage(getQicsController().getClientModel().getDunnageNumber());
			if (dunnageCount >= dunnageCapacity) {
				List<BaseProduct> products = new ArrayList<BaseProduct>(getDunnagePanel().getProductPane().getItems());
				products.add(getQicsController().getProductModel().getProduct());
				getQicsController().printDunnage(products);
			}
		}
		super.execute(e);		
	}
	
	@Override
	protected MainPanel getQicsPanel() {
		return (MainPanel) super.getQicsPanel();
	}

	public DunnagePanel getDunnagePanel() {
		return (DunnagePanel) getQicsPanel().getInputPanel(QicsViewId.DUNNAGE);
	}
	
	// === business utility methods === //
	protected boolean executeForwardToOffProcessPoint(ActionEvent e) {
		if (getClientConfig().isOffProcessPointIdDefined()) {
			 getQicsController().trackOffProcessPoint();
		}
		return true;
	}
}
