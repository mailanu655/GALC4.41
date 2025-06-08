package com.honda.galc.service.msip.dto.inbound;

import com.honda.galc.util.ToStringUtil;

public class Gal102DetailRecord   implements IMsipInboundDto { 
	private static final long serialVersionUID = 1L;
	private String subCode;
	private String dataCode;
	private String shipper;
	private String purchaseContractNumber;
	private String recordId;
	private String salesModelId;
	private String salesModelTypeCode;
	private String salesModelOptionCode;
	private String factoryColorCode;
	private String salesInteriorColorCode;
	private String currencyCode;
	private String currencySettlementCode;
	private int orderedUnit;
	private String prodGroupCode;
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

	public String getSalesModelId()  {
		return salesModelId;
	}

	public void setSalesModelId(String newSalesModelId)  {
		salesModelId=newSalesModelId;
	}

	public String getSalesModelTypeCode()  {
		return salesModelTypeCode;
	}

	public void setSalesModelTypeCode(String newSalesModelTypeCode)  {
		salesModelTypeCode=newSalesModelTypeCode;
	}

	public String getSalesModelOptionCode()  {
		return salesModelOptionCode;
	}

	public void setSalesModelOptionCode(String newSalesModelOptionCode)  {
		salesModelOptionCode=newSalesModelOptionCode;
	}

	public String getFactoryColorCode()  {
		return factoryColorCode;
	}

	public void setFactoryColorCode(String newFactoryColorCode)  {
		factoryColorCode=newFactoryColorCode;
	}

	public String getSalesInteriorColorCode()  {
		return salesInteriorColorCode;
	}

	public void setSalesInteriorColorCode(String newSalesInteriorColorCode)  {
		salesInteriorColorCode=newSalesInteriorColorCode;
	}

	public String getCurrencyCode()  {
		return currencyCode;
	}

	public void setCurrencyCode(String newCurrencyCode)  {
		currencyCode=newCurrencyCode;
	}

	public String getCurrencySettlementCode()  {
		return currencySettlementCode;
	}

	public void setCurrencySettlementCode(String newCurrencySettlementCode)  {
		currencySettlementCode=newCurrencySettlementCode;
	}

	public int getOrderedUnit()  {
		return orderedUnit;
	}

	public void setOrderedUnit(int newOrderedUnit)  {
		orderedUnit=newOrderedUnit;
	}

	public String getProdGroupCode()  {
		return prodGroupCode;
	}

	public void setProdGroupCode(String newProdGroupCode)  {
		prodGroupCode=newProdGroupCode;
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
		result = prime * result + ((salesModelId == null) ? 0 : salesModelId.hashCode());
		result = prime * result + ((salesModelTypeCode == null) ? 0 : salesModelTypeCode.hashCode());
		result = prime * result + ((salesModelOptionCode == null) ? 0 : salesModelOptionCode.hashCode());
		result = prime * result + ((factoryColorCode == null) ? 0 : factoryColorCode.hashCode());
		result = prime * result + ((salesInteriorColorCode == null) ? 0 : salesInteriorColorCode.hashCode());
		result = prime * result + ((currencyCode == null) ? 0 : currencyCode.hashCode());
		result = prime * result + ((currencySettlementCode == null) ? 0 : currencySettlementCode.hashCode());
		result = prime * result + orderedUnit;
		result = prime * result + ((prodGroupCode == null) ? 0 : prodGroupCode.hashCode());
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
		Gal102DetailRecord other = (Gal102DetailRecord) obj;
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
		if (salesModelId == null) {
			if (other.salesModelId != null)
				return false;
		} else if (!salesModelId.equals(other.salesModelId))
			return false;
		if (salesModelTypeCode == null) {
			if (other.salesModelTypeCode != null)
				return false;
		} else if (!salesModelTypeCode.equals(other.salesModelTypeCode))
			return false;
		if (salesModelOptionCode == null) {
			if (other.salesModelOptionCode != null)
				return false;
		} else if (!salesModelOptionCode.equals(other.salesModelOptionCode))
			return false;
		if (factoryColorCode == null) {
			if (other.factoryColorCode != null)
				return false;
		} else if (!factoryColorCode.equals(other.factoryColorCode))
			return false;
		if (salesInteriorColorCode == null) {
			if (other.salesInteriorColorCode != null)
				return false;
		} else if (!salesInteriorColorCode.equals(other.salesInteriorColorCode))
			return false;
		if (currencyCode == null) {
			if (other.currencyCode != null)
				return false;
		} else if (!currencyCode.equals(other.currencyCode))
			return false;
		if (currencySettlementCode == null) {
			if (other.currencySettlementCode != null)
				return false;
		} else if (!currencySettlementCode.equals(other.currencySettlementCode))
			return false;
		if (orderedUnit != other.orderedUnit) 
			return false;
		if (prodGroupCode == null) {
			if (other.prodGroupCode != null)
				return false;
		} else if (!prodGroupCode.equals(other.prodGroupCode))
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
