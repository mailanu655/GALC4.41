package com.honda.galc.dao.jpa.product;



import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;

import com.honda.galc.dao.product.LetProgramCategoryDao;
import com.honda.galc.entity.product.LetProgramCategory;
import com.honda.galc.entity.product.LetProgramCategoryId;
import com.honda.galc.service.Parameters;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public class LetProgramCategoryDaoImpl extends BaseDaoImpl<LetProgramCategory, LetProgramCategoryId> implements LetProgramCategoryDao {
	
	private static String GET_ALL_COLOR_EXPLANATIONS ="select e from LetProgramCategory e order by e.id.criteriaPgmAttr,e.id.inspectionDeviceType";
	private static String GET_SPECIFIC_COLOR_EXPLANATIONS ="SELECT * FROM GALADM.GAL730TBX WHERE INSPECTION_DEVICE_TYPE || CRITERIA_PGM_ATTRIBUTE IN (?1) ORDER BY CRITERIA_PGM_ATTRIBUTE, INSPECTION_DEVICE_TYPE";
		
	
	public List<LetProgramCategory> getColorExplanation(String parameterValues) 
	{
        if(parameterValues.trim().equals(""))
        {
        	return findAllByQuery(GET_ALL_COLOR_EXPLANATIONS);
        }else
        {
			Parameters params = Parameters.with("1", parameterValues);
			return findAllByNativeQuery(GET_SPECIFIC_COLOR_EXPLANATIONS, params,LetProgramCategory.class);
        }		
	}

	
}
