package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.KnuckleBarMeasurement;
import com.honda.galc.entity.product.KnuckleBarMeasurementId;
import com.honda.galc.service.IDaoService;

public interface KnuckleBarMeasurementDao extends IDaoService<KnuckleBarMeasurement, KnuckleBarMeasurementId>{

	@SuppressWarnings("unchecked")
	public List findDistPartNameId (String PartSerialNumber);
	
	public List<KnuckleBarMeasurement> getMeasurementForNameSlNo (String partName, String partSlNo);
}
