package com.honda.galc.client.datacollection.property;

import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>BuildResultCheckPropertyBean Class description</h3>
 * <p> BuildResultCheckPropertyBean description </p>
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
 * Jan 25, 2012
 *
 *
 */
@PropertyBean
public interface BuildResultCheckPropertyBean extends IProperty{
	/**
     * Get a list of parts used to check build reult based on model type 
     * @return
     */
    @PropertyBeanAttribute(defaultValue = "")
    public Map<String, String> getBuildResultCheckPartList();
    
    @PropertyBeanAttribute(defaultValue = "")
     public String[] getBuildAttributeCheckPartList();
}
