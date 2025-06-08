package com.honda.galc.client.ui.component;



import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;


/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>TextFieldState</code> is ...
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
 * <TR>
 * <TD>Karol Wozniak</TD>
 * <TD>Apr 7, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 */
public enum TextFieldState {

	DISABLED(false,"text-field-state-disabled"),
	EDIT(true,"text-field-state-edit"), 
	READ_ONLY(false,"text-field-state-read-only"), 
	ERROR(true,"text-field-state-error"),
	ERROR_READ_ONLY(false,"text-field-state-error-read-only"),
	GOOD_READ_ONLY(false,"text-field-state-good-read-only");
	
	private boolean enabled;
	private String  css;

	private TextFieldState() {
	}

	private TextFieldState(boolean enabled, String css) {
		this.enabled = enabled;
		this.css =css;
	}

	protected boolean isEnabled() {
		return enabled;
	}

	public void setState(TextField field) {
		if (field == null) return;
		
		field.setEditable(isEnabled());

		if(isEnabled()) {
			field.requestFocus();
		} 
		
		field.setUserData(this);
		
		removePreviousStyle(field);
		field.getStyleClass().add(css);
	}
	
	private void removePreviousStyle(TextField field) {
		ArrayList<String> removeStyleList = new ArrayList<String>();
		
		ObservableList<String> styleList =  field.getStyleClass();
		for (String style : styleList) {
			if (style.startsWith("text-field-state")) {
				removeStyleList.add(style);
			}
		}

		for (String style : removeStyleList) {
			styleList.remove(style);
		}
	}

	public boolean isInState(TextField field) {
		if (field == null) {
			return false;
		}
		return this.equals(field.getUserData());
	}
}
