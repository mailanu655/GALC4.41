package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartsLoadingMaintenanceDao;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartsLoadingMaintenance;
import com.honda.galc.service.Parameters;

import java.util.List;

public class PartsLoadingMaintenanceDaoImpl extends BaseDaoImpl<PartsLoadingMaintenance, String> implements PartsLoadingMaintenanceDao {
	
	public List<PartsLoadingMaintenance> findAllByProcessPointId(String processPointId) {
		return findAll(Parameters.with("processPointId", processPointId));	
	}

	public PartsLoadingMaintenance findByProcessPointIdAndBinName(String processPointId, String binName) {
		Parameters params = new Parameters();
		params.put("processPointId", processPointId);
		params.put("binName", binName);
		return findFirst(params);
	}

	public PartsLoadingMaintenance findByPartNameAndPartId(String partName, String partSpecId) {
		Parameters params = new Parameters();
		params.put("partName", partName);
		params.put("partSpecId", partSpecId);
		return findFirst(params);
	}
	
	public PartsLoadingMaintenance findByProcessPointPartNameAndPartId(PartSpec spec, String processPointId) {
		Parameters params = new Parameters();
		params.put("processPointId", processPointId);
		params.put("partName", spec.getId().getPartName());
		params.put("partSpecId", spec.getId().getPartId());
		
		return findFirst(params);
		
	}
}
