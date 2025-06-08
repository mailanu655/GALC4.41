package com.honda.galc.client.ui;

import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.ProximityCardReader;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.device.SmartCardReaderUtil;
import com.honda.galc.entity.conf.User;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
 * @author Alok Ghode<br>
 *         Apr 24, 2014
 * 
 */

public class LoginPanelWithKeyPad extends BorderPane {

	private static final long serialVersionUID = 1L;

	private GridPane mainPanel;
	private Label userIdLabel = UiFactory.createLabel("userIdLabel", "Username");
	private TextField userIdField = UiFactory.createTextField("userNameTxtFld", UiFactory.getIdle().getButtonFont(), TextFieldState.EDIT);
	
	private Label passwordLabel = UiFactory.createLabel("passwordLabel", "Password");
	private PasswordField passwordField = UiFactory.createPasswordField("Password");

	private Label newPasswordLabel = UiFactory.createLabel("newPasswordLabel", "New password");
	private PasswordField newPasswordField = UiFactory.createPasswordField("NewPassword");

	private Label confirmPasswordLabel = UiFactory.createLabel("confirmPasswordLabel", "Confirm password");
	private PasswordField confirmPasswordField = UiFactory.createPasswordField("ConfirmPassword");

	private Label messageField = UiFactory.createLabel("messageField");
	private Button okButton =  UiFactory.createButton("Ok",  UiFactory.getIdle().getButtonFont(), true);
	private Button cancelButton = UiFactory.createButton("Cancel",  UiFactory.getIdle().getButtonFont(), true);
	
	private Button changePasswordButton = UiFactory.createButton("Change Password");
	private boolean changePasswordClicked = false;
	private boolean resetPasswordButtonEnable = false;
	private boolean scannerFailed = false;
	private boolean showCancelButton = false;
	private String associateId;
	
	private ApplicationPropertyBean appBean;
	private Text headingText = UiFactory.createText(ClientConstants.DEFAULT_LOGIN_CAPTION);

	public LoginPanelWithKeyPad() {
		createLoginPanelWithKeyPad(ClientConstants.DEFAULT_LOGIN_CAPTION);
	}
	
	public LoginPanelWithKeyPad(String header) {
		createLoginPanelWithKeyPad(header);
	}
	
	public LoginPanelWithKeyPad(String header, boolean showCancelButton) {
		setShowCancelButton(showCancelButton);
		createLoginPanelWithKeyPad(header);
	}
	
	private void createLoginPanelWithKeyPad(String header) {
		appBean = PropertyService.getPropertyBean(
				ApplicationPropertyBean.class, ClientMainFx.getInstance().getApplicationContext()
						.getApplicationId());
		if(appBean.isLoginWithoutPassword()) {
			setPasswdResetEnableFlag();
		}
		mainPanel = initMainPanel();
		LoginPanelWithKeyPad.setMargin(mainPanel, new Insets(10,10,100,10));
		this.setCenter(mainPanel);
		StackPane heading = new StackPane();
		header = (header!=null)? header : ClientConstants.DEFAULT_LOGIN_CAPTION;
		headingText.setText(header);
        headingText.setStyle(" -fx-font-size: 2em; -fx-font-weight: bold; ");
        heading.setStyle("-fx-padding: 4em 0px 20px 0px;");
        heading.getChildren().add(headingText);
        this.setTop(heading);
        messageField.setStyle("-fx-text-fill: red");
	}

	private GridPane initMainPanel() {
		GridPane panel = new GridPane();
		
		panel.setGridLinesVisible(false);   // set true for debug
		panel.setVgap(5);
		panel.setHgap(5);
		panel.setAlignment(Pos.CENTER);
		panel.setStyle("-fx-font-size: 1.25em");
		int row = 0;
		//userIdField.setMaxWidth(100);
		panel.addRow(row++,userIdLabel,userIdField);
		if(!appBean.isLoginWithoutPassword()) {
			panel.addRow(row++,passwordLabel,passwordField);

			if (changePasswordClicked) {
				panel.addRow(row++,newPasswordLabel,newPasswordField);
				panel.addRow(row++,confirmPasswordLabel,confirmPasswordField);
			}
		}
		
		Node buttonPane = createButtonPanel();
		panel.add(buttonPane,2,row-1,2,1);

		GridPane mainPanel = new GridPane();
		mainPanel.setGridLinesVisible(false);   // set true for debug
		mainPanel.setVgap(2);
		mainPanel.setHgap(5);
		mainPanel.setAlignment(Pos.CENTER);
		mainPanel.setStyle("-fx-font-size: 1.25em");
		mainPanel.add(panel, 1, 1);
		mainPanel.add(messageField,1,2);
		return mainPanel;
	}

	private HBox createButtonPanel() {
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER_RIGHT);
		hbox.setPadding(new Insets(5));
		hbox.setSpacing(10);
		hbox.getChildren().addAll(okButton);
		if(isShowCancelButton())
			hbox.getChildren().addAll(cancelButton);
		
