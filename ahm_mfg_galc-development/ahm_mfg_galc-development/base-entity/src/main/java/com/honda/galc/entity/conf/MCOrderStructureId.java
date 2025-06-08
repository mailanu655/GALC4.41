package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Fredrick Yessaian
 * @date Mar 04, 2015
 */

@Embeddable
public class MCOrderStructureId implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="ORDER_NO", nullable=false, length=20)
	private String orderNo;
	
	@Column(name="DIVISION_ID", nullable=false, length=16)
	private String divisionId;
	
	public MCOrderStructureId() {}
	
	public MCOrderStructureId(String orderNo, String divisionId) {
		this.setOrderNo(orderNo);
		this.setDivisionId(divisionId);
	}

	public String getOrderNo() {
		return StringUtils.trim(orderNo);
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getDivisionId() {
		return StringUtils.trim(divisionId);
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getOrderNo(), getDivisionId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result + ((orderNo == null) ? 0 : orderNo.hashCode());
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
		MCOrderStructureId other = (MCOrderStructureId) obj;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (orderNo == null) {
			if (other.orderNo != null)
				return false;
		} else if (!orderNo.equals(other.orderNo))
			return false;
		return true;
	}
	
	
	
}
