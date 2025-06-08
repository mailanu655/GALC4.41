package com.honda.galc.client.datacollection.property;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * 
 * <h3>PartLotPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotPropertyBean description </p>
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
 * <TR>
 * <TD>P.Chou</TD>
 * <TD>Jan 24, 2012</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 24, 2012
 */
@PropertyBean(componentId ="Default_PartLot")
public interface PartLotPropertyBean  extends IProperty{
	
	@PropertyBeanAttribute(defaultValue = "TRUE")
	public boolean isAutoSavePartLot();

	@PropertyBeanAttribute(defaultValue = "Q")
	public String getPartLotQuantityPrefix();

	@PropertyBeanAttribute(defaultValue = "")
	public String getPartLotSnPrefix();
	
}
