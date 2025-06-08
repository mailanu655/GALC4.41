package com.honda.galc.service;

public enum SpecialCheker {
	CHECK_ENGINE_TYPE_FOR_ENGINE_ASSIGNMENT,
	PASSED_PROCESS_POINT_CHECK;

	static boolean has(String checkType) {
		for(SpecialCheker checker : values()) {
			if(checkType.equals(checker.name()))
				return true;
		}
		return false;
	}
}
