package com.honda.galc.util;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.honda.galc.common.logging.Logger;

public class ParseInfo {

	public static String DELIMETER_KEY = "DELIMITER";
	public static String OFFSET_KEY = "OFFSET";
	public static String INDEX_KEY = "INDEX";
	public static String LENGTH_KEY = "LENGTH";
	public static String MIN_PART_SN_LENGTH_KEY = "MIN_PART_SN_LENGTH";
	
	private HashMap<String, String> map = new HashMap<String, String>();
	private String pattern = "\\{(.*?)\\}"; 	// Example: {DELIMITER=,}{LENGTH=5}{OFFSET=4}{INDEX=6}

	private String parseString = "";

	public ParseInfo(String parseString) {
		this.parseString = parseString;
		this.map = buildMap(parseString);
	}

	private HashMap<String, String> buildMap(String parseString) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(parseString);
		while(m.find()) {
			String group = m.group(1);
			String[] keyValues = group.split("=");
			map.put(keyValues[0].trim(), keyValues[1].trim());
		}
		Logger.getLogger().check(map.toString());
		return map;
	}

	public String getDelimiter() {
		try {
			return map.get(DELIMETER_KEY);
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Unable to parse Delimiter");
			return null;
		}
	}

	public Integer getLength() {
		try {
			return Integer.parseInt(map.get(LENGTH_KEY));
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Unable to parse length");
			return -1;
		}
	}

	public Integer getIndex() {
		try {
			return Integer.parseInt(map.get(INDEX_KEY));
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Unable to parse index");
			return -1;
		}
	}

	public Integer getOffset() {
		try {
			return Integer.parseInt(map.get(OFFSET_KEY));
		} catch (Exception ex) {
			Logger.getLogger().error(ex, "Unable to parse offset");
			return -1;
		}
	}
	
	public Integer getMinPartSnLength() {
		try {
			return Integer.parseInt(map.get(MIN_PART_SN_LENGTH_KEY));
		} catch (Exception ex) {
			//OK to not define MIN_PART_SN_LENGTH
			return -1;
		}
	}
	
	public String getParseString() {
		return parseString;
	}

	public void setParseString(String parseString) {
		this.parseString = parseString;
	}
}
