package com.honda.galc.service.msip.property.inbound;

import java.util.List;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;


@PropertyBean()
public interface RckaePropertyBean extends BaseMsipPropertyBean {
	
	//EIN_TRACK_STATUS_EXCLUSIONS
	@PropertyBeanAttribute(defaultValue = "")
	public List<String> getEinTrackStatusExclusions();
	
	//SHIPPING_RECV_PPID
	@PropertyBeanAttribute(defaultValue = "")
	public String getShippingRecvPpid();
	
	//OPTIONAL_LOGGING_ENABLED
	@PropertyBeanAttribute(defaultValue = "")
	public Boolean getOptionalLoggingEnabled();
	
	@PropertyBeanAttribute(defaultValue = "yyyy-MM-dd HH:mm:ss.SSS z")
	public String getTimestampFormat();
}
