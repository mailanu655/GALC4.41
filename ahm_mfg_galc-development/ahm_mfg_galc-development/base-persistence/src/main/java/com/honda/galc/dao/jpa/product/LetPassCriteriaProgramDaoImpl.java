package com.honda.galc.dao.jpa.product;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.product.LetPassCriteriaProgramDao;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.service.Parameters;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 25, 2013
 */
public class LetPassCriteriaProgramDaoImpl extends BaseDaoImpl<LetPassCriteriaProgram, Integer> implements LetPassCriteriaProgramDao
{
	private final String UPDATE_LET_PASS_CRITERIA_PGM ="update LetPassCriteriaProgram e set e.criteriaPgmName=:criteriaPgmName, e.inspectionDeviceType=:inspectionDeviceType,e.criteriaPgmAttr=:criteriaPgmAttr where e.criteriaPgmId=:criteriaPgmId";
	
	public List<LetPassCriteriaProgram> getLetPassCriteriaPgmByPgmName(String criteriaPgmName)
	{
		Parameters param=Parameters.with("criteriaPgmName",criteriaPgmName);
		return findAll(param);
	}
	
	public List<LetPassCriteriaProgram> getLetPassCriteriaPgmByPgmId(Integer pgmId)
	{
		Parameters param=Parameters.with("criteriaPgmId",pgmId);
		return findAll(param);
	}
	
	@Transactional
	public int updateLetPassCriteriaPgm(String criteriaPgmName,String inspectionDeviceType,String criteriaPgmAttr,Integer criteriaPgmId) 
	{
		Parameters params = Parameters.with("criteriaPgmName", criteriaPgmName).put("inspectionDeviceType", inspectionDeviceType).put("criteriaPgmAttr", criteriaPgmAttr).put("criteriaPgmId", criteriaPgmId);		
		return executeUpdate(UPDATE_LET_PASS_CRITERIA_PGM, params);
	}
	
	@Transactional
	public int deleteLetPassCriteriaPgmByPgmId(Integer pgmId)
	{
		Parameters param=Parameters.with("criteriaPgmId",pgmId);
		return delete(param);
	}
}