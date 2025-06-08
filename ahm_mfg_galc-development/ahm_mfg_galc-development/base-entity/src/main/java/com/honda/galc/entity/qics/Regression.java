package com.honda.galc.entity.qics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * 
 * <h3>Regression Class description</h3>
 * <p> Regression description </p>
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
@Table(name="GAL219TBX")
public class Regression extends  AuditEntry {
	@Id
	@Column(name="REGRESSION_CODE")
	private String regressionCode;

	private static final long serialVersionUID = 1L;

	public Regression() {
		super();
	}	

	public Regression(String regressionCode) {
		super();
		this.regressionCode = regressionCode;
	}

	public String getRegressionCode() {
		return StringUtils.trim(this.regressionCode);
	}
	
	public String getId() {
		return getRegressionCode();
	}

	public void setRegressionCode(String regressionCode) {
		this.regressionCode = regressionCode;
	}

	@Override
	public String toString() {
		return toString(getRegressionCode());
	}

}
