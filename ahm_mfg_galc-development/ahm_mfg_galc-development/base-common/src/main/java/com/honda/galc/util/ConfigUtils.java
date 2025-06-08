package com.honda.galc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.honda.galc.constant.Delimiter;
/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ConfigUtils</code> is ...
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
 * <TD>Nov 19, 2008</TD>
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
public class ConfigUtils {

	
	public static List<Map<String, String>> stringToListOfMaps(String str) {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		if (isEmpty(str)) {
			return list;
		}

		String[] a = str.split("[}][,][{]");

		for (String el : a) {
			if (isEmpty(el)) {
				continue;
			}
			String s = el.replace("{", "").replace("}", "");
			if (isEmpty(s)) {
				continue;
			}

			Map<String, String> map = stringToMap(s);
			if (!map.isEmpty()) {
				list.add(map);
			}
		}
		return list;
	}

	public static Map<String, String> stringToMap(String str) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (isEmpty(str)) {
			return map;
		}
		String[] a = str.split(Delimiter.COMMA);

		for (String el : a) {
			if (isEmpty(el)) {
				continue;
			}
			Map<String, String> entry = stringToMapEntry(el);
			if (!entry.isEmpty()) {
				map.putAll(entry);
			}
		}
		return map;
	}

	protected static Map<String, String> stringToMapEntry(String str) {
		Map<String, String> entry = new HashMap<String, String>();
		if (isEmpty(str)) {
			return entry;
		}

		String[] ar = str.split(Delimiter.COLON);
		String name = ar[0];
		String value = "";
		if (isEmpty(name)) {
			return entry;
		}
		if (ar.length > 1) {
			value = ar[1];
			if (isEmpty(value)) {
				value = "";
			}
		}
		entry.put(name, value);
		return entry;
	}
	
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		return str.trim().length() == 0;
	}
}
