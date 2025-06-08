package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DunnageContentDao;
import com.honda.galc.entity.product.DunnageContent;
import com.honda.galc.entity.product.DunnageContentId;
import com.honda.galc.service.Parameters;

public class DunnageContentDaoImpl extends BaseDaoImpl<DunnageContent, DunnageContentId> implements DunnageContentDao{
	
	private static final String FIND_ALL_PRODUCTS_IN_DUNNAGE = " select dunn.* from galadm.DUNNAGE_CONTENT_TBX dunn  " +
			" where dunn.dunnage_id = ?1 " +
			" order by dunn.create_timestamp desc ";
	
	private static final String FIND_ALL_PRODUCT_IDS_IN_DUNNAGE = " select dunn.product_Id from galadm.DUNNAGE_CONTENT_TBX dunn  " +
			" where dunn.dunnage_id = ?1 " +
			" order by dunn.create_timestamp desc ";
	
	public List<DunnageContent> findAllProductsInDunnage(String dunnageId){
		Parameters params = Parameters.with("1", dunnageId);
		return findAllByNativeQuery(FIND_ALL_PRODUCTS_IN_DUNNAGE, params);
	}
	
	public List<String> findAllProductIdsInDunnage(String dunnageId) {
		return findAllByNativeQuery(FIND_ALL_PRODUCT_IDS_IN_DUNNAGE, Parameters.with("1", dunnageId), String.class);
	}
	
	public DunnageContent findById(String dunnageNumber){
		return findFirst(Parameters.with("id.dunnageId", dunnageNumber));
	}
}
