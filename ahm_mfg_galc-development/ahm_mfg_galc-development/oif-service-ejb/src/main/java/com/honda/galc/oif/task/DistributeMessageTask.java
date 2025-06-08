package com.honda.galc.oif.task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.honda.galc.common.MQUtility;
import com.honda.galc.common.MQUtilityException;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.OIFConstants;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <h3>EngineMasterSpecTask Class description</h3>
 * <p> EngineMasterSpecTask description </p>
 * Engine Master Spec task is an OIF task, which executes every day at 5:00 am.(The setting can be change)
 * It retrieves data from file to get the original priority production. 
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
 * @author Larry Karpov 
 * @since Nov 05, 2013
*/

public class DistributeMessageTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from GPCS(MQ).
	protected List<String> aReceivedFileList;
	
	public DistributeMessageTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			if(args[0] == null) {
				logger.emergency("Interface name for distribution is missing.");
				errorsCollector.emergency("Interface name for distribution is missing.");
				setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
				return;
			}
			@SuppressWarnings("unchecked")
			String interfaceName = ((KeyValue<String, String>) args[0]).getValue();
			distributeMessage(interfaceName);
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e, "Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and distribute it/them to queues for each line 
	 * <p>
	 * @param interfaceName - Interface to distribute
	 */
	private void distributeMessage(String interfaceName) {
		logger.info("start to distribute " + interfaceName);
		String strComponentId = PropertyService.getProperty(interfaceName, OIFConstants.DISTRIBUTION_PARAM);
		if(strComponentId == null) {
			logger.error("Distribution config is missing.");
			errorsCollector.error("Distribution config is missing.");
			setJobStatus(OifRunStatus.MISSING_CONFIGURATION);
			return;
		} else {
			this.componentId = strComponentId;
		}
		refreshProperties();

//		Distribute file from GPCS(MQ) to local queues for each line
		String[] distributionList = getDistributionLines(OIFConstants.DISTRIBUTION_LINES);
		if(distributionList == null || distributionList.length == 0) {
			logger.error("Distribution List is empty.");
			errorsCollector.error("Distribution List is empty.");
			return;
		}
//		Receive file from GPCS(MQ)
		MQUtility mqutil = new MQUtility(this);
		
		String strInterfaceId = getProperty(OIFConstants.INTERFACE_ID);
		int fileLength = PropertyService.getPropertyInt(interfaceName, OIFConstants.MESSAGE_LINE_LENGTH); 
		String[] receivedFiles = getFilesFromMQ(strInterfaceId, fileLength);
		if (receivedFiles == null) {
			return;
		}
		aReceivedFileList = Arrays.asList(receivedFiles);
		for (String receivedFile : aReceivedFileList) {
			String fileToSend = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT) + receivedFile; 
			for(int i=0; i<distributionList.length; i++) {
				String interfaceId = distributionList[i];
				try {
					mqutil.executeMQSendAPI(interfaceId, PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.MQ_CONFIG), fileToSend);
				} catch (MQUtilityException e) {
					StringBuffer emergencyMessage = new StringBuffer();
					emergencyMessage.append("Failed to distribute interface. ").append(interfaceId)
						.append(", File: ").append(fileToSend)
						.append(", Line: ").append(distributionList[i])
						.append(".");
					logger.emergency(emergencyMessage.toString());
					errorsCollector.emergency(emergencyMessage.toString());
					setJobStatus(OifRunStatus.DISTRIBUTION_MESSAGE_FAILED);
				}
			}
		}
		setIncomingJobCount(aReceivedFileList.size(),0,receivedFiles);
		
	}

	/**
	 * Get a list of interfaces (effectively queues for those interfaces) <br>
	 * from properties with property key like <propertyKey>{N} <br>
	 * N defines an order
	 * <p>
	 * @param propertyKey
	 */
	private String[] getDistributionLines(String propertyKey) {
		Map<Integer, String> unsortedResult = new HashMap<Integer, String>();
		String regex = new StringBuilder(propertyKey).append("(?:\\{(.*)})").toString();
		Pattern p = Pattern.compile(regex);
		List<ComponentProperty> componentProperties = getProperties();
		for(ComponentProperty property : componentProperties) {
			Matcher m = p.matcher(property.getPropertyKey());
			if(m.matches()) {
				String strOrder = property.getPropertyKey().split("\\{")[1].split("\\}")[0];
				Integer order = Integer.valueOf(strOrder);
				unsortedResult.put(order, property.getPropertyValue());
			}
		}
		Map<Integer, String> treeMap = new TreeMap<Integer, String>(unsortedResult);
		return treeMap.values().toArray(new String[treeMap.size()]);
	}
}