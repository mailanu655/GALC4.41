package com.honda.galc.entity.bearing;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixId</code> is ... .
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
 */
@Embeddable
public class BearingMatrixId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "MODEL_YEAR_CODE")
	private String modelYearCode;

	@Column(name = "MODEL_CODE")
	private String modelCode;

	public String getModelYearCode() {
		if (modelYearCode != null) {
			modelYearCode = modelYearCode.trim();
		}
		return modelYearCode;
	}

	public void setModelYearCode(String modelYearCode) {
		this.modelYearCode = modelYearCode;
	}

	public String getModelCode() {
		if (modelCode != null) {
			modelCode = modelCode.trim();
		}
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}


	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BearingMatrixId)) {
			return false;
		}
		BearingMatrixId other = (BearingMatrixId) o;
		boolean myc = equals(getModelYearCode(), other.getModelYearCode());
		boolean mc = equals(getModelCode(), other.getModelCode());
		return myc && mc;
	}

	@Override
	public int hashCode() {
		int hash = getModelYearCode() == null ? 0 : getModelYearCode().hashCode();
		hash = 37 * hash + (getModelCode() == null ? 0 : getModelCode().hashCode());
		return hash;
	}
	
	protected boolean equals(Object o, Object o2) {
		return o == null ? o2 == null : o.equals(o2);
	}
}
