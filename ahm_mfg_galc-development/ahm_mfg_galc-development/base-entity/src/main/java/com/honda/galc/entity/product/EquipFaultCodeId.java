package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>EquipFaultCodeId Class description</h3>
 * <p> EquipFaultCodeId description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Dec 17, 2012
 *
 *
 */
@Embeddable
public class EquipFaultCodeId implements Serializable {
	@Column(name="UNIT_ID")
	private short unitId;

	@Column(name="FAULT_CODE")
	private int faultCode;

	private static final long serialVersionUID = 1L;

	public EquipFaultCodeId() {
		super();
	}

	public short getUnitId() {
		return this.unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	public int getFaultCode() {
		return this.faultCode;
	}

	public void setFaultCode(int faultCode) {
		this.faultCode = faultCode;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof EquipFaultCodeId)) {
			return false;
		}
		EquipFaultCodeId other = (EquipFaultCodeId) o;
		return (this.unitId == other.unitId)
			&& (this.faultCode == other.faultCode);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.unitId);
		hash = hash * prime + this.faultCode;
		return hash;
	}

}
