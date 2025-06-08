package com.honda.galc.service.msip.dto.outbound;

import com.honda.galc.util.ToStringUtil;

/*
 * 
 * @author Parthasarathy Palanisamy
 * @date Nov 17, 2017
 */
public class Gpp103Dto extends BaseOutboundDto implements IMsipOutboundDto {
	private static final long serialVersionUID = 1L;

	private String productIdSubString;
	private String startProductId;
	private String plantCode;
	private String lineNo;
	private String processLocation;
	private String onoff;
	private String afOnSequenceNumber;
	private String productSpecCode;
	private String productId;
	private String createDate;
	private String createTime;
	private String lotSizeCalc;
	private String errorMsg;
	private Boolean isError;
	
	public Boolean getIsError() {
		return isError;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setIsError(Boolean isError) {
		this.isError = isError;
		
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getProductIdSubString() {
		return productIdSubString;
	}

	public void setProductIdSubString(String productIdSubString) {
		this.productIdSubString = productIdSubString;
	}

	public String getStartProductId() {
		return startProductId;
	}

	public void setStartProductId(String startProductId) {
		this.startProductId = startProductId;
	}

	public String getPlantCode() {
		return plantCode;
	}

	public void setPlantCode(String plantCode) {
		this.plantCode = plantCode;
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

	public String getOnoff() {
		return onoff;
	}

	public void setOnoff(String onoff) {
		this.onoff = onoff;
	}

	public String getAfOnSequenceNumber() {
		return afOnSequenceNumber;
	}

	public void setAfOnSequenceNumber(String afOnSequenceNumber) {
		this.afOnSequenceNumber = afOnSequenceNumber;
	}

	public String getProductSpecCode() {
		return productSpecCode;
	}

	public void setProductSpecCode(String productSpecCode) {
		this.productSpecCode = productSpecCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLotSizeCalc() {
		return lotSizeCalc;
	}

	public void setLotSizeCalc(String lotSizeCalc) {
		this.lotSizeCalc = lotSizeCalc;
	}

	@Override
	public String getSiteName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlantName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProcessPointId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productIdSubString == null) ? 0 : productIdSubString.hashCode());
		result = prime * result + ((lineNo == null) ? 0 : lineNo.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((startProductId == null) ? 0 : startProductId.hashCode());
		result = prime * result + ((processLocation == null) ? 0 : processLocation.hashCode());
		result = prime * result + ((onoff == null) ? 0 : onoff.hashCode());
		result = prime * result + ((afOnSequenceNumber == null) ? 0 : afOnSequenceNumber.hashCode());
		result = prime * result + ((productSpecCode == null) ? 0 : productSpecCode.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((lotSizeCalc == null) ? 0 : lotSizeCalc.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
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
		Gpp103Dto other = (Gpp103Dto) obj;
		if (productIdSubString == null) {
			if (other.productIdSubString != null)
				return false;
		} else if (!productIdSubString.equals(other.productIdSubString))
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
		if (startProductId == null) {
			if (other.startProductId != null)
				return false;
		} else if (!startProductId.equals(other.startProductId))
			return false;
		if (onoff == null) {
			if (other.onoff != null)
				return false;
		} else if (!onoff.equals(other.onoff))
			return false;
		if (afOnSequenceNumber == null) {
			if (other.afOnSequenceNumber != null)
				return false;
		} else if (!afOnSequenceNumber.equals(other.afOnSequenceNumber))
			return false;
		if (productSpecCode == null) {
			if (other.productSpecCode != null)
				return false;
		} else if (!productSpecCode.equals(other.productSpecCode))
			return false;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (lotSizeCalc == null) {
			if (other.lotSizeCalc != null)
				return false;
		} else if (!lotSizeCalc.equals(other.lotSizeCalc))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (createTime == null) {
			if (other.createTime != null)
				return false;
		} else if (!createTime.equals(other.createTime))
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
