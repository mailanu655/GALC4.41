package com.honda.galc.client.teamleader.qi.stationconfig;

import com.honda.galc.enumtype.IdEnum;

/**
 * 
 * <h3>QiEntryStationConfigurationSettings Class description</h3>
 * <p>
 * QiEntryStationConfigurationSettings contains the setting values for an Entry station
 * </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * 
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 */
public enum AdditionalStationConfigSettings implements IdEnum<AdditionalStationConfigSettings> {
	REPAIR_AREA(1,"Repair Area for Fixed Units","Automatically assigns unit scanned at this station to a repair area when all defects are fixed");
	
	private int id;
	private String settingName;
	private String settingDesc;

	private AdditionalStationConfigSettings(int id,String settingName, String settingDesc){
		this.id=id;
		this.settingName = settingName;
		this.settingDesc = settingDesc;
	}

	@Override
	public int getId() {
		return id;
	}
	
	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

	public String getSettingDesc() {
		return settingDesc;
	}

	public void setSettingDesc(String settingDesc) {
		this.settingDesc = settingDesc;
	}
}
