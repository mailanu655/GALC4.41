package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HostPriorityPlanDao;
import com.honda.galc.entity.product.HostPriorityPlan;
import com.honda.galc.entity.product.HostPriorityPlanId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>HostPriorityPlanDaoImpl Class description</h3>
 * <p> HostPriorityPlanDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Nov 5, 2012
 *
 *
 */
public class HostPriorityPlanDaoImpl extends BaseDaoImpl<HostPriorityPlan, HostPriorityPlanId> implements HostPriorityPlanDao{

	public List<HostPriorityPlan> findAllByLineNumber(String lineNumber) {
		
		return findAll(Parameters.with("id.lineNumber", lineNumber),new String[]{"id.prodSeqNumber"},true);
		
	}

	public List<HostPriorityPlan> findAllNotProcessedByLineNumber(String lineNumber) {
		
		return findAll(Parameters.with("id.lineNumber", lineNumber).put("rowProcessed","N"),new String[]{"id.prodSeqNumber"},true);
		
	}

	public List<HostPriorityPlan> findAllNewPlans(String processLocation) {
		Parameters params = Parameters.with("id.planProcLoc", processLocation).put("rowProcessed","N");
		return findAll(params,new String[]{"id.prodSeqNumber"},true);
	}
	
	
}
