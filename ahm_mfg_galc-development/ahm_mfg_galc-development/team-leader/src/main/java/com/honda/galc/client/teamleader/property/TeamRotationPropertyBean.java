package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>TeamRotationPropertyBean is the property bean for team rotation  team leader screen</h3>
 * <p>
 * </p>
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
 * @author Yang Xin<br>
 *         Oct 10, 2014
 */
@PropertyBean(componentId ="Team_Rotation_Properties")
public interface TeamRotationPropertyBean extends IProperty{
	
	/**
	 * Gets the team rotation shifts.
	 *
	 * @return the team rotation shifts
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getTeamRotationShifts();
	
	/**
	 * Gets the team rotation teams.
	 *
	 * @return the team rotation teams
	 */
	@PropertyBeanAttribute (defaultValue = "")
	public String[] getTeamRotationTeams();
}
