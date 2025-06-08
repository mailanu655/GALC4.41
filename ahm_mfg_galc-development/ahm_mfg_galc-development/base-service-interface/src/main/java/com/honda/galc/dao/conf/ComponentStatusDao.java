package com.honda.galc.dao.conf;

import java.util.List;
import java.util.Map;

import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.service.IDaoService;

public interface ComponentStatusDao extends IDaoService<ComponentStatus, ComponentStatusId> {
	
	public List<ComponentStatus> findAllByComponentId(String componentId);
	
	public Map<String, ComponentStatus> findAllByComponentIdMap(String componentId);
	
	public ComponentStatus findByKey(String componentId, String statusKey);
	
	public void updateComponentStatusValue(String componentId, String statusKey, String statusValue);
	
	public void updateComponentStatusDescription(String componentId, String statusKey, String description);
	
	public void updateComponentStatus(String componentId, String statusKey, String statusValue, String description);

	public void removeAllByComponentId(String componentId);

}
