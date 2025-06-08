package com.honda.galc.client.ui;

public class MassMessageFor{
	
	/** * * 
	* @author Fredrick Yessaian 
	* @since Sep 03, 2014
	*/
	
	private static final long serialVersionUID = 1L;
	
	static final String NONE = "NONE";
	
	String plantName = NONE;
	String divisionId = NONE;
	String lineId = NONE;
	
	public MassMessageFor(String plant, String division, String line) {
		setPlantName(plant);
		setDivisionId(division);
		setLineId(line);
	}
	
	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}


}
