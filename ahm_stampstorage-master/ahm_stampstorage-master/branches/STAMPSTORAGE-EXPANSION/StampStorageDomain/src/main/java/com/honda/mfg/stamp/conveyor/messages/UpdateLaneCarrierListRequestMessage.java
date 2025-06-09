package com.honda.mfg.stamp.conveyor.messages;

import java.util.List;

import com.honda.mfg.stamp.conveyor.domain.Stop;

public class UpdateLaneCarrierListRequestMessage implements ServiceRequestMessage {

	/**
	 * @param carrierNumberList
	 * @param laneStopId
	 * @param targetOp
	 */
	public UpdateLaneCarrierListRequestMessage(List<Integer> carrierNumberList, Long laneStopId,
			CarrierUpdateOperations targetOp) {
		super();
		setCarrierNumberList(carrierNumberList);
		setLaneStop(laneStopId);
		this.targetOp = targetOp;
	}

	public boolean isEmptyString(String s) {
		return (s == null || s.trim().isEmpty());
	}

	List<Integer> carrierNumberList = null;

	public List<Integer> getCarrierNumberList() {
		return carrierNumberList;
	}

	public void setCarrierNumberList(List<Integer> carrierNumberList) {
		this.carrierNumberList = carrierNumberList;
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

}
