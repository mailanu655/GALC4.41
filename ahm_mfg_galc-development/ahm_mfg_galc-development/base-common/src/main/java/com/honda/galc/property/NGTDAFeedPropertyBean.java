package com.honda.galc.property;

@PropertyBean(componentId="NGT_DA_FEED")
public interface NGTDAFeedPropertyBean extends IProperty {
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_CLIENT_ID")
	String getNGTDAFeedClientID();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_ENABLED", defaultValue="false")
	boolean isNGTDAFeedEnabled();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_PARAM_SN", defaultValue="SUPPLIER_AUDIO_SERIAL")
	String getAudioSerialParamName();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_PARAM_DWG", defaultValue="AUDIO_DWG")
	String getAudioDwgParamName();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_PARAM_VER", defaultValue="AUDIO_VERSION")
	String getAudioVersionParamName();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_TAG_SN", defaultValue="SUPPLIER_AUDIO_SERIAL")
	String getAudioSerialTagName();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_TAG_DWG", defaultValue="AUDIO_DWG")
	String getAudioDwgTagName();
	
	@PropertyBeanAttribute(propertyKey="NGT_DA_FEED_TAG_VER", defaultValue="AUDIO_VERSION")
	String getAudioVersionTagName();
}
