package com.honda.galc.client.qics.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Zone;





public class QicsUtils {
	public final static String	NULL_STRING	=	"";

	public static String getTaskArgument(String argument, String key) {

		if (argument == null || key == null || "null".equalsIgnoreCase(argument) || "null".equalsIgnoreCase(key)) {
			return NULL_STRING;
		}

		StringTokenizer st = new StringTokenizer(argument, ",");

		while (st.hasMoreElements()) {

			String argPart = (String) st.nextElement();
			int ix = argPart.indexOf(":");
			if (ix > -1) {
				String argTitle = argPart.substring(0, ix);
				if (argTitle.equals(key)) {
					return argPart.substring(argPart.indexOf(":") + 1, argPart.length());
				}
			}
		}
		return NULL_STRING;
	}

	public static List<String> getDepartmentNameList(List<Division> dataList) {

		List<String> nameList = new ArrayList<String>();
		if (dataList == null || dataList.isEmpty()) {
			return nameList;
		}

		for (Division data : dataList) {
			nameList.add(data.getDivisionName());
		}
		return nameList;
	}

	public static String getDepartmentId(List<Division> dataList, String name) {
		String id = null;
		if (dataList == null || dataList.isEmpty() || name == null) {
			return id;
		}

		for (Division data : dataList) {
			if (name.equals(data.getDivisionName())) {
				return data.getDivisionId();
			}
		}
		return id;
	}

	public static String getLineId(List<Line> dataList, String name) {
		String id = null;
		if (dataList == null || dataList.isEmpty() || name == null) {
			return id;
		}

		for (Line data : dataList) {
			if (name.equals(data.getLineName())) {
				return data.getLineId();
			}
		}
		return id;
	}

	public static List<String> getLineNameList(List<Line> dataList) {

		List<String> nameList = new ArrayList<String>();
		if (dataList == null || dataList.isEmpty()) {
			return nameList;
		}

		for (Line data : dataList) {
			nameList.add(data.getLineName());
		}
		return nameList;
	}

	public static List<String> getZoneNameList(List<Zone> dataList) {

		List<String> nameList = new ArrayList<String>();
		if (dataList == null || dataList.isEmpty()) {
			return nameList;
		}

		for (Zone data : dataList) {
			nameList.add(data.getZoneName());
		}
		return nameList;
	}

	
	public static boolean getBooleanPropertyValue(Properties map, String key) {
		boolean value = false;
		if (map == null || key == null) {
			return false;
		}
		String stringValue = map.getProperty(key);
		value = Boolean.valueOf(stringValue);
		return value;
	}

	public static String getStringPropertyValue(Properties map, String key) {
		String value = "";
		if (map == null) {
			return "";
		}

		String stringValue = map.getProperty(key);
		if (stringValue == null) {
			return "";
		}
		value = stringValue.toString();
		return value;
	}

	public static Integer getIntegerPropertyValue(Properties map, String key) {
		Integer value = null;
		if (map == null) {
			return null;
		}

		String stringValue = map.getProperty(key);
		if (!isInteger(stringValue)) {
			return null;
		}
		value = Integer.valueOf(stringValue);
		return value;
	}

	public static boolean isInteger(String string) {
		if (string == null) {
			return false;
		}
		try {
			Integer.valueOf(string);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T copy(Object object) {
		if (object == null) {
			return null;
		}
		T copy = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(object);
			out.flush();
			out.close();
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
			copy = (T) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return copy;
	}
}
