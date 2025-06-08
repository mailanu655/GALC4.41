package com.honda.galc.oif.task.ahm;

import java.text.ParseException;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.oif.task.OifTask;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;

/**
 * 
 * <h3>AHMShippingTransactionTask.java</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> This class is to receive the daily shipping transaction file. This file will be 
 *                sent from to HMA every night along with the Inventory file and the shipping log
 *                file. Note that the interface file id is D--GMG#HMAGAL#AHM020
 * </p>
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
 * <TR>
 * <TD>LK</TD>
 * <TD>Jan 06, 2015</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @version 0.1
 * @author Larry Karpov
 * @created Jan 06, 2015
 */
public class AHMShippingTransactionTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
	public AHMShippingTransactionTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processAHMShippingTransaction();
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
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line 
	 * <p>
	 * @throws ParseException 
	 */
	private void processAHMShippingTransaction() throws ParseException {
		logger.info("Start to process AHMShippingTransaction");
		ShippingTransactionPropertyBean propBean = PropertyService.getPropertyBean(ShippingTransactionPropertyBean.class, componentId);
		String interfaceId = propBean.getInterfaceId();
		Integer lineLength = propBean.getMessageLineLength();
//		Receive file from AHM(MQ);
		
		String[] aReceivedFileList = getFilesFromMQ(interfaceId, lineLength);
		if (aReceivedFileList != null && aReceivedFileList.length > 0) {
        	logger.info("File received = " + aReceivedFileList[0].toString());
        }
	}

}