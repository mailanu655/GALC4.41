package com.honda.galc.client.teamleader.reprint;

import java.awt.Component;

import javax.swing.JPanel;

import com.honda.galc.client.ui.component.LabeledComboBox;

/**
 * 
 * <h3>FormSelectionPanel Class description</h3>
 * <p> FormSelectionPanel description </p>
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
 * Apr 19, 2011
 *
 *
 */
public class FormSelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private String[] forms;
	private LabeledComboBox formBox;
	
	public FormSelectionPanel(String[] forms){
		this.forms = forms;
		
		initComponents();
	}
	
	
	private void initComponents() {
		
		add(createFormComboBox());
		
	}
	
	private Component createFormComboBox() {
		
		formBox = new LabeledComboBox("1. Select form to Print");
		formBox.setModel(forms, 0);
		return formBox;
		
	}
	

}
