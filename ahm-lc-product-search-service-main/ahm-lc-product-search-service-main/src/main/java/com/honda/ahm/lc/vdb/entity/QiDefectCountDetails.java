package com.honda.ahm.lc.vdb.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import com.honda.ahm.lc.vdb.util.Constants;
/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <p>
 * <code>QiDefectCountDetails</code> view for QICS Details .
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Hemant Rajput
 * @created May 05, 2021
 */

@Entity
@Immutable
@Subselect(value="SELECT PRODUCT_ID as id " + 
		", COUNT(CASE WHEN CURRENT_DEFECT_STATUS = "+Constants.FIXED+" THEN 1 " + 
		"         ELSE NULL " + 
		"    END) AS fixed_count " + 
		", COUNT(CASE WHEN CURRENT_DEFECT_STATUS = "+Constants.NON_REPAIRABLE+" THEN 1 " + 
		"         ELSE NULL " + 
		"    END) AS non_repairable_count " + 
		", COUNT(CASE WHEN CURRENT_DEFECT_STATUS = "+Constants.NOT_FIXED+" THEN 1 " + 
		"        ELSE NULL " + 
		"   END) AS not_fixed_count " + 
		"FROM QI_DEFECT_RESULT_TBX " + 
		"GROUP BY PRODUCT_ID")
public class QiDefectCountDetails extends VdbEntity<String> {
    
	private static final long serialVersionUID = 1L;

	@Column(name="fixed_count")
	private Integer fixedCount;
	
	@Column(name="non_repairable_count")
	private Integer nonRepairableCount;
	
	@Column(name="not_fixed_count")
	private Integer notFixedCount;
	
	public QiDefectCountDetails() {
		super();
	}

	public Integer getFixedCount() {
		return fixedCount;
	}

	public void setFixedCount(Integer fixedCount) {
		this.fixedCount = fixedCount;
	}

	public Integer getNonRepairableCount() {
		return nonRepairableCount;
	}

	public void setNonRepairableCount(Integer nonRepairableCount) {
		this.nonRepairableCount = nonRepairableCount;
	}

	public Integer getNotFixedCount() {
		return notFixedCount;
	}

	public void setNotFixedCount(Integer notFixedCount) {
		this.notFixedCount = notFixedCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((fixedCount == null) ? 0 : fixedCount.hashCode());
		result = prime * result + ((nonRepairableCount == null) ? 0 : nonRepairableCount.hashCode());
		result = prime * result + ((notFixedCount == null) ? 0 : notFixedCount.hashCode());
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
		QiDefectCountDetails other = (QiDefectCountDetails) obj;
		if (fixedCount == null) {
			if (other.fixedCount != null)
				return false;
		} else if (!fixedCount.equals(other.fixedCount))
			return false;
		if (nonRepairableCount == null) {
			if (other.nonRepairableCount != null)
				return false;
		} else if (!nonRepairableCount.equals(other.nonRepairableCount))
			return false;
		if (notFixedCount == null) {
			if (other.notFixedCount != null)
				return false;
		} else if (!notFixedCount.equals(other.notFixedCount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QiDefectCountDetails [id=" + getId() + ", fixedCount=" + getFixedCount() + ", nonRepairableCount=" + getNonRepairableCount()
				+ ", notFixedCount=" + getNotFixedCount() + "]";
	}
		   
}