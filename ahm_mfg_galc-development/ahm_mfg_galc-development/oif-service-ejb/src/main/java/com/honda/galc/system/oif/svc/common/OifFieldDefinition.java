package com.honda.galc.system.oif.svc.common;

import java.sql.Types;


/**
 * <h3>OifFieldDefinition</h3>
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * Field defintion value object
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update Date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>R.Lasenko</TD>
 * <TD>Apr 10, 2008</TD>
 * <TD>&nbsp;</TD>
 * <TD>@???</TD>
 * <TD>Initial Version</TD>
 * </TR>
 * </TABLE>
 */
public class OifFieldDefinition {
	
	private static final int POSITION_UNDEF = 0;
	private static final String SEPARATOR = ",";
	private static final String REGEX_SEP = "\\s*\\" + SEPARATOR + "\\s*";
	
	private String fieldName = null;
	private boolean keyField = false;
	private FieldType fieldType = FieldType.NONE;
	private String fieldJustified = null;
	private String fieldFormat = null;
	private int position = POSITION_UNDEF;
	private int fieldLength = -1;

	public enum FieldType {
		C("String", Types.NULL), I("Integer", Types.INTEGER), E("Date", Types.DATE), F("Float", Types.FLOAT),
		D("Double", Types.DOUBLE), U("Boolean", Types.BOOLEAN), T("TimeStamp", Types.TIMESTAMP), 
		M("Time", Types.TIME), NONE("Not defined", Types.NULL);
		
		private String longName;
		private int sqlType;
		
		FieldType(String aLongName, int aSqlType) {
			this.longName = aLongName;
			this.sqlType = aSqlType;
		}
		
		/**
		 * Getter to SQL type
		 * 
		 * @return the SQL type
		 */
		public int getSqlType() {
			return sqlType;
		}
		
		/**
		 * Pretty printer
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return new StringBuilder(this.name())
				.append("(").append(this.longName).append(")").toString();
		}

	}
	
	/**
	 * Do not use consturctor - use factory methods
	 */
	private OifFieldDefinition() {
	}

	/**
	 * @return the fieldFormat
	 */
	public String getFieldFormat() {
		return fieldFormat;
	}

	/**
	 * @return the fieldJustified
	 */
	public String getFieldJustified() {
		return fieldJustified;
	}

	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the fieldType
	 */
	public FieldType getFieldType() {
		return fieldType;
	}

	/**
	 * @return the keyField
	 */
	public boolean isKeyField() {
		return keyField;
	}

	/**
	 * @return the startPosition
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Factory method to create instances of OifFieldDefinition
	 * 
	 * @param columnName
	 * @param definition - is a String with comma separated tokens:<ul>
	 * <li>Key flag:<ul>
	 * 		<li>K - key - will be used in 'where' clause
	 *      <li>F - field - just a filed value
	 *      </ul></li>
	 * <li>Field type - FieldType</li>
	 * <li>Field length</li>
	 * <li>Field position - 0-based</li>
	 * </ul>
	 * @return OifFieldDefinition or <b>null</b> if it cannot be obtained
	 * 
	 */
	public static OifFieldDefinition parseFormat(String columnName, String definition) {

		String[] defFields = definition.split(REGEX_SEP);
		
		if(defFields.length < 2) {
			return null;
		}
		OifFieldDefinition fieldDefinition = new OifFieldDefinition();
		
		fieldDefinition.fieldName = columnName;
		
		fieldDefinition.keyField = defFields[0].equalsIgnoreCase("k");
		try {
			fieldDefinition.fieldType = FieldType.valueOf(defFields[1]);
		} catch (IllegalArgumentException e) {
			fieldDefinition.fieldType = FieldType.NONE;
		}
		
		if (defFields.length > 2) {
			try {
				fieldDefinition.fieldLength = Integer.parseInt(defFields[2]);
			} catch (IllegalArgumentException e) {
				// do nothing - it's already initialized
			}			
			if (defFields.length > 3) {
				final String posProperty = defFields[3];
				// Check weather we want to use default position
				if (!posProperty.equals("*")) {
					fieldDefinition.position = Integer.parseInt(posProperty);
				}
				if (defFields.length > 4) {
					fieldDefinition.fieldJustified = defFields[4];
					if (defFields.length > 5) {
						fieldDefinition.fieldFormat = defFields[5];
					}
				}
			}
		}		
		
		return fieldDefinition;
		
	}

	/**
	 * Pretty printer
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(":[");
		sb.append(this.fieldName).append(",");
		sb.append(this.keyField ? "K" : "C").append(",");
		sb.append(this.fieldType).append(",");
		sb.append(this.position).append(",");
		sb.append(this.fieldLength);
		return sb.append("]").toString();
	}

	/**
	 * Checks if the parameter subject to trimming
	 * 
	 * @param param - parameter 
	 * @return <code>true</code> - if requires trimming<br>
	 * <code>false</code> - otherwise
	 */
	public boolean isStringTooLong(Object param) {		
		return 
			this.fieldType == FieldType.C
			&& param != null
			&& this.fieldLength > 0
			&& param.toString().length() > this.fieldLength;
	}

	/**
	 * Position setter
	 * 
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Checks wether position was defined
	 * 
	 * @return <code>true</code> - if position was defined<br>
	 * <code>false</code> - otherwise
	 */
	public boolean isPositionDefined() {
		return this.position != POSITION_UNDEF;
	}

}
