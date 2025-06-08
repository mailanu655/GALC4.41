package com.honda.galc.dao.pdda;

import java.util.List;

import com.honda.galc.entity.pdda.UnitReactionPlan;
import com.honda.galc.entity.pdda.UnitReactionPlanId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Apr 16, 2014
 */
public interface UnitReactionPlanDao extends IDaoService<UnitReactionPlan, UnitReactionPlanId> {
	
	public List<UnitReactionPlan> findAllReactionPlans(int maintenanceId);
}
