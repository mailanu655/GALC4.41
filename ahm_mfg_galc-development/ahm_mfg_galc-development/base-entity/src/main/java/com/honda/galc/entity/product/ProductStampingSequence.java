package com.honda.galc.entity.product;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.ProductStampingSendStatus;


/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */
@Entity(name = "ProductStampingSequence")
@Table(name = "GAL216TBX")
public class ProductStampingSequence extends  AuditEntry {
    
    
	@EmbeddedId
    private ProductStampingSequenceId id;
    @Column(name = "STAMPING_SEQUENCE_NO")
    private Integer stampingSequenceNumber;
    @Column(name = "SEND_STATUS")
    private Integer sendStatus;
    private static final long serialVersionUID = 1L;
    
    public ProductStampingSequence(ProductStampingSequenceId id) {
		super();
		this.id = id;
	}
    
    public ProductStampingSequence() {
		super();
		
	}
    
    public Object getPrimaryKey() {
        return id;
    }
    public void setPrimaryKey(Object primaryKey) {
        this.id = (ProductStampingSequenceId) primaryKey;
    }
    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
    public Integer getStampingSequenceNumber() {
        return stampingSequenceNumber;
    }
    public void setStampingSequenceNumber(Integer stampingSequenceNumber) {
        this.stampingSequenceNumber = stampingSequenceNumber;
    }
    public ProductStampingSequenceId getId() {
        return id;
    }
    public void setId(ProductStampingSequenceId id) {
        this.id = id;
    }    
    
	@Override
	public String toString() {
		return toString(getId().getProductID(), getId().getProductionLot(), getSendStatus(), getStampingSequenceNumber());
	}

	public void setProductId(String productId) {
		if(id == null) id = new ProductStampingSequenceId();
		id.setProductID(productId);
		
	}
	
	
	public String getProductId() {
		return getId() != null ? getId().getProductID() : null;
	}

	public void setProductionLot(String productionLot) {
		if(id == null) id = new ProductStampingSequenceId();
		id.setProductionLot(productionLot);
		
	}
	public String getProductionLot() {
		return getId() != null ? getId().getProductionLot() : null;
	}

	public Boolean isLayoutBody() {
		return getSendStatus() == ProductStampingSendStatus.LAYOUT_BODY.getId();
	}
}