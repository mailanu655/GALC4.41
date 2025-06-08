package com.honda.galc.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.BeanUtils;

/**
 * 
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>FlfFormat</code> is a Fiexed Length File formatter .
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
 * @created Mar 27, 2015
 */
public class FlfFormat {

	// === flf formating api === //
	public static List<String> format(List<?> data, List<FlfFieldConfig> fieldConfigs, int lineLength) {
		List<String> list = new ArrayList<String>();
		for (Object obj : data) {
			String line = formatLine(obj, fieldConfigs, lineLength);
			list.add(line);
		}
		return list;
	}

	public static String formatLine(Object bean, List<FlfFieldConfig> fieldConfigs, int lineLength) {
		StringBuilder sb = new StringBuilder(StringUtils.repeat(" ", lineLength));
		for (FlfFieldConfig fc : fieldConfigs) {
			Object obj = BeanUtils.getNestedPropertyValue(bean, fc.getPropertyName());
			String value = formatField(obj, fc, lineLength);
			if (value != null) {
				if (fc.getStartIx() < lineLength && fc.getEndIx() < lineLength) {
					sb.replace(fc.getStartIx(), fc.getStartIx() + value.length(), value);
				}
			}
		}
		return sb.toString();
	}

	public static String formatField(Object fieldValue, FlfFieldConfig fc, int lineLength) {
		if (fieldValue == null) {
			fieldValue = fc.getDefaultValue();
		}
		String value = null;
		if (fieldValue != null) {
			value = ObjectUtils.toString(fieldValue);
			if (value.length() > fc.getLength()) {
				value = StringUtils.repeat("*", fc.getLength());
			}
		}
		return value;
	}
}
