package com.honda.galc.client.product;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.mvc.ProductController;
import com.honda.galc.client.schedule.ProductEvent;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.util.CarrierUtil;

/**
 * 
 * @author vec15809
 *
 */
public class QiCarrierIdProcessor extends QiProductIdProcessor {

	public QiCarrierIdProcessor(ProductController productController) {
		super(productController);
	}
	
	@Override
	protected void setProductIdInputNumber(ProductEvent event) {
		if (null!=event) {
			if(!StringUtils.isEmpty(getProductController().getView().getApplicationPropertyBean().getTrackingArea())) {
			String productIdFromCarrier = CarrierUtil.findProductIdByCarrier(getProductController().getView().getApplicationPropertyBean().getTrackingArea(), (String)event.getTargetObject());
			
			if(StringUtils.isEmpty(productIdFromCarrier)) {
				Logger.getLogger().warn("No produdt associated with carrier:", (String)event.getTargetObject());
				productIdFromCarrier =  (String)event.getTargetObject();
			}
			getProductController().getView().getInputPane().getProductIdField().setText(productIdFromCarrier);
			}
		}
	}

}
