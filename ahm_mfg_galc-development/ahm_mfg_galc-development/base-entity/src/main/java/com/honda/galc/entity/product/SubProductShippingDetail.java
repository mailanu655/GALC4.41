package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.SubProductShippingDetailStatus;

@Entity
@Table(name="SUB_PRODUCT_SHIPPING_DETAIL_TBX")
public class SubProductShippingDetail extends AuditEntry {
	@EmbeddedId
	private SubProductShippingDetailId id;

	@Column(name="PRODUCT_ID")
	private String productId;
	
	@Column(name="STATUS")
	private Integer statusId = 0;
	
	@Column(name="CONTAINER_ID")
	private String containerId;
	
	@Column(name="PRODUCT_SPEC_CODE")
	private String productSpecCode;

	private static final long serialVersionUID = 1L;

	public SubProductShippingDetail() {
		super();
	}
	
	public SubProductShippingDetail(String kdLotNumber,String productionLot,String subId, int productSeqNo) {
		
		id = new SubProductShippingDetailId();
		id.setKdLotNumber(kdLotNumber);
		id.setProductionLot(productionLot);
		id.setSubId(subId);
		id.setProductSeqNo(productSeqNo);
		
	}

	public SubProductShippingDetailId getId() {
		return this.id;
	}

	public void setId(SubProductShippingDetailId id) {
		this.id = id;
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getKdLot() {
		return getId().getKdLotNumber();
	}
	
	public String getProductionLot() {
		return getId().getProductionLot();
	}

	public int getProductSeqNo() {
		return getId().getProductSeqNo();
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer status) {
		this.statusId = status;
	}
	
	public SubProductShippingDetailStatus getStatus() {
		return SubProductShippingDetailStatus.getType(statusId);
	}

	public void setStatus(SubProductShippingDetailStatus status) {
		this.statusId = status.getId();
	}
	
	public String getContainerId() {
		return StringUtils.trim(containerId);
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}
	
	public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
	public void setKdLotNumber(String kdLotNumber) {
		if(id == null)
			id = new SubProductShippingDetailId();
		
		this.id.setKdLotNumber(kdLotNumber);
	}

	public void setProductionLot(String productionLot) {
		if(id == null)
			id = new SubProductShippingDetailId();
		
		id.setProductionLot(productionLot);
	}

	public void setSubId(String subId) {
		if(id == null)
			id = new SubProductShippingDetailId();
		
		id.setSubId(subId);
	}

	public void setProductSeqNo(int productSeqNo) {
		if(id == null)
			id = new SubProductShippingDetailId();
		
		id.setProductSeqNo(productSeqNo);
	}

	
	
	

	public String toString() {
		return toString(getId().getKdLotNumber(),
				getId().getProductionLot(),
				getId().getSubId(),
				getId().getProductSeqNo(),
				getProductSpecCode(),
				getProductId());
	}

}
