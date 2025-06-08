package com.honda.galc.client.gts.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import com.honda.galc.common.message.Message;
import com.honda.galc.common.message.MessageType;

/**
 * 
 * 
 * <h3>MessageBox Class description</h3>
 * <p> MessageBox description </p>
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
 * @author Jeffray Huang<br>
 * Sep 8, 2015
 *
 *
 */
public class MessageBox extends JDialog implements ActionListener,ComponentListener{
    
    private static final long serialVersionUID = 1L;
    
    private static int ColumnSize = 40;
    private static int DefaultTime = 10;   // seconds
    private static Point location = new Point(20,20);
    private static int offset = 0;   // for location shift

    private JButton okButton = new JButton("Ok");
    private static MessageBox instance = new MessageBox();
    private Message message;
    private Timer timer;
    
    
    public MessageBox(){
        
    }
    public MessageBox(Message message){
        this(message,DefaultTime);
    }
    
    public MessageBox(Message message, int seconds){
        this.message = message;
        setTitle("Message - " + message.getType().name() + " - " + message.getTimestamp());
        initComponent();
        addActionListeners();
        pack();
        this.setAlwaysOnTop(true);
		if (offset < 10) {
			offset = offset + 1;
			location.x = location.x + 1;
			location.y = location.y + 1;
		} else {
			offset = 0;
			location.x = location.x - 10;
			location.y = location.y - 10;
		}
        this.setLocation(location);
        this.addComponentListener(this);
        if(message.getType() != MessageType.EMERGENCY){
            timer = new Timer(seconds*1000,this);
            timer.start();
        }
        this.setVisible(true);
    }
    
    public static void showMessage(Message message){
        showMessage(message,DefaultTime); 
    }
    
    public static void showMessage(Message message,int seconds){
        new MessageBox(message, seconds);
    }
    
    private static MessageBox getInstance(){
        return instance;
    }
    
    
    
    private void initComponent(){
        JTextArea textArea = new JTextArea(4,ColumnSize);
        textArea.setFont(new Font("Sans-Serif", Font.PLAIN, 20));
        textArea.setEditable(false);
        textArea.setText(message.getMessage());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.lightGray);
        textArea.setForeground(Color.red);
        textArea.setCaretPosition(0);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(textArea));
        panel.add(okButton);
        this.setModal(false);
        this.getContentPane().add(panel);
   }
    
   public void displayMessage(Message message,int seconds){
       
   }
    
    private void addActionListeners(){
        okButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
       close();
    }
    
    private void close(){
    	if(this.timer != null) this.timer.stop();
        this.timer = null;
        this.setVisible(false);
        this.dispose();
    }

    public void componentHidden(ComponentEvent e) {
    }
    
    public void componentMoved(ComponentEvent e) {
        if(e.getSource() == this){
            location = this.getLocation();
        }
    }
    
    public void componentResized(ComponentEvent e) {
    }
    
    public void componentShown(ComponentEvent e) {
    }
}
