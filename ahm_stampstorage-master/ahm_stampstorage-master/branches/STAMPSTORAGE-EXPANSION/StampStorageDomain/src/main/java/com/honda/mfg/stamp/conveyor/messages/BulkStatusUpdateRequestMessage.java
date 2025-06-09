/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;

/**
 * @author VCC44349
 *
 */
public class BulkStatusUpdateRequestMessage extends ServiceRequest implements ServiceRequestMessage {

	/**
	 * @param targetOp
	 * @param user
	 * @param carrierNumberList
	 * @param newStatus
	 */
	public BulkStatusUpdateRequestMessage(List<Integer> carrierNumberList, CarrierStatus newStatus, String user,
			CarrierUpdateOperations targetOp) {
		super(targetOp, user);
		this.carrierNumberList = carrierNumberList;
		this.newStatus = newStatus;
	}

	private List<Integer> carrierNumberList;

	public List<Integer> getCarrierNumberList() {
		return carrierNumberList;
	}

	public void setCarrierNumberList(List<Integer> carrierNumberList) {
		this.carrierNumberList = carrierNumberList;
	}

	private CarrierStatus newStatus;

	public CarrierStatus getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(CarrierStatus newStatus) {
		this.newStatus = newStatus;
	}

}
