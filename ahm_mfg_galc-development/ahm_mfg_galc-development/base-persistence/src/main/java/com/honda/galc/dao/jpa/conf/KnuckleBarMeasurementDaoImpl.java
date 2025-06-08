package com.honda.galc.dao.jpa.conf;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.KnuckleBarMeasurementDao;
import com.honda.galc.entity.product.KnuckleBarMeasurement;
import com.honda.galc.entity.product.KnuckleBarMeasurementId;
import com.honda.galc.service.Parameters;

@SuppressWarnings("unchecked")
public class KnuckleBarMeasurementDaoImpl extends BaseDaoImpl<KnuckleBarMeasurement, KnuckleBarMeasurementId> implements KnuckleBarMeasurementDao {

	private String PART_NAME = "partName";
	private String PART_SERIAL_NUMBER = "partSerialNumber";
	
	private String GET_ROW_FOR_PARTNAME_SERIALNO = "SELECT kbm FROM KnuckleBarMeasurement AS kbm WHERE kbm.id.partName = :partName AND kbm.id.partSerialNumber= :partSerialNumber";
	
	private String DISTINCT_PARTNAME_ID = "SELECT DISTINCT kbm.id.partName, kbm.id.partId FROM KnuckleBarMeasurement AS kbm WHERE kbm.id.partSerialNumber= :partSerialNumber";
	

	@Transactional
	public KnuckleBarMeasurement findByKey(KnuckleBarMeasurementId id) {
		return findByKey(id);
	}
	
	@Transactional
	public List findDistPartNameId(String PartSerialNumber) {
		
		Parameters params = new Parameters();
		params.put("partSerialNumber", PartSerialNumber);
		
		return findAllByQuery(DISTINCT_PARTNAME_ID, params);

	}
	
	@Transactional
	public List<KnuckleBarMeasurement> getMeasurementForNameSlNo(String partName, String partSlNo) {
			Parameters params = Parameters.with(PART_NAME, partName);
 			params.put(PART_SERIAL_NUMBER, partSlNo);
		return findAllByQuery(GET_ROW_FOR_PARTNAME_SERIALNO, params);
	}
}
