package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.ApplicationCheckerDto;
import com.honda.galc.entity.conf.MCAppChecker;
import com.honda.galc.entity.conf.MCAppCheckerId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Sep 30, 2014
 */
public interface MCAppCheckerDao extends IDaoService<MCAppChecker, MCAppCheckerId> {
	
	public List<MCAppChecker> findAllByApplicationId(String applicationId);
	public List<MCAppChecker> findAllBy(String applicationId, String checkPoint);
	public List<ApplicationCheckerDto> findAllByApplicationAndChecker(String applicationId, String checker, String divisionId);
		
	public int getMaxCheckSeqBy(String applicationId, String checkPoint);
	public void removeAndInsert(MCAppChecker insertAppChecker, MCAppChecker removeAppChecker);
}
