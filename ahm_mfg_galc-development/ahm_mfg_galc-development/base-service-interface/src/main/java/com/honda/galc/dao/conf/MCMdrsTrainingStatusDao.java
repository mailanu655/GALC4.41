package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MCMdrsTrainingStatus;
import com.honda.galc.entity.conf.MCMdrsTrainingStatusId;
import com.honda.galc.service.IDaoService;

/**
 * @author vfc01778
 * @date December 09, 2014
 */
public interface MCMdrsTrainingStatusDao extends IDaoService<MCMdrsTrainingStatus, MCMdrsTrainingStatusId> {

	public List<Object[]> getUnmappedUsersFromMDRS();

}
