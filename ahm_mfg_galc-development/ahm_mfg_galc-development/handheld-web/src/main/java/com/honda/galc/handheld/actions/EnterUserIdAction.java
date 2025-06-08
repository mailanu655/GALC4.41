package com.honda.galc.handheld.actions;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.*;
import com.honda.galc.common.exception.LoginException;
import com.honda.galc.dao.conf.AccessControlEntryDao;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.entity.conf.Application;
import com.honda.galc.entity.conf.UserSecurityGroupId;
import com.honda.galc.handheld.data.HandheldConstants;
import com.honda.galc.handheld.forms.EnterUserIdForm;
import com.honda.galc.handheld.plugin.InitializationPlugIn;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.LDAPService;
import com.honda.galc.util.StringUtil;

public class EnterUserIdAction extends HandheldAction{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
		String userInput = ((EnterUserIdForm)form).getUserInput();		
		if(StringUtil.isNullOrEmpty(userInput))
			return mapping.findForward("failure");
		
		HttpSession currentSession = request.getSession();

		currentSession.setAttribute(HandheldConstants.TIMEOUT_INTERVAL, getHandheldPropertyBean().getSessionTimeoutDuration());
		
		String validUserId = getHandheldPropertyBean().isProxCardValidationRequired()
			? getValidUserIdFromProxCard(userInput)
			: userInput;

		if (validUserId == null)
			return mapping.findForward("failure");
		
		currentSession.setAttribute(HandheldConstants.USER_ID, validUserId);
		currentSession.setAttribute(HandheldConstants.DIVISIONS, getInstalledPartUpdateDivisions());
		InitializationPlugIn.info(validUserId, "Login");
		return mapping.findForward("success");
    }

	private String getUserIdPortionOfProxCard(String proxCardBarCode) {
		String userIdDigits = proxCardBarCode;
		int prefixLength = -1;
		int suffixLength = -1;
		try {
			 prefixLength = getHandheldPropertyBean().getProxCardBarCodePrefixLength();
			 suffixLength = getHandheldPropertyBean().getProxCardBarCodeSuffixLength();
		} catch (NumberFormatException e) {
			InitializationPlugIn.error(e.getMessage());
			return "";
		}
		
		if (userIdDigits.length() > prefixLength)
			//remove leading characters
			userIdDigits = userIdDigits.substring(prefixLength, userIdDigits.length()); 
		
		//remove leading zeros
		userIdDigits = userIdDigits.replaceAll("^0+", ""); //remove leading zeros
		
		return userIdDigits.length() <= suffixLength
			? "" //not long enough so remove everything
			: userIdDigits.substring(0, userIdDigits.length() - suffixLength);//remove trailing characters
	}
	
	private List<String> getRepairScreenIds() {
		ArrayList<String> result = new ArrayList<>();
		for (String screenId : getHandheldPropertyBean().getRepairScreenIds()) {
			Application manualLotControlRepairApp = ServiceFactory.getDao(ApplicationDao.class).findByKey(screenId);
			if (manualLotControlRepairApp != null)
				result.add(manualLotControlRepairApp.getScreenId());
		}
		return result;
	}
	
	private String getValidUserIdFromProxCard(String proxCardBarCode) {
		String proxCardId = getUserIdPortionOfProxCard(proxCardBarCode);

		for (String eachScreenId : getRepairScreenIds()) {
			for (String eachPrefix : getHandheldPropertyBean().getAssociatePrefixes()) {
				String prefixedProxCardId = eachPrefix + proxCardId;
				if (isAccessPermitted(eachScreenId, prefixedProxCardId)) 
					return prefixedProxCardId;
			}
		}
		return null;
	}

	private boolean isAccessPermitted(String screenId, String prefixedProxCardId) {
		return ServiceFactory.getDao(AccessControlEntryDao.class).isAccessPermitted(getUserSecurityGroupIds(prefixedProxCardId),screenId);
	}

	private List<UserSecurityGroupId> getUserSecurityGroupIds(String userId) {
		try {
			return LDAPService.getInstance().authenticate_without_pasword(userId);
		} catch (LoginException e) {
			return new ArrayList<UserSecurityGroupId>();
		}
	}

	@Override
	protected String formName() {
		return HandheldConstants.ENTER_USER_ID_FORM;
	}
}
