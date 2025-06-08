package com.honda.galc.dao.conf;

import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.service.IDaoService;

import java.util.List;

public interface ComponentPropertyDao extends IDaoService<ComponentProperty, ComponentPropertyId> {

    public List<ComponentProperty> findAllByComponentId(String componentId);
    
    public List<String> findAllComponentIds();
    /**
     * find all components' log level setting
     * @return
     */
    public List<ComponentProperty> findAllLogLevels();
    
    public List<ComponentProperty> findAllProductTypes();
    
    public List<ComponentProperty> findAllSubAssyProductTypes();
    
    public void removeAllByComponentId(String componentId);
    
    public String findValueForCompIdAndKey(String componentId, String propertyKey);
    
    public List<ComponentProperty> findAllByPropertyKey(String propertyKey);
    
    public List<String> getAllPlanCodes();
    
    public void updateTimestamp(String componentID, String propertyKey);
    
}
