package com.honda.galc.service;

public enum CheckerStateCheckStyle {
	INSTALLED_PARTS_STATUS_CHECK;
	
    public static boolean hasChecker(String type) {
        for (CheckerStateCheckStyle checkerType : CheckerStateCheckStyle.values()) {
            if (checkerType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
