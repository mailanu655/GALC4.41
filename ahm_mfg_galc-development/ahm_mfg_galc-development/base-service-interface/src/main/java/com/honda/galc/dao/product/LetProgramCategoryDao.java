package com.honda.galc.dao.product;

import java.util.List;

import com.honda.galc.entity.product.LetProgramCategory;
import com.honda.galc.entity.product.LetProgramCategoryId;
import com.honda.galc.service.IDaoService;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 26, 2013
 */
public interface LetProgramCategoryDao extends IDaoService<LetProgramCategory, LetProgramCategoryId> {
	
	public List<LetProgramCategory> getColorExplanation(String parameterValues) ;

}
