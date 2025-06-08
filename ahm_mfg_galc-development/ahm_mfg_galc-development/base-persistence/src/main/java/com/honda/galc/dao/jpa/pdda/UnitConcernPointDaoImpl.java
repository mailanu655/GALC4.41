package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitConcernPointDao;
import com.honda.galc.entity.pdda.UnitConcernPoint;
import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitConcernPointDaoImpl extends BaseDaoImpl<UnitConcernPoint, Integer>
		implements UnitConcernPointDao {
	public List<UnitConcernPoint> findAllConcernPoint(int maintenanceId) {
		return findAll(Parameters.with("maintenanceId", maintenanceId));
	}
}
