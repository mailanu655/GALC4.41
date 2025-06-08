package com.honda.galc.entity.qi;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.util.ToStringUtil;

/**
 * 
 * <h3>QiRepairAreaRowId Class description</h3>
 * <p>
 * QiRepairAreaRowId contains the getter and setter of the QiRepairAreaRowId properties
 * and maps this class with database table and properties with the database its
 * columns .
 * </p>
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * </Table>
 * 
 * @author LnTInfotech<br>
 * 
 */
@Embeddable
public class QiRepairAreaRowId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "REPAIR_AREA_NAME")
	private String repairAreaName;
	@Column(name = "REPAIR_AREA_ROW")
	private int repairAreaRow;
	
	public QiRepairAreaRowId() {
		
	}
	public QiRepairAreaRowId(String repairAreaName,int repairAreaRow){
		this.setRepairAreaName(repairAreaName);
		this.setRepairAreaRow(repairAreaRow);
	}
	public String getRepairAreaName() {
		return StringUtils.trimToEmpty(repairAreaName);
	}

	public void setRepairAreaName(String repairAreaName) {
		this.repairAreaName = repairAreaName;
	}

	public int getRepairAreaRow() {
		return repairAreaRow;
	}

	public void setRepairAreaRow(int repairAreaRow) {
		this.repairAreaRow = repairAreaRow;
	}
	@Override
	public String toString() {
		return ToStringUtil.generateToStringForAuditLogging(this);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((repairAreaName == null) ? 0 : repairAreaName.hashCode());
		result = prime * result + repairAreaRow;
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
		QiRepairAreaRowId other = (QiRepairAreaRowId) obj;
		if (repairAreaName == null) {
			if (other.repairAreaName != null)
				return false;
		} else if (!repairAreaName.equals(other.repairAreaName))
			return false;
		if (repairAreaRow != other.repairAreaRow)
			return false;
		return true;
	}
	
}
