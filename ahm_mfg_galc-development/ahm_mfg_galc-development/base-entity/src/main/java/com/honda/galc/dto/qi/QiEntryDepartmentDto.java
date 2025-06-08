package com.honda.galc.dto.qi;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * 
 * <h3>QiEntryDepartmentDto Class description</h3>
 * <p>
 * QiEntryDepartmentDto contains the getter and setter of the Location properties and maps this class with database table and properties with the database its columns .
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
 * 
 */
public class QiEntryDepartmentDto implements IDto,Comparator<QiEntryDepartmentDto>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DtoTag(outputName = "DIVISION_ID")
	private String divisionId;
	@DtoTag(name = "DIVISION_NAME")
    private String divisionName;
	@DtoTag(name = "DIVISION_DESCRIPTION")
    private String divisionDescription;

	@DtoTag(outputName = "IS_DEFAULT")
	private short isDefault;
    private String currentDefectStatus;
    private String originalDefectStatus;
	
	public String getCurrentDefectStatus() {
		return StringUtils.trimToEmpty(currentDefectStatus);
	}
	public void setCurrentDefectStatus(String currentDefectStatus) {
		this.currentDefectStatus = currentDefectStatus;
	}
	public String getOriginalDefectStatus() {
		return StringUtils.trimToEmpty(originalDefectStatus);
	}
	public void setOriginalDefectStatus(String originalDefectStatus) {
		this.originalDefectStatus = originalDefectStatus;
	}
	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	public short getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(short isDefault) {
		this.isDefault = isDefault;
	}

	public String getDivisionName() {
		return divisionName;
	}
	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}
	public String getDivisionDescription() {
		return divisionDescription;
	}
	public void setDivisionDescription(String divisionDescription) {
		this.divisionDescription = divisionDescription;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((divisionId == null) ? 0 : divisionId.hashCode());
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
		QiEntryDepartmentDto other = (QiEntryDepartmentDto) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		return true;
	}
	public int compare(QiEntryDepartmentDto qiEntryDepartmentDto1, QiEntryDepartmentDto qiEntryDepartmentDto2) {
		return qiEntryDepartmentDto1.getDivisionId().compareToIgnoreCase(qiEntryDepartmentDto2.getDivisionId());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(divisionId).append("-").append(divisionName);
		return sb.toString();
	}


}
