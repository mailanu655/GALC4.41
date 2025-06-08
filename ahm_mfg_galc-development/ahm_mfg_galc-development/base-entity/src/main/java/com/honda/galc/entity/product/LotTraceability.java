package com.honda.galc.entity.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.entity.AuditEntry;

/** * * 
* @version 1.0 
* @author Vivek Bettada 
* @since Mar 2, 2015
*/
@Entity
@Table(name="LOT_TRACE_TBX")
public class LotTraceability extends AuditEntry {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private LotTraceabilityId id;

	@Column(name = "SUPPLIER_LOT")
	private String supplierLotNumber;

	@Column(name = "CAPTURE_TIMESTAMP")
	private Date captureDate;

	@Column(name = "PART_NUM")
	private String partNum;

	@Column(name = "PART_COLOR")
	private String partColor;

	@Column(name = "PART_QTY")
	private String partQty;

	/**
	 * @return the id
	 */
	public LotTraceabilityId getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(LotTraceabilityId id) {
		this.id = id;
	}

	public String getSupplierLotNumber()  {
		return StringUtils.trim(supplierLotNumber);
	}

	public void setSupplierLotNumber(String newSupplierLotNumber)  {
		supplierLotNumber=newSupplierLotNumber;
	}

	public Date getCaptureDate()  {
		return captureDate;
	}

	public void setCaptureDate(Date newCaptureDate)  {
		captureDate=newCaptureDate;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LotTraceability other = (LotTraceability) obj;
		if (captureDate == null) {
			if (other.captureDate != null)
				return false;
		} else if (!captureDate.equals(other.captureDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
		return "LotTraceability [id=" + id + ", supplierLotNumber=" + supplierLotNumber + ", captureDate="
				+ captureDate + ", partNum=" + partNum + ", partColor=" + partColor + ", partQty=" + partQty + "]";
	}

}
