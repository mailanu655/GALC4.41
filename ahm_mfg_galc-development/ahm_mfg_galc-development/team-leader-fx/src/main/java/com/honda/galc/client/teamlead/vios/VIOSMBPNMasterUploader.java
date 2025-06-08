package com.honda.galc.client.teamlead.vios;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.MCOperationMatrixDao;
import com.honda.galc.dao.conf.MCOperationPartMatrixDao;
import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;
import com.honda.galc.util.StringUtil;

public class VIOSMBPNMasterUploader extends ViosExcelUploader<MCViosMasterMBPNMatrixData> {

	private String viosPlatform;
	
	public VIOSMBPNMasterUploader(String viosPlatform, String userId) {
		super(viosPlatform, userId);
		this.viosPlatform = viosPlatform;
	}
	
	public String getViosPlatform() {
		return viosPlatform;
	}

	public void setViosPlatform(String viosPlatform) {
		this.viosPlatform = viosPlatform;
	}

	@Override
	public String doUpload(List<MCViosMasterMBPNMatrixData> entityList) {
		StringBuffer infoLog = new StringBuffer();
		int recordSaved = 0;
		int recordSkipped = 0;
		List<MCViosMasterMBPNMatrixData> validatedEntityList = new ArrayList<MCViosMasterMBPNMatrixData>();
		for (MCViosMasterMBPNMatrixData entity : entityList) {
			try {
				
				String validationStatus = doValidation(entity);
				if(StringUtils.isNotBlank(validationStatus)) {
					infoLog.append(validationStatus+"\n");
					recordSkipped++;
				} else {
					entity.getId().setMtcModel(entity.getId().getMtcModel().toUpperCase());
					validatedEntityList.add(entity);
				}
			} catch (Exception e) {
				infoLog.append("Record skipped: " + entity.toString() + ": " + e.getMessage() + "\n");
				recordSkipped++;
				Logger.getLogger().error(e, new LogRecord("Record skipped: " + entity.toString()));
			}
		}
		if(validatedEntityList.size() > 0) {
			boolean isValid = true;
			MCViosMasterMBPNMatrixData oldMatrix  = new MCViosMasterMBPNMatrixData();
			for (MCViosMasterMBPNMatrixData currentMatrix : validatedEntityList) {
				
				if(oldMatrix !=null && oldMatrix.getId() != null) {
					if(oldMatrix.getId().getMtcModel().charAt(0) != currentMatrix.getId().getMtcModel().charAt(0)) {
						infoLog.append("All Records skipped due to Different Model Codes\n");
						isValid = false;
					}
				}
				oldMatrix = currentMatrix;
			}
			if(isValid) {
				ServiceFactory.getService(ViosMaintenanceService.class).deleteByPlatform(viosPlatform);
				ServiceFactory.getDao(MCOperationMatrixDao.class).deleteMBPNOpMatrixData(entityList.get(0), viosPlatform, true);
				ServiceFactory.getDao(MCOperationPartMatrixDao.class).deleteMBPNPartMatrixData(entityList.get(0), viosPlatform, true);
			} else  {
				validatedEntityList = new ArrayList<MCViosMasterMBPNMatrixData>();
			}
		}
		
		
		for (MCViosMasterMBPNMatrixData entity : validatedEntityList) {
			try {
				uploadEntity(entity);
				recordSaved++;
			} catch (Exception e) {
				recordSkipped++;
			}
		}
		
		infoLog.append("\n");
		infoLog.append("No of Records Saved: " + recordSaved + "\n");
		infoLog.append("No of Records Skipped: " + recordSkipped + "\n");
		return infoLog.toString();
	}
}
