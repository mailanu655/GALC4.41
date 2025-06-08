package com.honda.galc.oif.task;

import java.io.IOException;
import java.util.HashMap;

import com.google.gson.Gson;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.RegularMQClient;
import com.ibm.mq.MQException;

/**
 * 
 * <h3>OIFPartDataImport Class description</h3>
 * <p> OIFPartDataImport description </p>
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
 * February 24, 2014
 *
 *
 */
/** * * 
* @version 1.0
* @author Ratul Chakravarty 
* @since February 24, 2014
*/

public class OIFPartDataImportTask extends OifAbstractTask implements IEventTaskExecutable {
	
	public OIFPartDataImportTask(String pObjectName) {
		super(pObjectName);
		componentId = pObjectName;
	}

	public void execute(Object[] args) {
		mainMethod();
	}

	private void mainMethod() {
		
		logger.info("Launching OIFPartDataImport. Interface Id = " + componentId + " Date & Time: " + new java.util.Date());
		RegularMQClient rmq = null;
		int noOfRetrievedMessages = 0;
		int noOfProcessedMessages = 0;
		boolean toDelete = false;
		String msg = null;
		try {
			// retrieve the messages from the queue 1 by 1 and insert/update the database
			while(true) { // run until the queue is empty
				if(rmq == null) {
					rmq = getRegMQClient();
					logger.info(rmq.toString());
				}
				msg = rmq.removeCurrentMsgAndFetchNextMsg(toDelete);
				if(!msg.contains("\"TEXT_ID\":\"0411\"")) {
					toDelete = false;
					continue;
				}
				noOfRetrievedMessages++;
				logger.info("retrieved message :- " + msg);
				processMsg(msg);
				noOfProcessedMessages++;
				toDelete = true;
			}
		} catch (MQException e) {
			if(e.completionCode == MQException.MQCC_FAILED && e.reasonCode == MQException.MQRC_NO_MSG_AVAILABLE) {
				logger.info("Message Queue is empty.");
			} else {
				logger.error("MQException occured: " + e);
				e.printStackTrace();
			}
		} catch (IOException e) {
			logger.error("IOException occured: " + e);
			e.printStackTrace();
		} catch(Exception e) {
			logger.error("Exception occured: " + e);
			e.printStackTrace();
		} finally {
			try {
				rmq.finalize(); // clean up
			} catch (MQException e) {
				logger.error("Exception in closing MQ connection: " + e.getMessage());
				e.printStackTrace();
			}
			rmq = null;
		}
		logger.info(noOfRetrievedMessages + " messages retrieved from the queue.");
		logger.info(noOfProcessedMessages + " messages processed.");
		logger.info("OIFPartDataImport ends. Interface Id = " + componentId + " Date & Time: " + new java.util.Date());
	}

	private void processMsg(String msgStr) {
		Gson gson = new Gson();
		
		@SuppressWarnings("unchecked")
		HashMap<String, String> dataMap = gson.fromJson(msgStr, HashMap.class);
		
		processDieCast(dataMap);
	}
	
	private void processDieCast(HashMap<String, String> dataMap) {
		
		String productType = dataMap.get("PART_NAME"); // will be either 'BLOCK' or 'HEAD'
		
		@SuppressWarnings("unchecked")
		DiecastDao<DieCast> dieCastDao = (DiecastDao<DieCast>) ProductTypeUtil.getTypeUtil(productType).getProductDao();
		
		String id = dataMap.get("MC_NUMBER"); // MC_NUMBER will always be the primary key 
		DieCast dieCast = (DieCast) dieCastDao.findByKey(id);
		if(dieCast == null) {
			dieCast = (DieCast) ProductTypeUtil.getTypeUtil(productType).createProduct(id);
		}
		dieCast.setMcSerialNumber(id);
		if(dataMap.get("DC_NUMBER") != null)
			dieCast.setDcSerialNumber(dataMap.get("DC_NUMBER"));
		if(dataMap.get("MODEL") != null) 
			dieCast.setModelCode(dataMap.get("MODEL"));
		dieCastDao.save(dieCast);
	}

	private RegularMQClient getRegMQClient() throws MQException {
		RegularMQClient rmq = new RegularMQClient(getProperty("HOST_NAME"), getPropertyInt("PORT_NUMBER"),
					getProperty("QUEUE_MANAGER_NAME"), getProperty("CHANNEL"), getProperty("QUEUE_NAME"), 
					getProperty("USER_NAME"), getProperty("PASSWORD"));
			
		rmq.initMQSTR(); // prepare the client to deal with MQSTR messages with 546 encoding
		return rmq;
	}
}
