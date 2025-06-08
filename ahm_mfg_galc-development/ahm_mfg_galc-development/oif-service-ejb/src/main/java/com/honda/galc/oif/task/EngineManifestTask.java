package com.honda.galc.oif.task;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.OifRunStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.oif.dto.EngineManifestDTO;
import com.honda.galc.oif.dto.NSEDataDTO;
import com.honda.galc.oif.property.EngineManifestPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>EngineManifestTask</h3>
 * <p> EngineManifestTask is for receiving AEP engines</p>
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
 * @author Larry Karpov<br>
 * March 05, 2015
 *
 */

public class EngineManifestTask extends OifTask<NSEDataDTO> implements IEventTaskExecutable {
	private static final String AEP_MODELS = "AEP_MODELS";
	private static final String DAYS_TO_CHECK = "DAYS_TO_CHECK";
	
	private int totalRecords=0, failedRecords = 0;
	protected String[] receivedFileList;
	
	public EngineManifestTask(String componentId) {
		super(componentId);
	}

	public void execute(Object[] args) {
		try {
			processEngineManifest();
			//Add method to check missing engines and notify AEP and Production floor
			checkIfMissingEngines();
		} catch(TaskException e) {
			logger.error(e);
			errorsCollector.emergency(e.getMessage());
		} catch(Exception e) {
			logger.error(e,"Unexpected exception occured");
			errorsCollector.error(e, "Unexpected exception occured");
		} finally {
			setIncomingJobCount(totalRecords-failedRecords, failedRecords, receivedFileList);
			errorsCollector.sendEmail();
		}
	}
	
