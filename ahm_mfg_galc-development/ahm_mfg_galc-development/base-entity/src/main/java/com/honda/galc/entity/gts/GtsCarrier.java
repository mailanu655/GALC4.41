package com.honda.galc.entity.gts;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.GtsCarrierStatus;
import com.honda.galc.entity.enumtype.GtsCarrierType;

/**
 * 
 * 
 * <h3>GtsCarrier Class description</h3>
 * <p> GtsCarrier description </p>
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
 * Nov 14, 2014
 *
 *
 */
@Entity
@Table(name="GTS_CARRIER_TBX")
public class GtsCarrier extends AuditEntry {
	@EmbeddedId
	private GtsCarrierId id;

	@Column(name="PRODUCT_ID")
	private String productId;

	@Column(name="CARRIER_TYPE")
	private int carrierType = 0;

	private int status = 0;
	
	@Transient
	private GtsProduct product;

	private static final long serialVersionUID = 1L;
	
	public static final String UNKNOWN ="UNKNOWN";

	public GtsCarrier(String area,String label){
		id = new GtsCarrierId(area, label);
    }
	
	
	public GtsCarrier() {
		super();
	}
	
	public GtsCarrier(String label) {
		this("",label);
	}

	public GtsCarrierId getId() {
		return this.id;
	}

	public void setId(GtsCarrierId id) {
		this.id = id;
	}
	
	public String getCarrierNumber() {
		return getId().getCarrierNumber();
	}

	public String getProductId() {
		return StringUtils.trim(this.productId);
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getCarrierTypeValue() {
		return this.carrierType;
	}
	
	public GtsCarrierType getCarrierType() {
		return GtsCarrierType.getType(carrierType);
	}

	public void setCarrierTypeValue(int carrierType) {
		this.carrierType = carrierType;
	}
	
	public void setCarrierType(GtsCarrierType carrierType) {
		this.carrierType = carrierType.getId();
	}
	
	public boolean isNormal(){
        return getCarrierType() == GtsCarrierType.NORMAL;
    }
    
    public boolean isBadCarrier(){
        return getCarrierType() == GtsCarrierType.BAD;
    }
    
    public boolean isEmptyCarrier(){
        return getCarrierType() == GtsCarrierType.EMPTY || this.product == null;
    }
    
    public boolean isScrap(){
        return getCarrierType() == GtsCarrierType.SCRAP;
    }
    
	public int getStatusValue() {
		return this.status;
	}
	
	public GtsCarrierStatus getStatus() {
		return GtsCarrierStatus.getType(status);
	}
	
	public String getStatusText(){
		
		GtsCarrierStatus carrierStatus = getStatus();
		
		return carrierStatus == null ? "" : carrierStatus.getDisplayName();
    }

	public void setStatusValue(int status) {
		this.status = status;
	}
	
	public void setStatus(GtsCarrierStatus status) {
		this.status = status.getId();
	}
	
	public GtsProduct getProduct() {
		return product;
	}

	public void setProduct(GtsProduct product) {
		this.product = product;
	}

	public boolean isUnknownCarrier() {
		return UNKNOWN.equalsIgnoreCase(id.getCarrierNumber());
	}
	
	public String toString() {
		return toString(getId().getTrackingArea(),getId().getCarrierNumber(),getProductId());
	}

}
