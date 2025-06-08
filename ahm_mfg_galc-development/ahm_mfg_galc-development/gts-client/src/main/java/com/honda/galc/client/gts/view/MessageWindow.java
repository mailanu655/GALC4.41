package com.honda.galc.client.gts.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.message.Message;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>CarrierInfoWindow</code> is a class to display  Carrier Info
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
 * <TD>Mar 5, 2008</TD>
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

public class MessageWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    public static int CHAR_COUNT = 50;
    
    private JPanel mainPanel = new JPanel();
    private JPanel messagePanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton = new JButton("Close");
    private JButton clearButton = new JButton("Clear");
    private ObjectTablePane<Message> messageTable; 
    
    private List<Message> messages;
    
    public MessageWindow(GtsDrawingView view,List<Message> messages){
        
        super(view.getDrawing().getController().getWindow(),true);
        
        setTitle("Message Window");
        
        this.messages = messages;
  
        ToolTipManager toolTipManager = ToolTipManager.sharedInstance();
        toolTipManager.setInitialDelay(500);
        toolTipManager.setDismissDelay(20000);
        UIManager.put("ToolTip.font",new Font("SansSerif",Font.ITALIC,14)); 
        initComponents();
        addActionListeners();
        pack();
        setSize(800, 400);
        
        this.setLocationRelativeTo(view.getDrawing().getController().getWindow());
        messageTable.reloadData(messages);
    }
    
    public static void showExceptionDialog(Component parentComp,String message){
        
        showExceptionDialog(parentComp,message,CHAR_COUNT);
        
    }    
    
    public static void showExceptionDialog(Component parentComp,String message,int countPerLine){
        
        JOptionPane.showMessageDialog(parentComp, 
                        new JLabel(getHtmlText(message,countPerLine)), 
                        "Exception Occured!", JOptionPane.ERROR_MESSAGE);

    }
    
    public static String getHtmlText(String str){
        
        return getHtmlText(str,CHAR_COUNT);
        
    }   
    
    public static String getHtmlText(String str, int count){
        if(str == null) return "";
        
        StringTokenizer st = new StringTokenizer(str, " ");
        StringBuilder buf = new StringBuilder();
        buf.append("<html>");
        int k = 0;
        for(int i=0; st.hasMoreTokens();i++){
            String s = st.nextToken();
            if(k + s.length() >= count && k > 0) {
                buf.append("<br>");
                k = 0;
            }
            buf.append(s);
            buf.append(" ");
            k += s.length();
        }
        buf.append("</html>");
        return buf.toString();
    }

    private void initComponents(){
        
        this.getContentPane().add(mainPanel);
        
        
        setCommandPanel();
        
        messageTable = createMessageTablePane();
        messagePanel.add(messageTable,BorderLayout.CENTER);
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(messagePanel,BorderLayout.CENTER);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
    }
    
    private ObjectTablePane<Message> createMessageTablePane() {
    
    	ColumnMappings clumnMappings = ColumnMappings.with("Type", "getType")
    	.put("Timestamp","getTimestamp").put("Message", "getMessage");

    	ObjectTablePane<Message> tablePane = new ObjectTablePane<Message>(clumnMappings.get(),false);
    	JTable messageTable = tablePane.getTable();
    	
    	tablePane.setColumnWidths(new int[] {80,160,600});
    	
    	messageTable.setRowHeight(25);
    	Dimension dim = messageTable.getPreferredSize();
    	if(dim.height > 400) dim.height = 400;
    	messageTable.setPreferredScrollableViewportSize(dim);

    	return tablePane;
    	
    }
    
    private void setCommandPanel(){
        
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        commandPanel.add(clearButton);
        commandPanel.add(closeButton);
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.setPreferredSize(new Dimension(80,50));
    }
    
    private void addActionListeners(){
 
        closeButton.addActionListener(this);
        clearButton.addActionListener(this);
        
    }
       
    
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
       }else if(e.getSource() == clearButton) {
           
           if(JOptionPane.showConfirmDialog(mainPanel, "Are you sure that you want to clear all the messages?", "Confirmation", JOptionPane.YES_NO_OPTION)
                           != JOptionPane.YES_OPTION) return;
           
           messages.clear();
           messageTable.updateUI();
       }
    }
     
}
