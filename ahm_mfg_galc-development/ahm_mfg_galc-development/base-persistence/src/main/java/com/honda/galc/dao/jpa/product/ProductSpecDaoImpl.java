package com.honda.galc.dao.jpa.product;


import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.ProductSpecDao;
import com.honda.galc.entity.product.ProductSpec;
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
public abstract class ProductSpecDaoImpl<E extends ProductSpec,K>  extends BaseDaoImpl<E,K>implements ProductSpecDao<E,K> {

    private static final long serialVersionUID = 1L;
    
    protected static final String QUERY_YEAR_CODES = "select distinct p.modelYearCode from ";
    protected static final String QUERY_SPEC_CODES = "select distinct p.productSpecCode from ";
    protected static final String QUERY_MODEL_CODES = "select distinct p.modelCode from ";
    private static final String WHERE_CLAUSE_AFTER_MODEL_YEAR_DESCRIPTION = " p where p.modelYearDescription >= :modelYearDescription order by p.modelYearDescription";
    protected static final String QUERY_MODEL_TYPE_CODES = "select distinct p.modelTypeCode from ";

    public List<E> findAllByPrefix(String prefix) {
		return new ArrayList<E>();
	}
    
    @SuppressWarnings("unchecked")
    public List<String> findAllModelYearCodes(String productType) {
        return entityManager.createQuery(QUERY_YEAR_CODES + entityClass.getSimpleName() + " p").getResultList();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<String> findAllModelYearCodesAfterModelYearDescription(String modelYearDescription) {
        return findResultListByQuery(QUERY_YEAR_CODES + entityClass.getSimpleName() + WHERE_CLAUSE_AFTER_MODEL_YEAR_DESCRIPTION, Parameters.with("modelYearDescription", modelYearDescription));
    }
    
    @SuppressWarnings("unchecked")
	public List<String> findAllProductSpecCodes(String productType) {
        return entityManager.createQuery(QUERY_SPEC_CODES + entityClass.getSimpleName() + " p").getResultList();
	}
    
    @SuppressWarnings("unchecked")
	public List<String> findAllModelCodes(String productType) {
		return entityManager.createQuery(QUERY_MODEL_CODES + entityClass.getSimpleName() + " p").getResultList();
	}
    
	public List<E> findAllByModelYearCode(String yearCode,String productType) {
		return findAll(Parameters.with("modelYearCode", yearCode));
	}
	
	/**
	 * @RGALCDEV-1628
	 * This alternative implementation of findByProductSpecCode is used
	 * when an isolated data source is required by the execution flow
	 * of the invoking service.
	 * 
	 * @param productSpecCode
	 * @param productType
	 * @return
	 */
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public E findByProductSpecCode_NoTxn(String productSpecCode,String productType) {
		
		return findByProductSpecCode(productSpecCode, productType);
	}

	/**
	 * returns where clause that matches product spec code
	 * Intended to be overridden by inherited classes
	 */
	public String getSpecCodeMatchSql(String productSpecCode) {
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findAllModelTypeCodes(String productType) {
		return entityManager.createQuery(QUERY_MODEL_TYPE_CODES + entityClass.getSimpleName() + " p").getResultList();
	}
}
