package com.honda.galc.client.codebroadcast;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.utils.ViewUtil;

public class CodeBroadcastSpecialTagByColorCodePanel extends CodeBroadcastPanel {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TITLE = "Special Tags by Color Code";
	private final Dimension comboBoxDimension;
	private JPanel specialTagPanel;
	private JPanel colorCodePanel;
	private JPanel specialColorPanel;
	private Map<String, CodeBroadcastConfirmationField> colorCodePanelFields;
	private Map<String, CodeBroadcastConfirmationField> specialColorPanelFields;
	private JTextField productSpecColorCodeTextField;
	private JTextField productSpecSpecialColorTextField;
	private ButtonGroup specialTagButtonGroup;
	private JRadioButton dummyButton;
	private JComboBox colorCodeComboBox;
	private JComboBox specialColorComboBox;

	public CodeBroadcastSpecialTagByColorCodePanel() {
		super(CodeBroadcastPanelType.SPECIAL_TAG_BY_COLOR_CODE, DEFAULT_TITLE, KeyEvent.VK_T);
		{
			JTextField jTextField = new JTextField(getPropertyBean().getTextFieldSize());
			jTextField.setFont(this.font);
			JComboBox jComboBox = new JComboBox();
			jComboBox.setFont(this.font);
			this.comboBoxDimension = new Dimension((int) jTextField.getPreferredSize().getWidth(), (int) jComboBox.getPreferredSize().getHeight());
		}
	}

	public CodeBroadcastSpecialTagByColorCodePanel(TabbedMainWindow mainWindow) {
		super(CodeBroadcastPanelType.SPECIAL_TAG_BY_COLOR_CODE, DEFAULT_TITLE, KeyEvent.VK_T, mainWindow);
		{
			JTextField jTextField = new JTextField(getPropertyBean().getTextFieldSize());
			jTextField.setFont(this.font);
			JComboBox jComboBox = new JComboBox();
			jComboBox.setFont(this.font);
			this.comboBoxDimension = new Dimension((int) jTextField.getPreferredSize().getWidth(), (int) jComboBox.getPreferredSize().getHeight());
		}
	}

	private CodeBroadcastSpecialTagByColorCodeController getSpecialTagController() {
		return (CodeBroadcastSpecialTagByColorCodeController) getController();
	}

	@Override
	public JPanel getCodePanel() {
		if (this.codePanel == null) {
			this.codePanel = initCodePanel(formatTitle(getPropertyBean().getLabelSpecialTag() + " Codes"), getPropertyBean().getLabelCodePanel(), getCodePanelFields(), getController().getStationJobCodes());
		}
		return this.codePanel;
	}

	@Override
	public Map<String, JTextField> getDisplayFields() {
		return null;
	}

	@Override
	public void populateDisplayFields(final Map<String, JTextField> displayFields, final List<CodeBroadcastCode> displayCodes) {
		throw new UnsupportedOperationException("The MANUAL_CODE_ENTRY_SPECIAL_TAG_BY_COLOR_CODE panel does not support display fields");
	}

	private JPanel getSpecialTagPanel() {
		if (this.specialTagPanel == null) {
			this.specialTagPanel = new JPanel();
			this.specialTagPanel.setBorder(createTitledBorder(formatTitle(getPropertyBean().getLabelSpecialTag())));
			this.specialTagPanel.setLayout(new GridBagLayout());
			this.specialTagPanel.setVisible(false);
		}
		return this.specialTagPanel;
	}

	public JPanel getColorCodePanel() {
		if (this.colorCodePanel == null) {
			this.colorCodePanel = initCodePanel(formatTitle(getPropertyBean().getLabelColorTag() + " Codes"), getPropertyBean().getLabelColorTagCodePanel(), getColorCodePanelFields(), getPropertyBean().getStationColorCodes());
		}
		return this.colorCodePanel;
	}

	public Map<String, CodeBroadcastConfirmationField> getColorCodePanelFields() {
		if (this.colorCodePanelFields == null) {
			this.colorCodePanelFields = new HashMap<String, CodeBroadcastConfirmationField>();
		}
		return this.colorCodePanelFields;
	}

