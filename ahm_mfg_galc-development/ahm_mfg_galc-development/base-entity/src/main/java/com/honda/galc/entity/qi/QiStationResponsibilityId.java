package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiStationResponsibilityId Class description</h3>
 * <p>
 * </p>
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
 *         10th April,2017
 * 
 */
@Embeddable
public class QiStationResponsibilityId implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public QiStationResponsibilityId() {
		super();
	}
	
	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "RESPONSIBLE_LEVEL_ID")
	private Integer responsibleLevelId;
	
	public QiStationResponsibilityId(String processPointId, Integer responsibleLevelId) {
		super();
		this.processPointId = processPointId;
		this.responsibleLevelId = responsibleLevelId;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public Integer getResponsibleLevelId() {
		return responsibleLevelId;
	}

	public void setResponsibleLevelId(Integer responsibleLevelId) {
		this.responsibleLevelId = responsibleLevelId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((responsibleLevelId == null) ? 0 : responsibleLevelId.hashCode());
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
		QiStationResponsibilityId other = (QiStationResponsibilityId) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (responsibleLevelId == null) {
			if (other.responsibleLevelId != null)
				return false;
		} else if (!responsibleLevelId.equals(other.responsibleLevelId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
}
