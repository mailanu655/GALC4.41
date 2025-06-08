package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.Auditable;
import com.honda.galc.entity.AuditEntry;

@Entity
@Table(name = "MODEL_TYPE_HOLD_TBX")
public class ModelTypeHold extends AuditEntry{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	@Auditable(isPartOfPrimaryKey= true,sequence=1)
	private ModelTypeHoldId id;
	
	@Column(name = "PRODUCTION_LOT", nullable=false)
	private String productionLot;
	
	@Column(name = "KD_LOT_NUMBER", nullable=false)
	private String kdLotNumber;

	@Override
	public ModelTypeHoldId getId() {
		return this.id;
	}

	public String getProductionLot() {
		return StringUtils.trim(productionLot);
	}

	public void setProductionLot(String productionLot) {
		this.productionLot = productionLot;
	}

	public String getKdLotNumber() {
		return StringUtils.trim(kdLotNumber);
	}

	public void setKdLotNumber(String kdLotNumber) {
		this.kdLotNumber = kdLotNumber;
	}

	public void setId(ModelTypeHoldId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((productionLot == null) ? 0 : productionLot.hashCode());
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
		ModelTypeHold other = (ModelTypeHold) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (productionLot == null) {
			if (other.productionLot != null)
				return false;
		} else if (!productionLot.equals(other.productionLot))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ModelTypeHold [id=" + id + ", productionLot=" + productionLot + ", kdLotNumber=" + kdLotNumber + "]";
	}
	
	
}
