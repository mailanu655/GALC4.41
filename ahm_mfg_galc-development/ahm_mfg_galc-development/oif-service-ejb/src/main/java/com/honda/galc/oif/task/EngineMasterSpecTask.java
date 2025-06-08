package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.EngineSpec;
import com.honda.galc.oif.dto.EngineSpecDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

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

public class EngineMasterSpecTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	private int totalRecords = 0, failedRecords=0;
	
	public EngineMasterSpecTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try {
			processEngineMasterSpec();
		} catch(TaskException e) {
			logger.emergency(e);
			errorsCollector.emergency(e.getMessage());
		} catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e, "Unexpected exception occured");
		} finally {
			setIncomingJobCount(totalRecords-failedRecords, failedRecords, receivedFileList);
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line 
	 * <p>
	 */
	private void processEngineMasterSpec() {
		logger.info("start to process EngineMasterSpec");
		refreshProperties();
		initEngineSpecData();
//		Receive file from GPCS(MQ);
		
		aReceivedFileList = getFilesFromMQ(getProperty("INTERFACE_ID"),
				getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));

		if (aReceivedFileList == null) {
			return;
		}

//		Get configured parsing data 
		String engMasterSpec = getProperty("PARSE_LINE_DEFS");
		OIFSimpleParsingHelper<EngineSpecDTO> engineSpecParseHelper = new OIFSimpleParsingHelper<EngineSpecDTO>(
				EngineSpecDTO.class, engMasterSpec, logger);
		engineSpecParseHelper.getParsingInfo();
		
//		Process file(s) and update engSpec data  
		for (int count=0; count<aReceivedFileList.length; count++) {
			String receivedFile = aReceivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecords = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecords.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				continue;
			}else totalRecords = receivedRecords.size();
		
			List<EngineSpecDTO> engSpecDtoList = new ArrayList<EngineSpecDTO>();
			for(String receivedRecord : receivedRecords) {
				EngineSpecDTO engSpecDTO = new EngineSpecDTO();
				engineSpecParseHelper.parseData(engSpecDTO, receivedRecord);
				if(engSpecDTO.getProductSpecCode() == null) {
					logger.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					errorsCollector.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					failedRecords++;
					setIncomingJobStatus(OifRunStatus.MISSING_PRIMARY_KEY);
					
				} else {
					engSpecDtoList.add(engSpecDTO);
				}
			}
//			Update or insert data
			for(EngineSpecDTO es : engSpecDtoList) {
				EngineSpec engSpec = es.deriveEngineSpec();
				ServiceFactory.getDao(EngineSpecDao.class).save(engSpec);
				logger.debug(" Engine Spec saved" + engSpec);
			}
			logger.info("file processed: " + receivedFile);
		}
	}

	private void initEngineSpecData() {
		initialize();
	}

}