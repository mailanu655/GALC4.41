package com.honda.galc.oif.dto;

import java.util.Map;

import com.honda.galc.util.GPCSData;

/**
 * 
 * <h3>NSEDataDTO</h3>
 * <p> NSEDataDTO holds a single record of data from fixed length NSE interface files after parsing. </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Ratul Chakravarty<br>
 * May 13, 2014
 *
 */


public class NSEDataDTO {
	
	
	@GPCSData("EIN")
	private String ein;
	
	@GPCSData("KD_LOT_NUMBER")
	private String kdLotNumber;
	
	@GPCSData("LINE_NUMBER")
	private int lineNumber;
	
	@GPCSData("MODEL")
	private String model;
	
	@GPCSData("OPTION")
	private String option;
	
	@GPCSData("RACK_ID")
	private String rackId;
	
	@GPCSData("TRAILER_ID")
	private String trailerId;
	
	@GPCSData("TIMESTAMP")
	private String timestamp;
	
	@GPCSData("TYPE")
	private String type;
	
	public NSEDataDTO() {
	}
	
	public NSEDataDTO(Map<String, String> map) {
		lineNumber = Integer.parseInt(map.get("LINE_NUMBER"));
		kdLotNumber = map.get("KD_LOT_NUMBER");
		model = map.get("MODEL");
		type = map.get("TYPE");
		option = map.get("OPTION");
		rackId = map.get("RACKID");
		ein = map.get("EIN");
		trailerId = map.get("TRAILERID");
		timestamp = map.get("TIMESTAMP");
	}
	
	// copy constructor
	public NSEDataDTO(NSEDataDTO dataDTO) {
		this.ein = dataDTO.getEin();
		this.kdLotNumber = dataDTO.getKdLotNumber();
		this.lineNumber = dataDTO.getLineNumber();
		this.model = dataDTO.getModel();
		this.option = dataDTO.getModel();
		this.rackId = dataDTO.getRackId();
		this.timestamp = dataDTO.getTimestamp();
		this.type = dataDTO.getType();
	}

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public String getKdLotNumber() {
		return kdLotNumber;
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTrailerId(String trailerId) {
		this.trailerId = trailerId;
	}

	public String getTrailerId() {
		return trailerId;
	}

}
