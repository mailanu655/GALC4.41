package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.Tag;

public class CommitReady extends InputData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Tag(name="COMMIT_READY")
	private boolean _value;
	@Tag(name="COMMIT_READY", optional=true)
	private String _strValue;
	
	public CommitReady() {
		super();
	}

	public CommitReady(String strVal) {
		super();
		_strValue = strVal;
		_value = parseBoolean(strVal);
	}
	
	public static boolean parseBoolean(String s) {
		boolean retVal = false;
		if (s != null) {
			s = s.trim().toUpperCase();
			if (s.equals("O") 				
					|| s.equals("1")) {	
				retVal = true;
			}
		}
		return retVal;
	}
	
	public CommitReady(boolean value) {
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