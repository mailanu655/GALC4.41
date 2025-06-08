package com.honda.galc.service.productionresult;

/**
 * 
 * <h3>ProductionResultServiceImpl</h3>
 * <p> ProductionResultServiceImpl is for GIV705 </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Ratul Chakravarty<br>
 * June 20, 2014
 *
 */


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.StandardScheduleDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.property.EngineLinePropertyBean;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.property.IpuLinePropertyBean;
import com.honda.galc.service.ProductionResultService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.AsynchProductionReportPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

public class ProductionResultServiceImpl implements ProductionResultService {

	private Logger logger = Logger.getLogger("ProductionResultServiceImpl");

	/**
	 * @param inputMap contains a map of input parameters.
	 * @return a map of lists of strings. The strings are the formatted records.
	 * @author Ratul Chakravarty
	 */
	public Map<String, List<String>> getProductionResults(String productionTimestampStartStr,String productionTimestampEndStr, String serviceId, boolean isJapanVINLeftJustified) {

		ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);		

		Long prodTimestampStartLong = Long.parseLong(productionTimestampStartStr);
		Timestamp startTimestamp = new Timestamp(prodTimestampStartLong);
		
		Long prodTimestampEndLong = Long.parseLong(productionTimestampEndStr);
		Timestamp endTimestamp = new Timestamp(prodTimestampEndLong);

		StringBuilder records = new StringBuilder();
		List<String> retList = new ArrayList<String>();
		List<String> errorsList = new ArrayList<String>();
		Map<String, List<String>> finalReturnData = new HashMap<String, List<String>>();
		FrameLinePropertyBean framePropertyBean = PropertyService.getPropertyBean(FrameLinePropertyBean.class, serviceId);
		EngineLinePropertyBean enginePropertyBean = PropertyService.getPropertyBean(EngineLinePropertyBean.class, serviceId);
		IpuLinePropertyBean ipuPropertyBean = PropertyService.getPropertyBean(IpuLinePropertyBean.class, serviceId);
		
		List<String> weOffResCntPPId = PropertyService.getPropertyList(serviceId, "WE_OFF_RES_CNT_PPID");
		List<String> paOffResCntPPId = PropertyService.getPropertyList(serviceId, "PA_OFF_RES_CNT_PPID");
		List<String> afOffResCntPPId = PropertyService.getPropertyList(serviceId, "AF_OFF_RES_CNT_PPID");
		List<String> iaOffResCntPPId = PropertyService.getPropertyList(serviceId, "IA_OFF_RES_CNT_PPID");
		String aeOffResCntPPId1 = PropertyService.getProperty(serviceId, "AE_OFF_RES_PPID_1");
		String aeOffResCntPPId2 = PropertyService.getProperty(serviceId, "AE_OFF_RES_PPID_2");
		String iaOffResCntPPId1 = PropertyService.getProperty(serviceId, "IA_OFF_RES_PPID_1");
		String iaOffResCntPPId2 = PropertyService.getProperty(serviceId, "IA_OFF_RES_PPID_2");
		
		String frameScrapLineId = framePropertyBean.getScrapLineId();
		String engineScrapLineId = enginePropertyBean.getScrapLineId();
		String ipuScrapLineId = ipuPropertyBean.getScrapLineId();

