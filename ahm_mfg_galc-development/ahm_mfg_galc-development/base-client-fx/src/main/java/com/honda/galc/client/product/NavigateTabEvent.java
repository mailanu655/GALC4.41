package com.honda.galc.client.product;

import com.honda.galc.client.ui.IEvent;

/**
* <h3>Class description</h3> <h4>Description</h4>
* <p>
* <code>NavigateTabEvent</code> is ... .
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
* @author L&T Infotech
*/

public class NavigateTabEvent implements IEvent {
	private String navigateToTab;
	private boolean switchProcessTab = false;
	private boolean nextTab = false;
	
	public String getNavigateToTab() {
		return navigateToTab;
	}
	public void setNavigateToTab(String navigateToTab) {
		this.navigateToTab = navigateToTab;
	}
	/**
	 * @return the switchProcessTab
	 */
	public boolean isSwitchProcessTab() {
		return switchProcessTab;
	}
	/**
	 * @param switchProcessTab the switchProcessTab to set
	 */
	public void setSwitchProcessTab(boolean switchProcessTab) {
		this.switchProcessTab = switchProcessTab;
	}
	/**
	 * @return the nextTab
	 */
	public boolean isNextTab() {
		return nextTab;
	}
	/**
	 * @param nextTab the nextTab to set
	 */
	public void setNextTab(boolean nextTab) {
		this.nextTab = nextTab;
	}

}

