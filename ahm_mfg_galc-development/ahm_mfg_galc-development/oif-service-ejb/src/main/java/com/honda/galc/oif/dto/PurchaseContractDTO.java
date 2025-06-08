package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.GPCSData;

public class PurchaseContractDTO   implements IOutputFormat { 


	@GPCSData("SUB_CODE")
	private String subCode;

	@GPCSData("DATA_CODE")
	private String dataCode;

	@GPCSData("SHIPPER")
	private String shipper;

	@GPCSData("PURCHASE_CONTRACT_NUMBER")
	private String purchaseContractNumber;

	@GPCSData("RECORD_ID")
	private String recordId;

	@GPCSData("SALES_MODEL_ID")
	private String salesModelId;

	@GPCSData("SALES_MODEL_TYPE_CODE")
	private String salesModelTypeCode;

	@GPCSData("SALES_MODEL_OPTION_CODE")
	private String salesModelOptionCode;

	@GPCSData("FACTORY_COLOR_CODE")
	private String factoryColorCode;

	@GPCSData("SALES_INTERIOR_COLOR_CODE")
	private String salesInteriorColorCode;

	@GPCSData("CURRENCY_CODE")
	private String currencyCode;

	@GPCSData("CURRENCY_SETTLEMENT_CODE")
	private String currencySettlementCode;

	@GPCSData("ORDERED_UNIT")
	private int orderedUnit;

	@GPCSData("PROD_GROUP_CODE")
	private String prodGroupCode;

	@GPCSData("DELIVERY_FORM_CODE")
	private String deliveryFormCode;

	@GPCSData("SPACE")
	private String space;

	@GPCSData("RECORD_SEQUENCE_NUMBER")
	private int recordSequenceNumber;

	public String getSubCode()  {
		return StringUtils.trim(subCode);
	}

	public void setSubCode(String newSubCode)  {
		subCode=newSubCode;
	}

	public String getDataCode()  {
		return StringUtils.trim(dataCode);
	}

	public void setDataCode(String newDataCode)  {
		dataCode=newDataCode;
	}

	public String getShipper()  {
		return StringUtils.trim(shipper);
	}

	public void setShipper(String newShipper)  {
		shipper=newShipper;
	}

	public String getPurchaseContractNumber()  {
		return StringUtils.trim(purchaseContractNumber);
	}

	public void setPurchaseContractNumber(String newPurchaseContractNumber)  {
		purchaseContractNumber=newPurchaseContractNumber;
	}

	public String getRecordId()  {
		return StringUtils.trim(recordId);
	}

	public void setRecordId(String newRecordId)  {
		recordId=newRecordId;
	}

	public String getSalesModelId()  {
		return StringUtils.trim(salesModelId);
	}

	public void setSalesModelId(String newSalesModelId)  {
		salesModelId=newSalesModelId;
	}

	public String getSalesModelTypeCode()  {
		return StringUtils.trim(salesModelTypeCode);
	}

	public void setSalesModelTypeCode(String newSalesModelTypeCode)  {
		salesModelTypeCode=newSalesModelTypeCode;
	}

	public String getSalesModelOptionCode()  {
		return StringUtils.trim(salesModelOptionCode);
	}

	public void setSalesModelOptionCode(String newSalesModelOptionCode)  {
		salesModelOptionCode=newSalesModelOptionCode;
	}

	public String getFactoryColorCode()  {
		return StringUtils.trim(factoryColorCode);
	}

	public void setFactoryColorCode(String newFactoryColorCode)  {
		factoryColorCode=newFactoryColorCode;
	}

	public String getSalesInteriorColorCode()  {
		return StringUtils.trim(salesInteriorColorCode);
	}

	public void setSalesInteriorColorCode(String newSalesInteriorColorCode)  {
		salesInteriorColorCode=newSalesInteriorColorCode;
	}

	public String getCurrencyCode()  {
		return StringUtils.trim(currencyCode);
	}

	public void setCurrencyCode(String newCurrencyCode)  {
		currencyCode=newCurrencyCode;
	}

	public String getCurrencySettlementCode()  {
		return StringUtils.trim(currencySettlementCode);
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
		return StringUtils.trim(prodGroupCode);
	}

	public void setProdGroupCode(String newProdGroupCode)  {
		prodGroupCode=newProdGroupCode;
	}

	public String getDeliveryFormCode()  {
		return StringUtils.trim(deliveryFormCode);
	}

	public void setDeliveryFormCode(String newDeliveryFormCode)  {
		deliveryFormCode=newDeliveryFormCode;
	}

	public String getSpace()  {
		return StringUtils.trim(space);
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

	public void initialize(Map<String,String>  inputValues)  {
		if(inputValues == null || inputValues.isEmpty())  return;
		Field[] fields = this.getClass().getDeclaredFields();
		for(Field f : fields)  {
			GPCSData a1 = f.getAnnotation(GPCSData.class);
			if(!inputValues.containsKey(a1.value()))  continue;
			if(f.getType().isAssignableFrom(String.class))  {
				try {
					f.set(this, inputValues.get(a1.value()));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
