package com.honda.galc.client.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.dao.conf.MassMessageDao;
import com.honda.galc.entity.conf.MassMessage;
import com.honda.galc.entity.enumtype.MassMessageSeverity;
import com.honda.galc.entity.enumtype.MassMessageType;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;


/** * * 
* @author Fredrick Yessaian 
* @since Sep 03, 2014
*/

public class MassMessagePane extends Pane{

	int messagePointer = 0;
	TranslateTransition transition = null;
	List<MassMessage> massMessageLst;
	boolean gotNotification = false;
	final static String NONE = "NONE";
	String plantName = NONE;
	String divisionId = NONE;
	String LineId = NONE;
	Status messageDisplayingSts = null;
	String msgForPlant = null;
	String msgForDivId = null;
	String msgForLineId = null;
	Queue<MassMessageFor> messageQueue = null;
	
	public MassMessagePane(double width, double height) {
		super();
		messageDisplayingSts = Status.STOPPED;
		Rectangle rectangle = new Rectangle(width - 8, 60);
		this.setClip(rectangle);
		this.setPrefSize(width, height);
		this.setStyle("-fx-border-color: #000000;-fx-border-width: 1px;");
		plantName = new String();
		massMessageLst = new ArrayList<MassMessage>(4);
		messageQueue = new LinkedList<MassMessageFor>();
	}
	
	private int getMsgScrollingDuration() {
		return  PropertyService.getPropertyBean(SystemPropertyBean.class).getMamScrollingDuration();
	}

	protected void checkForLatestMessage(){
		if(messageDisplayingSts.equals(Status.STOPPED)){
			messagePointer = 0;
			checkMsgForPlantDivLine(getMessageQueue().poll());
			startMessageDisplay();
		}
	}

	protected void intialize() {
		getLatestMessageFromDB();
		startMessageDisplay();	
	}
	
	private void startMessageDisplay(){
		
		if(getMassMessageLst() != null && getMassMessageLst().size()>0){
			MassMessage massMessage = getMassMessageLst().get(messagePointer);
			transition = buildTransition(buildText(MassMessageType.getMessageType(massMessage.getId().getMassMessageType()).getMessageWithString() + massMessage.getMassMessage(), massMessage.getMessageSeverity()));
			addChangeListener(transition);
			this.getChildren().add(0, transition.getNode());
			transition.play();
		}
	}

	private void getLatestMessageFromDB(){
		setMassMessageLst(ServiceFactory.getDao(MassMessageDao.class).getLatestMessage(getPlantName(), getDivisionId(), getLineId()));
	}
	
	private void checkMsgForPlantDivLine(MassMessageFor messageFor){
		if(getLineId().equalsIgnoreCase(messageFor.getLineId()) && getDivisionId().equalsIgnoreCase(messageFor.getDivisionId()) && getPlantName().equalsIgnoreCase(messageFor.getPlantName())){
			/* Notification received for Line level Message */
			setMassMessageLst(null);
			getLatestMessageFromDB();
		}else if(messageFor.getLineId().equalsIgnoreCase(NONE) && getDivisionId().equalsIgnoreCase(messageFor.getDivisionId()) && getPlantName().equalsIgnoreCase(messageFor.getPlantName())){
			/* Notification received for Division Level Message*/
			setMassMessageLst(null);
			getLatestMessageFromDB();
		}else if(messageFor.getLineId().equalsIgnoreCase(NONE) && messageFor.getDivisionId().equalsIgnoreCase(NONE) && getPlantName().equalsIgnoreCase(messageFor.getPlantName())){
			/* Notification received for Plant Level Message*/
			setMassMessageLst(null);
			getLatestMessageFromDB();
		}

	}
	
	private TranslateTransition buildTransition(Text text){
		TranslateTransition transition = new TranslateTransition(Duration.seconds(getMsgScrollingDuration()));
	    transition.setNode(text);
	    int lgt = text.getText().trim().length() * 16;
	    transition.setToX(-lgt);
	    transition.setCycleCount(1);
	    transition.setDelay(Duration.seconds(3.0));
	    transition.setInterpolator(Interpolator.EASE_IN);

	    return transition;
	}
	
