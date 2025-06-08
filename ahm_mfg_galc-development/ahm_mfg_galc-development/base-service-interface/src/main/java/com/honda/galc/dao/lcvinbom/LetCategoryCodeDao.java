package com.honda.galc.dao.lcvinbom;

import java.util.List;

import com.honda.galc.entity.lcvinbom.LetCategoryCode;
import com.honda.galc.service.IDaoService;

public interface LetCategoryCodeDao extends IDaoService<LetCategoryCode, Long>{
	List<LetCategoryCode> findByCategoryName(String categoryName);
}
