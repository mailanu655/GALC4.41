/**
 * 
 */
package com.honda.galc.client.device.plc;

import java.util.TreeMap;


/**
 * @author Subu Kathiresan
 * @date Feb 13, 2012
 */
public enum BitLoc implements IMemoryLoc {
	DM(2),
	E0(32),
	E1(33),
	E2(34),
	E3(35),
	E4(36),
	E5(37),
	E6(38),
	E7(39);

	private static TreeMap<Integer, BitLoc> _enumCodeMap = new TreeMap<Integer, BitLoc>();
	private int _code;
	
	static {
		for (BitLoc bank: BitLoc.values())	{
			_enumCodeMap.put(bank.code(), bank);
		}
	}
	
	BitLoc(int code) {
		_code = code;
	}
	
	public BitLoc getBank(String key) {
		if (key == null || key.trim().length() < 1)
			return null;

		key = key.trim().toUpperCase();
		return Enum.valueOf(BitLoc.class, key);
	}

	public BitLoc getBank(int code) {
		return _enumCodeMap.get(code);
	}

	public int code() {
		return _code;
	}
}

