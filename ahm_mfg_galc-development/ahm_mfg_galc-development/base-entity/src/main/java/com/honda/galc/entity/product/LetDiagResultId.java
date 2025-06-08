package com.honda.galc.entity.product;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 04, 2013
 */

@Embeddable
public class LetDiagResultId implements Serializable {
	@Column(name="END_TIMESTAMP")
	private Timestamp endTimestamp;

	@Column(name="LET_TERMINAL_ID")
	private String letTerminalId;

	private static final long serialVersionUID = 1L;

	public LetDiagResultId() {
		super();
	}

	public LetDiagResultId(Timestamp endTimestamp, String letTerminalId) {
		super();
		this.endTimestamp = endTimestamp;
		this.letTerminalId = letTerminalId;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((endTimestamp == null) ? 0 : endTimestamp.hashCode());
		result = prime * result
		+ ((letTerminalId == null) ? 0 : letTerminalId.hashCode());
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
		LetDiagResultId other = (LetDiagResultId) obj;
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

	@Override
	public String toString() {
		return super.toString();
	}

}
