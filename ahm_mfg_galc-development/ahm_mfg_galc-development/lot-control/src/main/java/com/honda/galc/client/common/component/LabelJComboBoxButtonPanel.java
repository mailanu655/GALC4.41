package com.honda.galc.client.common.component;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * <h3>LabelJComboBoxButtonPanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> LabelJComboBoxButtonPanel description </p>
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
 * May 6, 2010
 *
 */
public class LabelJComboBoxButtonPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JComboBox comboBox;
	private JButton button;
	private String labelName;
	private String buttonName;
	private ComboBoxModel comboBoxModel;
	
	
	public LabelJComboBoxButtonPanel(String labelName, ComboBoxModel model, String buttonName) {
		super();
		this.labelName = labelName;
		this.comboBoxModel = model;
		this.buttonName = buttonName;
		initialize();
	}

	private void initialize() {
		setLayout(new BorderLayout(5, 10));
		setPreferredSize(new Dimension(700, 25));
		
		createComponents();
		
	}

	public void createComponents() {
		add(getLabel(), BorderLayout.WEST);
		add(getComboBox(), BorderLayout.CENTER);
		
		if(getButton() != null)
			add(getButton(),BorderLayout.EAST);
	}

	public JLabel getLabel() {
		if(label == null){
			label = new JLabel();
			label.setPreferredSize(new Dimension(120, 25));
			label.setText(labelName);
    	}
		
		return label;
	}
	
	public JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
			comboBox.setPreferredSize(new Dimension(200, 25));
			
			if(comboBoxModel != null)
				comboBox.setModel(comboBoxModel);
		}
		return comboBox;
	}
	
	public JButton getButton() {
		if(button == null ){
			button = new JButton();
			button.setPreferredSize(new Dimension(80, 25));
			
			if(buttonName != null)
				button.setText(buttonName);
			else
				button.setVisible(false);
				
		}
		
		return button;
	}
	
}

