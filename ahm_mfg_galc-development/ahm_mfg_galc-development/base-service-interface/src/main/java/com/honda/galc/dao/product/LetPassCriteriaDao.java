package com.honda.galc.dao.product;

import java.sql.Timestamp;
import com.honda.galc.entity.product.LetPassCriteria;
import com.honda.galc.entity.product.LetPassCriteriaId;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetPassCriteriaDao extends IDaoService<LetPassCriteria, LetPassCriteriaId> {
	public LetPassCriteria getLetPassCriteriaByPgmId(Integer pgmId);
	public int deleteLetPassCriteria(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effTime ) ;
	public int updateEffectiveTime(Timestamp newEffTime,String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp oldEffTime) ;
}