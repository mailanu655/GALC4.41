package com.honda.galc.property;

public interface LoggerConfigPropertyBean extends IProperty{
	
	@PropertyBeanAttribute(propertyKey="CLIENT_SUFFIX",defaultValue="")
	String getClientPreferredSuffix();
	@PropertyBeanAttribute(propertyKey="USE_PPID",defaultValue="false")
	boolean isUsePPID();
	
}
