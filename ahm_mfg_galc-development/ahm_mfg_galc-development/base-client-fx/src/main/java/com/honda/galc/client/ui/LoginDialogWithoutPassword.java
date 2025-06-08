package com.honda.galc.client.ui;
import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.ScannerService;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>LogonDialog</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Alok Ghode</TD>
 * <TD>Apr 24, 2014</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * 
 * @author Alok Ghode
 * Apr 24 2014 
 */


public class LoginDialogWithoutPassword extends Stage implements EventHandler<javafx.event.ActionEvent>{
    
    private static final long serialVersionUID = 1L;
    
    private LoginStatus loginStatus;
    
    private boolean changePasswordClicked = false;
   
    private static String userId = null;
    
    private LoginPanelWithKeyPad loginPanel;
    
    private KeyBoardPopup popup;
    
    private static LoginDialogWithoutPassword loginDialog;
    
    private ApplicationPropertyBean appBean;
    
    private LoginDialogWithoutPassword(Stage owner, String dialogHeader, boolean showCancelButton){
    	super(StageStyle.UTILITY);
    	appBean = PropertyService.getPropertyBean(
				ApplicationPropertyBean.class, ClientMainFx.getInstance().getApplicationContext()
						.getApplicationId());
    	loginDialog = this;
    	loginPanel = new LoginPanelWithKeyPad(dialogHeader, showCancelButton);
    	loginPanel.setShowCancelButton(showCancelButton);
    	setResizable(false);
    	setTitle("Welcome to the GALC 2.0");
    	initOwner(owner);
    	initModality(Modality.APPLICATION_MODAL);
    	setScreenSize();
        initComponent();
        centerOnScreen();
        toFront();
        if(appBean.isScannerActive()) {
        	startScannerService();
        }
		showAndWait();
     }
    
    private LoginDialogWithoutPassword(){
    	this(null, ClientConstants.DEFAULT_LOGIN_CAPTION, false);
    }
    
    private void setScreenSize() {
		Rectangle2D primarySceneBounds = Screen.getPrimary().getVisualBounds();
		this.setMaxHeight(primarySceneBounds.getHeight());
		this.setMaxWidth(primarySceneBounds.getWidth());
		this.setHeight(primarySceneBounds.getHeight());
		this.setWidth(primarySceneBounds.getWidth());
	}

    private void initComponent() {
        Scene primaryScene = new Scene(loginPanel);
        
        primaryScene.getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());
        this.setScene(primaryScene);
        
    	loginPanel.getOkButton().setOnAction(this);
        loginPanel.getCancelButton().setOnAction(this);
        loginPanel.getOkButton().setDefaultButton(true);
        
        /**
		 * Added by vfc01778: Keypad settings: Start
		 */
        createKeyBoardPopup();
		primaryScene.getStylesheets().add(this.getClass().getResource("/resource/com/honda/galc/client/ui/keypad/css/KeyboardButtonStyle.css").toExternalForm());

