package com.honda.galc.client.ui.component;

import java.text.Format;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>PropertiesMapping</code> is ... .
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
// TODO Remark, do not want to modify ColumnMappings/ColumnMapping at this time
// as a lot of components depend on them
// for qsr requirements will extend and use subclases
public class PropertiesMapping extends ColumnMappings {

	public ColumnMappings put(String name, String propertyPath) {
		get().add(new PropertyMapping(name, propertyPath));
		return this;
	}

	public ColumnMappings put(String name, String propertyPath, Class<?> type) {
		get().add(new PropertyMapping(name, propertyPath, type));
		return this;
	}

	public ColumnMappings put(String name, String propertyPath, Format format) {
		get().add(new PropertyMapping(name, propertyPath, format));
		return this;
	}

	public ColumnMappings put(String name, String propertyPath, Class<?> type, Format format) {
		get().add(new PropertyMapping(name, propertyPath, type, format));
		return this;
	}
}
