package com.honda.galc.entity.product;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.conf.ProcessPoint;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL215TBX")
public class ProductResult extends ProductHistory {
	
    @EmbeddedId
    private ProductResultId id;

    @Column(name = "PRODUCT_SPEC_CODE")
    private String productSpecCode;

    @Column(name = "PRODUCTION_LOT")
    private String productionLot;
    
    @Column(name = "APPROVER_NO", length=11)
    private String approverNo;
    
    private static final long serialVersionUID = 1L;

    public ProductResult() {
        super();
    }
  
    public ProductResult(String productId, String processPointId, Timestamp actualTimestamp) {
    	super();
    	this.id = new ProductResultId();
    	this.id.setProductId(productId);
    	this.id.setProcessPointId(processPointId);
    	this.id.setActualTimestamp(actualTimestamp);
    }

    public ProductResult(String productId, String processPointId) {
    	this(productId, processPointId, null);
    }
    
    public ProductResult(String productId, String processPointId, String associateNo, String approverNo) {
    	this(productId, processPointId);
    	setAssociateNo(associateNo);
    	this.approverNo = approverNo;
    }
    
    public ProductResult(BaseProduct product, String processPointId) {
    	this(product.getProductId(), processPointId);
    	this.productionLot = product.getProductionLot();
    	this.productSpecCode = product.getProductSpecCode();
    }

    public ProductResult(BaseProduct product, ProcessPoint processPoint) {
    	this(product, processPoint.getProcessPointId());
	}

    public ProductResult(BaseProduct product, ProcessPoint processPoint, String associateNo, String approverNo) {
    	this(product, processPoint);
    	setAssociateNo(associateNo);
    	this.approverNo = approverNo;
	}
    
	public ProductResultId getId() {
        return this.id;
    }

    public void setId(ProductResultId id) {
        this.id = id;
    }

    public String getProductSpecCode() {
        return StringUtils.trim(this.productSpecCode);
    }

    public void setProductSpecCode(String productSpecCode) {
        this.productSpecCode = productSpecCode;
    }

    public String getProductionLot() {
        return StringUtils.trim(this.productionLot);
    }

    public void setProductionLot(String productionLot) {
        this.productionLot = productionLot;
    }

	@Override
	public String getProcessPointId() {
		return id.getProcessPointId();
	}
	
	@Override
	public void setProcessPointId(String processPointId) {
		if(id == null) id = new ProductResultId();
		id.setProcessPointId(processPointId);
	}

	@Override
	public String getProductId() {
		return id.getProductId();
	}
	
	@Override
	public void setProductId(String productId) {
		if(id == null) id = new ProductResultId();
		id.setProductId(productId);
	}

	@Override
	public Timestamp getActualTimestamp() {
		return id.getActualTimestamp();
	}

	@Override
	public void setActualTimestamp(Timestamp timestamp) {
		if(id == null) id = new ProductResultId();
		id.setActualTimestamp(timestamp);
	}
	
	@Override
	public String getApproverNo() {
		return StringUtils.trimToEmpty(this.approverNo);
	}
	
	@Override
	public void setApproverNo(String approverNo) {
		this.approverNo = approverNo;
	}

	@Override
	public String toString() {
		return toString(getProductId(), getProcessPointId(),
				getActualTimestamp(), getProductSpecCode(),
				getProductionDate(), getProductionLot(), getProcessCount(),
				getAssociateNo(), getApproverNo());
	}
}
