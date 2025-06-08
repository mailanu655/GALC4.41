package com.honda.galc.client.datacollection.processor;

import java.util.List;

import com.honda.galc.checkers.CheckResult;
import com.honda.galc.checkers.PartSnExpirationDateChecker;
import com.honda.galc.client.common.component.Message;
import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.ProductBuildResult;

/**
 * @author Subu Kathiresan
 * @date Sep 25, 2017
 */
public class IpuExpirationCheckProcessor extends SubproductPartSerialNumberProcessor {

	PartSnExpirationDateChecker expirationChecker = new PartSnExpirationDateChecker();
	
	public IpuExpirationCheckProcessor(ClientContext context) {
		super(context);
	}

	public synchronized boolean execute(PartSerialNumber partSerialNumber) {
		Logger.getLogger().debug("IpuExpirationCheckProcessor : Enter execute");		
		try {
			Logger.getLogger().info("Processing part: " + partSerialNumber.getPartSn());
			confirmPartSerialNumber(partSerialNumber);
			installedPart.setPartSerialNumber(partSerialNumber.getPartSn());
			checkExpirationDate(partSerialNumber.getPartSn());
				
			installedPart.setValidPartSerialNumber(true);
			installedPart.setPartIndex(getCurrentRule().getSequenceNumber());
			installedPart.setPartId(getCurrentRule().getParts().get(0).getId().getPartId());
			getController().getFsm().partSnOk(installedPart);
			Logger.getLogger().debug("IpuExpirationCheckProcessor:: Exit execute OK");
			return true;
		} catch (TaskException te) {
			Logger.getLogger().error(te.getMessage());
			installedPart.setValidPartSerialNumber(false);
			getController().getFsm().partSnNg(installedPart, PART_SN_MESSAGE_ID, te.getMessage());
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "ThreadID = " + Thread.currentThread().getName() + " :: execute() : Exception : " + ex.toString());
			getController().getFsm().error(new Message(ex.getMessage()));
		}
		Logger.getLogger().debug("IpuExpirationCheckProcessor:: Exit execute NG");
		return false;
	}

	public boolean validate(LotControlRule lotControlRule, String productId, ProductBuildResult result){
		return checkExpirationDate(result.getPartSerialNumber());
	}
	
	public boolean checkExpirationDate(String partSn) {
		PartSerialScanData scanData = new PartSerialScanData();
		scanData.setSerialNumber(partSn);
		List<CheckResult> checkResults = expirationChecker.executeCheck(scanData);
		
		if (checkResults.size() > 0) {
			String errMsg = checkResults.get(0).getCheckMessage();
			Logger.getLogger().error(errMsg);
			handleException(errMsg);
		} else {
			Logger.getLogger().info("IPU " + partSn + " expires on " + expirationChecker.getExpirationDate() + ". Passed verification!");
		} 
		return true;
	}
		
	private LotControlRule getCurrentRule() {
		return getController().getState().getCurrentLotControlRule();	
	}
}
