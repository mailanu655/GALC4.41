package com.honda.galc.dao.jpa.pdda;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.pdda.UnitReactionPlanDao;
import com.honda.galc.entity.pdda.UnitReactionPlan;
import com.honda.galc.entity.pdda.UnitReactionPlanId;
import com.honda.galc.service.Parameters;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public class UnitReactionPlanDaoImpl extends BaseDaoImpl<UnitReactionPlan, UnitReactionPlanId>
		implements UnitReactionPlanDao {

	public List<UnitReactionPlan> findAllReactionPlans(int maintenanceId) {
		return findAll(Parameters.with("id.maintenanceId", maintenanceId));
	}
}
