package com.honda.galc.client.schedule.mbpn;

import org.bushe.swing.event.EventBus;

import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.tracking.MbpnProductHelper;
import com.honda.galc.service.tracking.MbpnProductProcessor;

/**
 * 
 * @author Wade Pei <br>
 * @date Aug 26, 2014
 */
public class PaintMbpnProductProcessor extends MbpnProductProcessor<String> {
	private String processPointId;

	public PaintMbpnProductProcessor(String processPointId) {
		super();
		this.processPointId = processPointId;
	}

	public static boolean isSupplierProduct(PreProductionLot productionLot) {
		if (null == productionLot) {
			return false;
		}
		String productType = MbpnProductHelper.getProdTypeByOrderNoAndProdSpecCode(productionLot.getProductionLot(), productionLot.getProductSpecCode());
		// For the supplier products, their types are "SUPPLIER"
		return "SUPPLIER".equals(productType);
	}

	@Override
	protected String validateProduct(String productId, PreProductionLot productionLot, ProductStampingSequence pss) {
		String errMsg = super.validateProduct(productId, productionLot, pss);
		// TODO since the schedule client is moved from BaseClientFx, so this code will no longer be maintained.
		if (null != errMsg) {
			EventBus.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
			getLogger().warn(errMsg);
		}
		return errMsg;
	}

	@Override
	protected boolean isProductAlreadyAssigned(String productId, PreProductionLot productionLot) {
		try {
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			if (mbpnProduct != null && null != productionLot && null != mbpnProduct.getCurrentOrderNo()
					&& mbpnProduct.getCurrentOrderNo().equals(productionLot.getProductionLot())) {
				return true;
			}
		} catch (Exception e) {
			getLogger().info("Could not check if MbpnProduct " + productId + " was already created");
			handleException(e);
		}
		return false;
	}

	@Override
	protected void actIfProductAlreadyAssigned(String productId) {
		String errMsg = "The Product " + productId + " is already assigned";
		EventBus.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
		getLogger().warn(errMsg);
	}

	@Override
	public String getProcessPointId() {
		return processPointId;
	}
}
