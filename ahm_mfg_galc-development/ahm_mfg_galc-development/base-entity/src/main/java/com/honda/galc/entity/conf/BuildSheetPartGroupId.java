package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.dto.Auditable;

@Embeddable
public class BuildSheetPartGroupId implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Column(name = "ATTRIBUTE")
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private String attribute;
    
    @Column(name = "FORM_ID")
    @Auditable(isPartOfPrimaryKey= true,sequence=2)
    private String formId;
    
    @Column(name = "MODEL_GROUP")
    @Auditable(isPartOfPrimaryKey= true,sequence=3)
    private String modelGroup;
    
    public BuildSheetPartGroupId() {
        super();
    }

	public BuildSheetPartGroupId(String attribute, String formId, String modelGroup) {
    	this.attribute = attribute;
    	this.formId = formId;
    	this.modelGroup = modelGroup;
    }

    public String getAttribute() {
		return StringUtils.trimToEmpty(attribute);
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getFormId() {
		return StringUtils.trimToEmpty(formId);
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getModelGroup() {
		return StringUtils.trimToEmpty(modelGroup);
	}

	public void setModelGroup(String modelGroup) {
		this.modelGroup = modelGroup;
	}

	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BuildSheetPartGroupId)) {
            return false;
        }
        BuildSheetPartGroupId other = (BuildSheetPartGroupId) o;
        return this.getAttribute().equals(other.getAttribute())
                && this.getFormId().equals(other.getFormId())
                && this.getModelGroup().equals(other.getModelGroup());
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.attribute.hashCode();
        hash = hash * prime + this.formId.hashCode();
        hash = hash * prime + this.modelGroup.hashCode();
        return hash;
    }
    
    public String toString() {
        return attribute + "," + formId + "," + modelGroup;
    }

}
