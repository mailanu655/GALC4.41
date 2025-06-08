package com.honda.galc.client.teamlead.vios;

import com.honda.galc.entity.conf.MCViosMasterMBPNMatrixData;
import com.honda.galc.entity.conf.MCViosMasterOperation;
import com.honda.galc.entity.conf.MCViosMasterOperationChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurement;
import com.honda.galc.entity.conf.MCViosMasterOperationMeasurementChecker;
import com.honda.galc.entity.conf.MCViosMasterOperationPart;
import com.honda.galc.entity.conf.MCViosMasterOperationPartChecker;
import com.honda.galc.entity.conf.MCViosMasterPlatform;
import com.honda.galc.entity.conf.MCViosMasterProcess;

public class ViosExcelUploadUtility {
	
	private ViosExcelUploadUtility() {
		super();
	}
	
	private static ViosExcelUploadUtility instance;
	
	public static ViosExcelUploadUtility getInstance() {
		if(instance == null) {
			instance = new ViosExcelUploadUtility();
		}
		return instance;
	}

	@SuppressWarnings("rawtypes")
	public <E> ViosMasterExecutor get(E entity) {
		if (entity instanceof MCViosMasterPlatform) {
			return new ViosMasterExecutorPlatform();
		} else if (entity instanceof MCViosMasterProcess) {
			return new ViosMasterExecutorProcess();
		} else if (entity instanceof MCViosMasterOperation) {
			return  new ViosMasterExecutorOperation();
		} else if (entity instanceof MCViosMasterOperationChecker) {
			return new ViosMasterOperationExecutorChecker();
		} else if (entity instanceof MCViosMasterOperationPart) {
			return new ViosMasterOperationExecutorPart();
		} else if (entity instanceof MCViosMasterOperationPartChecker) {
			return new ViosMasterOperationPartExecutorChecker();
		} else if (entity instanceof MCViosMasterOperationMeasurement) {
			return new ViosMasterOperationExecutorMeasurement();
		} else if (entity instanceof MCViosMasterOperationMeasurementChecker) {
			return new ViosMasterOperationMeasurementExecutorChecker();	
		}else if (entity instanceof MCViosMasterMBPNMatrixData) {
			return new ViosMasterMBPNMatrixExcecutor();	
		}
		return null; 
	}
}
