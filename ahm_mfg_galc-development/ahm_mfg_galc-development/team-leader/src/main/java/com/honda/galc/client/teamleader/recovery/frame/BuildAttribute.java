package com.honda.galc.client.teamleader.recovery.frame;

import com.honda.galc.entity.product.ProductBuildResult;


/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jul 17, 2008</TD>
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
public class BuildAttribute {

	public static String DELIMITER = "[|]";

	public static final BuildAttribute MC_BLOCK_LEAK_TEST = new BuildAttribute("MC BLOCK LEAK TEST", "Leak Test Status", 30);
	public static final BuildAttribute WATER_JACKET = new BuildAttribute("WATER JACKET", "Water Jacket");
	public static final BuildAttribute INTERNAL_OIL_HOLE = new BuildAttribute("INTERNAL OIL HOLE", "Internal Oil Hole");
	public static final BuildAttribute CRANK_ROOM = new BuildAttribute("CRANK ROOM", "Crank Room");
	public static final BuildAttribute HP_OILWAY = new BuildAttribute("HP OILWAY", "HP Oilway");
	public static final BuildAttribute CRANK_JOURNAL_MEASURE = new BuildAttribute("CRANK JOURNAL MEASURE", "Crank Journal Status", 10, "status") {
		public String getValue(ProductBuildResult result) {
			return getStatusValue(result);
		}
	};
	public static final BuildAttribute CRANK_JOURNAL_1 = new BuildAttribute("CRANK JOURNAL 1", "CRANK JOURNAL 1", 2, "value");
	public static final BuildAttribute CRANK_JOURNAL_2 = new BuildAttribute("CRANK JOURNAL 2", "CRANK JOURNAL 2", 2, "value");
	public static final BuildAttribute CRANK_JOURNAL_3 = new BuildAttribute("CRANK JOURNAL 3", "CRANK JOURNAL 3", 2, "value");
	public static final BuildAttribute CRANK_JOURNAL_4 = new BuildAttribute("CRANK JOURNAL 4", "CRANK JOURNAL 4", 2, "value");
	public static final BuildAttribute CRANK_JOURNAL_5 = new BuildAttribute("CRANK JOURNAL 5", "CRANK JOURNAL 5", 2, "value");

	public static final BuildAttribute LOWER_BLOCK_NUMBER = new BuildAttribute("LOWER BLOCK NUMBER", "Lower Block SN", 30, "value");
	public static final BuildAttribute SOLUTION_COUNT = new BuildAttribute("SOLUTION_COUNT", "Solution", 30, "value");
	public static final BuildAttribute AGE_FURNACE_COUNT = new BuildAttribute("AGE_FURNACE_COUNT", "Age", 30, "value");
	public static final BuildAttribute HARDNESS = new BuildAttribute("HARDNESS", "Hardness Results", 14, "both", "decimal", 2, ",") {
		public String getValue(ProductBuildResult result) {
			return getStatusValue(result);
		}
	};
	public static final BuildAttribute MC_HEAD_LEAK_TEST = new BuildAttribute("MC HEAD LEAK TEST", "Leak Test Status", 30, "status") {
		public String getValue(ProductBuildResult result) {
			return getStatusValue(result);
		}
	};
	public static final BuildAttribute COMBUSTION_CHAMBER_VTEC = new BuildAttribute("COMBUST CHMBR VTEC", "Com Chmbr VTec");
	public static final BuildAttribute OIL_GALLERY = new BuildAttribute("OIL GALLERY", "Oil Gallery");
	public static final BuildAttribute OIL_HOLE_EGR = new BuildAttribute("OIL HOLE EGR", "Oil Hole EGR");
	public static final BuildAttribute CAM_JOURNAL_MEASURE = new BuildAttribute("CAM JOURNAL MEASURE", "Cam Journal Status", 5, "status") {
		public String getValue(ProductBuildResult result) {
			return getStatusValue(result);
		}
	};

	private String name;
	private String label;
	private int length;
	private String type;
	private String valueType;
	private int tokenCount;
	private String delimiter;

	private BuildAttribute(String partName, String label) {
		this.name = partName;
		this.label = label;
		this.length = 30;
	}

	private BuildAttribute(String partName, String label, int length) {
		this.name = partName;
		this.label = label;
		this.length = length;
	}

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
