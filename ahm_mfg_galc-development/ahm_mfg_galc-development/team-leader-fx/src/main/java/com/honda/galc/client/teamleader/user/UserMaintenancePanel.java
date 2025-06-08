package com.honda.galc.client.teamleader.user;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.mvc.AbstractTabbedView;
import com.honda.galc.client.product.mvc.ViewId;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.ColumnMappingList;
import com.honda.galc.client.ui.component.LabeledPasswordField;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.entity.conf.SecurityGroup;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.conf.UserSecurityGroup;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

/**
 * 
 * <h3>UserMaintenancePanel Class description</h3>
 * <p>
 * UserMaintenancePanel is used to interact on UI screen to perform actions like create,update and delete system user 
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
public class UserMaintenancePanel extends AbstractTabbedView<UserMaintenanceModel, UserMaintenanceController> {

	private ObjectTablePane<User> userTablePane;
	private ObjectTablePane<UserSecurityGroup> userSecurityGroupTablePane;
	private ObjectTablePane<SecurityGroup> securityGroupTablePane;
	protected LoggedButton newButton;
	protected LoggedButton saveButton;
	protected LoggedButton deleteButton;
	protected LoggedButton assignedButton;
	protected LoggedButton unassignedButton;
	protected LabeledTextField userFilterTextfield ;
	protected LabeledTextField userIdTextbox ;
	protected LabeledTextField userNameTextbox ;
	protected LabeledPasswordField passwordTextbox ;
	protected LabeledPasswordField conformPasswordTextbox ;
	protected LabeledTextField updatedPasswordTextbox;
	protected LabeledTextField actualTimestampTextbox ;
	protected LabeledTextField expiryDayTextbox ;


	public UserMaintenancePanel(TabbedMainWindow mainWindow) {
		super(ViewId.DATA_COLLECTION_VIEW, mainWindow);
		
	}

	/**
	 * This method is used to initialize the components of panel
	 */
	public void initView() {
		getMainWindow().getStylesheets().add(QiConstant.CSS_PATH);
		final HBox mainContainer = new HBox();
		mainContainer.getChildren().addAll(createMainContainerMigPanel());
		this.setCenter(createMainContainerMigPanel());
	}

	/**
	 * Get MigPane for User List Table
	 */
	private MigPane createMainContainerMigPanel() {
		MigPane pane = new MigPane("insets 0 0 0 0", "[center,grow]", "");
		pane.add(createUserListContainer(),"dock west");
		pane.add(createUserDetailsContainer(),"dock north");
		pane.add(createSecurityGroupContainer(),"dock center");
		pane.add(createUserOperationContainer(),"dock south");
		pane.setId("UserContainerMigPanel");
		return pane;
	}
	
	/**
	 * This method is used to User List Container including user filter 
	 */
	private VBox createUserListContainer() {
		final VBox userListVBox = new VBox();
		userListVBox.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.7);
		userListVBox.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.18);
		userListVBox.setPadding(new Insets(10, 5, 10, 10));
		userListVBox.getChildren().addAll(createTitledPane("User List", createUserListMigPanel()));
		return userListVBox;
	}

	/**
	 * This method is used to create TitledPane for User List.
	 */
	private TitledPane createTitledPane(String title,Node content) {
		final TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		return titledPane;
	}
	
	/**
	 * This method is used to create TitledPane for User Security Group.
	 */
	private TitledPane createAssignedSecurityGroupTitledPane(String title,Node content) {
		final TitledPane titledPane = new TitledPane();
		titledPane.setText(title);
		titledPane.setContent(content);
		titledPane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.37);
		titledPane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.37);
		titledPane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.15);
		titledPane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.15);
		return titledPane;
	}
	
	/**
	 * Get MigPane for User List Table
	 */
	private MigPane createUserListMigPanel() {
		final MigPane pane = new MigPane("insets 0 0 0 0", "[center, grow]", "");
		userTablePane=getUserListTableView();
		userFilterTextfield = new LabeledTextField("User List Filter",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER,false);
		userFilterTextfield.setPadding(new Insets(5));
		userFilterTextfield.getControl().setMinHeight(25);
		userFilterTextfield.getControl().setPrefHeight(25);
		pane.add(userFilterTextfield,"center, span, wrap");
		pane.add(userTablePane);
		pane.setId("UserListMigPane");
		pane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.7);
		pane.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth()/3.7, Screen.getPrimary().getVisualBounds().getHeight());
		return pane;
	}
	
	/**
	 * This method is used to create table in Box for user list 
	 */
	private ObjectTablePane<User> getUserListTableView(){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("User ID", "userId").put("User Name", "userName");
		final Double[] columnWidth = new Double[] {0.10,0.20};
		final ObjectTablePane<User> panel = new ObjectTablePane<User>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("UserList");
		panel.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.7);
		panel.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.7);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/1.1);
		return panel;
	}

	/**
	 * This method is used to create container user details 
	 */
	private VBox createUserDetailsContainer() {
		final VBox userDetailsContainerVBox = new VBox();
		userDetailsContainerVBox.setSpacing(5);
		userDetailsContainerVBox.setPadding(new Insets(10, 5, 5, 5));
		userDetailsContainerVBox.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/3);
		userDetailsContainerVBox.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		userDetailsContainerVBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		userIdTextbox = new LabeledTextField("User ID",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,true);
		userNameTextbox = new LabeledTextField("User Name",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,false);
		passwordTextbox = new LabeledPasswordField("Password",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,true);
		conformPasswordTextbox =new LabeledPasswordField("Confirm Password",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,true); 
		updatedPasswordTextbox =new LabeledTextField("Password Updated",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,false);
		updatedPasswordTextbox.setEnable(true);
		actualTimestampTextbox = new LabeledTextField("Actual Timestamp",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,false);
		actualTimestampTextbox.setEnable(true);
		expiryDayTextbox = new LabeledTextField("Expire Days",true,TextFieldState.EDIT,new Insets(0),160,Pos.CENTER_LEFT,false);
		userDetailsContainerVBox.getChildren().addAll(userIdTextbox,userNameTextbox,
				passwordTextbox, conformPasswordTextbox, updatedPasswordTextbox,
				actualTimestampTextbox,expiryDayTextbox);
		return userDetailsContainerVBox;
	}

	/**
	 * This method is used to create container for security group
	 */
	private HBox createSecurityGroupContainer() {
		final HBox userSecurityGroupContainerHBox = new HBox();
		userSecurityGroupContainerHBox.getChildren().addAll(createAssignedSecurityGroupContainer(),
				createSecurityGroupButtonContainer(),createSecurityGroupListContainer());
		userSecurityGroupContainerHBox.setPadding(new Insets(0, 15, 0, 5));
		userSecurityGroupContainerHBox.setSpacing(32);
		userSecurityGroupContainerHBox.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		userSecurityGroupContainerHBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		
		return userSecurityGroupContainerHBox;
	}

	/**
	 * This method is used to create container for security group list
	 */
	private VBox createSecurityGroupListContainer() {
		final VBox securityGroupListVBox = new VBox();
		securityGroupListVBox.getChildren().addAll(createTitledPane("Available Security Group(s)", createSecurityGroupMigPanel()));
		securityGroupListVBox.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.5);
		securityGroupListVBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.5);
		return securityGroupListVBox;
	}
	
	/**
	 * Get MigPane for SecurityGroup Table
	 */
	private MigPane createSecurityGroupMigPanel() {
		final MigPane pane = new MigPane("insets 0 0 0 0", "[center,grow,fill]", "");
		securityGroupTablePane=createSecurityGroupList();
		securityGroupTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		pane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.5);
		pane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.5);
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		pane.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		pane.add(securityGroupTablePane);
		pane.setId("SecurityGroupMigPane");
		return pane;
	}
	
	/**
	 * This method is used to create table for security group list
	 */
	private ObjectTablePane<SecurityGroup> createSecurityGroupList(){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("Group ID", "id").put("Group Name", "groupName");
		final Double[] columnWidth = new Double[]{0.15,0.15};
		final ObjectTablePane<SecurityGroup> panel = new ObjectTablePane<SecurityGroup>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		panel.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		panel.setId("SecurityGroup");
		return panel;
	}

	/**
	 * This method is used to create assignment and unassignment button for security group list
	 */
	private VBox createSecurityGroupButtonContainer() {
		final VBox securityGroupButtonVBox = new VBox();
		assignedButton = createButton("<<");
		unassignedButton = createButton(">>");
		securityGroupButtonVBox.setSpacing(20);
		securityGroupButtonVBox.getChildren().addAll(assignedButton, unassignedButton);
		securityGroupButtonVBox.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/14.38);
		securityGroupButtonVBox.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/14.38);
		securityGroupButtonVBox.setAlignment(Pos.CENTER);
		return securityGroupButtonVBox;
	}

	/**
	 * This method is used to create container for Assigned SecurityGroup 
	 */
	private VBox createAssignedSecurityGroupContainer() {
		final VBox securityGroupVBox = new VBox();
		securityGroupVBox.getChildren().addAll(createAssignedSecurityGroupTitledPane("Assigned Security Group(s)", createUserSecurityGroupMigPanel()));
		return securityGroupVBox;
	}
	
	/**
	 * Get MigPane for User SecurityGroup Table
	 */
	private MigPane createUserSecurityGroupMigPanel() {
		final MigPane pane = new MigPane("insets 0 0 0 0","[center,grow]","");
		userSecurityGroupTablePane = createSecurityGroupView();
		userSecurityGroupTablePane.setSelectionMode(SelectionMode.MULTIPLE);
		pane.add(userSecurityGroupTablePane);
		pane.setMinHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		pane.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight()/2.32);
		pane.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.4);
		pane.setMinWidth(Screen.getPrimary().getVisualBounds().getWidth()/3.4);
		pane.setId("UserSecurityGroupMigPane");
		return pane;
	}
	
	/**
	 * This method is used to create table for  SecurityGroup list
	 */
	private ObjectTablePane<UserSecurityGroup> createSecurityGroupView(){
		final ColumnMappingList columnMappingList = ColumnMappingList.with("Group ID", "id.securityGroup").put("Group Name", "groupName");
		final Double[] columnWidth = new Double[] {0.15,0.15};
		final ObjectTablePane<UserSecurityGroup> panel = new ObjectTablePane<UserSecurityGroup>(columnMappingList,columnWidth);
		panel.setConstrainedResize(false);
		panel.setId("UserSecurityGroup");
		return panel;
	}
	
	/**
	 * This method is used to create container for new, save, remove button 
	 */
	private HBox createUserOperationContainer() {
		final HBox userOperation = new HBox();
		userOperation.setPadding(new Insets(5, 15, 5, 0));
		userOperation.setSpacing(10);
		userOperation.setAlignment(Pos.CENTER_RIGHT);
		newButton= createButton("New");
		saveButton = createButton("Save");
		deleteButton = createButton("Delete");
		userOperation.getChildren().addAll(newButton, saveButton, deleteButton);
		userOperation.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		userOperation.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()/1.4);
		return userOperation;
	}

	/**
	 * this method is used to create button
	 */
	private LoggedButton createButton(String buttonName) {
		LoggedButton button = UiFactory.createButton(buttonName, buttonName);
		button.getStyleClass().add("main-screen-btn");
		return button;
	}
	
	/**
	 * This method is used to load data for users and securityGroup in UI 
	 */
	private void loadData() {
		try {
			this.userTablePane.setData(getModel().findAllUser());
			this.securityGroupTablePane.setData(getModel().findAllSecurityGroup());
			this.userSecurityGroupTablePane.getTable().getItems().clear();
			newUser();
		} catch (Exception exception) {
			handleException(exception);
		}
	}

	/**
	 * This method is used to  clear for users and  securityGroup data in UI 
	 */
	public void newUser() {
		this.clearUserDetailsFields();
		this.newButton.setDisable(false);
		this.saveButton.setDisable(true);
		this.deleteButton.setDisable(true);
	}

	public void clearUserDetailsFields() {
		this.userIdTextbox.clear();
		this.userIdTextbox.getControl().setDisable(false);
		this.userNameTextbox.clear();
		this.passwordTextbox.clear();
		this.conformPasswordTextbox.clear();
		this.updatedPasswordTextbox.clear();
		this.expiryDayTextbox.clear();
		this.actualTimestampTextbox.clear();
		this.expiryDayTextbox.setText("0");
		this.userFilterTextfield.getControl().clear();
	}

	/**
	 * This method is used to while selecting tab  
	 */
	public void onTabSelected() {
		getController().clearMessages();
		getController().activate();
		assignedButton.setDisable(true);
		unassignedButton.setDisable(true);
		reload();
	}

	/**
	 * This method is used to return name for the tab in main screen 
	 */
	public String getScreenName() {
		return "User";
	}

	/**
	 * This method is used to  load the users detail
	 */
	@Override
	public void reload() {
		loadData();
	}

	/**
	 * This method is used to  filter the users 
	 */
	public void reload(final String filterString) {
		this.userTablePane.getTable().getItems().clear();
		this.userTablePane.getTable().getItems().addAll(getModel().getUsersByFilter(filterString));
	}

	@Override
	public void start() {
	}

	public LoggedButton getNewButton(){
		return this.newButton;
	}

	public LoggedButton getSaveButton(){
		return this.saveButton;
	}
	
	public LoggedButton getDeleteButton(){
		return this.deleteButton;
	}
	
	public LabeledTextField getUserIdTextbox(){
		return this.userIdTextbox;
	};
	public void setUserIdTextbox(LabeledTextField userIdTextbox){
		 this.userIdTextbox=userIdTextbox;
	};
	public LabeledTextField getUserNameTextbox(){
		return this.userNameTextbox;
	};
	public void setUserNameTextbox(LabeledTextField userNameTextbox){
		 this.userNameTextbox=userNameTextbox;
	};
	public LabeledPasswordField getPasswordTextbox(){
		return this.passwordTextbox;
	};
	public void setPasswordTextbox(LabeledPasswordField passwordTextbox){
		 this.passwordTextbox=passwordTextbox;
	};
	public LabeledPasswordField getConformPasswordTextbox(){
		return this.conformPasswordTextbox;
	};
	public void setConformPasswordTextbox(LabeledPasswordField conformPasswordTextbox){
		 this.conformPasswordTextbox=conformPasswordTextbox;
	};
	public LabeledTextField getUpdatedPasswordTextbox(){
		return this.updatedPasswordTextbox;
	};
	public void setUpdatedPasswordTextbox(LabeledTextField updatedPasswordTextbox){
		 this.updatedPasswordTextbox=updatedPasswordTextbox;
	};
	public LabeledTextField getExpiryDayTextbox(){
		return this.expiryDayTextbox;
	};
	public void setExpiryDayTextbox(LabeledTextField expiryDayTextbox){
		 this.expiryDayTextbox=expiryDayTextbox;
	};
	
	public ObjectTablePane<User> getUserTablePane() {
		return userTablePane;
	}

	public ObjectTablePane<UserSecurityGroup> getUserSecurityGroupTablePane() {
		return userSecurityGroupTablePane;
	}

	public ObjectTablePane<SecurityGroup> getSecurityGroupTablePane() {
		return securityGroupTablePane;
	}

	public LabeledTextField getActualTimestampTextbox() {
		return actualTimestampTextbox;
	}

	/**
	 * @return the assignedButton
	 */
	public LoggedButton getAssignedButton() {
		return assignedButton;
	}

	/**
	 * @return the unassignedButton
	 */
	public LoggedButton getUnassignedButton() {
		return unassignedButton;
	}

	/**
	 * @return the userFilterTextfield
	 */
	public LabeledTextField getUserFilterTextfield() {
		return this.userFilterTextfield;
	}

	/**
	 * this method used to display user operation message
	 */
	public void setUserOperationMessage(final String message){
		EventBusUtil.publish(new StatusMessageEvent(message, StatusMessageEventType.INFO));
	}
}