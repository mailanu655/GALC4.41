package com.honda.galc.checkers;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public enum CheckerType {
	
	Application,
	Operation,
	Part,
	Measurement;
	
	public static CheckerType getCheckerType(String checkerType) {
		return CheckerType.valueOf(checkerType);
	}
}
