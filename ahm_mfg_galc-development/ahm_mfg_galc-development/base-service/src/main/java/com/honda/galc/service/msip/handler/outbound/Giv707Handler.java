package com.honda.galc.service.msip.handler.outbound;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.msip.dto.outbound.Giv707Dto;
import com.honda.galc.service.msip.property.outbound.Giv707PropertyBean;
import com.honda.galc.service.productionlot.ProductionLotService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFConstants.DEPARTMENT_CODE;
/*
 * 
 * @author Anusha Gopalan
 * @date Nov 17, 2017
 */
public class Giv707Handler extends BaseMsipOutboundHandler<Giv707PropertyBean> {
	
	private final static String DATE_FORMAT 			=	"yyyyMMdd";
	private final static String TIME_FORMAT 			=	"HHmmss";
	private final static Integer LOT_STATUS 			=	4;
	String errorMsg = null;
	Boolean isError = false;
	
	/**
	 * This interface is only scheduled once a day at the end of the day
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Giv707Dto> fetchDetails() {
		List<Giv707Dto> dtoList = new ArrayList<Giv707Dto>();
		try{
			Boolean isTransmissionPlant	= getPropertyBean().getIsTransmissionPlant();
			if (!isTransmissionPlant )
			{
				return exportRecords();
			}
			else
			{
				return productionProgressAM();
			}
		}catch(Exception e){
			dtoList.clear();
			getLogger().error("Unexpected Error Occured: " + e.getMessage());
			Giv707Dto dto = new Giv707Dto();
			dto.setErrorMsg("Unexpected Error Occured: " + e.getMessage());
			dto.setIsError(true);
			dtoList.add(dto);
			return dtoList;
		}
	}
	
	private List<Giv707Dto> productionProgressAM()
	{	
		final String[] listProcessLocation 	=	getPropertyBean().getDepartments();
		final String propertyDate 			=	getPropertyBean().getInitialDate();
		final String processPointAmOn 		=	getPropertyBean().getProcessPointAmOn();
		final String processPointAmOff 		=	getPropertyBean().getProcessPointAmOff();
		final String plantCode 				=	PropertyService.getSiteName();
		final String[]	activeLineUrl		=	getPropertyBean().getActiveLines();
		final List<Giv707Dto> result = new ArrayList<Giv707Dto>();
		
		Date createDate;
		try {
			createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(propertyDate);		
			List<Object> productionProgressList = new ArrayList<Object>();			
			for (String activeLine : activeLineUrl) {
				
				getLogger().info( "Process the line " + activeLine );
				//get the production result service 
				ProductionLotService productionLotService = HttpServiceProvider.getService(activeLine + OIFConstants.HTTP_SERVICE_URL_PART, ProductionLotService.class);
				for (String processLocation : listProcessLocation) {
					productionProgressList.addAll(productionLotService.getProductionProgress(processLocation, plantCode, createDate, processPointAmOn, processPointAmOff));							
				}						
					
				// copy properties from entity to a DTO
				Date actualDate = new Date();
				
	
				for (Object object : productionProgressList) {
					
					Integer remainingLot = 0;
					final Integer lotSize = (Integer) ((Object[]) object)[8];
					final Integer productionCountOn = (Integer) ((Object[]) object)[11];
					final Integer productionCountOff = (Integer) ((Object[]) object)[12];
					final Integer scrapCount = (Integer) ((Object[]) object)[13];
					final Integer exceptionalCountOn = (Integer) ((Object[]) object)[14];
					final Integer exceptionalCountOff = (Integer) ((Object[]) object)[15];
	
					
					for (int i = 1; i <= 2; i++) {
						if (i == 1) {
							remainingLot = lotSize - productionCountOn - scrapCount + exceptionalCountOn;
						} else {
							remainingLot = lotSize - productionCountOff - scrapCount + exceptionalCountOff;
						}
						ProductionLot productionLot = productionLotService.getProductionLot((String) ((Object[]) object)[16]);  
							//productionLotDao.findByKey((String) ((Object[]) object)[16]);
						if (remainingLot.intValue() <= 0) {
							if (i == 2) {
								// This block only apply in off of process location
								productionLot.setLotStatus(LOT_STATUS);
								productionLotService.updateProductionLot(productionLot);								
							}
							continue;
						}
	
						final Giv707Dto productionProgressDTO = new Giv707Dto();
						productionProgressDTO.setPlanCode(productionLot.getPlanCode());
						productionProgressDTO.setLineNo(productionLot.getLineNo());
						productionProgressDTO.setProcessLocation(productionLot.getProcessLocation());
						productionProgressDTO.setOnOffFlag(i + "");
						productionProgressDTO.setKdLotNo(productionLot.getKdLotNumber());
						productionProgressDTO.setProdSeqNo(productionLot.getLotNumber());
						productionProgressDTO.setMbpn((String) ((Object[]) object)[5]);
						productionProgressDTO.setHesColor((String) ((Object[]) object)[6]);
						productionProgressDTO.setMtoc(productionLot.getProductSpecCode());	//productSpectCode
						productionProgressDTO.setProductionQty(String.format("%5s", productionLot.getLotSize()).replace(' ', '0'));
						productionProgressDTO.setResultQty(String.format("%5s", remainingLot.toString()).replace(' ', '0'));
						productionProgressDTO.setCreatedDate(new SimpleDateFormat(DATE_FORMAT).format(actualDate));
						productionProgressDTO.setCreatedTime(new SimpleDateFormat(TIME_FORMAT).format(actualDate));
						productionProgressDTO.setMinusFlag("");
						productionProgressDTO.setFiller("");
						result.add(productionProgressDTO);
					}
				}
			}
			if ( result != null && result.size() > 0)
			{
				return result;
			}
		} catch (ParseException parseException) {
			getLogger().error("Error when try to convert date for Transmission Production Progress GIV707 Interface " + parseException);
			result.clear();
			Giv707Dto dto = new Giv707Dto();
			dto.setErrorMsg("Error when try to convert date for Transmission Production Progress GIV707 Interface " + parseException);
			dto.setIsError(true);
			result.add(dto);
			return result;
		}
		return result;		
	}
	
	private List<Giv707Dto> exportRecords() {
		final List<Giv707Dto> results = new ArrayList<Giv707Dto>();
		Map<String,String> processPointOnMap = getPropertyBean().getProcessPointOn();
		Map<String,String> processPointOffMap = getPropertyBean().getProcessPointOff();
		// call the web services on the active lines and get production progress data from each line
		List<String> lines = Arrays.asList(getPropertyBean().getTrackingStatus());
		Boolean useSequenceForBuildSequence = getPropertyBean().getUseSeqToBuildSeq();
		Integer  sequenceNumberScale = getPropertyBean().getSequenceNumberScale();
		Boolean excludeListedPlanCodes = getPropertyBean().getExcludeListedPlanCode();
		Boolean allowDBUpdate = getPropertyBean().getAllowDbUpdate();
		List<String> planCodesToExclude = Arrays.asList(getPropertyBean().getPlanCodesToExclude());
		int prodProgressType = 707; // GPP102	
		
		Map<String, List<String>> productionProgressMap;
		for(DEPARTMENT_CODE div : DEPARTMENT_CODE.values()) {
			try {
				String ppOn=null, ppOff=null;
				if(processPointOnMap!=null && processPointOnMap.containsKey(div)){
					ppOn = processPointOnMap.get(div);
				}
				if(processPointOffMap!=null && processPointOffMap.containsKey(div)){
					ppOff = processPointOffMap.get(div);
				}
				List<String> ppOnList = StringUtils.isNotEmpty(ppOn)?Arrays.asList(ppOn.split(",")): null;
				List<String> ppOffList = StringUtils.isNotEmpty(ppOff)?Arrays.asList(ppOff.split(",")): null;
				//get the production result service 
				productionProgressMap = ServiceFactory.getDao(ProductionLotDao.class)
						.getProductionProgress(prodProgressType, ppOnList, ppOffList, div.toString(),
								lines, allowDBUpdate,useSequenceForBuildSequence,sequenceNumberScale, excludeListedPlanCodes, planCodesToExclude);
				results.addAll(createProductionResult(productionProgressMap.get("GIV707")));
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().error("Exception occured while executing task with interface ID : " + getComponentId() + " and department: " + div.name());
				results.clear();
				Giv707Dto dto = new Giv707Dto();
				dto.setErrorMsg("Exception occured while executing task with interface ID : " + getComponentId() + " and department: " + div.name());
				dto.setIsError(true);
				results.add(dto);
				return results;
			}
		}
		return results;
	}
	
	private List<Giv707Dto> createProductionResult(List<String> results) {
		List<Giv707Dto> productionResults = new ArrayList<Giv707Dto>();
		
		for(String result : results) {
			Giv707Dto giv707Dto = new Giv707Dto();
			
			giv707Dto.setPlanCode(result.substring(0,11));
			giv707Dto.setLineNo(result.substring(11,13));
			giv707Dto.setProcessLocation(result.substring(13,15));
			giv707Dto.setOnOffFlag(result.substring(15,16));
			giv707Dto.setProdSeqNo(result.substring(16,28));
			giv707Dto.setMtoc(result.substring(28,50));
			giv707Dto.setKdLotNo(result.substring(50,68));
			giv707Dto.setMbpn(result.substring(68,86));
			giv707Dto.setHesColor(result.substring(86,97));
			giv707Dto.setFiller(result.substring(97,126));
			giv707Dto.setProductionQty(result.substring(127,131));
			giv707Dto.setResultQty(result.substring(131,136));
			giv707Dto.setCreatedDate(result.substring(136,144));
			giv707Dto.setCreatedTime(result.substring(144,150));
			
			productionResults.add(giv707Dto);
		}
		
		return productionResults;
	}
}
