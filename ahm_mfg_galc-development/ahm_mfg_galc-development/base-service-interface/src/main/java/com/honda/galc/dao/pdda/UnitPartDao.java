package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitPart;
import com.honda.galc.entity.pdda.UnitPartId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitPartDao extends IDaoService<UnitPart, UnitPartId> {
	public List<UnitPart> findAllByMaintenanceId(int maintId);
	public List<UnitPart> findAllByMaintenanceIdAndMTOC(int maintId,String modelCode,String modelTypeCode);
	public String findPartBlockCode(String ymto, String opName, String partId, int partRev, String partNo);	
	public String findDeliveryLocation(String model, String modelType, String opName, int opRev, String partNo, String itemNo, String secCode);

}