	public void processEngineManifest() {
//		The list of file names that are received from AEP(MQ).
		EngineManifestPropertyBean propBean = PropertyService.getPropertyBean(EngineManifestPropertyBean.class, componentId);
		String interfaceId = propBean.getInterfaceId();
		Integer lineLength = propBean.getMessageLineLength();
		receivedFileList = getFilesFromMQ(interfaceId, lineLength);
		if (receivedFileList == null) {
			return;
		}
//		Get configured parsing data 
		String parsingDefs = propBean.getParseLineDefs();
		OIFSimpleParsingHelper<EngineManifestDTO> manifestEngineParseHelper = new OIFSimpleParsingHelper<EngineManifestDTO>(
				EngineManifestDTO.class, parsingDefs, logger);
		manifestEngineParseHelper.getParsingInfo();
		List<EngineManifestDTO> engineManifestDtoList = new ArrayList<EngineManifestDTO>();
		initialize();
		boolean checkLineNeeded = propBean.isCheckLine();
		String aepFiredFlag = propBean.getAepFiredFlag();
		short galcFiredFlag = Short.parseShort(propBean.getGalcFiredFlag());
		short galcNotFiredFlag = Short.parseShort(propBean.getGalcNotFiredFlag());
		String recievingPpid = propBean.getShippingRecvPpid();
		for (int count=0; count<receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
			if (receivedFile == null) {
				continue;
			}
			String resultPath = PropertyService.getProperty(OIFConstants.OIF_SYSTEM_PROPERTIES, OIFConstants.RESULT); 
			List<String> receivedRecordList = OIFFileUtility.loadRecordsFromFile(resultPath + receivedFile, logger);
			if (receivedRecordList.isEmpty()) {
				logger.error("No records in received file: " + receivedFile);
				errorsCollector.error("No records in received file: " + receivedFile);
				setIncomingJobStatus(OifRunStatus.NO_RECORDS_IN_RECEIVED_FILE);
				continue;
			}else {
				totalRecords +=  receivedRecordList.size();
			}
			for(String receivedRecord : receivedRecordList) {
				EngineManifestDTO engineManifestDTO = new EngineManifestDTO();
				manifestEngineParseHelper.parseData(engineManifestDTO, receivedRecord);
				String engSN = engineManifestDTO.getEngineSerialNumber();
				if(engSN == null || StringUtils.isEmpty(engSN.trim()))  {
					String error ="Engine serial number missing or incorrect";
					logger.error(error);
					errorsCollector.error(error);
					setIncomingJobStatus(OifRunStatus.ATLEAST_ONE_ENGINE_SERIAL_MISSING);
					failedRecords++;
					continue;
				}
				if(checkLineNeeded) {
					String lineNo = engineManifestDTO.getLineNo();
					if(lineNo.equals(siteLineId)) {
						engineManifestDtoList.add(engineManifestDTO);
					} else {
						logger.info("[" + lineNo + "] is not site line: " + siteLineId + "; skipping record.");
					}
				} else {
					engineManifestDtoList.add(engineManifestDTO);
				}
			}
		}
		
		for(EngineManifestDTO engineManifestDTO : engineManifestDtoList) {
			
			StringBuilder msg = new StringBuilder();
			EngineDao engineDao = getDao(EngineDao.class);
			String engSN = engineManifestDTO.getEngineSerialNumber();
			Engine engine = engineDao.findByKey(engSN);
			msg.append("Engine:").append(engineManifestDTO.getEngineSerialNumber()).append((engine == null) ? "was created" : "was updated");
			if (engine == null) {
				engine = new Engine();
				engine.setProductId(engineManifestDTO.getEngineSerialNumber());
			}
			engine.setProductionLot("");
			engine.setProductSpecCode(generateProductSpecCode(engineManifestDTO));
			engine.setKdLotNumber(engineManifestDTO.getEngineShipKD());
			engine.setActualMissionType(engineManifestDTO.getMissionModelType());
			engine.setMissionSerialNo(engineManifestDTO.getMissionSerialNumber());
			engine.setPlantCode(getPlantCode(propBean, engineManifestDTO));
			short eff = aepFiredFlag.equals(engineManifestDTO.getEngineFiredFlag()) ? galcFiredFlag : galcNotFiredFlag;   
			engine.setEngineFiringFlag(eff);
			engineDao.save(engine);
						
			logger.info(msg.toString());
			
			
			//update KD lot number in Frame table if AUTO_ASSIGN = true in Gal489TBX
			if(propBean.isAutoAssign()){
				FrameDao frameDao = getDao(FrameDao.class);
				Frame frame =  null;
				if(propBean.getNotSellableTrackingStatus() != null && propBean.getNotSellableTrackingStatus().length >0 )
					frame = frameDao.findByKDLotNumber(engine.getKdLotNumber(),  Arrays.asList(propBean.getNotSellableTrackingStatus()));
				else frame = frameDao.findByKDLotNumber(engine.getKdLotNumber());
				
				
				if(frame == null){
					String error = "No existing Frame for the given KD Lot number :"+engine.getKdLotNumber();
					logger.error(error);
					errorsCollector.error(error);
					setIncomingJobStatus(OifRunStatus.NO_EXISTING_FRAME_FOR_GIVEN_KD_LOT);
					continue;
				}
				else {  // there is a frame matching kd-lot and this engine has not been assigned to any frame
					if(StringUtils.isEmpty(frame.getEngineSerialNo())){
						frame.setEngineSerialNo(engine.getProductId());
						frameDao.update(frame);
						logger.info("Updated Frame with engine serial number :"+engine.getProductId());
						//update vin number in the engine table
						engine.setVin(frame.getProductId());
						engineDao.update(engine);
						logger.info("Updated Engine with Frame VIN number :"+frame.getProductId());
					}else if(!frame.getEngineSerialNo().equalsIgnoreCase(engine.getProductId())){
						failedRecords++;
						StringBuilder sb = new StringBuilder();
						sb.append("The received Engine serial number ").append(engine.getProductId())
								.append(" doesn't match with assigned engine serial number ")
								.append(frame.getEngineSerialNo());
						String error = sb.toString();
						logger.error(error);
						errorsCollector.error(error);
						setIncomingJobStatus(OifRunStatus.ENGINE_DOES_NOT_MATCH_ASSIGNED_NO);
						continue;
					}
				}
			}
			
			TrackingService ts = ServiceFactory.getService(TrackingService.class);
//			track method should create ProductStampingSequence, InProcessProduct entities and update tracking status on Engine  
			ts.track(ProductType.ENGINE, engineManifestDTO.getEngineSerialNumber(), recievingPpid);
			logger.info("Tracking of Engine; " + engine.toString() + " completed.");
		}
	}
	
private String getPlantCode(EngineManifestPropertyBean propBean, EngineManifestDTO engineManifestDTO) {
		return (!StringUtils.isEmpty(engineManifestDTO.getEngineSource()) ? engineManifestDTO.getEngineSource() : propBean.getPlantCode());
	}

