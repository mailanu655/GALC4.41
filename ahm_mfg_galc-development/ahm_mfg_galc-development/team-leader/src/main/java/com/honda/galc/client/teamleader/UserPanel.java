package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.teamleader.model.SecurityGroupTableModel;
import com.honda.galc.client.teamleader.model.UserSecurityGroupTableModel;
import com.honda.galc.client.teamleader.model.UserTableModel;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.LabeledPasswordField;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.conf.SecurityGroupDao;
import com.honda.galc.dao.conf.UserDao;
import com.honda.galc.dao.conf.UserSecurityGroupDao;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.util.SortedArrayList;
/**
 * 
 * <h3>UserPanel Class description</h3>
 * <p> UserPanel description </p>
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
 * Mar 29, 2011
 *
 *
 */
public class UserPanel extends TabbedPanel implements ListSelectionListener, DocumentListener {

	private static final long serialVersionUID = 1L;
	
	private List<User> users;
	private List<SecurityGroup> securityGroups;
	private List<UserSecurityGroup> userSecurityGroups = new ArrayList<UserSecurityGroup>();
	
	private TablePane userTablePane;
	private TablePane securityGroupTablePane;
	private TablePane userSecurityGroupTablePane;
	
	private UserTableModel userTableModel;
	private SecurityGroupTableModel securityGroupTableModel;
	private UserSecurityGroupTableModel userSecurityGroupTableModel;
	
	private LabeledTextField userIdField = new LabeledTextField("User ID");
	private LabeledTextField userNameField = new LabeledTextField("User Name");
	private LabeledPasswordField passwordField = new LabeledPasswordField("Password");
	private LabeledPasswordField confirmPasswordField = new LabeledPasswordField("Confirm Password");
	private LabeledTextField updateDateField = new LabeledTextField("Password Updated");
	private LabeledTextField expireDaysField = new LabeledTextField("Expire Days");
	
	private JButton addButton = new JButton("<<");
	private JButton removeButton = new JButton(">>");
	
	private JButton newButton = new JButton("New");
	private JButton saveButton = new JButton("Save");
	private JButton deleteButton = new JButton("Delete");
	
	
	public UserPanel() {
		super("User", KeyEvent.VK_U);
	}

	@Override
	public void onTabSelected() {
		
		if(isInitialized) return;
		
		initComponents();
		loadData();
		addListeners();
		isInitialized = true;
	}
	
	private void initComponents() {
		setLayout(new BorderLayout());
		add(createUserTablePane(),BorderLayout.WEST);
		add(createRightPanel(),BorderLayout.CENTER);
		
	}
	
	private void addListeners() {
		
		userTablePane.addListSelectionListener(this);
		userIdField.getComponent().getDocument().addDocumentListener(this);
		userNameField.getComponent().getDocument().addDocumentListener(this);;
		passwordField.getComponent().getDocument().addDocumentListener(this);
		expireDaysField.getComponent().getDocument().addDocumentListener(this);
		addButton.addActionListener(this);
		removeButton.addActionListener(this);
		newButton.addActionListener(this);
		saveButton.addActionListener(this);
		deleteButton.addActionListener(this);
		
	}
	
	private Component createUserTablePane() {
		
		userTablePane = new TablePane("User List");
		userTableModel = new UserTableModel(users,userTablePane.getTable());
		userTableModel.pack();
		userTablePane.setPreferredWidth(300);
		return userTablePane;
		
	}
	
	private Component createSecurityGroupTablePane() {
		
		securityGroupTablePane = new TablePane("Security Group List");
		securityGroupTablePane.setPreferredWidth(300);
		securityGroupTableModel = new SecurityGroupTableModel(securityGroups,securityGroupTablePane.getTable());
		securityGroupTableModel.pack();
		return securityGroupTablePane;
		
	}
	
