package com.honda.galc.client.ui.component;


import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * 
 * <h3>LabeledPasswordField Class description</h3>
 * <p>
 * LabeledPasswordField is used to create LabeledPasswordField which will contain combined Label and PasswordField component 
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
public class LabeledPasswordField extends LabeledControl<LoggedPasswordField> {

	public LabeledPasswordField(String labelName) {
		this(labelName, true,false);
	}
	
	public LabeledPasswordField(String labelName, LoggedPasswordField control,boolean isMandetory) {
		super(labelName, control);
		applyComponentStyle(isMandetory);
	}
	
	public LabeledPasswordField(String labelName, boolean isHorizontal,boolean isMandatory) {
		super(labelName, new LoggedPasswordField(), isHorizontal);
		applyComponentStyle(isMandatory);
	}
	
	public LabeledPasswordField(String labelName, boolean isHorizontal,TextFieldState state, String cssLabelStyle,String cssTextStyle,Insets insets,int prefLabelWidth, Pos position,boolean isMandatory) {
		this(labelName, isHorizontal,state,insets,prefLabelWidth,position,isMandatory);
		if (state != null) {
			state.setState(getControl());
		}
		applyComponentStyle(cssLabelStyle,cssTextStyle);
	}
	
	public LabeledPasswordField(String labelName, boolean isHorizontal,TextFieldState state,Insets insets,int prefLabelWidth, Pos position,boolean isMandatory) {
		super(labelName, new LoggedPasswordField(), isHorizontal);
		if (state != null) {
			state.setState(getControl());
		}
		this.setPadding(insets);
		getLabel().setPrefWidth(prefLabelWidth);
		getLabel().setAlignment(position);
		getLabel().getStyleClass().add("display-label-bold");
		if(isMandatory) {
			markFieldMandatory(getLabel());
		}
	}
	

	private void applyComponentStyle(String cssLabelStyle,String cssTextStyle) {
		getLabel().getStyleClass().add(cssLabelStyle);
		getControl().getStyleClass().add(cssTextStyle);
		getControl().setAlignment(Pos.BASELINE_RIGHT);
		
	}

	/**
	 * This method is used to apply CSS Style
	 */
	private void applyComponentStyle(boolean isMandatory) {
		this.setPadding(new Insets(0));
		setMargin(getControl(), new Insets(0, 10, 0, 10));
		if(isMandatory) {
			markFieldMandatory(getLabel());
		}
		getLabel().setPrefWidth(120);
		getLabel().getStyleClass().add("display-label-bold");
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
		getControl().setText(input);
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