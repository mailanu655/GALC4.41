package com.honda.galc.client.ui;
import static com.honda.galc.common.logging.Logger.getLogger;

import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.ui.component.FxDialog;
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
 * <TD>Jeffray Huang</TD>
 * <TD>Nov 6, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 * 
 * @author Suriya Sena JavaFx Migration
 * Feb 20 2014 
 */


public class LoginDialog extends FxDialog implements EventHandler<javafx.event.ActionEvent>{
    
    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
    
    private LoginStatus loginStatus;
    
    private boolean changePasswordClicked = false;
   
    private String userId = null;
    
    private LoginPanel loginPanel = new LoginPanel();
    
    private boolean allowExit = true;
    private boolean allowCancel = true;
    
    private KeyBoardPopup popup;
    
    private ApplicationPropertyBean appBean;
    
    private static LoginDialog loginDialog;
    
    private LoginPanelWithKeyPad  keyPad;
    
    public LoginDialog(Stage owner){
    	super("", owner);
    	super.setResizable(false);
    	setTitle("Welcome to the GALC 2.0");
    	initOwner(owner);
    	initModality(Modality.WINDOW_MODAL);
        initComponent();
        centerOnScreen();
        sizeToScene();
        toFront();
        showAndWait();
        loginDialog = this;
     }
    
    public LoginDialog(){
    	this(null);
    }

    public LoginDialog(Stage owner, boolean allowExit, boolean allowCancel) {
    	super("", owner);
    	appBean = PropertyService.getPropertyBean(
				ApplicationPropertyBean.class, ClientMainFx.getInstance().getApplicationContext()
						.getApplicationId());
    	loginDialog = this;
    	this.allowExit = allowExit;
    	this.allowCancel = allowCancel;
    	setResizable(false);
    	setTitle("Welcome to the GALC 2.0");
    	initOwner(owner);
    	initModality(Modality.WINDOW_MODAL);
        initComponent();
        centerOnScreen();
        sizeToScene();
        toFront();
        if(appBean.isLoginWithoutPassword() && appBean.isScannerActive()) {
        	startScannerService();
        }
        showAndWait();
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
							keyPad.showMessage(loginStatus.getMessage() + ". " + ClientConstants.LOGIN_MESSAGE);
							setDefaultFocusAndStartScannerService();
						}
						else {
							loginDialog.setUserId(associateId.trim());
							loginDialog.close();
						}
					}
					else {
						//Associate id from scanner mapping table is blank
						keyPad.showMessage("User name not found. " + ClientConstants.LOGIN_MESSAGE);
						setDefaultFocusAndStartScannerService();
					}
				}
				else {
					//If scanner returns null for an associate id
					keyPad.setScannerFailed(true);
					if(loginDialog.isShowing()) {
						keyPad.showMessage(ClientConstants.LOGIN_MESSAGE);
						keyPad.setDefaultFocus();
					}
				}
			}

			

			
		});
		
		
		service.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent t) {
				if(null != keyPad) {
					keyPad.setScannerFailed(true);
					if(loginDialog.isShowing()) {
						keyPad.showMessage("Scanning Failed! " + ClientConstants.LOGIN_MANUALLY_MESSAGE);
						keyPad.setDefaultFocus();
					}
				}
			}
		});
		
		service.start();
		
	}
	
	protected void setDefaultFocusAndStartScannerService() {
		keyPad.setDefaultFocus();
		startScannerService();
		if(!loginDialog.isShowing()) {
			loginDialog.showAndWait();		
		}
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

	private void initComponent() {
		loginPanel = new LoginPanel(allowCancel);
        Scene primaryScene = new Scene(loginPanel);
        
        primaryScene.getStylesheets().add(ClientMainFx.getInstance().getStylesheetPath());
        
        this.setScene(primaryScene);
        
    	loginPanel.getOkButton().setOnAction(this);
        loginPanel.getCancelButton().setOnAction(this);
        
    	if(loginPanel.isResetPasswordButtonEnable()) {
           loginPanel.getChangePasswordButton().setOnAction(this);
    	}
        
        loginPanel.getOkButton().setDefaultButton(true);
        if(!allowExit){
        	this.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent we) {
					we.consume();
					
				}
			});
        }
       
       
        createKeyBoardPopup();
		primaryScene.getStylesheets().add(this.getClass().getResource("/resource/com/honda/galc/client/ui/keypad/css/KeyboardButtonStyle.css").toExternalForm());

		// add keyboard scene listener to all text components
			primaryScene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
				@Override
				public void changed(ObservableValue<? extends Node> value, Node n1, Node n2) {
					if (n2 != null) {
						if(n2 instanceof ComboBox){
							CommonUtil.setPopupVisible(true, (ComboBox) n2,popup);
						}else if(n2 instanceof PasswordField){
							CommonUtil.setPopupVisible(true, (PasswordField) n2,popup);
						}
						
						
					} else {
						CommonUtil.setPopupVisible(false, null,popup);
					}
				}
			});
			
			popup.show(this);
		
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
	
	public String getUserId() {
		return userId;
	}
	
	public static LoginStatus login() {
		return login(null);
	}
	
	public static LoginStatus login(Stage owner, boolean allowExit, boolean allowCancel) {
    	LoginDialog loginDialog = new LoginDialog(owner,allowExit, allowCancel);
    	loginDialog.toFront();
    	loginDialog.centerOnScreen();
		return loginDialog.getLoginStatus();
	}

	public static LoginStatus login(Stage owner) {
		String componentId = ClientMainFx.getInstance().getApplicationContext().getProcessPointId();
		ApplicationPropertyBean  property = PropertyService.getPropertyBean(ApplicationPropertyBean.class, componentId);
	
		LoginStatus loginStatus = LoginStatus.CANCEL;
		boolean autoLogin = property.isAutoLogin();
		String autoLoginUser = property.autoLoginUser();
		if(autoLogin && autoLoginUser.trim().length() > 0){
			loginStatus = ClientMainFx.getInstance().getAccessControlManager().login_without_password(autoLoginUser.trim());
		}
			
		
		if(loginStatus == LoginStatus.OK) return loginStatus;
		
		LoginDialog loginDialog = new LoginDialog(owner);
		
		loginDialog.centerOnScreen();
		return loginDialog.getLoginStatus();
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
		}else{
			popup.show(popup.getOwnerWindow());
		}
	}
	
	private void cancel() {
		if(changePasswordClicked) {
			changePasswordClicked = false;
			loginPanel.reinitComponents(changePasswordClicked);
			sizeToScene();
		} else {
			loginStatus = LoginStatus.CANCEL;
			this.close();
		}
	}
	
	private void changePasswordButtonClicked() {
		changePasswordClicked = true;
		loginPanel.reinitComponents(changePasswordClicked);
		sizeToScene();
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
	
	
	public void setUserId(String userId) {
		loginDialog.userId = userId;
	}
	
}    
