package com.honda.galc.dao.conf;

import java.util.List;

import com.honda.galc.entity.conf.MassMessage;
import com.honda.galc.entity.conf.MassMessageId;
import com.honda.galc.service.IDaoService;

public interface MassMessageDao extends
		IDaoService<MassMessage, MassMessageId> {
	
	public List<MassMessage> getLatestMessage(String PlantName, String DepartmentID, String LineId);
	
}
