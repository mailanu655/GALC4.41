package com.honda.galc.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.constant.Delimiter;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>FlfFieldConfig</code> is Fixed Length File item info.
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
public class FlfFieldConfig {

	private String propertyName;
	private int startIx;
	private int length;
	private Object defaultValue;

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public int getStartIx() {
		return startIx;
	}

	public void setStartIx(int startIx) {
		this.startIx = startIx;
	}

	public int getLength() {
		return length;
	}

	public int getEndIx() {
		return getStartIx() + getLength();
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	// === factory === //
	public static List<FlfFieldConfig> parseFieldConfigs(Map<String, String> parseDefProperty) {
		List<FlfFieldConfig> configs = new ArrayList<FlfFieldConfig>();
		if (parseDefProperty == null) {
			return configs;
		}
		for (String propertyName : parseDefProperty.keySet()) {
			configs.add(create(propertyName, parseDefProperty.get(propertyName)));
		}
		return configs;
	}

	public static FlfFieldConfig create(String propertyName, String csValues) {
		FlfFieldConfig config = new FlfFieldConfig();
		config.setPropertyName(propertyName);
		if (StringUtils.isNotBlank(csValues)) {
			String[] ar = csValues.split(Delimiter.COMMA);
			config.setStartIx(Integer.parseInt(ar[0]));
			config.setLength(Integer.parseInt(ar[1]));
			if (ar.length > 2) {
				config.setDefaultValue(ar[2]);
			}
		}
		return config;
	}
}
