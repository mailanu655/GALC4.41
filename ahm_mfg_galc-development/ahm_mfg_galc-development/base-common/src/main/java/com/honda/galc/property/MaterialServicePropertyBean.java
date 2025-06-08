package com.honda.galc.property;

/**
 * 
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>MaterialServicePropertyBean</code> is ... .
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
 * @created Mar 4, 2015
 */
public interface MaterialServicePropertyBean extends IProperty {

	@PropertyBeanAttribute(defaultValue = "2")
	public int getNumberOfDaysToSelect();

	@PropertyBeanAttribute(defaultValue = "7")
	public int getNumberOfDaysToKeep();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProcessPointIds();
	
	@PropertyBeanAttribute(defaultValue = "yyMMdd")
	public String getDateFormatPattern();
	
	@PropertyBeanAttribute(defaultValue = "HHmmss")
	public String getTimeFormatPattern();
	
	@PropertyBeanAttribute(defaultValue = "yyyyMMddHHmmss")
	public String getDateTimeFormatPattern();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProcessLocations();
}
