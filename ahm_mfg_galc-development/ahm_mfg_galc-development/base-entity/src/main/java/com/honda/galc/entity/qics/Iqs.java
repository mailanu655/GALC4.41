package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>Iqs Class description</h3>
 * <p> Iqs description </p>
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
  /**
 * 
 * @author Gangadhararao Gadde
 * @date Oct 30, 2014
 IQS and Regression Code Maintenance Screens
 */
@Entity
@Table(name="GAL192TBX")
public class Iqs extends AuditEntry {
	@EmbeddedId
	private IqsId id;

	@Column(name="COEFFICIENT")
	private String coefficient;


	private static final long serialVersionUID = 1L;

	public Iqs() {
		super();
	}	

	public Iqs(IqsId id, String coefficient) {
		super();
		this.id = id;
		this.coefficient = coefficient;
	}

	public IqsId getId() {
		return this.id;
	}

	public void setid(IqsId id) {
		this.id = id;
	}

	public String getCoefficient() {
		return StringUtils.trim(this.coefficient);
	}

	public void setCoefficient(String coefficient) {
		this.coefficient = coefficient;
	}
	
	public String getIqsCategoryName() {
		return id.getIqsCategoryName();
	}
	
	public String getIqsItemName() {
	    return getId() != null ? id.getIqsItemName() : null;
	}

	@Override
	public String toString() {
		return toString(getIqsCategoryName(),getIqsItemName());
	}

}
