package com.honda.galc.handheld.forms;

import org.apache.struts.action.ActionForm;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.data.HandheldWebPropertyBean;
import com.honda.galc.service.property.PropertyService;

public abstract class ValidatedUserHandheldForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	private String sessionTimedOut = HandheldConstants.SESSION_ACTIVE;
	private HandheldWebPropertyBean handheldPropertyBean;

	public HandheldWebPropertyBean getHandheldPropertyBean() {
		if(handheldPropertyBean == null)
			handheldPropertyBean = PropertyService.getPropertyBean(HandheldWebPropertyBean.class);
		return handheldPropertyBean;
	}

	public String getSessionTimedOut() {
		return sessionTimedOut;
	}

	public void setSessionTimedOut(String sessionTimedOut) {
		this.sessionTimedOut = sessionTimedOut;
	}
}
