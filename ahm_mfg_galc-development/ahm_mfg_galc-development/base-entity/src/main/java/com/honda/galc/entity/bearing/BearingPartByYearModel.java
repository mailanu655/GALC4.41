package com.honda.galc.entity.bearing;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingPartByYearModel</code> is ... .
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

@Entity
@Table(name = "GAL256TBX")
public class BearingPartByYearModel extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private BearingPartByYearModelId id;

	// === get/set === //
	public BearingPartByYearModelId getId() {
		return id;
	}

	public void setId(BearingPartByYearModelId id) {
		this.id = id;
	}
}
