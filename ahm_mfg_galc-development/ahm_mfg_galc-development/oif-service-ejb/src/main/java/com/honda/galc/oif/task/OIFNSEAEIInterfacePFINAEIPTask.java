package com.honda.galc.oif.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ProductCarrierDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.system.oif.svc.common.OifErrorsCollector;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;
import com.honda.galc.util.StringUtil;

/**
 * 
 * <h3>OIFNSEAEIInterfacePFINAEIPTask</h3>
 * <p> OIFNSEAEIInterfacePFINAEIPTask is for sending engine shipping data to NSE with PFINAEIP interface file </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TD>Larry Karpov</TD>
 * <TH>Update date</TH>
 * <TD>02/24/2015</TD>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * <TD>RGALCDEV-2709</TD>
 * <TD>In case of deadlock an outgoing file is not created and next time data for some engines might be missing for NSE</TD> 
 * <TD>due to partial update of tracking status of engine(s).  Sending data to NSE first should guarantee all engine data to be sent</TD>
 * <TD>if deadlock happen afterwards those data might be sent twice</TD>
</TR>
 *
 * </TABLE>
 *   
 * @author Ratul Chakravarty<br>
 * May 14, 2014
 *
 */


public class OIFNSEAEIInterfacePFINAEIPTask extends OifAbstractTask implements IEventTaskExecutable {

	private OifErrorsCollector errorsCollector;
	private boolean optionalLoggingEnabled = false;

	public OIFNSEAEIInterfacePFINAEIPTask(String componentId) {
		super(componentId);
		errorsCollector = new OifErrorsCollector(componentId);
	}

	public void execute(Object[] args) {
		try{
			processPFINAEIP();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	public void processPFINAEIP() {
		String interfaceId = getProperty("INTERFACE_ID");
		logger.info("Launching OIFNSEAEIInterfacePFINAEIPTask. Interface Id = " + interfaceId + " Date & Time: " + new java.util.Date());
		optionalLoggingEnabled = getPropertyBoolean("OPTIONAL_LOGGING_ENABLED", false);
		String engineAEOffStatusValue = getProperty("ENGINE_AE_OFF_STATUS");
		String engineShippedProcessPointID = getProperty("SHIPPING_RECV_PPID");
		String[] validProcessPoints = getPropertyArray("VALID_PROCESS_POINTS");
		
		// get engines from product_carrier_tbx table at AE OFF status
		ProductCarrierDao pcd = ServiceFactory.getDao(ProductCarrierDao.class);
		
		String rackID;
		String ein;
		String prodSpecCode;
		String formattedCreateTimestampStr;
		SimpleDateFormat sdf = new SimpleDateFormat(getProperty("TIMESTAMP_FORMAT"));
		Timestamp dbCreateTimestamp;
		String prefix=StringUtil.padRight(PropertyService.getProperty(componentId,"PREFIX",""), Integer.parseInt(getProperty("PREFIX_LENGTH")),' ',true);
		
		@SuppressWarnings("unchecked")
		List<Object[]> recList = pcd.findRackAssociatedEnginesInGivenStatus(engineAEOffStatusValue,validProcessPoints);
		if(recList == null || recList.isEmpty()) {
			logger.info("No data for " + interfaceId);
			return;
		}
		List<String> enginesInRacksList = new ArrayList<String>(); 
		List<String> submittedEngines = new ArrayList<String>(); 
		for (Object[] singleRec : recList) {
			rackID = singleRec[0].toString();
			ein = singleRec[1].toString();
			prodSpecCode = singleRec[2].toString();
			dbCreateTimestamp = (Timestamp) singleRec[3];
			formattedCreateTimestampStr = sdf.format(dbCreateTimestamp);
			formattedCreateTimestampStr = StringUtil.padRight(formattedCreateTimestampStr, Integer.parseInt(getProperty("TIMESTAMP_LENGTH")), '0', false);
			
			String rackIDStr = StringUtil.padLeft(rackID, Integer.parseInt(getProperty("RACK_ID_LENGTH")), ' ');
			String einStr = StringUtil.padLeft(ein, Integer.parseInt(getProperty("EIN_LENGTH")), ' ');
			String mtoStr = StringUtil.padLeft(prodSpecCode, Integer.parseInt(getProperty("PRODUCT_SPEC_CODE_LENGTH")), ' ');
			if(optionalLoggingEnabled)
				logger.info("Rack ID:- " + rackIDStr + ", einStr:- " + einStr + ", mtoStr:-" + mtoStr + ",  prefix:-" + prefix + ",formattedCreateTimestampStr:- " + formattedCreateTimestampStr);
			StringBuffer enginesInRacks = new StringBuffer();
			enginesInRacks.append(rackIDStr);
			enginesInRacks.append(einStr);
			enginesInRacks.append(mtoStr);
			enginesInRacks.append(prefix);
			enginesInRacks.append(formattedCreateTimestampStr);
			enginesInRacksList.add(enginesInRacks.toString());
			submittedEngines.add(ein);
		}
		SimpleDateFormat stsf1 = new SimpleDateFormat(
				PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, "TIMESTAMP_FORMAT"));
		Timestamp ts = new Timestamp((new Date()).getTime());
		String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
		String exportFilePath = new StringBuffer(resultPath).append(interfaceId).append(stsf1.format(ts)).append(".oif").toString();
		try {
			OIFFileUtility.writeToFile(enginesInRacksList, exportFilePath);
		} catch (IOException e) {
			String errorResult = "Failed to write data for: " + interfaceId + " to file: " + exportFilePath;
			logger.error(e, errorResult);
			errorsCollector.emergency(e, errorResult);
			return;
		}
		// send to MQ
		try {
			MQUtility mqu = new MQUtility(this);
			String mqConfig = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG);
			mqu.executeMQSendAPI(interfaceId, mqConfig, exportFilePath);
		} catch (MQUtilityException e) {
			logger.error("MQException occured: " + e);
			errorsCollector.emergency(e, "MQException occured in OIFNSEAEIInterfacePFINAEIPTask.");
			return;
		} catch (Exception e) {
			logger.error(e, "Exception occured. ");
			errorsCollector.emergency(e, "Exception occured in OIFNSEAEIInterfacePFINAEIPTask.");
			return;
		}

		// track it
		TrackingService trackingService = ServiceFactory.getService(TrackingService.class);
		for(String e : submittedEngines) {
			trackingService.track(ProductType.ENGINE, e, engineShippedProcessPointID);
		}
		
	}
	
}