		// add keyboard scene listener to all text components
		primaryScene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> value, Node n1, Node n2) {
				if (n2 != null && n2 instanceof TextInputControl) {
					CommonUtil.setPopupVisible(true, (TextInputControl) n2,popup);
					
				} else {
					CommonUtil.setPopupVisible(false, null,popup);
				}
			}
		});
		
		popup.show(this);
		/**
		 * Added by vfc01778: Keypad settings: End
		 */
   }
    
	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public boolean isLoginOk() {
		return loginStatus == LoginStatus.OK;
	}
	
	public static String getUserId() {
		return userId;
	}
	
	public static LoginStatus login() {
		return login(null, ClientConstants.DEFAULT_LOGIN_CAPTION, false);
	}
	
	public static LoginStatus login(String header) {
		return login(null, header, false);
	}

	public static LoginStatus login(String header, boolean showCancelButton){
		return login(null, header, showCancelButton);
	}
	
	public static LoginStatus login(Stage owner, String header, boolean showCancelButton) {
		ApplicationPropertyBean  property = PropertyService.getPropertyBean(ApplicationPropertyBean.class, 
				ClientMainFx.getInstance().getApplicationContext().getProcessPointId());
	
		LoginStatus loginStatus = LoginStatus.CANCEL;
		boolean autoLogin = property.isAutoLogin();
		String autoLoginUser = property.autoLoginUser();
		if(autoLogin && autoLoginUser.trim().length() > 0)
			loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(autoLoginUser);
		
		if(loginStatus == LoginStatus.OK) return loginStatus;
		
		loginDialog = new LoginDialogWithoutPassword(owner, header, showCancelButton);
		loginDialog.toFront();
		return loginDialog.getLoginStatus();
	}

	private void startScannerService() {
		ScannerService service = new ScannerService(
				appBean.getProximityCardReaderName());

		service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent t) {
				if(t.getSource().getValue()!=null) {
					String cardNumber = t.getSource().getValue().toString();
					String associateId = ClientMainFx.getInstance().getAccessControlManager()
							.mapProxCardNumber(new Integer(cardNumber));

					if(!StringUtils.isBlank(associateId)) {
						LoginStatus loginStatus = doScannedAssociateLogin(associateId);
						loginDialog.setLoginStatus(loginStatus);
						if (loginStatus == null) {
							ClientMainFx.getInstance().exitApplication(1);
						}
						else if(loginStatus != LoginStatus.OK) {
							loginPanel.showMessage(loginStatus.getMessage() + ". " + ClientConstants.LOGIN_MESSAGE);
							loginPanel.setDefaultFocus();
							startScannerService();
							if(!loginDialog.isShowing()) {
								loginDialog.showAndWait();		
							}
						}
						else {
							loginDialog.setUserId(associateId.trim());
							loginDialog.close();
						}
					}
					else {
						//Associate id from scanner mapping table is blank
						loginPanel.showMessage("User name not found. " + ClientConstants.LOGIN_MESSAGE);
						loginPanel.setDefaultFocus();
						startScannerService();
						if(!loginDialog.isShowing()) {
							loginDialog.showAndWait();		
						}
					}
				}
				else {
					//If scanner returns null for an associate id
					loginPanel.setScannerFailed(true);
					if(loginDialog.isShowing()) {
						loginPanel.showMessage(ClientConstants.LOGIN_MESSAGE);
						loginPanel.setDefaultFocus();
					}
				}
			}
		});
		service.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				loginPanel.setScannerFailed(true);
				if(loginDialog.isShowing()) {
					loginPanel.showMessage("Scanning Failed! " + ClientConstants.LOGIN_MANUALLY_MESSAGE);
					loginPanel.setDefaultFocus();
				}
			}
		});
		
		service.start();
		
	}
	
	private LoginStatus doScannedAssociateLogin(String associateId) {
		String[] prefixes = appBean.getAssociatePrefixes();
		if(prefixes!=null && prefixes.length > 0) {
			LoginStatus loginStatus = null;
			for(String prefix: prefixes) {
				getLogger().info("Validating for Honda id prefix: "+prefix);
				loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(prefix.toLowerCase().trim() + associateId.trim());
				if (loginStatus == null) {
					ClientMainFx.getInstance().exitApplication(1);
				}
				else if(loginStatus == LoginStatus.USER_NOT_EXIST) {
					continue;
				}
				else {
					return loginStatus;
				}
			}
		}
		return ClientMainFx.getInstance().getAccessControlManager().login_without_password(associateId.trim());
	}

	public LoginPanelWithKeyPad getLoginPanel() {
		return loginPanel;
	}

	public void setUserId(String userId) {
		LoginDialogWithoutPassword.userId = userId;
	}

	@Override
	public void handle(ActionEvent e) {
		if(e.getSource().equals(loginPanel.getOkButton())) {
			doLogin();
		} else if(e.getSource().equals(loginPanel.getCancelButton())) {
			cancel();
		} else if(e.getSource().equals(loginPanel.getChangePasswordButton())) {
			changePasswordButtonClicked();
		}
	}
	
	private void doLogin() {
		popup.hide();
		loginStatus = changePasswordClicked ? 	loginPanel.changePassword() : 
			loginPanel.login();
		if(loginStatus == LoginStatus.OK) {
			userId = loginPanel.getUserId();
			this.close();
		}
		else {
			popup.show(popup.getOwnerWindow());
		}
	}
	
	private void cancel() {
		if(changePasswordClicked) {
			changePasswordClicked = false;
			loginPanel.reinitComponents(changePasswordClicked);
		} else {
			loginStatus = LoginStatus.CANCEL;
			this.close();
		}
	}
	
	private void changePasswordButtonClicked() {
		changePasswordClicked = true;
		loginPanel.reinitComponents(changePasswordClicked);
	}
	
	private void createKeyBoardPopup() {
		String fontUrl = this.getClass().getResource("/resource/com/honda/galc/client/ui/keypad/font/FontKeyboardFX.ttf").toExternalForm();
		Font.loadFont(fontUrl, -1);
		popup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		popup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null,popup);
			}
		});
	}
	
	}    
