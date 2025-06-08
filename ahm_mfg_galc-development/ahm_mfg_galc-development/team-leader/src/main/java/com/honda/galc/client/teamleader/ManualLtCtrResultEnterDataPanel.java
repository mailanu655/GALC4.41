package com.honda.galc.client.teamleader;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import com.honda.galc.client.ui.component.DefaultFieldRender;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LengthFieldBean;
import com.honda.galc.client.ui.component.Text;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.product.MeasurementSpec;

/**
 * 
 * <h3>ManualLtCtrResultEnterDataPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ManualLtCtrResultEnterDataPanel description </p>
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
 * @author Paul Chou
 * Aug 18, 2010
 *
 */
public class ManualLtCtrResultEnterDataPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	private static final int MAX_NUMBER_OF_TORQUES = 20;
	private static final int MAX_NUMBER_OF_STRING_VALUE = 6;
   
    private JTextArea partNameTextArea = null;
    private JLabel labelTorque = null;
    private JLabel labelHLCompleted = null;
    private JLabel partCommentLabel = null;
    
    private List<LengthFieldBean> torqueFieldList;
    private List<LengthFieldBean> stringValueList;
    private List<JTextArea> measurementNameList;

	private UpperCaseFieldBean partSnField = null;
    private UpperCaseFieldBean hlCompletedField = null;
    private JCheckBox checkBoxHLCompleted = null;
   
    private JButton buttonExit = null;
    private JButton buttonSave = null;
    private JButton buttonReset = null;
    private JButton buttonHlCompleted = null;
    private JButton buttonTorqueValue = null;
   
    private JScrollPane commentAreascrollPane;
    private JTextArea partCommentTextArea = null;
    private JTextArea dialogPartCommentTextArea;
    private boolean torqueValueButtonEnabled=true;

    public ManualLtCtrResultEnterDataPanel() {
        super();
        initialize();
    }

   
    public void refresh() {
    	init();
    }
    

    public JTextField getCurrentTorque(int index) {
        
    	if(index < torqueFieldList.size())
        	return torqueFieldList.get(index);
        
        return null;
    }
   

    public JButton getButtonExit() {
    	if (buttonExit == null) {
    		buttonExit = new javax.swing.JButton();
    		buttonExit.setName("JButtonExit");
    		buttonExit.setFont(new java.awt.Font("dialog", 1, 14));
    		buttonExit.setText("Exit");
    		buttonExit.setBounds(847, 450, 135, 38);
    		
    	}
    	return buttonExit;
    }
    
    public JTextArea getPartNameTextArea() {
    	if (partNameTextArea == null) {
    		partNameTextArea = new JTextArea();
    		partNameTextArea.setName("partNameTextArea");
    		partNameTextArea.setText("PartName :");
    		partNameTextArea.setFont(new java.awt.Font("dialog", 0, 18));
    		partNameTextArea.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    		partNameTextArea.setEditable(false);
    		partNameTextArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    	}
    	return partNameTextArea;
    }
    
    private JLabel getJLabelTorque() {
    	if (labelTorque == null) {
    		labelTorque = new javax.swing.JLabel();
    		labelTorque.setName("labelTorque");
    		labelTorque.setText("Torque :");
    		labelTorque.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
    		labelTorque.setForeground(java.awt.Color.black);
    		labelTorque.setFont(new java.awt.Font("dialog", 0, 18));
    		labelTorque.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    		labelTorque.setBounds(10, 163, 94, 45);
    		labelTorque.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    	}
    	return labelTorque;
    }
   
   
    
    private JLabel getPartCommentLabel() {
        if (partCommentLabel == null) {
            partCommentLabel = new JLabel();
            partCommentLabel.setBounds(new Rectangle(43, 286, 94, 45));
            partCommentLabel.setName("partCommentLabel");
            partCommentLabel.setText("Comment :");
            partCommentLabel.setComponentOrientation(java.awt.ComponentOrientation.RIGHT_TO_LEFT);
            partCommentLabel.setForeground(java.awt.Color.black);
            partCommentLabel.setFont(new java.awt.Font("dialog", 0, 18));
            partCommentLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            partCommentLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            partCommentLabel.setVisible(false);

        }    
        return partCommentLabel;
    }
  
    private LengthFieldBean getTorqueLengthField(int index) {
    	LengthFieldBean field = new LengthFieldBean(new DefaultFieldRender());
    	field.setName("TorqueLengthField" + index);
    	field.setFont(new java.awt.Font("dialog", 0, 48));
    	field.setText("");
    	
    	field.setBounds(150 + (index % 5)*140, 163 + (index/5) *55, 135, 45 );
    	field.setMaximumLength(5);
    	field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        return field;
    }
    
    private LengthFieldBean getStringvalueField(int idx) {
    	LengthFieldBean field = new LengthFieldBean(new DefaultFieldRender());
    	field.setName("StringValueField" + idx);
		field.setFont(Fonts.DIALOG_PLAIN_45);
		field.setText("");
		
		field.setBounds(150 + (idx % 3)*260, 270 + (idx/3) *80, 250, 45 );
		field.setMaximumLength(30);
    	field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		
		return field;
	}

    private JTextArea getMeasurementName(int i) {
    	JTextArea textArea = new JTextArea();
		textArea.setName("MNameArea" + i);
		textArea.setText("");
		textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		textArea.setFont(Fonts.DIALOG_PLAIN_20);
		lineUpTorqueArea(textArea, getStringvalueFieldList().get(i));
		textArea.setEditable(false);
		textArea.setEnabled(false);
		textArea.setVisible(false);
		textArea.setDisabledTextColor(Color.BLACK);
		textArea.setOpaque(false);
		return textArea;
	}
    
   
    protected UpperCaseFieldBean getPartSnField() {
    	if (partSnField == null) {
    		partSnField = new UpperCaseFieldBean(new DefaultFieldRender());
    		partSnField.setName("partSnUpperCaseField");
    		partSnField.setFont(new java.awt.Font("dialog", 0, 48));
    		partSnField.setBounds(150, 94, 830, 45);
    		partSnField.setMaximumLength(255);
    	}
    	return partSnField;
    }
   
    protected void handleException(Throwable t) {

    	Logger.getLogger().error(t, "Exception " + this.getClass().getSimpleName());
    }
   

	private void initialize() {
        try {
            setName(this.getClass().getSimpleName());
            setPreferredSize(new Dimension(1024, 608));
            setLayout(null);
            initComponents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }
    
    
    private void initComponents() {
    	//Labels
    	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(getPartNameTextArea());
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(151, 20, 830, 70);
		add(scrollPane, getPartNameTextArea().getName());


        add(getJLabelTorque(), getJLabelTorque().getName());
        add(getLabelHlCompleted(), getLabelHlCompleted().getName());
       
        
        //fields
        torqueFieldList = new ArrayList<LengthFieldBean>();
        for(int i = 0; i < MAX_NUMBER_OF_TORQUES; i++){
        	torqueFieldList.add(getTorqueLengthField(i));
        	add(torqueFieldList.get(i), torqueFieldList.get(i).getName());
        }
        
        //string value fields 
        stringValueList = new ArrayList<LengthFieldBean>();
        measurementNameList = new ArrayList<JTextArea>();
        for(int i = 0; i < MAX_NUMBER_OF_STRING_VALUE; i++){
        	stringValueList.add(getStringvalueField(i));
        	measurementNameList.add(getMeasurementName(i));
        	add(stringValueList.get(i), stringValueList.get(i).getName());
        	add(measurementNameList.get(i), measurementNameList.get(i).getName());
        	
        }
       
        add(getPartSnField(), getPartSnField().getName());
        add(getJCheckBoxHLCompleted(), getJCheckBoxHLCompleted().getName());
       
        
        //buttons
        add(getButtonSave(), getButtonSave().getName());
        add(getButtonReset(), getButtonReset().getName());
        add(getButtonExit(), getButtonExit().getName());
        add(getButtonTorqueValue(), getButtonTorqueValue().getName());
        
        //comment
        add(getJTextFieldHLCompleted(), getJTextFieldHLCompleted().getName());
        add(getButtonHlCompleted(), getButtonHlCompleted().getName());
        add(getCommentAreascrollPane(), getCommentAreascrollPane().getName());
        add(getPartCommentLabel(), getPartCommentLabel().getName());
      
		
	}

	protected void partCommentVisible(boolean enabled) {
        getCommentAreascrollPane().setVisible(enabled);
        getPartCommentTextArea().setVisible(enabled);
        getPartCommentLabel().setVisible(enabled);
        if(!enabled){
        	getPartCommentTextArea().setText("");
        	getPartCommentTextAreaForDialog().setText("");
        }
    }
    
 
    protected void torqueLabelAndButtonControl(boolean visible, int iMeasurementCount) {
        if (iMeasurementCount > 0) {
            getJLabelTorque().setVisible(visible);
            getButtonTorqueValue().setVisible(visible);
        }
        else {
            getJLabelTorque().setVisible(false);
            getButtonTorqueValue().setVisible(false);
        }
        enableDisableTorqueValueButton();
    }
    
    public JCheckBox getJCheckBoxHLCompleted() {
        if (checkBoxHLCompleted == null) {
            checkBoxHLCompleted = new JCheckBox();
            checkBoxHLCompleted.setBounds(new Rectangle(282, 94, 51, 40));
            checkBoxHLCompleted.setVisible(false);
            checkBoxHLCompleted.setPreferredSize(new Dimension(80, 80));		

        }
        return checkBoxHLCompleted;
    }


    private javax.swing.JLabel getLabelHlCompleted() {
    	if (labelHLCompleted == null) {
    		labelHLCompleted = new JLabel();
    		labelHLCompleted.setBounds(new Rectangle(150, 95, 121, 43));
    		labelHLCompleted.setText("JLabel");
    		labelHLCompleted.setName("JLabelPartName");
    		labelHLCompleted.setText("Completed :");
    		labelHLCompleted.setForeground(java.awt.Color.black);
    		labelHLCompleted.setFont(new java.awt.Font("dialog", 0, 18));
    		labelHLCompleted.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    		labelHLCompleted.setVisible(false);			
    	}
    	return labelHLCompleted;
    }
    
    public JButton getButtonSave() {
        if (buttonSave == null) {
            buttonSave = new JButton();
            buttonSave.setBounds(new Rectangle(538, 450, 132, 40));
            buttonSave.setName("JButtonSave");
            buttonSave.setFont(new java.awt.Font("dialog", 1, 14));
            buttonSave.setText("Save");
            buttonSave.setEnabled(false);

        }
        return buttonSave;
    }
    
    
    public JButton getButtonHlCompleted() {
        if (buttonHlCompleted == null) {
            buttonHlCompleted = new JButton();
            buttonHlCompleted.setBounds(new Rectangle(150, 95, 134, 41));
            buttonHlCompleted.setFont(new java.awt.Font("dialog", 0, 14));
            buttonHlCompleted.setText("Completed");
            buttonHlCompleted.setVisible(false);

        }
        return buttonHlCompleted;
    }
   
    public UpperCaseFieldBean getJTextFieldHLCompleted() {
    	if (hlCompletedField == null) {
    		hlCompletedField = new UpperCaseFieldBean(new DefaultFieldRender());
    		hlCompletedField.setBounds(new Rectangle(294, 95, 442, 45));
    		hlCompletedField.setVisible(false);
    		hlCompletedField.setFont(new java.awt.Font("dialog", 0, 48));
    		hlCompletedField.setText("");
    		hlCompletedField.setEditable(false);

    	}
    	return hlCompletedField;
    }

    protected void init()
    {
        getButtonSave().setEnabled(false);
        getButtonReset().setEnabled(false);
        
        setEnabledHeadLess(false, false);
        setVisibleHeadLess(false);
        setEnabledInstalledPart(false);
        setVisibleInstalledPart(false);
        disableStringValueFields();
        disableMeasurementNameFields();
        setEnabledTorqueFields(false, MAX_NUMBER_OF_TORQUES);
        setVisibleTorqueFields(false, MAX_NUMBER_OF_TORQUES);
        enableDisableTorqueValueButton();
       
        
        partCommentVisible(false);
    }

    private void disableMeasurementNameFields() {
		for(JTextArea ta : measurementNameList)
			ta.setVisible(false);
	}


	private void disableStringValueFields() {
		for(LengthFieldBean fd : getStringvalueFieldList())
			fd.setVisible(false);
	}


	public JButton getButtonReset() {
        if (buttonReset == null) {
            buttonReset = new JButton();
            buttonReset.setBounds(new Rectangle(698, 450, 128, 39));
            buttonReset.setText("Reset");
            buttonReset.setEnabled(false);
        }
        return buttonReset;
    }

    public void notifyLoginDialogClosed() {
        this.setCursor(new java.awt.Cursor(Cursor.DEFAULT_CURSOR));	
    }
    
    public JButton getButtonTorqueValue() {
        if (buttonTorqueValue == null) {
            buttonTorqueValue = new JButton();
            buttonTorqueValue.setText("Value");
            buttonTorqueValue.setName("JButtonValue");
            buttonTorqueValue.setBounds(new Rectangle(856, 163, 111, 40));
        }
        return buttonTorqueValue;
    }
   
    private JTextArea getPartCommentTextArea() {
        if (partCommentTextArea == null) {
            partCommentTextArea = new JTextArea();
            partCommentTextArea.setBounds(new Rectangle(152, 278, 810, 81));
            partCommentTextArea.setFont(new java.awt.Font("dialog", Font.BOLD, 20));
            partCommentTextArea.setBackground(Color.yellow);
            partCommentTextArea.setLineWrap(true);
            partCommentTextArea.setWrapStyleWord(true);

            partCommentTextArea.setEditable(false);
            partCommentTextArea.setEnabled(true);
            partCommentTextArea.setVisible(false);
           

        }
        return partCommentTextArea;
    }
    
    public JScrollPane getCommentAreascrollPane() {
        if(commentAreascrollPane == null)
        {
            commentAreascrollPane = new JScrollPane(getPartCommentTextArea()); 
            commentAreascrollPane.setBounds(new Rectangle(150, 278, 814, 81));
            commentAreascrollPane.setVisible(false);

        }
        return commentAreascrollPane;
    }
    
    protected JTextArea getPartCommentTextAreaForDialog() {
        if (dialogPartCommentTextArea == null) {
            dialogPartCommentTextArea = new JTextArea();
            dialogPartCommentTextArea.setBounds(new Rectangle(152, 278, 810, 81));
            dialogPartCommentTextArea.setFont(new java.awt.Font("dialog", Font.BOLD, 20));
            dialogPartCommentTextArea.setBackground(Color.yellow);
            dialogPartCommentTextArea.setLineWrap(true);
            dialogPartCommentTextArea.setWrapStyleWord(true);
            dialogPartCommentTextArea.setVisible(true);
        }
        return dialogPartCommentTextArea;
    }


	public List<LengthFieldBean> getTorqueFieldList() {
		return torqueFieldList;
	}
	
	
	public void setEnabledHeadLess(boolean enabled, boolean isStatusOnly) {
		getPartNameTextArea().setEnabled(enabled);
		getButtonHlCompleted().setEnabled(enabled);
		getJTextFieldHLCompleted().setEnabled(enabled && !isStatusOnly);
		
	}
	
	public void setEnabledInstalledPart(boolean enabled){
	    getPartNameTextArea().setEnabled(enabled);
		getPartSnField().setEnabled(enabled);
	   
	}
	
	public void setEnabledTorqueFields(boolean enabled, int count){
		getJLabelTorque().setEnabled(enabled);
		getButtonTorqueValue().setEnabled(enabled);
		for(int i = 0; i < count; i++){
			getTorqueFieldList().get(i).setEnabled(enabled);
		}
	}
	
	public void setVisibleHeadLess(boolean enabled) {
		getPartNameTextArea().setVisible(true);
		getButtonHlCompleted().setVisible(enabled);
		getJTextFieldHLCompleted().setVisible(enabled);
		
	}
	
	public void setVisibleInstalledPart(boolean enabled){
	    getPartNameTextArea().setVisible(enabled);
		getPartSnField().setVisible(enabled);
	   
	}
	
	public void setVisibleTorqueFields(boolean enabled, int count){
		getJLabelTorque().setVisible(enabled);
		getButtonTorqueValue().setVisible(enabled);
		for(int i = 0; i < count; i++){
			getTorqueFieldList().get(i).setVisible(enabled);
		}
	}



	public void enableHeadLess(boolean statusOnly) {
		setEnabledHeadLess(true, statusOnly);
		setVisibleHeadLess(true);
		getJTextFieldHLCompleted().requestFocus();
		
	}

	
	public void enableHeadLess() {
		enableHeadLess(false);
	}
	
	public void enableInstalledPart() {
		setEnabledInstalledPart(true);
		setVisibleInstalledPart(true);
		getPartSnField().requestFocus();
		
	}
	
	public void enableTorqueFields(int count) {
		setEnablePartNameLabel(true);//always enable part name label
		setEnabledTorqueFields(true, count);
		setVisibleTorqueFields(true, count);
		getTorqueFieldList().get(0).requestFocus();
		enableDisableTorqueValueButton();
		
	}
	
	private void setEnablePartNameLabel(boolean enabled) {
		getPartNameTextArea().setVisible(enabled);
		getPartNameTextArea().setEnabled(enabled);
		
	}

	public void enableOperationButtons(boolean enabled){
	    getButtonSave().setEnabled(enabled);	
	    getButtonReset().setEnabled(enabled);
	    getButtonSave().requestFocus();
	}

	public void resetScreen() {
		Text emptyField = new Text("");
		getJTextFieldHLCompleted().setText(emptyField);
		getPartSnField().setText(emptyField);
		for(LengthFieldBean bean : getTorqueFieldList())
			bean.setText(emptyField);
		for(LengthFieldBean bean : getStringvalueFieldList())
			bean.setText(emptyField);
			
		init();
		
	}


	public void showPartComment(String comment) {
		getPartCommentTextArea().setText(comment);
		partCommentVisible(true);
		getPartCommentTextAreaForDialog().setText(comment);
		JOptionPane.showMessageDialog(this, getPartCommentTextAreaForDialog(), 
				"Important Message:", JOptionPane.OK_OPTION);
	}


	public void enableStringValueFields(List<MeasurementSpec> list) {
		
		setEnablePartNameLabel(true);//always enable part name label
		getJLabelTorque().setEnabled(true);
		
		for(int i = 0; i < list.size(); i++){
			getStringvalueFieldList().get(i).setEnabled(true);
			getStringvalueFieldList().get(i).setVisible(true);
			measurementNameList.get(i).setVisible(true);
			measurementNameList.get(i).setText(list.get(i).getMeasurementName());
		}
		
		getStringvalueFieldList().get(0).requestFocus();
	}

	protected void lineUpTorqueArea(JTextArea textArea, JTextField valueTextField) {
		int gap = 2;
		Rectangle bounds = valueTextField.getBounds();
		textArea.setBounds((int)bounds.getX(), (int)bounds.getY()+(int)bounds.getHeight() + gap, 
				(int)bounds.getWidth(), (int)bounds.getHeight());
	}

	public List<LengthFieldBean> getStringvalueFieldList() {
		return stringValueList;
	}

	public LengthFieldBean getCurrentStringValueField(int index) {
		return stringValueList.get(index);
	}

	public List<JTextArea> getMeasurementNameList() {
		return measurementNameList;
	}
	
	public void enableDisableTorqueValueButton() {
		if (!isTorqueValueButtonEnabled()) {
			getButtonTorqueValue().setEnabled(false);
			getButtonTorqueValue().setVisible(false);
		}
	}
	
	public boolean isTorqueValueButtonEnabled() {
		return torqueValueButtonEnabled;
	}


	public void setTorqueValueButtonEnabled(boolean torqueValueButtonEnabled) {
		this.torqueValueButtonEnabled = torqueValueButtonEnabled;
	}
}
