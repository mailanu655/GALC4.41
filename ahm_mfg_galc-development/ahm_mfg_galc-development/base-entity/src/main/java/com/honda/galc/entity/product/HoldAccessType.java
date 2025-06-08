package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "HOLD_ACCESS_TYPE_TBX")
public class HoldAccessType extends AuditEntry{
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private HoldAccessTypeId id;

    @Column(name = "DESCRIPTION")
    private String description;

    public HoldAccessType() {
        super();
    }

    public HoldAccessType(String typeId,String securityGroup){
    	this.id = new HoldAccessTypeId(typeId,securityGroup);
    }
    
    public HoldAccessType(String typeId, String securityGroup, String productType) {
    	this.id = new HoldAccessTypeId(typeId, securityGroup, productType);
    }

    public HoldAccessTypeId getId() {
        return this.id;
    }

    public void setId(HoldAccessTypeId id) {
        this.id = id;
    }

	public String getDescription() {
		return StringUtils.trim(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "HoldAccessType [id=" + id + ", description=" + description + "]";
	}

  

}
