package com.honda.mfg.stamp.conveyor.domain.enums;

/**
 * User: vcc30690 Date: 7/7/11
 */
public enum LaneCondition {
	/** Mixed but die requested is in the front */
	MIXED_FRONT,
	/** Mixed but die requested is in the back used for storein */
	MIXED_BACK,

	PARTIAL, FULL,
	/** Die requested is blocked by another die for storing out */
	MIXED_BLOCK, VACANT,
	/** Generally mixed row */
	MIXED, PARTIAL_DIFFERENT_DIE, EMPTY
}
