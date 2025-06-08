package com.honda.galc.entity.conf;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;
/**
 * <h3>MCViosMasterOperation Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_OP_TBX
 * </p>
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
 * @author Hemant Kumar<br>
 *        Oct 4, 2018
 */
@Entity
@Table(name="MC_VIOS_MASTER_OP_TBX")
public class MCViosMasterOperation extends AuditEntry {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCViosMasterOperationId id;
	
	@Column(name="OP_VIEW", length=255)
	@ExcelSheetColumn(name="View")
	private String view;

	@Column(name="OP_PROCESSOR", length=255)
	@ExcelSheetColumn(name="Processor")
	private String processor;
	
	@Column(name="COMMON_NAME", length=255)
	@ExcelSheetColumn(name="Common_Name")
	private String commonName;
	
	@Column(name="OP_CHECK")
	@ExcelSheetColumn(name="Op_Check")
	private int opCheck;
	
	@Column(name = "USER_ID")
    private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterOperation() {
		super();
	}

	public MCViosMasterOperation(String viosPlatformId, String unitNo, String view, String processor, String commonName, boolean isOpCheckSelected) {
		super();
		this.id = new MCViosMasterOperationId(viosPlatformId, unitNo);
		this.view = view;
		this.processor = processor;
		this.commonName = commonName;
		this.opCheck = isOpCheckSelected ? 1 : 0;
	}

	public MCViosMasterOperationId getId() {
		return id;
	}

	public void setId(MCViosMasterOperationId id) {
		this.id = id;
	}

	public String getView() {
		return StringUtils.trimToEmpty(view);
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getProcessor() {
		return StringUtils.trimToEmpty(processor);
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public String getCommonName() {
		return StringUtils.trimToEmpty(commonName);
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public int getOpCheck() {
		return opCheck;
	}

	public void setOpCheck(int opCheck) {
		this.opCheck = opCheck;
	}
	
	public void setOpCheck(boolean isOpCheckSelected) {
		this.opCheck = isOpCheckSelected ? 1 : 0;
	}
	
	public boolean isOpCheckSelected() {
		return this.opCheck == 1;
	}
	
	public String getOpCheckAsString() {
		return isOpCheckSelected() ? "TRUE" : "FALSE";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((commonName == null) ? 0 : commonName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + opCheck;
		result = prime * result + ((processor == null) ? 0 : processor.hashCode());
		result = prime * result + ((view == null) ? 0 : view.hashCode());
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
		MCViosMasterOperation other = (MCViosMasterOperation) obj;
		if (commonName == null) {
			if (other.commonName != null)
				return false;
		} else if (!commonName.equals(other.commonName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (opCheck != other.opCheck)
			return false;
		if (processor == null) {
			if (other.processor != null)
				return false;
		} else if (!processor.equals(other.processor))
			return false;
		if (view == null) {
			if (other.view != null)
				return false;
		} else if (!view.equals(other.view))
			return false;
		return true;
	}


}
