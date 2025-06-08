package com.honda.galc.client.codebroadcast;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.utils.ViewUtil;

public class CodeBroadcastSpecialTagPanel extends CodeBroadcastPanel {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TITLE = "Special Tags";
	private JPanel specialTagPanel;
	private ButtonGroup specialTagButtonGroup;
	private JRadioButton dummyButton;

	public CodeBroadcastSpecialTagPanel() {
		super(CodeBroadcastPanelType.SPECIAL_TAG, DEFAULT_TITLE, KeyEvent.VK_T);
	}

	public CodeBroadcastSpecialTagPanel(TabbedMainWindow mainWindow) {
		super(CodeBroadcastPanelType.SPECIAL_TAG, DEFAULT_TITLE, KeyEvent.VK_T, mainWindow);
	}

	@Override
	public JPanel getCodePanel() {
		if (this.codePanel == null) {
			this.codePanel = initCodePanel(formatTitle(getPropertyBean().getLabelSpecialTag() + " Codes"), getPropertyBean().getLabelCodePanel(), getCodePanelFields(), getController().getStationJobCodes());
		}
		return this.codePanel;
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
		ViewUtil.setGridBagConstraints(getMainPanel(), getSpecialTagPanel(), 0, 0, 2, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), createLabel(getPropertyBean().getLabelSpecialTag(), getProductSpecTextField()), 0, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.FIRST_LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getProductSpecTextField(), 1, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.FIRST_LINE_START, 1.0, null);
		int rowIndex = initDisplayComponents(getMainPanel(), 2, getDisplayFields());
		if (getCodePanel() != null) {
			ViewUtil.setGridBagConstraints(getMainPanel(), getCodePanel(), 0, rowIndex, 2, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
			rowIndex++;
			ViewUtil.setGridBagConstraints(getMainPanel(), getPropertyBean().isDisplayOnly() ? getDummyLabel() : getConfirmButton(), 0, rowIndex, 2, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		} else {
			ViewUtil.setGridBagConstraints(getMainPanel(), getPropertyBean().isDisplayOnly() ? getDummyLabel() : getConfirmButton(), 0, rowIndex, 2, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
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
			tagButton.addItemListener(((CodeBroadcastSpecialTagController) getController()).getSpecialTagPanelItemListener(tagButton, specialTag));
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
	}

	@Override
	public void actionPerformed(ActionEvent e) {}

}
