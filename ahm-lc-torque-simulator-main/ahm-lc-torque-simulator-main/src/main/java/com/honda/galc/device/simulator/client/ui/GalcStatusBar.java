
package com.honda.galc.device.simulator.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * <h3>Class description</h3>
 * This class creates a status bar for GalcWindow. The status
 * bar consists of 4 areas, name, message, date and time.
 * <h4>Description</h4>
 * displayMessage(String aMessage) displays aMessage in 
 * the message area. <br><br>
 * displayWarning(String aMessage) displays aMessage with
 * blinking yellow background. <br><br>
 * displayError(String aMessage) displays aMessage with 
 * blinking red background. <br><br>
 * 
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 *
 * <TR>
 * <TD>Guang Yang</TD>
 * <TD>Jun 22, 2007</TD>
 * <TD>1.0</TD>
 * <TD>20070622</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * @see 
 * @ver 1.0
 * @author Guang Yang
 */

public class GalcStatusBar extends JPanel {
	private static final long serialVersionUID = 1L;

	public static final int NORMAL_MESSAGE = 0;
	public static final int WARNING_MESSAGE = 1;
	public static final int ERROR_MESSAGE = 2;
    
    private JLabel nameField;
	private JLabel messageField;
	private JLabel dateField;
	private JLabel timeField;
	private int messageType;	// 0 - normal  1 - warning  2 - error
	private StatusBarClock clockThread;
	private boolean indicator = true;
	
	public GalcStatusBar() {
		super();
		initialize();
	}
	
/**
 * Initialize the status bar.
 *
 */
	private void initialize() {
		setLayout(new BorderLayout());
		initializeFields();
		addFields();
		setBackground(java.awt.Color.lightGray);
		setForeground(java.awt.Color.black);
		initializeClock();
	}

/**
 * Initialize fields.
 *
 */
	private void initializeFields() {
		nameField = new JLabel();
		nameField.setPreferredSize(new Dimension(100,22));
		messageField = new JLabel();
		messageField.setOpaque(true);
		messageField.setPreferredSize(new Dimension(200,22));
		dateField = new JLabel();
		dateField.setPreferredSize(new Dimension(130,22));
		timeField = new JLabel();
		timeField.setPreferredSize(new Dimension(100,22));

		setupField(nameField);
		setupField(dateField);
		setupField(timeField);
		setupField(messageField);
	    messageField.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT); 
	    messageField.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
	}
	
/**
 * Initialize clock thread.
 *
 */
	private void initializeClock() {
		clockThread = new StatusBarClock(this, dateField, timeField);
		clockThread.start();
	}
	
/**
 * Stop clock thread.
 *
 */
	public void stopClock() {
		clockThread.stopRunning();
	}
	
/**
 * Display user name in the name field.
 * @param aName The name string to be displayed.
 */
	public void displayName(String aName) {
		nameField.setText(aName);
	}
	
/**
 * Display aMessage in the message field. The message will be
 * displayed as normal message.
 * @param aMessage The string message to be displayed.
 */
	public void displayMessage(String aMessage) {
		setMessageType(NORMAL_MESSAGE);
		setMessageText(aMessage);
	}

/**
 * Display aMessage in the message field. The message will be 
 * displayed based on the message type aType.
 * @param aMessage The string message to be displayed.
 * @param aType The message type. Available types are
 *           NORMAL_MESSAGE   Display the message as normal message
 *           WARNING_MESSAGE  Display the message as warning message
 *           ERROR_MESSAGE    Display the message as error message
 */
	public void displayMessage(String aMessage, int aType) {
		setMessageType(aType);
		setMessageText(aMessage);
	}
	
/**
 * Display aMessage in the message field. The message will be 
 * displayed as warning message.
 * @param aMessage The string message to be displayed.
 */
	public void displayWarning(String aMessage) {
		setMessageType(WARNING_MESSAGE);
		setMessageText(aMessage);
	}
	
/**
 * Display aMessage in the message field. The message will be
 * displayed as error message. 
 * @param aMessage The string message to be displayed.
 */
	public void displayError(String aMessage) {
		setMessageType(ERROR_MESSAGE);
		setMessageText(aMessage);
	}
	
/**
 * Display aString in the time field. Usually this is the string
 * represents current time.
 * @param aString The string to be displayed.
 */
	public void displayTime(String aString) {
		timeField.setText(aString);
	}
	
/**
 * Change background color of message field based on
 * the type of the message being displayed.
 *
 */
	public void changeMessageBackgroundColor()	{
		java.awt.Color backgroundColor;
		
		if(indicator) {
			switch(messageType) {
				case WARNING_MESSAGE :
					backgroundColor = java.awt.Color.yellow;
					break;
				case ERROR_MESSAGE :
					backgroundColor = java.awt.Color.red;
					break;
				default :
					backgroundColor = java.awt.Color.lightGray;
			}
			indicator = false;
		} else {
			backgroundColor = java.awt.Color.lightGray;
			indicator = true;
		}

		messageField.setBackground(backgroundColor);
	}

/**
 * Return message type of the message being displayed.
 * @return
 */
	public int getMessageType() {
		return messageType;
	}

/**
 * Add fields to the status bar.
 *
 */
	private void addFields() {
		JPanel aPanel = new JPanel();
		aPanel.setLayout(new GridBagLayout());
		aPanel.add(dateField);
		aPanel.add(timeField);
	    aPanel.setBackground(java.awt.Color.lightGray);
	    aPanel.setForeground(java.awt.Color.black);
		add(nameField, BorderLayout.WEST);
		add(messageField, BorderLayout.CENTER);
		add(aPanel, BorderLayout.EAST);
	}
	
/**
 * Setup common properties for a field.
 * @param aField The field to be setup.
 */
	private void setupField(JLabel aField) {
	    aField.setBorder(new BevelBorder(BevelBorder.LOWERED)); 
	    aField.setText("");
	    aField.setBackground(java.awt.Color.lightGray);
	    aField.setForeground(java.awt.Color.black);
	    aField.setFont(new java.awt.Font("dialog", 0, 14));
	    aField.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER); 
	    aField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}

/**
 * Stop clock thread and get ready to exit.
 * @return 0 indicating it is ok to exit.
 */
	public int exitApplication() {
		stopClock();
		return 0;
	}
	
/**
 * Setup message type to anInteger.
 * @param anInteger The message type to be set to.
 */
	private void setMessageType(int anInteger) {
		messageType = anInteger;
	}

/**
 * Set the text of message field to aMessage.
 * @param aMessage The string message to be displayed.
 */
	private void setMessageText(String aMessage) {
		messageField.setText(aMessage);
	}

	public JLabel getDateField() {
		return dateField;
	}

	public void setDateField(JLabel dateField) {
		this.dateField = dateField;
	}

	public JLabel getMessageField() {
		return messageField;
	}

	public void setMessageField(JLabel messageField) {
		this.messageField = messageField;
	}

	public JLabel getNameField() {
		return nameField;
	}

	public void setNameField(JLabel nameField) {
		this.nameField = nameField;
	}

	public JLabel getTimeField() {
		return timeField;
	}

	public void setTimeField(JLabel timeField) {
		this.timeField = timeField;
	}
}

