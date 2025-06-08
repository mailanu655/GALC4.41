package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PlanStatus;

/**
 * * *
 * 
 * @version 1
 * @author Gangadhararao Gadde,Subu Kathiresan
 * @since Nov 01, 2012
 * 
 *        
 * */

@Entity
@Table(name = "PRODUCT_PRIORITY_PLAN_TBX")
public class ProductPriorityPlan extends AuditEntry {

	@EmbeddedId
	private ProductPriorityPlanId id;

	@Column(name = "ORDER_NO")
	private String orderNo;

	@Column(name = "PRODUCT_ID")
	private String productId;

	@Column(name = "PLAN_STATUS_ID")
	private Integer planStatusId;

	@Column(name = "PLAN_TIMESTAMP")
	private Date planTimestamp;

	@Column(name = "FORECAST_TIMESTAMP")
	private Date forecastTimestamp;
	
	@Column(name = "ACTUAL_TIMESTAMP")
	private Date actualTimestamp;
	
	@Column(name = "CONTAINER_ID")
	private String containerId;
	
	@Column(name = "CONTAINER_POS")
	private Integer containerPos;
	
	private static final long serialVersionUID = 1L;

	public ProductPriorityPlan() {
		super();
	}

	public ProductPriorityPlanId getId() {
		return id;
	}

	public void setId(ProductPriorityPlanId id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getPlanStatusId() {
		return planStatusId;
	}

	public void setPlanStatusId(Integer planStatusId) {
		this.planStatusId = planStatusId;
	}

    public PlanStatus getPlanStatus() {
    	return PlanStatus.getType(planStatusId);
    }

    public void setPlanStatus(PlanStatus planStatus) {
    	this.planStatusId = planStatus.getId();
    }
    
	public Date getPlanTimestamp() {
		return planTimestamp;
	}

	public void setPlanTimestamp(Date planTimestamp) {
		this.planTimestamp = planTimestamp;
	}

	public Date getForecastTimestamp() {
		return forecastTimestamp;
	}

	public void setForecastTimestamp(Date forecastTimestamp) {
		this.forecastTimestamp = forecastTimestamp;
	}

	public String getContainerId() {
		return containerId;
	}

	public void setContainerId(String containerId) {
		this.containerId = containerId;
	}

	public Integer getContainerPos() {
		return containerPos;
	}

	public void setContainerPos(Integer containerPos) {
		this.containerPos = containerPos;
	}

	public Date getActualTimestamp() {
		return actualTimestamp;
	}

	public void setActualTimestamp(Date actualTimestamp) {
		this.actualTimestamp = actualTimestamp;
	}
	
	public void setTrackingStatus(String trackingStatus) {
		if(id == null)
			id = new ProductPriorityPlanId();
		id.setTrackingStatus(trackingStatus);
	}

	public void setTrackingSequenceNo(Double trackingSequenceNo) {
		if(id == null)
			id = new ProductPriorityPlanId();
		id.setTrackingSequenceNo(trackingSequenceNo);
	}
}
