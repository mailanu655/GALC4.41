package com.honda.galc.client.ui.component;

import javax.swing.JTextField;

/**
 * <h3>Class description</h3>
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Dylan Yang</TD>
 * <TD>Apr 22, 2014</TD>
 * <TD>1.0</TD>
 * <TD>GY 20140422</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
public class LabeledTextPanel extends LabeledComponent<JTextField> {
	private static final long serialVersionUID = -241311157201603438L;

	private UpperCaseFieldBean textField;
	private TextInputRender textRender;
	
	public LabeledTextPanel(String labelName) {
		super(labelName, new UpperCaseFieldBean(), true);
		initialize();
	}

	public LabeledTextPanel(String labelName, UpperCaseFieldBean component,	boolean isHorizontal) {
		super(labelName, component, isHorizontal);
		initialize();
	}

	private void initialize() {
		textRender = new TextInputRender();
		getTextField().setRender(textRender);
	}

	public void inputMode() {
		getTextRender().inputMode();
	}
	
	public void errorCorrectionMode() {
		getTextRender().errorCorrectionMode();
	}
	
	public void okMode() {
		getTextRender().renderField(true);
	}
	
	public void errorMode() {
		getTextRender().renderField(false);
	}
	
	public void disableMode() {
		getTextRender().disableMode();
	}
	
	public void setText(String string) {
		getTextField().setText(string);
	}
	
	public void setText(Text text) {
		getTextRender().renderField(text);
	}
	
	public void clear() {
		getTextRender().clear();
	}
	
	public void requestFocus() {
		getTextField().requestFocus();
	}
	
	public void setEditable(boolean aBoolean) {
		getTextField().setEditable(aBoolean);
	}
	
	public void setEnabled(boolean aBoolean) {
		getTextField().setEnabled(aBoolean);
	}

	public UpperCaseFieldBean getTextField() {
		if(textField == null) {
			textField = (UpperCaseFieldBean) getComponent();
		}
		return textField;
	}

	public TextInputRender getTextRender() {
		return textRender;
	}
}
