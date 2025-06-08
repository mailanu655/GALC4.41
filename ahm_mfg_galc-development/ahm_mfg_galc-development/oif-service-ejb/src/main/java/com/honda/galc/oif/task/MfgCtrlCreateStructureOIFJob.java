package com.honda.galc.oif.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.ApplicationConstants;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.conf.MCOrderStructure;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.enumtype.StructureCreateMode;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.StructureCreateService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifServiceEMailHandler;
import com.honda.galc.vios.dto.PddaPlatformDto;

public class MfgCtrlCreateStructureOIFJob  extends OifTask<Object> implements
IEventTaskExecutable {

	private static final String NOTHING_TO_PROCESS = " No order(s) to process on ";
	private static final String MESSAGE_SUFFIX1 = "OIF Job completed today ";
	private static final String MESSAGE_SUFFIX2 = " and the following message for you";
	private static final String EMAIL_SUBJECT = "OIF Job for Manufacturing Control Maintenance completed on : ";	
	private static final String EXCEPTION_EMAIL_SUBJECT1 = "Exception caught while processing OIF Job of Manufacturing Control Maintanence on : ";
	private static final String EXCEPTION_MESSAGE1 = "Exception caught while processing OIF Job for Manufacturing Control Maintenance.\nPlease report the issue to the support team";
	private static final String THANKS = "\n\nMessage From";
	private static String THANKS_MESSAGE = "\nManufacturing Control Maintenance OIF Job";
	
	private static final String DIVISION_IDS = "DIVISION_IDS";
	private static final String NO_STRUCTURE = "NO_STRUCTURE";
	private static final String ORDER_LIST = "ORDER_LIST";
	private static final String ERROR_LIST = "ERROR_LIST";
	private static final String ENVIRONMENT_INFO = "ENVIRONMENT_INFO";
	private static String SERVER_ENVIRONMENT = null;
	private static final String ACTION_REQUIRED = "\n\n*********** Action Required : Please contact support team to configure environment info. For ex PMC Prod or AEI Prod ***********";
	private static final String DEFAULT_VIOS = "DEFAULT_VIOS";
	private static final String CANT_RUN = "\n\nSorry...Structure Create Scheduler will not create structures if the STRUCTURE_CREATE_MODE is PROCESS_POINT_MODE. Please contact support team. ";
	
	public MfgCtrlCreateStructureOIFJob(String name) {
		super(name);
	}

	@Override
	public void execute(Object[] args) {
		Timestamp startTs = new Timestamp(System.currentTimeMillis());
		String processingDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(startTs);
		Logger.getLogger().info("MfgCtrl Populate Order structure OIF task started");
		OifServiceEMailHandler emailHandler = new OifServiceEMailHandler(getName());
		String messageContent = null;
		
		/* User should have configured below COMPONENT_ID & PROPERTY_KEY  in GAL489TBX to run this task.
		 * COMPONENT_ID :  OIF_MFG_CTRL_CREATE_STRUCTURE
		 * PROPERTY_KEY : DIVISION_IDS  
		 * 
		 * Release 1.59 : OIF Job modified to run only for STRUCTURE_CREATE_MODE = DIVISION_MODE.
		 * */
		
		try {
			
			String mode = ServiceFactory.getDao(ComponentPropertyDao.class).findValueForCompIdAndKey(ApplicationConstants.DEFAULT_VIOS, ApplicationConstants.STRUCTURE_CREATE_MODE);
			
			if(!StringUtils.isNotBlank(mode))
				mode = StructureCreateMode.DIVISION_MODE.toString();
			
			SERVER_ENVIRONMENT = (getProperty(ENVIRONMENT_INFO)) != null? getProperty(ENVIRONMENT_INFO) : " *** No Environment Info found *** ";
			String[] configuredDivIdLst = getPropertyArray(DIVISION_IDS);
			
			if(StringUtils.equalsIgnoreCase(mode, StructureCreateMode.DIVISION_MODE.toString())) {
			
				Map<String, List<String>> responseFromOrdCreate = findOrCreateOrders(configuredDivIdLst);
	
				messageContent = MESSAGE_SUFFIX1 + processingDate + MESSAGE_SUFFIX2;
				
				String newOrders = constructMsgStr(responseFromOrdCreate.get(ORDER_LIST));
				String errorOrders = constructMsgStr(responseFromOrdCreate.get(ERROR_LIST));
				String noStructures = constructMsgStr(responseFromOrdCreate.get(NO_STRUCTURE));
				
				if(newOrders != null || errorOrders != null || noStructures != null){
					if(newOrders != null)
						messageContent = messageContent + "\n\nFollowing Order(s) has been processed and loaded with structure details : " + newOrders;
					
					if(errorOrders != null)
						messageContent = messageContent + "\n\nErrored while processing following order(s), Please contact support team : " + errorOrders;
							
					if(noStructures != null)
						messageContent = messageContent + "\n\nFollowing Order(s) for Spec code and Division Ids does not have proper details in the system, Please contact support team: " + noStructures;
	
				}else{
					
					messageContent = MESSAGE_SUFFIX1 + processingDate + "\n\n"+NOTHING_TO_PROCESS + processingDate;
				}
				
				if(SERVER_ENVIRONMENT.trim().equalsIgnoreCase("*** No Environment Info found ***"))
					THANKS_MESSAGE = THANKS_MESSAGE + ACTION_REQUIRED;
			} else {
				messageContent = MESSAGE_SUFFIX1 + processingDate + CANT_RUN ;
			}
			emailHandler.delivery(EMAIL_SUBJECT + processingDate +" @ " + SERVER_ENVIRONMENT,messageContent+THANKS+THANKS_MESSAGE);
		} catch (Exception e) {
			emailHandler.delivery(EXCEPTION_EMAIL_SUBJECT1 + processingDate,EXCEPTION_MESSAGE1+THANKS+THANKS_MESSAGE);
			errorsCollector.error("EXCEPTION_EMAIL_SUBJECT1");
		}
		

	}
	
	
	private Map<String, List<String>> findOrCreateOrders(String[] configuredDivIdLst){
		
		List<String> processedOrdLst = new ArrayList<String>();
		List<String> erroredOrdLst = new ArrayList<String>();
		HashSet<String> newDivIdLst = new HashSet<String>();
		
		Map<String, List<String>> responseMap = new HashMap<String, List<String>>();
		
		List<String> unmappedOrderIdLst = ServiceFactory.getDao(PreProductionLotDao.class).getUnmappedOrderIds();
		
		try {
			for(String unMappedOrderId: unmappedOrderIdLst) {
				
				PreProductionLot preProdLot = ServiceFactory.getDao(PreProductionLotDao.class).findByKey(unMappedOrderId.trim());
				// Get all mapped division ids from GPCS Division
				List<String> gpcsDivIdsForOrderId = ServiceFactory.getDao(GpcsDivisionDao.class).getDivIdLst(preProdLot.getPlantCode(), preProdLot.getLineNo(),preProdLot.getProcessLocation());
				
				/* Iterate each unmapped order is for each configured division ids */
				for(String divisionId : configuredDivIdLst){
					 
					for(String gpcsDivId : gpcsDivIdsForOrderId){
						if(gpcsDivId.trim().equals(divisionId)){
							try{
								MCOrderStructure mcOrdStruc =  ServiceFactory.getService(StructureCreateService.class).findOrCreateOrderStructure(preProdLot.getProductionLot().trim(), divisionId, new PddaPlatformDto());
								
								processedOrdLst.add("[ "+mcOrdStruc.getId().getOrderNo()+" , "+ mcOrdStruc.getId().getDivisionId()+" , "+ mcOrdStruc.getProductSpecCode()+" ]"); 
								
							}catch(Exception ex){
								if(ex.getMessage().trim().equalsIgnoreCase("java.lang.Exception: No Structure for Division Id")){
									newDivIdLst.add("[ "+preProdLot.getProductionLot().trim() +" , "+ divisionId +" , "+ preProdLot.getProductSpecCode()+" ]");
								}else{
									erroredOrdLst.add("[ "+preProdLot.getProductionLot().trim() +" , "+ divisionId +" , "+ preProdLot.getProductSpecCode()+" ]");
								}
								Logger.getLogger().error("Error captured while processing order id: " + "[ "+preProdLot.getProductionLot().trim() +" , "+ divisionId +" , "+ preProdLot.getProductSpecCode()+" ]" );
								errorsCollector.error("Error captured while processing order id ");
							}
						}
					}
				}
			}
			responseMap.put(NO_STRUCTURE, new ArrayList<String>(newDivIdLst));
			responseMap.put(ORDER_LIST, processedOrdLst);
			responseMap.put(ERROR_LIST, erroredOrdLst);
			setSuccessCount(processedOrdLst.size());
			setFailedCount(erroredOrdLst.size()+newDivIdLst.size());
		} catch (Exception e) {
			e.printStackTrace();
			errorsCollector.error(e.toString());
		}
		
		return responseMap;
	}
	
	private String constructMsgStr(List<String> msgLst){
		String message = null;
		
		if(msgLst != null && msgLst.size() == 0 ) return null;
		
		for(String msgStr : msgLst){
			if(message == null)
				message = msgStr; 
			else{
				message = message + " , " + msgStr;
			}
		}
		
		return message;
	}
	
}
