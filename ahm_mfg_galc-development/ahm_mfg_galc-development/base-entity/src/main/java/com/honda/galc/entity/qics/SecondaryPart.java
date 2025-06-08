package com.honda.galc.entity.qics;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;
/**
 * 
 * <h3>SecondaryPart Class description</h3>
 * <p> SecondaryPart description </p>
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
@Table(name="GAL227TBX")
public class SecondaryPart extends AuditEntry {
	@Id
	@Column(name="SECONDARY_PART_NAME")
	private String secondaryPartName;

	@Column(name="SECONDARY_PART_DESC_SHORT")
	private String secondaryPartDescShort;

	@Column(name="SECONDARY_PART_DESC_LONG")
	private String secondaryPartDescLong;


	private static final long serialVersionUID = 1L;

	public SecondaryPart() {
		super();
	}

	public String getSecondaryPartName() {
		return StringUtils.trim(this.secondaryPartName);
	}

	public void setSecondaryPartName(String secondaryPartName) {
		this.secondaryPartName = secondaryPartName;
	}

	public String getId() {
		return getSecondaryPartName();
	}

	
	public String getSecondaryPartDescShort() {
		return StringUtils.trim(this.secondaryPartDescShort);
	}

	public void setSecondaryPartDescShort(String secondaryPartDescShort) {
		this.secondaryPartDescShort = secondaryPartDescShort;
	}

	public String getSecondaryPartDescLong() {
		return StringUtils.trim(this.secondaryPartDescLong);
	}

	public void setSecondaryPartDescLong(String secondaryPartDescLong) {
		this.secondaryPartDescLong = secondaryPartDescLong;
	}

	@Override
	public String toString() {
		return toString(getSecondaryPartName());
	}



}
