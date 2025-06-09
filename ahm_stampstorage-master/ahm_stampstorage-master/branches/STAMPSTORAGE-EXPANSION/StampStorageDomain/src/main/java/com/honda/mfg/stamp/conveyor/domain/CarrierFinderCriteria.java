package com.honda.mfg.stamp.conveyor.domain;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

/**
 * User: VCC30690 Date: 10/11/11
 */
public class CarrierFinderCriteria {

	private Integer carrierNumber;
	private Die die;
	private Stop currentLocation;
	private Stop destination;
	private CarrierStatus carrierStatus;
	private Press press;
	private Integer productionRunNo;
	private BitInfo bitInfo;

	public CarrierFinderCriteria() {
		bitInfo = new BitInfo();
	}

	public Integer getCarrierNumber() {
		return carrierNumber;
	}

	public void setCarrierNumber(Integer carrierNumber) {
		this.carrierNumber = carrierNumber;
	}

	public Die getDie() {
		return die;
	}

	public void setDie(Die die) {
		this.die = die;
	}

	public Stop getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Stop currentLocation) {
		this.currentLocation = currentLocation;
	}

	public Stop getDestination() {
		return destination;
	}

	public void setDestination(Stop destination) {
		this.destination = destination;
	}

	public CarrierStatus getCarrierStatus() {
		return carrierStatus;
	}

	public void setCarrierStatus(CarrierStatus carrierStatus) {
		this.carrierStatus = carrierStatus;
	}

	public Press getPress() {
		return press;
	}

	public void setPress(Press press) {
		this.press = press;
	}

	public Integer getProductionRunNo() {
		return productionRunNo;
	}

	public void setProductionRunNo(Integer productionRunNo) {
		this.productionRunNo = productionRunNo;
	}

	public BitInfo getBitInfo() {
		return bitInfo;
	}

	public void setBitInfo(BitInfo bitInfo) {
		this.bitInfo = bitInfo;
	}
}
