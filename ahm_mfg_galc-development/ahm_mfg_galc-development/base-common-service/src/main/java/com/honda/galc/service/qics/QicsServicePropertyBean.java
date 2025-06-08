package com.honda.galc.service.qics;

import java.util.Map;

import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.property.IProperty;
import com.honda.galc.property.PropertyBean;
/**
 * 
 * <h3>QicsServicePropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsServicePropertyBean description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 */
@PropertyBean(componentId = "qics")
public interface QicsServicePropertyBean  extends IProperty{
	
	public Map<String, String> getDefectCodeMapping() throws PropertyException;
}
