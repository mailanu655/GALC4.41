package com.honda.galc.client.qi.homescreen;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ClientConstants;
import com.honda.galc.client.qi.base.QiFxDialog;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.ui.event.StatusMessageEvent;
import com.honda.galc.client.ui.event.StatusMessageEventType;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopup;
import com.honda.galc.client.ui.keypad.control.KeyBoardPopupBuilder;
import com.honda.galc.client.ui.keypad.robot.RobotFactory;
import com.honda.galc.client.utils.CommonUtil;
import com.honda.galc.client.utils.QiCommonUtil;
import com.honda.galc.client.utils.QiConstant;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.mail.MailContext;
import com.honda.galc.mail.MailSender;
import com.honda.galc.property.QiPropertyBean;
import com.honda.galc.service.property.PropertyService;

public class CommentDialog extends QiFxDialog<HomeScreenModel> implements EventHandler<ActionEvent>{

	private LoggedButton sendBtn ;
	private LoggedButton cancelBtn;
	private LoggedTextArea menuDescTxtFld;
	private UpperCaseFieldBean menuNameTxtFld;
	private LoggedTextArea commentTxtArea;
	
	private LoggedLabel toValueLbl ;
	private LoggedLabel commentCount;
	private static final int MAX_COMMENT_COUNT = 240;
	private static final String MAIL_SUBJECT = "Comment from "+
			StringUtils.trim(ApplicationContext.getInstance().getUserId()) + " at " +
			ApplicationContext.getInstance().getTerminalId() + "(" +
			ApplicationContext.getInstance().getProcessPointId() + ")";
	
	private KeyBoardPopup popup;
	
	public CommentDialog(String title, HomeScreenModel model, String applicationId) {
		super(title, applicationId, model);
		this.getScene().getStylesheets().add(QiConstant.CSS_PATH);
		initComponents();
		setModel(model);
		
	}


