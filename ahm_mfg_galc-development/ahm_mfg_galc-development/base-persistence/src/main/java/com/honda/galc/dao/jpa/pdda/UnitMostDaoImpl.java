package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitMostDao;
import com.honda.galc.entity.pdda.UnitMost;
import com.honda.galc.entity.pdda.UnitMostId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitMostDaoImpl extends BaseDaoImpl<UnitMost, UnitMostId>
		implements UnitMostDao {
	public List<UnitMost> findAllWorkingPointDtl(int maintenanceId) {
		Parameters params = Parameters.with("id.maintenanceId", maintenanceId);
		params.put("id.showOnPrint","Y");
		return findAll(params);
	}


}