	private void addChangeListener(TranslateTransition transition){
	    transition.statusProperty().addListener(new ChangeListener<Status>() {

			public void changed(ObservableValue<? extends Status> current,
					Status old, Status next) {
				messageDisplayingSts = current.getValue();
				System.out.println("current : " + current.getValue());
				if(current.getValue().equals(Status.STOPPED)){
					triggerNext();
				}
					
			}
		});
	}
	
	private void triggerNext(){
		messagePointer = messagePointer + 1;

		if(messagePointer >= getMassMessageLst().size()) {
			if(getMessageQueue().size()>0){
				checkMsgForPlantDivLine(getMessageQueue().poll());
			}
			messagePointer = 0;
		}
		displayMessage();

	}
	
	private void displayMessage(){
		if(getMassMessageLst() != null && getMassMessageLst().size() > 0){
			transition = null;
			MassMessage massMessage = getMassMessageLst().get(messagePointer);
			transition = buildTransition(buildText(MassMessageType.getMessageType(massMessage.getId().getMassMessageType()).getMessageWithString() + massMessage.getMassMessage(), massMessage.getMessageSeverity()));
			Node textNode = this.getChildren().get(0);
			if(textNode != null){
				@SuppressWarnings("unused")
				Text text = (Text)this.getChildren().get(0);
				text= null;
			}
			this.getChildren().remove("MassMessageTxt");
			this.getChildren().add(transition.getNode());
			addChangeListener(transition);
			transition.play();
		}else{
			transition = null;
		}	
	}

	private Text buildText(String strValue, MassMessageSeverity severityType){
		Text text = UiFactory.createText(strValue);
		text.setId("MassMessageTxt");
		text.setX(5);
		text.setY(22);
		text.fillProperty().set(getSeverityColor(severityType));
		text.setFont(Font.font("SansSerif", FontWeight.BOLD, 28));
		text.setTextAlignment(TextAlignment.CENTER);
		text.setTextOrigin(VPos.CENTER);

		
		return text;
	}
	
	
	
	public boolean isGotNotification() {
		return gotNotification;
	}

	public void setGotNotification(boolean gotNotification) {
		this.gotNotification = gotNotification;
	}

	public int getMessagePointer() {
		return messagePointer;
	}

	public void setMessagePointer(int messagePointer) {
		this.messagePointer = messagePointer;
	}

	public List<MassMessage> getMassMessageLst() {
		return massMessageLst;
	}

	public void setMassMessageLst(List<MassMessage> massMsgLst) {
		this.massMessageLst = massMsgLst;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public String getLineId() {
		return LineId;
	}

	public void setLineId(String lineId) {
		LineId = lineId;
	}
	
	private Color getSeverityColor(MassMessageSeverity severityType){
		Color colorChosen = null;
		switch(severityType){
		case NORMAL : 
			colorChosen = Color.BLACK;
			break;
		case WARNING : 
			colorChosen= Color.BLUE;
			break;
		case CRITICAL : 
			colorChosen = Color.RED;
			break;
		}
		return colorChosen;
	}

	public String getMsgForPlant() {
		return msgForPlant;
	}

	public void setMsgForPlant(String msgForPlant) {
		this.msgForPlant = msgForPlant;
	}

	public String getMsgForDivId() {
		return msgForDivId;
	}

	public void setMsgForDivId(String msgForDivId) {
		this.msgForDivId = msgForDivId;
	}

	public String getMsgForLineId() {
		return msgForLineId;
	}

	public Queue<MassMessageFor> getMessageQueue() {
		return messageQueue;
	}

	public void setMessageQueue(Queue<MassMessageFor> messageQueue) {
		this.messageQueue = messageQueue;
	}

	public void setMsgForLineId(String msgForLineId) {
		this.msgForLineId = msgForLineId;
	}

}
