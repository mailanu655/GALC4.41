package com.honda.galc.checkers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.device.dataformat.BaseProductCheckerData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.check.PartResultData;

/**
 * @author Brandon Kroeger
 * @date Jul 17, 2015
 */
public class OutstandingMCOperationChecker extends AbstractBaseChecker<BaseProductCheckerData> {

	public String getName() {
		return this.getClass().getSimpleName();
	}

	public CheckerType getType() {
		return CheckerType.Application;
	}

	public int getSequence() {
		return 0;
	}

	public List<CheckResult> executeCheck(BaseProductCheckerData inputData) {
		List<CheckResult> checkResults = new ArrayList<CheckResult>();
		
		BaseProduct product = null;
		if ((inputData.getProductType() != null) && (inputData.getProductId() != null)) {
			product = ProductTypeUtil.getProductDao(inputData.getProductType()).findBySn(inputData.getProductId());
		}
		
		ProcessPoint currentProcessPoint = null;
		if (inputData.getCurrentProcessPoint() != null){
			currentProcessPoint = ServiceFactory.getDao(ProcessPointDao.class).findByKey(inputData.getCurrentProcessPoint());
		}
				
		if (product == null) {
			checkResults.add(createCheckResult("Outstanding MC Operations Check Failed - The Product does not exist."));
			return checkResults;
		}

		if (currentProcessPoint == null) {;
			checkResults.add(createCheckResult("Outstanding MC Operations Check Failed - The specified process point does not exist."));
			return checkResults;
		}
		
		//Load Properties for process point list and filter type		
		String ppList = PropertyService.getProperty(inputData.getCurrentProcessPoint(), "PROCESS_POINT_LIST");
		String filterType = PropertyService.getProperty(inputData.getCurrentProcessPoint(), "PROCESS_POINT_LIST_FILTER_TYPE");
		String divisionList = PropertyService.getProperty(inputData.getCurrentProcessPoint(), "DIVISION_LIST_TO_CHECK");
		
		ProductCheckUtil checkUtil = new ProductCheckUtil(product, currentProcessPoint);	
		List<PartResultData> partResults = checkUtil.outstandingMCOperationCheck(ppList, filterType, divisionList);
		if (partResults.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append("Outstanding MC Operations Check Failed for below parts :");
			String tmpProcessPoint = "";
			for (PartResultData partResult : partResults) {
				if(StringUtils.isEmpty(tmpProcessPoint) || !tmpProcessPoint.equals(partResult.procecssPointId)){
					if(!StringUtils.isEmpty(tmpProcessPoint))  sb.append("\n");
					tmpProcessPoint = partResult.procecssPointId;
					sb.append("\nIncomplete/Missing parts in process point '"+tmpProcessPoint+"/ "+partResult.procecssPointName+"' : ");
				}
				sb.append("\n" +partResult.part_Name+"  "+StringUtils.trim(partResult.part_Desc));								
			}
			checkResults.add(createCheckResult(sb.toString()));
		}		
		
		return checkResults;
	}
}
