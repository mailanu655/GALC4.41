package com.honda.galc.client.product.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.honda.galc.client.ui.component.ComboBoxState;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.TextFieldState;
import com.honda.galc.client.ui.component.UpperCaseDocument;

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

	private Font labelFont;
	private Font inputFont;
	private Font buttonFont;

	private UiFactory(Font labelFont, Font inputFont, Font buttonFont) {
		this.labelFont = labelFont;
		this.inputFont = inputFont;
		this.buttonFont = buttonFont;
	}

	public static UiFactory getDefault() {
		return new UiFactory(null, null, null);
	}

	public static UiFactory getIdle() {
		return new UiFactory(Fonts.DIALOG_PLAIN_50, Fonts.DIALOG_PLAIN_60, Fonts.DIALOG_PLAIN_50);
	}

	public static UiFactory getInfo() {
		return new UiFactory(Fonts.DIALOG_PLAIN_22, Fonts.DIALOG_PLAIN_30, Fonts.DIALOG_PLAIN_22);
	}

	public static UiFactory getInfoSmall() {
		return new UiFactory(Fonts.DIALOG_PLAIN_18, Fonts.DIALOG_PLAIN_22, Fonts.DIALOG_PLAIN_18);
	}

	public static UiFactory getInputLarge() {
		return new UiFactory(Fonts.DIALOG_BOLD_16, Fonts.DIALOG_PLAIN_26, Fonts.DIALOG_BOLD_16);
	}

	public static UiFactory getInputBig() {
		return new UiFactory(null, Fonts.DIALOG_PLAIN_24, null);
	}

	public static UiFactory getInput() {
		return new UiFactory(null, Fonts.DIALOG_PLAIN_22, Fonts.DIALOG_BOLD_12);
	}

	public static UiFactory getInputSmall() {
		return new UiFactory(null, Fonts.DIALOG_PLAIN_18, null);
	}
	
	public static UiFactory create(Font labelFont, Font inputFont, Font buttonFont) {
		return new UiFactory(labelFont, inputFont, buttonFont);
	}

	// === static factory methods === //
	public static void addSeparator(JPanel panel, String text) {
		JLabel label = getDefault().createLabel(text);
		String labelStyle = "gapbottom 1, span, split 2, aligny center";
		panel.add(label, labelStyle);
		JSeparator separator = new JSeparator();
		panel.add(separator, "gapleft rel, growx");
	}
	
	public static void addSeparator(JPanel panel, String text, String style) {
		JLabel label = getDefault().createLabel(text);
		String labelStyle = "gapbottom 1, span, split 2, aligny center";
		if (style != null) {
			labelStyle = String.format("%s , %s", labelStyle, style);
		}
		panel.add(label, labelStyle);
		JSeparator separator = new JSeparator();
		panel.add(separator, "gapleft rel, growx");
	}	
	
	

	// === static label factory methods === //
	public static JLabel createLabel(String text, Font font) {
		JLabel component = new JLabel(text, SwingConstants.LEADING);
		if (font != null) {
			component.setFont(font);
		}
		return component;
	}

	public static JLabel createLabel(String text, Font font, int alignment) {
		JLabel component = createLabel(text, font);
		component.setHorizontalAlignment(alignment);
		return component;
	}

	// === public label factory methods === //
	public JLabel createLabel(String text) {
		return createLabel(text, getLabelFont());
	}

	public JLabel createLabel(String text, int alignment) {
		return createLabel(text, getLabelFont(), alignment);
	}

	// === public static textField factory methods === //
	public static JTextField createTextField(Font font, TextFieldState state) {
		JTextField component = new JTextField();
		if (font != null) {
			component.setFont(font);
		}
		if (state != null) {
			state.setState(component);
		}
		return component;
	}

	public static JTextField createTextField(int length, Font font, TextFieldState state) {
		JTextField component = createTextField(font, state);
		component.setDocument(new UpperCaseDocument(length));
		return component;
	}

	public static JTextField createTextField(int length, Font font, TextFieldState state, int alignment) {
		JTextField component = createTextField(length, font, state);
		component.setHorizontalAlignment(alignment);
		return component;
	}

	public static JTextField createBearingMeasurementTextField(int conrodCount) {
		int fontSize = 20;
		if (conrodCount < 5) {
			fontSize = 28;
		} else if (conrodCount > 6) {
			fontSize = 18;
		}
		Font font = new Font("Dialog", Font.PLAIN, fontSize);
		return UiFactory.createTextField(1, font, TextFieldState.EDIT, JTextField.CENTER);
	}

	public static JTextField createBearingColorTextField(int conrodCount) {
		int fontSize = 30;
		if (conrodCount > 4) {
			fontSize = 24;
		}
		Font font = new Font("Dialog", Font.PLAIN, fontSize);
		return UiFactory.createTextField(20, font, TextFieldState.EDIT, JTextField.CENTER);
	}

	// === public textField factory methods === //
	public JTextField createTextField(TextFieldState state) {
		return createTextField(getInputFont(), state);
	}

	public JTextField createTextField(int length, TextFieldState state) {
		return createTextField(length, getInputFont(), state);
	}

	public JTextField createTextField(int length, TextFieldState state, int alignment) {
		return createTextField(length, getInputFont(), state, alignment);
	}

	// === public static button factory methods === //
	public static JButton createButton(String name, String text, Font font, boolean enabled) {
		JButton component = new JButton(text);
		component.setName(name);
		component.setText(text);
		if (font != null) {
			component.setFont(font);
		}
		component.setEnabled(enabled);
		return component;
	}

	// === public button factory methods === //
	public JButton createButton(String text) {
		return createButton(text, getButtonFont(), false);
	}

	public JButton createButton(String text, boolean enabled) {
		return createButton(text, getButtonFont(), enabled);
	}
	
	public static JButton createButton(String text, Font font, boolean enabled) {
		return createButton(text.replaceAll("\\s","")+"_Button", text, font, enabled);
	}
	
	public JComboBox createRequiredTypeComboBox(ComboBoxState state) {
		JComboBox comboBox = new JComboBox();
		state.setState(comboBox);
		comboBox.setFont(this.inputFont);
		comboBox.setMaximumSize(new Dimension(250,42));
		ComboBoxEditor editor = comboBox.getEditor();
		JTextField editorTextField = (JTextField)editor.getEditorComponent();
		editorTextField.setBorder(new JTextField().getBorder());
		comboBox.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (c instanceof JLabel) {
					list.setSelectionBackground(Color.GREEN);
				}
                return c;
            }
        });
		return comboBox;
	}

	// === get/set === //
	public Font getLabelFont() {
		return labelFont;
	}

	public Font getButtonFont() {
		return buttonFont;
	}

	public Font getInputFont() {
		return inputFont;
	}
}