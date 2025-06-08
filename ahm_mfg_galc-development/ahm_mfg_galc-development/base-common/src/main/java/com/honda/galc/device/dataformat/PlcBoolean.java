package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.Tag;

/**
 * 
 * <h3>PlcBoolean</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PlcBoolean description </p>
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
 * @author Paul Chou
 * Mar 8, 2011
 * @author Subu Kathiresan
 * Nov 28, 2012
 */
public class PlcBoolean extends InputData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Tag(name="BIT")
	private boolean _value;
	@Tag(name="BIT", optional=true)
	private String _strValue;
	
	public PlcBoolean() {
		super();
	}

	public PlcBoolean(String strVal) {
		super();
		_strValue = strVal;
		_value = parseBoolean(strVal);
	}
	
	public static boolean parseBoolean(String s) {
		boolean retVal = false;
		if (s != null) {
			s = s.trim().toUpperCase();
			if (s.equals("O") 				// PLCs implementing FEC spec uses 'O' to represent 'true' 
					|| s.equals("1")) {		// Universal numeric code to represent 'true'
				retVal = true;
			}
		}
		return retVal;
	}
	
	public PlcBoolean(boolean value) {
		super();
		_value = value;
	}

	public boolean getValue() {
		return _value;
	}

	public void setValue(boolean value) {
		_value = value;
	}
	
	public String getStringValue() {
		return _strValue;
	}

	public void setStringValue(String strValue) {
		_strValue = strValue;
	}
	
	public void setStrValue(String strValue) {
		setStringValue(strValue);
	}
}
