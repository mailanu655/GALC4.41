package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.List;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.FrameMTOCMasterSpecDao;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.FrameMTOCMasterSpec;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.oif.dto.FrameMTOCMasterSpecDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>FrameMTOCMasterSpecTask Class description</h3>
 * <p> FrameMTOCMasterSpecTask description </p>
 * Common functionality for 
 * 		Frame MTOC Master Specification Tasks
 * 
 *  Message Source: GPCS-PP 
 *  Message Destination: 2SD-F-GALC 
 *  Interface ID: EY-GPC#PMCGAL#GMM111 
 *  Run Frequency---------------------------------------------------- 
 *  Received once a day from GPCS nightly batch 
 *  Business Purpose------------------------------------------------ 
 *  Need Master data(MTOC) to produce the Vehicle
 
 *  
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>KG</TH>
 * <TH>9/11/2014</TH>
 * <TH>0.1</TH>
 * <TH>None</TH>
 * <TH>Create the interface task</TH>
 * </TR>
 *
 * </TABLE>
 *    
 * @author Kenneth Gibson 
 * @since September 11, 2014
*/


public class FrameMTOCMasterSpecTask extends OifTask<FrameMTOCMasterSpecDTO> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	private int failedCount=0,totalRecords=0;
	
	protected OIFSimpleParsingHelper<FrameMTOCMasterSpecDTO> parseHelper; 
	
	public FrameMTOCMasterSpecTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			processMTOSpecs();
		}catch(TaskException e) {
			logger.emergency(e.getMessage());
			errorsCollector.emergency(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			logger.emergency(e,"Unexpected exception occured");
			errorsCollector.emergency(e,"Unexpected exception occured");
		} finally {
			setIncomingJobCount(totalRecords-failedCount, failedCount, aReceivedFileList);
			errorsCollector.sendEmail();
		}
	}
		
	protected void initParseHelper() { 
//	Get configured parsing data 
		String FMMSdefs = getProperty("PARSE_LINE_DEFS");
		parseHelper = new OIFSimpleParsingHelper<FrameMTOCMasterSpecDTO>(FrameMTOCMasterSpecDTO.class, FMMSdefs, logger);
		parseHelper.getParsingInfo();
	}
	
	
	//Need method to get data off the queue and do something with it.
	protected void processMTOSpecs(){
		logger.info("Start to process FrameMTOCMasterSpec");
		refreshProperties();
		
		aReceivedFileList = getFilesFromMQ(getProperty(OIFConstants.INTERFACE_ID),getPropertyInt(OIFConstants.MESSAGE_LINE_LENGTH));
		
		if(aReceivedFileList == null){
			return;
		}
		//Get configured parsing data
		initParseHelper();

		
//		Process file(s) and update Frame MTO Master Spec info
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
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}
		
			List<FrameMTOCMasterSpecDTO> frameMTOCMasterSpecDtoList = new ArrayList<FrameMTOCMasterSpecDTO>();
			totalRecords += receivedRecords.size();
			for(String receivedRecord : receivedRecords) {
				FrameMTOCMasterSpecDTO frameMTOCMasterSpecDTO = new FrameMTOCMasterSpecDTO();
				parseHelper.parseData(frameMTOCMasterSpecDTO, receivedRecord);
				if(frameMTOCMasterSpecDTO.getPlantCodeFrame() == null) {
					logger.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					errorsCollector.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					failedCount++;
				} else {
					String productSpecCode = createProductSpecCode(frameMTOCMasterSpecDTO);  
					frameMTOCMasterSpecDTO.setProductSpecCode(productSpecCode);
					String engineMTO = createEngineMTO(frameMTOCMasterSpecDTO);
					frameMTOCMasterSpecDTO.setEngineMTO(engineMTO);
					frameMTOCMasterSpecDtoList.add(frameMTOCMasterSpecDTO);
				}
			}
			//Update or insert data
			for(FrameMTOCMasterSpecDTO fmms : frameMTOCMasterSpecDtoList){
				FrameMTOCMasterSpec frameMTOCMasterSpec = fmms.deriveFrameMTOCMasterSpec();
				FrameSpec frameSpec = fmms.deriveFrameSpec();
				ServiceFactory.getDao(FrameMTOCMasterSpecDao.class).save(frameMTOCMasterSpec);
				ServiceFactory.getDao(FrameSpecDao.class).save(frameSpec);
				logger.debug(" Frame Master Spec saved" + frameMTOCMasterSpec);
			}
			logger.info(" Frame MTOC Master Specification saved; file processed: " + receivedFile);
		}
	}

	String createProductSpecCode(FrameMTOCMasterSpecDTO dto) {
		StringBuffer result = new StringBuffer();
		if(dto.getModelYearCode() != null ) {
			result.append(dto.getModelYearCode()); 
		}
		if(dto.getModelCode() != null ) {
			result.append(dto.getModelCode()); 
		}
		if(dto.getModelTypeCode() != null ) {
			result.append(dto.getModelTypeCode()); 
		}
		if(dto.getModelOptionCode() != null ) {
			result.append(dto.getModelOptionCode()); 
		}
		if(dto.getExtColorCode() != null ) {
			result.append(dto.getExtColorCode()); 
		}
		if(dto.getIntColorCode() != null ) {
			result.append(dto.getIntColorCode()); 
		}
		return result.toString();
	}

	String createEngineMTO(FrameMTOCMasterSpecDTO dto) {
		StringBuffer result = new StringBuffer();
		if(dto.getEngineModelYearCode() != null ) {
			result.append(dto.getEngineModelYearCode()); 
		}
		if(dto.getEngineModelCode() != null ) {
			result.append(dto.getEngineModelCode()); 
		}
		if(dto.getEngineModelTypeCode() != null ) {
			result.append(dto.getEngineModelTypeCode()); 
		}
		if(dto.getEngineOptionCode() != null ) {
			result.append(dto.getEngineOptionCode()); 
		}
		return result.toString();
	}
	
}