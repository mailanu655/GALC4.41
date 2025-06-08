package com.honda.galc.entity.product;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Entity
@Table(name = "GAL176TBX")
public class InProcessProduct extends AuditEntry {
    @Id
    @Column(name = "PRODUCT_ID")
    private String productId;

    @Column(name = "LINE_ID")
    private String lineId;

    @Column(name = "NEXT_PRODUCT_ID")
    private String nextProductId;

    @Column(name = "PRODUCT_SPEC_CODE")
    private String productSpecCode;

    @Column(name = "PRODUCTION_LOT")
    private String productionLot;

    @Column(name = "PLAN_OFF_DATE")
    private Date planOffDate;

    @Column(name = "LAST_PASSING_PROCESS_POINT_ID")
    private String lastPassingProcessPointId;

    private static final long serialVersionUID = 1L;

    public InProcessProduct() {
        super();
    }

    public InProcessProduct(BaseProduct product) {
		this.productId = product.getProductId();
		this.productSpecCode = product.getProductSpecCode();
		this.productionLot = product.getProductionLot();
		if (product instanceof Product) {
			Product p = (Product) product;
			if (p.getPlanOffDate() != null) {
				this.planOffDate = new Date(p.getPlanOffDate().getTime());
			}
		}
	}    
    
	public String getProductId() {
        return StringUtils.trim(this.productId);
    }
	
	public String getId() {
		return getProductId();
	}

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLineId() {
        return StringUtils.trim(this.lineId);
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getNextProductId() {
        return StringUtils.trim(this.nextProductId);
    }

    public void setNextProductId(String nextProductId) {
        this.nextProductId = nextProductId;
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

    public Date getPlanOffDate() {
        return this.planOffDate;
    }

    public void setPlanOffDate(Date planOffDate) {
        this.planOffDate = planOffDate;
    }

    public String getLastPassingProcessPointId() {
        return StringUtils.trim(this.lastPassingProcessPointId);
    }

    public void setLastPassingProcessPointId(String lastPassingProcessPointId) {
        this.lastPassingProcessPointId = lastPassingProcessPointId;
    }

	@Override
	public String toString() {
		return toString(getProductId(), getLineId(), getNextProductId(),
				getProductSpecCode(), getProductionLot(), getPlanOffDate(),
				getLastPassingProcessPointId());
	}
}
