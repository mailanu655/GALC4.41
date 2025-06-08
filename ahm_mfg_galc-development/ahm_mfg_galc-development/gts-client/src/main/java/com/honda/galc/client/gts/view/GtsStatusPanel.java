package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

import org.jhotdraw.draw.action.ButtonFactory;

import com.honda.galc.common.message.Message;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>GtsStatusPanel</code> displays the current user name, system time etc
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 7, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class GtsStatusPanel extends JPanel implements ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private GtsDrawingView view;
    private JLabel userLabel = new JLabel("",JLabel.CENTER);
    private PolygonButton comStatusButton = new PolygonButton("COM");
    private JLabel dateLabel = new JLabel("",JLabel.CENTER);
    private JLabel timeLabel = new JLabel("",JLabel.CENTER);
    private JButton messageButton = new JButton("MSG");
    
    // timer for updating current time 
    private Timer timer;
    
    private CommStatusWindow comStatusWindow ;
    
    private List<ComStatus> comStatusList;
    
    private List<Message> messages = new ArrayList<Message>();
 
    public GtsStatusPanel(GtsDrawingView view){
        
        this.view = view;
        
        initComponents();
        
        this.addActionListeners();
        
        timer = new Timer(1000,this);
        timer.start();
   }
    
    protected void initComponents(){
        
        this.setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(createZoomButton(),BorderLayout.WEST);
        
        if(GtsDrawing.isEditingMode()){
            leftPanel.add(createGridButton(),BorderLayout.EAST);
        }
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createUserLabel(),BorderLayout.WEST);
        rightPanel.add(createDateLabel(),BorderLayout.CENTER);
        rightPanel.add(createTimeLabel(),BorderLayout.EAST);
        
        this.add(leftPanel,BorderLayout.WEST);
        
        if(GtsDrawing.isProductionMode()){
            JPanel midPanel = new JPanel(new BorderLayout());
            midPanel.add(messageButton,BorderLayout.WEST);
            midPanel.add(createComStatusButton(),BorderLayout.CENTER);
            this.add(midPanel,BorderLayout.CENTER);
        }
        this.add(rightPanel,BorderLayout.EAST);
        setDateTime();
        
    }
    
    protected AbstractButton createZoomButton(){
        AbstractButton pButton = ButtonFactory.createZoomButton(view, new double[] {
            3, 2, 1.5,1.4,1.3,1.2,1.1, 1,0.9,0.8,0.7, 0.6,0.5, 0.4,0.3,0.2, 0.10});
        pButton.putClientProperty("Quaqua.Button.style","placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin",new Insets(0,0,0,0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        return pButton;
    }
    
    protected AbstractButton createGridButton(){
        AbstractButton pButton = ButtonFactory.createToggleGridButton(view);
        pButton.putClientProperty("Quaqua.Button.style","placard");
        pButton.putClientProperty("Quaqua.Component.visualMargin",new Insets(0,0,0,0));
        pButton.setFont(UIManager.getFont("SmallSystemFont"));
        return pButton;
    }
    
    protected JLabel createUserLabel(){
        userLabel.setBorder(new BevelBorder(BevelBorder.LOWERED)); 
        userLabel.setBackground(Color.lightGray);
        userLabel.setForeground(Color.black);
        userLabel.setFont(new java.awt.Font("dialog", 0, 14));
        userLabel.setPreferredSize(new Dimension(80,20));
        return userLabel;
    }
    
    protected JLabel createDateLabel(){
        dateLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        dateLabel.setBackground(Color.lightGray);
        dateLabel.setForeground(Color.black);
        dateLabel.setFont(new java.awt.Font("dialog", 0, 14));
        return dateLabel;
    }
    
    protected JLabel createTimeLabel(){
        timeLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        timeLabel.setBackground(Color.lightGray);
        timeLabel.setForeground(Color.black);
        timeLabel.setFont(new java.awt.Font("dialog", 0, 14));
        return timeLabel;
    }
    
    protected JButton createComStatusButton(){
        
        comStatusButton.setFont(new java.awt.Font("dialog", 0, 11));
        comStatusButton.setPreferredSize(new Dimension(40,20));
        comStatusButton.setComStatus(true);
        
        return comStatusButton;
        
    }
    
    private void addActionListeners(){
        comStatusButton.addActionListener(this);
        messageButton.addActionListener(this);
    }
    
    public void setUserName(String name){
        userLabel.setText(name);
    }
    
    public void addMessage(Message message){
        this.messages.add(message);
    }
    
    public void updateComStatus(boolean allComStatus, List<ComStatus> comStatusList){
        
        comStatusButton.setComStatus(allComStatus);
        
        this.comStatusList = comStatusList ;
        
    }
    
    /**
     *  set the current system date and time for every 1 second
     */
    public void actionPerformed(ActionEvent e){

        if(e.getSource() == timer) setDateTime();
        else if(e.getSource() == comStatusButton) displayComStatusWindow();
        else if(e.getSource() == messageButton) displayMessageWindow();
    }
    
    
    
    private void setDateTime() {
        
        Date date = new Date(System.currentTimeMillis());
        timeLabel.setText(DateFormat.getTimeInstance().format(date));
        Format formatter = new SimpleDateFormat("E MMM dd yyyy");
        dateLabel.setText(formatter.format(date));
        
    }
    
    private void displayComStatusWindow(){
        
        if(comStatusWindow == null || !comStatusWindow.isVisible()) {
            comStatusWindow = new CommStatusWindow(comStatusButton,comStatusList);
            comStatusWindow.setVisible(true);
        }else if(comStatusWindow.isVisible()){
            comStatusWindow.dispose();
            comStatusWindow = null;
        }
    }
    
    private void displayMessageWindow(){
        MessageWindow window = new MessageWindow(view,messages);
        window.setVisible(true);
    }
    
}
