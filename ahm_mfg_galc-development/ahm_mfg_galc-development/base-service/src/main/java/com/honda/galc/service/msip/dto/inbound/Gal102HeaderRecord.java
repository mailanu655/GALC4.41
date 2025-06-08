package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

public class Gal102HeaderRecord   implements IMsipInboundDto { 
	private static final long serialVersionUID = 1L;
	private String subCode;
	private String dataCode;
	private String shipper;
	private String purchaseContractNumber;
	private String recordId;
	private String portCode;
	private String portName;
	private String logicalProductionMonth;
	private int quantity;
	private String orderDueDate;
	private String productGroup;
	private String deliveryFormCode;
	private String space;
	private int recordSequenceNumber;

	public String getSubCode()  {
		return subCode;
	}

	public void setSubCode(String newSubCode)  {
		subCode=newSubCode;
	}

	public String getDataCode()  {
		return dataCode;
	}

	public void setDataCode(String newDataCode)  {
		dataCode=newDataCode;
	}

	public String getShipper()  {
		return shipper;
	}

	public void setShipper(String newShipper)  {
		shipper=newShipper;
	}

	public String getPurchaseContractNumber()  {
		return purchaseContractNumber;
	}

	public void setPurchaseContractNumber(String newPurchaseContractNumber)  {
		purchaseContractNumber=newPurchaseContractNumber;
	}

	public String getRecordId()  {
		return recordId;
	}

	public void setRecordId(String newRecordId)  {
		recordId=newRecordId;
	}

	public String getPortCode()  {
		return portCode;
	}

	public void setPortCode(String newPortCode)  {
		portCode=newPortCode;
	}

	public String getPortName()  {
		return portName;
	}

	public void setPortName(String newPortName)  {
		portName=newPortName;
	}

	public String getLogicalProductionMonth()  {
		return logicalProductionMonth;
	}

	public void setLogicalProductionMonth(String newLogicalProductionMonth)  {
		logicalProductionMonth=newLogicalProductionMonth;
	}

	public int getQuantity()  {
		return quantity;
	}

	public void setQuantity(int newQuantity)  {
		quantity=newQuantity;
	}

	public String getOrderDueDate()  {
		return orderDueDate;
	}

	public void setOrderDueDate(String newOrderDueDate)  {
		orderDueDate=newOrderDueDate;
	}

	public String getProductGroup()  {
		return productGroup;
	}

	public void setProductGroup(String newProductGroup)  {
		productGroup=newProductGroup;
	}

	public String getDeliveryFormCode()  {
		return deliveryFormCode;
	}

	public void setDeliveryFormCode(String newDeliveryFormCode)  {
		deliveryFormCode=newDeliveryFormCode;
	}

	public String getSpace()  {
		return space;
	}

	public void setSpace(String newSpace)  {
		space=newSpace;
	}

	public int getRecordSequenceNumber()  {
		return recordSequenceNumber;
	}

	public void setRecordSequenceNumber(int newRecordSequenceNumber)  {
		recordSequenceNumber=newRecordSequenceNumber;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subCode == null) ? 0 : subCode.hashCode());
		result = prime * result + ((dataCode == null) ? 0 : dataCode.hashCode());
		result = prime * result + ((shipper == null) ? 0 : shipper.hashCode());
		result = prime * result + ((purchaseContractNumber == null) ? 0 : purchaseContractNumber.hashCode());
		result = prime * result + ((recordId == null) ? 0 : recordId.hashCode());
		result = prime * result + ((portCode == null) ? 0 : portCode.hashCode());
		result = prime * result + ((portName == null) ? 0 : portName.hashCode());
		result = prime * result + ((logicalProductionMonth == null) ? 0 : logicalProductionMonth.hashCode());
		result = prime * result + quantity;
		result = prime * result + ((orderDueDate == null) ? 0 : orderDueDate.hashCode());
		result = prime * result + ((productGroup == null) ? 0 : productGroup.hashCode());
		result = prime * result + ((deliveryFormCode == null) ? 0 : deliveryFormCode.hashCode());
		result = prime * result + ((space == null) ? 0 : space.hashCode());
		result = prime * result + recordSequenceNumber;
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
		Gal102HeaderRecord other = (Gal102HeaderRecord) obj;
		if (subCode == null) {
			if (other.subCode != null)
				return false;
		} else if (!subCode.equals(other.subCode))
			return false;
		if (dataCode == null) {
			if (other.dataCode != null)
				return false;
		} else if (!dataCode.equals(other.dataCode))
			return false;
		if (shipper == null) {
			if (other.shipper != null)
				return false;
		} else if (!shipper.equals(other.shipper))
			return false;
		if (purchaseContractNumber == null) {
			if (other.purchaseContractNumber != null)
				return false;
		} else if (!purchaseContractNumber.equals(other.purchaseContractNumber))
			return false;
		if (recordId == null) {
			if (other.recordId != null)
				return false;
		} else if (!recordId.equals(other.recordId))
			return false;
		if (portCode == null) {
			if (other.portCode != null)
				return false;
		} else if (!portCode.equals(other.portCode))
			return false;
		if (portName == null) {
			if (other.portName != null)
				return false;
		} else if (!portName.equals(other.portName))
			return false;
		if (logicalProductionMonth == null) {
			if (other.logicalProductionMonth != null)
				return false;
		} else if (!logicalProductionMonth.equals(other.logicalProductionMonth))
			return false;
		if (quantity != other.quantity) 
			return false;
		if (orderDueDate == null) {
			if (other.orderDueDate != null)
				return false;
		} else if (!orderDueDate.equals(other.orderDueDate))
			return false;
		if (productGroup == null) {
			if (other.productGroup != null)
				return false;
		} else if (!productGroup.equals(other.productGroup))
			return false;
		if (deliveryFormCode == null) {
			if (other.deliveryFormCode != null)
				return false;
		} else if (!deliveryFormCode.equals(other.deliveryFormCode))
			return false;
		if (space == null) {
			if (other.space != null)
				return false;
		} else if (!space.equals(other.space))
			return false;
		if (recordSequenceNumber != other.recordSequenceNumber) 
			return false;
		return true;
	}

	public String toString() {
		return ToStringUtil.generateJsonToString(this);
	}
	
}
