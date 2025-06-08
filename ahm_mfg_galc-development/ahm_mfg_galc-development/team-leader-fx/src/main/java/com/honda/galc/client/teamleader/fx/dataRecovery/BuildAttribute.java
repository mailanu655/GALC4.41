package com.honda.galc.client.teamleader.fx.dataRecovery;

import com.honda.galc.entity.product.ProductBuildResult;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>BuildAttribute</code> is ...
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
 * <TD>L&T Infotech</TD>
 * <TD>Jul 17, 2017</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class BuildAttribute {

	public static String DELIMITER = "[|]";
	private String name;
	private String label;
	private int length;
	private String type;
	private String valueType;
	private int tokenCount;
	private String delimiter;

	public BuildAttribute(String name, String label, int length, String type) {
		this.name = name;
		this.label = label;
		this.length = length;
		this.type = type;
	}

	public BuildAttribute(String name, String label, int length, String type, String valueType) {
		this.name = name;
		this.label = label;
		this.length = length;
		this.type = type;
		this.valueType = valueType;
	}

	public BuildAttribute(String name, String label, int length, String type, String valueType, int tokenCount, String delimiter) {
		this.name = name;
		this.label = label;
		this.length = length;
		this.type = type;
		this.valueType = valueType;
		this.tokenCount = tokenCount;
		this.delimiter = delimiter;
	}

	// === get/set ===/
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getName() {
		return name;
	}

	public void setName(String partName) {
		this.name = partName;
	}

	protected String getType() {
		return type;
	}

	protected String getValueType() {
		return valueType;
	}

	public int getTokenCount() {
		return tokenCount;
	}

	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getValue(ProductBuildResult result) {
		if (result == null) {
			return "";
		}
		return result.getResultValue();
	}

	public String getStatusValue(ProductBuildResult result) {

		if (result == null) {
			return "";
		}

		return result.isStatusOk()?"OK":"Failed";

	}

	public boolean isStatusOnly() {
		return safeEquals(getType(), "status");
	}

	public boolean isValueOnly() {
		return safeEquals(getType(), "value");
	}

	public boolean isValueDecimal() {
		return safeEquals(getValueType(), "decimal");
	}

	public boolean isValueNumeric() {
		return safeEquals(getValueType(), "numeric");
	}

	// === utility api === //
	protected boolean safeEquals(String str1, String str2) {
		str1 = str1 == null ? "" : str1.trim();
		str2 = str2 == null ? "" : str2.trim();
		return str1.equals(str2);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!(o instanceof BuildAttribute)) {
			return false;
		}
		BuildAttribute attribute = (BuildAttribute) o;
		return getName().equals(attribute.getName());
	}

	@Override
	public int hashCode() {
		if (getName() != null) {
			return getName().hashCode();
		}
		return super.hashCode();
	}

	public String getDefaultValue(){
		String elementValue = "";
		if(isValueDecimal()) 
			elementValue = "0.0";
		else if(isValueNumeric())
			elementValue = "0";

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getTokenCount(); i++) {
			if (builder.length() > 0) {
				builder.append(getDelimiter());
			}
			builder.append(elementValue);
		}
		return builder.toString();
	}
}
