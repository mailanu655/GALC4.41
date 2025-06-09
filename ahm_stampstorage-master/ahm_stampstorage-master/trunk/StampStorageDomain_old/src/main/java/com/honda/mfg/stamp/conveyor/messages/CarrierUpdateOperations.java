package com.honda.mfg.stamp.conveyor.messages;

/**
 * Enum representing CarrierManagementService operations.
 * For example:
 * <pre>
 *    CarrierUpdateOperations targetOp = CarrierUpdateOperation.SAVE
 * </pre>
 *
 * @author  Vivek Bettada
 * @see     com.honda.mfg.stamp.conveyor.messages.CarrierUpdateRequestMessage
 */
public enum CarrierUpdateOperations {

	SAVE,
	REWORK_UPDATE,
	RECALC_DEST,
	STORE_REWORK,
	REMOVE_FROM_ROW,
	RELEASE_EMPTIES_FROM_ROW,
	RELEASE_CARRIERS,
	GROUP_HOLD,
	ROW_RESEQUENCE,  //saveCarriersInToRow, addCarrierToRow
	ADD_TO_ROW,		 // ROW_RESEQUENCE
	REORDER_CARRIERS_IN_ROW,
    REFRESH_STORAGE_STATE,
    UPDATE_ROW;
}
