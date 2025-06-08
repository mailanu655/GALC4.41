package com.honda.galc.client.teamleader.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.mvc.AbstractController;
import com.honda.galc.client.teamleader.qi.view.ReasonForChangeDialog;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;
import com.honda.galc.util.AuditLoggerUtil;
import com.honda.galc.util.CommonUtil;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * 
 * <h3>UserMaintenanceController Class description</h3>
 * <p>
 * UserMaintenanceController is used to perform the action on the user operation like create,update and delete system user .
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
 * @author LnTInfotech<br>
 * 
 */
public class UserMaintenanceController extends AbstractController<UserMaintenanceModel, UserMaintenancePanel>
implements EventHandler<ActionEvent> {

	private User currentUser;
	public final static String DATE_FORMAT =  "yyyyMMdd";
	

	public UserMaintenanceController(UserMaintenanceModel model, UserMaintenancePanel view) {
		super(model, view);
		this.currentUser = new User();
		getModel().setApplicationContext(getView().getMainWindow().getApplicationContext());
	}
	
	
	/**
	 * This method is used to handle event on User screen
	 * 
	 */
	public void handle(ActionEvent actionEvent) {
		if (actionEvent.getSource() instanceof LoggedButton) {
			if(((LoggedButton) actionEvent.getSource()).getId().equals(getView().getAssignedButton().getId())) 
				userSecurityGroupAssignment(actionEvent);
			else if(((LoggedButton) actionEvent.getSource()).getId().equals(getView().getUnassignedButton().getId()))
				userSecurityGroupUnassignment(actionEvent);
			else if(((LoggedButton) actionEvent.getSource()).getId().equals(getView().getNewButton().getId()))
				createNewUser(actionEvent);
			else if(((LoggedButton) actionEvent.getSource()).getId().equals(getView().getSaveButton().getId()))
				saveOrUpdateUser(actionEvent);
			else if(((LoggedButton) actionEvent.getSource()).getId().equals(getView().getDeleteButton().getId()))
				deleteUser(actionEvent);
		}

		if (actionEvent.getSource() instanceof LoggedTextField) {
			if(( (LoggedTextField) actionEvent.getSource()).getId().equals(
					getView().getUserFilterTextfield().getControl().getId())){
				if (getView().getUserFilterTextfield().getControl().isFocused())
					getView().reload(StringUtils.trimToEmpty(getView().getUserFilterTextfield().getText()));
			}
		}
	}

	/**
	 * This method is used to handle New button click event 
	 * 
	 */
	private void createNewUser(ActionEvent e) {
		getView().clearUserDetailsFields();
		getView().getUserTablePane().clearSelection();
		getView().getSecurityGroupTablePane().clearSelection();
		getView().getUserSecurityGroupTablePane().clearSelection();
		clearMessages();
		getView().getUserFilterTextfield().clear();
		getView().getUserTablePane().getTable().getItems().clear();
		getView().getUserSecurityGroupTablePane().getTable().getItems().clear();
		getView().getSecurityGroupTablePane().getTable().getItems().clear();
		getView().getUserTablePane().setData(getModel().findAllUser());
		getView().getSecurityGroupTablePane().getTable().getItems().addAll(getModel().findAllSecurityGroup());
		getView().getDeleteButton().setDisable(true);
		getView().getNewButton().setDisable(false);
		getView().getSaveButton().setDisable(true);
	}


	/**
	 * This method is used to handle Save button click event 
	 * 
	 */
	private void saveOrUpdateUser(ActionEvent e) {
		String days = StringUtils.trimToEmpty(getView().getExpiryDayTextbox().getText()).replaceFirst("^0+(?!$)", StringUtils.EMPTY);
		boolean isUserIdDisabled=getView().getUserIdTextbox().getControl().isDisabled();
		if (!isUserIdDisabled && hasUser(getView().getUserIdTextbox().getText())) {
			displayErrorMessage("User exists");
			return;
		}
		if (getView().getUserIdTextbox().getText()!=null && StringUtils.trimToEmpty(getView().getUserIdTextbox().getControl().getText()).length()<1) {
			displayErrorMessage("Please set the user Id");
			return;
		}
		if (StringUtils.trimToEmpty(getView().getPasswordTextbox().getControl().getText()).equals(StringUtils.EMPTY)) {
			displayErrorMessage("Please set the password");
			return;
		}
		String passwd = StringUtils.trimToEmpty(getView().getPasswordTextbox().getControl().getText());
		if (StringUtils.length(passwd) > 8) {
			displayErrorMessage("Password must be no more than 8 chars");
			return;
		}
		if (StringUtils.trimToEmpty(getView().getConformPasswordTextbox().getControl().getText()).equals(StringUtils.EMPTY)) {
			displayErrorMessage("Confirm password is incorrect");
			return;
		}
		if (!StringUtils.trimToEmpty(getView().getPasswordTextbox().getControl().getText()).equals(StringUtils.trimToEmpty(getView().getConformPasswordTextbox().getControl().getText()))) {
			displayErrorMessage("Confirm password is incorrect");
			return;
		}
		if (!StringUtils.isNumeric(days)) {
			displayErrorMessage("Expire days must be digits");
			return;
		}
		if(Long.parseLong(days) > Integer.MAX_VALUE){
			displayErrorMessage("Exceeds limit :"+Integer.MAX_VALUE);
			return;
		}
		final User user = isUserIdDisabled ?  (User) currentUser.deepCopy(): new User();
		if (user == null)
			return;
		try {
			if (!isUserIdDisabled) {
				createNewUserDetail(days, user);
			} else {
                 updateUserDetail(days, user);
			}	
		}catch (Exception exception) {
			displayErrorMessage("An error occured in saveOrUpdateUser() method, Failed to Save/Update User and assigned security group");
		}
	}

	/**
	 * update or modify existing user detail
	 * 
	 */
	private void updateUserDetail(String days, final User user) {
		boolean expireUpdated=false;
		ReasonForChangeDialog qiUserDailog=new ReasonForChangeDialog(getApplicationId());
		if (MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), "Are you sure you want to update the User details?")) {
			if (qiUserDailog.showReasonForChangeDialog(null)) {
				getLogger().info("update user " + user);
				expireUpdated = setUserValues(days, user,currentUser);
				List<UserSecurityGroup> extingSecurityGroupList= getModel().findAllUserSecurityGroup(user);
				List<UserSecurityGroup> currentSecurityGroupList = getView().getUserSecurityGroupTablePane().getTable().getItems();
				if(extingSecurityGroupList.size()>0){
					for (UserSecurityGroup extingSecurityGroup : extingSecurityGroupList) {
						boolean found=true;
						for (UserSecurityGroup newSecurityGroup : currentSecurityGroupList) {
							if (newSecurityGroup.getId().getSecurityGroup().equals(extingSecurityGroup.getId().getSecurityGroup())) {
								found=false;
							}
						}
						if (found){
							AuditLoggerUtil.logAuditInfo(extingSecurityGroup, null, qiUserDailog.getReasonForChangeText(), getView().getScreenName(),getModel().getUserId());
							getModel().deleteByUserSecurityGroup(extingSecurityGroup);
						}
					}
				} 
				if (currentSecurityGroupList.size() > 0) {
					setUserSecurityGroup(user,currentSecurityGroupList,expireUpdated); 
				}else if (currentSecurityGroupList.size() == 0 && extingSecurityGroupList.size()>0){
					getModel().deleteAllUserSecurityGroup(user);
				}
				user.setRowUpdateTstmp(getCurrentTime());
				user.setUpdateTimestamp(getCurrentTime());
				getModel().updateUser(user);
				AuditLoggerUtil.logAuditInfo(removeWhiteSpace(currentUser), removeWhiteSpace(user), qiUserDailog.getReasonForChangeText(),getView().getScreenName(),getModel().getUserId());
				getView().getUpdatedPasswordTextbox().setText(user.getPasswordUpdateDateString());
				getView().getExpiryDayTextbox().setText(Integer.toString(user.getExpireDays()));
				getView().getActualTimestampTextbox().setText(StringUtils.trimToEmpty(user.getRowUpdateTstmp().toString()));
				getView().getUserTablePane().setData(getModel().findAllUser());
				getView().setUserOperationMessage("User information updated successfully");
				currentUser=user;
				getView().getNewButton().setDisable(false);
				getView().getSaveButton().setDisable(true);
				getView().getDeleteButton().setDisable(false);
				getView().getUserFilterTextfield().clear();
			}
		}
	}

	
	/**
	 *  create new user and set its detail 
	 */
	private void createNewUserDetail(String days, final User user) {
		getLogger().info("created new user " + user);
		setUserValues(days, user,null);
		List<UserSecurityGroup> userSecurityGroup=getView().getUserSecurityGroupTablePane().getTable().getItems();
		if (userSecurityGroup.size() > 0) 
			setUserSecurityGroup(user,userSecurityGroup,false);
		user.setRowInsertTstmp(getCurrentTime());
		user.setCreateTimestamp(getCurrentTime());
		getModel().createUser(user);
		currentUser=user;
		getView().getUserIdTextbox().setEnable(true);
		getView().getUpdatedPasswordTextbox().setText(user.getPasswordUpdateDateString());
		getView().getActualTimestampTextbox().setText(StringUtils.trimToEmpty(user.getRowInsertTstmp().toString()));
		getView().setUserOperationMessage("User information created successfully");
		getView().getUserTablePane().setData(getModel().findAllUser());
		getView().getNewButton().setDisable(false);
		getView().getSaveButton().setDisable(true);
		getView().getDeleteButton().setDisable(false);
		getView().getUserFilterTextfield().clear();
	}


	/**
	 *  Set the security to user while creating a user 
	 */

	private void setUserSecurityGroup(final User user, List<UserSecurityGroup> assignedSecurityGroupList,boolean updateCase) {
		boolean securityGroupExist=true;
		for (UserSecurityGroup item : assignedSecurityGroupList) {
			if (StringUtils.isEmpty(item.getId().getUserId())) {
				item.getId().setUserId(StringUtils.trimToEmpty(user.getUserId()));
				item.setEntryInsertTimestamp(getCurrentTime());
				item.setCreateTimestamp(getCurrentTime());
				item.setEntryDisuseTimestamp(user.getRowDisuseTstmp());
				item.setEntryHisotricCounter(user.getExpireDays());
				securityGroupExist=false;
				getModel().saveUserSecurityGroup(item);
			}
			if(updateCase && securityGroupExist){
				item.setEntryUpdateTimetamp(getCurrentTime());
				item.setEntryDisuseTimestamp(user.getRowDisuseTstmp());
				item.setEntryHisotricCounter(user.getExpireDays());
				getModel().updateUserSecurityGroup(item);
			}
			securityGroupExist=true;
		}
		
	}

	/**
	 * This method is used to set value to user while creating/updating user   
	 * 
	 */
	private boolean setUserValues(final String days, final User modifiedUser,final User existingUser) {
		String lastpassword=StringUtils.EMPTY;
		boolean isExpireDaysUpdated=false;
		modifiedUser.setUserId(StringUtils.trimToEmpty(getView().getUserIdTextbox().getText()));
		modifiedUser.setUserName(StringUtils.trimToEmpty(getView().getUserNameTextbox().getText()));
		if (existingUser!=null)  lastpassword=StringUtils.trimToEmpty(existingUser.getPasswd());
		String currentPassword=StringUtils.trimToEmpty(getView().getPasswordTextbox().getText());
		if (!lastpassword.equals(StringUtils.EMPTY) && !lastpassword.equals(currentPassword)){
			modifiedUser.setPasswd(StringUtils.trimToEmpty(currentPassword));
			modifiedUser.setPasswd1(StringUtils.trimToEmpty(lastpassword));
			modifiedUser.setPasswd2(StringUtils.trimToEmpty(lastpassword));
			modifiedUser.setPasswd3(StringUtils.trimToEmpty(lastpassword));	
			modifiedUser.setPasswd4(StringUtils.trimToEmpty(lastpassword));
			modifiedUser.setPasswd5(StringUtils.trimToEmpty(lastpassword));
			modifiedUser.setPasswordUpdateDate(StringUtils.trimToEmpty(DateFormatUtils.format(new Date(),DATE_FORMAT)));
			modifiedUser.setUpdateTimestamp(getCurrentTime());
		}
		else if(lastpassword.equals(StringUtils.EMPTY))
		{
			modifiedUser.setPasswd(StringUtils.trimToEmpty(currentPassword));
		}	
		if(Integer.parseInt(days)==modifiedUser.getExpireDays()) {
			 isExpireDaysUpdated=false;
		} else {
			modifiedUser.setExpireDays(Integer.parseInt(days)); 
			isExpireDaysUpdated=true;
		}
		return isExpireDaysUpdated;
	}
	
	/**
	 * This method is used to get current time stamp  
	 * 
	 */
	private Timestamp getCurrentTime(){ 
		return CommonUtil.convertTimeStamp(CommonUtil.formatDate(new Date()));
    } 


	/**
	 * This method is used to find existence of user    
	 * 
	 */
	private boolean hasUser(final String text) {
		boolean search = false;
		if (text == null || StringUtils.trimToEmpty(text).equals(StringUtils.EMPTY))
			return false;
		List<User> userList = getModel().findAllUser();
		for (User user : userList) {
			if (StringUtils.trimToEmpty(user.getUserId()).equals(StringUtils.trimToEmpty(text))) {
				search = true;
				break;
			}
		}
		return search;
	}

	/**
	 * This method is used to handle delete button event    
	 * 
	 */
	private void deleteUser(ActionEvent e) {
		try {
			ReasonForChangeDialog qiUserDailog=new ReasonForChangeDialog(getApplicationId());
			if (MessageDialog.confirm(ClientMainFx.getInstance().getStage(getApplicationId()), "Are you sure to delete the User details?")) {
				if (qiUserDailog.showReasonForChangeDialog(null)) {
					User user = getView().getUserIdTextbox().getControl().isDisabled() ? currentUser: null;
					if (user == null)
						return;
					deleteUserConfirmed(user,qiUserDailog.getReasonForChangeText());
					AuditLoggerUtil.logAuditInfo(currentUser, null, qiUserDailog.getReasonForChangeText(),getView().getScreenName(),getModel().getUserId() );
					getView().setUserOperationMessage("User information deleted successfully");
				}
			}
		} catch (Exception exception) {
			displayErrorMessage("An error occured in deleteUser() method, Failed to delete User and assigned security group");
		}
		e.consume();
	}

	/**
	 * This method is used to  delete user    
	 * 
	 */
	private void deleteUserConfirmed(final User user,String reasonForChangeText) {
		List<UserSecurityGroup> userSecurityGroup=new ArrayList<UserSecurityGroup>();
		userSecurityGroup.addAll(getView().getUserSecurityGroupTablePane().getTable().getItems());
		if (userSecurityGroup.size() > 0){
			getModel().deleteAllUserSecurityGroup(userSecurityGroup);
		}
		getModel().deleteUser(user);	
		getView().clearUserDetailsFields();
		getView().getUserFilterTextfield().clear();
		getView().getUserTablePane().getTable().getItems().clear();
		getView().getUserSecurityGroupTablePane().getTable().getItems().clear();
		getView().getSecurityGroupTablePane().getTable().getItems().clear();
		getView().getUserTablePane().setData(getModel().findAllUser());
		getView().getSecurityGroupTablePane().getTable().getItems().addAll(getModel().findAllSecurityGroup());		
		getView().getNewButton().setDisable(false);
		getView().getSaveButton().setDisable(true);
		getView().getDeleteButton().setDisable(true);

	}

	/**
	 * This method is used to handle assignment button event 
	 * 
	 */
	private void userSecurityGroupAssignment(ActionEvent e) {
		final List<SecurityGroup> selectedSecurityGroup = getView().getSecurityGroupTablePane().getSelectedItems();
		if (selectedSecurityGroup != null && selectedSecurityGroup.size() > 0){
			securityGroupLeftShiftAssignment(selectedSecurityGroup);
		}
		e.consume();
	}

	/**
	 * This method is used to assignment of Security group to user  
	 * 
	 */
	private void securityGroupLeftShiftAssignment(final List<SecurityGroup> selectedSecurityGroup) {
		CopyOnWriteArraySet<UserSecurityGroup> selectedUserSecurityGroup = new CopyOnWriteArraySet<UserSecurityGroup>();
		for (SecurityGroup securityGroup : selectedSecurityGroup) {
			selectedUserSecurityGroup.add(copySecurityGroupToUserGroupSecurity(securityGroup));
		}
		getView().getUserSecurityGroupTablePane().getTable().getItems().addAll(selectedUserSecurityGroup);
		getView().getUserSecurityGroupTablePane().clearSelection();
		getView().getSecurityGroupTablePane().getTable().getItems().removeAll(selectedSecurityGroup);
		getView().getSecurityGroupTablePane().clearSelection();
		getView().getNewButton().setDisable(false);
		getView().getSaveButton().setDisable(false);
	}

	/**
	 * This method is used to typecast to SecurityGroup to UserSecurityGroup  
	 * 
	 */
	private UserSecurityGroup copySecurityGroupToUserGroupSecurity(final SecurityGroup securityGroup) {
		final UserSecurityGroup userSecurityGroup = new UserSecurityGroup(StringUtils.EMPTY, securityGroup.getId());
		userSecurityGroup.setSecurityGroup(securityGroup);
		return userSecurityGroup;
	}

	/**
	 * This method is used to handle Unassignment button event 
	 * 
	 */
	private void userSecurityGroupUnassignment(ActionEvent e) {
		final List<UserSecurityGroup> selectedUserSecurityGroup = getView().getUserSecurityGroupTablePane()
				.getSelectedItems();
		if (selectedUserSecurityGroup != null && selectedUserSecurityGroup.size() > 0)
			securityGroupRightShiftAssignment(selectedUserSecurityGroup);
		e.consume();
	}

	/**
	 * This method is used to unassignment of Security group to user  
	 * 
	 */
	private void securityGroupRightShiftAssignment(final List<UserSecurityGroup> selectedUserSecurityGroup) {
		CopyOnWriteArraySet<SecurityGroup> securityGroupList=new CopyOnWriteArraySet<SecurityGroup>();
		securityGroupList.clear();
		securityGroupList.addAll(getView().getSecurityGroupTablePane().getTable().getItems());
		for (UserSecurityGroup userSecurityGroup : selectedUserSecurityGroup) {
			securityGroupList.add(userSecurityGroup.getSecurityGroup());
		}
		getView().getSecurityGroupTablePane().getTable().getItems().clear();
		getView().getSecurityGroupTablePane().getTable().getItems().addAll(securityGroupList);
		getView().getUserSecurityGroupTablePane().getTable().getItems().removeAll(selectedUserSecurityGroup);
		getView().getUserSecurityGroupTablePane().clearSelection();
		getView().getSecurityGroupTablePane().clearSelection();
		securityGroupList.clear();
		getView().getSaveButton().setDisable(false);


	}

	/**
	 * This method is used to handle user click event   
	 * 
	 */
	private void userDetailsListener() {
		getView().getUserTablePane().getTable().getSelectionModel().selectedItemProperty()
		.addListener(new ChangeListener<User>() {
			public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
				setUserDetails();
			}
		});
	}
	
	/**
	 * This method is used to handle unassign security group button  
	 * 
	 */
	private void unassignSecurityGroupListener() {
		getView().getUserSecurityGroupTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserSecurityGroup>() {
			public void changed(ObservableValue<? extends UserSecurityGroup> arg0,UserSecurityGroup arg1, UserSecurityGroup arg2) {
				if (getView().getUserSecurityGroupTablePane().getTable().getSelectionModel().getSelectedItem() != null) {
					clearMessages();
					getView().getUnassignedButton().setDisable(false);
					getView().getAssignedButton().setDisable(true);
					getView().getSecurityGroupTablePane().clearSelection();
				}
				else{
					getView().getUnassignedButton().setDisable(true);
				}
			}
		});
	}
	
	/**
	 * This method is used to handle assign security group button  
	 * 
	 */

	private void assignSecurityGroupListener() {
		getView().getSecurityGroupTablePane().getTable().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SecurityGroup>() {
			public void changed(ObservableValue<? extends SecurityGroup> observable,SecurityGroup oldValue, SecurityGroup newValue) {
				if (getView().getSecurityGroupTablePane().getTable().getSelectionModel().getSelectedItem() != null) {
					clearMessages();
					getView().getUnassignedButton().setDisable(true);
					getView().getAssignedButton().setDisable(false);
					getView().getUserSecurityGroupTablePane().clearSelection();
				}
				else{
					getView().getAssignedButton().setDisable(true);
				}
			}
		});
	}

	/**
	 * This method is used to populate user on user click event   
	 * 
	 */
	private void setUserDetails() {
		CopyOnWriteArraySet<UserSecurityGroup> assignedSecurityGroupList = new CopyOnWriteArraySet<UserSecurityGroup>();
		getView().getUserSecurityGroupTablePane().clearSelection();
		getView().getSecurityGroupTablePane().clearSelection();
		final User selectedItem = getView().getUserTablePane().getSelectedItem();
		if (selectedItem!=null ) {
			getView().getUserSecurityGroupTablePane().getTable().getItems().clear();
			final User selectedUser = removeWhiteSpace(selectedItem);
			currentUser = selectedUser;
			populateUserInformation(selectedUser);
			getView().getExpiryDayTextbox().setText(String.valueOf(selectedUser.getExpireDays()));
			List<UserSecurityGroup> userSecurityGroupList = getModel().findAllUserSecurityGroup(selectedUser);
			assignedSecurityGroupList.addAll(userSecurityGroupList);
			securityGroupAssignment(assignedSecurityGroupList);
			assignedSecurityGroupList.clear();
		}
		getView().getNewButton().setDisable(false);
		getView().getSaveButton().setDisable(true);
		if(isFullAccess()){
			getView().getDeleteButton().setDisable(false);
		}
		else{
			getView().getDeleteButton().setDisable(true);
		}
	}
	
	/**
	 * This method is used to remove whitespace in user variable    
	 * 
	 */
	private User removeWhiteSpace(User user){
		user.setUserId(StringUtils.trimToEmpty(user.getUserId()));
		user.setUserName(StringUtils.trimToEmpty(user.getUserName()));
		user.setPasswd(StringUtils.trimToEmpty(user.getPasswd()));
		user.setPasswordUpdateDate(StringUtils.trimToEmpty(user.getPasswordUpdateDateString()));
		return user;
	}

	/**
	 * This method is used to populate user on textfield    
	 * 
	 */
	private void populateUserInformation(final User adminUser) {
		getView().getUserIdTextbox().setText(StringUtils.trimToEmpty(adminUser.getUserId()));
		getView().getUserIdTextbox().setEnable(true);;
		getView().getUserNameTextbox().setText(StringUtils.trimToEmpty(adminUser.getUserName()));
		getView().getPasswordTextbox().setText(StringUtils.trimToEmpty(adminUser.getPasswd()));
		getView().getConformPasswordTextbox().setText(StringUtils.trimToEmpty(adminUser.getPasswd()));
		getView().getUpdatedPasswordTextbox().setText(StringUtils.trimToEmpty(adminUser.getPasswordUpdateDateString()));
		if(adminUser.getRowUpdateTstmp()!= null && StringUtils.trimToEmpty(adminUser.getRowUpdateTstmp().toString())!=StringUtils.EMPTY)
			getView().getActualTimestampTextbox().setText(StringUtils.trimToEmpty(adminUser.getRowUpdateTstmp().toString()));
		else
			getView().getActualTimestampTextbox().setText(StringUtils.EMPTY);
	}

	/**
	 * This method is used to populate user assigned security group and
	 *  balancing for the security group list    
	 * 
	 */
	private void securityGroupAssignment(final CopyOnWriteArraySet<UserSecurityGroup> userSecurityGroupList) {
		CopyOnWriteArraySet<SecurityGroup> securityGroupList=new CopyOnWriteArraySet<SecurityGroup>();
		if (userSecurityGroupList!=null&&userSecurityGroupList.size() > 0) {
			securityGroupList.clear();
			getView().getUserSecurityGroupTablePane().getTable().getItems().addAll(userSecurityGroupList);
			securityGroupList.addAll(getModel().findAllSecurityGroup());
			for (UserSecurityGroup userSecurityGroup : userSecurityGroupList) {
				outerloop:	for (SecurityGroup securityGroup : securityGroupList) {
					if (StringUtils.trimToEmpty(securityGroup.getSecurityGroup()).equalsIgnoreCase(StringUtils.trimToEmpty(userSecurityGroup.getId().getSecurityGroup()))) {
						securityGroupList.remove(securityGroup);
						break outerloop;
					}
				}
			}
			getView().getSecurityGroupTablePane().getTable().getItems().clear();
			getView().getSecurityGroupTablePane().getTable().getItems().addAll(securityGroupList);
		} else {
			getView().getUserSecurityGroupTablePane().getTable().getItems().clear();
			getView().getSecurityGroupTablePane().setData(getModel().findAllSecurityGroup());
		}
		securityGroupList.clear();
	}



	/**
	 * This method is used to handle user detail changed
	 * 
	 */
	private void addTextFieldChangeListener() {
		final ChangeListener<String> changeListener= new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> value, final String oldValue,
					final String newValue) {
						getView().getSaveButton().setDisable(false);
					clearMessages();
				}
		};
		
		final ChangeListener<String> filterListener= new ChangeListener<String>() {
			public void changed(final ObservableValue<? extends String> value, final String oldValue,
					final String newValue) {
					clearMessages();
				}
		};
		getView().getUserIdTextbox().getControl().textProperty().addListener(changeListener);
		getView().getUserNameTextbox().getControl().textProperty().addListener(changeListener);
		getView().getPasswordTextbox().getControl().textProperty().addListener(changeListener);
		getView().getConformPasswordTextbox().getControl().textProperty().addListener(changeListener);
		getView().getExpiryDayTextbox().getControl().textProperty().addListener(changeListener);
		getView().getUserFilterTextfield().getControl().textProperty().addListener(filterListener);
	}

	/**
	 * This method is used to enable User Filter
	 * 
	 */
	private void addUIScreenComponentListener() {
		getView().getUserFilterTextfield().getControl().setOnAction(getView().getController());
		getView().getAssignedButton().setOnAction(getView().getController());
		getView().getUnassignedButton().setOnAction(getView().getController());
		getView().getNewButton().setOnAction(getView().getController());
		getView().getSaveButton().setOnAction(getView().getController());
		getView().getDeleteButton().setOnAction(getView().getController());
		getView().getExpiryDayTextbox().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(10));
		getView().getUserFilterTextfield().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30)); 
		getView().getUserIdTextbox().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(11));
		getView().getUserNameTextbox().getControl().addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(30));
	}

	/**
	 * This method is used to add listener on User Ui screen, security handled
	 */

	@Override
	public void initEventHandlers() {
		addUIScreenComponentListener();
		userDetailsListener();
		getView().getUnassignedButton().setDisable(true);
		getView().getAssignedButton().setDisable(true);
		if(isFullAccess()){
			addTextFieldChangeListener();
			unassignSecurityGroupListener();
			assignSecurityGroupListener();
		} 
	}

}
