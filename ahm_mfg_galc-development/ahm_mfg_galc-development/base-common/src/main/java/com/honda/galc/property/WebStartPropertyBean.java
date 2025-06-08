package com.honda.galc.property;

public interface WebStartPropertyBean extends IProperty{

	@PropertyBeanAttribute(propertyKey="ALC_DIR_LINUX")
	String getAlcDirLinux();
	
	@PropertyBeanAttribute(propertyKey="ALC_DIR_WINDOWS")
	String getAlcDirWindows();
	
	@PropertyBeanAttribute(propertyKey="CACHE_TIME_TO_LIVE",defaultValue = "10000")
	int getCacheTimeToLive();

	/**
	 * Specifies the swing look and feel for Legacy and GALCv1 clients.  
	 */
	@PropertyBeanAttribute(propertyKey="LOOK_AND_FEEL",defaultValue = "metal")
	String getLookAndFeel();

	/**
	 * When this flag is set to true the GALC application will automatically register the build with webstart and set the build as default. 
	 */
	@PropertyBeanAttribute(defaultValue="true")
	boolean isSetupDefaultBuild();
	
	@PropertyBeanAttribute(propertyKey="DEFAULT_INITIAL_HEAP_SIZE",defaultValue="256")
	String getDefaultInitialHeapSize();
	
	@PropertyBeanAttribute(propertyKey="DEFAULT_MAX_HEAP_SIZE",defaultValue="512")
	String getDefaultMaxHeapSize();
	
}
