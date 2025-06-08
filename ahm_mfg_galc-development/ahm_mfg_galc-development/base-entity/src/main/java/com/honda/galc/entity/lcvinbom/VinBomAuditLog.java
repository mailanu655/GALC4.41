package com.honda.galc.entity.lcvinbom;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.VinBomAuditLogChangeType;


/**
 * The persistent class for the AUDIT_LOG database table.
 * 
 */
@Entity
@Table(name="AUDIT_LOG", schema="LCVINBOM")
public class VinBomAuditLog extends AuditEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AUDIT_LOG_ID", unique=true, nullable=false)
	private Long auditLogId;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	@Column(name="ASSOCIATE_NUMBER", length=11)
	private String associateNumber;

	@Enumerated(EnumType.STRING)
	@Column(name="CHANGE_TYPE", nullable=false, length=18)
	private VinBomAuditLogChangeType changeType;

	@Column(name="COLUMN_NAME", nullable=false, length=128)
	private String columnName;

	@Column(name="CURRENT_VALUE", length=256)
	private String currentValue;

	@Column(name="PRIMARY_KEY_VALUE", nullable=false, length=286)
	private String primaryKeyValue;

	@Column(name="\"TABLE_NAME\"", nullable=false, length=64)
	private String tableName;

	@Column(name="UPDATED_VALUE", length=256)
	private String updatedValue;

	public VinBomAuditLog() {
	}

	public Long getAuditLogId() {
		return this.auditLogId;
	}

	public void setAuditLogId(Long auditLogId) {
		this.auditLogId = auditLogId;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	public String getAssociateNumber() {
		return StringUtils.trim(this.associateNumber);
	}

	public void setAssociateNumber(String associateNumber) {
		this.associateNumber = associateNumber;
	}

	public VinBomAuditLogChangeType getChangeType() {
		return this.changeType;
	}

	public void setChangeType(VinBomAuditLogChangeType changeType) {
		this.changeType = changeType;
	}

	public String getColumnName() {
		return StringUtils.trim(this.columnName);
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getCurrentValue() {
		return StringUtils.trim(this.currentValue);
	}

	public void setCurrentValue(String currentValue) {
		this.currentValue = currentValue;
	}

	public String getPrimaryKeyValue() {
		return StringUtils.trim(this.primaryKeyValue);
	}

	public void setPrimaryKeyValue(String primaryKeyValue) {
		this.primaryKeyValue = primaryKeyValue;
	}

	public String getTableName() {
		return StringUtils.trim(this.tableName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getUpdatedValue() {
		return StringUtils.trim(this.updatedValue);
	}

	public void setUpdatedValue(String updatedValue) {
		this.updatedValue = updatedValue;
	}

	@Override
	public Object getId() {
		return this.auditLogId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((associateNumber == null) ? 0 : associateNumber.hashCode());
		result = prime * result + ((auditLogId == null) ? 0 : auditLogId.hashCode());
		result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
		result = prime * result + ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result + ((currentValue == null) ? 0 : currentValue.hashCode());
		result = prime * result + ((primaryKeyValue == null) ? 0 : primaryKeyValue.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((updatedValue == null) ? 0 : updatedValue.hashCode());
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
		VinBomAuditLog other = (VinBomAuditLog) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (associateNumber == null) {
			if (other.associateNumber != null)
				return false;
		} else if (!associateNumber.equals(other.associateNumber))
			return false;
		if (auditLogId == null) {
			if (other.auditLogId != null)
				return false;
		} else if (!auditLogId.equals(other.auditLogId))
			return false;
		if (changeType != other.changeType)
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (currentValue == null) {
			if (other.currentValue != null)
				return false;
		} else if (!currentValue.equals(other.currentValue))
			return false;
		if (primaryKeyValue == null) {
			if (other.primaryKeyValue != null)
				return false;
		} else if (!primaryKeyValue.equals(other.primaryKeyValue))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (updatedValue == null) {
			if (other.updatedValue != null)
				return false;
		} else if (!updatedValue.equals(other.updatedValue))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getId(), getActualTimestamp(), getAssociateNumber(), 
				getChangeType(), getColumnName(), getCurrentValue(), 
				getPrimaryKeyValue(), getUpdatedValue());
	}
}