	//	productSpecCode = engineModel + engineType + engineOption
	public String generateProductSpecCode(EngineManifestDTO emDTO) {
		StringBuffer productSpecCode = new StringBuffer(); 
		boolean isValid = true;
		String engineModel = emDTO.getEngineModel();
		if(engineModel == null || engineModel.trim().length() == 0) {
			isValid = false;
			logger.error("Missing engineCode: " + engineModel);
			setIncomingJobStatus(OifRunStatus.MISSING_ENGINE_CODE);
		}
		String engineType = emDTO.getEngineType();
		if(engineType == null || engineType.trim().length() == 0) {
			isValid = false;
			logger.error("Missing engineTypeCode: " + engineType);
			setIncomingJobStatus(OifRunStatus.MISSING_ENGINE_TYPE);
		}
		String engineOption = emDTO.getEngineOption();
		if(engineOption == null) {
			isValid = false;
			logger.error("Missing engineOption: " + engineOption);
			setIncomingJobStatus(OifRunStatus.MISSING_ENGINE_OPTION);
		}
		if(isValid) {
			productSpecCode
				.append(engineModel)
				.append(engineType)
				.append(engineOption);
		}
		return productSpecCode.toString();
	}
	
	//	Add validation to check if needed AEP engines are in DB for the Lots that need AEP engines in next 2 days
	// The following are the models of engines that AEP will send to HMA per Corporate Planning. 
    //Pilot - G5MH R05 
   //**MDX - H5WS - A00, C00, H00, H01, R00 
	
	public boolean checkIfMissingEngines() {
		
		boolean missingEng = false;
		
		String[] aepEngineModels = StringUtils.split(getProperty(AEP_MODELS), ",");
		int daysToCheck = Integer.parseInt(getProperty(DAYS_TO_CHECK));
		
		EngineManifestPropertyBean propBean = PropertyService.getPropertyBean(EngineManifestPropertyBean.class, componentId);
		String plantCode = propBean.getPlantCode(); 
		String[] notSellableTrackingStatus = propBean.getNotSellableTrackingStatus();
		
		logger.info("Days to check :  " + daysToCheck + " Plant code : "+plantCode );
		
		FrameDao frameDao = ServiceFactory.getDao(FrameDao.class);
		EngineDao engineDao = ServiceFactory.getDao(EngineDao.class);
		
		for(String engineModel : aepEngineModels){			
			// get count of required engines by MTOC
			List<String> framesToBuild = frameDao.getProductsByEngMTOC(engineModel, daysToCheck,notSellableTrackingStatus);
			int requiredEngCount = framesToBuild.size();
			logger.info("Count of frames to build  for engine model "+ engineModel + "is :"+requiredEngCount);
			
			//get count of available engines by MTOC
			List<String> availableEng = engineDao.findAvailEnginesByMTOC(plantCode,engineModel);
			int availableEngineCount = availableEng.size();
			logger.info("Count of engines available for model "+ engineModel + "is :"+availableEngineCount);
			
			// calculate the missing count
			int missingEngCount = requiredEngCount - availableEngineCount;
			
			// If missing Engines add to notification
			if (missingEngCount > 0){
				// message - n engines of model ENGMODEL are missing
				String error = Integer.toString(missingEngCount) +" engines of model "+engineModel+" are missing";
				logger.error(error);
				errorsCollector.error(error);
				setIncomingJobStatus(OifRunStatus.MISSING_ENGINE);
				missingEng = true;
			}
			logger.info("Engine model " + engineModel + " has " + availableEngineCount + "Engines and need " + requiredEngCount + "engines");
		}
		return missingEng ;
	}

}