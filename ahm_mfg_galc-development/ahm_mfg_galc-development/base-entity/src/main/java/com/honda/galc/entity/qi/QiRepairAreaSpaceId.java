package com.honda.galc.entity.qi;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <h3>QiRepairAreaSpace Class description</h3>
 * <p> QiRepairAreaSpace description </p>
 * 
 * <h4>Repair Area</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 * 
 * @author LnTInfotech<br>
 *        Mar 1, 2017
 * 
 */
@Embeddable
public class QiRepairAreaSpaceId implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "REPAIR_AREA_NAME")
	private String repairAreaName;
	
	@Column(name = "REPAIR_AREA_ROW")
	private int repairArearRow;
	
	@Column(name = "REPAIR_AREA_SPACE")
	private int repairArearSpace;

	public QiRepairAreaSpaceId() {
		super();
	}

	public QiRepairAreaSpaceId(String repairAreaName, int repairArearRow, int repairArearSpace) {
		super();
		this.repairAreaName = repairAreaName;
		this.repairArearRow = repairArearRow;
		this.repairArearSpace = repairArearSpace;
	}

	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public int getRepairArearRow() {
		return repairArearRow;
	}

	public int getRepairArearSpace() {
		return repairArearSpace;
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public void setRepairArearRow(int repairArearRow) {
		this.repairArearRow = repairArearRow;
	}

	public void setRepairArearSpace(int repairArearSpace) {
		this.repairArearSpace = repairArearSpace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + repairArearRow;
		result = prime * result + repairArearSpace;
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
		QiRepairAreaSpaceId other = (QiRepairAreaSpaceId) obj;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairArearRow != other.repairArearRow)
			return false;
		if (repairArearSpace != other.repairArearSpace)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiRepairAreaSpaceId [repairAreaName=" + repairAreaName + ", repairArearRow=" + repairArearRow
				+ ", repairArearSpace=" + repairArearSpace + "]";
	}


	
}
