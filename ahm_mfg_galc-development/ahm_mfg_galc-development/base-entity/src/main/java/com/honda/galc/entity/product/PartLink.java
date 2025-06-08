package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.dto.Auditable;

@Entity
@Table(name = "PART_LINK_TBX")
public class PartLink implements Serializable {
	
	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private PartLinkId id;
	
	private static final long serialVersionUID = 1L;
    	
	public PartLink() {
		super();		
	}
	
	public PartLink(PartLinkId id) {
		super();
		this.id = id;
	}

	public PartLinkId getId() {
		return this.id;
	}

	public void setId(PartLinkId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getId().getParentPartName()).append(",");
		builder.append(getId().getProductSpecCode()).append(",");
		builder.append(getId().getChildPartName()).append(",");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartLink other = (PartLink) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}