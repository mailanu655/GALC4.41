package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCMdrsTraining;
import com.honda.galc.entity.conf.MCMdrsTrainingId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date May 30, 2014
 */
public interface MCMdrsTrainingDao extends IDaoService<MCMdrsTraining, MCMdrsTrainingId> {
	
	
	public List<Object[]> getUnmappedUsersFromMDRS();
	
	public List<Object[]> modelsNotMappedToProcessPoint();
 
}