		if(!appBean.isLoginWithoutPassword()) {
			if (resetPasswordButtonEnable && !changePasswordClicked) {
				hbox.getChildren().addAll(changePasswordButton);
			}
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

	public TextField getUserIdField() {
		return userIdField;
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
		String userId = getUserId();
		LoginStatus loginStatus = null;
		if(appBean.isLoginWithoutPassword()) {
			loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(userId);
		}
		else {
			 if(StringUtils.isBlank(userId)) loginStatus = LoginStatus.USER_NOT_EXIST;
			 else if(passwordField.getText()!=null && !StringUtils.isBlank(passwordField.getText().trim())) {
				String password = passwordField.getText().trim();
				loginStatus = ClientMainFx.getInstance().getAccessControlManager().login(userId, password);
			 }else loginStatus = LoginStatus.PASSWORD_INCORRECT;	
		}
		if (loginStatus != LoginStatus.OK) {
			String errorMsg = loginStatus.getMessage();
			if(appBean.isScannerActive()) {
				errorMsg = errorMsg+". "+(this.isScannerFailed()?ClientConstants.LOGIN_MANUALLY_MESSAGE:ClientConstants.LOGIN_MESSAGE);
			}
			this.messageField.setText(errorMsg);
		}
		else {
			/**
			 * Added for scanning failure : Confirm associate name
			 */
			String associateName = ClientMainFx.getInstance().getAccessControlManager().getAssociateName(userId);
			associateName = StringUtils.isEmpty(associateName)?userId:associateName;
			if(!MessageDialog.confirm((Stage)this.getScene().getWindow(),"Are you "+associateName+"?")) {
				userIdField.setText("");
				userIdField.requestFocus();
				return LoginStatus.CANCEL;
			} else {
				// Call C++ function to stop scanning 
				getLogger().info("LoginPanelWithKeyPad.login() - User logged in manually...");
				if(appBean.isScannerActive()) {
					ProximityCardReader reader = new ProximityCardReader(appBean.getProximityCardReaderName());
					reader.loggedInManually();
				}
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
		if(appBean.isMapCardNumber()) {
			Integer cardIdMaxLength = appBean.getCardIdMaxLength();
			associateId = SmartCardReaderUtil.getUserId(userIdField.getText().trim(), appBean.getProximityCardReaderName(), cardIdMaxLength);
			if(associateId != null) {
				associateId = getAssociateIdWithPrefix(associateId);
			}
			return (associateId != null) ? associateId : StringUtils.EMPTY; 
		} 
		
		return userIdField.getText().trim();
	}

	public void reinitComponents(boolean changePasswordClicked) {
		this.getChildren().remove(mainPanel);
		if(!appBean.isLoginWithoutPassword()) {
			this.changePasswordClicked = changePasswordClicked;
		}
		mainPanel = initMainPanel();
		this.setCenter(mainPanel);
	}
	
	public boolean isResetPasswordButtonEnable() {
		return resetPasswordButtonEnable;
	}

	public void setResetPasswordButtonEnable(boolean resetPasswordButtonEnable) {
		this.resetPasswordButtonEnable = resetPasswordButtonEnable;
	}
	
	public void showMessage(String message) {
		message = (message!=null)?message:"";
		this.messageField.setText(message);
	}
	
	public void setDefaultFocus() {
		userIdField.requestFocus();
	}

	public boolean isScannerFailed() {
		return scannerFailed;
	}

	public void setScannerFailed(boolean scannerFailed) {
		this.scannerFailed = scannerFailed;
	}
	
	public void setHeading(String header) {
		headingText.setText(header!=null?header:"");
	}

	public void initFields() {
		userIdField.setText("");
		passwordField.setText("");
		setDefaultFocus();
		
	}

	/**
	 * @param showCancelButton the showCancelButton to set
	 */
	public void setShowCancelButton(boolean showCancelButton) {
		this.showCancelButton = showCancelButton;
	}

	/**
	 * @return the showCancelButton
	 */
	public boolean isShowCancelButton() {
		return showCancelButton;
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

	private String getAssociateIdWithPrefix(String associateId) {
		String[] prefixes = appBean.getAssociatePrefixes();
		if(prefixes != null && prefixes.length > 0) {
			LoginStatus loginStatus = null;
			String associateIdWithPrefix = "";
			for(String prefix: prefixes) {
				associateIdWithPrefix = prefix.trim() + associateId.trim();
				loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(associateIdWithPrefix);
				if (loginStatus == null) {
					ClientMainFx.getInstance().exitApplication(1);
				} else if(loginStatus == LoginStatus.USER_NOT_EXIST) {
					continue;
				} else if (loginStatus == LoginStatus.OK) {
					return associateIdWithPrefix;
				}
			}
		}
		return associateId;
	}

}
