package com.honda.galc.service.msip.property.inbound;

import com.honda.galc.property.PropertyBean;
import com.honda.galc.property.PropertyBeanAttribute;
import com.honda.galc.service.msip.property.BaseMsipPropertyBean;

/**
 * @author Subu Kathiresan
 * @date Mar 27, 2017
 */
@PropertyBean()
public interface Ahm010PropertyBean extends BaseMsipPropertyBean {
	
	//CUTOFF_TIME
	@PropertyBeanAttribute(defaultValue = "")
	public String getCutoffTime();
	
	//DISTRIBUTION_LIST
	@PropertyBeanAttribute(defaultValue = "")
	public String getDistributionList();
	
	//EMAIL_SUBJECT
	@PropertyBeanAttribute(defaultValue = "")
	public String getEmailSubject();
	
	//OPEN_STATUS
	@PropertyBeanAttribute(defaultValue = "")
	public String getOpenStatus();
	
	//OTHER_STATUS
	@PropertyBeanAttribute(defaultValue = "")
	public String getOtherStatus();
	
	//ALERT_MAIL_RECIPIENTS
	@PropertyBeanAttribute(defaultValue = "")
	public String getAlertMailRecipients();
	
}
