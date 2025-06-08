package com.honda.galc.dto.qi;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>QiPddaResponsibleLoadTriggerDto</code> is the Dto class for Pdda Responsibility data.
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TABLE>
 * @see
 * @ver 1.0.0
 * @author L&T Infotech
 */
public class QiPddaResponsibleLoadTriggerDto implements IDto{
	private static final long serialVersionUID = 1L;
	@DtoTag(outputName ="PDDA_RESPONSIBILITY_ID")
	private Integer pddaResponsibilityId;
	@DtoTag(outputName ="DATA_LOCATION")
	private String dataLocation;
	@DtoTag(outputName ="ADMIN_CONFIRMED_FIX")
	private String adminConfirmedFix;
	@DtoTag(outputName ="DATE_TIMESTAMP")
	private Timestamp dateTimestamp;
	@DtoTag(outputName ="LINE")
	private String line;
	@DtoTag(outputName ="BASE_PART_NO")
	private String basePartNo;
	@DtoTag(outputName ="TEAM_GROUP_NO")
	private String teamGroupNo;
	@DtoTag(outputName ="TEAM_NO")
	private String teamNo;
	@DtoTag(outputName ="TEAM_NAME")
	private String teamName;
	@DtoTag(outputName ="PROCESS_NO")
	private String processNo;
	@DtoTag(outputName ="PROCESS_NAME")
	private String processName;
	@DtoTag(outputName ="UNIT_NO")
	private String unitNo;
	@DtoTag(outputName ="UNIT_DESC")
	private String unitDesc;

	public Integer getPddaResponsibilityId() {
		return pddaResponsibilityId;
	}
	public void setPddaResponsibilityId(Integer pddaResponsibilityId) {
		this.pddaResponsibilityId = pddaResponsibilityId;
	}
	public String getDataLocation() {
		return StringUtils.trimToEmpty(dataLocation);
	}
	public void setDataLocation(String dataLocation) {
		this.dataLocation = dataLocation;
	}
	public String getAdminConfirmedFix() {
		return StringUtils.trimToEmpty(adminConfirmedFix);
	}
	public void setAdminConfirmedFix(String adminConfirmedFix) {
		this.adminConfirmedFix = adminConfirmedFix;
	}
	public String getDateTimestamp() {
		return (null!=dateTimestamp?StringUtils.trimToEmpty(dateTimestamp.toString()):StringUtils.EMPTY);
	}
	public void setDateTimestamp(Timestamp dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}
	public String getLine() {
		return StringUtils.trimToEmpty(line);
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getBasePartNo() {
		return StringUtils.trimToEmpty(basePartNo);
	}
	public void setBasePartNo(String basePartNo) {
		this.basePartNo = basePartNo;
	}
	public String getTeamGroupNo() {
		return StringUtils.trimToEmpty(teamGroupNo);
	}
	public void setTeamGroupNo(String teamGroupNo) {
		this.teamGroupNo = teamGroupNo;
	}
	public String getTeamNo() {
		return StringUtils.trimToEmpty(teamNo);
	}
	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}
	public String getTeamName() {
		return StringUtils.trimToEmpty(teamName);
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getProcessNo() {
		return StringUtils.trimToEmpty(processNo);
	}
	public void setProcessNo(String processNo) {
		this.processNo = processNo;
	}
	public String getProcessName() {
		return StringUtils.trimToEmpty(processName);
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getUnitNo() {
		return StringUtils.trimToEmpty(unitNo);
	}
	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}
	public String getUnitDesc() {
		return StringUtils.trimToEmpty(unitDesc);
	}
	public void setUnitDesc(String unitDesc) {
		this.unitDesc = unitDesc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminConfirmedFix == null) ? 0 : adminConfirmedFix.hashCode());
		result = prime * result + ((basePartNo == null) ? 0 : basePartNo.hashCode());
		result = prime * result + ((dataLocation == null) ? 0 : dataLocation.hashCode());
		result = prime * result + ((dateTimestamp == null) ? 0 : dateTimestamp.hashCode());
		result = prime * result + ((line == null) ? 0 : line.hashCode());
		result = prime * result + ((pddaResponsibilityId == null) ? 0 : pddaResponsibilityId.hashCode());
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((processNo == null) ? 0 : processNo.hashCode());
		result = prime * result + ((teamGroupNo == null) ? 0 : teamGroupNo.hashCode());
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
		result = prime * result + ((teamNo == null) ? 0 : teamNo.hashCode());
		result = prime * result + ((unitDesc == null) ? 0 : unitDesc.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
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
		QiPddaResponsibleLoadTriggerDto other = (QiPddaResponsibleLoadTriggerDto) obj;
		if (adminConfirmedFix == null) {
			if (other.adminConfirmedFix != null)
				return false;
		} else if (!adminConfirmedFix.equals(other.adminConfirmedFix))
			return false;
		if (basePartNo == null) {
			if (other.basePartNo != null)
				return false;
		} else if (!basePartNo.equals(other.basePartNo))
			return false;
		if (dataLocation == null) {
			if (other.dataLocation != null)
				return false;
		} else if (!dataLocation.equals(other.dataLocation))
			return false;
		if (dateTimestamp == null) {
			if (other.dateTimestamp != null)
				return false;
		} else if (!dateTimestamp.equals(other.dateTimestamp))
			return false;
		if (line == null) {
			if (other.line != null)
				return false;
		} else if (!line.equals(other.line))
			return false;
		if (pddaResponsibilityId == null) {
			if (other.pddaResponsibilityId != null)
				return false;
		} else if (!pddaResponsibilityId.equals(other.pddaResponsibilityId))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (processNo == null) {
			if (other.processNo != null)
				return false;
		} else if (!processNo.equals(other.processNo))
			return false;
		if (teamGroupNo == null) {
			if (other.teamGroupNo != null)
				return false;
		} else if (!teamGroupNo.equals(other.teamGroupNo))
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		if (teamNo == null) {
			if (other.teamNo != null)
				return false;
		} else if (!teamNo.equals(other.teamNo))
			return false;
		if (unitDesc == null) {
			if (other.unitDesc != null)
				return false;
		} else if (!unitDesc.equals(other.unitDesc))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QiPddaResponsibleLoadTriggerDto [pddaResponsibilityId=" + pddaResponsibilityId + ", dataLocation="
				+ dataLocation + ", adminConfirmedFix=" + adminConfirmedFix + ", dateTimestamp=" + dateTimestamp
				+ ", line=" + line + ", basePartNo=" + basePartNo + ", teamGroupNo=" + teamGroupNo + ", teamNo="
				+ teamNo + ", teamName=" + teamName + ", processNo=" + processNo + ", processName=" + processName
				+ ", unitNo=" + unitNo + ", unitDesc=" + unitDesc + "]";
	}
}
