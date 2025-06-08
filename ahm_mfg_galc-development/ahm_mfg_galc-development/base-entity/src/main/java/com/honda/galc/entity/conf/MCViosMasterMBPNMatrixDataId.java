package com.honda.galc.entity.conf;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

import com.honda.galc.dto.ExcelSheetColumn;

/**
 * Entity implementation class for Entity: MCMBPNMasterData
 *
 */
@Embeddable
public class MCViosMasterMBPNMatrixDataId implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@Column(name = "VIOS_PLATFORM_ID", nullable = false)
	private String viosPlatformId;
	
  
	@Column(name = "ASM_PROC_NO", nullable = false)
	@ExcelSheetColumn(name="PDDA Process Number")
	private String asmProcNo;
	

	@Column(name = "MBPN_MASK", nullable = false)
	@ExcelSheetColumn(name="MBPN Mask")
	private String mbpnMask;
	

	@Column(name = "MTC_TYPE", nullable = false)
	@ExcelSheetColumn(name="MTC Types")
	private String mtcType;
	

	@Column(name = "MTC_MODEL", nullable = false)
	@ExcelSheetColumn(name="MTC Model")
	private String mtcModel;


	public MCViosMasterMBPNMatrixDataId(String asmProcNo, String mbpnMask, String mtcType, String mtcModel) {
		super();
		this.asmProcNo = asmProcNo;
		this.mbpnMask = mbpnMask;
		this.mtcType = mtcType;
		this.mtcModel = mtcModel;
	}

	public MCViosMasterMBPNMatrixDataId() {
		super();
	}
	public String getViosPlatformId() {
		return this.viosPlatformId;
	}

	public void setViosPlatformId(String viosPlatformId) {
		this.viosPlatformId = viosPlatformId;
	}

	public String getAsmProcNo() {
		return asmProcNo;
	}

	public void setAsmProcNo(String asmProcNo) {
		this.asmProcNo = asmProcNo;
	}

	public String getMbpnMask() {
		return mbpnMask;
	}

	public void setMbpnMask(String mbpnMask) {
		this.mbpnMask = mbpnMask;
	}

	public String getMtcType() {
		return mtcType;
	}

	public void setMtcType(String mtcType) {
		this.mtcType = mtcType;
	}

	public String getMtcModel() {
		return mtcModel;
	}

	public void setMtcModel(String mtcModel) {
		this.mtcModel = mtcModel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asmProcNo == null) ? 0 : asmProcNo.hashCode());
		result = prime * result + ((mbpnMask == null) ? 0 : mbpnMask.hashCode());
		result = prime * result + ((mtcModel == null) ? 0 : mtcModel.hashCode());
		result = prime * result + ((mtcType == null) ? 0 : mtcType.hashCode());
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
		MCViosMasterMBPNMatrixDataId other = (MCViosMasterMBPNMatrixDataId) obj;
		if (asmProcNo == null) {
			if (other.asmProcNo != null)
				return false;
		} else if (!asmProcNo.equals(other.asmProcNo))
			return false;
		if (mbpnMask == null) {
			if (other.mbpnMask != null)
				return false;
		} else if (!mbpnMask.equals(other.mbpnMask))
			return false;
		if (mtcModel == null) {
			if (other.mtcModel != null)
				return false;
		} else if (!mtcModel.equals(other.mtcModel))
			return false;
		if (mtcType == null) {
			if (other.mtcType != null)
				return false;
		} else if (!mtcType.equals(other.mtcType))
			return false;
		if (viosPlatformId == null) {
			if (other.viosPlatformId != null)
				return false;
		} else if (!viosPlatformId.equals(other.viosPlatformId))
			return false;
		return true;
	}
	
	
   
}
