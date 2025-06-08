package com.honda.galc.entity.audit;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>AuditLogId Class description</h3>
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
 */
@Embeddable
public class AuditLogId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "TABLE_NAME")
	private String tableName;

	@Column(name = "COLUMN_NAME")
	private String columnName;

	@Column(name = "PRIMARY_KEY_VALUE")
	private String primaryKeyValue;

	@Column(name = "ACTUAL_TIMESTAMP")
	private Timestamp actualTimeStamp;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditLogId other = (AuditLogId) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (columnName == null) {
			if (other.columnName != null)
				return false;
		} else if (!columnName.equals(other.columnName))
			return false;
		if (primaryKeyValue == null) {
			if (other.primaryKeyValue != null)
				return false;
		} else if (!primaryKeyValue.equals(other.primaryKeyValue))
			return false;
		if (actualTimeStamp == null) {
			if (other.actualTimeStamp != null)
				return false;
		} else if (!actualTimeStamp.equals(other.actualTimeStamp))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result
				+ ((columnName == null) ? 0 : columnName.hashCode());
		result = prime * result
				+ ((primaryKeyValue == null) ? 0 : primaryKeyValue.hashCode());
		result = prime * result
				+ ((actualTimeStamp == null) ? 0 : actualTimeStamp.hashCode());
		return result;
	}

	public String getTableName() {
		return StringUtils.trim(tableName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return StringUtils.trim(columnName);
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getPrimaryKeyValue() {
		return StringUtils.trim(primaryKeyValue);
	}

	public void setPrimaryKeyValue(String primaryKey) {
		this.primaryKeyValue = primaryKey;
	}

	public Timestamp getActualTimeStamp() {
		return actualTimeStamp;
	}

	public void setActualTimeStamp(Timestamp actualTimeStamp) {
		this.actualTimeStamp = actualTimeStamp;
	}

}
