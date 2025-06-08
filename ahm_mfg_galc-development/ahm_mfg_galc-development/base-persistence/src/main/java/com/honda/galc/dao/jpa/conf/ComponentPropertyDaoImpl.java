package com.honda.galc.dao.jpa.conf;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.honda.galc.common.logging.LogLevel;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.jpa.BaseDaoImpl;
import com.honda.galc.entity.conf.ComponentProperty;
import com.honda.galc.entity.conf.ComponentPropertyId;
import com.honda.galc.notification.service.ILogLevelNotification;
import com.honda.galc.service.Parameters;
import static com.honda.galc.service.ServiceFactory.getNotificationService;

public class ComponentPropertyDaoImpl extends BaseDaoImpl<ComponentProperty, ComponentPropertyId> implements ComponentPropertyDao{

	private static final String FIND_ALL_PLAN_CODE = "select distinct property_value from galadm.gal489tbx where property_key='PLAN_CODE'";

	private static final String UPDATE_COMPONENT_PROPERTY=
			"UPDATE galadm.gal489tbx " +
			"SET property_value=CURRENT_TIMESTAMP " +
			"WHERE component_id = ?1 AND property_key= ?2";
	
	@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
    public List<ComponentProperty> findAllByComponentId(String componentId) {
		
		return this.findAll(Parameters.with("id.componentId",componentId), new String[]{"id.propertyKey"});
		
	}

    @SuppressWarnings("unchecked")
	public List<String> findAllComponentIds() {
        return entityManager.createQuery(
                 "select distinct e.id.componentId from " + entityClass.getName() + " as e")
                 .getResultList();
    }
    
    @Transactional
    public ComponentProperty save(ComponentProperty entity) { 
    	if(entity.getCreateTimestamp() == null) {
    		entity.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
    	}
        ComponentProperty updated= super.save(entity);
        if(entity.getId().getPropertyKey().trim().equals(LogLevel.LOG_LEVEL)){
        	getNotificationService(ILogLevelNotification.class, "").execute(entity.getId().getComponentId(), entity.getPropertyValue());
        	
        }
        return updated;
    }

	public List<ComponentProperty> findAllLogLevels() {
		
		return this.findAll(Parameters.with("id.propertyKey", LogLevel.LOG_LEVEL),new String[] {"id.componentId"},true);
		
	}
	
    public List<ComponentProperty> findAllByPropertyKey(String propertyKey) {
    	Parameters params = Parameters.with("id.propertyKey", propertyKey);
    	return findAll(params);
    }

	public List<ComponentProperty> findAllProductTypes() {
		return this.findAll(Parameters.with("id.propertyKey", "PRODUCT_TYPE"),new String[] {"id.componentId"},true);
	}

	public List<ComponentProperty> findAllSubAssyProductTypes() {
		return this.findAll(Parameters.with("id.propertyKey", "SUB_ASSY_PRODUCT_TYPE"),new String[] {"id.componentId"},true);
	}

	@Transactional
	public void removeAllByComponentId(String componentId) {
		delete(Parameters.with("id.componentId",componentId));
	}

	public String findValueForCompIdAndKey(String componentId,
			String propertyKey) {
		
		ComponentProperty property = null;
		ComponentPropertyId compPropKey = new ComponentPropertyId(componentId, propertyKey);
		property = findByKey(compPropKey);
		
		if(property != null)
			return property.getPropertyValue();
		else
			return null;
	}

	public List<String> getAllPlanCodes() {
		
		return findAllByNativeQuery(FIND_ALL_PLAN_CODE, null, String.class);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateTimestamp(String componentID, String propertyKey){
		Parameters params = Parameters.with("1", componentID);
		params.put("2", propertyKey);
		executeNativeUpdate(UPDATE_COMPONENT_PROPERTY, params);
	}

}
