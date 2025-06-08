package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>LetDiagDetail</h3>
 * <h3>Class description</h3>
 * <p>
 * Class contains details related to LetDiagDetail
 * </p>
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
 * <TD>vcc01419</TD>
 * <TD>June 12, 2017</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD>
 * </TR>
 *
 * </TABLE>
 * 
 * @see
 * @version 0.1
 * @author vcc01419
 * @since June 12, 2017
 */

@Entity
@Table(name = "GAL727TBX")
public class LetDiagDetail extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetDiagDetailId id;

	@Column(name = "DIAG_TEST_STATUS")
	private String diagTestStatus;

	public LetDiagDetail() {
	}

	public LetDiagDetail(LetDiagDetailId newId) {
		setId(newId);
	}

	public String getDiagTestStatus() {
		return StringUtils.trim(diagTestStatus);
	}

	public void setDiagTestStatus(String diagTestStatus) {
		this.diagTestStatus = diagTestStatus;
	}

	public LetDiagDetailId getId() {
		return this.id;
	}

	public void setId(LetDiagDetailId id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagTestStatus == null) ? 0 : diagTestStatus.hashCode());
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
		LetDiagDetail other = (LetDiagDetail) obj;
		if (diagTestStatus == null) {
			if (other.diagTestStatus != null)
				return false;
		} else if (!diagTestStatus.equals(other.diagTestStatus))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LetDiagDetail [id=");
		builder.append(id);
		builder.append(", diagTestStatus=");
		builder.append(diagTestStatus);
		builder.append("]");
		return builder.toString();
	}

}
