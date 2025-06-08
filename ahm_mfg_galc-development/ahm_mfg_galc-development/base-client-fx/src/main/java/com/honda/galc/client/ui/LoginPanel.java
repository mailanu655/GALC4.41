package com.honda.galc.client.ui;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.ProximityCardReader;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.product.StationUserDao;
import com.honda.galc.device.SmartCardReaderUtil;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.product.StationUser;
import com.honda.galc.entity.product.StationUserId;
import com.honda.galc.entity.qi.QiStationConfiguration;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.DailyDepartmentScheduleUtil;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * 
 * <h3>LoginPanel Class description</h3>
 * <p>
 * LoginPanel description
 * </p>
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
 * @author Jeffray Huang<br>
 *         Feb 29, 2010
 * 
 * @author Suriya Sena JavaFx Migration 
 *         Feb 19, 2014
 * 
 */
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */

public class LoginPanel extends BorderPane {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	private GridPane mainPanel;
	
	private ApplicationPropertyBean appBean;
	
	
	private Label assocaiteIdLabel = UiFactory.createLabel("assocaiteIdLabel", "Associate");
	private PasswordField confirmPasswordField = UiFactory.createPasswordField("ConfirmPassword");

	private Label passwordLabel = UiFactory.createLabel("passwordLabel", "Password");
	private PasswordField passwordField = UiFactory.createPasswordField("Password");

	private Label newPasswordLabel = UiFactory.createLabel("newPasswordLabel", "New password");
	private PasswordField newPasswordField = UiFactory.createPasswordField("NewPassword");

	private Label confirmPasswordLabel = UiFactory.createLabel("confirmPasswordLabel", "Confirm password");
	private ComboBox<String> associateComboBox  = new ComboBox<String>();
	


	private Label messageField = UiFactory.createLabel("messageField", "");
	private Button okButton = UiFactory.createButton("Okay",  UiFactory.getIdle().getButtonFont(), true);
	private Button cancelButton = UiFactory.createButton("Cancel",  UiFactory.getIdle().getButtonFont(), true);
	private Button changePasswordButton = UiFactory.createButton("Change Password", UiFactory.getIdle().getButtonFont(), true);
	private boolean changePasswordClicked = false;
	private boolean resetPasswordButtonEnable = false;
	private boolean allowCancel = true;
	
	private String newAssociateId = null;
	private QiStationConfiguration qiEntryStationConfigManagement;
	

	public LoginPanel() {
		setPasswdResetEnableFlag();
		mainPanel = initMainPanel();
		this.setCenter(mainPanel);
	}
	
	 public LoginPanel(boolean allowCancel) {
	 	   setPasswdResetEnableFlag();
	 	   this.allowCancel = allowCancel;
	 	   mainPanel = initMainPanel();
	 	   this.setCenter(mainPanel);
	    }

	private GridPane initMainPanel() {
		GridPane panel = new GridPane();
		appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, ClientMainFx.getInstance().getApplicationContext().getApplicationId());
		

		panel.setGridLinesVisible(false);   // set true for debug
		panel.setVgap(5);
		panel.setHgap(5);
		panel.setPadding(new Insets(20,30,10,30));
		
		associateComboBox.setPrefHeight(30);
		associateComboBox.setMinHeight(30);
		associateComboBox.setMaxHeight(30);
		associateComboBox.setEditable(true);
		associateComboBox.setId("associateIdCmbBx");
		
		List<String> stationUsers = findRecentUserList();
		if(null!=stationUsers){
			associateComboBox.setItems(FXCollections.observableArrayList(stationUsers));
		}
		associateComboBox.setStyle("-fx-font-size: 11px;-fx-pref-width: 250;-fx-pref-height: 10;-fx-font-weight: bold;");
		
		
		int row = 0;

		panel.addRow(row++,assocaiteIdLabel,associateComboBox);
		if(appBean.isManualLogin()){
			panel.addRow(row++,passwordLabel,passwordField);
		}
		
	

		if (changePasswordClicked) {
			panel.addRow(row++,newPasswordLabel,newPasswordField);
			panel.addRow(row++,confirmPasswordLabel,confirmPasswordField);
		}

		Node buttonPane = createButtonPanel();

