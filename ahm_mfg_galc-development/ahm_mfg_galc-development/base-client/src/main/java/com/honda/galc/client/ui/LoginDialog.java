package com.honda.galc.client.ui;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import com.honda.galc.client.ClientMain;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.property.PropertyService;


/**
 * 
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
 */

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LoginDialog extends JDialog implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private LoginStatus loginStatus;
    
    private boolean changePasswordClicked = false;
   
    String userId = null;
    
    private boolean allowExit = true;
    private boolean allowCancel = true;
    private boolean allowChangePsswd = true;
    
    private LoginPanel loginPanel ;
    
    public LoginDialog(){
    	
    	super(MainWindow.getFocusedMainWindow(),true);
    	
    	setTitle("GALC USER LOGIN");
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(420, 280);
        initComponent();
        this.setAlwaysOnTop(true);
        pack();
    }
    
    public LoginDialog(boolean allowExit) {
    	this();
    	this.allowExit = allowExit;
    }
    
    public LoginDialog(Frame frame){
        super(MainWindow.getFocusedMainWindow(),true);
    	setTitle("GALC USER LOGIN");
    	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(420, 280);
       // initComponent();
        this.setAlwaysOnTop(true);
        pack();
        
     }
    
    public LoginDialog(Frame frame,boolean allowExit, boolean allowCancel){
    	this(frame, allowExit, allowCancel, true);
    }    
    
    public LoginDialog(Frame frame, boolean allowExit, boolean allowCancel, boolean allowChangePsswd) {
    	this(frame);
    	this.allowExit = allowExit;
    	this.allowCancel = allowCancel;
    	this.allowChangePsswd = allowChangePsswd;
    	initComponent();
    	pack();
	}

	private void initComponent(){
        loginPanel = new LoginPanel(allowCancel, allowChangePsswd);
        this.getContentPane().add(loginPanel);
        setName("Login Dialog");
    	loginPanel.getOkButton().addActionListener(this);
        loginPanel.getCancelButton().addActionListener(this);
    	if(loginPanel.isResetPasswordButtonEnable())
    	{
           loginPanel.getChangePasswordButton().addActionListener(this);
    	}
        
        this.getRootPane().setDefaultButton(loginPanel.getOkButton());
        this.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		if(allowCancel)
        		((LoginDialog)e.getSource()).dispose();
        		if(!allowExit & allowCancel) 
        			ClientMain.getInstance().exitApplication(1);
        	}
        });
   }

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(loginPanel.getOkButton())) doLogin();
		else if(e.getSource().equals(loginPanel.getCancelButton())) cancel();
		else if(e.getSource().equals(loginPanel.getChangePasswordButton())) changePasswordButtonClicked();
	}
    
	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}
	
	private void doLogin() {
		
		loginStatus = changePasswordClicked ? 	loginPanel.changePassword() : 
												loginPanel.login();
		if(loginStatus == LoginStatus.OK) {
			userId = loginPanel.getUserId();
			this.setVisible(false);
			this.dispose();
		}
	}
	
	private void cancel() {
		if(changePasswordClicked) {
			changePasswordClicked = false;
			loginPanel.reinitComponents(changePasswordClicked);
			pack();
		}else {
			if(allowExit){
				loginStatus = LoginStatus.CANCEL;
				this.setVisible(false);
				this.dispose();
			}
		}
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

	public static LoginStatus login(Frame frame) {
		return login(frame,true, true, true);
	}
	
	public static LoginStatus login(Frame frame, boolean allowExit, boolean allowCancel, boolean enableChangePsswrd){
		String componentId = ClientMain.getInstance().getApplicationContext().getProcessPointId();
		ApplicationPropertyBean  property = PropertyService.getPropertyBean(ApplicationPropertyBean.class, componentId);
		
		
		LoginStatus loginStatus = LoginStatus.CANCEL;
		boolean autoLogin = property.isAutoLogin();
		String autoLoginUser = property.autoLoginUser();
		if(autoLogin && autoLoginUser.trim().length() > 0)
			loginStatus = ClientMain.getInstance().getAccessControlManager().login(autoLoginUser, "");
		
		if(loginStatus == LoginStatus.OK) return loginStatus;
		
		LoginDialog loginDialog = new LoginDialog(frame,allowExit, allowCancel,enableChangePsswrd);
		loginDialog.setLocationRelativeTo(frame);
		loginDialog.setVisible(true);
		return loginDialog.getLoginStatus();
	}
	
	
	private void changePasswordButtonClicked() {
		changePasswordClicked = true;
		loginPanel.reinitComponents(changePasswordClicked);
		pack();
		
	}
	


}    
