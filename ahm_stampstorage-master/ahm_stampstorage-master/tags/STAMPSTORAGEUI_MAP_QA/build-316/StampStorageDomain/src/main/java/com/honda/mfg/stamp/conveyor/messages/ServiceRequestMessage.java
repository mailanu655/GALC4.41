/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

/**
 * @author VCC44349
 *
 */
public interface ServiceRequestMessage extends Message {

	CarrierUpdateOperations getTargetOp();

}
