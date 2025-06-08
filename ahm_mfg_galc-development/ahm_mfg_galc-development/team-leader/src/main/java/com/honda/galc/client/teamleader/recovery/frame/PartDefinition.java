package com.honda.galc.client.teamleader.recovery.frame;

/**
 * 
 * <h3>PartDefinition Class description</h3>
 * <p>
 * PartDefinition description
 * </p>
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
 * 
 * </TABLE>
 * 
 * @author Jeffray Huang<br>
 *         Dec 16, 2011
 * 
 * 
 */
public class PartDefinition {

	public enum ValueType {
		STRING, INTEGER, FLOAT
	}
	
	public enum ElementType {STATUS, VALUE, STATUS_VALUE}

	// === part properties === //
	private String name;
	private String label;
	private int length;
	private ValueType valueType;
	private int tokenCount;
	private String delimiter;
	private ElementType elementType;
	private boolean editable;
	private boolean unique; 
	// === validation === //
	private String[] values;
	private String valuePattern;
	
	// === subParts === //
	private PartDefinition[] multiParts;
	private int rowCount;

	public PartDefinition() {
		this.length = 30;
		this.elementType = ElementType.VALUE;
		this.valueType = ValueType.STRING;
		this.tokenCount = 1;
		this.delimiter = ",";
		this.editable = true;
		this.unique = false;
		this.rowCount = 1;
	}


	public PartDefinition(String name, String label, int length, ValueType valueType, ElementType elementType, int tokenCount, String delimiter, boolean editable, int rowCount, PartDefinition... multiParts) {
		this();
		this.name = name;
		this.label = label;
		this.length = length;
		this.elementType = elementType;
		this.valueType = valueType;
		this.tokenCount = tokenCount;
		this.delimiter = delimiter;
		this.editable = editable;
		this.rowCount = rowCount;
		this.multiParts = multiParts;
	}	
	
	public PartDefinition(String name, String label, boolean editable) {
		this();
		this.name = name;
		this.label = label;
		this.editable = editable;
	}	
	
	public PartDefinition(String name, String label, boolean editable, boolean unique) {
		this();
		this.name = name;
		this.label = label;
		this.editable = editable;
		this.unique = unique;
	}	

	public PartDefinition(String name, String label, ElementType elementType, boolean editable) {
		this(name, label, 30, ValueType.STRING,elementType,  editable);
	}
	
	public PartDefinition(String name, int length, boolean editable) {
		this(name, null, length, ValueType.STRING, ElementType.VALUE, editable);
	}

	public PartDefinition(String name, String label, int length, ValueType valueType, boolean editable) {
		this(name, label, length, valueType, ElementType.VALUE, editable);
	}
	
	public PartDefinition(String name, String label, int length, ValueType valueType, ElementType elementType, boolean editable) {
		this(name, label, length, valueType, elementType, 1, ",", editable, 1);
	}

	public PartDefinition(String name, String label, int length, ValueType valueType, ElementType elementType, int tokenCount, String delimiter, boolean editable) {
		this( name, label, length, valueType, elementType, tokenCount, delimiter, editable, 1);
	}

	/**
	 * Multi Parts only
	 * @param label
	 * @param rowCount
	 * @param multiParts
	 */
	public PartDefinition(String label, int rowCount, PartDefinition... multiParts) {
		this(null, label, 1, ValueType.STRING, ElementType.STATUS, 1, ",", false, rowCount, multiParts);
	}

	/**
	 * Status and Multi Parts
	 * @param name
	 * @param label
	 * @param editable
	 * @param rowCount
	 * @param multiParts
	 */
	public PartDefinition(String name, String label, boolean editable, int rowCount, PartDefinition... multiParts) {
		this(name, label, 1, ValueType.STRING, ElementType.STATUS, 1, ",", editable, rowCount, multiParts);
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

	protected ValueType getValueType() {
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

	public boolean isStatusOnly() {
		return ElementType.STATUS.equals(getElementType());
	}

	public boolean isStatus() {
		return ElementType.STATUS.equals(getElementType()) || ElementType.STATUS_VALUE.equals(getElementType());
	}

	public boolean isValue() {
		return ElementType.VALUE.equals(getElementType()) || ElementType.STATUS_VALUE.equals(getElementType());
	}

	public boolean isValueOnly() {
		return ElementType.VALUE.equals(getElementType());
	}

	public boolean isValueDecimal() {
		return valueType == ValueType.FLOAT;
	}

	public boolean isValueNumeric() {
		return valueType == ValueType.INTEGER;
	}

	public int getElementCount() {
		return tokenCount <= 0 ? 1 : tokenCount;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public PartDefinition[] getMultiParts() {
		return multiParts;
	}

	public void setMultiParts(PartDefinition[] multiParts) {
		this.multiParts = multiParts;
	}
	
	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isMultiPartEditable() {
		if (getMultiParts() == null || getMultiParts().length == 0) {
			return false;
		}
		
		for (PartDefinition pd : getMultiParts()) {
			if (pd != null && pd.isEditable()) {
				return true;
			}
		}
		return false;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public void setTokenCount(int tokenCount) {
		this.tokenCount = tokenCount;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}	
	
	public ElementType getElementType() {
		if (elementType == null) {
			elementType = ElementType.VALUE;
		}
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}	

	public String getValuePattern() {
		return valuePattern;
	}

	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}
	
	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
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
		if (!(o instanceof PartDefinition)) {
			return false;
		}
		PartDefinition attribute = (PartDefinition) o;
		return getName().equals(attribute.getName());
	}

	@Override
	public int hashCode() {
		if (getName() != null) {
			return getName().hashCode();
		}
		return super.hashCode();
	}

	public String getDefaultValue() {
		String elementValue = "";
		if (isValueDecimal())
			elementValue = "0.0";
		else if (isValueNumeric())
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
