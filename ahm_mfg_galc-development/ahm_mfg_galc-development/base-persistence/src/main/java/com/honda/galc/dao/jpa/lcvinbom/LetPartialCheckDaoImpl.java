package com.honda.galc.dao.jpa.lcvinbom;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.dao.lcvinbom.LetPartialCheckDao;
import com.honda.galc.entity.lcvinbom.LetPartialCheck;
import com.honda.galc.entity.lcvinbom.LetPartialCheckId;
import com.honda.galc.service.Parameters;

public class LetPartialCheckDaoImpl extends BaseDaoImpl<LetPartialCheck, LetPartialCheckId> 
	implements LetPartialCheckDao {

	@Override
	public List<LetPartialCheck> findAssignedInspectionsByCategoryCode(long categoryCodeId) {
		Parameters parameters = Parameters.with("id.categoryCodeId", categoryCodeId);
		return findAll(parameters);
	}

	@Override
	@Transactional
	public int removeByCategoryCode(long categoryCodeId) {
		Parameters parameters = Parameters.with("id.categoryCodeId", categoryCodeId);
		return delete(parameters);
	}

}
