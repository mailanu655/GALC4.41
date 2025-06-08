package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.List;
import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.FrameEngineModelMapDao;
import com.honda.galc.entity.product.FrameEngineModelMap;
import com.honda.galc.oif.dto.FrameEngineModelMapDTO;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>FrameEngineModelMapTask Class description</h3>
 * <p> FrameEngineModelMapTask description </p>
 * Common functionality for 
 * 		Frame Engine Model Map Tasks
 * 
 *  Message Source: GPCS-PP 
 *  Message Destination: 2SD-F-GALC 
 *  Interface ID: EY-GPC#PMCGAL#GMM117
 *  Run Frequency---------------------------------------------------- 
 *  Received once a day from GPCS nightly batch 
 *  Business Purpose------------------------------------------------ 
 *  Need Master data(MTOC) to produce the Vehicle
 
 *  
 *
 * </TABLE>
 *    
 * @author Dmitri Kouznetsov 
 * @since May 8, 2018
*/


public class FrameEngineModelMapTask extends OifTask<FrameEngineModelMapDTO> 
	implements IEventTaskExecutable {
	
//	The list of file names that are received from GPCS(MQ).
	protected String[] aReceivedFileList;
	
	protected OIFSimpleParsingHelper<FrameEngineModelMapDTO> parseHelper; 
	
	public FrameEngineModelMapTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try{
			this.processModelMaps();
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
		
	protected void initParseHelper() { 
//	Get configured parsing data 
		String FMMSdefs = getProperty("PARSE_LINE_DEFS");
		parseHelper = new OIFSimpleParsingHelper<FrameEngineModelMapDTO>(FrameEngineModelMapDTO.class, FMMSdefs, logger);
		parseHelper.getParsingInfo();
	}
	
	
	//Need method to get data off the queue and do something with it.
	protected void processModelMaps(){
		logger.info("Start to process FrameEngineModelMaps");
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
				continue;
			}
		
			List<FrameEngineModelMapDTO> frameEngineModelMapDtoList = new ArrayList<FrameEngineModelMapDTO>();
			for(String receivedRecord : receivedRecords) {
				FrameEngineModelMapDTO frameEngineModelMapDTO = new FrameEngineModelMapDTO();
				parseHelper.parseData(frameEngineModelMapDTO, receivedRecord);
				if(	frameEngineModelMapDTO.getFrmYearCode() == null ||
					frameEngineModelMapDTO.getFrmModelCode() == null || 
					frameEngineModelMapDTO.getFrmTypeCode() == null ||
					frameEngineModelMapDTO.getFrmOptionCode() == null ||
					frameEngineModelMapDTO.getEngYearCode() == null ||
					frameEngineModelMapDTO.getEngModelCode() == null ||
					frameEngineModelMapDTO.getEngTypeCode() == null ||
					frameEngineModelMapDTO.getEngOptionCode() == null) {
					logger.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
					errorsCollector.emergency("The primary key is missing for this record: " 
							+ receivedRecord);
				}
				frameEngineModelMapDtoList.add(frameEngineModelMapDTO);
			}
			
			// Clear table and insert new data
			List<FrameEngineModelMap> newData = new ArrayList<FrameEngineModelMap>();
			for(FrameEngineModelMapDTO femm : frameEngineModelMapDtoList)
				newData.add(femm.deriveFrameEngineModelMap());

			try {
				ServiceFactory.getDao(FrameEngineModelMapDao.class).removeAll();
				logger.info("Table FRAME_ENGINE_MODEL_MAP_TBX cleared.");
				ServiceFactory.getDao(FrameEngineModelMapDao.class).saveAll(newData);
				logger.info("Table FRAME_ENGINE_MODEL_MAP_TBX updated. File processed: " + receivedFile + ".");
			} catch (Exception e) {
				logger.emergency(e.getClass() + ": " +  e.getMessage() + ": " + e.getCause() + "\n" +  e.getStackTrace().toString());
			}
		}
	}
}
