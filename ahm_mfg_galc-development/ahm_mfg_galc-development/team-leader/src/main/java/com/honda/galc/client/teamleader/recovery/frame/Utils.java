package com.honda.galc.client.teamleader.recovery.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.text.Document;

import com.honda.galc.client.ui.component.DecimalDocument;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.NumericDocument;
import com.honda.galc.client.ui.component.UpperCaseDocument;

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
 * <TD>Karol Wozniak</TD>
 * <TD>Jul 17, 2008</TD>
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
public class Utils {

	// === layout metrics === //
	public static int getLeftMargin() {
		return 20;
	}

	public static int getTopMargin() {
		return 20;
	}

	public static int getDataAttributeTopMargin(int elementCount) {
		if (elementCount == 0) {
			return Utils.getTopMargin();
		}
		int margin = (Utils.getDataPanelHeight() / elementCount - getDataElementHeight(elementCount)) / 2;
		if (margin > 20) {
			return Utils.getTopMargin();
		}
		return margin;
	}

	public static int getDataElementPanelHeight(int elementCount) {

		int maxHeight = Utils.getElementHeight();

		if (elementCount == 0) {
			return maxHeight;
		}
		int height = getDataPanelHeight() / elementCount;

		if (height > maxHeight) {
			return maxHeight;
		}

		return height;
	}

	public static int getDataElementHeight(int elementCount) {
		if (elementCount > 4) {
			return getDataElementPanelHeight(elementCount) - 4;
		}
		return getDataElementPanelHeight(elementCount);
	}

	public static int getLeftInputMargin() {
		return getLeftMargin() + getProductInfoLabelWidth();
	}

	public static int getLeftInputValueMargin() {
		return getLeftInputMargin() + getStatusFieldWidth() + 5;
	}

	public static int getButtonMargin() {
		return getProductInfoPanelWidth() - getButtonWidth() - 20;
	}

	public static int getProductInfoLabelWidth() {
		return 220;
	}

	public static int getElementHeight() {
		return 40;
	}

	public static int getNumberFieldWidth() {
		return 550;
	}

	public static int getPanelLeftMargin() {
		return 30;
	}

	public static int getProductInfoPanelHeight() {
		return 180;
	}

	public static int getProductInfoPanelWidth() {
		return 1000;
	}

	public static int getDataPanelWidth() {
		return getProductInfoPanelWidth();
	}

	public static int getButtonWidth() {
		return 130;
	}

	public static int getStatusFieldWidth() {
		return 120;
	}

	public static int getDefectStatusFieldWidth() {
		return getStatusFieldWidth();
	}

	public static int getDataPanelHeight() {
		return 360;
	}

	public static int getLabelWidth() {
		return 220;
	}

	// === fonts === //
	public static Font getInputLabelFont() {
		return Fonts.DIALOG_PLAIN_24;
	}

	public static Font getInputMediumFont() {
		return Fonts.DIALOG_PLAIN_30;
	}

	public static Font getInputFont() {
		return Fonts.DIALOG_PLAIN_36;
	}

	public static Font getButtonFont() {
		return Fonts.DIALOG_PLAIN_18;
	}

	public static Font getInputSmallFont() {
		return Fonts.DIALOG_BOLD_16;
	}

	public static Font getDataLabelFont() {
		return Fonts.DIALOG_PLAIN_20;
	}

	// === state control === //
	public static void setTextFieldInputColors(JTextField textfield) {
		if (textfield == null) {
			return;
		}
		textfield.setBackground(Color.BLUE);
		textfield.setForeground(Color.WHITE);
		textfield.setCaretColor(Color.WHITE);
	}

	public static void setIdleColors(JTextField textField) {
		textField.setBackground(Color.LIGHT_GRAY);
		textField.setForeground(Color.BLACK);
	}
	
	public static void setIdleMode(JTextField textField) {
		textField.setText("");
		textField.setEditable(false);
		setIdleColors(textField);
	}

	public static void setInputReadOnlyColors(JComponent component) {
		if (component == null) return;
		component.setBackground(Color.GREEN);
		component.setForeground(Color.BLACK);
	}

	public static void setErrorColors(JComponent component) {
		if (component == null) return;
		component.setBackground(Color.RED);
		component.setForeground(Color.BLACK);
	}

	public static void setTextFieldEditable(JTextField field) {
		if (field == null) return;
		Utils.setTextFieldInputColors(field);
		field.setEditable(true);

	}

	// === ui elements factory methods === //
	public static JButton createButton(String text, int locationX, int locationY) {
		JButton button = new JButton(text);
		button.setFont(Utils.getButtonFont());
		button.setSize(Utils.getButtonWidth(), Utils.getElementHeight());
		button.setLocation(locationX, locationY);
		return button;
	}

	public static JLabel createDataLabel(String text) {
		JLabel label = new JLabel(text, JLabel.LEFT);
		label.setSize(220, 40);
		label.setFont(Utils.getDataLabelFont());
		return label;
	}

	public static JComboBox createStatusCombo() {
		JComboBox combo = new JComboBox();
		int width = Utils.getStatusFieldWidth();
		int height = Utils.getElementHeight();
		combo.setSize(width, height);
		combo.setFont(Utils.getInputMediumFont());
		combo.setRequestFocusEnabled(true);
		combo.setEditable(false);
		ComboBoxModel model = new DefaultComboBoxModel(BuildAttributeStatus.values());
		combo.setModel(model);
		combo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				setHorizontalAlignment(JLabel.CENTER);
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});
		return combo;
	}

	public static JTextField createStatusField() {
		JTextField field = new JTextField();
		field.setText("");
		field.setSize(Utils.getStatusFieldWidth(), Utils.getElementHeight());
		field.setFont(Utils.getInputMediumFont());
		field.setHorizontalAlignment(JTextField.CENTER);
		field.setRequestFocusEnabled(true);
		field.setEditable(false);
		return field;
	}

	public static JTextField createSingleValueField(BuildAttribute attribute) {
		JTextField field = new JTextField();
		field.setText("");
		field.setFont(Utils.getInputMediumFont());
		field.setRequestFocusEnabled(true);
		field.setDocument(createDocument(attribute));
		if (attribute.getLength() < 6) {
			field.setHorizontalAlignment(JTextField.CENTER);
		}
		field.setEditable(false);
		return field;
	}

	public static Document createDocument(BuildAttribute attribute) {
		Document document = null;
		if (attribute == null) {
			document = new UpperCaseDocument();
		} else if (attribute.isValueDecimal()) {
			document = new DecimalDocument(attribute.getLength());
		} else if (attribute.isValueNumeric()) {
			document = new NumericDocument(attribute.getLength());
		} else {
			document = new UpperCaseDocument(attribute.getLength());
		}
		return document;
	}
	
	public static JTextField createValueField(BuildAttribute attribute) {
		JTextField field = createSingleValueField(attribute);
		field.setText("");
		field.setSize(Utils.getNumberFieldWidth() - getStatusFieldWidth() - 5, Utils.getElementHeight());
		field.setFont(Utils.getInputMediumFont());
		field.setRequestFocusEnabled(true);
		field.setDocument(new UpperCaseDocument(attribute.getLength()));
		field.setEditable(false);
		return field;
	}

	public static int calculateFontSize(double elementHeight) {
		double screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		double ehInch = elementHeight / screenRes;
		double fontHeightPoints = ehInch * 72;
		return (int) fontHeightPoints;
	}
}
