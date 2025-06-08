package com.honda.galc.property;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ProductPropertyBean</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jackie
 */
@PropertyBean(componentId ="Default_RecipeDownloadPDAService")
public interface RecipeDownloadPDAServicePropertyBean extends IProperty{
	/**
	 * define the client id for get NG part lot control rule
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "",propertyKey="NG_PART_LOT_CONTROL_RULE_CLIENT_ID")
	public String getNGPartLotControlRuleClientId();
	
	/**
	 * define the client id for update NG part
	 * @return
	 */
	@PropertyBeanAttribute(defaultValue = "",propertyKey="UPDATE_NG_PART_CLIENT_ID")
	public String getUpdateNGPartClientId();
}
