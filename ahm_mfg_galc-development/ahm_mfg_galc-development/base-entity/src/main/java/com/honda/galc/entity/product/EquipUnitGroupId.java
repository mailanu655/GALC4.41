package com.honda.galc.entity.product;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>EquipUnitGroupId Class description</h3>
 * <p> EquipUnitGroupId description </p>
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
public class EquipUnitGroupId implements Serializable {
	@Column(name="GROUP_ID")
	private short groupId;

	@Column(name="UNIT_ID")
	private short unitId;

	private static final long serialVersionUID = 1L;

	public EquipUnitGroupId() {
		super();
	}

	public short getGroupId() {
		return this.groupId;
	}

	public void setGroupId(short groupId) {
		this.groupId = groupId;
	}

	public short getUnitId() {
		return this.unitId;
	}

	public void setUnitId(short unitId) {
		this.unitId = unitId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof EquipUnitGroupId)) {
			return false;
		}
		EquipUnitGroupId other = (EquipUnitGroupId) o;
		return (this.groupId == other.groupId)
			&& (this.unitId == other.unitId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + ((int) this.groupId);
		hash = hash * prime + ((int) this.unitId);
		return hash;
	}

}
