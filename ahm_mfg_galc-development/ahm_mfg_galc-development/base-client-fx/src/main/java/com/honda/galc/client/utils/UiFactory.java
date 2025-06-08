package com.honda.galc.client.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.text.TextAlignment;

import org.tbee.javafx.scene.layout.MigPane;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ITextFieldRender;
import com.honda.galc.client.ui.component.LoggedButton;
import com.honda.galc.client.ui.component.LoggedComboBox;
import com.honda.galc.client.ui.component.LoggedCustomMenuItem;
import com.honda.galc.client.ui.component.LoggedLabel;
import com.honda.galc.client.ui.component.LoggedMenu;
import com.honda.galc.client.ui.component.LoggedMenuBar;
import com.honda.galc.client.ui.component.LoggedMenuItem;
import com.honda.galc.client.ui.component.LoggedPasswordField;
import com.honda.galc.client.ui.component.LoggedRadioButton;
import com.honda.galc.client.ui.component.LoggedTableCell;
import com.honda.galc.client.ui.component.LoggedTableColumn;
import com.honda.galc.client.ui.component.LoggedTableView;
import com.honda.galc.client.ui.component.LoggedText;
import com.honda.galc.client.ui.component.LoggedTextArea;
import com.honda.galc.client.ui.component.LoggedTextField;
import com.honda.galc.client.ui.component.LoggedTextFieldTableCell;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>UiFactory</code> is ... .
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
 * <TD>&nbsp;</TD>
 * <TD>&nbsp;</TD>
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
public class UiFactory {

	private static String labelFont;
	private static String inputFont;
	private static String buttonFont;
	private Class T;

	private UiFactory(String labelFont, String inputFont, String buttonFont) {
		this.labelFont = labelFont;
		this.inputFont = inputFont;
		this.buttonFont = buttonFont;
	}

	public static UiFactory getDefault() {
		return new UiFactory(null, null, null);
	}

	public static UiFactory getIdle() {
		return new UiFactory(Fonts.SS_DIALOG_PLAIN(20), Fonts.SS_DIALOG_PLAIN(20), Fonts.SS_DIALOG_PLAIN(20));
	}

	public static UiFactory getInfo() {
		return new UiFactory(Fonts.SS_DIALOG_PLAIN(20), Fonts.SS_DIALOG_PLAIN(20), Fonts.SS_DIALOG_PLAIN(20));
	}
	
	public static UiFactory getInfoLarge() {
		return new UiFactory(Fonts.SS_DIALOG_PLAIN(20), Fonts.SS_DIALOG_PLAIN(80), Fonts.SS_DIALOG_PLAIN(20));
	}
	
	public static UiFactory getLargeLabel() {
		return new UiFactory(Fonts.SS_DIALOG_PLAIN(50), null, null);
	}

	public static UiFactory getInfoSmall() {
		return new UiFactory(Fonts.SS_DIALOG_PLAIN(16), Fonts.SS_DIALOG_PLAIN(16), Fonts.SS_DIALOG_PLAIN(16));
	}

	public static UiFactory getInputLarge() {
		return new UiFactory(Fonts.SS_DIALOG_BOLD(16), Fonts.SS_DIALOG_PLAIN(26), Fonts.SS_DIALOG_PLAIN(16));
	}

	public static UiFactory getInputBig() {
		return new UiFactory(null, Fonts.SS_DIALOG_PLAIN(24), null);
	}

	public static UiFactory getInput() {
		return new UiFactory(null, Fonts.SS_DIALOG_PLAIN(22), Fonts.SS_DIALOG_BOLD(12));
	}

	public static UiFactory getInputSmall() {
		return new UiFactory(null, Fonts.SS_DIALOG_PLAIN(18), null);
	}
	
	public static UiFactory create(String labelFont, String inputFont, String buttonFont) {
		return new UiFactory(labelFont, inputFont, buttonFont);
	}	

	public static LoggedLabel createLabel(String id, String text, String fontStyle) {
		LoggedLabel component = new LoggedLabel(id, text);
		if (fontStyle != null) { component.setStyle(fontStyle);}
		return component;
	}

	public static LoggedLabel createLabel(String id, String text, String fontStyle, TextAlignment alignment) {
		LoggedLabel component = createLabel(id, text, fontStyle);
		component.setTextAlignment(alignment);
		return component;
	}

	public static LoggedLabel createLabel(String id) {
		return new LoggedLabel(id);
	}
	
	public static LoggedLabel createLabel(String id, String text) {
		return new LoggedLabel(id, text);
	}
	
	/**
	 * Create Label with relative font size
	 */
	public static LoggedLabel createLabel(String id, String text, int relativeFontSize) {
		LoggedLabel label= UiFactory.createLabel(id, text);
		label.setStyle(String.format("-fx-font-weight: bold; -fx-font-size: %dpx;", relativeFontSize));
		return label;
	}

	public static LoggedLabel createLabel(String id, String text, String fontStyle, TextAlignment alignment, double width) {
		LoggedLabel label = createLabel(id, text, fontStyle, alignment);
		label.setPrefWidth(width);
		return label;
	}
	
