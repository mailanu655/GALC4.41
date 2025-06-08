package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.DunnageHistDao;
import com.honda.galc.entity.product.DunnageHist;
import com.honda.galc.entity.product.DunnageHistId;
import com.honda.galc.service.Parameters;

public class DunnageHistDaoImpl extends BaseDaoImpl<DunnageHist,DunnageHistId> implements DunnageHistDao{

	
	private final String FIND_FIRST_EXPECTED_DUNNAGE_HIST_ID = "select dh.* from galadm.DUNNAGE_HIST_TBX dh  " +
	" where dh.product_id = ?1 " +
	" and dh.dunnage_id = ?2 " +
	" and  dh.off_timestamp is null " +
	" order by dh.create_timestamp desc ";		
	
	public List<DunnageHist> findAll(String productId,String dunnageId){
		Parameters params = Parameters.with("1", productId);
		params.put("2", dunnageId);			
		
		return findAllByNativeQuery(FIND_FIRST_EXPECTED_DUNNAGE_HIST_ID, params);
	}

	public DunnageHist findfirstProduct(String productId){
		Parameters params = Parameters.with("id.productId", productId);
		return findFirst(params);
	}
	
	
	public DunnageHist findByDunnageId(String dunnageId){
		Parameters params = Parameters.with("id.dunnageId", dunnageId);
		return findFirst(params, new String[] { "id.onTimestamp" },false);
	}
	
	public DunnageHist findByProductId(String productId) {
		Parameters params = Parameters.with("1", productId);
		return findFirst(params);
	}
		
	public List<DunnageHist> findAllByProductId(String productId){
		Parameters params = Parameters.with("id.productId", productId);
		return findAll(params);
	}

}
