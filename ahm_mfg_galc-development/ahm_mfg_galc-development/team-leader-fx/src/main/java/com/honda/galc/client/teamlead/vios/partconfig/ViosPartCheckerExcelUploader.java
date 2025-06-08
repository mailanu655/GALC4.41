package com.honda.galc.client.teamlead.vios.partconfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.teamlead.vios.ViosExcelUploader;
import com.honda.galc.common.logging.LogRecord;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.UnitPartDto;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.ViosMaintenanceService;

public class ViosPartCheckerExcelUploader extends ViosExcelUploader<MCViosMasterOperationPartChecker> {

	private String viosPlatform;
	public ViosPartCheckerExcelUploader(String viosPlatform, String userId) {
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
	public String doUpload(List<MCViosMasterOperationPartChecker> entityList) {
		StringBuffer infoLog = new StringBuffer();
		int recordSaved = 0;
		int recordSkipped = 0;
		Set<UnitPartDto> partCheckerSet = new HashSet<UnitPartDto>();
		List<MCViosMasterOperationPartChecker> validatedEntityList = new ArrayList<MCViosMasterOperationPartChecker>();
		for (MCViosMasterOperationPartChecker entity : entityList) {
			try {
				
				String validationStatus = doValidation(entity);
				if(StringUtils.isNotBlank(validationStatus)) {
					infoLog.append(validationStatus+"\n");
					recordSkipped++;
				} else {
					UnitPartDto unitPartDAO =  new UnitPartDto();
					unitPartDAO.setPartNo(entity.getId().getPartNo());
					unitPartDAO.setUnitNo(entity.getId().getUnitNo());
					partCheckerSet.add(unitPartDAO);
					validatedEntityList.add(entity);
				}
			} catch (Exception e) {
				infoLog.append("Record skipped: " + entity.toString() + ": " + e.getMessage() + "\n");
				recordSkipped++;
				Logger.getLogger().error(e, new LogRecord("Record skipped: " + entity.toString()));
			}
		}
		
		//delete records from master table and Revision Table
		
		ServiceFactory.getService(ViosMaintenanceService.class).deleteByPartNumber(viosPlatform, partCheckerSet);
		
		for (MCViosMasterOperationPartChecker entity : validatedEntityList) {
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
