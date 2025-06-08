package com.honda.galc.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.client.ui.component.LabeledPasswordField;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.device.SmartCardReaderUtil;
import com.honda.galc.entity.conf.User;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LoginPanel Class description</h3>
 * <p> LoginPanel description </p>
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
 * Feb 29, 2010
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class LoginPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final String CAPS_LOCK_ON = "Caps Lock is on";

	private JPanel mainPanel;
	private LabeledTextField userIdField = new LabeledTextField("USER ID");
	private LabeledPasswordField passwordField = new LabeledPasswordField("PASSWORD");
	private LabeledPasswordField newPasswordField = new LabeledPasswordField("NEW PASSWORD");
	private LabeledPasswordField confirmPasswordField = new LabeledPasswordField("CONFIRM");
	private JLabel messageField = new JLabel();
	private JLabel capsLockField = new JLabel();
	private JButton okButton = new JButton("Ok");
	private JButton cancelButton = new JButton("Cancel");
	private JButton changePasswordButton = new JButton("Change Password");
	private boolean changePasswordClicked = false;
	private boolean resetPasswordButtonEnable=false;
	private boolean cancelOption = true;
	private boolean allowChangePsswd = true;

	private boolean loginWithoutPassword = false;
	private boolean mapCardNumer = false;
	private ApplicationPropertyBean  property;



	public LoginPanel(boolean canceOption, boolean allowChangePsswd) {
		this.allowChangePsswd = allowChangePsswd;
		setPasswdResetEnableFlag();
		this.cancelOption = canceOption;
		mainPanel = initMainPanel();
		this.add(mainPanel);
		userIdField.getComponent().addActionListener(this);
		passwordField.getComponent().addActionListener(this);
		passwordField.getComponent().addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				doCapsLockCheck();
			}
			@Override
			public void focusLost(FocusEvent e) {
				capsLockField.setText(null);
			}
		});
		{
			final String doCapsLockCheck = "doCapsLockCheck";
			this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("CAPS_LOCK"), doCapsLockCheck);
			this.getActionMap().put(doCapsLockCheck, new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					if (passwordField.getComponent().hasFocus()) {
						doCapsLockCheck();
					}
				}
			});
		}
	}

	private JPanel initMainPanel() {
		String componentId = ClientMain.getInstance().getApplicationContext().getProcessPointId();
		property = PropertyService.getPropertyBean(ApplicationPropertyBean.class, componentId);
		loginWithoutPassword  = property.isLoginWithoutPassword();
		mapCardNumer = property.isMapCardNumber();

		JPanel panel = new JPanel();
		int rows = changePasswordClicked ? 6:4;
		panel.setLayout(new GridLayout(rows,1));
		userIdField.setFont(new java.awt.Font("dialog", 0, 18));
		userIdField.getLabel().setPreferredSize(new Dimension(160, 25));
		passwordField.setFont(new java.awt.Font("dialog", 0, 18));
		passwordField.getLabel().setPreferredSize(new Dimension(160, 25));
		newPasswordField.setFont(new java.awt.Font("dialog", 0, 18));
		newPasswordField.getLabel().setPreferredSize(new Dimension(160, 25));
		confirmPasswordField.setFont(new java.awt.Font("dialog", 0, 18));
		confirmPasswordField.getLabel().setPreferredSize(new Dimension(160, 25));
		messageField.setHorizontalAlignment(SwingConstants.LEFT);
		messageField.setFont(new java.awt.Font("dialog", 0, 18));
		messageField.setBackground(Color.darkGray);
		messageField.setForeground(Color.red);
		capsLockField.setHorizontalAlignment(SwingConstants.RIGHT);
		capsLockField.setFont(new java.awt.Font("dialog", 0, 18));
		capsLockField.setBackground(Color.darkGray);
		capsLockField.setForeground(Color.blue);

		okButton.setName("ok");
		cancelButton.setName("cancel");
		changePasswordButton.setName("change password");
		panel.setBorder(new AbstractBorder() {

			private static final long serialVersionUID = 1L;

			public Insets getBorderInsets(Component c) { 
				return new Insets(10, 10, 10, 10);
			}
		});

		panel.add(userIdField);
		if(!loginWithoutPassword){
			panel.add(passwordField);
		}
		if(changePasswordClicked){
			panel.add(newPasswordField);
			panel.add(confirmPasswordField);
		}
		panel.add(createButtonPanel());
		panel.add(createMessagePanel());

		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		panel.add(okButton);
		if(cancelOption)
			panel.add(cancelButton);
		if(resetPasswordButtonEnable==true)
		{
			if(!changePasswordClicked) 
			{
				panel.add(changePasswordButton);
			}
		}
		return panel;
	}

	private JPanel createMessagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(messageField);
		panel.add(capsLockField);
		return panel;
	}

	private void setPasswdResetEnableFlag() 
	{

		String passwdResetButtonEnable = "";
		try {
			passwdResetButtonEnable = PropertyService.getProperty("ACL","PASSWORD_RESET_ENABLE");
			if(passwdResetButtonEnable!=null)
			{
				if(passwdResetButtonEnable.equals("TRUE"))
				{					
					this.resetPasswordButtonEnable=this.allowChangePsswd;
				}
			}				
		} catch (Exception e1) {
			System.out.println(" PASSWORD_RESET_ENABLE property is not found");
			e1.printStackTrace();
		}
	}

	public JTextField getUserIdField(){
		return userIdField.getComponent();
	}

	public JPasswordField getPasswordField() {
		return passwordField.getComponent();
	}

	public JButton getOkButton(){
		return okButton;
	}

	public JButton getCancelButton(){
		return cancelButton;
	}

	public JButton getChangePasswordButton() {
		return changePasswordButton;
	}


	public LoginStatus login() {
		String userId = getUserId();

		LoginStatus loginStatus;
		if (!loginWithoutPassword){
			String password = new String(getPasswordField().getPassword()).trim();
			loginStatus = ClientMain.getInstance().getAccessControlManager().login(userId, password);
		}else{
			loginStatus = ClientMain.getInstance().getAccessControlManager().loginWithoutPassword(userId);
		}

		if(loginStatus != LoginStatus.OK) this.messageField.setText(loginStatus.getMessage());
		if(loginStatus == LoginStatus.USER_NOT_EXIST) {
			this.userIdField.getComponent().setSelectionStart(0);
			this.userIdField.getComponent().setSelectionEnd(userIdField.getComponent().getText().length());
			this.userIdField.getComponent().requestFocus();
		}

		return loginStatus;

	}

	@SuppressWarnings("deprecation")
	public LoginStatus changePassword() {


		LoginStatus loginStatus = login();

		if(loginStatus != LoginStatus.OK) return loginStatus;
		String userId = getUserId();
		String newPassword = newPasswordField.getComponent().getText().trim();
		String confirmPassword = confirmPasswordField.getComponent().getText().trim();

		if(StringUtils.isEmpty(newPassword)){
			this.messageField.setText("new password cannot be empty");
			return LoginStatus.PASSWORD_INCORRECT;
		}

		if(!newPassword.equals(confirmPassword)) {
			messageField.setText("confirm password is different from new password");
			return LoginStatus.PASSWORD_INCORRECT;
		}

		if(!ServiceFactory.isServerAvailable()) {
			messageField.setText("Server is disconnected.Could not change the password");
			return LoginStatus.CANCEL;
		}

		try{
			User user = ServiceFactory.getDao(UserDao.class).findByKey(userId);
			user.setPasswd(newPassword);
			user.setPasswordUpdateDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
			ServiceFactory.getDao(UserDao.class).update(user);
		}catch(Exception e) {
			messageField.setText("Could not change the password due to server error");
			return LoginStatus.CANCEL;
		}


		return LoginStatus.OK;

	}

	public String getUserId() {
		String newUserId = userIdField.getComponent().getText().trim();

		if(mapCardNumer) {
			Integer cardIdMaxLength = property.getCardIdMaxLength();
			String userId = SmartCardReaderUtil.getUserId(newUserId, property.getProximityCardReaderName(), cardIdMaxLength);
			if(userId != null) {
				   userId = getAssociateIdWithPrefix(userId);
			}
			return (userId != null) ? userId : StringUtils.EMPTY; 
		} 
		else
			return newUserId;
	}

	public void reinitComponents(boolean changePasswordClicked ) {

		this.remove(mainPanel);
		this.changePasswordClicked = changePasswordClicked;
		mainPanel = initMainPanel();
		this.add(mainPanel);
		this.validate();

	}

	public boolean isResetPasswordButtonEnable() {
		return resetPasswordButtonEnable;
	}

	public void setResetPasswordButtonEnable(boolean resetPasswordButtonEnable) {
		this.resetPasswordButtonEnable = resetPasswordButtonEnable;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(userIdField.getComponent())) {
			if(loginWithoutPassword){
				getOkButton().doClick(1);
			}else{
				passwordField.getComponent().requestFocus();
			}
		}else if(e.getSource().equals(passwordField.getComponent())){
			getOkButton().doClick(1);
		}
	}

	protected void doCapsLockCheck() {
		final boolean capsLockState;
		try {
			capsLockState = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
		} catch (UnsupportedOperationException uoe) {
			return;
		}
		if (capsLockState) {
			this.capsLockField.setText(CAPS_LOCK_ON);
		} else {
			this.capsLockField.setText(null);
		}
	}

	private String getAssociateIdWithPrefix(String associateId) {
		String[] prefixes = property.getAssociatePrefixes();
		if(prefixes!=null && prefixes.length > 0) {
			LoginStatus loginStatus = null;
			String associateIdWithPrefix = "";
			for(String prefix: prefixes) {
				associateIdWithPrefix = prefix.trim() + associateId.trim();
				loginStatus = ClientMain.getInstance().getAccessControlManager().loginWithoutPassword(associateIdWithPrefix);
				if (loginStatus == null) {
					ClientMain.getInstance().exitApplication(1);
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
