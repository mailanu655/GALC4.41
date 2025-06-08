package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.entity.pdda.UnitModelType;
import com.honda.galc.entity.pdda.UnitModelTypeId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitModelTypeDao extends IDaoService<UnitModelType, UnitModelTypeId> {
	
	public List<UnitModelType> findAllByMaintenanceId(Unit unit);

}
