package com.honda.galc.property;

import com.honda.galc.common.logging.LogLevel;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public interface JpaServerPropertyBean extends IProperty{
	
	// default threshold value 1000 milliseconds
	@PropertyBeanAttribute(propertyKey="EXECUTION_TIME_WARNING_THRESHOLD",defaultValue="1000")
	int getExecutionTimeWarningThreshold();
	
	@PropertyBeanAttribute(propertyKey= LogLevel.LOG_LEVEL,defaultValue="TRACE")
	String getLogLevel();
	
	@PropertyBeanAttribute(propertyKey="RESULT_LIST_WARNING_THRESHOLD",defaultValue="300")
	int getResultListWarningThreshold();
	
	@PropertyBeanAttribute(defaultValue="true")
	boolean isLogMultipleLine();
}
