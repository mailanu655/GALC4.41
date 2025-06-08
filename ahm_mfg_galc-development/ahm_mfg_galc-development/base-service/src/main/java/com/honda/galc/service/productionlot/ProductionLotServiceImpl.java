package com.honda.galc.service.productionlot;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

public class ProductionLotServiceImpl implements ProductionLotService {
	private Logger logger = Logger.getLogger("ProductionLotServiceImpl");

	@Autowired
	public ProductionLotDao productionLotDao;
	
	@Autowired
	public PreProductionLotDao preProductionLotDao;

	public List<Object[]> getProcessingBody(String componentId) {
		List<Object[]> resultRecs = new ArrayList<Object[]>(); 
		List<String> departments = PropertyService.getPropertyList(componentId, "DEPARTMENTS");
		Boolean isDebug = PropertyService.getPropertyBoolean(componentId, "DEBUG", false);
		int totalRecordCount = 0;
		
		for (String div : departments) {
			List<String> processPoints = PropertyService.getPropertyList(componentId, createDepartmentKey("PROCESS_POINT", div));
			List<String> lines = PropertyService.getPropertyList(componentId, createDepartmentKey("TRACKING_STATUS", div));
			List<Object[]> processingBodyRecs = productionLotDao.getProcessingBody(div, processPoints, lines);
			if (processingBodyRecs != null) {
				totalRecordCount += processingBodyRecs.size();
				resultRecs.addAll(processingBodyRecs);
				if (isDebug) {
					logger.info("Total Record Count: " + totalRecordCount + ". " + div + " Department Record Count : " + processingBodyRecs.size());
				}
			}
		}
		return resultRecs;
	}

	public Map<String, List<String>> getProductionProgress(String componentId, Integer prodProgressType, Boolean allowDBUpdate) {
		String department = "";
		switch (prodProgressType / 1000) {
		case 1:
			department = "AE";
			break;
		case 2:
			department = "AF";
			break;
		case 3:
			department = "PA";
			break;
		case 4:
			department = "WE";
			break;
		case 5:
			department = "IA";
			break;
		
		}
		List<String> departments = PropertyService.getPropertyList(componentId, "DEPARTMENTS");
		List<String> lines = PropertyService.getPropertyList(componentId, "TRACKING_STATUS");
		Map<String, List<String>> productionProgressMap = new HashMap<String, List<String>>();
		if(departments.contains(department)) {
			List<String> processPointOn = PropertyService.getPropertyList(componentId, createDepartmentKey("PROCESS_POINT_ON", department));
			List<String> processPointOff = PropertyService.getPropertyList(componentId, createDepartmentKey("PROCESS_POINT_OFF", department));
			Boolean useSequenceForBuildSequence = PropertyService.getPropertyBoolean(componentId, "USE_SEQ_TO_BUILD_SEQ", false);
			Integer  sequenceNumberScale = PropertyService.getPropertyInt(componentId, "SEQUENCE_NUMBER_SCALE",4);
			Boolean excludeListedPlanCodes = PropertyService.getPropertyBoolean(componentId, "EXCLUDE_LISTED_PLAN_CODES", false);
			List<String> planCodesToExclude = PropertyService.getPropertyList(componentId, "PLAN_CODES_TO_EXCLUDE");
			List<String> exceptionProcessPoints = PropertyService.getPropertyList(componentId, "EXCEPTION_PROCESS_POINTS");
			Boolean useLinkedListForSequence=PropertyService.getPropertyBoolean(componentId, "USE_LINKED_LIST_FOR_SEQ", false);
			Boolean useCustomRunTimestamp=PropertyService.getPropertyBoolean(componentId, "USE_CUSTOM_RUN_TIMESTAMP", false);
			Timestamp runTimeStamp=null;
			if(useCustomRunTimestamp) {
				try {
					String strProdDate = PropertyService.getProperty(componentId, "CUSTOM_RUN_TIMESTAMP","");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
					java.util.Date customStartProdDate = sdf.parse(strProdDate);
					runTimeStamp = new Timestamp(customStartProdDate.getTime());
				} catch (ParseException e) {
					logger.info("Cannot parse custom production date value, defaulting to current date.");
				}
			}else {
				runTimeStamp = new Timestamp(System.currentTimeMillis());
			}
			
			// change to exclude lots of listed plants
			if(useLinkedListForSequence) {
				productionProgressMap = preProductionLotDao.getProductionProgress(prodProgressType % 1000, processPointOn, processPointOff, department, lines, allowDBUpdate,useSequenceForBuildSequence,sequenceNumberScale, excludeListedPlanCodes, planCodesToExclude,exceptionProcessPoints,runTimeStamp);
			}else {
				productionProgressMap = productionLotDao.getProductionProgress(prodProgressType % 1000, processPointOn, processPointOff, department, lines, allowDBUpdate,useSequenceForBuildSequence,sequenceNumberScale, excludeListedPlanCodes, planCodesToExclude);
			}
		}
		return productionProgressMap;
	}

	private String createDepartmentKey(String key, String div) {
		return (new StringBuilder(key)).append("{").append(div).append("}").toString();
	}
	
	/**
	 * Method to get the Processing Body information receiving the process point and the tracking line id
	 * @param processPoints
	 * @param lineIds
	 * @return
	 */
	public List<Object[]> getProcessingBody ( final String processPoints, final String lineIds )
	{
		ProductionLotDao productionLotDao	=	ServiceFactory.getDao( ProductionLotDao.class );
		return productionLotDao.getProcessingBody(processPoints, lineIds);
	}
	
	
	public List<Object> getProductionProgress(final String processLocation, final String plantCode, final java.util.Date createTimestamp, final String processPointAmOn, final String processPointAmOff) {	
		return productionLotDao.getProductionProgress(processLocation, plantCode, createTimestamp, processPointAmOn, processPointAmOff);
	}
	
	public ProductionLot getProductionLot(final String productId)
	{
		return productionLotDao.findByKey(productId);
	}
	
	public void updateProductionLot(final ProductionLot productionLot)
	{		
		productionLotDao.update(productionLot);
	}

}
