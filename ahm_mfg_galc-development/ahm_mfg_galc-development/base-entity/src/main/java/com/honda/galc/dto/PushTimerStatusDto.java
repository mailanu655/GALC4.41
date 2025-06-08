package com.honda.galc.dto;

import java.util.Date;

import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Aug 4, 2015
 */
public class PushTimerStatusDto implements IDto {

	private static final long serialVersionUID = 1L;

	@DtoTag()
	private int seqNumber = -1;

	@DtoTag()
	private String processPointId = null;
	
	@DtoTag()
	private String productId = null;
	
	@DtoTag()
	private String associateNo = null;
	
	@DtoTag()
	private long statusInSecs = -1L;
	
	@DtoTag()
	private int opsPlanned = -1;

	@DtoTag()
	private int opsCompleted = -1;
	
	@DtoTag()
	private Date lastUpdated = null;

	@DtoTag()
	private String divisionId = null;
	
	public PushTimerStatusDto() {}
	
	public int getSeqNumber() {
		return seqNumber;
	}

	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	public String getProcessPointId() {
		return processPointId;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public String getAssociateNo() {
		return associateNo;
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}
	
	public long getStatusInSecs() {
		return statusInSecs;
	}

	public void setStatusInSecs(long statusInSecs) {
		this.statusInSecs = statusInSecs;
	}

	public void setOpsCompleted(int opsCompleted) {
		this.opsCompleted = opsCompleted;
	}

	public int getOpsPlanned() {
		return opsPlanned;
	}
	
	public int getOpsCompleted() {
		return opsCompleted;
	}

	public void setOpsPlanned(int opsPlanned) {
		this.opsPlanned = opsPlanned;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result
				+ ((divisionId == null) ? 0 : divisionId.hashCode());
		result = prime * result
				+ ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + opsCompleted;
		result = prime * result + opsPlanned;
		result = prime * result
				+ ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + seqNumber;
		result = prime * result + (int) (statusInSecs ^ (statusInSecs >>> 32));
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
		PushTimerStatusDto other = (PushTimerStatusDto) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (divisionId == null) {
			if (other.divisionId != null)
				return false;
		} else if (!divisionId.equals(other.divisionId))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (opsCompleted != other.opsCompleted)
			return false;
		if (opsPlanned != other.opsPlanned)
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
		if (seqNumber != other.seqNumber)
			return false;
		if (statusInSecs != other.statusInSecs)
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
