package com.honda.galc.dao.product;

import java.sql.Date;
import java.util.List;

import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.IDaoService;

public interface PurchaseContractDao extends IDaoService<PurchaseContract, String>{

	public List<PurchaseContract> findBySpecCode(String modelCode, String modelTypeCode, String extColorCode);
	
	public List<PurchaseContract> findAllBySalesMtc(String salesModel, String salesType, String salesExtColor);
	
	public List<PurchaseContract> findAllBySalesMtcAndOrderDueDate(String salesModel, String salesType, String salesExtColor, Date orderDueDate);
	
	public PurchaseContract findByContractNoAndSpecCode(String purchaseContractNo, String modelCode, String modelTypeCode, String optionCode, String extColorCode, String intColorCode);
	/**
	 * find all purchase contracts matching to product spec code
	 * @param specCode
	 * @return
	 */
	public List<PurchaseContract> findByProductSpecCode(String specCode);
	
	public List<PurchaseContract> getAllByOrderDueDate(Date OrderDueDate);

}
