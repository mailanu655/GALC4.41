package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.HostPriorityPlan;
import com.honda.galc.entity.product.HostPriorityPlanId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * <h3>HostPriorityPlanDao Class description</h3>
 * <p> HostPriorityPlanDao description </p>
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
public interface HostPriorityPlanDao extends IDaoService<HostPriorityPlan,HostPriorityPlanId>{

	List<HostPriorityPlan> findAllByLineNumber(String lineNumber);
	
	List<HostPriorityPlan> findAllNotProcessedByLineNumber(String lineNumber);
	
	List<HostPriorityPlan> findAllNewPlans(String processLocation);
	
}
