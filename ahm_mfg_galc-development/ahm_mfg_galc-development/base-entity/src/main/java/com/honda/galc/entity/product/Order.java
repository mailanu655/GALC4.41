package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * * *
 * 
 * @version 1
 * @author Gangadhararao Gadde,Subu Kathiresan
 * @since Nov 01, 2012
 * */

@Entity
@Table(name = "ORDER_TBX")
public class Order extends AuditEntry {

	@EmbeddedId
	private OrderId id;

	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode;

	@Column(name = "PRIORITY_SEQ")
	private Integer prioritySeq;

	@Column(name = "PROD_ORDER_QTY")
	private Integer prodOrderQty;

	@Column(name = "ORDER_STATUS_ID")
	private Integer orderStatusId;

	@Column(name = "HOLD_STATUS_ID")
	private Integer holdStatusId;

	@Column(name = "LOCATION_LEVEL_ID")
	private String locationLevelId;

	@Column(name = "PRIORITY_DATE")
	private Date priorityDate;

	private static final long serialVersionUID = 1L;

	public Order() {
		super();
	}

	public Order(OrderId id) {
		super();
		this.id = id;
	}

	public OrderId getId() {
		return id;
	}

	public void setId(OrderId id) {
		this.id = id;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public Integer getPrioritySeq() {
		return prioritySeq;
	}

	public void setPrioritySeq(Integer prioritySeq) {
		this.prioritySeq = prioritySeq;
	}

	public Integer getProdOrderQty() {
		return prodOrderQty;
	}

	public void setProdOrderQty(Integer prodOrderQty) {
		this.prodOrderQty = prodOrderQty;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getHoldStatusId() {
		return holdStatusId;
	}

	public void setHoldStatusId(Integer holdStatusId) {
		this.holdStatusId = holdStatusId;
	}

	public String getLocationLevelId() {
		return locationLevelId;
	}

	public void setLocationLevelId(String locationLevelId) {
		this.locationLevelId = locationLevelId;
	}

	public Date getPriorityDate() {
		return priorityDate;
	}

	public void setPriorityDate(Date priorityDate) {
		this.priorityDate = priorityDate;
	}
	
	public void setOrderNo(String orderNo) {
		if(id == null)
			id = new OrderId();
		
		id.setOrderNo(orderNo);
	}

	public void setPlanCode(String planCode) {
		if(id == null)
			id = new OrderId();
		
		id.setPlanCode(planCode);
	}
	
	
}
