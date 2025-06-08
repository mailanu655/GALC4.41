package com.honda.galc.dao.jpa.product;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductIdMaskDao;
import com.honda.galc.entity.product.ProductIdMask;
import com.honda.galc.entity.product.ProductIdMaskId;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ProductIdMaskDaoImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductIdMaskDaoImpl description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Kamlesh Maharjan
 * March 05, 2016
 *
 */

public class ProductIdMaskDaoImpl extends BaseDaoImpl<ProductIdMask, ProductIdMaskId> implements ProductIdMaskDao{

	private final String FIND_ALL_BY_MASK_AND_PROCESS_POINT_ID ="select r " +
			"from ProductIdMask r where r.id.productIdMask=:productIdMask and r.id.processPointId=:processPointId";
	
	private final String FIND_ALL_BY_PRODUCT_TYPE ="SELECT distinct(PRODUCT_ID_MASK) FROM galadm.PRODUCT_ID_MASK_TBX where PRODUCT_TYPE= ?1";
	
	private final String FIND_ALL_BY_PROCESS_POINT_ID ="select r from ProductIdMask r where r.id.processPointId = :processPointId";
	
	private static String SELECT_ALL_MATCH_SPEC_CODE = "select r from ProductIdMask r where r.id.processPointId = :processPointId and r.productSpecCode ";
	
	private final String FIND_ALL_BY_PROCESS_POINT_IDS ="select r from ProductIdMask r where r.id.processPointId in (:processPointIds)";
	
	private final String FIND_ALL_BY_PRODUCT_SPEC_CODE ="select r from ProductIdMask r where r.productSpecCode ";
	
	
	public List<String> findAllByProductType(String productType){
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PRODUCT_TYPE);
		Parameters params = new Parameters(); 
	    params.put("1", productType);
		return findAllByNativeQuery(sb.toString(), params, String.class);		
	}
	
	public List<ProductIdMask> findAllByProcessPointAndProductSpec(String processPointId, String productSpecCode){
		StringBuilder sb = new StringBuilder(SELECT_ALL_MATCH_SPEC_CODE);
		sb.append((productSpecCode.endsWith("%")? "like " : "= "));
		sb.append(":productSpecCode");
		sb.append(" order by r.productSpecCode asc");
		Parameters params = new Parameters(); 
	    params.put("processPointId", processPointId);
	    params.put("productSpecCode", productSpecCode);
		return findAllByQuery(sb.toString(), params);		
	}
	
	public ProductIdMask findByMaskAndProcessPointId(String productIdMask, String processPointId){
		StringBuilder sb = new StringBuilder(FIND_ALL_BY_MASK_AND_PROCESS_POINT_ID);
		Parameters params = new Parameters(); 
	    params.put("productIdMask", productIdMask);
	    params.put("productSpecCode", processPointId);
		return findFirstByQuery(sb.toString(), params);	
	}
	
	public List<ProductIdMask> findAllByProcessPointId(String processPointId){
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_ID);
		Parameters params = new Parameters(); 
	    params.put("processPointId", processPointId);
		return findAllByQuery(sb.toString(), params);		
	}
	
	public List<ProductIdMask> findAllByProcessPointAndProductType(String processPointId, String productType){
		Parameters params = new Parameters();
		params.put("id.processPointId", processPointId);
		params.put("id.productType", productType);
		return findAll(params);
	}

	public List<ProductIdMask> findAllByProcessPointIds(List<String> onProcessPoints) {
		StringBuilder sb = new StringBuilder();
		sb.append(FIND_ALL_BY_PROCESS_POINT_IDS);
		Parameters params = new Parameters(); 
	    params.put("processPointIds", onProcessPoints);
		return findAllByQuery(sb.toString(), params);		
	}

	public List<ProductIdMask> findAllByProductSpecCode(String productSpecCode) {
		Parameters params = Parameters.with("productSpecCode", productSpecCode);	
		StringBuilder sb = new StringBuilder(FIND_ALL_BY_PRODUCT_SPEC_CODE);
		sb.append((productSpecCode.endsWith("%")? "like " : "= "));
		sb.append(":productSpecCode");
		return findAllByQuery(sb.toString(), params);
	}
}