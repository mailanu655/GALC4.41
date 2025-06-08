package com.honda.galc.dao.jpa.product;


import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.PartByProductSpecCodeDao;
import com.honda.galc.entity.product.FrameSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartByProductSpecCodeId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.Parameters;
import com.honda.galc.util.ProductSpecUtil;
;
public class PartByProductSpecCodeDaoImpl extends BaseDaoImpl<PartByProductSpecCode,PartByProductSpecCodeId> implements PartByProductSpecCodeDao {
	
	public List<PartByProductSpecCode> findByProductSpec(ProductSpec productSpec) {
		return findAll(prepareParameters(productSpec));
	}
	
	private Parameters prepareParameters(ProductSpec productSpec) {
		Parameters params = Parameters.with("id.modelYearCode", productSpec.getModelYearCode());
		params.put("id.modelCode", productSpec.getModelCode());
		params.put("id.modelTypeCode", productSpec.getModelTypeCode());
		params.put("id.modelOptionCode", productSpec.getModelOptionCode());
		
		if(productSpec instanceof FrameSpec){
			params.put("id.extColorCode", ((FrameSpec)productSpec).getExtColorCode());
			params.put("id.intColorCode", ((FrameSpec)productSpec).getIntColorCode());
		}
		
		return params;
	}
	
	public PartByProductSpecCode getPartId(String productSpecCode, String partName) {
		
		Parameters params = new Parameters();
		params.put("id.productSpecCode", productSpecCode);
		params.put("id.partName", partName);
			
		return (PartByProductSpecCode) findFirst(params);
	}
	
	public List<PartByProductSpecCode> findAllByPartName(String partName) {
		Parameters params = new Parameters();
		params.put("id.partName",partName);
		String[] orderBy = {"id.partName"};
		return findAll(params, orderBy, true);	
	}  
	
    public List<PartByProductSpecCode> findAllByPartNameAndProductSpec(String partName, String productSpec, String productType) {
    	List<PartByProductSpecCode> parts = findAllByPartName(partName);
    	
    	return ProductSpecUtil.getMatchedRuleList(productSpec, productType, parts, PartByProductSpecCode.class);
    }
    
}
