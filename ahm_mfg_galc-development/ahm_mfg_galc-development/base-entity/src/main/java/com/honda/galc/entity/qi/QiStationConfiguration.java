package com.honda.galc.entity.qi;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.CreateUserAuditEntry;
import com.honda.galc.entity.enumtype.QiEntryStationDefaultStatus;

/**
 * 
 * <h3>QiEntryStationConfigManagement Class description</h3>
 * <p>
 * QiEntryStationConfigManagement contains the getter and setter of the Part properties and maps this class with database table and properties with the database its columns .
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
 *        Oct 25,2016
 * 
 */
/**   
 * @author Gangadhararao Gadde
 * @since Jan 15, 2018
 * Simulation changes
 */
@Entity
@Table(name="QI_STATION_CONFIG_TBX")
public class QiStationConfiguration extends CreateUserAuditEntry {
	
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey = true, sequence = 1)
	QiStationConfigurationId id;
	@Auditable
	@Column(name="PROPERTY_VALUE")
	private String propertyValue;
	@Auditable
	@Column(name="ACTIVE")
	private short active;

	public QiStationConfiguration() {
		super();
	}
	
	public String getPropertyValue() {
		return StringUtils.trimToEmpty(this.propertyValue);
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public short getActive() {
		return active;
	}

	public void setActive(short active) {
		this.active = active;
	}
	
	public QiStationConfigurationId getId() {
		return id;
	}

	public void setId(QiStationConfigurationId id) {
		this.id = id;
	}

	public String getStatus() {
		return QiEntryStationDefaultStatus.getType(active).getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + active;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((propertyValue == null) ? 0 : propertyValue.hashCode());
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
		QiStationConfiguration other = (QiStationConfiguration) obj;
		if (active != other.active)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (propertyValue == null) {
			if (other.propertyValue != null)
				return false;
		} else if (!propertyValue.equals(other.propertyValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId().getProcessPointId(), getId().getPropertyKey(), getPropertyValue(), getActive());
	}
	
}
