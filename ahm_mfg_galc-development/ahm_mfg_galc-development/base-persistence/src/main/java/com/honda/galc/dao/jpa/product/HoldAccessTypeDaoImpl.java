package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.HoldAccessTypeDao;
import com.honda.galc.entity.product.HoldAccessType;
import com.honda.galc.entity.product.HoldAccessTypeId;
import com.honda.galc.util.StringUtil;


public class HoldAccessTypeDaoImpl  extends BaseDaoImpl<HoldAccessType,HoldAccessTypeId> implements HoldAccessTypeDao{

	
	private static final String FIND_ALL_BY_MATCHING_SECURITY_GROUPS = " select * from Hold_Access_Type_Tbx p  where p.SECURITY_GRP in ";
	private static final String FIND_ALL_BY_MATCHING_PRODUCT_TYPE = "select * from HOLD_ACCESS_TYPE_TBX p where p.PRODUCT_TYPE = ";

	@Override
	public List<HoldAccessType> findAllByMatchingSecurityGroups(List<String> groups) {
		String sql = FIND_ALL_BY_MATCHING_SECURITY_GROUPS +"("+StringUtil.toSqlInString(groups)+")";
		
	  	return findAllByNativeQuery(sql,null);
	}

	@Override
	public List<HoldAccessType> findAllByMatchingProductType(String productType) {
		String sql = FIND_ALL_BY_MATCHING_PRODUCT_TYPE + "" + StringUtil.toSqlInString(productType);
		
		return findAllByNativeQuery(sql, null);
	}

	

}
