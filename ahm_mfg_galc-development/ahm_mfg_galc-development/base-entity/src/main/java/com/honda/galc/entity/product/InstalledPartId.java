package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.device.Tag;
import com.honda.galc.dto.RestData;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class InstalledPartId implements Serializable {
	
	@RestData()
    @Tag(name="PRODUCT_ID", alt="ENGINE_SN")
    @Column(name = "PRODUCT_ID")
    private String productId;

	@RestData()
    @Column(name = "PART_NAME")
    private String partName;

    private static final long serialVersionUID = 1L;

    public InstalledPartId() {
        super();
    }
    
    public InstalledPartId(String productId, String partName) {
        super();
        this.productId = productId;
        this.partName = partName;
    }

    public String getProductId() {
        return StringUtils.trim(this.productId);
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPartName() {
        return StringUtils.trim(this.partName);
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof InstalledPartId)) {
            return false;
        }
        InstalledPartId other = (InstalledPartId) o;
        return this.getProductId().equals(other.getProductId())
                && this.getPartName().equals(other.getPartName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.productId.hashCode();
        hash = hash * prime + this.partName.hashCode();
        return hash;
    }

}
