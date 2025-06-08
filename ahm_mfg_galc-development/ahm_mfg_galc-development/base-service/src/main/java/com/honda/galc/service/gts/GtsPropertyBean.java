package com.honda.galc.service.gts;

import com.honda.galc.property.GtsDefaultPropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
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
 * Jan 31, 2018
 *
 *
 */
public interface GtsPropertyBean extends GtsDefaultPropertyBean{
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getUnsolicitedDataList();

	@PropertyBeanAttribute(propertyKey ="NOT_UPDATE_TRACKING_READERS",defaultValue = "")
	public String[] getNotUpdateTrackingReaders();

}
