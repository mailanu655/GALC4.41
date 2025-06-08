package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.SkippedProductStatus;

@Entity
@Table(name="SKIPPED_PRODUCT_TBX")
public class SkippedProduct extends AuditEntry {
	@EmbeddedId
	private SkippedProductId id;

	@Column(name="SUB_ID")
	private String subId;

	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="KD_LOT_NUMBER")
	private String kdLotNumber;

	@Column(name="PRODUCTION_LOT")
	private String productionLot;

	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name = "STATUS")
	private short statusId;

	@Column(name="SKIP_TIMESTAMP")
	private Timestamp skipTimestamp;

	@Column(name="DISABLE_TIMESTAMP")
	private Timestamp disableTimestamp;

	private static final long serialVersionUID = 1L;

	public SkippedProduct() {
		super();
	}
	
	public SkippedProduct(SkippedProductId id) {
		super();
		this.id = id;
	}

	public SkippedProductId getId() {
		return this.id;
	}

	public void setId(SkippedProductId id) {
		this.id = id;
	}

	public String getSubId() {
		return StringUtils.trim(this.subId);
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getProductType() {
		return  StringUtils.trim(this.productType);
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getKdLotNumber() {
		return  StringUtils.trim(this.kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public String getProductionLot() {
		return  StringUtils.trim(this.productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getProductSpecCode() {
		return  StringUtils.trim(this.productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public short getStatusId() {
		return this.statusId;
	}

	public void setStatusId(short statusId) {
		this.statusId = statusId;
	}
	
	public SkippedProductStatus getStatus() {
		return SkippedProductStatus.getType(statusId);
	}

	public void setStatus(SkippedProductStatus status) {
		this.statusId = (short)status.getId();
	}
	
	public Timestamp getSkipTimestamp() {
		return this.skipTimestamp;
	}

	public void setSkipTimestamp(Timestamp skipTimestamp) {
		this.skipTimestamp = skipTimestamp;
	}

	public Timestamp getDisableTimestamp() {
		return this.disableTimestamp;
	}

	public void setDisableTimestamp(Timestamp disableTimestamp) {
		this.disableTimestamp = disableTimestamp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getId().getProductId()).append("\"");
        sb.append(",\"").append(getId().getProcessPointId()).append("\"");
        sb.append(",\"").append(getStatus()).append("\"");
        
        return sb.toString();
	}
	
}
