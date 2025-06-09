package com.honda.mfg.stamp.conveyor.messages;

/**
 * Created by IntelliJ IDEA. User: Ambica Gawarla Date: 9/10/13 Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class StorageStateResetRequestMessage implements ServiceRequestMessage {

	private CarrierUpdateOperations targetOp;

	public StorageStateResetRequestMessage(CarrierUpdateOperations targetOp) {
		super();
		this.targetOp = targetOp;
	}

	@Override
	public CarrierUpdateOperations getTargetOp() {
		return targetOp;
	}
}
