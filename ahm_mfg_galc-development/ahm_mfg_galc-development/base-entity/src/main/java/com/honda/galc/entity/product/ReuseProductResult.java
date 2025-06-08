package com.honda.galc.entity.product;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity(name="product_ReuseProductResult")
@Table(name="GAL224TBX")
public class ReuseProductResult extends AuditEntry {
	@EmbeddedId
	private ReuseProductResultId id;

	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="ASSOCIATE_NO")
	private String associateNo;

	@Column(name="REUSE_VIN_REASON")
	private String reuseVinReason;

	private static final long serialVersionUID = 1L;

	public ReuseProductResult() {
		super();
	}
	
	public ReuseProductResult(String productId,String associateNumber,String reason) {
		
		this.id = new ReuseProductResultId();
		id.setProductId(productId);
		long time = System.currentTimeMillis();
		id.setActualTimestamp(new Timestamp(time));
		setProductionDate(new Date(time));
		setAssociateNo(associateNumber);
		setReuseVinReason(reason);
		
	}
	
	public ReuseProductResultId getId() {
		return this.id;
	}

	public void setId(ReuseProductResultId id) {
		this.id = id;
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public String getAssociateNo() {
		return StringUtils.trim(this.associateNo);
	}

	public void setAssociateNo(String associateNo) {
		this.associateNo = associateNo;
	}

	public String getReuseVinReason() {
		return StringUtils.trim(this.reuseVinReason);
	}

	public void setReuseVinReason(String reuseVinReason) {
		this.reuseVinReason = reuseVinReason;
	}

	@Override
	public String toString() {
		return toString(id.getProductId(),getReuseVinReason(),getAssociateNo());
	}

}
