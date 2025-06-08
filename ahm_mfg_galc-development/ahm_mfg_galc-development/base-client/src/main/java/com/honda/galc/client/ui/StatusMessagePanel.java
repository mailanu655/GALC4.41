package com.honda.galc.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.IMessageArea;
import com.honda.galc.common.logging.Logger;
/**
 * 
 * @author Gangadhararao Gadde
 * @date Jan 23, 2014
added method to set the desired font to message label
 */
public class StatusMessagePanel extends JPanel implements IMessageArea{

	private static final long serialVersionUID = 1L;
	
	public static final Color BACKGROUND_ERROR = Color.red;
	public static final Color BACKGROUND_NEUTRAL = Color.lightGray;
	public static final Color BACKGROUND_WARNING = Color.yellow;
	
	private JLabel messageLabel = new JLabel("",JLabel.LEFT);
	
	private StatusPanel statusPanel = null;
	
	public StatusMessagePanel() {
		super();
		
		initComponents();
		
	}
	
	protected void initComponents() {
		this.setLayout(new BorderLayout());
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(initMessageLabel(),BorderLayout.CENTER);
		this.add(createStatusPanel(),BorderLayout.SOUTH);
		
		this.messageLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				displayMessageDialog();
			}

		});
	}
	
	protected void displayMessageDialog() {
		String text = messageLabel.getText();
		
		if(!StringUtils.isEmpty(text))
			MessageDialog.showScrollingInfo(this.getParent(), text);
		
	}
	
	public StatusPanel createStatusPanel() {
		statusPanel = new StatusPanel();
		return statusPanel;
	}

	public StatusPanel getStatusPanel() {
		if(statusPanel == null){
			statusPanel = createStatusPanel();
		}
		return statusPanel;
	}

	private JLabel initMessageLabel() {
		messageLabel.setName("ErrorMessageLabel");
		messageLabel.setOpaque(true);
        messageLabel.setBorder(new BevelBorder(BevelBorder.LOWERED)); 
        messageLabel.setBackground(BACKGROUND_NEUTRAL);
        messageLabel.setForeground(Color.black);
        messageLabel.setFont(new java.awt.Font("dialog", 0, 18));
        messageLabel.setPreferredSize(new Dimension(1024,40));
        return messageLabel;
	}
	
	public void setErrorMessageArea(String errorMessage) {
		Color color = BACKGROUND_NEUTRAL;
		if(!StringUtils.isEmpty(errorMessage)){
			color = BACKGROUND_ERROR;
		}
		messageLabel.setBackground(color);
		messageLabel.setText(errorMessage);
		Logger.getLogger().check("Sent " + errorMessage + ":"+color+" to message area");
	}
	
	public String getMessage() {
		return messageLabel.getText();
	}
	
	public void setMessage(String message) {
		messageLabel.setBackground(BACKGROUND_NEUTRAL);
		messageLabel.setText(message);
		Logger.getLogger().check("Sent " + message + ":"+BACKGROUND_NEUTRAL+" to message area");
	}
	
	public void clearErrorMessageArea() {
		setErrorMessageArea("");
		Logger.getLogger().check("Cleared error message area");
	}
	
	public String getStatusMessage() {
		return this.statusPanel.getStatusMessage();
	}
	
	public void setStatusMessage(String message) {
		this.statusPanel.setStatusMessage(message);
		Logger.getLogger().check("Status message set to: " + message);
	}

	public void setErrorMessageArea(String errorMessage, Color color) {
		
		if(color != null)
			messageLabel.setBackground(color);
		
		messageLabel.setText(errorMessage);
		Logger.getLogger().check("Sent " + errorMessage + ":"+color+" to message area");
		messageLabel.repaint();
	}

	public boolean isWarning() {
		return !StringUtils.isEmpty(messageLabel.getText()) && BACKGROUND_WARNING.equals(messageLabel.getBackground());
	}

	public boolean isError() {
		return !StringUtils.isEmpty(messageLabel.getText()) && BACKGROUND_ERROR.equals(messageLabel.getBackground());
	}

	public void setMessageFont(Font font)
	{
		messageLabel.setFont(font);
	}
	
	public void setWarningMessageArea(String warningMessage) {
		Color color = BACKGROUND_NEUTRAL;
		if(!StringUtils.isEmpty(warningMessage)){
			color = BACKGROUND_WARNING;
		}
		messageLabel.setBackground(color);
		messageLabel.setText(warningMessage);
		Logger.getLogger().check("Sent " + warningMessage + ":"+color+" to message area");
	}
	
}
