package com.honda.galc.client.datacollection.processor;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductDigitCheckUtil;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;

public class CheckDigitPartSerialNumberProcessor extends
		PartSerialNumberProcessor {

	public CheckDigitPartSerialNumberProcessor(ClientContext context) {
		super(context);
	}

	@Override
	protected boolean confirmPartSerialNumber(PartSerialNumber partnumber) {
		super.confirmPartSerialNumber(partnumber);
		if(validate(getController().getCurrentLotControlRule(), getController().getState().getProductId(), installedPart)){
		return true;
		}
		return true;
	}

	@Override
	protected void handleException(String info) {
		throw new TaskException(info, ProductIdProcessor.PROCESS_PRODUCT);
	}
	
	public boolean validate(LotControlRule lotControlRule, String productId, ProductBuildResult result){
		String partSerialNumber = result.getPartSerialNumber();
		boolean isCheckValid;
		isCheckValid = ProductDigitCheckUtil.isCheckDigitValid(partSerialNumber);
		if (!isCheckValid) {
			Logger.getLogger().error(
					"Check Digit verification failed for part serial number: "
							+ partSerialNumber);
			handleException("Check Digit verification failed for part serial number: "
					+ partSerialNumber);
		}
		return true;
	}

}
