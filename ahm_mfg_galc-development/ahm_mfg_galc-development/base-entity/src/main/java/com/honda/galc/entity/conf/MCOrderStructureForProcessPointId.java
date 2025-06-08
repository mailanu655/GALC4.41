package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Fredrick Yessaian
 * @date Mar 04, 2015
 * 
 */

@Embeddable
public class MCOrderStructureForProcessPointId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="ORDER_NO", nullable=false, length=20)
	private String orderNo;
	
	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	private String processPointId;
	
	public MCOrderStructureForProcessPointId() {}
	
	public MCOrderStructureForProcessPointId(String orderNo, String divisionId) {
		this.setOrderNo(orderNo);
		this.setProcessPointId(divisionId);
	}

	public String getOrderNo() {
		return StringUtils.trim(orderNo);
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getProcessPointId() {
		return StringUtils.trim(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getOrderNo(), getProcessPointId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
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
		MCOrderStructureForProcessPointId other = (MCOrderStructureForProcessPointId) obj;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		return true;
	}
	
}
