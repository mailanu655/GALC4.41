/**
 * 
 */
package com.honda.mfg.stamp.conveyor.messages;

import com.honda.mfg.stamp.conveyor.domain.Stop;

/**
 * @author VCC44349
 *
 */
public class LaneUpdateRequestMessage implements ServiceRequestMessage {

	/**
	 * @param targetOp
	 * @param carrierNumber
	 */
	public LaneUpdateRequestMessage(Integer carrierNumber, CarrierUpdateOperations targetOp) {
		super();
		this.targetOp = targetOp;
		setCarrierNumber(carrierNumber);
	}

	/**
	 * @param targetOp
	 * @param carrierNumber
	 * @param position
	 * @param laneStop
	 */
	public LaneUpdateRequestMessage(Integer carrierNumber, Integer position, Long laneStop,
			CarrierUpdateOperations targetOp) {
		super();
		this.targetOp = targetOp;
		setCarrierNumber(carrierNumber);
		setPosition(position);
		setLaneStop(laneStop);
	}

	public boolean isEmptyString(String s) {
		return (s == null || s.trim().isEmpty());
	}

	/**
	 * The target operation in CarrierManagementService to be invoked to process
	 * carrier update
	 *
	 */
	private CarrierUpdateOperations targetOp;

	public CarrierUpdateOperations getTargetOp() {
		return targetOp;
	}

	public void setTargetOp(CarrierUpdateOperations targetOp) {
		this.targetOp = targetOp;
	}

	private String carrierNumber = "";

	public Integer getCarrierNumber() {
		Integer thisCarrierNum = null;
		if (!isEmptyString(carrierNumber)) {
			thisCarrierNum = (Integer.valueOf(carrierNumber));
		}
		return thisCarrierNum;
	}

	public void setCarrierNumber(Integer newCarrierNumber) {
		if (newCarrierNumber != null) {
			this.carrierNumber = String.valueOf(newCarrierNumber);
		}
	}

	private String position = "";

	public Integer getPosition() {
		Integer thisPostion = null;
		if (!isEmptyString(position)) {
			thisPostion = (Integer.valueOf(position));
		}
		return thisPostion;
	}

	public void setPosition(Integer thisPosition) {
		if (position != null) {
			this.position = String.valueOf(thisPosition);
		}
	}

	private String laneStop = "";

	public Stop getLaneStop() {
		Stop thisStop = null;
		if (!isEmptyString(laneStop)) {
			thisStop = Stop.findStop(Long.valueOf(laneStop));
		}
		return thisStop;
	}

	public Long getLaneStopId() {
		Long laneStopId = null;
		if (!isEmptyString(laneStop)) {
			laneStopId = Long.valueOf(laneStop);
		}
		return laneStopId;
	}

	public void setLaneStop(Long laneStopId) {
		if (laneStopId != null) {
			this.laneStop = String.valueOf(laneStopId);
		}
	}

}
