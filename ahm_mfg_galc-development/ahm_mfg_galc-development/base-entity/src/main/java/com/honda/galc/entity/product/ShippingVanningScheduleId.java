package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * <h3>ShippingVanningScheduleId Class description</h3>
 * <p> ShippingVanningScheduleId description </p>
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
 * Jun 1, 2012
 *
 *
 */
@Embeddable
public class ShippingVanningScheduleId implements Serializable {
	@Column(name="PRODUCTION_DATE")
	private Date productionDate;

	@Column(name="VANNING_SEQ")
	private int vanningSeq;

	private static final long serialVersionUID = 1L;

	public ShippingVanningScheduleId() {
		super();
	}

	public Date getProductionDate() {
		return this.productionDate;
	}

	public void setProductionDate(Date productionDate) {
		this.productionDate = productionDate;
	}

	public int getVanningSeq() {
		return this.vanningSeq;
	}

	public void setVanningSeq(int vanningSeq) {
		this.vanningSeq = vanningSeq;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof ShippingVanningScheduleId)) {
			return false;
		}
		ShippingVanningScheduleId other = (ShippingVanningScheduleId) o;
		return this.productionDate.equals(other.productionDate)
			&& (this.vanningSeq == other.vanningSeq);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.productionDate.hashCode();
		hash = hash * prime + this.vanningSeq;
		return hash;
	}

}
