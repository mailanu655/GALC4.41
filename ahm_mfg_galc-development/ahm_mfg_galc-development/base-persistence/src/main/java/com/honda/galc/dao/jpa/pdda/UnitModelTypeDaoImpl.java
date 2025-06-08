package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitModelTypeDao;
import com.honda.galc.entity.pdda.Unit;
import com.honda.galc.entity.pdda.UnitModelType;
import com.honda.galc.entity.pdda.UnitModelTypeId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitModelTypeDaoImpl extends BaseDaoImpl<UnitModelType, UnitModelTypeId>
		implements UnitModelTypeDao {
	
	
	public List<UnitModelType> findAllByMaintenanceId(Unit unit) {
		return findAll(Parameters.with("unit", unit));
	}

}
