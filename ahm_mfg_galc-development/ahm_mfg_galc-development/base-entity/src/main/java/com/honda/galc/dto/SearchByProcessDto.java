package com.honda.galc.dto;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

public class SearchByProcessDto {
	
	@DtoTag(name = "PRODUCT_ID")
	private String productId;
	
	@DtoTag(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(name = "ACTUAL_TIMESTAMP")
	private Timestamp timestamp;
	
	public String getProductId() {
		return StringUtils.trim(this.productId);
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getProcessPointId() {
		return StringUtils.trim(this.processPointId);
	}
	
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public Timestamp getTimestamp() {
		return this.timestamp;
	}
	
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		SearchByProcessDto other = (SearchByProcessDto) obj;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SearchByProcessDto [productId=" + productId + ", processPointId=" + processPointId + ", timestamp="
				+ timestamp + "]";
	}
}
