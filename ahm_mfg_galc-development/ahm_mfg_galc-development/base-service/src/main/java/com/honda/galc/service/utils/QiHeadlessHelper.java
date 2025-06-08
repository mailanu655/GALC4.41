/**
 * 
 */
package com.honda.galc.service.utils;

import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.service.property.PropertyService;

/**
 * @author VCC44349
 *
 */
public class QiHeadlessHelper {
	
	protected HeadLessPropertyBean property;
	
	public QiHeadlessHelper(String processPointId) {
		property = PropertyService.getPropertyBean(HeadLessPropertyBean.class, processPointId);		
	}

	public QiHeadlessHelper(HeadLessPropertyBean newProp) {
		property = newProp;
	}

	public boolean isNAQicsSecondary()  {
		final boolean secondaryMode = getProperty().isUseQicsService() && getProperty().isReplicateToNaqics();
		return secondaryMode;
	}
	
	public boolean isSendToNAQics()  {
		boolean isFlag = !getProperty().isUseQicsService() || isNAQicsSecondary();
		return isFlag;
	}

	public HeadLessPropertyBean getProperty() {
		return property;
	}

	public void setProperty(HeadLessPropertyBean property) {
		this.property = property;
	}
	
}
