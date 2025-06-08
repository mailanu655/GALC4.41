package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.util.ToStringUtil;
/**
 * 
 * <h3>QiEntryStationConfigManagementId Class description</h3>
 * <p>
 * QiEntryStationConfigManagementId contains the getter and setter of the Location properties and maps this class with database table and properties with the database its columns .
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
@Embeddable
public class QiStationConfigurationId implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name="PROCESS_POINT_ID", nullable=false)
	private String processPointId;
	@Column(name="PROPERTY_KEY", nullable=false)
	private String propertyKey;
	public QiStationConfigurationId() {
		super();
	}
	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	public String getPropertyKey() {
		return StringUtils.trimToEmpty(propertyKey);
	}
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((propertyKey == null) ? 0 : propertyKey.hashCode());
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
		QiStationConfigurationId other = (QiStationConfigurationId) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (propertyKey == null) {
			if (other.propertyKey != null)
				return false;
		} else if (!propertyKey.equals(other.propertyKey))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}

	
}
