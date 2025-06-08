package com.honda.galc.service.msip.property.inbound;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Anusha Gopalan
 * @date May, 2017
 */
@PropertyBean()
public interface EngDataPropertyBean extends BaseMsipPropertyBean {

	@PropertyBeanAttribute(defaultValue = "")
	public String getAepFiredFlag();
	
	@PropertyBeanAttribute(defaultValue = "")
	public short getGalcFiredFlag();
	
	@PropertyBeanAttribute(defaultValue = "")
	public short getGalcNotFiredFlag();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getShippingRecvPpid();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String getPlantCode();
	
	@PropertyBeanAttribute(defaultValue = "")
	public Boolean isAutoAssign();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getAepModels();
	
	@PropertyBeanAttribute(defaultValue = "")
	public int getDaysToCheck();
	
	@PropertyBeanAttribute(defaultValue = "")
	public String[] getNotSellableTrackingStatus();
}
