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
public class LetPartCheckSpecId implements Serializable {

	@Column(name = "PART_ID")
	private String partId;

	@Column(name = "PART_NAME")
	private String partName;

	@Column(name = "INSPECTION_PROGRAM_ID")
	private Integer inspectionProgramId;

	@Column(name = "INSPECTION_PARAM_ID")
	private Integer inspectionParamId;

	private static final long serialVersionUID = 1L;

	public LetPartCheckSpecId() {
		super();
	}

	public String getPartId() {
		return StringUtils.trim(this.partId);
	}

	public void setPartId(String partId) {
		this.partId = partId;
	}

	public String getPartName() {
		return StringUtils.trim(this.partName);
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Integer getInspectionProgramId() {
		return this.inspectionProgramId;
	}
	
	public void setInspectionProgramId(Integer inspectionProgramId) {
		this.inspectionProgramId = inspectionProgramId;
	}
	
	public Integer getInspectionParamId() {
		return this.inspectionParamId;
	}
	
	public void setInspectionParamId(Integer inspectionParamId) {
		this.inspectionParamId = inspectionParamId;
	}
	
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LetPartCheckSpecId)) {
            return false;
        }
        LetPartCheckSpecId other = (LetPartCheckSpecId) o;
        return (this.partId.equals(other.partId)
                && this.partName.equals(other.partName)
                && this.inspectionProgramId == (other.inspectionProgramId)
                && this.inspectionParamId == (other.inspectionParamId));
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 7;
        hash = hash * prime + this.partId.hashCode();
        hash = hash * prime + this.partName.hashCode();
        hash = hash * prime + inspectionProgramId;
        hash = hash * prime + inspectionParamId;
        return hash;
    }
}