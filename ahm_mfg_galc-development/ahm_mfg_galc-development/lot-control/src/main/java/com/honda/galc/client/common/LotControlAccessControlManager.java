package com.honda.galc.client.common;

import javax.swing.JOptionPane;

import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMain;
import com.honda.galc.client.datacollection.property.ViewManagerPropertyBean;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LotControlAccessControlManager</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LotControlAccessControlManager description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Jul 5, 2010
 *
 */
public class LotControlAccessControlManager {
	private static LotControlAccessControlManager instance;
	private String authorizedUser;
	private String authorizationGroup;
	
	private LotControlAccessControlManager() {
		super();
		
		init();
	}
	

	private void init() {
		ViewManagerPropertyBean bean = PropertyService.getPropertyBean(ViewManagerPropertyBean.class,
				ApplicationContext.getInstance().getProcessPointId());
		authorizationGroup = bean.getAuthorizationGroup();
	}


	public static LotControlAccessControlManager getInstance(){
		if(instance == null){
			instance = new LotControlAccessControlManager();
		}
		return instance;
	}

	public boolean login(){
		authorizedUser = null;
		if(LoginDialog.login() != LoginStatus.OK) return false;
		
		if(!ClientMain.getInstance().getAccessControlManager().isAuthorized(getAuthorizationGroup() )) {
			JOptionPane.showMessageDialog(null, "Terminating application! \nYou have no access permission of default application of this terminal",
					"Error", JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		authorizedUser = ClientMain.getInstance().getAccessControlManager().getUserName();
		Logger.getLogger().info("User:" + authorizedUser + " logged in.");
		return true;

	}
	
	//Getter & Setters
	public String getAuthorizedUser() {
		return authorizedUser;
	}

	public void setAuthorizedUser(String authorizedUser) {
		this.authorizedUser = authorizedUser;
	}

	public String getAuthorizationGroup() {
		return authorizationGroup;
	}

	public void setAuthorizationGroup(String authorizationGroup) {
		this.authorizationGroup = authorizationGroup;
	}
}
