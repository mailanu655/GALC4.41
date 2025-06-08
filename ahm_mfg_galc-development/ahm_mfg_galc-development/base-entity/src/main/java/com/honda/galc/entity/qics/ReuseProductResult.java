package com.honda.galc.entity.qics;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>ReuseProductResult Class description</h3>
 * <p> ReuseProductResult description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Jun 3, 2012
 *
 *
 */
@Entity
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
		return this.reuseVinReason;
	}

	public void setReuseVinReason(String reuseVinReason) {
		this.reuseVinReason = reuseVinReason;
	}


}
