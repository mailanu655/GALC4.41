package com.honda.galc.oif.dto;

import java.sql.Timestamp;

import com.honda.galc.util.OutputData;

public class ProductionProgressDTO implements IOutputFormat {

	private String productionLot;
	@OutputData("KD_LOT_NO")
	private String kdLotNumber;
	@OutputData("PROC_LOC")
	private String processLocation;
	@OutputData("LOT_SIZE")
	private int lotSize;
	private String ppId;	// send stripped to 7 last characters ppId
	@OutputData("PPID7")
	private String ppId7;
	@OutputData("PP_DESCRIPTION")
	private String ppIdDescription;
	@OutputData("UNITS_PASSED")
	private int unitsPassed;
	@OutputData("LINE_ON_TIMESTAMP")
	private Timestamp lineOnTimestamp;

	public ProductionProgressDTO() {
		super();
	}

	public int getLotSize() {
		return lotSize;
	}

	public void setLotSize(int lotSize) {
		this.lotSize = lotSize;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public String getPpIdDescription() {
		return ppIdDescription;
	}

	public void setPpIdDescription(String ppIdDescription) {
		this.ppIdDescription = ppIdDescription;
	}

	public int getUnitsPassed() {
		return unitsPassed;
	}

	public void setUnitsPassed(int unitsPassed) {
		this.unitsPassed = unitsPassed;
	}

	public void setLineOnTimestamp(Timestamp lineOnTimestamp) {
		this.lineOnTimestamp = lineOnTimestamp;
	}

	public Timestamp getLineOnTimestamp() {
		return lineOnTimestamp;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setPpId7(String ppId7) {
		this.ppId7 = ppId7;
	}

	public String getPpId7() {
		return ppId7;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("ProductionProgressDTO:");
		result.append("\nProduction Lot: " + productionLot);
		result.append("\nKD Lot: " + kdLotNumber);
		result.append("\nProcess Location: " + processLocation);
		result.append("\nLot Size: " + lotSize);
		result.append("\nProcess Point Id : " + ppId);
		result.append("\nppId Description: " + ppIdDescription);
		result.append("\nUnits Passed: " + unitsPassed);
		result.append("\nLineOn Timestamp: " + lineOnTimestamp);
		return result.toString();
	}

}