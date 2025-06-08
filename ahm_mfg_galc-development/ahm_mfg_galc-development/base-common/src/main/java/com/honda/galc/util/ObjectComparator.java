package com.honda.galc.util;

import java.util.Comparator;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>ObjectComparator</code> is ... .
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
public class ObjectComparator<T> implements Comparator<T> {

	private String[] propertyNames;

	public ObjectComparator(String... propertyNames) {
		this.propertyNames = propertyNames;
	}

	public int compare(T o1, T o2) {
		if (getPropertyNames() == null || getPropertyNames().length == 0) {
			return BeanUtils.safeCompare(o1, o2);
		}
		for (String pn : getPropertyNames()) {
			if (pn == null) {
				continue;
			}
			Object p1 = BeanUtils.getNestedPropertyValue(o1, pn);
			Object p2 = BeanUtils.getNestedPropertyValue(o2, pn);
			int result = BeanUtils.safeCompare(p1, p2);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	protected String[] getPropertyNames() {
		return propertyNames;
	}
}
