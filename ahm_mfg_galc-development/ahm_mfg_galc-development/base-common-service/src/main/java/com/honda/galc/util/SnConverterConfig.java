package com.honda.galc.util;

import java.util.HashMap;

import com.google.gson.Gson;
import com.honda.galc.util.SnConverter.ConversionType;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>SnConverterConfig</code> is ... .
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
 * @created Mar 31, 2014
 */
public class SnConverterConfig {
	private static Gson gson = new Gson();
	private String tokenName;
	private ConversionType conversionType;
	private Integer startIx;
	private Integer length;
	private String value;
	private HashMap<String, String> mappedValues;

	protected String getTokenName() {
		return tokenName;
	}

	protected void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	protected Integer getStartIx() {
		return startIx;
	}

	protected void setStartIx(Integer startIx) {
		this.startIx = startIx;
	}

	protected Integer getLength() {
		return length;
	}

	protected void setLength(Integer length) {
		this.length = length;
	}

	protected ConversionType getConversionType() {
		return conversionType;
	}

	protected void setConversionType(ConversionType conversionType) {
		this.conversionType = conversionType;
	}

	protected String getValue() {
		return value;
	}

	protected void setValue(String value) {
		this.value = value;
	}

	protected HashMap<String, String> getMappedValues() {
		return mappedValues;
	}

	protected String getMappedValue(String key) {
		if (getMappedValues() != null) {
			return getMappedValues().get(key);
		}
		return null;
	}

	protected void setMappedValues(HashMap<String, String> mappedValues) {
		this.mappedValues = mappedValues;
	}

	@Override
	public String toString() {
		return gson.toJson(this).replace("\"", "");
	}
}
