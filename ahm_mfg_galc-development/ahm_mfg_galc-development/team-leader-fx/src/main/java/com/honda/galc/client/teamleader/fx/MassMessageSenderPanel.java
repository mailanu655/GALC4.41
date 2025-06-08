package com.honda.galc.client.teamleader.fx;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.FXOptionPane;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MassMessageDao;
import com.honda.galc.entity.conf.MassMessage;
import com.honda.galc.entity.conf.MassMessageId;
import com.honda.galc.entity.enumtype.MassMessageSeverity;
import com.honda.galc.entity.enumtype.MassMessageType;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.vios.PublishMassMessageService;
import com.honda.galc.util.LDAPService;

/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public abstract class MassMessageSenderPanel extends TabbedPanel{
	
	protected final String NONE = "NONE";
	protected final String PUBLISH_MESSAGE = "Publish Message";
	protected final String RELOAD_MESSAGE = "Reload Message";
	protected final String DELETE_MESSAGE = "Delete Message";
	private TextArea massMessageTxtArea = null;
	private Pane messageAreaPane = null;
	private Pane messageControlPane = null;
	private Pane headerPane = null;
	private MassMessageSeverity messageSeverityType = null;
	public final String WELCOME_MESSAGE = "Please enter your message";
	private RadioButton normalMessage =  null;
	private Label messageCount = null;
	private String plantName = NONE;
	private String divisionId = NONE;
	private String divisionName = NONE;
	private String lineId = NONE;
	private String lineName = NONE;
	public String hostName  = null;
	protected String associateId = "NONE";
	protected Button deleteButton = null;
	protected Button reloadButton = null;
	protected List<RadioButton> severityRadioBtnLst = new ArrayList<RadioButton>();
	Button deleteMessageBtn = null;
	Button reloadMessageBtn = null;
	Button publishMessageBtn = null;
	Label infoLabel = null;
	private MassMessageType massMessageType;
	String userId = null;
	List<String> userGroupLst = null;

	boolean verifiedFromGroup = false;
	HBox plantDivLineControls = null;
	
	public enum UserDataMode{
		CREATE, MODIFY, NONE
	}
	protected UserDataMode DataMode = UserDataMode.NONE;

	public MassMessageSenderPanel(String screenName, int keyEvent) {
		super(screenName, keyEvent);
		initComponents();
	}
	
	public void initComponents() {
		
		VBox messageControlVBox = new VBox(20);
		messageControlVBox.setAlignment(Pos.CENTER);
		headerPane = createHeaderInfo();
		messageAreaPane = createMassMessageArea();
		messageControlPane = createMessageControlPanel();
		setMessageSeverityType(MassMessageSeverity.NORMAL);
		this.setStyle("-fx-background-color: #D8D8D8;-fx-border-color: #000000;-fx-border-width: 20px;");
		messageControlVBox.getChildren().add(headerPane);
		messageControlVBox.getChildren().add(messageAreaPane);
		messageControlVBox.getChildren().add(messageControlPane);
		this.setCenter(messageControlVBox);
		super.setPadding(new Insets(0, 50, 0, 50));
		
	}
	
	private VBox createHeaderInfo() {
		
		VBox headerVBox = new VBox(10);
		headerVBox.setStyle("-fx-border-color: #000000;-fx-border-width: 2px;");
		headerVBox.setMinWidth(750);
		Text screenText = UiFactory.createText(getScreenName());
		screenText.setFont(Font.font ("dialog", 30));
		headerVBox.setAlignment(Pos.CENTER);

		headerVBox.getChildren().add(screenText);
		headerVBox.getChildren().add(getPlantDivLineControls());
		return headerVBox;
	}

	private HBox createMessageControlPanel() {
		
		Pane controlPane = new Pane();
		controlPane.setStyle("-fx-background-color: #071918;-fx-border-color: #000000;-fx-border-width: 2px;");
		HBox messageControlHBox = new HBox(10);
		messageControlHBox.setFillHeight(true);
		messageControlHBox.setAlignment(Pos.CENTER);
		messageControlHBox.setStyle("-fx-background-color: #BDBDBD;-fx-border-color: #000000;-fx-border-width: 2px;");
		messageControlHBox.setPadding(new Insets(20,20,20,20));
		publishMessageBtn = createButtonObj(PUBLISH_MESSAGE);
		reloadMessageBtn = createButtonObj(RELOAD_MESSAGE);
		deleteMessageBtn = createButtonObj(DELETE_MESSAGE);
		Pane severityPane = createSeverityPanel();
		HBox buttonHBox = new HBox(25);
		buttonHBox.setFillHeight(true);
		buttonHBox.setPadding(new Insets(0,50,0,50));
		buttonHBox.setAlignment(Pos.CENTER);
		buttonHBox.setStyle("-fx-background-color: #A4A4A4;-fx-border-color: #000000;-fx-border-width: 2px;");
		buttonHBox.getChildren().add(publishMessageBtn);
		buttonHBox.getChildren().add(deleteMessageBtn);
		buttonHBox.getChildren().add(reloadMessageBtn);
		
		messageControlHBox.getChildren().add(severityPane);
		messageControlHBox.getChildren().add(buttonHBox);
		controlPane.getChildren().add(messageControlHBox);
		return messageControlHBox;
	}

	private VBox createSeverityPanel() {
		Pane severityPane = new Pane();
		severityPane.setPrefSize(220, 170);
		
		final ToggleGroup severityGroup = new ToggleGroup();
		VBox severityVBox = new VBox(25);
		severityVBox.setFillWidth(true);
		severityVBox.setStyle("-fx-background-color: #A4A4A4;-fx-border-color: #000000;-fx-border-width: 2px;");
		normalMessage = UiFactory.createRadioButton("Normal Message");
		normalMessage.setPrefSize(200, 40);
		normalMessage.setFont(Font.font ("dialog", 18));
		normalMessage.setStyle("-fx-background-color: #FFFFFF;");
		normalMessage.setTextFill(Color.BLACK);
		normalMessage.setId(MassMessageSeverity.NORMAL.toString());
		normalMessage.setSelected(true);
		normalMessage.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				setMessageSeverityType(MassMessageSeverity.valueOf(normalMessage.getId()));
				
			}
		});
		normalMessage.setToggleGroup(severityGroup);
		severityRadioBtnLst.add(normalMessage);
		
		final RadioButton warningMessage = UiFactory.createRadioButton("Warning Message");
		warningMessage.setPrefSize(200, 40);
		warningMessage.setTextFill(Color.BLUE);
		warningMessage.setId(MassMessageSeverity.WARNING.toString());
		warningMessage.setFont(Font.font ("dialog", 18));
		warningMessage.setStyle("-fx-background-color: #0066FF;");
		warningMessage.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent arg0) {
				setMessageSeverityType(MassMessageSeverity.valueOf(warningMessage.getId()));
				
			}
		});
		warningMessage.setToggleGroup(severityGroup);
		severityRadioBtnLst.add(warningMessage);
		
		
		final RadioButton importantMessage = UiFactory.createRadioButton("Critical Message");
		importantMessage.setTextFill(Color.RED);
		importantMessage.setPrefSize(200, 40);
		importantMessage.setFont(Font.font ("dialog", 18));
		importantMessage.setStyle("-fx-background-color: #FF3300;");
		importantMessage.setId(MassMessageSeverity.CRITICAL.toString());
		importantMessage.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				setMessageSeverityType(MassMessageSeverity.valueOf(importantMessage.getId()));
				
			}
		});
		importantMessage.setToggleGroup(severityGroup);
		severityRadioBtnLst.add(importantMessage);
		
		severityVBox.getChildren().add(normalMessage);
		severityVBox.getChildren().add(warningMessage);
		severityVBox.getChildren().add(importantMessage);
		severityPane.getChildren().add(severityVBox);
		
		return severityVBox;
	}

	private Button createButtonObj(String title){
		Button buttonObj = UiFactory.createButton(title);
		buttonObj.setFont(Font.font ("dialog", 10));
		buttonObj.setOnAction(this);
		buttonObj.setPrefSize(200, 30);
		buttonObj.setVisible(true);
		return buttonObj;
	}

	public HBox createMassMessageArea() {
		
		Pane messagePane = new Pane();
		messagePane.setStyle("-fx-border-color: #000000;-fx-border-width: 2px;");
		HBox messageAreaHBox = new HBox(50);
		messageAreaHBox.setStyle("-fx-background-color: #BDBDBD;-fx-border-color: #000000;-fx-border-width: 2px;");
		messageAreaHBox.setAlignment(Pos.CENTER);
		messageAreaHBox.setFillHeight(true);
		messageAreaHBox.setPadding(new Insets(10,0,10,25));
		messageAreaHBox.setPrefHeight(200);
		Label messageCountLbl = UiFactory.createLabel("massMessageCountLbl", "Character Count : ");
		messageCountLbl.setFont(Font.font ("dialog", 25));
		messageCountLbl.setTextFill(Color.BLACK);
		messageCountLbl.setAlignment(Pos.CENTER_RIGHT);
		messageCountLbl.setStyle("-fx-background-color: #FFFFFF;");
		messageCount = UiFactory.createLabel("messageCount");
		messageCount.setTextFill(Color.BLACK);
		messageCount.setFont(Font.font ("dialog", 25));
		messageCount.setAlignment(Pos.CENTER);
		messageCount.setStyle("-fx-background-color: #FFFFFF;");
		massMessageTxtArea = UiFactory.createTextArea(WELCOME_MESSAGE);
		massMessageTxtArea.setEditable(true);
		massMessageTxtArea.setVisible(true);
	
		massMessageTxtArea.setPrefRowCount(6);
		massMessageTxtArea.setPrefColumnCount(50);
		massMessageTxtArea.setWrapText(true);
		massMessageTxtArea.setPrefSize(500, 80);
		massMessageTxtArea.setStyle("-fx-border-width: 10px;-fx-font-size: 20;");
		massMessageTxtArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(massMessageTxtArea.getText().trim().length()> 250){
					messageCount.setTextFill(Color.RED);
				}else{
					messageCount.setTextFill(Color.BLACK);
				}
				messageCount.setText(String.valueOf(newValue.trim().length()));
			}
		});

		messageAreaHBox.getChildren().add(massMessageTxtArea);
		messageAreaHBox.getChildren().add(messageCountLbl);
		messageAreaHBox.getChildren().add(messageCount);
		
		return messageAreaHBox;
	}
	
	protected void setStatusOfButtons(){
		
		switch (getUserDataMode()) {
		case CREATE:
			publishMessageBtn.setDisable(false);
			deleteMessageBtn.setDisable(true);
			reloadMessageBtn.setDisable(true);			
			break;
		case MODIFY:
			publishMessageBtn.setDisable(false);
			deleteMessageBtn.setDisable(false);
			reloadMessageBtn.setDisable(false);
			break;
		default:
			publishMessageBtn.setDisable(true);
			deleteMessageBtn.setDisable(true);
			reloadMessageBtn.setDisable(true);
			break;
		}

	}

	@Override
	public  void handle(ActionEvent ae) {
		
		Object obj = ae.getSource();
		if(obj instanceof Button){
			Button buttonObj = (Button)obj;
			
			if(buttonObj.getId().equalsIgnoreCase(PUBLISH_MESSAGE)){
				if(getMassMessageTxtArea().getText().trim().isEmpty() || getMassMessageTxtArea().getText().trim().equalsIgnoreCase(WELCOME_MESSAGE)){
					 FXOptionPane.showMessageDialog(null, "Please enter valid message and publish", "Mass Awareness Message", FXOptionPane.Type.INFORMATION);
					 getMassMessageTxtArea().setText(WELCOME_MESSAGE);
					 getMessageCount().setTextFill(Color.BLACK);
					 getMessageCount().setText(String.valueOf(getMassMessageTxtArea().getText().trim().length()));
				}else if(!getMassMessageTxtArea().getText().trim().isEmpty() && getMassMessageTxtArea().getText().trim().length() > 250){
					FXOptionPane.showMessageDialog(null, "Message is too long, limit your message to 250 characters. Thanks", "Mass Awareness Message", FXOptionPane.Type.INFORMATION);
					getMassMessageTxtArea().setFocusTraversable(true);
				}else{
					 FXOptionPane.Response response = FXOptionPane.showConfirmDialog(null, "Please confirm to publish the message", "Mass Awareness Message", FXOptionPane.Type.CONFIRM);
					 if(response.equals(FXOptionPane.Response.YES)){
						 callToPublish();
						 getMassMessageTxtArea().setText(getCurrentMessage());
						 getMessageCount().setTextFill(Color.BLACK);
						 getMessageCount().setText(String.valueOf(getMassMessageTxtArea().getText().trim().length()));
						 getNormalMessage().setSelected(true);
						 setSeverityButton();
					 }
				}
			}else if(buttonObj.getId().equalsIgnoreCase(DELETE_MESSAGE)){
				FXOptionPane.Response response = FXOptionPane.showConfirmDialog(null, "Please confirm to delete the message", "Mass Awareness Message", FXOptionPane.Type.CONFIRM);
				if(response.equals(FXOptionPane.Response.YES)){
					callToDelete();
					getMassMessageTxtArea().setText(getCurrentMessage());
					resetSeverityButton();
					getMessageCount().setTextFill(Color.BLACK);
					getMessageCount().setText(String.valueOf(getMassMessageTxtArea().getText().trim().length()));
				}
			}else if(buttonObj.getId().equalsIgnoreCase(RELOAD_MESSAGE)){
				getMassMessageTxtArea().setText(getCurrentMessage());
				setSeverityButton();
				getMessageCount().setTextFill(Color.BLACK);
				getMessageCount().setText(String.valueOf(getMassMessageTxtArea().getText().trim().length()));
			}
		}
	}
	
	@Override
	public void onTabSelected() {
		if(!isVerifiedFromGroup()){
			getPlantDivLineControls().getChildren().add(constructControlPanelFromGroupIds());
			setVerifiedFromGroup(true);
		}
		setStatusOfButtons();
		setSeverityButton();
	}
	
	protected void setSeverityButton() {
		Iterator<RadioButton> rdoBtnItr = severityRadioBtnLst.iterator();
		
		while(rdoBtnItr.hasNext()){
			RadioButton severityBtn = (RadioButton)rdoBtnItr.next();
			
			if(getMessageSeverityType().name().equalsIgnoreCase(severityBtn.getId())){
				severityBtn.setSelected(true);
			}else{
				severityBtn.setSelected(false);
			}
		}
	}
	
	protected void resetSeverityButton(){
		Iterator<RadioButton> rdoBtnItr = severityRadioBtnLst.iterator();
		
		while(rdoBtnItr.hasNext()){
			RadioButton severityBtn = (RadioButton)rdoBtnItr.next();
			if(severityBtn.getId().equalsIgnoreCase(MassMessageSeverity.NORMAL.toString())){
				severityBtn.setSelected(true);
				setMessageSeverityType(MassMessageSeverity.valueOf(severityBtn.getId()));
			}else{
				severityBtn.setSelected(false);
			}
		}
	}
	
	protected abstract HBox constructControlPanelFromGroupIds();
	
	protected void callToDelete(){
		deleteMassMessageFromDB();
		DataMode = UserDataMode.CREATE;
		notifyChangesToClient();
		setStatusOfButtons();
	}
	
	protected void callToPublish(){
		insertOrUpdateMessage();
		DataMode = UserDataMode.MODIFY;
		notifyChangesToClient();
		setStatusOfButtons();
	}
	
	public String constructMessage() {
		return getMassMessageTxtArea().getText().trim();
	}
	
	public TextArea getMassMessageTxtArea() {
		return massMessageTxtArea;
	}

	public void setMassMessageTxtArea(TextArea massMessageTxtArea) {
		this.massMessageTxtArea = massMessageTxtArea;
	}

	public Label getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Label messageCount) {
		this.messageCount = messageCount;
	}
	
	public RadioButton getNormalMessage() {
		return normalMessage;
	}

	public void setNormalMessage(RadioButton normalMessage) {
		this.normalMessage = normalMessage;
	}
	
	public void setMessageSeverityType(MassMessageSeverity messageSeverityType) {
		this.messageSeverityType = messageSeverityType;
	}

	public MassMessageSeverity getMessageSeverityType() {
		return messageSeverityType;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getPlantName() {
		return plantName;
	}

	public String getInfoLabelText(){
		return infoLabel.getText();
	}

	protected String getCurrentMessage(){
		MassMessage existingMessage = getMassMessageFromDB();
		
		if (existingMessage == null || (existingMessage != null && existingMessage.getMassMessage().trim().length() == 0)){
			DataMode = UserDataMode.CREATE;
			setMessageSeverityType(MassMessageSeverity.NORMAL);
			setStatusOfButtons();
			return WELCOME_MESSAGE;
		}
		DataMode = UserDataMode.MODIFY;
		setMessageSeverityType(MassMessageSeverity.valueOf(existingMessage.getMassMessageSeverity()));
		setStatusOfButtons();
		return existingMessage.getMassMessage().trim();
	}
	
	protected MassMessage getMassMessageFromDB(){
		MassMessageId massMessageId= new MassMessageId();
		massMessageId.setPlantName(getPlantName());
		massMessageId.setDepartmentId(getDivisionId());
		massMessageId.setLineId(getLineId());
		massMessageId.setMassMessageType((short)getMassMessageType().getId());
		
		return ServiceFactory.getDao(MassMessageDao.class).findByKey(massMessageId);
	}
	
	protected void deleteMassMessageFromDB(){
		MassMessageId massMessageId = new MassMessageId(getPlantName(), getDivisionId(), getLineId(), getMassMessageType().getId());
		MassMessage massMessage = ServiceFactory.getDao(MassMessageDao.class).findByKey(massMessageId);
		ServiceFactory.getDao(MassMessageDao.class).remove(massMessage);
	}
	
	protected void notifyChangesToClient(){
		ServiceFactory.getService(PublishMassMessageService.class).notifyClients(getPlantName(), getDivisionId(), getLineId());
	}
	
	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}


	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionName(String divisionName) {
		this.divisionName = divisionName;
	}


	public String getDivisionName() {
		return divisionName;
	}
	
	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineId() {
		return lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	protected void insertOrUpdateMessage(){
		MassMessageId massMessageId = new MassMessageId( getPlantName(), getDivisionId(), getLineId(), getMassMessageType().getId());
		MassMessage massMessage = new MassMessage(massMessageId, constructMessage() , getMessageSeverityType().name(), getAssociateNo());
		ServiceFactory.getDao(MassMessageDao.class).save(massMessage);
	}
	
	public void setMassMessageType(MassMessageType massMessageType) {
		this.massMessageType = massMessageType;
	}

	public MassMessageType getMassMessageType() {
		return massMessageType;
	}
	
	public List<String> getUserGroupLst(String userId) {
		if(userGroupLst == null)
			setUserGroupLst(getUserGroupsFromLdapForMam(userId));
		
		return userGroupLst;
	}

	public void setUserGroupLst(List<String> userGroupLst) {
		this.userGroupLst = userGroupLst;
	}

	public String getAssociateNo() {
		return userId;
	}

	public void setAssociateNo(String userId) {
		this.userId = userId;
	}

	private List<String> getUserGroupsFromLdapForMam(String userId){
		return LDAPService.getInstance().getMemberList(userId);
	}
	
	protected HBox createInfoLabel(String infoText, boolean forErrorMsg){
		HBox infoLablePane = new HBox();
		infoLablePane.getChildren().add(buildInfoLabel(infoText, forErrorMsg));
		return infoLablePane;
	}
	
	protected Label buildInfoLabel(String text, boolean forErrorMsg){
		Label infoLabel = UiFactory.createLabel("infoLabel");
		infoLabel.setAlignment(Pos.CENTER_LEFT);
		infoLabel.setTextAlignment(TextAlignment.CENTER);
		if(forErrorMsg){
			infoLabel.setFont(Font.font ("dialog", FontWeight.EXTRA_BOLD , FontPosture.ITALIC, 23));
			infoLabel.setStyle("-fx-text-fill: #FF0000");
		}else{
			infoLabel.setFont(Font.font ("dialog", FontPosture.ITALIC, 21));
			infoLabel.setStyle("-fx-text-fill: #071019");
		}
		infoLabel.setText(text);
		return infoLabel;
	}
	
	/**
	 * @param verifiedFromGroup the verifiedFromGroup to set
	 */
	public void setVerifiedFromGroup(boolean verifiedFromGroup) {
		this.verifiedFromGroup = verifiedFromGroup;
	}

	/**
	 * @return the verifiedFromGroup
	 */
	public boolean isVerifiedFromGroup() {
		return verifiedFromGroup;
	}

	public HBox getPlantDivLineControls() {
		if(plantDivLineControls == null)
			plantDivLineControls = new HBox();
		plantDivLineControls.setAlignment(Pos.CENTER);
		return plantDivLineControls;
	}

	public void setPlantDivLineControls(HBox plantDivLineControls) {
		this.plantDivLineControls = plantDivLineControls;
	}
	
	public UserDataMode getUserDataMode() {
		return DataMode;
	}

	public void setUserDataMode(UserDataMode dataMode) {
		DataMode = dataMode;
	}

}
