package com.honda.galc.oif.dto;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.util.GPCSData;

public class PurchaseContractHeaderDTO   implements IOutputFormat { 

	@GPCSData("HDR_SUB_CODE")
	private String subCode;

	@GPCSData("HDR_DATA_CODE")
	private String dataCode;

	@GPCSData("HDR_SHIPPER")
	private String shipper;

	@GPCSData("HDR_PURCHASE_CONTRACT_NUMBER")
	private String purchaseContractNumber;

	@GPCSData("HDR_RECORD_ID")
	private String recordId;

	@GPCSData("HDR_PORT_CODE")
	private String portCode;

	@GPCSData("HDR_PORT_NAME")
	private String portName;

	@GPCSData("HDR_LOGICAL_PRODUCTION_MONTH")
	private String logicalProductionMonth;

	@GPCSData("HDR_QUANTITY")
	private int quantity;

	@GPCSData("HDR_ORDER_DUE_DATE")
	private String orderDueDate;

	@GPCSData("HDR_PRODUCT_GROUP")
	private String productGroup;

	@GPCSData("HDR_DELIVERY_FORM_CODE")
	private String deliveryFormCode;

	@GPCSData("HDR_SPACE")
	private String space;

	@GPCSData("HDR_RECORD_SEQUENCE_NUMBER")
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

	public String getPortCode()  {
		return StringUtils.trim(portCode);
	}

	public void setPortCode(String newPortCode)  {
		portCode=newPortCode;
	}

	public String getPortName()  {
		return StringUtils.trim(portName);
	}

	public void setPortName(String newPortName)  {
		portName=newPortName;
	}

	public String getLogicalProductionMonth()  {
		return StringUtils.trim(logicalProductionMonth);
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
		return StringUtils.trim(orderDueDate);
	}

	public void setOrderDueDate(String newOrderDueDate)  {
		orderDueDate=newOrderDueDate;
	}

	public String getProductGroup()  {
		return StringUtils.trim(productGroup);
	}

	public void setProductGroup(String newProductGroup)  {
		productGroup=newProductGroup;
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
