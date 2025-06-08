package com.honda.galc.entity.bearing;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPartByYearModelId</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @created May 13, 2013
 */
@Embeddable
public class BearingPartByYearModelId extends BearingMatrixId {

	private static final long serialVersionUID = 1L;

	@Column(name = "BEARING_SERIAL_NO")
	private String bearingSerialNumber;

	// === get/set === //
	public String getBearingSerialNumber() {
		return StringUtils.trim(bearingSerialNumber);
	}

	public void setBearingSerialNumber(String bearingSerialNumber) {
		this.bearingSerialNumber = bearingSerialNumber;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BearingPartByYearModelId)) {
			return false;
		}
		BearingPartByYearModelId other = (BearingPartByYearModelId) o;
		boolean myc = equals(getModelYearCode(), other.getModelYearCode());
		boolean mc = equals(getModelCode(), other.getModelCode());
		boolean bsn = equals(getBearingSerialNumber(), other.getBearingSerialNumber());
		return myc && mc && bsn;
	}

	@Override
	public int hashCode() {
		int hash = getModelYearCode() == null ? 0 : getModelYearCode().hashCode();
		hash = 37 * hash + (getModelCode() == null ? 0 : getModelCode().hashCode());
		hash = 37 * hash + (getBearingSerialNumber() == null ? 0 : getBearingSerialNumber().hashCode());
		return hash;
	}

	protected boolean equals(Object o, Object o2) {
		return o == null ? o2 == null : o.equals(o2);
	}
}
