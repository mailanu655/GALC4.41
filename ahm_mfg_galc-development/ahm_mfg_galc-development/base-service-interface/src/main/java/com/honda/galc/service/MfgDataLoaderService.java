package com.honda.galc.service;

import java.util.Set;

import com.honda.galc.dto.PartDto;
import com.honda.galc.entity.conf.MCOperationMatrix;
import com.honda.galc.entity.conf.MCOperationPartMatrix;
import com.honda.galc.entity.conf.MCOperationPartRevision;


public interface MfgDataLoaderService extends IService {

	public void saveMatrix(Set<MCOperationMatrix> opMatrixSet, Set<MCOperationPartMatrix> partMatrixSet);
	
	public void createMfgPart(String operationName, PartDto p, MCOperationPartRevision defaultMFgPart,	MCOperationPartRevision refPart);
	
	public void addMeasurement(String opName, String partId, int partRev, int qty,
			String minLimit, String maxLimit, int maxAttempts, String pset, String tool);
	
	public void addMeasurementByQty(String opName, String partId, int partRev, int qty,
			String minLimit, String maxLimit, int maxAttempts, String pset, String tool, int measSeq);
	
}
