package com.honda.galc.client.ui.component;

import javax.swing.text.AbstractDocument;

/**
 * 
 * <h3>LabeledUpperCaseTextField Class description</h3>
 * <p> LabeledUpperCaseTextField description </p>
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
 * May 18, 2011
 *
 *
 */
public class LabeledUpperCaseTextField extends LabeledComponent<UpperCaseFieldBean> {

	private static final long serialVersionUID = 1L;

	
	public LabeledUpperCaseTextField(String labelName) {
		this(labelName, true);
		
	}
	
	public LabeledUpperCaseTextField(String labelName,boolean isHorizontal) {
		super(labelName, new UpperCaseFieldBean(),isHorizontal);
		getComponent().setColumns(20);
		this.setInsets(10, 10, 10, 10);
	}
	
	public void setUpperCaseField(boolean upperCase){
		((AbstractDocument)getComponent().getDocument()).setDocumentFilter(new UpperCaseDocumentFilter());
	}
	
	public void clear() {
		getComponent().setText("");
	}
}
