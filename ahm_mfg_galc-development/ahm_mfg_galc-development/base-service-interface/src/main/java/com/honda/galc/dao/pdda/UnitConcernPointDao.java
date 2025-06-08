package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitConcernPoint;
import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitConcernPointDao extends IDaoService<UnitConcernPoint, Integer> {
	public List<UnitConcernPoint> findAllConcernPoint(int maintenanceId);
}
