package com.honda.galc.client.datacollection.view.action;

import java.awt.event.ActionEvent;

import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.client.datacollection.view.ViewManagerBase;
import com.honda.galc.client.datacollection.view.ViewManagerBase.UniqueScanType;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.util.CarrierUtil;
/**
 * 
 * <h3>ProductIdInputAction</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductIdInputAction description </p>
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
 * @author Paul Chou
 * Jun 17, 2010
 *
 */
public class ProductIdInputAction extends BaseDataCollectionAction {
	private static final long serialVersionUID = 4256139899025982427L;
	
	public ProductIdInputAction(ClientContext context, String name) {
		super(context, name);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		confirmProductID(e);
	}

	private void confirmProductID(ActionEvent e) {
		JTextField productIdTextField = (JTextField)e.getSource(); 
		String productId = productIdTextField.getText();
		
		if(!StringUtils.isEmpty(context.getProperty().getTrackingArea())) {
			String productIdFromCarrier = CarrierUtil.findProductIdByCarrier(context.getProperty().getTrackingArea(), productId);
			if(!StringUtils.isEmpty(productIdFromCarrier)) {
				Logger.getLogger().info("Product Id:", productIdFromCarrier, " associated with carrier:", productId, " was found.");
				productId = productIdFromCarrier;
			} else
				Logger.getLogger().warn("Invalid carrier:", productId, " can not find product associated with it.");
		}
		
		String testTorque = getProperty().getScanMap().get(UniqueScanType.TESTTORQUE.name());
		if(StringUtils.isNotEmpty(productId) && StringUtils.isNotEmpty(testTorque) && productId.equalsIgnoreCase(testTorque)) {
			ViewManagerBase vmb = context.getCurrentViewManager();
			if (vmb == null) {
				context.createViewManager();
				vmb = context.getCurrentViewManager();
			}
			vmb.getView().getTestTorqueButton().doClick();
			
		}else{
			Logger.getLogger().info("ProductId:" + productId + " is received from Terminal.");
	
			ProductId id = new ProductId(productId);
			if(!super.context.isTrimProductId()){
				id.setProductIdWithoutTrim(productId);
			}
			runInSeparateThread(id);
		}
	}

}
	