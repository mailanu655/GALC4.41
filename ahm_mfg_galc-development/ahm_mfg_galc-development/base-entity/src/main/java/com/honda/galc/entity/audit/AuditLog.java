package com.honda.galc.entity.audit;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>AuditLog Class description</h3>
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
 * <TR>
 * <TD>1.0</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 * </TABLE>
 * 
 * @author L&T Infotech<br>
 *         October 10, 2016
 * 
 * 
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
@Entity
@Table(name = "AUDIT_LOG_TBX")
public class AuditLog extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AuditLogId id;
	
	@Column(name = "PRIMARY_KEY")
	private String primaryKey;

	@Column(name = "PREVIOUS_VALUE")
	private String previousValue;

	@Column(name = "CURRENT_VALUE")
	private String currentValue;

	@Column(name = "UPDATE_REASON")
	private String updateReason;

	@Column(name = "MAINTENANCE_SCREEN")
	private String maintenanceScreen;

	@Column(name = "SYSTEM")
	private String system;

	@Column(name = "CHANGE_TYPE")
	private String changeType;

	@Column(name = "UPDATE_USER")
	private String updateUser;

	public AuditLog() {
	}

	public AuditLogId getId() {
		return this.id;
	}

	public void setId(AuditLogId id) {
		this.id = id;
	}

	public String getPreviousValue() {
		return StringUtils.trim(previousValue);
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getCurrentValue() {
		return StringUtils.trim(currentValue);
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getUpdateReason() {
		return StringUtils.trim(updateReason);
	}

	public void setUpdateReason(String updateReason) {
		this.updateReason = updateReason;
	}

	public String getMaintenanceScreen() {
		return StringUtils.trim(maintenanceScreen);
	}

	public void setMaintenanceScreen(String maintenanceScreen) {
		this.maintenanceScreen = maintenanceScreen;
	}

	public String getSystem() {
		return StringUtils.trim(system);
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getChangeType() {
		return StringUtils.trim(changeType);
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getUpdateUser() {
		return StringUtils.trim(updateUser);
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getPrimaryKey() {
		return StringUtils.trim(primaryKey);
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
		result = prime * result + ((currentValue == null) ? 0 : currentValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((maintenanceScreen == null) ? 0 : maintenanceScreen.hashCode());
		result = prime * result + ((previousValue == null) ? 0 : previousValue.hashCode());
		result = prime * result + ((primaryKey == null) ? 0 : primaryKey.hashCode());
		result = prime * result + ((system == null) ? 0 : system.hashCode());
		result = prime * result + ((updateReason == null) ? 0 : updateReason.hashCode());
		result = prime * result + ((updateUser == null) ? 0 : updateUser.hashCode());
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
		AuditLog other = (AuditLog) obj;
		if (changeType == null) {
			if (other.changeType != null)
				return false;
		} else if (!changeType.equals(other.changeType))
			return false;
		if (currentValue == null) {
			if (other.currentValue != null)
				return false;
		} else if (!currentValue.equals(other.currentValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (maintenanceScreen == null) {
			if (other.maintenanceScreen != null)
				return false;
		} else if (!maintenanceScreen.equals(other.maintenanceScreen))
			return false;
		if (previousValue == null) {
			if (other.previousValue != null)
				return false;
		} else if (!previousValue.equals(other.previousValue))
			return false;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		if (system == null) {
			if (other.system != null)
				return false;
		} else if (!system.equals(other.system))
			return false;
		if (updateReason == null) {
			if (other.updateReason != null)
				return false;
		} else if (!updateReason.equals(other.updateReason))
			return false;
		if (updateUser == null) {
			if (other.updateUser != null)
				return false;
		} else if (!updateUser.equals(other.updateUser))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return toString( getId().getTableName(), 
				getId().getColumnName(),
				getId().getPrimaryKeyValue(),
				getId().getActualTimeStamp(),
				getPrimaryKey(),
				getPreviousValue(),
				getCurrentValue(), 
				getUpdateReason(), 
				getMaintenanceScreen(),
				getSystem(), 
				getChangeType(), 
				getUpdateUser());
	}  

}
