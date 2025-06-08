package com.honda.galc.entity.product;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="PRODUCT_ATTRIBUTES_TBX")
public class ProductAttribute extends AuditEntry {
	@EmbeddedId
	private ProductAttributeId id;

	@Column(name="ATTRIBUTE_VALUE")
	private String attributeValue;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	
	private static final long serialVersionUID = 1L;

	public ProductAttribute() {
		super();
	}

	public ProductAttributeId getId() {
		return id;
	}
	
	public  void setId(ProductAttributeId id) {
		this.id = id;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public Timestamp getActualTimestamp() {
		return actualTimestamp;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public void setActualTimestamp(Timestamp timestamp) {
		this.actualTimestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualTimestamp == null) ? 0 : actualTimestamp.hashCode());
		result = prime * result + ((attributeValue == null) ? 0 : attributeValue.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ProductAttribute other = (ProductAttribute) obj;
		if (actualTimestamp == null) {
			if (other.actualTimestamp != null)
				return false;
		} else if (!actualTimestamp.equals(other.actualTimestamp))
			return false;
		if (attributeValue == null) {
			if (other.attributeValue != null)
				return false;
		} else if (!attributeValue.equals(other.attributeValue))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProductAttribute [id=" + id + ", attributeValue=" + attributeValue + ", actualTimestamp="
				+ actualTimestamp + "]";
	}
	
	
	
}
