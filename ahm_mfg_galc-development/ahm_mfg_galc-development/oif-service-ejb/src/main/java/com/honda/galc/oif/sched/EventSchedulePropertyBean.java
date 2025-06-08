package com.honda.galc.oif.sched;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>EventSchedulePropertyBean</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Value type for holding OIF schedule property values
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 9, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@RL009</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
@PropertyBean(componentId = "EVENT_SCHEDULE")
public interface EventSchedulePropertyBean extends IProperty{

	public static final String COMPONENT = "EVENT_SCHEDULE";
	
	/**
	 * @return map of tasks to launch upon certain event
	 * 
	 * @throws PropertyException
	 */
	public Map<String, String> getEventTasks();

	/**
	 * @return list of event names to schedule
	 * 
	 * @throws PropertyException
	 */
	public String[] getEventList() ;

	public String[] getNewEventList();
	/**
	 * @return map of schedules for a certain event
	 * 
	 * @throws PropertyException
	 */
	public Map<String, String> getEventSchedules();
	
	/**
	 * configure if the new scheduler enabled
	 * @return
	 */
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isEventScheduleEnabled();
	
	@PropertyBeanAttribute (defaultValue = "false")
	public boolean isUseNewEventList();
}
