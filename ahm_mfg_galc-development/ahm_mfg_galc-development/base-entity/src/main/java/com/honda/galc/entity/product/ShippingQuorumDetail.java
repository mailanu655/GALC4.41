package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;

/**
 * 
 * <h3>ShippingQuorumDetail Class description</h3>
 * <p> ShippingQuorumDetail description </p>
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
 * Jun 1, 2012
 *
 *
 */
@Entity
@Table(name="QUORUM_DETAIL_TBX")
public class ShippingQuorumDetail extends AuditEntry {
	@EmbeddedId
	private ShippingQuorumDetailId id;

	@Column(name="KD_LOT")
	private String kdLot;

	private String ymto;

	@Column(name="ENGINE_NUMBER")
	private String engineNumber;

	@Column(name="STATUS")
	private int statusId;

	private static final long serialVersionUID = 1L;

	public ShippingQuorumDetail() {
		super();
	}
	
	public ShippingQuorumDetail(Date quorumDate, int quorumId,int quorumSeq) {
		this.id = new ShippingQuorumDetailId();
		id.setQuorumDate(quorumDate);
		id.setQuorumId(quorumId);
		id.setQuorumSeq(quorumSeq);
	}

	public ShippingQuorumDetailId getId() {
		return this.id;
	}

	public void setId(ShippingQuorumDetailId id) {
		this.id = id;
	}

	public String getKdLot() {
		return StringUtils.trim(this.kdLot);
	}

	public void setKdLot(String kdLot) {
		this.kdLot = kdLot;
	}

	public String getYmto() {
		return StringUtils.trim(this.ymto);
	}

	public void setYmto(String ymto) {
		this.ymto = ymto;
	}

	public String getEngineNumber() {
		return StringUtils.trim(this.engineNumber);
	}

	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}

	public int getStatusId() {
		return this.statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
	public ShippingQuorumDetailStatus getStatus() {
		return ShippingQuorumDetailStatus.getType(statusId);
	}
	
	public void setStatus(ShippingQuorumDetailStatus status) {
		this.statusId = status.getId();
	}
	
	public int getQuorumSeq() {
		return id.getQuorumSeq();
	}
	
	public String toString() {
		return toString(id.getQuorumDate(),id.getQuorumId(),id.getQuorumSeq(),getEngineNumber(), getStatus());
	}

}
