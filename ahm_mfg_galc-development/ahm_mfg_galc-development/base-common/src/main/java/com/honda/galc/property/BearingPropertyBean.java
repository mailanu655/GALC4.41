package com.honda.galc.property;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPropertyBean</code> is ... .
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
 * @author Karol Wozniak
 */
@PropertyBean(componentId="DEFAULT_BEARING_SELECT")
public interface BearingPropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue = "5")
	public int getMainBearingCount();

	@PropertyBeanAttribute(defaultValue = "4")
	public int getConrodCount();

	@PropertyBeanAttribute(defaultValue = "4,5,7,9")
	public Integer[] getMainBearingCountValues();

	@PropertyBeanAttribute(defaultValue = "4,6,8")
	public Integer[] getConrodCountValues();

	@PropertyBeanAttribute(defaultValue = "A,B,C,D,E,F")
	public String[] getMainBearingColumnValues();

	@PropertyBeanAttribute(defaultValue = "1,2,3,4,5,6")
	public String[] getMainBearingRowValues();

	@PropertyBeanAttribute(defaultValue = "1,2,3,4,5,6")
	public String[] getConrodBearingColumnValues();

	@PropertyBeanAttribute(defaultValue = "A,B,C,D,E,F")
	public String[] getConrodBearingRowValues();
	
	@PropertyBeanAttribute(defaultValue = "Conrod")
	public String[] getConrodBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "Main Upper,Main Lower")
	public String[] getMainBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "Conrod,Main Upper,Main Lower")
	public String[] getBearingTypes();
	
	@PropertyBeanAttribute(defaultValue = "false")//change default to false after testing
	public boolean getUseBearingMatrixForBearingPick();
}
