package com.honda.galc.oif.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.OIFSimpleParsingHelper;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.MissionSpecDao;
import com.honda.galc.entity.product.MissionSpec;
import com.honda.galc.oif.dto.MissionSpecDTO;
import com.honda.galc.oif.property.MissionMasterSpecPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.OIFConstants;
import com.honda.galc.util.OIFFileUtility;

/**
 * 
 * <h3>MissionMasterSpecTask Class description</h3>
 * <p> MissionMasterSpecTask description </p>
 * Mission Master Spec task is an OIF task, which executes every day 
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
 * @since Feb 09, 2015
*/

public class MissionMasterSpecTask extends OifTask<Object> 
	implements IEventTaskExecutable {
	
	public MissionMasterSpecTask(String name) {
		super(name);
	}
	
	public void execute(Object[] args) {
		try {
			processMissionMasterSpec();
		} catch(TaskException e) {
			logger.error(e);
			errorsCollector.emergency(e.getMessage());
		} catch(Exception e) {
			logger.error(e,"Unexpected exception occured");
			errorsCollector.error(e, "Unexpected exception occured");
		} finally {
			errorsCollector.sendEmail();
		}
	}
	
	/**
	 * Receive file(s) from MQ.<br>
	 * and process it/them for current line 
	 * <p>
	 */
	private void processMissionMasterSpec() {
//		The list of file names that are received from GPCS(MQ).
		String[] receivedFileList;
		
		logger.info("start to process MissionMasterSpec");
		refreshProperties();
		initMissionSpecData();
//		Receive file from GPCS(MQ);
		
		MissionMasterSpecPropertyBean propBean = PropertyService.getPropertyBean(MissionMasterSpecPropertyBean.class, componentId);
		String interfaceId = propBean.getInterfaceId();
		Integer lineLength = propBean.getMessageLineLength();
		receivedFileList = getFilesFromMQ(interfaceId, lineLength);

		if (receivedFileList == null) {
			return;
		}

//		Get configured parsing data 
		String missionMasterSpec = propBean.getParseLineDefs();
		OIFSimpleParsingHelper<MissionSpecDTO> missionSpecParseHelper = new OIFSimpleParsingHelper<MissionSpecDTO>(
				MissionSpecDTO.class, missionMasterSpec, logger);
		missionSpecParseHelper.getParsingInfo();
		
//		Process file(s) and update missionSpec data  
		for (int count=0; count<receivedFileList.length; count++) {
			String receivedFile = receivedFileList[count];
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
		
			List<MissionSpecDTO> missionSpecDtoList = new ArrayList<MissionSpecDTO>();
			for(String receivedRecord : receivedRecords) {
				MissionSpecDTO missionSpecDTO = new MissionSpecDTO();
				missionSpecParseHelper.parseData(missionSpecDTO, receivedRecord);
				String productSpecCode = makeProductSpecCode(missionSpecDTO);
				if(productSpecCode.length() > 0) {
					missionSpecDTO.setProductSpecCode(productSpecCode);
					missionSpecDtoList.add(missionSpecDTO);
					logger.info("Mission Spec added: " + missionSpecDTO);
				} else {
					logger.error("Missing ProductSpecCode. Received record: " + receivedRecord);
					errorsCollector.error("Missing ProductSpecCode. Received record: " + receivedRecord);
				}
			}
			if(missionSpecDtoList.size() > 0) {
				List<MissionSpec> missionSpecList = new ArrayList<MissionSpec>();
				for(MissionSpecDTO missionSpecDto : missionSpecDtoList) {
					MissionSpec missionSpec = missionSpecDto.deriveMissionSpec();
					missionSpecList.add(missionSpec);
				}
				ServiceFactory.getDao(MissionSpecDao.class).saveAll(missionSpecList);
				logger.info(" Mission Spec List saved: " + missionSpecList.size() + " objects.");
			} else {
				logger.error("No Mission Spec created. ");
				errorsCollector.error("No Mission Spec created. ");
			}
			logger.info("file processed: " + receivedFile);
		}
		logger.info("" + receivedFileList.length + " file(s) processed for interface " + interfaceId);
	}

	private void initMissionSpecData() {
		initialize();
	}

	String makeProductSpecCode(MissionSpecDTO missionSpec) {
		StringBuffer productSpecCode = new StringBuffer(); 
		String modelYearCode = missionSpec.getModelYearCode();
		String modelCode = missionSpec.getModelCode();
		String modelTypeCode = missionSpec.getModelTypeCode();
//		String modelOptionCode = missionSpec.getModelOptionCode();
		boolean isValid = true;
		if(modelYearCode == null || modelYearCode.trim().length() == 0) {
			isValid = false;
			logger.error("Missing ModelYearCode: " + modelYearCode);
		}
		if(modelCode == null || modelCode.trim().length() == 0) {
			isValid = false;
			logger.error("Missing modelCode: " + modelCode);
		}
		if(modelTypeCode == null || modelTypeCode.trim().length() == 0) {
			isValid = false;
			logger.error("Missing modelTypeCode: " + modelTypeCode);
		}
//		if(modelOptionCode == null || modelOptionCode.trim().length() == 0) {
//			isValid = false;
//			logger.error("Missing modelOptionCode: " + modelOptionCode);
//		}
		if(isValid) {
			productSpecCode.append(modelYearCode)
				.append(modelCode)
				.append(modelTypeCode);
//				.append(modelOptionCode);
		}
		return productSpecCode.toString();
	}
}