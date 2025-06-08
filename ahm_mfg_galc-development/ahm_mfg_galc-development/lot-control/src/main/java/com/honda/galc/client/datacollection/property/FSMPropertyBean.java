package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>FSMPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> FSMPropertyBean description </p>
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
 * Apr 23, 2010
 *
 */
@PropertyBean(componentId ="Default_FSM")
public interface FSMPropertyBean extends IProperty {
	/**
	 * Indicate the FMS type. Currently only support Default and Classic. 
	 * @return
	 */
	String getFsmType();
	
	/**
	 * Specifies the delay count for Refresh state
	 * It's can also be used as a flag to turn off the Refresh delay be set delay count 0;
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "10")
	int getDelayCount();

}
