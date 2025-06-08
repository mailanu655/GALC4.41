package com.honda.galc.entity.product;

import java.util.List;

import com.honda.galc.dto.qi.QiMtcToEntryModelDto;
import com.honda.galc.service.IDaoService;

public interface BaseProductSpecDao<E extends BaseProductSpec, K> extends IDaoService<E,K>{
	public E findByKey(K productSpecCode);
	public E findByProductSpecCode(String productSpecCode,String productType);
	
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
	public E findByProductSpecCode_NoTxn(String productSpecCode,String productType);
		
	/**
	 * This Method is used to populate available Mtc model based on filter
	 * 
	 * @return
	 */
	public List<QiMtcToEntryModelDto> findAllMtcModelYearCodesByFilter(String filter,String productType);	
	public List<String> findAllModelCodes(String productType);
	/**
     * only populate the product spec code and individual year code, type code etc
     * to save query time
     * @return
     */
	public List<E> findAllProductSpecCodesOnly(String productType);
	
	/**
	 * returns the product spec code match where clause String
	 * 
	 * @param productSpecCode
	 * @return
	 */
    public String getSpecCodeMatchSql(String productSpecCode);
    
    
    /**
     * returns the product spec code by pruduct id
     * @param productId
     * @return
     */
    public E findByProductId(String productId);
}
