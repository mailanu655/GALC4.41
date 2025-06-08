package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;
import com.honda.galc.entity.AuditEntry;
/**
 * <h3>MCViosMasterProcess Class description</h3>
 * <p>
 * Entity class for galadm.MC_VIOS_MASTER_ASM_PROC_TBX
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
 *        Aug 28, 2018
 */
@Entity
@Table(name="MC_VIOS_MASTER_ASM_PROC_TBX")
public class MCViosMasterProcess extends AuditEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private MCViosMasterProcessId id;
	
	@Column(name="PROCESS_POINT_ID", nullable=false, length=16)
	@ExcelSheetColumn(name="Process_Point_Id")
	private String processPointId;

	@Column(name="PROCESS_SEQ_NUM", nullable=false)
	@ExcelSheetColumn(name="Process_Seq_No")
	private int processSeqNum;
	
	
	@Column(name = "USER_ID")
    private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MCViosMasterProcess() {
		super();
	}

	public MCViosMasterProcess(String viosPlatformId, String asmProcNo, String processPointId, int processSeqNum) {
		super();
		this.id = new MCViosMasterProcessId(viosPlatformId, asmProcNo);
		this.processPointId = processPointId;
		this.processSeqNum = processSeqNum;
	}

	public String getProcessPointId() {
		return StringUtils.trimToEmpty(processPointId);
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public int getProcessSeqNum() {
		return processSeqNum;
	}

	public void setProcessSeqNum(int processSeqNum) {
		this.processSeqNum = processSeqNum;
	}

	public void setId(MCViosMasterProcessId id) {
		this.id = id;
	}

	public MCViosMasterProcessId getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + processSeqNum;
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
		MCViosMasterProcess other = (MCViosMasterProcess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (processPointId == null) {
			if (other.processPointId != null)
				return false;
		} else if (!processPointId.equals(other.processPointId))
			return false;
		if (processSeqNum != other.processSeqNum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString(getProcessPointId(), getProcessSeqNum());
	}
	
	
}
