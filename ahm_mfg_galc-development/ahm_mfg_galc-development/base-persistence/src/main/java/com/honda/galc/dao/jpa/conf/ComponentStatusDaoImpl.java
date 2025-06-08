
package com.honda.galc.dao.jpa.conf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.dao.conf.ComponentStatusDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ComponentStatus;
import com.honda.galc.entity.conf.ComponentStatusId;
import com.honda.galc.service.Parameters;

public class ComponentStatusDaoImpl extends BaseDaoImpl<ComponentStatus, ComponentStatusId> implements ComponentStatusDao {
	
	public List<ComponentStatus> findAllByComponentId(String componentId){
		Parameters params = Parameters.with("id.componentId", componentId);
		return findAll(params);
	}
	
	public Map<String, ComponentStatus> findAllByComponentIdMap(String componentId){
		Map<String, ComponentStatus> componentStatusMap = null;
		List<ComponentStatus> componentLst = findAllByComponentId(componentId);
		if(componentLst != null && componentLst.size()>0){
			componentStatusMap = new HashMap<String, ComponentStatus>();
			for(ComponentStatus componentStatus : componentLst){
				componentStatusMap.put(componentStatus.getId().getStatusKey(), componentStatus);
			}
			return componentStatusMap;
		}else
			return componentStatusMap;
	}

	public ComponentStatus findByKey(String componentId, String statusKey) {
		ComponentStatusId key = new ComponentStatusId(componentId, statusKey);
		return findByKey(key);
	}
	
	@Transactional
	public void updateComponentStatusValue(String componentId, String statusKey, String statusValue) {
		this.updateComponentStatus(componentId, statusKey, statusValue, null);
	}
	
	@Transactional
	public void updateComponentStatusDescription(String componentId, String statusKey, String description) {
		this.updateComponentStatus(componentId, statusKey, null, description);
	}
	
	@Transactional
	public void updateComponentStatus(String componentId, String statusKey, String statusValue, String description) {
		ComponentStatus entity = this.findByKey(componentId,statusKey); 
		if (entity == null)
			entity = new ComponentStatus(componentId, statusKey, "");
		if (StringUtils.isNotEmpty(statusValue)) 
			entity.setStatusValue(statusValue);
		if (StringUtils.isNotEmpty(description)) 
			entity.setDescription(description);
		
		this.update(entity);
	}
	
	@Transactional
	public void removeAllByComponentId(String componentId) {
		delete(Parameters.with("id.componentId",componentId));
	}
}
