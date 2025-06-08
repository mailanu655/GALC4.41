package com.honda.galc.entity.product;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class PhysicalTableMapId implements Serializable {
	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="TABLE_NAME")
	private String tableName;

	private static final long serialVersionUID = 1L;

	public PhysicalTableMapId() {
		super();
	}
	
	public PhysicalTableMapId(String productId, String tableName) {
		super();
		this.productId = productId;
		this.tableName = tableName;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getTableName() {
		return StringUtils.trim(this.tableName);
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof PhysicalTableMapId)) {
			return false;
		}
		PhysicalTableMapId other = (PhysicalTableMapId) o;
		return this.productId.equals(other.productId)
			&& this.tableName.equals(other.tableName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productId.hashCode();
		hash = hash * prime + this.tableName.hashCode();
		return hash;
	}

}
