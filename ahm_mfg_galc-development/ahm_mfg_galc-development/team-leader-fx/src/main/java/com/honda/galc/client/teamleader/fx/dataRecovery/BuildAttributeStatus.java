package com.honda.galc.client.teamleader.fx.dataRecovery;

import com.honda.galc.entity.enumtype.InstalledPartStatus;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>BuildAttributeStatus</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * <TR>
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>Jul 12, 2017</TD>
 * <TD>1.0.0</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public enum BuildAttributeStatus {
	OK(InstalledPartStatus.OK.getId(), "OK"), FAILED(InstalledPartStatus.NG.getId(), "Failed");

	private int intValue;
	private String label;

	private BuildAttributeStatus(int intValue, String label) {
		this.intValue = intValue;
		this.label = label;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return getLabel();
	}
}