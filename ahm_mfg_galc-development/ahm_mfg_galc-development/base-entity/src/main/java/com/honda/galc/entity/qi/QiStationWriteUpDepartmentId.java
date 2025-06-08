package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiStationWriteUpDepartmentId Class description</h3>
 * <p>
 * QiStationWriteUpDepartmentId contains the getter and setter of the Part
 * properties and maps this class with database table and properties with the
 * database its columns .
 * </p>
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
 * @author LnTInfotech<br>
 *         Oct 25,2016
 */
@Embeddable
public class QiStationWriteUpDepartmentId implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "SITE")
	private String site;
	
	@Column(name = "PLANT")
	private String plant;
	
	@Column(name = "DEPT")
	private String divisionId;
	
	@Column(name = "COLOR_CODE")
	private String colorCode;

	public QiStationWriteUpDepartmentId() {
		super();
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getDivisionId() {
		return StringUtils.trimToEmpty(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getColorCode() {
		return StringUtils.trimToEmpty(colorCode);
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	public String getSite() {
		return StringUtils.trimToEmpty(site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPlant() {
		return StringUtils.trimToEmpty(plant);
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((colorCode == null) ? 0 : colorCode.hashCode());
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((plant == null) ? 0 : plant.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
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
		QiStationWriteUpDepartmentId other = (QiStationWriteUpDepartmentId) obj;
		if (colorCode == null) {
			if (other.colorCode != null)
				return false;
		} else if (!colorCode.equals(other.colorCode))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (plant == null) {
			if (other.plant != null)
				return false;
		} else if (!plant.equals(other.plant))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
