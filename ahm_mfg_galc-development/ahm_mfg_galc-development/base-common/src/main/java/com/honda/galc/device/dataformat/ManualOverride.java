package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.device.Tag;

public class ManualOverride extends InputData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Tag(name="MANUAL_OVERRIDE")
	private boolean _value;
	
	public ManualOverride() {
		super();
	}

	public ManualOverride(String strVal) {
		super();
		_value = parseBoolean(strVal);
	}
	
	public static boolean parseBoolean(String s) {
		boolean retVal = false;
		if (s != null) {
			s = s.trim().toUpperCase();
			if (s.equals("O")) retVal = false;
			else if (s.equals("1"))	retVal = true;
		}
		return retVal;
	}
	
	public ManualOverride(boolean value) {
		super();
		_value = value;
	}

	public boolean getValue() {
		return _value;
	}

	public void setValue(boolean value) {
		_value = value;
	}
}
