package com.honda.galc.property;


/**
 * 
 * @author Gangadhararao Gadde
 * @date Feb 19, 2015
 */

@PropertyBean
public interface GenericTableMaintenancePropertyBean extends IProperty{
	
	/* defines the name of primary entity */	 
	@PropertyBeanAttribute(defaultValue ="")
	public String getEntityName();
	
	/*defines the name of entity for the history table*/ 
	@PropertyBeanAttribute(defaultValue ="")
	public String getHistoryEntityName();
	
	/*defines the name of primary entity DAO class */ 
	@PropertyBeanAttribute(defaultValue ="")
	public String getDaoName();
	
	/*defines the name of history entity DAO class */  
	@PropertyBeanAttribute(defaultValue ="")
	public String getHistoryDaoName();
		
	/*defines if a history entity needs to be created whenever the primary entity is updated */ 
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isHistoryEntitySaveEnabled();
	
	
	/*defines if mass update functionality is enabled */ 
	@PropertyBeanAttribute(defaultValue = "false")
	public boolean isMassUpdateEnabled();
	
	/*defines the maximum number of rows that can be displayed on screen */ 
	@PropertyBeanAttribute(defaultValue = "1000")
	public int getMaxRowCount();
	
}
