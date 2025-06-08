package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.entity.pdda.UnitControlMethodId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitControlMethodDao extends IDaoService<UnitControlMethod, UnitControlMethodId> {
	
	public List<UnitControlMethod> findAllControlMethods(int maintenanceId);
}
