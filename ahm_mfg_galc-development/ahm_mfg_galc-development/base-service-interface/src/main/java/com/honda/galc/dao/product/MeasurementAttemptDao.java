package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.service.IDaoService;

public interface MeasurementAttemptDao  extends IDaoService<MeasurementAttempt, Long>{

	public void saveMeasurementHistory(Measurement measurement);
	
	public int getMaxAttemptForMeasurement(String productId, String partName, int measurementSeqNo);
	
	public int moveAllData(String newProductId,String currentProductId);
	
	public List<MeasurementAttempt> findAllByProductIdAndOperationName(String newProductId,String operationName);
}
