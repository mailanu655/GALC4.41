package com.honda.galc.dao.jpa.product;

import java.sql.Timestamp;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetPassCriteriaDao;
import com.honda.galc.entity.product.LetPassCriteria;
import com.honda.galc.entity.product.LetPassCriteriaId;
import com.honda.galc.service.Parameters;


/**
 * 
 * @author Gangadhararao Gadde
 * @date nov 26, 2013
 */

public class LetPassCriteriaDaoImpl extends BaseDaoImpl<LetPassCriteria, LetPassCriteriaId> implements LetPassCriteriaDao
{
	public LetPassCriteria getLetPassCriteriaByPgmId(Integer pgmId)
	{
		Parameters param=Parameters.with("id.criteriaPgmId",pgmId);
		return findFirst(param);
	}

	@Transactional
	public int deleteLetPassCriteria(String modelYearCode,String modelCode,String modelTypeCode,String modelOptionCode,Timestamp effTime ) 
	{
		Parameters params = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode).put("id.modelTypeCode", modelTypeCode).put("id.modelOptionCode", modelOptionCode).put("id.effectiveTime", effTime);			
		return	delete(params);
	}
	
	@Transactional
	public int updateEffectiveTime(Timestamp newEffTime, String modelYearCode, String modelCode, String modelTypeCode, String modelOptionCode, Timestamp oldEffTime) {

		Parameters updateParams = Parameters.with("id.effectiveTime", newEffTime);
		Parameters whereParams = Parameters.with("id.modelYearCode", modelYearCode).put("id.modelCode", modelCode)
				.put("id.modelTypeCode", modelTypeCode).put("id.modelOptionCode", modelOptionCode)
				.put("id.effectiveTime", oldEffTime);

		return update(updateParams, whereParams);
	}

}