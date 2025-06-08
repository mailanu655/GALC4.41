package com.honda.galc.dto;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Subu Kathiresan
 * @date Jan 06, 2023
 */
public class ExtProductCheckDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	@DtoTag()
	private String id;
	
	@DtoTag()
    private String productType;
	
	@DtoTag()
    private String productSpecCode;
	
	@DtoTag()
    private String productionLot;
	
	@DtoTag()
    private String productSpecType;
    
	@DtoTag()
    private String productKind;
	
	@DtoTag()
    private Timestamp createTimestamp;
    
	@DtoTag()
    private Timestamp updateTimestamp;
	
	@DtoTag()
	private List<ExtProductStatusDto> productStatusList;
	
	public ExtProductCheckDto() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createTimestamp == null) ? 0 : createTimestamp.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((productKind == null) ? 0 : productKind.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productSpecType == null) ? 0 : productSpecType.hashCode());
		result = prime * result + ((productStatusList == null) ? 0 : productStatusList.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
		result = prime * result + ((updateTimestamp == null) ? 0 : updateTimestamp.hashCode());
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
		ExtProductCheckDto other = (ExtProductCheckDto) obj;
		if (createTimestamp == null) {
			if (other.createTimestamp != null)
				return false;
		} else if (!createTimestamp.equals(other.createTimestamp))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productKind == null) {
			if (other.productKind != null)
				return false;
		} else if (!productKind.equals(other.productKind))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productSpecType == null) {
			if (other.productSpecType != null)
				return false;
		} else if (!productSpecType.equals(other.productSpecType))
			return false;
		if (productStatusList == null) {
			if (other.productStatusList != null)
				return false;
		} else if (!productStatusList.equals(other.productStatusList))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		if (updateTimestamp == null) {
			if (other.updateTimestamp != null)
				return false;
		} else if (!updateTimestamp.equals(other.updateTimestamp))
			return false;
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getProductionLot() {
		return productionLot;
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductSpecType() {
		return productSpecType;
	}

	public void setProductSpecType(String productSpecType) {
		this.productSpecType = productSpecType;
	}

	public String getProductKind() {
		return productKind;
	}

	public void setProductKind(String productKind) {
		this.productKind = productKind;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public List<ExtProductStatusDto> getProductStatusList() {
		return productStatusList;
	}

	public void setProductStatusList(List<ExtProductStatusDto> productStatusList) {
		this.productStatusList = productStatusList;
	}
}