	private Component createUserSecurityGroupTablePane() {
		
		userSecurityGroupTablePane = new TablePane("Assigned Security Group");
		userSecurityGroupTablePane.setPreferredWidth(300);
		userSecurityGroupTableModel = new UserSecurityGroupTableModel(userSecurityGroups,userSecurityGroupTablePane.getTable());
		userSecurityGroupTableModel.pack();
		return userSecurityGroupTablePane;
		
	}
	
	private JPanel createAddRemovePanel() {
		
		JPanel panel = new JPanel();
		addButton.setAlignmentX(CENTER_ALIGNMENT);
		removeButton.setAlignmentX(CENTER_ALIGNMENT);
		deleteButton.setAlignmentX(CENTER_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		panel.add(Box.createVerticalGlue());
		panel.add(addButton);
		panel.add(Box.createVerticalStrut(10));
		panel.add(removeButton);
		panel.add(Box.createVerticalGlue());
		return panel;
		
	}
	
	private JPanel createButtonPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		
		newButton.setFont(new java.awt.Font("dialog", 0, 18));
		saveButton.setFont(new java.awt.Font("dialog", 0, 18));
		saveButton.setEnabled(false);
		deleteButton.setFont(new java.awt.Font("dialog", 0, 18));
		deleteButton.setEnabled(false);
		
		
		panel.add(Box.createHorizontalGlue());
		panel.add(newButton);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(saveButton);
		panel.add(Box.createHorizontalStrut(10));
		panel.add(deleteButton);
		panel.add(Box.createHorizontalStrut(10));
		
		ViewUtil.setInsets(panel, 10, 10, 10, 10);
		return panel;
		
	}
	
	private Component createUserDataPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
		userIdField.setFont(new java.awt.Font("dialog", 0, 18));
		userIdField.getLabel().setPreferredSize(new Dimension(160, 20));
		userIdField.setInsets(5, 5, 5, 5);
		userNameField.setFont(new java.awt.Font("dialog", 0, 18));
		userNameField.getLabel().setPreferredSize(new Dimension(160, 20));
		userNameField.setInsets(5, 5, 5, 5);
		passwordField.setFont(new java.awt.Font("dialog", 0, 18));
		passwordField.getLabel().setPreferredSize(new Dimension(160, 20));
		passwordField.setInsets(5, 5, 5, 5);
		
		
		confirmPasswordField.setFont(new java.awt.Font("dialog", 0, 18));
		confirmPasswordField.getLabel().setPreferredSize(new Dimension(160, 20));
		confirmPasswordField.setInsets(5, 5, 5, 5);
		updateDateField.setFont(new java.awt.Font("dialog", 0, 18));
		updateDateField.getLabel().setPreferredSize(new Dimension(160, 20));
		updateDateField.getComponent().setEnabled(false);
		updateDateField.setInsets(5, 5, 5, 5);
		expireDaysField.getComponent().setText("0");
		expireDaysField.setFont(new java.awt.Font("dialog", 0, 18));
		expireDaysField.getLabel().setPreferredSize(new Dimension(160, 20));
		expireDaysField.setInsets(5, 5, 5, 5);
		