	public static LoggedLabel createLabel(String id, String text, String fontStyle, double width) {
		return createLabel(id, text, fontStyle, TextAlignment.CENTER, width);
	}
	
	public static LoggedLabel createLabel(String id, String text, TextAlignment alignment) {
		return createLabel(id, text, getLabelFont(), alignment);
	}

	public static LoggedTextField createTextField(String id){
		LoggedTextField component = new LoggedTextField(id);
		return component;
	}
	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state) {
		LoggedTextField component = createTextField(id);
		if (fontStyle != null) component.setStyle(fontStyle);
		if (state != null) {
			state.setState(component);
		}
		return component;
	}

	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state, double width) {
		LoggedTextField textField = createTextField(id, fontStyle, state);
		textField.setPrefWidth(width);
		return textField;
	}
	
	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state, double width, double height) {
		LoggedTextField textField = createTextField(id, fontStyle, state);
		textField.setPrefWidth(width);
		textField.setPrefHeight(height);
		return textField;
	}
	
	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state, double width, String textFieldId) {
		LoggedTextField textField = createTextField(id, fontStyle, state, textFieldId);
		textField.setPrefWidth(width);
		return textField;
	}
		
	public static LoggedTextField createTextField(String id, int length, String fontStyle, TextFieldState state) {
		LoggedTextField component = createTextField(id, fontStyle, state);
		component.setPrefColumnCount(length);
		return component;
	}
	
	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state, String textFieldId) {
		LoggedTextField component = new LoggedTextField(id);
		component.setId(textFieldId);
		if (fontStyle != null) component.setStyle(fontStyle);
		if (state != null) {
			state.setState(component);
		}
		return component;
	}
	
	public static LoggedTextField createTextField(String id, int length, String fontStyle, TextFieldState state, String textFeldId) {
		LoggedTextField component = createTextField(id, fontStyle, state);
		component.setId(textFeldId);
		component.setPrefColumnCount(length);
		return component;
	}

	public static LoggedTextField createTextField(String id, String fontStyle, TextFieldState state, Pos alignment) {
		LoggedTextField component = createTextField(id, fontStyle, state);
		component.setAlignment(alignment);
		return component;
	}

	public static LoggedTextField createTextField(String id, int length, String fontStyle, TextFieldState state, Pos alignment) {
		LoggedTextField component = createTextField(id, length, fontStyle, state);
		component.setAlignment(alignment);
		return component;
	}

	public static LoggedTextField createBearingMeasurementTextField(String id, int conrodCount) {
		int fontSize = 20;
		if (conrodCount < 5) {
			fontSize = 28;
		} else if (conrodCount > 6) {
			fontSize = 18;
		}
		UpperCaseFieldBean tf= (UpperCaseFieldBean) UiFactory.createTextField(id, 1, Fonts.SS_DIALOG_PLAIN(fontSize), TextFieldState.EDIT, Pos.CENTER, true);
		//Don't use the text field background color. Use the background color from the css style
		tf.setColor(null);
		return tf;
	}

	// === public textField factory methods === //
	public static LoggedTextField createTextField(String id, TextFieldState state) {
		return createTextField(id, getInputFont(), state);
	}

	public static LoggedTextField createTextField(String id, int length, TextFieldState state) {
		return createTextField(id, length, getInputFont(), state);
	}

	public static LoggedTextField createTextField(String id, int length, TextFieldState state, Pos alignment) {
		return createTextField(id, length, getInputFont(), state, alignment);
	}
	
	public static LoggedTextField createTextField(String id, int length, String fontStyle, TextFieldState state, Pos alignment, boolean useUpperCaseTextField) {
		if(useUpperCaseTextField){
			UpperCaseFieldBean component = new UpperCaseFieldBean(id);
			component.setMaximumLength(length);
			if (fontStyle != null) {
				component.setStyle(fontStyle);
			}
			if (state != null) {
				state.setState(component);
			}
			component.setPrefColumnCount(length);
			component.setAlignment(alignment);
			return component;
		}
		else{
			return createTextField(id, length, fontStyle, state, alignment);
		}
	}
	
	public static UpperCaseFieldBean createUpperCaseFieldBean(String id, int length, String fontStyle, TextFieldState state, Pos alignment){
		return createUpperCaseFieldBean(id, length, fontStyle, state, alignment, null);
	}
	
	public static UpperCaseFieldBean createUpperCaseFieldBean(String id, int length, String fontStyle, TextFieldState state, Pos alignment, ITextFieldRender renderer)  {
		UpperCaseFieldBean component = null;
		if(renderer == null)  {
			component = new UpperCaseFieldBean(id);
		}
		else  {
			component = new UpperCaseFieldBean(id,renderer);			
		}
		component.setMaximumLength(length);
		if (fontStyle != null) {
			component.setStyle(fontStyle);
		}
		if (state != null) {
			state.setState(component);
		}
		component.setPrefColumnCount(length);
		component.setAlignment(alignment);
		return component;
	}
	
	public static LoggedButton createButton(String text, String fontStyle, boolean enabled) {
		LoggedButton component = new LoggedButton(text, text);
		if (fontStyle != null) {
			component.setStyle(fontStyle);
		}
		component.setDisable(!enabled);
		return component;
	}
	
	public static LoggedButton createButton(String text, String fontStyle, boolean enabled, String id) {
		LoggedButton component = new LoggedButton(text, id);
		if (fontStyle != null) {
			component.setStyle(fontStyle);
		}
		component.setDisable(!enabled);
		return component;
	}
	
	public static LoggedButton createButton(String text) {
		return createButton(text, getButtonFont(), true);
	}
	
	public static LoggedButton createButton(String text, String id) {
		return createButton(text, getButtonFont(), true, id);
	}

	public static LoggedButton createButton(String text, Node arg1) {
		LoggedButton component = new LoggedButton(text, arg1, text);
		return component;
	}

	public static LoggedButton createButton(String text, boolean enabled) {
		return createButton(text, getButtonFont(), enabled);
	}
	
	public static void addSeparator(MigPane panel, String text) {
		LoggedLabel label = getDefault().createLabel(text, text);
		String labelStyle = "gapbottom 1, span, split 2, aligny center";
		panel.add(label, labelStyle);
		Separator separator = new Separator();
		panel.add(separator, "gapleft rel, growx");
	}
	
	public static void addSeparator(MigPane panel, String text, String style) {
		LoggedLabel label = getDefault().createLabel(text, text);
		String labelStyle = "gapbottom 1, span, split 2, aligny center";
		if (style != null) {
			labelStyle = String.format("%s , %s", labelStyle, style);
		}
		panel.add(label, labelStyle);
		Separator separator = new Separator();
		panel.add(separator, "gapleft rel, growx");
	}	

	public static LoggedTextField createBearingColorTextField(String id, int conrodCount) {
		int fontSize = 30;
		if (conrodCount > 4) {
			fontSize = 24;
		}
		return UiFactory.createTextField(id, 20, Fonts.SS_DIALOG_PLAIN(fontSize), TextFieldState.EDIT, Pos.CENTER, true);
	}

	public static LoggedTextField createTextField(String id, String text) {
		return new LoggedTextField(id, text);
	}
	
	public static LoggedPasswordField createPasswordField(String id){
		LoggedPasswordField passwordField = new LoggedPasswordField();
		passwordField.setId(id);
		return passwordField;
	}
	
	public static LoggedRadioButton createRadioButton(){
		return new LoggedRadioButton();
	}
	
	public static LoggedRadioButton createRadioButton(String arg1){
		return new LoggedRadioButton(arg1);
	}
	
	// === get/set === //
	public static String getLabelFont() {
		return labelFont;
	}

	public static String getButtonFont() {
		return buttonFont;
	}

	public static String getInputFont() {
		return inputFont;
	}
	
	public static LoggedTableView createTableView(){
		return new LoggedTableView();
	}
	public static <T, className> LoggedTableView<className> createTableView(Class className){
		return new LoggedTableView<className>();
	}
	
	public static <T, className> LoggedTableView<className> createTableView(Class className, String id){
		return new LoggedTableView<className>(id);
	}
	
	public static <T, S> LoggedTableColumn<T, S> createTableColumn(String columnTitle){
		return new LoggedTableColumn<T, S>(columnTitle);
	}

	public static <class1, class2> LoggedTableColumn<class1, class2> createTableColumn(Class class1, Class class2, String columnTitle) {
		return new LoggedTableColumn<class1, class2>(columnTitle);
	}
	
	public static <class1, class2> LoggedTableColumn<class1, class2> createTableColumn(Class class1, Class class2) {
		return new LoggedTableColumn<class1, class2>();
	}
	
	public static <class1, class2> LoggedTableCell<class1, class2> createTableCell(Class class1, Class class2){
		return new LoggedTableCell<class1, class2>();
	}

	public static LoggedTextFieldTableCell createTextFieldTableCell(){
		return new LoggedTextFieldTableCell();
	}
	
	public static LoggedText createText() {
		return new LoggedText();
	}
	
	public static LoggedText createText(String text){
		return new LoggedText(text);
	}
	
	public static LoggedTextArea createTextArea(){
		return new LoggedTextArea();
	}
	
	public static LoggedTextArea createTextArea(String text){
		return new LoggedTextArea(text);
	}

	public static LoggedMenu createMenu(String label) {
		return new LoggedMenu(label);
	}
	
	public static LoggedMenuItem createMenuItem(String label){
		return new LoggedMenuItem(label);
	}
	
	public static LoggedMenuBar createMenuBar(){
		return new LoggedMenuBar();
	}
	
	public static LoggedCustomMenuItem createCustomMenuItem(Node arg1){
		return new LoggedCustomMenuItem(arg1);
	}
	
	public static <className>LoggedComboBox createComboBox(Class className){
		return new LoggedComboBox<className>();
	}
	
	public static <classname>LoggedComboBox createComboBox(Class classname, String id){
		return new LoggedComboBox<classname>(id);
	}
	
	public static LoggedLabel createLabelWithStyle(String id, String text,String style) {
		LoggedLabel label = new LoggedLabel(id, text);
		label.getStyleClass().add(style);
		return label;
	}
}
