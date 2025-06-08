package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;

/**
 * <h3>LetDiagDetailId</h3>
 * <h3>Class description</h3>
 * <p>
 * Class act as embedded id for entity
 * {@link com.honda.galc.entity.product.LetDiagDetail}
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

@Embeddable
public class LetDiagDetailId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "END_TIMESTAMP")
	private Timestamp endTimestamp;

	@Column(name = "LET_TERMINAL_ID")
	private String letTerminalId;

	@Column(name = "DIAG_TEST_ID")
	private int diagTestId;

	public LetDiagDetailId() {
	}

	public LetDiagDetailId(Timestamp endTimestamp, String letTerminalId, int diagTestId) {
		this.endTimestamp = endTimestamp;
		this.letTerminalId = letTerminalId;
		this.diagTestId = diagTestId;
	}

	public Timestamp getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(Timestamp endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public String getLetTerminalId() {
		return StringUtils.trim(letTerminalId);
	}

	public void setLetTerminalId(String letTerminalId) {
		this.letTerminalId = letTerminalId;
	}

	public int getDiagTestId() {
		return diagTestId;
	}

	public void setDiagTestId(int diagTestId) {
		this.diagTestId = diagTestId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + diagTestId;
		result = prime * result + ((endTimestamp == null) ? 0 : endTimestamp.hashCode());
		result = prime * result + ((letTerminalId == null) ? 0 : letTerminalId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LetDiagDetailId other = (LetDiagDetailId) obj;
		if (diagTestId != other.diagTestId)
			return false;
		if (endTimestamp == null) {
			if (other.endTimestamp != null)
				return false;
		} else if (!endTimestamp.equals(other.endTimestamp))
			return false;
		if (letTerminalId == null) {
			if (other.letTerminalId != null)
				return false;
		} else if (!letTerminalId.equals(other.letTerminalId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LetDiagDetailId [endTimestamp=");
		builder.append(endTimestamp);
		builder.append(", letTerminalId=");
		builder.append(letTerminalId);
		builder.append(", diagTestId=");
		builder.append(diagTestId);
		builder.append("]");
		return builder.toString();
	}
}
