package com.honda.galc.entity.product;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.honda.galc.entity.AuditEntry;

/**
 * <h3>LetDiagFaultDetail</h3>
 * <h3>Class description</h3>
 * <p>
 * Class contains details related to LetDiagFaultDetail
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
@Table(name = "GAL728TBX")
public class LetDiagFaultDetail extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetDiagFaultDetailId id;

	@Column(name = "SHORT_DESC_ID")
	private int shortDescId;

	public LetDiagFaultDetail() {
	}

	public LetDiagFaultDetail(LetDiagFaultDetailId newId) {
		setId(newId);
	}

	public LetDiagFaultDetailId getId() {
		return this.id;
	}

	public void setId(LetDiagFaultDetailId id) {
		this.id = id;
	}

	public int getShortDescId() {
		return shortDescId;
	}

	public void setShortDescId(int shortDescId) {
		this.shortDescId = shortDescId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + shortDescId;
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
		LetDiagFaultDetail other = (LetDiagFaultDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (shortDescId != other.shortDescId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LetDiagFaultDetail [id=");
		builder.append(id);
		builder.append(", shortDescId=");
		builder.append(shortDescId);
		builder.append("]");
		return builder.toString();
	}

}
