package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitSpecialControlDao;
import com.honda.galc.entity.pdda.UnitSpecialControl;
import com.honda.galc.entity.pdda.UnitSpecialControlId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitSpecialControlDaoImpl extends BaseDaoImpl<UnitSpecialControl, UnitSpecialControlId>
		implements UnitSpecialControlDao {
	public List<UnitSpecialControl> findAllSpecialControl(int maintenanceId) {
		return findAll(Parameters.with("id.maintenanceId", maintenanceId));
	}


}
