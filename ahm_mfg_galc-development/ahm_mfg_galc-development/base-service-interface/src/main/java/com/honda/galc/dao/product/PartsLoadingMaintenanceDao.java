/**
 * 
 */
package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartsLoadingMaintenance;
import com.honda.galc.service.IDaoService;

/**
 * @author vf031824
 *
 */
public interface PartsLoadingMaintenanceDao extends IDaoService<PartsLoadingMaintenance, String> {

	List<PartsLoadingMaintenance> findAllByProcessPointId(String processPointId);

	PartsLoadingMaintenance findByProcessPointIdAndBinName(String processPointId, String binName);

	PartsLoadingMaintenance findByPartNameAndPartId(String partName, String partSpecId);
	
	PartsLoadingMaintenance findByProcessPointPartNameAndPartId(PartSpec spec, String processPointId);
}
