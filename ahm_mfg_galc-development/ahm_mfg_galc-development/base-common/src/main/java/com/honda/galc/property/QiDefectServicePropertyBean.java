package com.honda.galc.property;

import java.util.Map;

/**
 * 
 * <h3>QicsPropertyBean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> QicsPropertyBean description </p>
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
 * <TD>Aug 26, 2016</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Vivek Bettada
 * @since Nov 11, 2019
 */


@PropertyBean(componentId ="QI_DEFECT_SERVICE")
public interface QiDefectServicePropertyBean extends IProperty{

	/**
	 * Rest Root URL to call for PUT
	 */
	@PropertyBeanAttribute(propertyKey="PUT", defaultValue="")
	public String getQiDefectServicePut();
	
	/**
	 * Rest Root URL to call for PUT
	 */
	@PropertyBeanAttribute(propertyKey="REPAIR", defaultValue="")
	public String getQiDefectServiceRepair();
	
	/**
	 * Rest Root URL to call for PUT
	 */
	@PropertyBeanAttribute(propertyKey="DELETE", defaultValue="")
	public String getQiDefectServiceDelete();
	
	/**
	 * Rest URL to call for LotControlService repair
	 * this is temporary.  This function will be moved to Device definition in next release
	 */
	@PropertyBeanAttribute(propertyKey="LOT_CONTROL_SERVICE_REPAIR", defaultValue="")
	public String getLotControlServiceRepair();
	
}
