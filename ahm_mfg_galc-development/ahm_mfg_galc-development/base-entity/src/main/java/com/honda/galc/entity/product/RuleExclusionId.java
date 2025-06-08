/**
 * 
 */
package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * @author vf031824
 *
 */
@Embeddable
public class RuleExclusionId  implements Serializable{

	@Column(name = "PART_NAME")
	private String partName = "";
	
	@Column(name = "PRODUCT_SPEC_CODE")
	private String productSpecCode = "";
	
    private static final long serialVersionUID = 1L;

    public RuleExclusionId() {
    	super();
    }
    
    public String getPartName() {
    	return StringUtils.trim(this.partName);
    }
    
    public void setPartName(String partName) {
    	this.partName = partName;
    }
    
    public String getProductSpecCode() {
    	return StringUtils.trim(this.productSpecCode);
    }
    
    public void setProductSpecCode(String productSpecCode) {
    	this.productSpecCode = productSpecCode;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.partName.hashCode();
        hash = hash * prime + this.productSpecCode.hashCode();
        return hash;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RuleExclusionId)) {
            return false;
        }
        RuleExclusionId other = (RuleExclusionId) o;
        return this.partName.equals(other.partName)
                && this.productSpecCode.equals(other.productSpecCode);
    }
}