package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;

public class PartOnHoldChecker extends AbstractBaseChecker<PartSerialScanData>  {
	
	private final String DEFAULT_LOT_CONTROL = "Default_LotControl";
	private final String PRODUCT_ID_PREFIX = "PRODUCT_ID_PREFIX";

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public CheckerType getType() {
		return CheckerType.Application;
	}

	@Override
	public int getSequence() {
		return 0;
	}


	@Override
	public List<CheckResult> executeCheck(PartSerialScanData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		Logger.getLogger().info("Executing the Part Hold Check  ");
		BaseProduct product = null;
		if ((inputData.getProductType() != null) && (inputData.getProductId() != null)) {
			product = ProductTypeUtil.getProductDao(inputData.getProductType()).findBySn(inputData.getProductId());
			Logger.getLogger().info("Part Hold Check - Product Id :"+inputData.getProductId());
		}
			
		if (product == null) {
			Logger.getLogger().info("Part Hold Check Failed - The Product does not exist. ");
			checkResults.add(createCheckResult("Part Hold Check Failed - The Product does not exist."));
			return checkResults;
		}
		if (StringUtils.isBlank(inputData.getSerialNumber())){
			Logger.getLogger().info("Part Hold Check Failed - Scanned part number is blank/null. ");
			checkResults.add(createCheckResult("Part Hold Check Failed - Scanned part number is blank/null."));
			return checkResults;
		}
		String productIdPrefix = PropertyService.getProperty(DEFAULT_LOT_CONTROL, PRODUCT_ID_PREFIX,null);
		
		ProductCheckUtil checkUtil = new ProductCheckUtil();
		checkUtil.setProduct(product);
		checkUtil.setInstalledPartNames(Arrays.asList(inputData.getSerialNumber()));
				
		List<String> productHoldList = null;
		try {
			productHoldList = checkUtil.partOnHoldCheck(productIdPrefix);
		} catch (Exception e) {
			Logger.getLogger().error(e,"Part Hold Check Failed - while retriving the hold information ");
			checkResults.add(createCheckResult("Part Hold Check Failed - while retriving the hold information "));
		}
		
		if(productHoldList!=null && productHoldList.size()>0) {		
			StringBuilder sb = new StringBuilder();			
			sb.append("Part is on Hold for VIN :"+product);
					
			for (String reason : productHoldList) {
				sb.append("\n" + reason);
			}
			checkResults.add(createCheckResult(sb.toString()));
			Logger.getLogger().info("Part Hold Check - "+ sb.toString());
		}
						
		return checkResults;
	}

}
