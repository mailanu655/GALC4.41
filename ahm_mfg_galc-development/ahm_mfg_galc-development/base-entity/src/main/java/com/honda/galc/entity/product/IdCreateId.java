package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class IdCreateId implements Serializable {
	@Column(name="TABLE_NAME")
	private String tableName;

	@Column(name="COLUMN_NAME")
	private String columnName;

	private static final long serialVersionUID = 1L;

	public IdCreateId() {
		super();
	}

	public String getTableName() {
		return StringUtils.trim(this.tableName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return StringUtils.trim(this.columnName);
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof IdCreateId)) {
			return false;
		}
		IdCreateId other = (IdCreateId) o;
		return this.tableName.equals(other.tableName)
			&& this.columnName.equals(other.columnName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.tableName.hashCode();
		hash = hash * prime + this.columnName.hashCode();
		return hash;
	}

}
