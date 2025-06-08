package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitControlMethodDao;
import com.honda.galc.entity.pdda.UnitControlMethod;
import com.honda.galc.entity.pdda.UnitControlMethodId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitControlMethodDaoImpl extends BaseDaoImpl<UnitControlMethod, UnitControlMethodId>
		implements UnitControlMethodDao {
	
	public List<UnitControlMethod> findAllControlMethods(int maintenanceId) {
		return findAll(Parameters.with("id.maintenanceId", maintenanceId));
	}
}
