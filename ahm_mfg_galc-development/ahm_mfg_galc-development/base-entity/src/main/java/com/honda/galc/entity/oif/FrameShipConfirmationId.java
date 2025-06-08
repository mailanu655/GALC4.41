package com.honda.galc.entity.oif;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

@Embeddable
public class FrameShipConfirmationId implements Serializable{

	public FrameShipConfirmationId(){	super();	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PROCESS_POINT_ID")
	private String processPointId;
	
	@Column(name = "ENGINE_ID")
	private String engineId;
	
	@Column(name = "PRODUCT_ID")
	private String productId;
	
	/**
	 * @return the processPointId
	 */
	public String getProcessPointId() {
		return StringUtils.trim( processPointId );
	}
	/**
	 * @param processPointId the processPointId to set
	 */
	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	/**
	 * @return the engineId
	 */
	public String getEngineId() {
		return StringUtils.trim( engineId );
	}
	/**
	 * @param engineId the engineId to set
	 */
	public void setEngineId(String engineId) {
		this.engineId = engineId;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return StringUtils.trim( productId );
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((engineId == null) ? 0 : engineId.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FrameShipConfirmationId other = (FrameShipConfirmationId) obj;
		if (engineId == null) {
			if (other.engineId != null)
				return false;
		} else if (!engineId.equals(other.engineId))
			return false;
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
		return true;
	}
}
