package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "PART_SHIPMENT_PRODUCTS_TBX")
public class PartShipmentProduct extends AuditEntry{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private PartShipmentProductId id;
	
	@Column(name = "BUILD_STATUS")
    private Integer buildStatus;
    
    public PartShipmentProductId getId() {
		// TODO Auto-generated method stub
		return id;
	}
    
    public void setId(PartShipmentProductId id){
    	this.id = id;
    }
    
    public Integer getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(Integer buildStatus) {
		this.buildStatus = buildStatus;
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartShipmentProduct other = (PartShipmentProduct) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PartShipmentProduct [id=" + id + "]";
	}

    
}