		panel.add(userIdField);
		panel.add(userNameField);
		panel.add(passwordField);
		panel.add(confirmPasswordField);
		panel.add(updateDateField);
		panel.add(expireDaysField);
		return panel;
		
	}
	
	private Component createSecurityGroupPanel() {
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createUserSecurityGroupTablePane(),BorderLayout.WEST);
		panel.add(createAddRemovePanel(),BorderLayout.CENTER);
		panel.add(createSecurityGroupTablePane(),BorderLayout.EAST);
		
		return panel;
		
	}
	
	private Component createRightPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(createUserDataPanel(),BorderLayout.NORTH);
		panel.add(createSecurityGroupPanel(),BorderLayout.CENTER);
		panel.add(createButtonPanel(),BorderLayout.SOUTH);

		return panel;
	}
	
	
	private void loadData() {
		try{
			users = new SortedArrayList<User>(getDao(UserDao.class).findAll(),"getUserId");
			userTableModel.refresh(users);
			securityGroups = getDao(SecurityGroupDao.class).findAll();
			securityGroupTableModel.refresh(securityGroups);
			newUser();
		}catch(Exception e) {
			handleException(e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		clearErrorMessage();
		
		try{
			if(e.getSource() == addButton) 	addSecurityGroup();
			else if(e.getSource() == removeButton)	removeSecurityGroup();
			else if(e.getSource() == newButton) newUser();
			else if(e.getSource() == saveButton)saveUser();
			else if(e.getSource() == deleteButton)deleteUser();
			
		}catch(Exception ex) {
			handleException(ex);
		}
	}
	
	private void addSecurityGroup() {
		
		SecurityGroup selected = securityGroupTableModel.getSelectedItem();
		if(selected == null ||containsSecurityGroup(selected.getId())) return;
		
		UserSecurityGroup userSecurityGroup = new UserSecurityGroup(userIdField.getComponent().getText(),selected.getId());
		userSecurityGroup.setSecurityGroup(selected);
		userSecurityGroups.add(userSecurityGroup);
		userSecurityGroupTableModel.refresh(userSecurityGroups);
		
		saveButton.setEnabled(true);
			
	}
	
	private void removeSecurityGroup() {
		
		UserSecurityGroup selected = userSecurityGroupTableModel.getSelectedItem();
		if(selected == null) return;
		
		userSecurityGroups.remove(selected);
		userSecurityGroupTableModel.refresh(userSecurityGroups);
		
		saveButton.setEnabled(true);
	}
	
	private void newUser() {
		
		userTablePane.clearSelection();
		userIdField.getComponent().setText("");
		userIdField.getComponent().setEnabled(true);
		userNameField.getComponent().setText("");
		passwordField.getComponent().setText("");
		confirmPasswordField.getComponent().setText("");
		updateDateField.getComponent().setText("");
		
		expireDaysField.getComponent().setText("0");
		
		userSecurityGroups.clear();
		userSecurityGroupTableModel.refresh(userSecurityGroups);
		
		saveButton.setEnabled(false);
		deleteButton.setEnabled(false);
		
	}
	
	private void deleteUser() {
		
		if(userIdField.getComponent().isEnabled()) return;
		
		String userId = userIdField.getComponent().getText().trim();
		
		if(MessageDialog.confirm(this, "Are you sure to delete the user " + userIdField.getComponent().getText() + " ?")) {
			getDao(UserSecurityGroupDao.class).deleteAllByUserId(userId);
			logUserAction("deleted UserSecurityGroup by user ID: " + userId);
			
			getDao(UserDao.class).removeByKey(userId);
			logUserAction("deleted User by ID: " + userId);
			loadData();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void saveUser() {
		if(userIdField.getComponent().isEnabled() && hasUser(userIdField.getComponent().getText())) {
			setErrorMessage("User exists");
			return;
		}
		if(StringUtils.isEmpty(userIdField.getComponent().getText())){
			setErrorMessage("Please set the user Id");
			return;
		}
		if(StringUtils.isEmpty(passwordField.getComponent().getText())){
			setErrorMessage("Please set the password");
			return;
		}
		
		String passwd = passwordField.getComponent().getText();
		if(StringUtils.length(passwd) > 8) {
			setErrorMessage("password must be no more than 8 chars");
			return;
		}
		
		if(!passwordField.getComponent().getText().equals(confirmPasswordField.getComponent().getText())) {
				setErrorMessage("Confirm password is incorrect");
				return;
		}
		
		String days = expireDaysField.getComponent().getText();
		if(!StringUtils.isNumeric(days)) {
			setErrorMessage("Expire days must be digits");
			return;
		}
		
		User user = userIdField.getComponent().isEnabled()? new User() : userTableModel.getSelectedItem();
		
		if(user == null) return;
		
		user.setUserId(userIdField.getComponent().getText());
		user.setUserName(userNameField.getComponent().getText());
		user.setPasswd(passwordField.getComponent().getText());
		user.setPasswordUpdateDate(DateFormatUtils.format(new Date(), "yyyyMMdd"));
		user.setExpireDays(Integer.parseInt(days));
		getDao(UserDao.class).save(user);
		logUserAction(SAVED, user);
		
		for(UserSecurityGroup item : userSecurityGroups) {
			if(StringUtils.isEmpty(item.getId().getUserId())) item.getId().setUserId(user.getUserId());
		}
		
		List<UserSecurityGroup> originalUserSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(user.getId());
		
		List<UserSecurityGroup> removedUserSecurityGroups = new ArrayList<UserSecurityGroup>();
		for(UserSecurityGroup userSecurityGroup : originalUserSecurityGroups) {
			if(!containUserSecurityGroup(userSecurityGroup)) removedUserSecurityGroups.add(userSecurityGroup);
		}
		
		getDao(UserSecurityGroupDao.class).saveAll(userSecurityGroups);
		
		if(!removedUserSecurityGroups.isEmpty()) {
			getDao(UserSecurityGroupDao.class).removeAll(removedUserSecurityGroups);
		}
			
		logUserAction(SAVED, userSecurityGroups);
		if(userIdField.getComponent().isEnabled()) {
			loadData();
			User aUser = findUser(user.getUserId());
			userTableModel.selectItem(aUser);
		}	
		userIdField.getComponent().setEnabled(false);
		
		saveButton.setEnabled(false);
		deleteButton.setEnabled(true);
		
		setMessage("User information saved successfully");
		
	}
	
	private boolean containUserSecurityGroup(UserSecurityGroup userSecurityGroup) {
		for(UserSecurityGroup item: userSecurityGroups) {
			if(userSecurityGroup.getId().equals(item.getId())) return true;
		}
		return false;

	}
	
	private User findUser(String userId) {
		for(User user : users) {
			if(user.getUserId().equals(userId)) return user;
		}
		return null;
	}
	
	
	private boolean containsSecurityGroup(String groupId) {
		for(UserSecurityGroup item : userSecurityGroups) {
			if(item.getSecurityGroupId().equals(groupId)) return true;
		}
		return false;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		if(e.getSource()==userTablePane.getTable().getSelectionModel())
			userSelected();
		
	}
	
	private void userSelected() {
		User user = userTableModel.getSelectedItem();
		if(user == null) return;
		
		userIdField.getComponent().setText(user.getId());
		userIdField.getComponent().setEnabled(false);
		userNameField.getComponent().setText(user.getUserName());
		passwordField.getComponent().setText(user.getPasswd());
		confirmPasswordField.getComponent().setText(user.getPasswd());
		updateDateField.getComponent().setText(user.getPasswordUpdateDateString());
		expireDaysField.getComponent().setText(Integer.toString(user.getExpireDays()));
		
		userSecurityGroups = getDao(UserSecurityGroupDao.class).findAllByUserId(user.getId());
		userSecurityGroupTableModel.refresh(userSecurityGroups);
		
		saveButton.setEnabled(false);
		deleteButton.setEnabled(true);
	}
	
	private boolean hasUser(String userId) {
		for(User user : users) {
			if(user.getUserId().equals(userId)) return true;
		}
		return false;
	}

	public void changedUpdate(DocumentEvent e) {
		
		clearErrorMessage();
		saveButton.setEnabled(true);
		
	}

	public void insertUpdate(DocumentEvent e) {
		clearErrorMessage();
		saveButton.setEnabled(true);
	}

	public void removeUpdate(DocumentEvent e) {
		clearErrorMessage();
		saveButton.setEnabled(true);
	}

}
