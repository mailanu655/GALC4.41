package com.honda.galc.entity.enumtype;

import com.honda.galc.enumtype.IdEnum;

/**
 * <h3>Class Description</h3>
 * <p>
 * <code>MetricDataFormat</code>
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
 * </TR>
 * <TR>
 * <TD>L&T Infotech</TD>
 * <TD>15/11/2016</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Release 2</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public enum MetricDataFormat implements IdEnum<MetricDataFormat> {

	PERCENTAGE(1, "%"), DECIMAL(2, "#.####");

	private int id;

	private String formatDesc;

	private MetricDataFormat(int id, String formatDesc) {
		this.id = id;
		this.formatDesc = formatDesc;
	}

	public int getId() {
		return id;
	}

	public String getFormatDesc() {
		return formatDesc;
	}
	
	public static MetricDataFormat getMetricDataFormat(String formatDesc) {

		for (MetricDataFormat dataFormat : MetricDataFormat.values()) {
			if (dataFormat.getFormatDesc().equalsIgnoreCase(formatDesc)) {
				return dataFormat;
			}
		}
		return null;
	}

}
