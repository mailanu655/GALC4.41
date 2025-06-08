package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.UserAuditEntry;

import java.io.Serializable;

/** * * 
* @version 0.2 
* @author Gangadhararao Gadde 
* @since Aug 09, 2012
*/
@Embeddable
public class LotControlRuleId implements Ymtoc, Serializable {
    @Column(name = "PROCESS_POINT_ID")
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private String processPointId;

    @Column(name = "MODEL_YEAR_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=2)
    private String modelYearCode = "";

    @Column(name = "MODEL_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=3)
    private String modelCode = "";

    @Column(name = "MODEL_TYPE_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=4)
    private String modelTypeCode = "";

    @Column(name = "MODEL_OPTION_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=5)
    private String modelOptionCode = "";

    @Column(name = "INT_COLOR_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=6)
    private String intColorCode = "";

    @Column(name = "EXT_COLOR_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=7)
    private String extColorCode = "";

    @Column(name = "PART_NAME")
    @Auditable(isPartOfPrimaryKey= true,sequence=8)
    private String partName;
    
    @Column(name="PRODUCT_SPEC_CODE")
    @Auditable(isPartOfPrimaryKey= true,sequence=9)
	private String productSpecCode = "";

	private static final long serialVersionUID = 1L;

    public LotControlRuleId() {
        super();
    }
    
    public LotControlRuleId(ProductSpec productSpec) {
        super();
        this.setModelYearCode(productSpec.getModelYearCode());
		this.setModelCode(productSpec.getModelCode());
		this.setModelTypeCode(productSpec.getModelTypeCode());
		this.setModelOptionCode(productSpec.getModelOptionCode());
    }
    
    public LotControlRuleId(FrameSpec frameSpec) {
        this((ProductSpec)frameSpec);
        this.setExtColorCode(frameSpec.getExtColorCode());
        this.setIntColorCode(frameSpec.getIntColorCode());
    }
    
    public LotControlRuleId(Mbpn mbpn) {
    	super();
        this.setProductSpecCode(mbpn.getProductSpecCode());
    }

    public LotControlRuleId(String name, String processPointId) {
		this.partName = name;
		this.processPointId = processPointId;
	}

	public String getProcessPointId() {
        return StringUtils.trim(this.processPointId);
    }

    public void setProcessPointId(String processPointId) {
        this.processPointId = processPointId;
    }

    public String getModelYearCode() {
        return StringUtils.trim(this.modelYearCode);
    }

    public void setModelYearCode(String modelYearCode) {
        this.modelYearCode = modelYearCode;
    }

    public String getModelCode() {
        return StringUtils.trim(this.modelCode);
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelTypeCode() {
    	return StringUtils.stripEnd(this.modelTypeCode, null);
    }

    public void setModelTypeCode(String modelTypeCode) {
        this.modelTypeCode = modelTypeCode;
    }

    public String getModelOptionCode() {
        return StringUtils.trim(this.modelOptionCode);
    }

    public void setModelOptionCode(String modelOptionCode) {
        this.modelOptionCode = modelOptionCode;
    }

    public String getIntColorCode() {
        return StringUtils.trim(this.intColorCode);
    }

    public void setIntColorCode(String intColorCode) {
        this.intColorCode = intColorCode;
    }

    public String getExtColorCode() {
        return StringUtils.trim(this.extColorCode);
    }

    public void setExtColorCode(String extColorCode) {
        this.extColorCode = extColorCode;
    }

    public String getPartName() {
        return StringUtils.trim(this.partName);
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }   

    public String getProductSpecCode() {
		return StringUtils.trim(productSpecCode);
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}
	
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LotControlRuleId)) {
            return false;
        }
        LotControlRuleId other = (LotControlRuleId) o;
        return this.getProcessPointId().equals(other.getProcessPointId())
                && this.getModelYearCode().equals(other.getModelYearCode())
                && this.getModelCode().equals(other.getModelCode())
                && this.getModelTypeCode().equals(other.getModelTypeCode())
                && this.getModelOptionCode().equals(other.getModelOptionCode())
                && this.getIntColorCode().equals(other.getIntColorCode())
                && this.getExtColorCode().equals(other.getExtColorCode())
                && this.getPartName().equals(other.getPartName())
                && this.getProductSpecCode().equals(other.getProductSpecCode());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.processPointId.hashCode();
        hash = hash * prime + this.modelYearCode.hashCode();
        hash = hash * prime + this.modelCode.hashCode();
        hash = hash * prime + this.modelTypeCode.hashCode();
        hash = hash * prime + this.modelOptionCode.hashCode();
        hash = hash * prime + this.intColorCode.hashCode();
        hash = hash * prime + this.extColorCode.hashCode();
        hash = hash * prime + this.partName.hashCode();
        hash = hash * prime + this.productSpecCode.hashCode();
        return hash;
    }
    
    public String getYMTO() {
    	if(!StringUtils.isEmpty(getProductSpecCode()))
			return getProductSpecCode();
		else
	    	return getModelYearCode()+" " +
	    	       getModelCode()+ " " +
	    	       getModelTypeCode()+ " " +
	    	       getModelOptionCode()+ " " +
	    	       getExtColorCode()+ " " + 
	    	       getIntColorCode();
    }
    
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append(processPointId).append(",");
    	builder.append(modelYearCode).append(modelCode).append(modelTypeCode)
    	       .append(modelOptionCode).append(intColorCode).append(extColorCode);
    	builder.append(",").append(partName);
    	builder.append(",").append(productSpecCode);
    	return builder.toString();
    }
    
    
    public LotControlRuleId clone(){
    	LotControlRuleId clone = new LotControlRuleId();
    	clone.setProcessPointId(this.processPointId);
    	clone.setModelYearCode(this.getModelYearCode());
    	clone.setModelCode(this.getModelCode());
    	clone.setModelTypeCode(this.getModelTypeCode());
    	clone.setModelOptionCode(this.getModelOptionCode());
    	clone.setExtColorCode(this.getExtColorCode());
    	clone.setIntColorCode(this.getIntColorCode());
    	clone.setPartName(this.getPartName());
    	clone.setProductSpecCode(this.productSpecCode);
    	return clone;
    }
 
}
