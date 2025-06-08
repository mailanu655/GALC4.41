package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.service.Parameters;

public class ExpectedProductDaoImpl extends BaseDaoImpl<ExpectedProduct, String> implements ExpectedProductDao {
	private final String FIND_ALL_FOR_PROCESS_POINT ="select * from galadm.gal135tbx where process_point_id like ?1";
	private final String FIND_FOR_PROCESS_POINT ="select * from galadm.gal135tbx where process_point_id = ?1";
	private final String FIND_FOR_PROCESS_POINT_AND_PRODUCT = "select * from gal135tbx where PROCESS_POINT_ID = ?1 and PRODUCT_ID = ?2";

	@Override
	public List<ExpectedProduct> findAllOrderByProcessPoint() {
		return findAll(null, new String[] {"processPointId"});
	}

	public List<ExpectedProduct> findAllForProcessPoint(String processPoint) {
		
		return findAllByNativeQuery(FIND_ALL_FOR_PROCESS_POINT, Parameters.with("1", processPoint +"%"));
	}
	
	public ExpectedProduct findForProcessPointAndProduct(String processPointId, String productId) {
		
		Parameters pars = new Parameters();
		pars.put("1", processPointId).put("2", productId);
		return findFirstByNativeQuery(FIND_FOR_PROCESS_POINT_AND_PRODUCT, pars);
	}
	
	public ExpectedProduct findForProcessPoint(String processPointId) {
		
		Parameters pars = new Parameters();
		pars.put("1", processPointId);
		return findFirstByNativeQuery(FIND_FOR_PROCESS_POINT, pars);
	}

	public List<ExpectedProduct> findAllByProductId(String productId) 
	{
		Parameters params = Parameters.with("productId", productId);
		return findAll(params);
	}	

}
