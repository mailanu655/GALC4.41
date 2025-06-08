package com.honda.galc.client.ui.component;

import com.honda.galc.common.logging.Logger;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * 
 * <h3>LabeledTextField Class description</h3>
 * <p>
 * LabeledTextField is used to create LabeledPasswordField which will contain combined Label and TextField component 
 * </p>
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
 * @author LnTInfotech<br>
 * 
 */
public class LabeledUpperCaseTextField extends LabeledControl<UpperCaseFieldBean> {

	public LabeledUpperCaseTextField(String labelName,String textFieldId, int length, String fontStyle, TextFieldState state, Pos alignment,boolean isMandatory,Insets insets) {
		super(labelName, new UpperCaseFieldBean(textFieldId));
		getLabel().setAlignment(alignment);
		getLabel().getStyleClass().add("display-label-bold");
		if(isMandatory) {
			markFieldMandatory(getLabel());
		}
		getControl().setMaximumLength(length);
		if (fontStyle != null) {
			getControl().setStyle(fontStyle);
		}
		if (state != null) {
			state.setState(getControl());
		}
		getControl().setPrefColumnCount(length);
		getControl().setAlignment(alignment);
		this.setPadding(new Insets(0));
		setMargin(getControl(), insets);
	}
	
	
	/**
	 * This method is used to clear text into TextField
	 */	
	public void clear() {
		getControl().setText("");
	}
	
	/**
	 * This method is used to set text into TextField
	 */
	public void setText(String input) {
		input = input == null ? "": input.toUpperCase();
		try{
			getControl().setText(input);

		}catch(IndexOutOfBoundsException e){
			Logger.getLogger().warn(e, this.getClass().getSimpleName() + "::setText() exception.");
		}
	}

	/**
	 * This method is used to get text from TextField
	 */
	public String getText() {
		return getControl().getText();
	}

	/**
	 * This method is used to disable TextField
	 */
	public void setEnable(boolean flag) {
		getControl().setDisable(flag);
	}
	
}
