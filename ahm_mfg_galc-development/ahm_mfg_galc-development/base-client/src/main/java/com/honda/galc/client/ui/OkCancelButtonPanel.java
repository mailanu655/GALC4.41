package com.honda.galc.client.ui;

import javax.swing.JButton;
import javax.swing.JPanel;



public class OkCancelButtonPanel extends JPanel {
	
	
	private static final long serialVersionUID = 1L;
	
	private JButton okButton= new JButton("OK");
	private JButton cancelButton= new JButton("CANCEL");
	
	public OkCancelButtonPanel() {
		add(okButton);
		add(cancelButton);
	}
	
	public JButton getOkButton(){
		return okButton;
	}
	
	public JButton getCancelButton(){
		return cancelButton;
	}
}