		panel.add(buttonPane,0,row++,2,1);
		panel.add(messageField,0,row++,2,1);

		return panel;
	}

	private HBox createButtonPanel() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_RIGHT);
		hbox.setPadding(new Insets(5));
		hbox.setSpacing(10);
		hbox.getChildren().addAll(okButton);
		if(allowCancel){
			hbox.getChildren().addAll(cancelButton);
		}
		
		if (resetPasswordButtonEnable && !changePasswordClicked) {
			hbox.getChildren().addAll(changePasswordButton);
		}
		return hbox;
	}

	private void setPasswdResetEnableFlag() {
		String passwdResetButtonEnable = "";
		try {
			passwdResetButtonEnable = PropertyService.getProperty("ACL", "PASSWORD_RESET_ENABLE");
			if (passwdResetButtonEnable != null	&& passwdResetButtonEnable.equals("TRUE")) {
				this.resetPasswordButtonEnable = true;
			}
		} catch (Exception e1) {
			System.out.println(" PASSWORD_RESET_ENABLE property is not found");
			e1.printStackTrace();
		}
	}


	public PasswordField getPasswordField() {
		return passwordField;
	}

	public Button getOkButton() {
		return okButton;
	}

	public Button getCancelButton() {
		return cancelButton;
	}

	public Button getChangePasswordButton() {
		return changePasswordButton;
	}

	public LoginStatus login() {
		String password;
		LoginStatus loginStatus;
		if(appBean.isManualLogin()){
			password = passwordField.getText().trim();
			loginStatus = ClientMainFx.getInstance().getAccessControlManager().login(getUserId(), password);
		}else{
			loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(getUserId());
		}

		
		if (loginStatus != LoginStatus.OK){
			this.messageField.setText(loginStatus.getMessage());
			this.messageField.setTextFill(Color.RED);
		}else{
			StationUser qiStationUser = new StationUser();
			StationUserId id = new StationUserId(ClientMainFx.getInstance().currentApplicationId,getUserId());
			Timestamp dbTimestamp = getDao(ProcessPointDao.class).getDatabaseTimeStamp();
			qiStationUser.setLoginTimestamp(dbTimestamp);
			qiStationUser.setId(id);
			
			
			if(null!=getDao(StationUserDao.class).findByKey(id)){
				getDao(StationUserDao.class).updateTimeStampByStationUser(qiStationUser);
			}else{
				getDao(StationUserDao.class).save(qiStationUser);
			}
			
			List<StationUser> qiStationUsers  = getDao(StationUserDao.class).findAllStationUser(ClientMainFx.getInstance().getApplicationContext().getApplicationId());
			if(null!=qiStationUsers && !qiStationUsers.isEmpty() && qiStationUsers.size() > appBean.getMaxRecentUsers()){
				getDao(StationUserDao.class).removeUser(qiStationUsers.get(0).getId());
			}
			
			
			
		}
		

		return loginStatus;

	}

	public LoginStatus changePassword() {

		LoginStatus loginStatus = login();

		if (loginStatus != LoginStatus.OK) {
			return loginStatus;
		}
		
		String userId = getUserId();
		String newPassword = newPasswordField.getText().trim();
		String confirmPassword = confirmPasswordField.getText().trim();

		if (StringUtils.isEmpty(newPassword)) {
			this.messageField.setText("new password cannot be empty");
			return LoginStatus.PASSWORD_INCORRECT;
		}

		if (!newPassword.equals(confirmPassword)) {
			messageField
					.setText("confirm password is different from new password");
			return LoginStatus.PASSWORD_INCORRECT;
		}

		if (!ServiceFactory.isServerAvailable()) {
			messageField
					.setText("Server is disconnected.Could not change the password");
			return LoginStatus.CANCEL;
		}

		try {
			User user = ServiceFactory.getDao(UserDao.class).findByKey(userId);
			user.setPasswd(newPassword);
			user.setPasswordUpdateDate(DateFormatUtils.format(new Date(),"yyyyMMdd"));
			ServiceFactory.getDao(UserDao.class).update(user);
		} catch (Exception e) {
			messageField.setText("Could not change the password due to server error");
			return LoginStatus.CANCEL;
		}

		return LoginStatus.OK;
	}

	public String getUserId() {
		 newAssociateId = associateComboBox.getEditor().getText().trim();
        if (appBean.isLoginWithoutPassword() && appBean.isScannerActive() && appBean.isSubstringRequiredForAssociateId()) {
				newAssociateId = getAssociateIdNumber(newAssociateId);
        }
        if(appBean.isMapCardNumber()) {
        	Integer cardIdMaxLength = appBean.getCardIdMaxLength();
        	 String userId = SmartCardReaderUtil.getUserId(newAssociateId, appBean.getProximityCardReaderName(), cardIdMaxLength);
        	 if(userId != null) {
        		 userId = getAssociateIdWithPrefix(userId);
  		   	 }
        	 return (userId != null) ? userId : StringUtils.EMPTY; 
        	
		}
		return newAssociateId;
	}
	
	private String getAssociateIdNumber(String associateId) {
		int startIndex = appBean.getStartIndex();
		int length = appBean.getLength();
		if(startIndex > -1 && length > -1 && associateId.length() >= 23){
			associateId = associateId.substring(startIndex,length);
				String[] prefixes = appBean.getAssociatePrefixes();
				if(prefixes!=null && prefixes.length > 0) {
					LoginStatus loginStatus = null;
					for(String prefix: prefixes) {
						associateId = prefix.toLowerCase().trim() + associateId.trim();
						loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(associateId);
						if (loginStatus == null) {
							ClientMainFx.getInstance().exitApplication(1);
						}
						else if(loginStatus == LoginStatus.USER_NOT_EXIST) {
							continue;
						}
						else {
							return associateId;
						}
					}
				}
		}
		return associateId;
		
		
	}

	

	public void reinitComponents(boolean changePasswordClicked) {
		this.getChildren().remove(mainPanel);
		this.changePasswordClicked = changePasswordClicked;
		mainPanel = initMainPanel();
		this.setCenter(mainPanel);
	}

	public boolean isResetPasswordButtonEnable() {
		return resetPasswordButtonEnable;
	}

	public void setResetPasswordButtonEnable(boolean resetPasswordButtonEnable) {
		this.resetPasswordButtonEnable = resetPasswordButtonEnable;
	}
	

	
	
	/**
	 * Finds the date range for which associate user id will shown.
	 * @param no. of days for which logged in user will be displayed
	 * @return Timestamp
	 */
	private List<String> findRecentUserList(){
		List<String> usersList = new ArrayList<String>();
		String processPointId = ClientMainFx.getInstance().getApplicationContext().getProcessPointId();
		this.appBean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, ClientMainFx.getInstance().getApplicationContext().getApplicationId());
		Timestamp oldTimestamp = CommonUtil.findDateRange(appBean.getDateRangeForRecentUsers());
		if (appBean.isClearRecentUser()) {
			ProcessPoint processPoint = ClientMainFx.getInstance().getApplicationContext().getProcessPoint();
			if (processPoint == null) return null;
			DailyDepartmentScheduleUtil sche = ApplicationContext.getInstance().getDailyDepartmentScheduleUtil();
			if (sche != null&&!StringUtils.isEmpty(sche.getCurrentShift())) oldTimestamp = sche.getShiftStartTimestamp(sche.getCurrentShift());
		}
		List<StationUser> stationUsers = getDao(StationUserDao.class).findAllRecentUsersByHostName(processPointId, oldTimestamp, appBean.getMaxRecentUsers());
		for(StationUser stationUser:stationUsers){
			usersList.add(stationUser.getId().getUser());
		}
		return usersList;
	}

	private String getAssociateIdWithPrefix(String associateId) {
		String[] prefixes = appBean.getAssociatePrefixes();
		if(prefixes!=null && prefixes.length > 0) {
			LoginStatus loginStatus = null;
			String associateIdWithPrefix = "";
			for(String prefix: prefixes) {
				associateIdWithPrefix = prefix.trim() + associateId.trim();
				loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(associateIdWithPrefix);
				if (loginStatus == null) {
					ClientMainFx.getInstance().exitApplication(1);
				} else if(loginStatus == LoginStatus.USER_NOT_EXIST) {
					continue;
				} else if(loginStatus == LoginStatus.OK) {
					return associateIdWithPrefix;
				}
			}
		}
		return associateId;
	}

}