	public JPanel getSpecialColorPanel() {
		if (this.specialColorPanel == null) {
			this.specialColorPanel = initCodePanel(formatTitle(getPropertyBean().getLabelSpecialColor() + " Codes"), getPropertyBean().getLabelSpecialColorCodePanel(), getSpecialColorPanelFields(), getPropertyBean().getStationSpecialColorCodes());
		}
		return this.specialColorPanel;
	}

	public Map<String, CodeBroadcastConfirmationField> getSpecialColorPanelFields() {
		if (this.specialColorPanelFields == null) {
			this.specialColorPanelFields = new HashMap<String, CodeBroadcastConfirmationField>();
		}
		return this.specialColorPanelFields;
	}

	public JTextField getProductSpecColorCodeTextField() {
		if (this.productSpecColorCodeTextField == null) {
			this.productSpecColorCodeTextField = createDisplayTextField();
		}
		return this.productSpecColorCodeTextField;
	}

	public JTextField getProductSpecSpecialColorTextField() {
		if (this.productSpecSpecialColorTextField == null) {
			this.productSpecSpecialColorTextField = createDisplayTextField();
		}
		return this.productSpecSpecialColorTextField;
	}

	private JPanel createComboBoxPanel(final String title, final JComboBox comboBox) {
		JPanel comboBoxPanel = new JPanel();
		comboBoxPanel.setBorder(createTitledBorder(formatTitle(title)));
		comboBoxPanel.setLayout(new GridBagLayout());
		ViewUtil.setGridBagConstraints(comboBoxPanel, comboBox, 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, 1.0);
		return comboBoxPanel;
	}

	public JComboBox getColorCodeComboBox() {
		if (this.colorCodeComboBox == null) {
			this.colorCodeComboBox = new JComboBox();
			this.colorCodeComboBox.setBackground(Color.WHITE);
			this.colorCodeComboBox.setFont(this.font);
			this.colorCodeComboBox.setPreferredSize(this.comboBoxDimension);
			this.colorCodeComboBox.setRenderer(new CodeBroadcastTagListCellRenderer());

			List<CodeBroadcastTag> colorTagsAndColorCodes = getSpecialTagController().getColorTagsAndColorCodes();
			if (colorTagsAndColorCodes != null) {
				final CodeBroadcastTag[] colorTagsAndColorCodesArray = colorTagsAndColorCodes.toArray(new CodeBroadcastTag[colorTagsAndColorCodes.size()]);
				final ComboBoxModel colorTagsAndColorCodesModel = new DefaultComboBoxModel(colorTagsAndColorCodesArray);
				this.colorCodeComboBox.setModel(colorTagsAndColorCodesModel);
			}
			this.colorCodeComboBox.setSelectedIndex(-1);
		}
		return this.colorCodeComboBox;
	}

	public JComboBox getSpecialColorComboBox() {
		if (this.specialColorComboBox == null) {
			this.specialColorComboBox = new JComboBox();
			this.specialColorComboBox.setBackground(Color.WHITE);
			this.specialColorComboBox.setFont(this.font);
			this.specialColorComboBox.setPreferredSize(this.comboBoxDimension);
			this.specialColorComboBox.setRenderer(new CodeBroadcastTagListCellRenderer());

			List<CodeBroadcastTag> specialColors = getSpecialTagController().getSpecialColorTags();
			if (specialColors != null) {
				final CodeBroadcastTag[] specialColorsArray = specialColors.toArray(new CodeBroadcastTag[specialColors.size()]);
				final ComboBoxModel specialColorsModel = new DefaultComboBoxModel(specialColorsArray);
				this.specialColorComboBox.setModel(specialColorsModel);
			}
			this.specialColorComboBox.setSelectedIndex(-1);
		}
		return this.specialColorComboBox;
	}

	public CodeBroadcastTag getSelectedColorCode() {
		Object selectedItem = getColorCodeComboBox().getSelectedItem();
		if (selectedItem == null) return null;
		return (CodeBroadcastTag) selectedItem;
	}

	public CodeBroadcastTag getSelectedSpecialColor() {
		Object selectedItem = getSpecialColorComboBox().getSelectedItem();
		if (selectedItem == null) return null;
		return (CodeBroadcastTag) selectedItem;
	}

	private ButtonGroup getSpecialTagButtonGroup() {
		if (this.specialTagButtonGroup == null) {
			this.specialTagButtonGroup = new ButtonGroup();
		}
		return this.specialTagButtonGroup;
	}

