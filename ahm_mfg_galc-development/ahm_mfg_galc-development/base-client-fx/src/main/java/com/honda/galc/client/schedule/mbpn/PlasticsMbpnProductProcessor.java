/**
 * 
 */
package com.honda.galc.client.schedule.mbpn;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.service.tracking.MbpnProductProcessor;

/**
 * @author Subu Kathiresan
 * @date Feb 1, 2013
 */
public class PlasticsMbpnProductProcessor extends MbpnProductProcessor<String> {

	private static final String ATTRIB_MOLD_CODE = "MOLD_CODE";
	private static final Integer PRODUCT_LENGTH = 17;
	private ApplicationContext _appContext;

	public PlasticsMbpnProductProcessor(ApplicationContext appContext) {
		_appContext = appContext;
	}

	private BuildAttribute getMoldCode(String productSpecCode) {
		BuildAttribute moldCodeBuildAttrib = getBuildAttributeDao().findById(ATTRIB_MOLD_CODE, productSpecCode);
		if (moldCodeBuildAttrib == null || StringUtils.trimToNull(moldCodeBuildAttrib.getAttributeValue()) == null) {
			EventBusUtil.publish(new DisplayMessageEvent("Mold code not found for Product Spec Code " + productSpecCode, MessageType.ERROR));
			getLogger().warn("Mold code not found for Product Spec Code " + productSpecCode);
		} else {
			getLogger().info(
					"Mold code retrieved for product spec code " + productSpecCode + " is "
							+ StringUtils.trimToEmpty(moldCodeBuildAttrib.getAttributeValue()));
		}
		return moldCodeBuildAttrib;
	}

	protected ApplicationContext getAppContext() {
		return _appContext;
	}

	@Override
	protected String validateProduct(String productId, PreProductionLot productionLot, ProductStampingSequence pss) {
		String errMsg = super.validateProduct(productId, productionLot, pss);
		if (null == errMsg) {
			BuildAttribute moldCodeBuildAttrib = getMoldCode(StringUtils.trimToEmpty(productionLot.getProductSpecCode()));
			String productMoldCode = StringUtils.trimToEmpty(productId.substring(1, 3));
			// check product length
			if (productId.length() != PRODUCT_LENGTH) {
				errMsg = "Scanned Product " + productId + " should be of length " + PRODUCT_LENGTH;
			} else

			// check product type. TODO add checks for other types
			if (!productId.substring(16).equals("I")) {
				errMsg = "Scanned Product " + productId + " is not an instrument panel. Please check bar code";
			} else

			// check Injection machine
			if (Integer.parseInt(productId.substring(0, 1)) > 3) {
				errMsg = "Scanned Product " + productId + " came from an unknown injection machine. Plase check bar code";
			} else

			// check mold code
			if (moldCodeBuildAttrib == null) {
				errMsg = "Mold code attribute not defined for product spec code ";
			} else

			if (!productMoldCode.equals(StringUtils.trimToEmpty(moldCodeBuildAttrib.getAttributeValue()))) {
				errMsg = "Scanned Product " + productId + " with MoldCode " + productMoldCode + " is invalid for order "
						+ pss.getId().getProductionLot();
			}
		}
		if (null != errMsg) {
			EventBusUtil.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
			getLogger().warn(errMsg);
		}
		return errMsg;
	}

	@Override
	protected void actIfProductAlreadyAssigned(String productId) {
		EventBusUtil.publish(new DisplayMessageEvent("Scanned Product " + productId + " is already assigned", MessageType.ERROR));
	}

	protected String getProcessPointId() {
		return getAppContext().getProcessPointId();
	}

	protected String getTerminalId() {
		return getAppContext().getTerminalId();
	}

	protected void actIfProductIsInvalid(String productId) {
		EventBusUtil.publish(new DisplayMessageEvent("Scanned Product " + productId + " is invalid", MessageType.ERROR));
	}

}
