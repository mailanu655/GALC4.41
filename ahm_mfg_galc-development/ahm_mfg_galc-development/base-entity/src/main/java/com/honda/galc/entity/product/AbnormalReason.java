package com.honda.galc.entity.product;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name="ABNORMAL_REASON_TBX")
public class AbnormalReason extends AuditEntry {

	@EmbeddedId
	private AbnormalReasonId id;

	@Column(name="ABNORMAL_REASON")
	private String abnormalReason;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="ACTUAL_TIMESTAMP")
	private Timestamp actualTimestamp;

	private static final long serialVersionUID = 1L;

	public AbnormalReason() {
		super();
	}

	public AbnormalReason(String productId, String abnormalType, String associateNo) {
		this(productId, abnormalType, null, associateNo, null);
	}

	public AbnormalReason(String productId, String abnormalType, String abnormalReason, String associateNo) {
		this(productId, abnormalType, abnormalReason, associateNo, null);
	}

	public AbnormalReason(String productId, String abnormalType, String associateNo, Timestamp actualTimestamp) {
		this(productId, abnormalType, null, associateNo, actualTimestamp);
	}

	public AbnormalReason(String productId, String abnormalType, String abnormalReason, String associateNo, Timestamp actualTimestamp) {
		this.id = new AbnormalReasonId(productId, abnormalType);
		this.abnormalReason = abnormalReason;
		this.associateNo = associateNo;
		this.actualTimestamp = actualTimestamp;
	}

	public AbnormalReasonId getId() {
		return this.id;
	}

	public String getProductId() {
		return this.id.getProductId();
	}

	public void setProductId(String productId) {
		this.id.setProductId(productId);
	}

	public String getAbnormalType() {
		return this.id.getAbnormalType();
	}

	public void setAbnormalType(String abnormalType) {
		this.id.setAbnormalType(abnormalType);
	}

	public String getAbnormalReason() {
		return StringUtils.trim(this.abnormalReason);
	}

	public void setAbnormalReason(String abnormalReason) {
		this.abnormalReason = abnormalReason;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public Timestamp getActualTimestamp() {
		return this.actualTimestamp;
	}

	public void setActualTimestamp(Timestamp actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}

	@Override
	public String toString() {
		return toString(getProductId(),
				getAbnormalType(),
				getAbnormalReason(),
				getAssociateNo(),
				getActualTimestamp());
	}
}
