package com.honda.galc.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>PropertyComparator</code> is ...
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
 * <TD>Karol Wozniak</TD>
 * <TD>Sep 2, 2008</TD>
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
public class PropertyComparator<T> implements Comparator<T> {

	private List<Method> propertyAccessors = new ArrayList<Method>();

	public PropertyComparator(Class<T> clazz, String... propertyNames) {
		try {
			if (clazz != null) {
				for (String propertyName : propertyNames) {
					Method method = BeanUtils.getPropertyAccessor(clazz, propertyName);
					if (method != null) {
						getPropertyAccessors().add(method);
					}
				}
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public int compare(T o1, T o2) {
		return BeanUtils.compare(getPropertyAccessors(), o1, o2);
	}

	protected List<Method> getPropertyAccessors() {
		return propertyAccessors;
	}
}
