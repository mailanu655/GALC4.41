package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.MCTraining;
import com.honda.galc.entity.conf.MCTrainingId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date May 30, 2014
 */
public interface MCTrainingDao extends IDaoService<MCTraining, MCTrainingId> {

	public MCTraining findMostRecentEntry(String processPt, String associateNo, Integer pddaPlatformId, String specCodeTyp, String specCodeMask);
	
	public boolean validateUserTraining(String associateNo, String processPt, String prodSpecCode);
	
}
