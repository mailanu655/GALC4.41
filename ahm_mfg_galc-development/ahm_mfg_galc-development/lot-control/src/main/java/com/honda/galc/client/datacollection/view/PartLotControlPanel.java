package com.honda.galc.client.datacollection.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.plaf.metal.MetalComboBoxEditor;
import javax.swing.text.AbstractDocument;

import com.honda.galc.client.ui.SwingSpringUtil;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.UpperCaseDocumentFilter;
/**
 * 
 * <h3>PartLotControlPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> PartLotControlPanel description </p>
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
 * Dec 15, 2010
 *
 */
public class PartLotControlPanel extends JPanel
{
	public static final Font FONT = new Font("Dialog", Font.BOLD, 26);
	private static final long serialVersionUID = 1L;
	
	LabeledTextField fieldPartName;
	LabeledComboBox fieldSerialNo; 
	LabeledComboBox fieldPartNo;
	LabeledTextField fieldQuantity;
	LabeledTextField fieldRemaining;
	JCheckBox saftyStock;
	Dimension labelDimension = new Dimension(150, 45);
	Dimension fieldDimension = new Dimension(300, 45);
	
	
	public PartLotControlPanel() throws HeadlessException {
		super();
		
		initialize();
	}

	private void initialize() {
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new SpringLayout());
		initComponents();
	}

	public void reset(){
		setComponent(getFieldPartName().getComponent(), null, null);
		((DefaultComboBoxModel)getFieldSerialNo().getComponent().getModel()).removeAllElements();
		initField(getFieldSerialNo().getComponent().getEditor().getEditorComponent());
		
		resetPartNoField();
		resetDataFields();
	}

	public void resetPartNoField() {
		((DefaultComboBoxModel)getFieldPartNo().getComponent().getModel()).removeAllElements();
		initField(getFieldPartNo().getComponent().getEditor().getEditorComponent());
		
	}

	public void resetDataFields() {
		
		setComponent(getFieldQuantity().getComponent(), "", null);
		setComponent(getFieldRemaining().getComponent(), "", null);
		
		getFieldRemaining().setVisible(false);
	}

	public void setComponent(JTextField textField, String text,	Boolean status) {
		
		if(text != null)
			textField.setText(text);
		if(status == null){
			textField.setBackground(Color.blue);
			textField.setForeground(Color.white);
		} else if(status){
			textField.setBackground(Color.green);
			textField.setForeground(Color.black);
		} else {
			textField.setBackground(Color.red);
			textField.setCaretPosition(0);
			textField.moveCaretPosition(textField.getText().length());
		}
		
	}

	private void initComponents() {
		add(getFieldPartName());
		add(getFieldSerialNo());
		add(getFieldPartNo());
		add(getFieldQuantity());
		add(getFieldRemaining());
		add(getSaftyStock());

		SwingSpringUtil.makeCompactGrid(this,
				6, 1,        //rows, cols
				6, 6,        //initX, initY
				6, 6);       //xPad, yPad

	}
	

	public LabeledTextField getFieldPartName() {
		if(fieldPartName == null){
			fieldPartName = new LabeledTextField("Part Name", true);
			fieldPartName.setUpperCaseField(true);
			fieldPartName.getLabel().setPreferredSize(labelDimension);
			fieldPartName.setFont(FONT);
			fieldPartName.getComponent().setPreferredSize(fieldDimension);
			fieldPartName.getComponent().setEditable(false);
		}
		return fieldPartName;
	}

	public LabeledComboBox getFieldPartNo() {
		if(fieldPartNo == null){
			fieldPartNo = new LabeledComboBox("Part No", true);
			fieldPartNo.getLabel().setPreferredSize(labelDimension);
			fieldPartNo.setFont(FONT);
			fieldPartNo.getComponent().setPreferredSize(fieldDimension);
			fieldPartNo.getComponent().setEditable(true);
			
			initComboBox(fieldPartNo.getComponent());
		}
		return fieldPartNo;
	}

	public LabeledTextField getFieldQuantity() {
		if(fieldQuantity == null){
			fieldQuantity = new LabeledTextField("Quantity", true);
			fieldQuantity.setUpperCaseField(true);
			fieldQuantity.getLabel().setPreferredSize(labelDimension);
			fieldQuantity.setFont(FONT);
			fieldQuantity.getComponent().setPreferredSize(fieldDimension);
			fieldQuantity.getComponent().setEditable(true);
		}
		return fieldQuantity;
	}

	public LabeledComboBox getFieldSerialNo() {
		if(fieldSerialNo == null){
			fieldSerialNo = new LabeledComboBox("Serial No", true);
			fieldSerialNo.getLabel().setPreferredSize(labelDimension);
			fieldSerialNo.setFont(FONT);
			fieldSerialNo.getComponent().setPreferredSize(fieldDimension);
			fieldSerialNo.getComponent().setEditable(true);
			
			initComboBox(fieldSerialNo.getComponent());
			
		}
		return fieldSerialNo;
	}
	
	private void initComboBox(JComboBox box) {
		box.setEditor(new MetalComboBoxEditor());
		final Component editorComp = box.getEditor().getEditorComponent();
		
		initField(editorComp);
		
		editorComp.addKeyListener(new KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent e) {
				initField(editorComp);
			}
		});
	}

	public void initField(final Component editorComp) {
		JTextField editorField = ((JTextField)editorComp);
		editorField.setBackground(Color.blue);
		editorField.setForeground(Color.white);
		editorField.setDisabledTextColor(Color.black);
		((AbstractDocument)editorField.getDocument()).setDocumentFilter(new UpperCaseDocumentFilter());
		
	}
	
	public LabeledTextField getFieldRemaining() {
		if(fieldRemaining == null){
			fieldRemaining = new LabeledTextField("Remaining", true);
			fieldRemaining.getLabel().setPreferredSize(labelDimension);
			fieldRemaining.setFont(FONT);
			fieldRemaining.getComponent().setPreferredSize(fieldDimension);
			fieldRemaining.getComponent().setEditable(true);
			fieldRemaining.setVisible(false);
		}
		return fieldRemaining;
	}
	
	public JCheckBox getSaftyStock() {
		if(saftyStock == null){
			saftyStock = new JCheckBox("Safety Stock");
			saftyStock.setFont(FONT);
			
		}
		return saftyStock;
	}
	

}

