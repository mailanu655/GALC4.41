package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class ProductAttributeId implements Serializable {
	

	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="ATTRIBUTE")
	private String attribute;



	private static final long serialVersionUID = 1L;

	public ProductAttributeId() {
		super();
	}

	public ProductAttributeId(String attribute, String productId) {
		this.attribute = attribute;
		this.productId = productId;
	}

	public String getAttribute() {
		return StringUtils.trim(this.attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ProductAttributeId)) {
			return false;
		}
		ProductAttributeId other = (ProductAttributeId) o;
		return getAttribute().equals(other.getAttribute())
			&& getProductId().equals(other.getProductId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + getAttribute().hashCode();
		hash = hash * prime + getProductId().hashCode();
		
		return hash;
	}

	@Override
	public String toString() {
		return "ProductAttributeId [productId=" + productId + ", attribute=" + attribute + "]";
	}

}
