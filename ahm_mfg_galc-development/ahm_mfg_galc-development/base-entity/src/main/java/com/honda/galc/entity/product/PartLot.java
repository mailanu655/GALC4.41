package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;
import com.honda.galc.entity.enumtype.PartLotStatus;

@Entity
@Table(name="PART_LOT_TBX")
public class PartLot extends AuditEntry {
	@EmbeddedId
	private PartLotId id;

	@Column(name = "STATUS")
	private short statusId;
	
	@Column(name="STARTING_QUANTITY")
	private int startingQuantity;

	@Column(name="CURRENT_QUANTITY")
	private int currentQuantity;

	private String comment;
	

	private static final long serialVersionUID = 1L;

	public PartLot() {
		super();
	}

	public PartLot(String partSerialNumber, String partName) {
		this.id = new PartLotId(partSerialNumber, partName);
	}

	public PartLotId getId() {
		return this.id;
	}

	public void setId(PartLotId id) {
		this.id = id;
	}

	public short getStatusId() {
		return this.statusId;
	}

	public void setStatusId(short statusId) {
		this.statusId = statusId;
	}
	
	public PartLotStatus getStatus() {
		return PartLotStatus.getType(statusId);
	}

	public void setStatus(PartLotStatus status) {
		this.statusId = (short)status.getId();
	}
	

	public int getStartingQuantity() {
		return this.startingQuantity;
	}

	public void setStartingQuantity(int startingQuantity) {
		this.startingQuantity = startingQuantity;
	}

	public int getCurrentQuantity() {
		return this.currentQuantity;
	}

	public void setCurrentQuantity(int currentQuantity) {
		this.currentQuantity = currentQuantity;
	}
	
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
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
		final PartLot other = (PartLot) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
        sb.append("\"").append(getId().getPartSerialNumber()).append("\"");
        sb.append(",\"").append(getId().getPartName()).append("\"");
        sb.append(",\"").append(getId().getPartSerialNumber()).append("\"");
        
        return sb.toString();
        
        
		//return id.getPartSerialNumber();
	}

	
	
}
