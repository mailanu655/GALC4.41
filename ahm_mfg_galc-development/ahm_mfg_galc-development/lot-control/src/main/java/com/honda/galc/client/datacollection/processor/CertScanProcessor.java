package com.honda.galc.client.datacollection.processor;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.PartSpec;

public class CertScanProcessor extends PartSerialNumberProcessor {
	
	private List<String> duplicateList = new ArrayList<String>();
	
	public CertScanProcessor(ClientContext context) {
		super(context);
	}
	
	@Override
	public synchronized boolean confirmPartSerialNumber(PartSerialNumber barcode) {
		duplicateList.clear();
		String partSn = barcode.getPartSn();
		String prodId = getController().getState().getProductId();
		List<PartSpec> partInfo = getController().getState().getCurrentLotControlRulePartList();
		Logger.getLogger("PartSpec" + partInfo);
		String hiddenChar = "I";
		if((hiddenChar.concat(prodId)).equals(partSn)){
			installedPart.setPartSerialNumber(barcode.getPartSn());
			installedPart.setValidPartSerialNumber(true);
			installedPart.setPartId(getController().getCurrentLotControlRule().getParts().get(0).getId().getPartId());
		}
		else
			handleException("Part serial number:" + partSn + 
					" verification failed." + partSn);
		if(isCheckDuplicatePart())
			checkDuplicatePart(barcode.getPartSn());
		return true;
	}
}
