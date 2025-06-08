package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;

/**
 * 
 * <h3>QiRepairArea Class description</h3>
 * <p> QiRepairArea description </p>
 * 
 * <h4>Repair Area</h4>
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
 * @author LnTInfotech<br>
 *        Oct 13, 2016
 * 
 */

@Entity
@Table(name = "QI_REPAIR_AREA_TBX")
public class QiRepairArea extends CreateUserAuditEntry {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "REPAIR_AREA_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	private String repairAreaName;
	
	@Column(name = "SITE_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 2)
	private String siteName;
	
	@Column(name = "PLANT_NAME")
	@Auditable(isPartOfPrimaryKey = true, sequence = 3)
	private String plantName;
	
	@Column(name = "REPAIR_AREA_DESCRIPTION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 4)
	private String repairAreaDescription;
	
	@Column(name = "LOCATION")
	@Auditable(isPartOfPrimaryKey = true, sequence = 5)
	private char location;
	
	@Column(name = "PRIORITY")
	@Auditable(isPartOfPrimaryKey = true, sequence = 6)
	private short priority;	
	
	@Column(name = "ROW_FILL_SEQ")
	@Auditable(isPartOfPrimaryKey = true, sequence = 7)
	private char rowFillSeq;	
	
	@Column(name = "DIVISION_NAME")
	@Auditable
	private String divName;
	
	public String getDivName() {
		return StringUtils.trimToEmpty(divName);
	}

	public void setDivName(String divName) {
		this.divName = divName;
	}

	public String getId() {
		return getRepairAreaName();
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public String getSiteName() {
		return StringUtils.trimToEmpty(siteName);
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getPlantName() {
		return StringUtils.trimToEmpty(plantName);
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getRepairAreaDescription() {
		return StringUtils.trimToEmpty(repairAreaDescription);
	}

	public void setRepairAreaDescription(String repairAreaDescription) {
		this.repairAreaDescription = repairAreaDescription;
	}	

	public short getPriority() {
		return priority;
	}

	public void setPriority(short priority) {
		this.priority = priority;
	}

	public char getLocation() {
		return location;
	}

	public void setLocation(char location) {
		this.location = location;
	}

	public char getRowFillSeq() {
		return rowFillSeq;
	}

	public void setRowFillSeq(char rowFillSeq) {
		this.rowFillSeq = rowFillSeq;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + location;
		result = prime * result + ((plantName == null) ? 0 : plantName.hashCode());
		result = prime * result + priority;
		result = prime * result + ((repairAreaDescription == null) ? 0 : repairAreaDescription.hashCode());
		result = prime * result + ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + rowFillSeq;
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + ((divName == null) ? 0 : divName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QiRepairArea other = (QiRepairArea) obj;
		if (location != other.location)
			return false;
		if (plantName == null) {
			if (other.plantName != null)
				return false;
		} else if (!plantName.equals(other.plantName))
			return false;
		if (priority != other.priority)
			return false;
		if (repairAreaDescription == null) {
			if (other.repairAreaDescription != null)
				return false;
		} else if (!repairAreaDescription.equals(other.repairAreaDescription))
			return false;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (rowFillSeq != other.rowFillSeq)
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (divName == null) {
			if (other.divName != null)
				return false;
		} else if (!divName.equals(other.divName))
			return false;
		return true;
	}

	
}
