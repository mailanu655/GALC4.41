package com.honda.galc.entity.product;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>LetDiagName</h3>
 * <h3>Class description</h3>
 * <p>
 * Class contains details related to LetDiagName
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
@Table(name = "GAL729TBX")
public class LetDiagName extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LetDiagNameId id;

	@Column(name = "DIAG_PARAM_TYPE")
	private String diagParamType;

	@Column(name = "DIAG_PARAM_VALUE")
	private String diagParamValue;

	public LetDiagName() {
	}

	public LetDiagName(LetDiagNameId newId) {
		setId(newId);
	}

	public LetDiagNameId getId() {
		return this.id;
	}

	public void setId(LetDiagNameId id) {
		this.id = id;
	}

	public String getDiagParamType() {
		return StringUtils.trim(diagParamType);
	}

	public void setDiagParamType(String diagParamType) {
		this.diagParamType = diagParamType;
	}

	public String getDiagParamValue() {
		return StringUtils.trim(diagParamValue);
	}

	public void setDiagParamValue(String diagParamValue) {
		this.diagParamValue = diagParamValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((diagParamType == null) ? 0 : diagParamType.hashCode());
		result = prime * result + ((diagParamValue == null) ? 0 : diagParamValue.hashCode());
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
		LetDiagName other = (LetDiagName) obj;
		if (diagParamType == null) {
			if (other.diagParamType != null)
				return false;
		} else if (!diagParamType.equals(other.diagParamType))
			return false;
		if (diagParamValue == null) {
			if (other.diagParamValue != null)
				return false;
		} else if (!diagParamValue.equals(other.diagParamValue))
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
		builder.append("LetDiagName [id=");
		builder.append(id);
		builder.append(", diagParamType=");
		builder.append(diagParamType);
		builder.append(", diagParamValue=");
		builder.append(diagParamValue);
		builder.append("]");
		return builder.toString();
	}

}