	private void initComponents() {
		VBox outerPane = new VBox();
		
		HBox fromContainer = new HBox();
		fromContainer.setPadding(new Insets(20,20,0,30));
		
		HBox toContainer = new HBox();
		toContainer.setPadding(new Insets(10,20,0,30));
		
		HBox commentContainer = new HBox();
		commentContainer.setPadding(new Insets(10,0,0,30));
		
		HBox commentBoxContainer = new HBox();
		commentBoxContainer.setPadding(new Insets(10,30,0,30));
		
		
		HBox btnContainer = new HBox();
		HBox saveContainer = new HBox();
		saveContainer.setPadding(new Insets(20,0,0,70));
		
		HBox cancelContainer = new HBox();
		cancelContainer.setPadding(new Insets(20,0,0,50));
		
		
		LoggedLabel fromLbl = UiFactory.createLabel("from", "From : ",Fonts.SS_DIALOG_BOLD(12));
		LoggedLabel userNameLbl = UiFactory.createLabel("from", ApplicationContext.getInstance().getUserId(),Fonts.SS_DIALOG_BOLD(12));
		
		fromContainer.getChildren().addAll(fromLbl,userNameLbl);
		
		LoggedLabel toLbl = UiFactory.createLabel("to", "To : ",Fonts.SS_DIALOG_BOLD(12));
		toValueLbl = UiFactory.createLabel("to",  PropertyService.getPropertyBean(QiPropertyBean.class,ApplicationContext.getInstance().getProcessPointId()).getToEmailGroup(),Fonts.SS_DIALOG_BOLD(12));
		
		
		toContainer.getChildren().addAll(toLbl,toValueLbl);
		
		LoggedLabel commentLbl = UiFactory.createLabel("comment", "Comment : ",Fonts.SS_DIALOG_BOLD(12));
		LoggedLabel noOfWordsLbl = UiFactory.createLabel("commentCount", "Max no of words  ",Fonts.SS_DIALOG_BOLD(12));
		commentCount = UiFactory.createLabel("comment", String.valueOf(MAX_COMMENT_COUNT),Fonts.SS_DIALOG_BOLD(12));
		
		commentContainer.getChildren().addAll(commentLbl,noOfWordsLbl,commentCount);
		
		commentTxtArea = UiFactory.createTextArea("");
		commentTxtArea.setPrefHeight(120);
		commentTxtArea.setPrefWidth(350);
		commentTxtArea.setWrapText(true);
		
		commentTxtArea.textProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if(commentTxtArea.getText().trim().length()> 190){
					commentCount.setTextFill(Color.RED);
					commentTxtArea.addEventFilter(KeyEvent.KEY_TYPED, QiCommonUtil.restrictLengthOfTextFields(240));
				}else{
					commentCount.setTextFill(Color.GREEN);
				}
				commentCount.setText(String.valueOf(MAX_COMMENT_COUNT-newValue.trim().length()));
			}
		});
		
		
		commentBoxContainer.getChildren().addAll(commentTxtArea);
		
		sendBtn = UiFactory.createButton(QiConstant.SEND);
		sendBtn.setPadding(new Insets(0,50,0,0));
		sendBtn.setOnAction(this);
		
		cancelBtn = UiFactory.createButton(QiConstant.CANCEL);
		cancelBtn.setOnAction(this);
		
		saveContainer.getChildren().addAll(sendBtn);
		cancelContainer.getChildren().addAll(cancelBtn);
		btnContainer.getChildren().addAll(saveContainer,cancelContainer);
		
		
		LoggedLabel noteLbl = UiFactory.createLabel("noteLbl", "Note : All comments are recorded & reviewed by your Site QICS Administrators.\nPlease add your contact information.",Fonts.SS_DIALOG_BOLD(12));
		noteLbl.setTextFill(Color.BLUE);
		noteLbl.setPadding(new Insets(50,0,0,0));
		outerPane.getChildren().addAll(fromContainer,toContainer,commentContainer,commentBoxContainer,btnContainer,noteLbl);
		
		outerPane.setPrefSize(500, 350);
		((BorderPane) this.getScene().getRoot()).setCenter(outerPane);
		
		createKeyBoardPopup();
		// add keyboard scene listener to all text components
		this.getScene().focusOwnerProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> value, Node n1, Node n2) {
				if (n2 != null && n2 instanceof LoggedTextArea) {
					CommonUtil.setPopupVisible(true, (LoggedTextArea) n2,popup);

				} else {
					CommonUtil.setPopupVisible(false, null,popup);
				}
			}
		});
		popup.show(this);
	}
	
	/**
	 * This method is used to create Keyboard Popup
	 */
	private void createKeyBoardPopup() {
		this.getScene().getStylesheets().add(this.getClass().getResource(ClientConstants.KEYBOARD_CSS_PATH).toExternalForm());
		String fontUrl = this.getClass().getResource(ClientConstants.KEYBOARD_FONT_URL).toExternalForm();
		Font.loadFont(fontUrl, -1);
		popup = KeyBoardPopupBuilder.create().initLayout("numblock").initScale(1.6).initLocale(Locale.ENGLISH).addIRobot(RobotFactory.createFXRobot()).build();
		popup.getKeyBoard().setOnKeyboardCloseButton(new EventHandler<Event>() {
			public void handle(Event event) {
				CommonUtil.setPopupVisible(false, null,popup);
			}
		});
	}
	
	public LoggedTextArea getMenuDescTxtFld() {
		return menuDescTxtFld;
	}


	public UpperCaseFieldBean getMenuNameTxtFld() {
		return menuNameTxtFld;
	}


	public LoggedButton getCreateBtn() {
		return sendBtn;
	}


	public LoggedButton getUpdateBtn() {
		return cancelBtn;
	}



	public LoggedButton getCancelBtn() {
		return cancelBtn;
	}


	/**
	 * @return the commentTxtArea
	 */
	public LoggedTextArea getCommentTxtArea() {
		return commentTxtArea;
	}


	/**
	 * @param commentTxtArea the commentTxtArea to set
	 */
	public void setCommentTxtArea(LoggedTextArea commentTxtArea) {
		this.commentTxtArea = commentTxtArea;
	}


	/**
	 * @return the commentCount
	 */
	public LoggedLabel getCommentCount() {
		return commentCount;
	}


	/**
	 * @param commentCount the commentCount to set
	 */
	public void setCommentCount(LoggedLabel commentCount) {
		this.commentCount = commentCount;
	}


	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == sendBtn) {
			sendMail();
		}if (event.getSource() == cancelBtn) {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
		}
		CommonUtil.setPopupVisible(false, null,popup);
	}


	private void sendMail() {
		EventBusUtil.publish(new StatusMessageEvent("",StatusMessageEventType.CLEAR));
		boolean isValidEmailsetting = false;
		String comment = commentTxtArea.getText();
		String emailGrp = getToValueLbl().getText();
		String emailServer = PropertyService.getPropertyBean(QiPropertyBean.class,getModel().getProcessPointId()).getSmtpServer();
		String senderEmail = PropertyService.getPropertyBean(QiPropertyBean.class,getModel().getProcessPointId()).getSenderEmail();
		
		if(!StringUtils.isEmpty(comment)){
			isValidEmailsetting = true;
		}else{
			EventBusUtil.publish(new StatusMessageEvent("Please enter Comment", StatusMessageEventType.DIALOG_ERROR));
			return;
		}
		
		if(!StringUtils.isEmpty(emailGrp)){
			isValidEmailsetting = true;
		}else{
			EventBusUtil.publish(new StatusMessageEvent("Please configure to recipient", StatusMessageEventType.DIALOG_ERROR));
			return;
		}
		
		if(!StringUtils.isEmpty(emailServer)){
			isValidEmailsetting = true;
		}else{
			EventBusUtil.publish(new StatusMessageEvent("Please configure email server", StatusMessageEventType.DIALOG_ERROR));
			return;
		}
		
		if(isValidEmailsetting){
				MailContext mailContext = new MailContext();
				mailContext.setSubject(MAIL_SUBJECT);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				mailContext.setMessage("Date: " + format.format(new Date()) + "\nMessage: " + commentTxtArea.getText() + "\n\n*****Please do not reply to this email.*****");
				mailContext.setRecipients(emailGrp);
				mailContext.setHost(emailServer);
				mailContext.setSender(senderEmail);
				boolean isMailSend = MailSender.send(mailContext);
				if(!isMailSend){
					EventBusUtil.publish(new StatusMessageEvent("Email Sending failed", StatusMessageEventType.DIALOG_ERROR));
					return;
				}else{
					Stage stage = (Stage) sendBtn.getScene().getWindow();
					stage.close();
				}
		}
		
		
	}


	/**
	 * @return the toValueLbl
	 */
	public LoggedLabel getToValueLbl() {
		return toValueLbl;
	}


	/**
	 * @param toValueLbl the toValueLbl to set
	 */
	public void setToValueLbl(LoggedLabel toValueLbl) {
		this.toValueLbl = toValueLbl;
	}

	
	
	
	
}
