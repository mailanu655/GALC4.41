package com.honda.galc.web;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.honda.galc.entity.product.ProductSequence;
import com.honda.galc.entity.product.ProductSequenceId;

@JsonRootName(value = "ProductSequenceDto")
public class ProductSequenceDto {

	private String productId;

	private String stationId;

	private Timestamp referenceTimestamp;

	private String associateNo;

	private Integer sequenceNumber;

	private String sourceSystemId;

	private String productType;

	
	public ProductSequenceDto() {
		super();
	}


	public String getProductId() {
		return StringUtils.trim(productId);
	}


	public void setProductId(String productId) {
		this.productId = StringUtils.trim(productId);
	}


	public String getStationId() {
		return StringUtils.trim(stationId);	}


	public void setStationId(String stationId) {
		this.stationId = StringUtils.trim(stationId);
	}


	public Timestamp getReferenceTimestamp() {
		return referenceTimestamp;
	}


	public void setReferenceDate(Timestamp referenceTimestamp) {
		this.referenceTimestamp = referenceTimestamp;
	}


	public String getAssociateNo() {
		return StringUtils.trim(associateNo);
	}


	public void setAssociateNo(String associateNo) {
		this.associateNo = StringUtils.trim(associateNo);
	}


	public Integer getSequenceNumber() {
		return sequenceNumber;
	}


	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}


	public String getSourceSystemId() {
		return StringUtils.trim(sourceSystemId);
	}


	public void setSourceSystemId(String sourceSystemId) {
		this.sourceSystemId = StringUtils.trim(sourceSystemId);
	}


	public String getProductType() {
		return StringUtils.trim(productType);
	}


	public void setProductType(String productType) {
		this.productType = StringUtils.trim(productType);
	}

	public ProductSequence createProductSequence()  {
		ProductSequence newSeq = new ProductSequence();
		ProductSequenceId id = new ProductSequenceId();
		id.setProcessPointId(getStationId());
		id.setProductId(getProductId());
		newSeq.setId(id);
		newSeq.setReferenceTimestamp(getReferenceTimestamp());
		newSeq.setProductType(getProductType());
		newSeq.setSourceSystemId(getSourceSystemId());
		newSeq.setSequenceNumber(getSequenceNumber());
		newSeq.setAssociateNo(getAssociateNo());
		return newSeq;
	}
	
	public static ProductSequenceDto createMeFromEntity(ProductSequence prodSeq)  {
		ProductSequenceDto dto = new ProductSequenceDto();
		dto.setAssociateNo(prodSeq.getAssociateNo());
		dto.setProductId(prodSeq.getId().getProductId());
		dto.setStationId(prodSeq.getId().getProcessPointId());
		dto.setProductType(prodSeq.getProductType());
		dto.setReferenceDate(prodSeq.getReferenceTimestamp());
		dto.setSequenceNumber(prodSeq.getSequenceNumber());
		dto.setSourceSystemId(prodSeq.getSourceSystemId());
		return dto;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((productType == null) ? 0 : productType.hashCode());
		result = prime * result + ((referenceTimestamp == null) ? 0 : referenceTimestamp.hashCode());
		result = prime * result + ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result + ((sourceSystemId == null) ? 0 : sourceSystemId.hashCode());
		result = prime * result + ((stationId == null) ? 0 : stationId.hashCode());
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
		ProductSequenceDto other = (ProductSequenceDto) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (productType == null) {
			if (other.productType != null)
				return false;
		} else if (!productType.equals(other.productType))
			return false;
		if (referenceTimestamp == null) {
			if (other.referenceTimestamp != null)
				return false;
		} else if (!referenceTimestamp.equals(other.referenceTimestamp))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (sourceSystemId == null) {
			if (other.sourceSystemId != null)
				return false;
		} else if (!sourceSystemId.equals(other.sourceSystemId))
			return false;
		if (stationId == null) {
			if (other.stationId != null)
				return false;
		} else if (!stationId.equals(other.stationId))
			return false;
		return true;
	}
	

}
