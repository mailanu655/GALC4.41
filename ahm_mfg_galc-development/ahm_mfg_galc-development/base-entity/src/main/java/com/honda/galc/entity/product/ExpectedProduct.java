package com.honda.galc.entity.product;

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
@Table(name = "GAL135TBX")
public class ExpectedProduct extends AuditEntry {
    @Id
    @Column(name = "PROCESS_POINT_ID")
    private String processPointId;

    @Column(name = "PRODUCT_ID")
    private String productId;
    
    @Column(name="LAST_PROCESSED_PRODUCT")
    private String lastProcessedProduct;

    private static final long serialVersionUID = 1L;

    public ExpectedProduct() {
        super();
    }

    public ExpectedProduct(String productId, String processPointId) {
		this.productId = productId;
		this.processPointId = processPointId;
	}

	public String getProcessPointId() {
        return StringUtils.trim(this.processPointId);
    }
    
    public String getId() {
    	return getProcessPointId();
    }

    public void setProcessPointId(String processPointId) {
        this.processPointId = processPointId;
    }

    public String getProductId() {
        return StringUtils.trim(this.productId);
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLastProcessedProduct() {
        return StringUtils.trim(this.lastProcessedProduct);
    }

    public void setLastProcessedProduct(String lastProcessedProduct) {
        this.lastProcessedProduct = lastProcessedProduct;
    }
    
	public void increaseProductId(int digit) {
		String prefix = productId.substring(0, productId.length() - digit);
		int sequenceNumber = Integer.parseInt(productId.substring(productId.length() - digit)) +1;
		String format ="%1$0" +  digit + "d";
		productId = prefix + String.format(format, sequenceNumber);
	}

	@Override
	public String toString() {
		return toString(getProcessPointId(),getProductId());
	}

}
