package com.honda.galc.client.qics.view.fragments;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.entity.qics.StationResult;
import com.honda.galc.util.BeanUtils;


/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * UI component with product number information.
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
public class UnitInfoIdlePanel extends JPanel {

	public enum UnitInfoConfig {
		INSPECTED("Inspected", "productIdInspect"), 
		DIRECT_PASSED("Direct Passed", "productIdDirectPassed"), 
		WITH_DEFECTS("With Defects", "productIdWithDefects"), 
		REPAIRED("Repaired", "repairedProductId"), 
		OUTSTANDING("Outstanding", "outstandingProductId"), 
		SCRAPPED("Scrapped", "scrap"), 
		PREHEAT("Preheat", "preheatScrapped"),
		TOPCOAT("Topcoat Pass Rate", "topCoatPassRate"), 
		REPAIR_PASS("Repair Pass Rate", "repairPassRate"), 
		AF_OUTSTANDING("AF Units w/ O/S Defects", "afDeptOutstandingVINs"), 
		AF_RESPONSIBLE_OUTSTANDING("Units w/ AF Resp/ O/S", "afResponsibleOutstandingVINs"), 
		AF_STRAIGHT_SHIP("AF Straight Ship", "straight_Ship_Percentage", createSimpleNumberFormat()),
		AF_RESPONSIBLE_STRAIGHT_SHIP("AF Resp/ Straight Ship", "afResponsibleStraightShip"), 
		AF_TOTAL_RPU("Total Rejections Per Unit", "afTotalRPU"), 
		AF_TOTAL("AF Units Produced", "deptUnitsProduced");

		private String name;
		
		// property name has to match method name of the target object
		private String propertyName;
		private NumberFormat numberFormat;

		private UnitInfoConfig(String name, String propertyName) {
			this.name = name;
			this.propertyName = propertyName;
		}

		private UnitInfoConfig(String name, String propertyName, NumberFormat numberFormat) {
			this.name = name;
			this.propertyName = propertyName;
			this.numberFormat = numberFormat;
		}

		public String getName() {
			return name;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public NumberFormat getNumberFormat() {
			return numberFormat;
		}
	};

	private static final long serialVersionUID = 1L;

	private UnitInfoConfig config;

	private String labelText;
	private int labelWidth;
	private int textFieldWidth;
	private int elementHeight;
	private JLabel label;
	private JTextField textField;

	public UnitInfoIdlePanel(String labelText, int labelWidth, int textFieldWidth) {
		this.labelText = labelText;
		this.labelWidth = labelWidth;
		this.textFieldWidth = textFieldWidth;
		this.elementHeight = 50;
		initialize();
	}

	public UnitInfoIdlePanel(String labelText, int labelWidth, int textFieldWidth, int height) {
		this.labelText = labelText;
		this.labelWidth = labelWidth;
		this.textFieldWidth = textFieldWidth;
		this.elementHeight = height;
		initialize();
	}

	protected void initialize() {
		setLayout(null);
		setSize(2 * getHSpacing() + getLabelWidth() + getHSpacing() + getTextFieldWidth() + 2 * getHSpacing(), getElementHeight() + 2 * getVSpacing());
		add(getLabel(), null);
		add(getTextField(), null);

	}

	protected JLabel createLabel() {
		JLabel label = new javax.swing.JLabel();
		label.setFont(Fonts.DIALOG_PLAIN_30);
		label.setForeground(java.awt.Color.black);
		label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		label.setSize(getLabelWidth(), getElementHeight());
		return label;
	}

	protected JTextField createTextField() {
		JTextField textField = new JTextField();
		textField.setText("");
		textField.setBackground(Color.black);
		textField.setCaretColor(Color.gray);
		textField.setForeground(Color.lightGray);
		textField.setFont(Fonts.DIALOG_PLAIN_36);
		textField.setAlignmentX(Component.RIGHT_ALIGNMENT);
		textField.setEditable(false);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		textField.setRequestFocusEnabled(false);
		textField.setEnabled(false);
		textField.setSize(getTextFieldWidth(), getElementHeight());
		return textField;

	}

	protected JLabel getLabel() {
		if (label == null) {
			label = createLabel();
			label.setText(labelText);
			label.setLocation(getVSpacing(), getHSpacing());
		}
		return label;
	}

	protected JTextField getTextField() {

		if (textField == null) {
			textField = createTextField();
			textField.setLocation(getLabelWidth() + 2 * getVSpacing(), getHSpacing());
		}

		return textField;
	}

	public void setText(String text) {
		getTextField().setText(text);
	}

	protected int getElementHeight() {
		return elementHeight;
	}

	protected int getLabelWidth() {
		return labelWidth;
	}

	protected int getTextFieldWidth() {
		return textFieldWidth;
	}

	protected int getVSpacing() {
		return 5;
	}

	protected int getHSpacing() {
		return 5;
	}

	public UnitInfoConfig getConfig() {
		return config;
	}

	public void setConfig(UnitInfoConfig config) {
		this.config = config;
	}

	public void setTextValue(StationResult result) {
		String text = "";
		if (result != null) {
			try {

				Object value = BeanUtils.getNestedPropertyValue(result, getConfig().getPropertyName());

				if (getConfig() != null && getConfig().getNumberFormat() != null) {
					value = getConfig().getNumberFormat().format(value);
				}
				text = toString(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		getTextField().setText(text);
	}

	protected String toString(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	private static NumberFormat createSimpleNumberFormat() {
		NumberFormat numberFormat = NumberFormat.getPercentInstance();
		numberFormat.setMinimumFractionDigits(1);
		numberFormat.setMaximumFractionDigits(1);
		return numberFormat;
	}
}
