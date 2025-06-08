package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.StandardSchedule;
import com.honda.galc.entity.product.StandardScheduleId;
import com.honda.galc.service.IDaoService;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>StandardScheduleDao</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StandardScheduleDao description </p>
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
 * @author Paul Chou
 * Mar 7, 2011
 *
 */
public interface StandardScheduleDao extends IDaoService<StandardSchedule, StandardScheduleId>, ScheduleDao{
	public List<StandardSchedule> findAllById(StandardScheduleId id);
	public boolean isProductionDay(String processLocation, String productionTimestampStr);
	public List getQueryResults(String query, Parameters params);
}