		try {

			// AF production result
			List<Object[]> afProdResultList = productResultDao.getAFProductionResult(startTimestamp, endTimestamp, afOffResCntPPId);
			createFormattedRecord(records, retList, afProdResultList, isJapanVINLeftJustified);


			// AE production result
			List<Object[]> aeProdResultList = productResultDao.getAEProductionResult(startTimestamp, endTimestamp, aeOffResCntPPId1, aeOffResCntPPId2);
		    createFormattedRecord(records, retList, aeProdResultList, true); //left justified for Engine Number


			// PA production result
			List<Object[]> paProdResultList = productResultDao.getPAProductionResult(startTimestamp, endTimestamp, paOffResCntPPId);
			createFormattedRecord(records, retList, paProdResultList, isJapanVINLeftJustified);
				

			// for WE
			List<Object[]> weProdResultList = productResultDao.getWEProductionResult(startTimestamp, endTimestamp, weOffResCntPPId);
			createFormattedRecord(records, retList, weProdResultList, isJapanVINLeftJustified);
				
			// for IA
			List<Object[]> iaProdResultList = productResultDao.getIAProductionResult(startTimestamp, endTimestamp, iaOffResCntPPId1, iaOffResCntPPId2);
			createFormattedRecord(records, retList, iaProdResultList, true);				

			// *****************************
			// Get Scrapped VINs
			// *****************************
			List<Object[]> afScrappedVINsList =  null;
			if (StringUtils.isBlank(frameScrapLineId)) {
				afScrappedVINsList = new ArrayList<Object[]>();
			} else {
				afScrappedVINsList = productResultDao.getFrameScrap(startTimestamp, endTimestamp, Arrays.asList(frameScrapLineId));
			}
			/***********************************************************************************************************************************************
			 * So for each "scrap VIN we find, we want to see what areas (WE, PA
			 * and AF) that VIN has already passed through and sent a previous
			 * result for. We need to send a counter to that result that tells
			 * them the VIN was scrapped. Since this file is used to backflush
			 * parts out of inventory, this means from a business prospective
			 * that if we scrap a VIN, we can reuse those parts, so we want to
			 * add them back to our inventory and NOT place an order for more
			 * parts. That is the general "gist" of this change because
			 * additional parts are being ordered and not used.
			 ***********************************************************************************************************************************************/
			for (Object[] afScrappedVinsArr : afScrappedVINsList) {
				String tempVal = afScrappedVinsArr[3].toString();

				List<Object> weOffResultCntList = productResultDao.getOffResultCnt(tempVal, weOffResCntPPId);

				List<Object> paOffResultCntList = productResultDao.getOffResultCnt(tempVal, paOffResCntPPId);

				List<Object> afOffResultCntList = productResultDao.getOffResultCnt(tempVal, afOffResCntPPId);
				
				List<Object> iaOffResultCntList = productResultDao.getOffResultCnt(tempVal, iaOffResCntPPId);

				if (weOffResultCntList != null && weOffResultCntList.size() > 0 && new Integer(weOffResultCntList.get(0).toString()) > 0) {
					createScrappedFormattedRecord(records, retList, afScrappedVinsArr, "WE", isJapanVINLeftJustified);
				}
				if (paOffResultCntList != null && paOffResultCntList.size() > 0 && new Integer(paOffResultCntList.get(0).toString()) > 0) {
					createScrappedFormattedRecord(records, retList, afScrappedVinsArr, "PA", isJapanVINLeftJustified);
				}
				if (afOffResultCntList != null && afOffResultCntList.size() > 0 && new Integer(afOffResultCntList.get(0).toString()) > 0) {
					createScrappedFormattedRecord(records, retList, afScrappedVinsArr, "AF", isJapanVINLeftJustified);
				}
				if (iaOffResultCntList != null && iaOffResultCntList.size() > 0 && new Integer(iaOffResultCntList.get(0).toString()) > 0) {
					createScrappedFormattedRecord(records, retList, afScrappedVinsArr, "IA", isJapanVINLeftJustified);
				}
			}

			// *****************************
			// Get Scrapped Ipu
			// *****************************
			List<Object[]> iaScrappedVINsList =  null;
			if (StringUtils.isBlank(ipuScrapLineId)) {
				iaScrappedVINsList = new ArrayList<Object[]>();
			} else {
				iaScrappedVINsList = productResultDao.getEngineScrap(startTimestamp, endTimestamp, Arrays.asList(ipuScrapLineId));
			}
			for(Object[] iaScrappedVinsArr : iaScrappedVINsList) {
				createScrappedFormattedRecord(records, retList, iaScrappedVinsArr, "IA", true); 
			}
			
			// *****************************
			// Get Scrapped Engines
			// *****************************
			List<Object[]> aeScrappedVINsList =  null;
			if (StringUtils.isBlank(engineScrapLineId)) {
				aeScrappedVINsList = new ArrayList<Object[]>();
			} else {
				aeScrappedVINsList = productResultDao.getEngineScrap(startTimestamp, endTimestamp, Arrays.asList(engineScrapLineId));
			}
			for(Object[] afScrappedVinsArr : aeScrappedVINsList) {
				createScrappedFormattedRecord(records, retList, afScrappedVinsArr, "AE", true); //left justified for Engine
			}

			finalReturnData.put("GIV705", retList);
		} catch (Exception e) {
			String errMsg = "Exception occured in ProductionResultServiceImpl.getProductionResults(HashMap<String, String> inputMap) method. The exception message is:- " + e.getMessage();
			logger.error(errMsg);
			errorsList.add(errMsg);
			finalReturnData.put("GIV705_ERRORS", errorsList);
			e.printStackTrace();
		}
		return finalReturnData;
	}

	private void createFormattedRecord(StringBuilder records, List<String> retList, List<Object[]> afProdResultList, boolean isJapanVINLeftJustified) {
		for (Object[] singleRecArr : afProdResultList) {
			records.delete(0, records.length());
			records.append(singleRecArr[0].toString());//plan code
			records.append(singleRecArr[1].toString());//line no
			records.append(singleRecArr[2].toString());//processlocation
			records.append(StringUtil.padRight(ProductNumberDef.justifyJapaneseVIN(singleRecArr[3].toString(), isJapanVINLeftJustified), ProductNumberDef.VIN.getLength(),' ', true));
			records.append(singleRecArr[4].toString());//productionlot
			records.append(singleRecArr[5].toString());//actual time
			records.append(singleRecArr[6].toString());//actual time
			records.append(singleRecArr[7].toString());//actual time
			records.append(singleRecArr[8].toString());//actual time
			records.append(singleRecArr[9].toString());//actual time
			records.append(singleRecArr[10].toString());//actual time
			records.append(singleRecArr[11].toString());//productspeccode
			records.append("                    "); // Band-No. (20)
			records.append(singleRecArr[12].toString()); // KD-Lot-No. (18)
			records.append("                  "); // Part-No. (18)
			records.append("           "); // Part-Clr_CD (11)
			if(singleRecArr[13] != null) {
				records.append(StringUtil.padLeft(singleRecArr[13].toString(),5,'0')); // BOS-Serial-No (5)
			}else {
				records.append("     ");
			}
			records.append("                                              ");	// Filler (46)
			records.append("0"); // Result-Flg (1)
			records.append(singleRecArr[14].toString()); // Cancel-Flg. (1)
			
			retList.add(records.toString());
		}
	}

	private void createScrappedFormattedRecord(StringBuilder records, List<String> retList, Object[] singleRecArr, String dept, boolean isJapanVINLeftJustified) {
		records.delete(0, records.length());
		records.append(singleRecArr[0].toString());
		records.append(singleRecArr[1].toString());
		records.append(dept);
		records.append(StringUtil.padRight(ProductNumberDef.justifyJapaneseVIN(singleRecArr[3].toString(), isJapanVINLeftJustified), ProductNumberDef.VIN.getLength(),' ', true));
		records.append(singleRecArr[4].toString());
		records.append(singleRecArr[5].toString());
		records.append(singleRecArr[6].toString());
		records.append(singleRecArr[7].toString());
		records.append(singleRecArr[8].toString());
		records.append(singleRecArr[9].toString());
		records.append(singleRecArr[10].toString());
		records.append(singleRecArr[11].toString());
		records.append("                    "); // Band-No. (20)
		records.append(singleRecArr[12].toString()); // KD-Lot-No. (18)
		records.append("                  "); // Part-No. (18)
		records.append("           "); // Part-Clr_CD (11)
		
		if(singleRecArr[13] != null) {
			records.append(StringUtil.padLeft(singleRecArr[13].toString(),5,'0')); // BOS-Serial-No (5)
		}else {
			records.append("     ");
		}
		records.append("                                             ");	// Filler (45)
		records.append("2");// Cancel Reason Code
		records.append("0"); // Result-Flg (1)
		records.append(singleRecArr[14].toString()); // Cancel-Flg. (1)
		retList.add(records.toString());
	}

	public boolean isProductionDay(String processLocation, String productionTimestampStr) {
		StandardScheduleDao ssd = ServiceFactory.getDao(StandardScheduleDao.class);
		return ssd.isProductionDay(processLocation, productionTimestampStr);
	}

	public boolean isProductionDayByProperty(String serviceId, String productionTimestampStr) {
		String processLocation = PropertyService.getProperty(serviceId, "CHECK_ISWORK_DEPARTMENT", "AF");
		StandardScheduleDao ssd = ServiceFactory.getDao(StandardScheduleDao.class);
		return ssd.isProductionDay(processLocation, productionTimestampStr);
	}
	
	public Map<String, List<Object[]>> getAsynchProductionReport(String componentId, Timestamp startTime, Timestamp endTime) {
		// refresh property to make sure get the updated LAST_RUN timestamp
		PropertyService.refreshComponentProperties(componentId);
		AsynchProductionReportPropertyBean properties = PropertyService.getPropertyBean(AsynchProductionReportPropertyBean.class, componentId);

		ProductResultDao productResultDao = ServiceFactory.getDao(ProductResultDao.class);
		
		Map<String, List<Object[]>> finalReturnData = new HashMap<String, List<Object[]>>();
		
		Map<String, String> planCodePPMap = properties.getPlanCodePpMap();
		
		if (planCodePPMap == null || planCodePPMap.isEmpty()) {
			logger.error("There is no plan code to process points mapping defined.");
			return finalReturnData;
		}
		
		Iterator<String> it = planCodePPMap.keySet().iterator();
		String planCode = "";
		String ppString = "";
		
		try {
			//loop through PLAN_CODE
			while (it.hasNext()) {
				planCode = it.next().toString();
				ppString = planCodePPMap.get(planCode);
				StringTokenizer tokenizer = new StringTokenizer(ppString, ",");
				String processPoints = "";
		    	while(tokenizer.hasMoreTokens()){
		    		if(!processPoints.equals("")){
		    			processPoints = processPoints + ",\'" + tokenizer.nextToken().trim() + "\'";
		    		}
		    		else{
		    			processPoints = "\'" + tokenizer.nextToken() + "\'";
		    		}
		    	}
		    	
				// raw query results
		    	List<Object[]> result = productResultDao.getProductSpecCnt(processPoints, startTime, endTime, planCode);
		    	
		    	finalReturnData.put(planCode, result);	
			}
			
		} catch (Exception e) {
			String errMsg = "Exception occured in ProductionResultServiceImpl.getAsynchProductionReport(). The exception message is:- " + e.getMessage();
			logger.error(errMsg);
		}
		return finalReturnData;
	}
	
	/**
	 * Get the production progress for each process point (WE, PA, AF, AE, AM).
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object[]> getProductionResult( final String processPoints, final String startDate, final String endDate )
	{
		ProductResultDao	productResultDao	= ServiceFactory.getDao( ProductResultDao.class );
		List<Object[]>		productionResult	= productResultDao.getProductionResult( processPoints, startDate, endDate);
		return productionResult;
	}
	
	/**
	 * Gets the Scrap production progress for product.
	 * AE = Engine data and use the scrap query for engine (gal131tbx)
	 * AF = Frame data and use the scrap query for frame (gal143tbx)
	 * AM = Transmission data an use the scrap query for transmission (mission_tbx)
	 * @param processLocation
	 * @param processPoints
	 * @param startDate
	 * @param endDate
	 * @param trackingStatus
	 * @return
	 */
	public List<Object[]> getProductionResult ( final String processLocation
												, final String processPoints
												, final String startDate
												, final String endDate
												, final String trackingStatus )
	{
		//get the instance DAO
		ProductResultDao	productResultDao	= ServiceFactory.getDao( ProductResultDao.class );
		//execute the query
		List<Object[]>		productionResult	= productResultDao.getProductionResult( processLocation
																						, processPoints
																						, startDate
																						, endDate
																						, trackingStatus );
		return productionResult;
	}
}
