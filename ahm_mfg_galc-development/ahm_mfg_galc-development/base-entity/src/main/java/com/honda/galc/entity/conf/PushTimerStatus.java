package com.honda.galc.entity.conf;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Subu Kathiresan
 * @date Jul 31, 2015
 */
@Entity
@Table(name="PUSH_TIMER_STATUS_TBX")
public class PushTimerStatus extends AuditEntry {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PushTimerStatusId id;
	
	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="ASSOCIATE_NO")
	private String associateNo;
	
	@Column(name="OPS_PLANNED")
	private int opsPlanned;
	
	@Column(name="OPS_COMPLETED")
	private int opsCompleted;

	@Column(name="STATUS_IN_SECS")
	private long statusInSecs;
	
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
    
    public PushTimerStatus() {
		super();
	}

	public PushTimerStatus(PushTimerStatusId id) {
    	this.id = id;
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
	
	public int getOpsPlanned() {
		return opsPlanned;
	}

	public void setOpsPlanned(int opsPlanned) {
		this.opsPlanned = opsPlanned;
	}

	public int getOpsCompleted() {
		return opsCompleted;
	}

	public void setOpsCompleted(int opsCompleted) {
		this.opsCompleted = opsCompleted;
	}

	public long getStatusInSecs() {
		return statusInSecs;
	}

	public void setStatusInSecs(long statusInSecs) {
		this.statusInSecs = statusInSecs;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public PushTimerStatusId getId() {
		return id;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((associateNo == null) ? 0 : associateNo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + opsCompleted;
		result = prime * result + opsPlanned;
		result = prime * result
				+ ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + (int) (statusInSecs ^ (statusInSecs >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PushTimerStatus other = (PushTimerStatus) obj;
		if (associateNo == null) {
			if (other.associateNo != null)
				return false;
		} else if (!associateNo.equals(other.associateNo))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (statusInSecs != other.statusInSecs)
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
}
	