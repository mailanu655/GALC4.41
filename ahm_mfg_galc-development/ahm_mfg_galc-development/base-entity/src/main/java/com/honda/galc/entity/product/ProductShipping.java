package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * 
 * <h3>ProductShipping Class description</h3>
 * <p> ProductShipping description </p>
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
 * Sep 10, 2014
 *
 *
 */

@Entity
@Table(name="PRODUCT_SHIPPING_TBX")
public class ProductShipping extends AuditEntry {
	private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProductShippingId id;

	@Column(name="PRODUCT_TYPE")
	private String productType;

	@Column(name="DUNNAGE")
	private String dunnage;

	@Column(name="SHIP_DATE")
	private Date shipDate;

	@Column(name="TRAILER_STATUS")
	private int trailerStatus = 0;
	
	@Column(name="TRACKING_STATUS")
	private String trackingStatus;
	

	public String getTrackingStatus() {
		return StringUtils.trim(trackingStatus);
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	@Transient
	private int count = 0;
	
	public ProductShipping() {
		super();
	}

	public String getProductTypeString() {
		return StringUtils.trim(productType);
	}
	
	public String getProductType() {
		return productType;
	}

	public void setProductTypeString(String productType) {
		this.productType = productType;
	}


	public String getDunnage() {
		return StringUtils.trim(dunnage);
	}

	public void setDunnage(String dunnage) {
		this.dunnage = dunnage;
	}

	public Date getShipDate() {
		return shipDate;
	}

	public void setShipDate(Date shipDate) {
		this.shipDate = shipDate;
	}

	public int getTrailerStatus() {
		return trailerStatus;
	}
	
	public String getProductId() {
		return getId().getProductId();
	}

	/**
	 * trailer status field is reused for storing count in certain query
	 * @return
	 */
	public int getCount(){
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}

	public void setTrailerStatus(int trailerStatus) {
		this.trailerStatus = trailerStatus;
	}

	public ProductShippingId getId() {
		return id;
	}

	public void setId(ProductShippingId id) {
		this.id = id;
	}

}
