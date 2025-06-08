package com.honda.galc.dto;

/**
 * @author Subu Kathiresan
 * @date Mar 21, 2023
 */
public class ExtProductStatusIdDto implements IDto {
	
	private static final long serialVersionUID = 1L;
	
	@DtoTag()
    private String productId;

	@DtoTag()
    private String originSiteInstance;
	
	public ExtProductStatusIdDto() {}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((originSiteInstance == null) ? 0 : originSiteInstance.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
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
		ExtProductStatusIdDto other = (ExtProductStatusIdDto) obj;
		if (originSiteInstance == null) {
			if (other.originSiteInstance != null)
				return false;
		} else if (!originSiteInstance.equals(other.originSiteInstance))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		return true;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getOriginSiteInstance() {
		return originSiteInstance;
	}

	public void setOriginSiteInstance(String originSiteInstance) {
		this.originSiteInstance = originSiteInstance;
	}
}
