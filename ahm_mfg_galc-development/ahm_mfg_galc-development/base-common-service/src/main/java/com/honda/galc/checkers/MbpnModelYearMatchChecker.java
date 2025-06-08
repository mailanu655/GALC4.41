package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.dao.conf.MCProductPddaPlatformDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.MCProductPddaPlatform;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.MbpnWeldUtility;

public class MbpnModelYearMatchChecker extends AbstractBaseChecker<PartSerialScanData> {

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
		
		float productIdModelYear = getModelYear(partSerialScanData.getProductId());
		float serialNumberModelYear= getModelYear(partSerialScanData.getSerialNumber());
		
		if(productIdModelYear!= 0.0 && serialNumberModelYear != 0.0 && 
				productIdModelYear != serialNumberModelYear) {
			CheckResult checkResult = MbpnWeldUtility.createCheckResult("Model Year of "+ProductType.MBPN.name()+" "+partSerialScanData.getSerialNumber()+
					" does not match with that of "+partSerialScanData.getProductId(), getReactionType());
			checkResults.add(checkResult);
		}
		
		return checkResults;
	}

	private float getModelYear(String productId) {
		MCProductPddaPlatform productPddaPlatform = ServiceFactory.getDao(MCProductPddaPlatformDao.class).findByKey(productId);
		return (productPddaPlatform !=null && productPddaPlatform.getModelYearDate()!=null) ? 
				productPddaPlatform.getModelYearDate().floatValue() : 
					0.0f;
	}
}