	private JRadioButton getDummyButton() {
		if (this.dummyButton == null) {
			this.dummyButton = new JRadioButton("DUMMY");
			this.dummyButton.setVisible(false);
		}
		return this.dummyButton;
	}

	protected void initComponents() {
		ViewUtil.setGridBagConstraints(getMainPanel(), getSpecialTagPanel(), 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), createLabel(getPropertyBean().getLabelSpecialTag(), getProductSpecTextField()), 1, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getProductSpecTextField(), 2, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getCodePanel(), 0, 1, 4, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), createComboBoxPanel(getPropertyBean().getLabelColorTag(), getColorCodeComboBox()), 0, 2, 2, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), createLabel(getPropertyBean().getLabelColorTag(), getProductSpecColorCodeTextField()), 1, 2, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getProductSpecColorCodeTextField(), 2, 2, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getColorCodePanel(), 0, 3, 4, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
		if (getSpecialTagController().isShowSpecialColors()) {
			ViewUtil.setGridBagConstraints(getMainPanel(), createComboBoxPanel(getPropertyBean().getLabelSpecialColor(), getSpecialColorComboBox()), 0, 4, 2, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), createLabel(getPropertyBean().getLabelSpecialColor(), getProductSpecSpecialColorTextField()), 1, 4, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), getProductSpecSpecialColorTextField(), 2, 4, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), getSpecialColorPanel(), 0, 5, 4, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), getConfirmButton(), 0, 6, 4, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		} else {
			ViewUtil.setGridBagConstraints(getMainPanel(), getConfirmButton(), 0, 4, 4, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}

		this.setLayout(new GridBagLayout());
		final JScrollPane scrollPane = createScrollPane(getMainPanel());
		ViewUtil.setGridBagConstraints(this, scrollPane, 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		if (getPropertyBean().isEnableRefreshButton()) {
			ViewUtil.setGridBagConstraints(this, getRefreshButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, null, null);
		}

		getMainWindow().setExtendedState(getMainWindow().getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH);
		this.requestFocusInWindow();
	}

	public void populateSpecialTagPanel(final List<CodeBroadcastTag> specialTags) {
		if (specialTags == null || specialTags.isEmpty()) return;
		getSpecialTagPanel().removeAll();
		getSpecialTagPanel().setVisible(true);
		getConfirmButton().setEnabled(false);
		getSpecialTagButtonGroup().add(getDummyButton());

		final int numColumns = getPropertyBean().getColumnCountSpecialTagPanel();
		if (numColumns <= 0) throw new RuntimeException("Number of special tag panel columns must be positive");
		int rowIndex = 1;
		int columnIndex = 0;
		for (final CodeBroadcastTag specialTag : specialTags) {
			final JRadioButton tagButton = new JRadioButton(specialTag.getTag());
			tagButton.setFont(this.font);
			tagButton.addItemListener(getSpecialTagController().getSpecialTagItemListener(tagButton, specialTag));
			getSpecialTagButtonGroup().add(tagButton);
			ViewUtil.setGridBagConstraints(getSpecialTagPanel(), tagButton, columnIndex, rowIndex, 1, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
			columnIndex++;
			if (columnIndex == numColumns) {
				rowIndex++;
				columnIndex = 0;
			}
		}
	}

	@Override
	public void clearTab() {
		super.clearTab();
		getSpecialTagButtonGroup().setSelected(getDummyButton().getModel(), true); // getSpecialTagButtonGroup().clearSelection();
		getColorCodeComboBox().setSelectedIndex(-1);
		getProductSpecColorCodeTextField().setText(null);
		getColorCodePanel().setVisible(false);
		if (getSpecialTagController().isShowSpecialColors()) {
			getSpecialColorComboBox().setSelectedIndex(-1);
			getProductSpecSpecialColorTextField().setText(null);
			getSpecialColorPanel().setVisible(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {}

	private class CodeBroadcastTagListCellRenderer extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = 1L;

		public CodeBroadcastTagListCellRenderer() {
			setOpaque(true);
			setFont(font);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			if (value != null && value.getClass().isAssignableFrom(CodeBroadcastTag.class)) {
				setText(((CodeBroadcastTag) value).getTag());
			} else {
				setText(null);
			}
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			return this;
		}
	}

}
