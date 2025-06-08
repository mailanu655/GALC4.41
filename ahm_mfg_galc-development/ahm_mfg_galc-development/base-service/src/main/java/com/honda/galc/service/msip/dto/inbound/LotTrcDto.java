package com.honda.galc.service.msip.dto.inbound;

import org.apache.commons.lang.StringUtils;
/*
 * 
 * @author Parthasarathy Palanisamy
 * @date June, 2017
*/
public class LotTrcDto implements IMsipInboundDto {
	private static final long serialVersionUID = 1L;

	private String lsn;
	private String kdLotNumber;
	private String supplierLotNumber;
	private String captureDate;
	private String captureTime;
	private String partNum;
	private String partColor;
	private String partQty;

	public String getLsn()  {
		return StringUtils.trim(lsn);
	}

	public void setLsn(String newLsn)  {
		lsn=newLsn;
	}

	public String getKdLotNumber()  {
		return StringUtils.trim(kdLotNumber);
	}

	public void setKdLotNumber(String newKdLotNumber)  {
		kdLotNumber=newKdLotNumber;
	}

	public String getSupplierLotNumber()  {
		return StringUtils.trim(supplierLotNumber);
	}

	public void setSupplierLotNumber(String newSupplierLotNumber)  {
		supplierLotNumber=newSupplierLotNumber;
	}

	public String getCaptureDate()  {
		return StringUtils.trim(captureDate);
	}

	public void setCaptureDate(String newCaptureDate)  {
		captureDate=newCaptureDate;
	}

	public String getCaptureTime()  {
		return StringUtils.trim(captureTime);
	}

	public void setCaptureTime(String newCaptureTime)  {
		captureTime=newCaptureTime;
	}

	public String getPartNum()  {
		return StringUtils.trim(partNum);
	}

	public void setPartNum(String newPartNum)  {
		partNum=newPartNum;
	}

	public String getPartColor()  {
		return StringUtils.trim(partColor);
	}

	public void setPartColor(String newPartColor)  {
		partColor=newPartColor;
	}

	public String getPartQty()  {
		return StringUtils.trim(partQty);
	}

	public void setPartQty(String newPartQty)  {
		partQty=newPartQty;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((captureDate == null) ? 0 : captureDate.hashCode());
		result = prime * result + ((captureTime == null) ? 0 : captureTime.hashCode());
		result = prime * result + ((kdLotNumber == null) ? 0 : kdLotNumber.hashCode());
		result = prime * result + ((lsn == null) ? 0 : lsn.hashCode());
		result = prime * result + ((partColor == null) ? 0 : partColor.hashCode());
		result = prime * result + ((partNum == null) ? 0 : partNum.hashCode());
		result = prime * result + ((partQty == null) ? 0 : partQty.hashCode());
		result = prime * result + ((supplierLotNumber == null) ? 0 : supplierLotNumber.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotTrcDto other = (LotTrcDto) obj;
		if (captureDate == null) {
			if (other.captureDate != null)
				return false;
		} else if (!captureDate.equals(other.captureDate))
			return false;
		if (captureTime == null) {
			if (other.captureTime != null)
				return false;
		} else if (!captureTime.equals(other.captureTime))
			return false;
		if (kdLotNumber == null) {
			if (other.kdLotNumber != null)
				return false;
		} else if (!kdLotNumber.equals(other.kdLotNumber))
			return false;
		if (lsn == null) {
			if (other.lsn != null)
				return false;
		} else if (!lsn.equals(other.lsn))
			return false;
		if (partColor == null) {
			if (other.partColor != null)
				return false;
		} else if (!partColor.equals(other.partColor))
			return false;
		if (partNum == null) {
			if (other.partNum != null)
				return false;
		} else if (!partNum.equals(other.partNum))
			return false;
		if (partQty == null) {
			if (other.partQty != null)
				return false;
		} else if (!partQty.equals(other.partQty))
			return false;
		if (supplierLotNumber == null) {
			if (other.supplierLotNumber != null)
				return false;
		} else if (!supplierLotNumber.equals(other.supplierLotNumber))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LotTraceabilityDTO [lsn=");
		builder.append(lsn);
		builder.append(", kdLotNumber=");
		builder.append(kdLotNumber);
		builder.append(", supplierLotNumber=");
		builder.append(supplierLotNumber);
		builder.append(", captureDate=");
		builder.append(captureDate);
		builder.append(", captureTime=");
		builder.append(captureTime);
		builder.append(", partNum=");
		builder.append(partNum);
		builder.append(", partColor=");
		builder.append(partColor);
		builder.append(", partQty=");
		builder.append(partQty);
		builder.append("]");
		return builder.toString();
	}

}
