package com.honda.galc.checkers;

/**
 * @author Subu Kathiresan
 * @date Sep 26, 2014
 */
public enum CheckPoints {
	
	BEFORE_PRODUCT_SCAN,
	AFTER_PRODUCT_SCAN,
	
	BEFORE_OPERATION,
	AFTER_OPERATION,
	
	BEFORE_PART_SCAN,
	AFTER_PART_SCAN,
	
	BEFORE_MEASUREMENT_INPUT,
	AFTER_MEASUREMENT_INPUT,
	
	BEFORE_PRODUCT_TRACKING,
	AFTER_PRODUCT_TRACKING,
	
	BEFORE_PRODUCT_PERSISTENCE,
	AFTER_PRODUCT_PERSISTENCE,
	
	BEFORE_STRUCTURE_CREATE,
	AFTER_STRUCTURE_CREATE,
	
	AFTER_SET_NEXT_UNIT,
	
	BEFORE_PRODUCT_PROCESSED,
	AFTER_PRODUCT_PROCESSED,
	AFTER_PART_SN_TRASHED;
	
	private CheckPoints() {}

	public static boolean isValid(String checkPointName) {
		try {
			if (CheckPoints.valueOf(checkPointName) != null) {
				return true;
			}
		} catch (Exception ex) {}
		return false;
	}
}
