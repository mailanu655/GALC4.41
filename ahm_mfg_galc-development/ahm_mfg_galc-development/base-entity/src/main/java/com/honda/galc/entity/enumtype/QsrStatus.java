package com.honda.galc.entity.enumtype;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QsrStatus</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 15, 2010</TD>
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
public enum QsrStatus {

	OPEN(0, "Open"), ACTIVE(5, "Active"), INACTIVE(10, "Inactive"), COMPLETED(20, "Completed"), CANCELLED(25, "Canceled");

	private int intValue;
	private String label;

	private QsrStatus(int intValue, String label) {
		this.intValue = intValue;
		this.label = label;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getLabel() {
		return label;
	}

	public static QsrStatus getByIntValue(int intValue) {
		for (QsrStatus s : values()) {
			if (intValue == s.getIntValue()) {
				return s;
			}
		}
		return null;
	}
}
