package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.LetCategoryCodeDao;
import com.honda.galc.entity.lcvinbom.LetCategoryCode;
import com.honda.galc.service.Parameters;

public class LetCategoryCodeDaoImpl extends BaseDaoImpl<LetCategoryCode, Long> 
implements LetCategoryCodeDao {

	@Override
	public List<LetCategoryCode> findByCategoryName(String categoryName) {
		Parameters parameters = Parameters.with("name", categoryName);
		return findAll(parameters);
	}

}
