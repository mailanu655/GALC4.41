package com.honda.galc.client.teamleader.property;

import com.honda.galc.property.PropertyBeanAttribute;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ManualDcMcOnOffPropertyBean</code> is ... .
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
 * @created Sep 3, 2014
 */
public interface ManualDcMcOnOffPropertyBean extends ManualOnOffPropertyBean {

	/**
	 * Comma delimited process point ids, ex: DC_ON_PROCESS_POINTS:PP1,PP2,PP3
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDcOnProcessPoints();

	/**
	 * Comma delimited process point ids, ex: DC_OFF_PROCESS_POINTS:PP1,PP2,PP3
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getDcOffProcessPoints();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMcOnProcessPoints();

	@PropertyBeanAttribute(defaultValue = "")
	public String[] getMcOffProcessPoints();

	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isNoneHomeProductDefault();
	/**
	 * Process to be populated on the screen ex: PROCESS: ON, PROCESS:OFF
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getProcess();
	
	/**
	 * Customizable String title to be set for screen ex: TITLE: Machining On
	 */
	@PropertyBeanAttribute(defaultValue = "Manual DC/MC - ON/OFF")
	public String getTitle();
	
	/**
	 * Client ID to which stopper release need to be sent ex: STOPPER_RELEASE: CLIENT_ID1
	 */
	@PropertyBeanAttribute(defaultValue = "")
	public String getStopperRelease();

}
