package com.honda.galc.client.device.plc;

import java.util.TreeMap;


/**
 * @author Subu Kathiresan
 * @date Dec 12, 2011
 */
public enum RegisterLoc implements IMemoryLoc {
	CIO(128), 
	LR(128), 
	HR(128), 
	AR(128), 
	TIMER(129), 
	COUNTER(129),
	DM(130), 
	E0(144), 
	E1(145), 
	E2(146), 
	E3(147), 
	E4(148), 
	E5(149),
	E6(150), 
	E7(151);

	private static TreeMap<Integer, RegisterLoc> _enumCodeMap = new TreeMap<Integer, RegisterLoc>();
	private int _code;
	
	static {
		for (RegisterLoc bank: RegisterLoc.values())	{
			_enumCodeMap.put(bank.code(), bank);
		}
	}

	RegisterLoc(int code) {
		_code = code;
	}

	public RegisterLoc getBank(int code) {
		return _enumCodeMap.get(code);
	}

	public int code() {
		return _code;
	}
}
