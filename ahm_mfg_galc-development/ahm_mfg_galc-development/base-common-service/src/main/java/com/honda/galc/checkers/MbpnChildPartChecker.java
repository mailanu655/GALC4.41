package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.util.MbpnWeldUtility;

public class MbpnChildPartChecker extends AbstractBaseChecker<PartSerialScanData> {

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Part;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(PartSerialScanData partSerialScanData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		MbpnProduct mbpnProduct = MbpnWeldUtility.getMbpnProduct(partSerialScanData.getSerialNumber());
		if(mbpnProduct!=null) {
			//Perform MBPN spec code mask and Complete check
			CheckResult checkResult = MbpnWeldUtility.mbpnChildPartVerification(mbpnProduct
					, partSerialScanData.getMask(), getReactionType());
			if(checkResult!=null) {
				checkResults.add(checkResult);
			}
		}
		else {
			checkResults.add(MbpnWeldUtility.createCheckResult(
					"MBPN Product "+partSerialScanData.getSerialNumber()+" does not exist"
					, getReactionType()));
		}
		
		return checkResults;
	}

}
