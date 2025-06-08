package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.product.ProductSpecCodeDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.ProductSpecCodeDef;
import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.entity.product.ProductSpecCode;
import com.honda.galc.entity.product.ProductSpecCodeId;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.Parameters;

/**
 * 
 * <h3>ProductSpecCodeDaoImpl Class description</h3>
 * <p> ProductSpecCodeDaoImpl description </p>
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
 * @author Jeffray Huang<br>
 * Feb 16, 2012
 *
 *
 */
public class ProductSpecCodeDaoImpl extends ProductSpecDaoImpl<ProductSpecCode,ProductSpecCodeId> implements ProductSpecCodeDao {

    private static final long serialVersionUID = 1L;
    private String UPDATE_SPEC_CODE="UPDATE GALADM.PRODUCT_SPEC_CODE_TBX t0 SET t0.PRODUCT_SPEC_CODE = ?1 WHERE (t0.PRODUCT_TYPE = ?2 AND t0.PRODUCT_SPEC_CODE = ?3)";
    private final static String QUERY_SPEC_CODES_ONLY = "select a.id, a.modelYearCode,a.modelCode,a.modelTypeCode,a.modelOptionCode,a.extColorCode,a.intColorCode from ProductSpecCode a";
	private final static String FIND_ALL_BY_PRODUCT_TYPE_AND_PREFIX = "select s from ProductSpecCode s where s.id.productType = :productType and s.id.productSpecCode like :prefix";
	private static final String FIND_BY_FILTER_MODEL_CODE = " select MODEL_YEAR_CODE, MODEL_CODE,MODEL_YEAR_DESCRIPTION from GALADM.PRODUCT_SPEC_CODE_TBX  where PRODUCT_TYPE = ?1 and (concat(MODEL_YEAR_CODE, MODEL_CODE) like ?2 or MODEL_YEAR_DESCRIPTION like ?2) group by MODEL_YEAR_CODE, MODEL_CODE,MODEL_YEAR_DESCRIPTION";
    private static final String WHERE_CLAUSE_PRODUCT_TYPE = " p where p.id.productType = ";
	private static final String FIND_BY_FILTER_MODEL_CODE_FOR_DIE_CAST = "select MODEL_CODE,MODEL_YEAR_DESCRIPTION from GALADM.PRODUCT_SPEC_CODE_TBX  where PRODUCT_TYPE = ?1 and MODEL_CODE like ?2 or MODEL_YEAR_DESCRIPTION like ?2 group by MODEL_CODE,MODEL_YEAR_DESCRIPTION";
        
    @Autowired
	private ProductTypeDao productTypeDao;
    
    @SuppressWarnings("unchecked")
    public List<String> findAllModelYearCodes(String productType) {
        return entityManager.createQuery(QUERY_YEAR_CODES + entityClass.getSimpleName() + WHERE_CLAUSE_PRODUCT_TYPE + quote(productType)).getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<String> findAllProductSpecCodes(String productType) {
        return entityManager.createQuery(QUERY_SPEC_CODES + entityClass.getSimpleName() + WHERE_CLAUSE_PRODUCT_TYPE + quote(productType)).getResultList();
	}
    
    @SuppressWarnings("unchecked")
	public List<String> findAllModelCodes(String productType) {
 		return entityManager.createQuery(QUERY_MODEL_CODES + entityClass.getSimpleName() + WHERE_CLAUSE_PRODUCT_TYPE + quote(productType) ).getResultList();
	}
    
	public List<ProductSpecCode> findAllByModelYearCode(String yearCode,String productType) {
		return findAll(Parameters.with("modelYearCode", yearCode).put("productType", productType));
	}

    
    public ProductSpecCode findByProductSpecCode(String productSpecCode,String productType) {
    	
    	ProductTypeData productTypeData = productTypeDao.findByKey(productType);
    	String querySpecCode = productSpecCode;
    	
    	
     	if(productTypeData != null && productTypeData.getProductSpecCodeDefs().size() > 0 && 
    			productTypeData.getProductSpecCodeDefs().get(0) == ProductSpecCodeDef.MODEL){
    		
     		querySpecCode = ProductSpecCodeDef.MODEL_YEAR_WILDCARD + querySpecCode;
    	} 
    	
    	ProductSpecCodeId id = new ProductSpecCodeId();
		id.setProductSpecCode(querySpecCode);
		id.setProductType(productType);
		return findByKey(id);
	}
    
    private String quote(String aStr){
    	return "'" + aStr + "'";
    }


	/**
     * only populate the product spec code and individual year code, type code etc
     * to save query time
     * @return
     */
    @SuppressWarnings("unchecked")
	public List<ProductSpecCode> findAllProductSpecCodesOnly(){
    	
    	List<ProductSpecCode> prodSpecs = new ArrayList<ProductSpecCode>();
    	List result =  findAllByQuery(QUERY_SPEC_CODES_ONLY);
    	
    	Iterator it = result.iterator();
    	while(it.hasNext()) {
    		Object[] objects = (Object[]) it.next();
    		
    		ProductSpecCode productSpecCode = new ProductSpecCode();
    		productSpecCode.setId((ProductSpecCodeId)objects[0]);
    		productSpecCode.setModelYearCode((String)objects[1]);
    		productSpecCode.setModelCode((String)objects[2]);
    		productSpecCode.setModelTypeCode((String)objects[3]);
    		productSpecCode.setModelOptionCode((String)objects[4]);
    		productSpecCode.setExtColorCode((String)objects[5]);
    		productSpecCode.setIntColorCode((String)objects[6]);
    		prodSpecs.add(productSpecCode);
    	}
    	
    	return prodSpecs;
    }
    
	public List<ProductSpecCode> findAllByProductTypeAndPrefix(String productType, String prefix) {
    	Parameters params = Parameters.with("productType", productType);
    	params.put("prefix", prefix + "%");
		return findAllByQuery(FIND_ALL_BY_PRODUCT_TYPE_AND_PREFIX, params);
	}
    

	public List<ProductSpecCode> findAllByProcessPointId(String processPointId) {
		// TODO Auto-generated method stub
		return null;
	}
	public ProductSpecCode findByProductId(String productId) {
		return null;
	}

	public List<ProductSpecCode> findAllProductSpecCodesOnly(String productType) {
		return findAll(Parameters.with("id.productType", productType));
	}

    @Transactional
	public void updateProductSpecCode(ProductSpecCode prodSpec,	String productSpecCode) {
    	Parameters params = Parameters.with("1", productSpecCode);
    	params.put("2", prodSpec.getId().getProductType());
		params.put("3", prodSpec.getId().getProductSpecCode());
		
		
		executeNativeUpdate(UPDATE_SPEC_CODE, params);
	}
	/**
	 * This Method is used to populate available Mtc model based on filter
	 * 
	 * @return
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter, String productType) {
		Parameters param = Parameters.with("1", StringUtils.trimToEmpty(productType)).put("2","%" + StringUtils.trimToEmpty(filter) + "%");
		List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_BY_FILTER_MODEL_CODE, param,QiMtcToEntryModelDto.class);
		return resultList;
	}

	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesForDieCastByFilter(String filter, String productType) {
		Parameters param = Parameters.with("1", StringUtils.trimToEmpty(productType)).put("2","%" + StringUtils.trimToEmpty(filter) + "%");
		List<QiMtcToEntryModelDto> resultList = findAllByNativeQuery(FIND_BY_FILTER_MODEL_CODE_FOR_DIE_CAST, param,QiMtcToEntryModelDto.class);
		return resultList;
	}
}
