package com.honda.galc.client.gts.property;

import com.honda.galc.property.GtsDefaultPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.property.SystemPropertyBean;

/**
 * 
 * 
 * <h3>GtsPropertyBean Class description</h3>
 * <p> GtsPropertyBean description </p>
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
 * May 11, 2015
 *
 *
 */
public interface GtsClientPropertyBean  extends GtsDefaultPropertyBean,SystemPropertyBean{
	
	/**
	 * optional user actions in the tracking client "Tracking" menu
	 * @return
	 */
	@PropertyBeanAttribute(propertyKey="OPTIONAL_USER_ACTIONS" , defaultValue = "")
	public String[] getOptionalUserActions();
	
	/**
	 * paint on process point id
	 * @return
	 */
	public String getPaintOnProcessPointId();
	
	/**
	 * Weld On Process Point Id
	 */
	public String getWeldOnProcessPointId();
	
	/**
	 * Weld Off Process Point Id
	 * @return
	 */
	public String getWeldOffProcessPointId();
	
	@PropertyBeanAttribute(propertyKey="ALLOW_MANUL_MODE" , defaultValue = "FALSE")
	public boolean isAllowManualMode();
	
	@PropertyBeanAttribute(propertyKey="ALLOW_ALARM" , defaultValue = "FALSE")
	public boolean isAllowAlarm();
}
