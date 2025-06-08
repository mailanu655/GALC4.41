package com.honda.galc.entity.product;

import java.text.MessageFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>Qsr</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TD>Karol Wozniak</TD>
 * <TD>Jan 7, 2010</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Oct 31, 2012</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Migrated to galc reg (jpa)</TD>
 * </TR>
 * 
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
@Entity
@Table(name = "QSR_TBX")
public class Qsr extends AuditEntry {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "QSR_ID")
	private Integer id;

	@Column(name = "PROCESS_LOCATION")
	private String processLocation;

	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "QSR_STATUS")
	private int status;

	@Column(name = "APPROVER_NAME")
	private String approverName;

	@Column(name = "COMMENT")
	private String comment;
	
	@Column(name="HOLD_ACCESS_TYPE_ID")
	private String holdAccessType;

	@Column(name="RESPONSIBLE_DEPARTMENT")
	private String responsibleDepartment;

	// === get/set === //
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		MessageFormat mf = new MessageFormat("QSR-{0}-{1,number,0000}");
		String qsrCode = mf.format(new Object[] { getProcessLocation(), getId() });
		return qsrCode;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getHoldAccessType() {
		return StringUtils.trim(holdAccessType);
	}

	public void setHoldAccessType(String holdAccessType) {
		this.holdAccessType = holdAccessType;
	}
	
	public String getResponsibleDepartment() {
		return StringUtils.trim(responsibleDepartment);
	}

	public void setResponsibleDepartment(String responsibleDepartment) {
		this.responsibleDepartment = responsibleDepartment;
	}
}
