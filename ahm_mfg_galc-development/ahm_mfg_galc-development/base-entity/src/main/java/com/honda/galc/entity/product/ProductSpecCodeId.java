package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductSpecCodeId implements Serializable {
	//@Column(name="PRODUCT_TYPE", insertable=false, updatable=false)
	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	private static final long serialVersionUID = 1L;

	public ProductSpecCodeId() {
		super();
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductSpecCode() {
		return this.productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ProductSpecCodeId)) {
			return false;
		}
		ProductSpecCodeId other = (ProductSpecCodeId) o;
		return this.productSpecCode.equals(other.productSpecCode)
			&& this.productType.equals(other.productType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productSpecCode.hashCode();
		hash = hash * prime + this.productType.hashCode();
		return hash;
	}

}
