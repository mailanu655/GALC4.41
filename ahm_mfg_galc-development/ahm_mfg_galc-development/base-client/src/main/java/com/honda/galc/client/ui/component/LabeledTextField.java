package com.honda.galc.client.ui.component;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class LabeledTextField extends LabeledComponent<JTextField> {

	private static final long serialVersionUID = 1L;

	
	public LabeledTextField(String labelName) {
		this(labelName, true);
		
	}
	
	public LabeledTextField(String labelName,boolean isHorizontal) {
		super(labelName, new JTextField(),isHorizontal);
		getComponent().setColumns(20);
		this.setInsets(10, 10, 10, 10);
	}
	
	public void setNumeric(int limit){
		getComponent().setDocument(new NumericDocument(limit));
	}
	
	public void setUpperCaseField(boolean upperCase){
		((AbstractDocument)getComponent().getDocument()).setDocumentFilter(new UpperCaseDocumentFilter());
	}
	
	public void clear() {
		getComponent().setText("");
	}
}
