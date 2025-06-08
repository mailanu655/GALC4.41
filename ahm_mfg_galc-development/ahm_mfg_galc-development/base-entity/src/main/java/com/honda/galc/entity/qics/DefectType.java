package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>DefectType Class description</h3>
 * <p> DefectType description </p>
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
 *
 * </TABLE>
 *   
 * @author Jeffray Huang<br>
 * Mar 30, 2011
 *
 *
 */
@Entity
@Table(name="GAL126TBX")
public class DefectType extends AuditEntry {
	@Id
	@Column(name="DEFECT_TYPE_NAME")
	private String defectTypeName;

	@Column(name="DEFECT_TYPE_DESCRIPTION_SHORT")
	private String defectTypeDescriptionShort;

	@Column(name="DEFECT_TYPE_DESCRIPTION_LONG")
	private String defectTypeDescriptionLong;

	@Column(name="TWO_PART_DEFECT_FLAG")
	private int twoPartDefectFlag;


	private static final long serialVersionUID = 1L;

	public DefectType() {
		super();
	}

	public String getDefectTypeName() {
		return StringUtils.trim(this.defectTypeName);
	}

	public void setDefectTypeName(String defectTypeName) {
		this.defectTypeName = defectTypeName;
	}

	public String getId() {
		return getDefectTypeName();
	}

	public String getDefectTypeDescriptionShort() {
		return StringUtils.trim(this.defectTypeDescriptionShort);
	}

	public void setDefectTypeDescriptionShort(String defectTypeDescriptionShort) {
		this.defectTypeDescriptionShort = defectTypeDescriptionShort;
	}

	public String getDefectTypeDescriptionLong() {
		return StringUtils.trim(this.defectTypeDescriptionLong);
	}

	public void setDefectTypeDescriptionLong(String defectTypeDescriptionLong) {
		this.defectTypeDescriptionLong = defectTypeDescriptionLong;
	}

	public int getTwoPartDefectFlagValue() {
		return this.twoPartDefectFlag;
	}

	public void setTwoPartDefectFlaValue(int twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag;
	}
	
	public boolean getTwoPartDefectFlag() {
		return this.twoPartDefectFlag == 1;
	}

	public void setTwoPartDefectFlag(boolean twoPartDefectFlag) {
		this.twoPartDefectFlag = twoPartDefectFlag ? 1 : 0;
	}

	@Override
	public String toString() {
		return toString(getDefectTypeName());
	}

}
