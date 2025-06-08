package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.service.msip.dto.outbound.IMsipOutboundDto;
import com.honda.galc.util.ToStringUtil;

/**
 * @author Anusha Gopalan
 * @date Aug 31, 2017
 */
public class Giv707Dto extends BaseOutboundDto implements IMsipOutboundDto {

	private static final long serialVersionUID = 1L;
	private String planCode;
	private String lineNo;
	private String processLocation;
	private String onOffFlag;
	private String kdLotNo;
	private String prodSeqNo;
	private String mbpn;
	private String hesColor;
	private String mtoc;
	private String resultQty;
	private String createdDate;
	private String createdTime;
	private String minusFlag;
	private String filler;
	private String productionQty;
	private String errorMsg;
	private Boolean isError;
	
	public String getVersion() {
		return version;
	}
	
	public String getSiteName() {
		return null;
	}

	public String getPlantName() {
		return null;
	}

	public String getProcessPointId() {
		return null;
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

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getProcessLocation() {
		return processLocation;
	}

	public void setProcessLocation(String processLocation) {
		this.processLocation = processLocation;
	}

	public String getOnOffFlag() {
		return onOffFlag;
	}

	public void setOnOffFlag(String onOffFlag) {
		this.onOffFlag = onOffFlag;
	}

	public String getKdLotNo() {
		return kdLotNo;
	}

	public void setKdLotNo(String kdLotNo) {
		this.kdLotNo = kdLotNo;
	}

	public String getProdSeqNo() {
		return prodSeqNo;
	}

	public void setProdSeqNo(String prodSeqNo) {
		this.prodSeqNo = prodSeqNo;
	}

	public String getMbpn() {
		return mbpn;
	}

	public void setMbpn(String mbpn) {
		this.mbpn = mbpn;
	}

	public String getHesColor() {
		return hesColor;
	}

	public void setHesColor(String hesColor) {
		this.hesColor = hesColor;
	}

	public String getMtoc() {
		return mtoc;
	}

	public void setMtoc(String mtoc) {
		this.mtoc = mtoc;
	}

	public String getResultQty() {
		return resultQty;
	}

	public void setResultQty(String resultQty) {
		this.resultQty = resultQty;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getMinusFlag() {
		return minusFlag;
	}

	public void setMinusFlag(String minusFlag) {
		this.minusFlag = minusFlag;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String getProductionQty() {
		return productionQty;
	}

	public void setProductionQty(String productionQty) {
		this.productionQty = productionQty;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planCode == null) ? 0 : planCode.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((onOffFlag == null) ? 0 : onOffFlag.hashCode());
		result = prime * result + ((kdLotNo == null) ? 0 : kdLotNo.hashCode());
		result = prime * result + ((prodSeqNo == null) ? 0 : prodSeqNo.hashCode());
		result = prime * result + ((mbpn == null) ? 0 : mbpn.hashCode());
		result = prime * result + ((hesColor == null) ? 0 : hesColor.hashCode());
		result = prime * result + ((mtoc == null) ? 0 : mtoc.hashCode());
		result = prime * result + ((resultQty == null) ? 0 : resultQty.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((createdTime == null) ? 0 : createdTime.hashCode());
		result = prime * result + ((minusFlag == null) ? 0 : minusFlag.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((productionQty == null) ? 0 : productionQty.hashCode());
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
		Giv707Dto other = (Giv707Dto) obj;
		if (planCode == null) {
			if (other.planCode != null)
				return false;
		} else if (!planCode.equals(other.planCode))
			return false;
		if (lineNo == null) {
			if (other.lineNo != null)
				return false;
		} else if (!lineNo.equals(other.lineNo))
			return false;
		if (processLocation == null) {
			if (other.processLocation != null)
				return false;
		} else if (!processLocation.equals(other.processLocation))
			return false;
		if (onOffFlag == null) {
			if (other.onOffFlag != null)
				return false;
		} else if (!onOffFlag.equals(other.onOffFlag))
			return false;
		if (kdLotNo == null) {
			if (other.kdLotNo != null)
				return false;
		} else if (!kdLotNo.equals(other.kdLotNo))
			return false;
		if (prodSeqNo == null) {
			if (other.prodSeqNo != null)
				return false;
		} else if (!prodSeqNo.equals(other.prodSeqNo))
			return false;
		if (mbpn == null) {
			if (other.mbpn != null)
				return false;
		} else if (!mbpn.equals(other.mbpn))
			return false;
		if (hesColor == null) {
			if (other.hesColor != null)
				return false;
		} else if (!hesColor.equals(other.hesColor))
			return false;
		if (mtoc == null) {
			if (other.mtoc != null)
				return false;
		} else if (!mtoc.equals(other.mtoc))
			return false;
		if (resultQty == null) {
			if (other.resultQty != null)
				return false;
		} else if (!resultQty.equals(other.resultQty))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (createdTime == null) {
			if (other.createdTime != null)
				return false;
		} else if (!createdTime.equals(other.createdTime))
			return false;
		if (minusFlag == null) {
			if (other.minusFlag != null)
				return false;
		} else if (!minusFlag.equals(other.minusFlag))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (productionQty == null) {
			if (other.productionQty != null)
				return false;
		} else if (!productionQty.equals(other.productionQty))
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
