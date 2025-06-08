package com.honda.galc.service.property;


import java.util.Map;

import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>AsynchProductionReportPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> AsynchProductionReportPropertyBean description </p>
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
 * <TD>Justin Jiang</TD>
 * <TD>Dec. 12, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Justin Jiang
 * @since Dec. 12, 2014
 */

@PropertyBean(componentId ="OIF_GIV730_ASYNCH_PRODUCTION_REPORT")
public interface AsynchProductionReportPropertyBean extends IProperty {
	
	//define plan code to process points mapping
	//this report groups units by plan code, which may have multiple process points
	@PropertyBeanAttribute    
	public Map<String, String> getPlanCodePpMap();
	
}
