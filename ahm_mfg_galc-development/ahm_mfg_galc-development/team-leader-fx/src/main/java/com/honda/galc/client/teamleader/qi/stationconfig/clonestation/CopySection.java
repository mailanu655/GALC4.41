package com.honda.galc.client.teamleader.qi.stationconfig.clonestation;


public enum CopySection {
	ENTRY_DEPT("Entry Depts"),
	WRITEUP_DEPT("Writeup Dept"),
	ENTRY_MODEL("Entry Models & Screens"),
	DEFECT_STATUS("Defect Status"),
	SETTINGS("Settings"),
	PREV_DEFECT_VISIBLE("Previous Defect Visible"),
	LIMIT_RESP("Limited Responsibility"),
	UPC("UPC")
	;
	private CopySection(String newDisplayText)  {
		displayText = newDisplayText;
	}
	String displayText = "";
	/**
	 * @return the displayText
	 */
	public String getDisplayText() {
		return displayText;
	}

}
