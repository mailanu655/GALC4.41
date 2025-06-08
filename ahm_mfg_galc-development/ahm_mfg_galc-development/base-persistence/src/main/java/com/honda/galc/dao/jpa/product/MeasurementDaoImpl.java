package com.honda.galc.dao.jpa.product;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.service.Parameters;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
public class MeasurementDaoImpl extends BaseDaoImpl<Measurement,MeasurementId> implements MeasurementDao {

    private static final long serialVersionUID = 1L;
    
    private static final String UPDATE_PRODUCT_ID = "update Measurement e set e.id.productId = :productId where e.id.productId = :oldProductId";
    
    @Autowired
    private MeasurementAttemptDao measurementAttemptDao;

	public List<InstalledPart> findMeasurementsForInstalledParts(
			List<InstalledPart> installedParts) {
		return findMeasurementsForInstalledParts(installedParts, false);
	}

	public List<InstalledPart> findMeasurementsForInstalledParts(List<InstalledPart> installedParts, boolean orderByMeasSeq) {
		for(InstalledPart part : installedParts){
			if(part != null){
				Parameters params = new Parameters(); 
				params.put("id.productId", part.getId().getProductId());
				params.put("id.partName", part.getId().getPartName());
				if(orderByMeasSeq){
					part.setMeasurements(findAll(params, new String[]{"id.measurementSequenceNumber"}, true));
				}else{
					part.setMeasurements(findAll(params));
				}
			}
		}
		return installedParts;
	}

	public List<Measurement> findAllByProductId(String productId) {
		Parameters params = new Parameters(); 
		params.put("id.productId", productId);
		return findAll(params);
	}

	@Transactional 
	public int deleteProdIds(List <String> prodIds) {
        int count = 0;
		for( String prodId : prodIds )
		{
            count = count + delete(Parameters.with("id.productId", prodId));
		}
        return count;
	}
	
	@Transactional
	public void deleteAll(String productId, String partName) {
		delete(Parameters.with("id.productId", productId).put("id.partName", partName));
		
	}	 
	@Override
	public Measurement findByRefId(Long refId) {
		Parameters parameters = Parameters.with("defectRefId", refId);
		return findFirst(parameters);
	}
	
	public List<Measurement> findAll(String productId, String partName) {
		return findAll(Parameters.with("id.productId", productId).put("id.partName", partName));
	}
	
	public List<Measurement> findAllOrderBySequence(String productId, String partName,  boolean isAscending){
		String[] orderBy ={"id.measurementSequenceNumber"};
		return findAll(Parameters.with("id.productId", productId).put("id.partName", partName), orderBy, isAscending);
	}
	
	@Transactional
	public List<Measurement> saveAll(List<Measurement> measurements) {
		return saveAll(measurements, true);
	}
	
	@Transactional
	public List<Measurement> saveAll(List<Measurement> measurements, boolean isSaveHistory) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		LinkedHashMap<MeasurementId, Measurement> finalMeasMap = new LinkedHashMap<MeasurementId, Measurement>();

		for(Measurement meas: measurements){
			if (meas != null){
				if (meas.getActualTimestamp() == null || meas.getActualTimestamp().after(timestamp)) {
					meas.setActualTimestamp(timestamp);
				}
				if(isSaveHistory) {
					measurementAttemptDao.saveMeasurementHistory(meas);
				}
				finalMeasMap.put(meas.getId(), meas);
			}
		}

		super.saveAll(new ArrayList<Measurement>(finalMeasMap.values()));
		return measurements;
	} 

	@Transactional 
	public int updateProductId(String productId, String oldProductId) {
		Parameters params = Parameters.with("productId", productId);
		params.put("oldProductId", oldProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}

	public int moveAllData(String newProductId, String currentProductId) {
		measurementAttemptDao.moveAllData(newProductId, currentProductId);
		
		Parameters params = Parameters.with("productId", newProductId);
		params.put("oldProductId", currentProductId);
		return executeUpdate(UPDATE_PRODUCT_ID, params);
	}

	public List<Measurement> findAllByProductIdAndPartNames(String productId,
			List<String> partNames) {
		List<Measurement> measurements = new ArrayList<Measurement>();
		for(String partName : partNames){
			measurements.addAll(findAll(productId, partName));
		}
		return measurements;
	}
	
	public void deleteAllByProductIdAndPartNames(String productId,List<String> partNames) {
		Parameters params = Parameters.with("id.productId", productId).put("id.partName", partNames);
		delete(params);
	}
}
