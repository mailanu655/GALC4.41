package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;

import java.io.Serializable;

@Embeddable
public class PartId implements Serializable {
    @Column(name = "PART_ID")
    @Auditable(isPartOfPrimaryKey= true,sequence=1)
    private String partId;

    @Column(name = "PART_NAME")
    @Auditable(isPartOfPrimaryKey= true,sequence=2)
    private String partName;

    private static final long serialVersionUID = 1L;

    public PartId() {
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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartId)) {
            return false;
        }
        PartId other = (PartId) o;
        return this.partId.equals(other.partId)
                && this.partName.equals(other.partName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.partId.hashCode();
        hash = hash * prime + this.partName.hashCode();
        return hash;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(StringUtils.trim(partName)).append(",");
		builder.append(StringUtils.trim(partId));
		return builder.toString();
	}
    
    

}
