package com.honda.mfg.stamp.conveyor.domain;

import java.sql.Timestamp;

import com.honda.mfg.stamp.conveyor.domain.enums.CarrierStatus;
import com.honda.mfg.stamp.conveyor.domain.enums.Press;

/**
 * User: VCC30690 Date: 10/11/11
 */
public class GroupHoldFinderCriteria {

	private Integer productionRunNumber;
	private Timestamp productionRunDate;
	private StorageRow row;
	private Integer numberAfterRunDate;
	private Integer numberBeforeRunDate;
	private CarrierStatus status;
	private Press robot;
	private Boolean rowAndProdRun;

	public Integer getProductionRunNumber() {
		return productionRunNumber;
	}

	public void setProductionRunNumber(Integer productionRunNumber) {
		this.productionRunNumber = productionRunNumber;
	}

	public Timestamp getProductionRunDate() {
		return productionRunDate;
	}

	public void setProductionRunDate(Timestamp productionRunDate) {
		this.productionRunDate = productionRunDate;
	}

	public StorageRow getRow() {
		return row;
	}

	public void setRow(StorageRow row) {
		this.row = row;
	}

	public Integer getNumberAfterRunDate() {
		return numberAfterRunDate;
	}

	public void setNumberAfterRunDate(Integer numberAfterRunDate) {
		this.numberAfterRunDate = numberAfterRunDate;
	}

	public Integer getNumberBeforeRunDate() {
		return numberBeforeRunDate;
	}

	public void setNumberBeforeRunDate(Integer numberBeforeRunDate) {
		this.numberBeforeRunDate = numberBeforeRunDate;
	}

	public CarrierStatus getStatus() {
		return status;
	}

	public void setStatus(CarrierStatus status) {
		this.status = status;
	}

	public Press getRobot() {
		return robot;
	}

	public void setRobot(Press robot) {
		this.robot = robot;
	}

	public Boolean getRowAndProdRun() {
		return rowAndProdRun;
	}

	public void setRowAndProdRun(Boolean rowAndProdRun) {
		this.rowAndProdRun = rowAndProdRun;
	}
}
