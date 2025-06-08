package com.honda.galc.dao.jpa.product;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PurchaseContractDao;
import com.honda.galc.entity.product.PurchaseContract;
import com.honda.galc.service.Parameters;

import java.sql.Date;
import java.util.List;



public class PurchaseContractDaoImpl extends BaseDaoImpl<PurchaseContract,String> implements PurchaseContractDao {
	
	private static final String SELECT_PURCHASE_CONTRACT = 
			"select p "
			+ "from PurchaseContract p where p.salesModelCode = :modelCode and p.salesModelTypeCode = :modelTypeCode "
			+ "and p.salesExtColorCode= :extColorCode "
			+ "and p.orderUnit > p.shipUnit "
			+ "order by p.receiveDate asc, p.purchaseContractNumber asc ";
	
	private static final String SELECT_PURCHASE_CONTRACT_PCNO_AND_SPECCODE = 
			"select p "
			+ "from PurchaseContract p where p.purchaseContractNumber = :purchaseContractNo "
			+ "and p.salesModelCode = :modelCode and p.salesModelTypeCode = :modelTypeCode "
			+ "and p.salesModelOptionCode = :optionCode and p.salesExtColorCode= :extColorCode "
			+ "and p.salesIntColorCode = :intColorCode "
			+ "and p.orderUnit > p.shipUnit "
			+ "order by p.receiveDate asc, p.purchaseContractNumber asc ";
	
	private static final String SELECT_PURCHASE_CONTRACT_BY_SPEC = 
			"SELECT p.* FROM GALADM.GAL144TBX s, GALADM.GAL313TBX p "
			+ "WHERE s.PRODUCT_SPEC_CODE= ?1" 
			+ "AND p.SALES_MODEL_CODE = COALESCE(s.COMMON_SALES_MODEL_CODE,s.SALES_MODEL_CODE) " 
			+ "AND s.SALES_EXT_COLOR_CODE = p.SALES_EXT_COLOR_CODE "
			+ "AND s.SALES_MODEL_TYPE_CODE = p.SALES_MODEL_TYPE_CODE " 
			+ "AND p.ORDER_UNIT > p.SHIP_UNIT " 
			+ "ORDER BY p.ORDER_DUE_DATE";
	
	public List<PurchaseContract> findBySpecCode(String modelCode, String modelTypeCode, String extColorCode) {
		Parameters p = Parameters.with("modelCode", modelCode);
		p.put("modelTypeCode", modelTypeCode);
		p.put("extColorCode", extColorCode);
		return findAllByQuery(SELECT_PURCHASE_CONTRACT, p);
	}
	
	public List<PurchaseContract> findAllBySalesMtc(String salesModel, String salesType, String salesExtColor) {
		Parameters p = Parameters.with("salesModelCode", salesModel);
		p.put("salesModelTypeCode", salesType);
		p.put("salesExtColorCode", salesExtColor);
		String[] orderBy = {"receiveDate", "purchaseContractNumber"};
		return findAll(p, orderBy);
	}
	
	public List<PurchaseContract> findAllBySalesMtcAndOrderDueDate(String salesModel, String salesType, String salesExtColor, Date orderDueDate) {
		if (orderDueDate == null) return this.findAllBySalesMtc(salesModel, salesType, salesExtColor);
		Parameters p = Parameters.with("salesModelCode", salesModel);
		p.put("salesModelTypeCode", salesType);
		p.put("salesExtColorCode", salesExtColor);
		p.put("orderDueDate", orderDueDate);
		String[] orderBy = {"receiveDate", "purchaseContractNumber"};
		return findAll(p, orderBy);
	}

	public PurchaseContract findByContractNoAndSpecCode(String purchaseContractNo, String modelCode,
			String modelTypeCode, String optionCode, String extColorCode, String intColorCode) {
		Parameters p = Parameters.with("purchaseContractNo", purchaseContractNo);
		p.put("modelCode", modelCode);
		p.put("modelTypeCode", modelTypeCode);
		p.put("optionCode", optionCode);
		p.put("extColorCode", extColorCode);
		p.put("intColorCode", intColorCode);
		PurchaseContract pContract = findFirstByQuery(SELECT_PURCHASE_CONTRACT_PCNO_AND_SPECCODE, p);

		return pContract;
	}

	@Override
	public List<PurchaseContract> findByProductSpecCode(String specCode) {
		Parameters params = Parameters.with("1", specCode);
		return findAllByNativeQuery(SELECT_PURCHASE_CONTRACT_BY_SPEC, params);
	}
	
	public List<PurchaseContract> getAllByOrderDueDate(Date orderDueDate) {
		Parameters parameters = Parameters.with("orderDueDate", orderDueDate);	
		return findAll(parameters);
	}

}
