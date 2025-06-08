package com.honda.galc.dao.jpa.product;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementAttempt;
import com.honda.galc.service.Parameters;

public class MeasurementAttemptDaoImpl extends
		BaseDaoImpl<MeasurementAttempt, Long> implements MeasurementAttemptDao {
	
	private static final String UPDATE_PRODUCT_ID = "update MeasurementAttempt e set e.id.productId = :productId where e.id.productId = :oldProductId";
	  
	@Transactional
	public void saveMeasurementHistory(Measurement measurement) {
		Parameters params = Parameters.with("id.productId", measurement.getId().getProductId());
		params.put("id.partName", measurement.getId().getPartName());
		params.put("id.measurementSequenceNumber", measurement.getId().getMeasurementSequenceNumber());
		MeasurementAttempt attempt = new MeasurementAttempt(measurement.getId().getProductId(),
				measurement.getId().getPartName(), 
				measurement.getId().getMeasurementSequenceNumber(),
				getMaxAttemptForMeasurement(measurement.getId().getProductId(), measurement.getId().getPartName(), measurement.getId().getMeasurementSequenceNumber()));
		attempt.setPartId(measurement.getPartId());
		attempt.setPartRevision(measurement.getPartRevision());
		attempt.setMeasurementValue(measurement.getMeasurementValue());
		attempt.setMeasurementStatusId(measurement.getMeasurementStatusId());
		attempt.setActualTimestamp(measurement.getActualTimestamp());
		attempt.setMeasurementAngle(measurement.getMeasurementAngle());
		attempt.setPartSerialNumber(measurement.getPartSerialNumber());
		attempt.setFeatureId(measurement.getFeatureId());
		attempt.setFeatureType(measurement.getFeatureType());
		attempt.setMeasurementName(measurement.getMeasurementName());
		attempt.setMeasurementStringValue(measurement.getMeasurementStringValue());
		attempt.setMethodId(measurement.getMethodId());
		attempt.setMethodDescription(measurement.getMethodDescription());
		save(attempt);
	}
	
	
	public int getMaxAttemptForMeasurement(String productId, String partName, int measurementSeqNo) {
		return (int) count(Parameters.with("id.productId", productId)
				.put("id.partName", partName)
				.put("id.measurementSequenceNumber", measurementSeqNo)) + 1;
	}
	
	@Transactional
	public int moveAllData(String newProductId, String currentProductId) {
		Parameters params = Parameters.with("productId", newProductId);
		params.put("oldProductId", currentProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}
	
	public List<MeasurementAttempt> findAllByProductIdAndOperationName(String productId, String operationName) {
		Parameters params = Parameters.with("id.productId", productId);
		params.put("id.partName", operationName);
		return findAll(params);
	}

}
