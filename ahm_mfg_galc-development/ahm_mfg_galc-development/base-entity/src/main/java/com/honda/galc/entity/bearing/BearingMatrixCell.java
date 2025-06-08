package com.honda.galc.entity.bearing;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixCell</code> is ... .
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
@Entity
@Table(name = "GAL251TBX")
public class BearingMatrixCell extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BearingMatrixCellId id;

	@Column(name = "UPPER_BEARING")
	private String upperBearing;

	@Column(name = "LOWER_BEARING")
	private String lowerBearing;

	public BearingMatrixCellId getId() {
		return id;
	}

	public void setId(BearingMatrixCellId id) {
		this.id = id;
	}

	public String getUpperBearing() {
		return StringUtils.trim(upperBearing);
	}

	public void setUpperBearing(String upperBearing) {
		this.upperBearing = upperBearing;
	}

	public String getLowerBearing() {
		return StringUtils.trim(lowerBearing);
	}

	public void setLowerBearing(String lowerBearing) {
		this.lowerBearing = lowerBearing;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("{");
		sb.append("id:").append(getId() == null ? getId() : getId());
		sb.append(",upperBearing:").append(StringUtils.trim(getUpperBearing()));
		sb.append(",lowerBearing:").append(StringUtils.trim(getLowerBearing()));
		sb.append("}");
		return sb.toString();
	}

}
