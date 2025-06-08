package com.honda.galc.oif.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>DepartmentSchedulePropertyBean Class description</h3>
 * <p> DepartmentSchedulePropertyBean description </p>
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
 * Oct 31, 2012
 *
 *
 */
public interface DepartmentSchedulePropertyBean extends IProperty{
	
	@PropertyBeanAttribute(propertyKey = "NUMBER_OF_DAYS",defaultValue = "10")
	public int getDays();
	
	public Map<String, String> getPlanCodeMap();
	
	@PropertyBeanAttribute(defaultValue = "0")
	public int getStartDateOffset();

	@PropertyBeanAttribute(propertyKey = "USE_STANDARD_SCHEDULE",defaultValue = "false")
	public boolean useStandardSchedule();
}
