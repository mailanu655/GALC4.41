package com.honda.galc.dto.qi;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.IDto;
import com.honda.galc.entity.enumtype.QiEntryStationConfigurationSettings;
/**
 * 
 * <h3>QiEntryStationSettingsDto Class description</h3>
 * <p>
 * QiEntryStationSettingsDto contains the getter and setter of the Location properties and maps this class with database table and properties with the database its columns .
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

public class QiEntryStationSettingsDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	private int id;
	private String settingName;
	private String settingDescription;
	private String settingPropertyValue;
	public QiEntryStationSettingsDto(){
		super();
	}

	public QiEntryStationSettingsDto(QiEntryStationConfigurationSettings entryStaionSetting) {
		super();
		this.id=entryStaionSetting.getId();
		this.settingName = entryStaionSetting.getSettingsName();
		this.settingDescription = entryStaionSetting.getSettingsComment();
		this.settingPropertyValue = entryStaionSetting.getDefaultPropertyValue();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSettingName() {
		return StringUtils.trimToEmpty(settingName);
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public String getSettingDescription() {
		return StringUtils.trimToEmpty(settingDescription);
	}
	public void setSettingDescription(String settingDescription) {
		this.settingDescription = settingDescription;
	}
	public String getSettingPropertyValue() {
		return StringUtils.trimToEmpty(settingPropertyValue);
	}
	public void setSettingPropertyValue(String settingPropertyValue) {
		this.settingPropertyValue = settingPropertyValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((settingDescription == null) ? 0 : settingDescription.hashCode());
		result = prime * result + ((settingName == null) ? 0 : settingName.hashCode());
		result = prime * result + ((settingPropertyValue == null) ? 0 : settingPropertyValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiEntryStationSettingsDto other = (QiEntryStationSettingsDto) obj;
		if (id != other.id)
			return false;
		if (settingDescription == null) {
			if (other.settingDescription != null)
				return false;
		} else if (!settingDescription.equals(other.settingDescription))
			return false;
		if (settingName == null) {
			if (other.settingName != null)
				return false;
		} else if (!settingName.equals(other.settingName))
			return false;
		if (settingPropertyValue == null) {
			if (other.settingPropertyValue != null)
				return false;
		} else if (!settingPropertyValue.equals(other.settingPropertyValue))
			return false;
		return true;
	}
	
}
