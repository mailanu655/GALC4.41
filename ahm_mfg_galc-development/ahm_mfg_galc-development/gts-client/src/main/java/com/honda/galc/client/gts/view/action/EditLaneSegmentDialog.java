package com.honda.galc.client.gts.view.action;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.gts.figure.LaneSegmentFigure;
import com.honda.galc.client.gts.view.GtsDrawing;
import com.honda.galc.client.gts.view.MessageWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.entity.gts.GtsLaneSegment;

/**
 * 
 * 
 * <h3>EditLaneSegmentDialog Class description</h3>
 * <p> EditLaneSegmentDialog description </p>
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
 * Jun 15, 2015
 *
 *
 */
public class EditLaneSegmentDialog extends JDialog implements  ActionListener {

    private static final long serialVersionUID = 1L;

    private LaneSegmentFigure figure;
    
    private JPanel mainPanel = new JPanel();
    private JPanel namePanel = new JPanel();
    private JPanel capacityPanel = new JPanel();
    private JPanel poweredPanel = new JPanel();
    private JPanel sourceDestPanel = new JPanel();
    private JPanel commandPanel = new JPanel();
    
    private JTextField nameField = new JTextField();
    private JFormattedTextField capacityField = new JFormattedTextField(NumberFormat.getIntegerInstance());
    private JCheckBox poweredCheckBox = new JCheckBox("Is Powered");
    private JCheckBox carrierVisibleCheckBox = new JCheckBox("Is Carrier Visible");
    private JCheckBox sourceCheckBox = new JCheckBox("Is Source Lane");
    private JCheckBox destCheckBox = new JCheckBox("Is Destination Lane");
    private JButton applyButton = new JButton("Apply");
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("    Cancel  ");

    
    public EditLaneSegmentDialog(LaneSegmentFigure figure){
        
        super(figure.getDrawing().getController().getWindow(),"Edit Lane Segment Window",true);
        this.figure = figure;
        initComponent();
        addActionListeners();
        pack();
        this.setLocationRelativeTo(figure.getDrawing().getController().getWindow());
    }
    
    private void initComponent(){
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        setNamePanel();
        setCapacityPanel();
        setPoweredPanel();
        setSourceDestPanel();
        setCommandPanel();
        
        mainPanel.setLayout(new GridLayout(5,1));
        
        mainPanel.add(namePanel);
        mainPanel.add(capacityPanel);
        mainPanel.add(poweredPanel);
        mainPanel.add(sourceDestPanel);
        mainPanel.add(commandPanel);
        
        contentPane.add(mainPanel);
        
        
        
    }
    
    private void setNamePanel(){
        namePanel.setBorder(new TitledBorder("Name"));
        nameField.setPreferredSize(new Dimension(160,20));
        nameField.setText(figure.getLaneSegment().getLaneSegmentName());
        namePanel.add(nameField);
    }
    
    private void setCapacityPanel(){
        capacityPanel.setBorder(new TitledBorder("Capacity"));
        capacityField.setPreferredSize(new Dimension(160,20));
        capacityField.setValue((long)figure.getLaneSegment().getCapacity());
        capacityPanel.add(capacityField);
    }
    
    private void setPoweredPanel(){
        poweredPanel.setBorder(new TitledBorder("Powered/Carrier Visible Setting"));
        poweredCheckBox.setPreferredSize(new Dimension(160,20));
        poweredCheckBox.setSelected(figure.getLaneSegment().isPowered());
        carrierVisibleCheckBox.setPreferredSize(new Dimension(160,20));
        carrierVisibleCheckBox.setSelected(figure.getLaneSegment().isCarrierVisible());
        poweredPanel.add(poweredCheckBox);
        poweredPanel.add(carrierVisibleCheckBox);
    }
    
    private void setSourceDestPanel(){
        sourceDestPanel.setBorder(new TitledBorder("Source/Destination Setting"));
        sourceCheckBox.setPreferredSize(new Dimension(160,20));
        sourceCheckBox.setSelected(figure.getLaneSegment().isSource());
        destCheckBox.setPreferredSize(new Dimension(160,20));
        destCheckBox.setSelected(figure.getLaneSegment().isDestination());
        sourceDestPanel.add(sourceCheckBox);
        sourceDestPanel.add(destCheckBox);
    }
    
    private void setCommandPanel(){
        commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.X_AXIS));
        commandPanel.setPreferredSize(new Dimension(300,60));
        commandPanel.add(Box.createHorizontalGlue());
        commandPanel.add(applyButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(okButton);
        commandPanel.add(Box.createHorizontalStrut(10));
        commandPanel.add(cancelButton);
        commandPanel.add(Box.createHorizontalStrut(60));
    }
    
    private void addActionListeners(){
        applyButton.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == applyButton){
            applyChange();
        }else if(e.getSource() == okButton){
            done();
        }else if(e.getSource() == cancelButton){
            close();
        }
        
    }

    private void applyChange(){
        boolean changed = false;
        
        GtsLaneSegment segment = figure.getLaneSegment();
        
        if(!StringUtils.equals(segment.getLaneSegmentName(),nameField.getText())){
        	String laneSegmentName = StringUtils.trimToEmpty(nameField.getText());
        	if(getDrawing().getModel().getLaneSegmentByName(laneSegmentName) == null) {
        		segment.setLaneSegmentName(nameField.getText());
        		changed = true;
        	}else {
        		MessageDialog.showError(this,"Lane Segment Name is duplicate","");
        		return;
        	}
        };
        
        int capacity = ((Long)capacityField.getValue()).intValue();
        if(segment.getCapacity() != capacity){
            segment.setCapacity(capacity);
            changed = true;
        }
        
        if(segment.isPowered() != poweredCheckBox.isSelected()){
            segment.setPowered(poweredCheckBox.isSelected());
            changed = true;
        }
        
        if(segment.isCarrierVisible() != carrierVisibleCheckBox.isSelected()){
            segment.setCarrierVisible(carrierVisibleCheckBox.isSelected());
            changed = true;
        }
        
        
        if(segment.isSource() != sourceCheckBox.isSelected()){
            segment.setSource(sourceCheckBox.isSelected());
            changed = true;
        }
        
        if(figure.getLaneSegment().isDestination() != destCheckBox.isSelected()){
            figure.getLaneSegment().setDestination(destCheckBox.isSelected());
            changed = true;
        }
        
        //      update database and figures
        if(changed){
            getDrawing().getModel().updateLaneSegment(segment);
            getDrawing().refreshLayout();
            getDrawing().initCarriers();
        }
    }
    
    
    
    private void done(){
        applyChange();
        close();
    }
    
    private void close(){
        this.setVisible(false);
        this.dispose();
    }
    
    
    private GtsDrawing getDrawing(){
        return ((LaneSegmentFigure)figure).getDrawing();
   }
    
    public void handleException(Exception e) {
        
        MessageWindow.showExceptionDialog(this, e.getMessage());

    }

  
}
