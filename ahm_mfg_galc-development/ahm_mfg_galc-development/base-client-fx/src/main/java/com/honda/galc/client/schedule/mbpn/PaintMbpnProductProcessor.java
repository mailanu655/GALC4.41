package com.honda.galc.client.schedule.mbpn;

import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dto.rest.ProductTrackDTO;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.tracking.MbpnProductProcessor;

/**
 * 
 * @author Wade Pei <br>
 * @date Aug 26, 2014
 */
public class PaintMbpnProductProcessor extends MbpnProductProcessor<ProductTrackDTO> {
	private static final Integer PRODUCT_LENGTH = 17;
	private String processPointId;
	private String plantLocCode;
	private String deptCode;

	public PaintMbpnProductProcessor(String processPointId) {
		super();
		this.processPointId = processPointId;
	}

	public PaintMbpnProductProcessor(String processPointId, String plantLocCode, String deptCode) {
		super();
		this.processPointId = processPointId;
		this.plantLocCode = plantLocCode;
		this.deptCode = deptCode;
	}

	@Override
	protected String validateProduct(String productId, PreProductionLot productionLot, ProductStampingSequence pss) {
		String errMsg = super.validateProduct(productId, productionLot, pss);
		if (null == errMsg) {
			if (productId.length() != PRODUCT_LENGTH) {
				errMsg = "The length of the product Id should be " + PRODUCT_LENGTH;
			} else if (!productId.startsWith(plantLocCode)) {
				errMsg = "The product Id should be start with '" + plantLocCode + "'";
			} else if (null != deptCode && !deptCode.equals(productId.substring(1, 3))) {
				errMsg = "The second to third characters should be the department code '" + deptCode + "'";
			}
		}
		if (null != errMsg) {
			EventBusUtil.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
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
		EventBusUtil.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
		getLogger().warn(errMsg);
	}

	@Override
	public String getProcessPointId() {
		return processPointId;
	}
}
