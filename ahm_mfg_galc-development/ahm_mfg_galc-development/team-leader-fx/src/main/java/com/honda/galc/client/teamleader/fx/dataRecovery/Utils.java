package com.honda.galc.client.teamleader.fx.dataRecovery;


import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.utils.UiFactory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>Utils</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD> L&T Infotech</TD>
 * <TD>Jul 17, 2017</TD>
 * <TD>1.0.0</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 1.0.0
 * @author  L&T Infotech
 */
public class Utils {

	// === fonts === //
	public static Font getMainLabelFont() {
		return Font.font("Dialog", FontWeight.SEMI_BOLD, getScreenHeight()*0.03);
	}

	public static Font getInputFont() {
		return Font.font("Dialog", FontWeight.BOLD, getScreenHeight()*0.05);
	}

	public static Font getButtonFont() {
		return Font.font("Dialog", FontWeight.BOLD, getScreenHeight()*0.025);
	}

	public static String getPlainFont(double size) {
		return Fonts.SS_FONT_PLAIN("Dialog", (int)(getScreenHeight() * size));
	}
	
	public static String getBoldFont(double size) {
		return Fonts.SS_FONT_BOLD("Dialog", (int)(getScreenHeight() * size));
	}
	
	// === state control === //
	public static <T extends TextField> void setTextFieldInputColors(T textfield) {
		if (textfield == null) {
			return;
		}
		TextFieldState.EDIT.setState(textfield);
	}

	public static <T extends TextField> void setIdleColors(T textfield) {
		TextFieldState.READ_ONLY.setState(textfield);
	}

	public static <T extends TextField> void setIdleMode(T textField) {
		textField.setText("");
		TextFieldState.READ_ONLY.setState(textField);
	}

	public static <T extends TextField> void setInputReadOnlyColors(T textField) {
		if (textField == null) return;
		TextFieldState.GOOD_READ_ONLY.setState(textField);
	}

	public static <T extends TextField> void setErrorColors(T textField) {
		if (textField == null) return;
		TextFieldState.ERROR_READ_ONLY.setState(textField);
	}
	
	public static <T extends TextField> void setEditableErrorColors(T textField) {
		if (textField == null) return;
		TextFieldState.ERROR.setState(textField);
	}

	public static <T extends TextField> void setTextFieldEditable(T field) {
		if (field == null) return;
		Utils.setTextFieldInputColors(field);
		field.setEditable(true);
	}

	/** This method will set font size of the components/labels 
	 * in the node according to screen size.
	 * 
	 * @param node
	 */
	public static void setFontSize(Node node){
		node.setStyle(String.format("-fx-font-size: %dpx; ", (int)(0.01 * getScreenWidth())));
	}

	/** This method will return the screen width.
	 * 
	 * @return screen width
	 */
	public static double getScreenWidth() {
		return  Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	/** This method will return the screen height.
	 * 
	 * @return screen height
	 */
	public static double getScreenHeight() {
		return  Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	public static LoggedButton createButton(String text, EventHandler<ActionEvent> controller) {
		LoggedButton button = UiFactory.createButton(text, text);
		button.defaultButtonProperty().bind(button.focusedProperty());
		button.setFont(Utils.getButtonFont());
		button.setOnAction(controller);
		button.setAlignment(Pos.CENTER);
		button.setMinSize(getScreenWidth() * 0.08 , getScreenHeight() * 0.055);
		return button;
	}
	
}
