package com.honda.galc.service;

public enum CheckerBooleanFalseAsGood {
	INVALID_PREVIOUS_LINE_CHECK("VALID_PREVIOUS_LINE_CHECK"),
	PRODUCT_SCRAPPED_CHECK("PRODUCT_NOT_SCRAPPED_CHECK"),
	CHECK_PMQA_RESULTS("VALID_CHECK_PMQA_RESULTS"),
	QI_NON_REPAIRABLE_DEFECT_CHECK("QI_NON_REPAIRABLE_DEFECT_CHECK_PASSED"),
	INCOMPLETE_ENGINE_DATA_CHECK("ENGINE_DATA_CHECK"),
	DUPLICATE_ENGINE_ASSIGNMENT_CHECK("ENGINE_ASSIGNMENT_CHECK"),
	KICKOUT_EXIST_CHECK("KICKOUT_NOT_EXIST_CHECK");
	
	
	private String type;
	
	private CheckerBooleanFalseAsGood(String type) {
		this.type = type;
	}
	
    public static boolean hasChecker(String type) {
        for (CheckerBooleanFalseAsGood checkerType : CheckerBooleanFalseAsGood.values()) {
            if (checkerType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
    public static CheckerBooleanFalseAsGood getChecker(String type) {
        for (CheckerBooleanFalseAsGood checkerType : CheckerBooleanFalseAsGood.values()) {
            if (checkerType.name().equals(type)) {
                return checkerType;
            }
        }
        return null;
    }

	public String getType() {
		return type;
	}
}
