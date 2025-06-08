package com.honda.galc.vios.dto;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.DtoTag;
import com.honda.galc.dto.IDto;
/**
 * <h3>MCViosMasterProcessDto Class description</h3>
 * <p>
 * Dto class for MCViosMasterProcess
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
public class MCViosMasterProcessDto implements IDto{

	private static final String UNMAPPED = "UNMAPPED";

	private static final long serialVersionUID = 1L;

	@DtoTag(name="PROCESS_POINT_ID")
	private String processPointId;
	
	@DtoTag(name="ASM_PROC_NO")
	private String asmProcNo;

	@DtoTag(name="PROCESS_SEQ_NUM")
	private int processSeqNum;

	public MCViosMasterProcessDto() {
		super();
	}

	public String getProcessPointId() {
		return StringUtils.isNotEmpty(processPointId) ? StringUtils.trimToEmpty(processPointId) : UNMAPPED;
	}

	public void setProcessPointId(String processPointId) {
		this.processPointId = processPointId;
	}

	public String getAsmProcNo() {
		return StringUtils.trimToEmpty(asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public int getProcessSeqNum() {
		return processSeqNum;
	}

	public void setProcessSeqNum(int processSeqNum) {
		this.processSeqNum = processSeqNum;
	}
	
	public String getProcessSeqNumAsString() {
		return processSeqNum != 0 ? String.valueOf(processSeqNum) : UNMAPPED;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result + ((processPointId == null) ? 0 : processPointId.hashCode());
		result = prime * result + processSeqNum;
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
		MCViosMasterProcessDto other = (MCViosMasterProcessDto) obj;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
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
	
}
