package com.honda.galc.client.ui;



import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.IMessageArea;
import com.honda.galc.client.utils.UiFactory;
import com.honda.galc.common.logging.Logger;
/**
 * 
 *    
 * @author Gangadhararao Gadde
 * @since Nov 9, 2017
 * Simulation changes
 */
public class StatusMessagePane extends BorderPane implements IMessageArea{

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	private Label messageLabel = UiFactory.createLabel("statusMessageLabel", "");
	
	private StatusPane statusPane;
	private boolean showMessageOnly = false;
	
	public StatusMessagePane() {
		super();
		this.showMessageOnly = false;
		initComponents();
	}

	public StatusMessagePane( boolean showMessageOnly) {
		super();
		this.showMessageOnly = showMessageOnly;
		initComponents();
	}
	
	protected void initComponents() {
		messageLabel.setAlignment(Pos.CENTER_LEFT);
		this.setId("status-pane");
		initMessageLabel();
		messageLabel.setWrapText(true);
		this.setCenter(messageLabel);
		this.setBottom(getStatusPane());
	}

	public void displayMessageDialog() {
		String text = messageLabel.getText();
		if(!StringUtils.isEmpty(text)) {
			MessageDialog.showScrollingInfo (ClientMainFx.getInstance().getStage(), text);
		}
		
	}

	public StatusPane getStatusPane() {
		if(statusPane == null){
			statusPane = new StatusPane(showMessageOnly);
		}
		return statusPane;
	}

	
	private Label initMessageLabel() {
		messageLabel.setId("status-message");
		messageLabel.setMaxWidth(Double.MAX_VALUE);
        return messageLabel;
	}
	
	public void setErrorMessageArea(String errorMessage, String newId) {
		setErrorMessageArea(errorMessage);
		if(!StringUtils.isBlank(newId))  {
			messageLabel.setId(newId);
		}
	}
	
	@Override
	public void setErrorMessageArea(String errorMessage) {
		if(StringUtils.isEmpty(errorMessage)){
			messageLabel.setId("status-message");
			messageLabel.setStyle(null);
		} else {
			messageLabel.setId("status-error-message");
		}
		
		messageLabel.setText(errorMessage);
		if(!StringUtils.isBlank(errorMessage))
		Logger.getLogger().check("Sent " + errorMessage + ":"+ messageLabel.getId() +" to message area");
	}
	
	public void setMessage(String message, Color  color) {
		messageLabel.setId("status-message");
		messageLabel.setText(message);
		if(color != null) {
			messageLabel.setStyle(String.format("-fx-background-color: #%s ",color.toString().replace("0x","")));
		}
		if(!StringUtils.isBlank(message))
		  Logger.getLogger().check("Sent " + message + ":"+ messageLabel.getId() +" to message area");
	}
	
	public void setMessageBig(String message, Color colorBg) {
		setMessageBig(message, colorBg, null);
	}

	public void setMessageBig(String message, Color colorBg, Color colorFg) {
		messageLabel.setId("status-message-big");
		messageLabel.setText(message);
		StringBuilder sb = new StringBuilder("");
		if(colorBg != null) {
			sb.append(String.format("-fx-background-color: #%s; ", colorBg.toString().replace("0x","")));
		}
		if(colorFg != null) {
			sb.append(String.format("-fx-text-fill: #%s; ", colorFg.toString().replace("0x","")));
		}
		sb.append("-fx-font-weight: bold;");
		if(!StringUtils.isBlank(sb.toString()))  {
			messageLabel.setStyle(sb.toString());
		}
		if(!StringUtils.isBlank(message))
		  Logger.getLogger().check("Sent " + message + ":"+ messageLabel.getId() +" to message area");
	}
	
	
	public void setMessage(String message) {
		messageLabel.setId("status-message");
		messageLabel.setText(message);
		this.setStatusMessage(message);
		if(!StringUtils.isBlank(message))
		  Logger.getLogger().check("Sent " + message + ":"+ messageLabel.getId() +" to message area");
	}
	
	public void clearOnlyIfStatus() {
		if(messageLabel != null && !StringUtils.isBlank(messageLabel.getId()) && messageLabel.getId().equalsIgnoreCase("status-message"))  {
			setErrorMessageArea("");
		}
	}
	
	public void clearById(String newId) {
		if(StringUtils.isBlank(newId))  return;
		else if(messageLabel == null || StringUtils.isBlank(messageLabel.getId()))  return;
		else if(messageLabel.getId().equalsIgnoreCase(newId))  {
			setErrorMessageArea("");
		}
	}
	
	public void clearErrorMessageArea() {
		setErrorMessageArea("");
		Logger.getLogger().info("Cleared error message area");
	}
	
	@Override
	public void setStatusMessage(String message) {
		this.statusPane.setStatusMessage(message);
		if(!StringUtils.isBlank(message))
		 Logger.getLogger().check("Status message set to: " + message);
	}

	@Override
	public void setErrorMessageArea(String errorMessage, Color color) {
		if(color != null) {
			messageLabel.setStyle(String.format("-fx-background-color: #%s ",color.toString().replace("0x","")));
		}			
		
		messageLabel.setText(errorMessage);
		if(!StringUtils.isBlank(errorMessage))
		Logger.getLogger().check("Sent " + errorMessage + ":"+color+" to message area");
	}

	@Override
	public boolean isError() {
		return !StringUtils.isEmpty(messageLabel.getText());
	}
}
