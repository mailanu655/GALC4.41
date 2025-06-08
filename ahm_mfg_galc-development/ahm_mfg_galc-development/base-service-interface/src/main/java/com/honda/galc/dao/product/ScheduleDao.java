package com.honda.galc.dao.product;

import java.util.List;


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
public interface ScheduleDao  {
	List<Object[]> getSchedule(String plant, String line, String processLocation, String productionDate);
	
	List<Object[]> getProcessInfo();
	
	void saveSchedules(List<Object[]> schedules); 
}
