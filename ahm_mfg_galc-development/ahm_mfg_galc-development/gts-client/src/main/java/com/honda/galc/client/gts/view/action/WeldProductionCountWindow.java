package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;


/**
 * 
 * 
 * 
 * <h3>WeldProductionCountWindow Class description</h3>
 * <p> WeldProductionCountWindow description </p>
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
 * Apr 4, 2018
 *
 *
 */
public class WeldProductionCountWindow extends JDialog implements  ActionListener{
    
    private static final long serialVersionUID = 1L;
    
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel commandPanel = new JPanel();
    private JButton closeButton =      new JButton("Close");
    
    
    private GtsDrawing drawing;
    
    
    public WeldProductionCountWindow(GtsDrawing drawing){
        
        super(drawing.getController().getWindow(),true);
        
        setTitle("Weld Count Window ");
        
        this.drawing = drawing;
        
        
        initComponent();
        addActionListeners();
        
        pack();

        this.setLocationRelativeTo(drawing.getController().getWindow());
 
    }
    
    private void initComponent(){
        
        this.getContentPane().add(mainPanel);
        
        
        
        LabeledTextField weldOnTextField = createTextField("Weld On",100); 
        LabeledTextField weldOffTextField = createTextField("Weld Off",100);
        
        setCommandPanel();
        
        mainPanel.add(weldOnTextField,BorderLayout.WEST);
        mainPanel.add(weldOffTextField,BorderLayout.EAST);
        mainPanel.add(commandPanel,BorderLayout.SOUTH);
        
        
        weldOnTextField.getComponent().setText("" + getWeldOnCount());
        
        weldOffTextField.getComponent().setText("" + getWeldOffCount());
    }
    
    private Long getWeldOnCount() {
    	String weldOnPPID = drawing.getModel().getPropertyBean().getWeldOnProcessPointId();
    	Long count = drawing.getModel().findCurrentCount(weldOnPPID);
    	return count == null ? 0: count;
    }
    
    private Long getWeldOffCount() {
       	String weldOffPPID = drawing.getModel().getPropertyBean().getWeldOffProcessPointId();
       	Long count = drawing.getModel().findCurrentCount(weldOffPPID);
       	return count == null ? 0: count;
    }
    
    private LabeledTextField createTextField(String label, int count) {
    	 LabeledTextField textField = new LabeledTextField(label,false);
    	 textField.setFont(Fonts.DIALOG_BOLD_26);
    	 textField.getComponent().setText("" + count);
    	 textField.getLabel().setAlignmentX(CENTER_ALIGNMENT);
    	 textField.getComponent().setHorizontalAlignment(JTextField.CENTER);
     	 textField.getComponent().setColumns(4);
     	 textField.getComponent().setEditable(false);
    	 return textField;
    }
    
   
    
    private void setCommandPanel(){
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        commandPanel.add(closeButton);
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.setPreferredSize(new Dimension(80,50));
    }
    
    private void addActionListeners(){
 
        closeButton.addActionListener(this);
        
    }
       
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() == closeButton){
            this.setVisible(false);
            this.dispose();
        }
    }
    
    
     
}
