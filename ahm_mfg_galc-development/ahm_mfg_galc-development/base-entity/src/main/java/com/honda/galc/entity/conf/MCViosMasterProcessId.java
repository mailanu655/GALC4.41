package com.honda.galc.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.dto.ExcelSheetColumn;

/**
 * <h3>MCViosMasterProcess Class description</h3>
 * <p>
 * ID class for galadm.MC_VIOS_MASTER_ASM_PROC_TBX
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
 *         Aug 28, 2018
 */
@Embeddable
public class MCViosMasterProcessId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "VIOS_PLATFORM_ID")
	private String viosPlatformId;

	@Column(name = "ASM_PROC_NO", nullable = false, length = 5)
	@ExcelSheetColumn(name = "Process_No")
	private String asmProcNo;

	public MCViosMasterProcessId() {
		super();
	}

	public MCViosMasterProcessId(String viosPlatformId, String asmProcNo) {
		super();
		this.viosPlatformId = viosPlatformId;
		this.asmProcNo = asmProcNo;
	}

	public String getViosPlatformId() {
		return StringUtils.trimToEmpty(viosPlatformId);
	}

	public void setViosPlatformId(String viosPlatformId) {
		this.viosPlatformId = viosPlatformId;
	}

	public String getAsmProcNo() {
		return StringUtils.trimToEmpty(asmProcNo);
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result + ((viosPlatformId == null) ? 0 : viosPlatformId.hashCode());
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
		MCViosMasterProcessId other = (MCViosMasterProcessId) obj;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
			return false;
		if (viosPlatformId == null) {
			if (other.viosPlatformId != null)
				return false;
		} else if (!viosPlatformId.equals(other.viosPlatformId))
			return false;
		return true;
	}

}
