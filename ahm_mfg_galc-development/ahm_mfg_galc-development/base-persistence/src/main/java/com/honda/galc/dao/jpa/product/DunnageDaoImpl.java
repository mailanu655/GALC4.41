package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DunnageDao;
import com.honda.galc.entity.product.Dunnage;
import com.honda.galc.service.Parameters;

public class DunnageDaoImpl extends BaseDaoImpl<Dunnage, String> implements DunnageDao{
	private static final String FIND_ALL_BY_MATCHING_DUNNAGE = "select d from Dunnage d where trim(d.dunnageId) like :dunnage order by d.dunnageId";
	private static final String FIND_ALL_BY_MATCHING_PRODUCT_ID = "select d.* from galadm.dunnage_tbx d join galadm.dunnage_content_tbx c on d.dunnage_Id = c.dunnage_Id where trim(c.product_Id) like ?1 order by d.dunnage_Id";
	private static final String FIND_ALL_BY_MATCHING_MTOC = "select d from Dunnage d where trim(d.productSpecCode) like :productSpecCode order by d.dunnageId";

	
	public List<Dunnage> findAllByPartialDunnage(String dunnage) {
		String queryString = FIND_ALL_BY_MATCHING_DUNNAGE;
		Parameters params = Parameters.with("dunnage", "%" + dunnage);
		return findAllByQuery(queryString, params);
	}
	
	public List<Dunnage> findAllByPartialProductId(String productId) {
		String queryString = FIND_ALL_BY_MATCHING_PRODUCT_ID;
		Parameters params = Parameters.with("1", "%" + productId);
		return findAllByNativeQuery(queryString, params);
	}
	
	public List<Dunnage> findAllByPartialMtoc(String productSpecCode) {
		String queryString = FIND_ALL_BY_MATCHING_MTOC;
		Parameters params = Parameters.with("productSpecCode", "%" + productSpecCode + "%");
		return findAllByQuery(queryString, params);
	}
}
