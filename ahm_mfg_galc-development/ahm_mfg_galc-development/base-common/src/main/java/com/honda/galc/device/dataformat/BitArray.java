/**
 * 
 */
package com.honda.galc.device.dataformat;

import java.io.Serializable;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Dec 3, 2012
 */
public class BitArray extends InputData implements Serializable {

	private static final long serialVersionUID = 2599967863337451158L;
	private StringBuilder _value;
	
	public BitArray() {
		super();
	}

	/**
	 * pass a string that represents the bit array, like "0100001001000011"
	 * @param strVal
	 */
	public BitArray(String strVal) {
		super();
		_value = parseBitArray(strVal);
	}
	
	public static StringBuilder parseBitArray(String binaryStr) {
		return StringUtil.bitArrayToChars(binaryStr);
	}

	public StringBuilder getValue() {
		return _value;
	}

	public void setValue(StringBuilder value) {
		_value = value;
	}
}

