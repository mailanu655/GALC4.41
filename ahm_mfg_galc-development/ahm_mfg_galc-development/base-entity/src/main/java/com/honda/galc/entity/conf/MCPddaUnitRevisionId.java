package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.StringUtil;

/**
 * @author Subu Kathiresan
 * @date Apr 7, 2014
 */
@Embeddable
public class MCPddaUnitRevisionId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	 
	@Column(name="OPERATION_NAME", nullable=false, length=32)
	private String operationName;

	@Column(name="OP_REV", nullable=false)
	private int operationRevision;
	
	@Column(name="PDDA_PLATFORM_ID", nullable=false)
	private int pddaPlatformId;

	@Column(name="UNIT_NO", nullable=false, length=6)
	private String unitNo;

	@Column(name="PDDA_REPORT", nullable=false, length=6)
	private String pddaReport;
	
	public MCPddaUnitRevisionId() {}

	public String getOperationName() {
		return StringUtils.trim(this.operationName);
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public int getOperationRevision() {
		return this.operationRevision;
	}

	public void setOperationRevision(int operationRevision) {
		this.operationRevision = operationRevision;
	}
	
	public int getPddaPlatformId() {
		return pddaPlatformId;
	}
	
	public void setPddaPlatformId(int pddaPlatformId) {
		this.pddaPlatformId = pddaPlatformId;
	}

	public String getUnitNo() {
		return StringUtils.trim(this.unitNo);
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getPddaReport() {
		return StringUtils.trim(this.pddaReport);
	}

	public void setPddaReport(String pddaReport) {
		this.pddaReport = pddaReport;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + operationRevision;
		result = prime * result + pddaPlatformId;
		result = prime * result
				+ ((pddaReport == null) ? 0 : pddaReport.hashCode());
		result = prime * result + ((unitNo == null) ? 0 : unitNo.hashCode());
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
		MCPddaUnitRevisionId other = (MCPddaUnitRevisionId) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (operationRevision != other.operationRevision)
			return false;
		if (pddaPlatformId != other.pddaPlatformId)
			return false;
		if (pddaReport == null) {
			if (other.pddaReport != null)
				return false;
		} else if (!pddaReport.equals(other.pddaReport))
			return false;
		if (unitNo == null) {
			if (other.unitNo != null)
				return false;
		} else if (!unitNo.equals(other.unitNo))
			return false;
		return true;
	}
		
	@Override
	public String toString() {
		return StringUtil.toString(this.getClass().getSimpleName(), getOperationName(), getOperationRevision(),
				getPddaPlatformId(), getUnitNo(), getPddaReport());
	}
}
