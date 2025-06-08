package com.honda.galc.entity.qi;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;

@Embeddable
public class QiResponsibilityMappingId implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "DEFAULT_RESPONSIBLE_LEVEL_ID", nullable=false)
	private Integer defaultResponsibleLevelId;
	
	@Column(name = "PLANT_CODE", nullable=false)
	private String plantCode;
	
	@Column(name = "RESPONSIBLE_LEVEL_ID", nullable=false)
	private Integer responsibleLevelId;
	
	public QiResponsibilityMappingId(int defaultRespLevelId, String plantCode2) {
		this.setDefaultRespLevelId(defaultRespLevelId);
		this.setPlantCode(plantCode2);
	}
	
	public QiResponsibilityMappingId() {
		super();
	}
	
	public QiResponsibilityMappingId(int defaultRespLevelId, String plantCode2, int respLevelId) {
		this.setDefaultRespLevelId(defaultRespLevelId);
		this.setPlantCode(plantCode2);
		this.setRespLevelId(respLevelId);
	}
	
	public int getDefaultRespLevelId () {
		return this.defaultResponsibleLevelId;
	}
	 
	public void setDefaultRespLevelId (int defaultRespLevelId) {
		this.defaultResponsibleLevelId = defaultRespLevelId;
	}
	
	public String getPlantCode () {
		return StringUtils.trim(plantCode);
	}
	
	public void setPlantCode (String plantCode) {
		this.plantCode = plantCode;
	}
	
	public int getRespLevelId () {
		return this.responsibleLevelId;
	}
	
	public void setRespLevelId (int respLevelId) {
		this.responsibleLevelId = respLevelId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultResponsibleLevelId == null) ? 0 : defaultResponsibleLevelId.hashCode());
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
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
		QiResponsibilityMappingId other = (QiResponsibilityMappingId) obj;
		if (defaultResponsibleLevelId == null) {
			if (other.defaultResponsibleLevelId != null)
				return false;
		} else if (!defaultResponsibleLevelId.equals(other.defaultResponsibleLevelId))
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
