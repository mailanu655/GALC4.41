package com.honda.galc.dao.product;

import java.util.List;
import com.honda.galc.entity.product.LetPassCriteriaProgram;
import com.honda.galc.service.IDaoService;

/**
 * 
 * @author Gangadhararao Gadde
 * @date Nov 25, 2013
 */
public interface LetPassCriteriaProgramDao extends IDaoService<LetPassCriteriaProgram, Integer>
{
	public List<LetPassCriteriaProgram> getLetPassCriteriaPgmByPgmName(String criteriaPgmName);
	public List<LetPassCriteriaProgram> getLetPassCriteriaPgmByPgmId(Integer pgmId);
	public int updateLetPassCriteriaPgm(String criteriaPgmName,String inspectionDeviceType,String criteriaPgmAttr,Integer criteriaPgmId);
	public int deleteLetPassCriteriaPgmByPgmId(Integer pgmId);
}