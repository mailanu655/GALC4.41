package com.honda.galc.client.ui;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class StatusPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	// timer for updating current time 
    private Timer timer;
    
    private JLabel userLabel = new JLabel("",JLabel.CENTER);
    private JLabel messageLabel = new JLabel("",JLabel.LEFT);
    private JLabel dateLabel = new JLabel("",JLabel.CENTER);
    private JLabel timeLabel = new JLabel("",JLabel.CENTER);
    protected JPanel statusContentPanel;
    
    
	public StatusPanel() {
		super();
		
		initComponents();
        
        timer = new Timer(1000,this);
        timer.start();
	}
	
	protected void initComponents(){
        
        this.setLayout(new BorderLayout());
        
        this.add(initUserLabel(),BorderLayout.WEST);
        this.add(initMessageLabel(),BorderLayout.CENTER);
        this.add(getStatusContentPanel(),BorderLayout.EAST);
        
	}

	protected JPanel getStatusContentPanel() {
		if(statusContentPanel == null) {
			statusContentPanel = new JPanel();
			statusContentPanel.setLayout(new BorderLayout());
			statusContentPanel.add(initDateLabel(),BorderLayout.CENTER);
			statusContentPanel.add(initTimeLabel(),BorderLayout.EAST);
		}
		return statusContentPanel;
	}
	
	
	private JLabel initUserLabel(){
		userLabel.setName("UserLabel");
        userLabel.setBorder(new BevelBorder(BevelBorder.LOWERED)); 
        userLabel.setBackground(Color.lightGray);
        userLabel.setForeground(Color.black);
        userLabel.setFont(new java.awt.Font("dialog", 0, 14));
        userLabel.setPreferredSize(new Dimension(100,20));
        return userLabel;
    }
	
	private JLabel initMessageLabel(){
		messageLabel.setName("MessageLabel");
        messageLabel.setBorder(new BevelBorder(BevelBorder.LOWERED)); 
        messageLabel.setBackground(Color.lightGray);
        messageLabel.setForeground(Color.black);
        messageLabel.setFont(new java.awt.Font("dialog", 0, 14));
        return messageLabel;
    }
    
    protected JLabel initDateLabel(){
    	dateLabel.setName("DateLabel");
        dateLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        dateLabel.setBackground(Color.lightGray);
        dateLabel.setForeground(Color.black);
        dateLabel.setFont(new java.awt.Font("dialog", 0, 14));
        return dateLabel;
    }
    
    protected JLabel initTimeLabel(){
    	timeLabel.setName("TimeLabel");
        timeLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        timeLabel.setBackground(Color.lightGray);
        timeLabel.setForeground(Color.black);
        timeLabel.setFont(new java.awt.Font("dialog", 0, 14));
        return timeLabel;
    }
	
	public void actionPerformed(ActionEvent e) {

		if(e.getSource() == timer) setDateTime();
		
	}
	
	private void setDateTime() {
        
        Date date = new Date(System.currentTimeMillis());
        timeLabel.setText(DateFormat.getTimeInstance().format(date));
        Format formatter = new SimpleDateFormat("E MMM dd yyyy");
        dateLabel.setText(formatter.format(date));
        
    }
	
	public String getStatusMessage() {
		return this.messageLabel.getText();
	}
	
	public void setStatusMessage(String message) {
		this.messageLabel.setText(message);
	}
	
	public void setUser(String user){
		this.userLabel.setText(user);
	}
	

}
