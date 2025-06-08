package com.honda.galc.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Utility methods for validation functionality.
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
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class ValidatorUtils {

	public static String formatMessages(List<String> messages) {
		return formatMessages(messages, "\n");
	}
	
	public static String formatMessages(List<String> messages, String separator) {
		if (messages == null || messages.isEmpty()) {
			return "";
		}
		StringBuilder stringBuilder = new StringBuilder();
		int ix = 0;
		int size = messages.size();
		for (String element : messages) {
			stringBuilder.append(element);
			ix++;
			if (ix < size) {
				stringBuilder.append(separator);
			}
		}
		return stringBuilder.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof Collection) {
			return isEmpty((Collection) value);
		} else if (value instanceof Map) {
			return isEmpty((Map) value);
		} else if (value.getClass().isArray() ) {
			return isEmpty((Object[]) value);
		} else if (value instanceof String) {
			return isEmpty((String) value);
		}
		return false;
	}
	
	public static boolean isEmpty(String value) {
		if (value == null) {
			return true;
		}
		if (value.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	//TODO for collection, should i iterate ?
	public static boolean isEmpty(Collection value) {
		if (value == null || value.size() == 0) {
			return true;
		}
		return false;
	}	
	public static boolean isEmpty(Map value) {
		if (value == null || value.size() == 0) {
			return true;
		}
		return false;
	}	
	
	public static boolean isEmpty(Object[] value) {
		if (value == null || value.length == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isFalse(Object value) {
		if (value == null) {
			return true;
		}
		
		if (value instanceof Boolean) {
			Boolean v = (Boolean) value;
			return v.booleanValue() == false;
		}
		
		return Boolean.valueOf(value.toString()) == false;
	}
}
