package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;


/**
 * @author Anusha Gopalan
 * @date Aug 31, 2017
 */
public class Giv719Dto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	private int recordId = 0;
	private String plantCode = "HMA ";
	private String invLocCode = "BSK01     ";
	private String lineId = ""; // Used to generate 11 character inventory
								// category (e.g. MP01, MP02)
	private String partNo = ""; // 18 character part number
	private String partColorCode = "           ";
	private int partQty = 0;
	private String reportTime = null; // Used to create 8 character
											// reporting date and 6
											// character reporting time
	private String filler = "                       ";
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}
	
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public String getPlantCode() {
		return plantCode;
	}
	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
	}
	public String getInvLocCode() {
		return invLocCode;
	}
	public void setInvLocCode(String invLocCode) {
		this.invLocCode = invLocCode;
	}
	public String getLineId() {
		return lineId;
	}
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}
	public String getPartNo() {
		return partNo;
	}
	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}
	public String getPartColorCode() {
		return partColorCode;
	}
	public void setPartColorCode(String partColorCode) {
		this.partColorCode = partColorCode;
	}
	public int getPartQty() {
		return partQty;
	}
	public void setPartQty(int partQty) {
		this.partQty = partQty;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getFiller() {
		return filler;
	}
	public void setFiller(String filler) {
		this.filler = filler;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Boolean getIsError() {
		return isError;
	}
	public void setIsError(Boolean isError) {
		this.isError = isError;
	}
	
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + recordId ;
		result = prime * result + ((plantCode == null) ? 0 : plantCode.hashCode());
		result = prime * result + ((invLocCode == null) ? 0 : invLocCode.hashCode());
		result = prime * result + ((lineId == null) ? 0 : lineId.hashCode());
		result = prime * result + ((partNo == null) ? 0 : partNo.hashCode());
		result = prime * result + ((partColorCode == null) ? 0 : partColorCode.hashCode());
		result = prime * result + partQty;
		result = prime * result + ((reportTime == null) ? 0 : reportTime.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((errorMsg == null) ? 0 : errorMsg.hashCode());
		result = prime * result + (isError ? 1231 : 1237);
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
		Giv719Dto other = (Giv719Dto) obj;
		if (recordId != other.recordId)
			return false;
		if (plantCode == null) {
			if (other.plantCode != null)
				return false;
		} else if (!plantCode.equals(other.plantCode))
			return false;
		if (invLocCode == null) {
			if (other.invLocCode != null)
				return false;
		} else if (!invLocCode.equals(other.invLocCode))
			return false;
		if (lineId == null) {
			if (other.lineId != null)
				return false;
		} else if (!lineId.equals(other.lineId))
			return false;
		if (partNo == null) {
			if (other.partNo != null)
				return false;
		} else if (!partNo.equals(other.partNo))
			return false;
		if (partColorCode == null) {
			if (other.partColorCode != null)
				return false;
		} else if (!partColorCode.equals(other.partColorCode))
			return false;
		if (partQty != other.partQty)
			return false;
		if (reportTime == null) {
			if (other.reportTime != null)
				return false;
		} else if (!reportTime.equals(other.reportTime))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (errorMsg == null) {
			if (other.errorMsg != null)
				return false;
		} else if (!errorMsg.equals(other.errorMsg))
			return false;
		if (!isError != other.isError)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}

	
}
