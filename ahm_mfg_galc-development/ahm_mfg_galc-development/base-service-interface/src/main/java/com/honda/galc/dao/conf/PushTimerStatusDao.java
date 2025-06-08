package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.dto.PushTimerStatusDto;
import com.honda.galc.entity.conf.PushTimerStatus;
import com.honda.galc.entity.conf.PushTimerStatusId;
import com.honda.galc.service.IDaoService;

/**
 * @author Subu Kathiresan
 * @date Aug 3, 2015
 */
public interface PushTimerStatusDao extends IDaoService<PushTimerStatus, PushTimerStatusId> {
	
	public List<PushTimerStatusDto> getPlantProgress(String plantName);
	
	public List<PushTimerStatusDto> getTopLaggingProcessPoints(String plantName, String divisionId, int howMany);

}
