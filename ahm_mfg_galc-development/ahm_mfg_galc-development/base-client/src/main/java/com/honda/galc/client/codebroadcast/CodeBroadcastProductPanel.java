package com.honda.galc.client.codebroadcast;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.component.UpperCaseFieldBean;
import com.honda.galc.client.utils.ViewUtil;

public class CodeBroadcastProductPanel extends CodeBroadcastPanel {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_TITLE = "Product";
	private UpperCaseFieldBean productIdTextField;
	private JTextField previousProductIdTextField;
	private JTextField previousProductSpecTextField;
	private JButton productIdButton;
	private JLabel productIdLabel;
	private JLabel previousProductIdLabel;

	public CodeBroadcastProductPanel() {
		super(CodeBroadcastPanelType.PRODUCT, DEFAULT_TITLE, KeyEvent.VK_P);
		setScreenName(getController().getProductIdLabelText());
	}

	public CodeBroadcastProductPanel(TabbedMainWindow mainWindow) {
		super(CodeBroadcastPanelType.PRODUCT, DEFAULT_TITLE, KeyEvent.VK_P, mainWindow);
		setScreenName(getController().getProductIdLabelText());
	}

	public JTextField getProductIdTextField() {
		if (this.productIdTextField == null) {
			this.productIdTextField = new UpperCaseFieldBean();
			this.productIdTextField.setColumns(getPropertyBean().getTextFieldSize());
			if (getPropertyBean().isDisplayOnly()) {
				this.productIdTextField.setEditable(false);
				this.productIdTextField.setFocusable(false);
			}
			this.productIdTextField.setFont(this.font);
		}
		return this.productIdTextField;
	}

	public JTextField getPreviousProductIdTextField() {
		if (this.previousProductIdTextField == null) {
			this.previousProductIdTextField = createDisplayTextField();
		}
		return this.previousProductIdTextField;
	}

	public JTextField getPreviousProductSpecTextField() {
		if (this.previousProductSpecTextField == null) {
			this.previousProductSpecTextField = createDisplayTextField();
		}
		return this.previousProductSpecTextField;
	}

	public JButton getProductIdButton() {
		if (this.productIdButton == null) {
			final String functionKey = getPropertyBean().getFunctionKeyProductSelect();
			this.productIdButton = new JButton(getController().getProductIdLabelText().concat(StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : "").concat(":"));
			this.productIdButton.setFocusable(false);
			this.productIdButton.setFont(this.font);
			this.productIdButton.setToolTipText("Select a product" + (StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : ""));
		}
		return this.productIdButton;
	}

	public JLabel getProductIdLabel() {
		if (this.productIdLabel == null) {
			this.productIdLabel = new JLabel(getController().getProductIdLabelText().concat(":"));
			this.productIdLabel.setFont(this.font);
			this.productIdLabel.setLabelFor(getProductIdTextField());
		}
		return this.productIdLabel;
	}

	public JLabel getPreviousProductIdLabel() {
		if (this.previousProductIdLabel == null) {
			this.previousProductIdLabel = new JLabel("Last ".concat(getController().getProductIdLabelText().concat(":")));
			this.previousProductIdLabel.setFont(this.font);
			this.previousProductIdLabel.setLabelFor(getProductIdTextField());
		}
		return this.previousProductIdLabel;
	}

	protected void initComponents() {
		ViewUtil.setGridBagConstraints(getMainPanel(), getPropertyBean().isDisplayOnly() ? getProductIdLabel() : getProductIdButton(), 0, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getProductIdTextField(), 1, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), createLabel(getController().getProductSpecLabelText(), getProductSpecTextField()), 0, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(getMainPanel(), getProductSpecTextField(), 1, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
		if (getPropertyBean().isShowPreviousProduct()) {
			ViewUtil.setGridBagConstraints(getMainPanel(), getPreviousProductIdLabel(), 2, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), getPreviousProductIdTextField(), 3, 0, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), createLabel("Last ".concat(getController().getProductSpecLabelText()), getPreviousProductSpecTextField()), 2, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(getMainPanel(), getPreviousProductSpecTextField(), 3, 1, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
		}
		int rowIndex = initDisplayComponents(getMainPanel(), 2, getDisplayFields());
		if (getCodePanel() != null) {
			ViewUtil.setGridBagConstraints(getMainPanel(), getCodePanel(), 0, rowIndex, 4, 1, null, null, null, INSETS, GridBagConstraints.LINE_START, 1.0, null);
			rowIndex++;
			ViewUtil.setGridBagConstraints(getMainPanel(), getPropertyBean().isDisplayOnly() ? getDummyLabel() : getConfirmButton(), 0, rowIndex, 4, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		} else {
			ViewUtil.setGridBagConstraints(getMainPanel(), getPropertyBean().isDisplayOnly() ? getDummyLabel() : getConfirmButton(), 0, rowIndex, 4, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		}

		this.setLayout(new GridBagLayout());
		final JScrollPane scrollPane = createScrollPane(getMainPanel());
		ViewUtil.setGridBagConstraints(this, scrollPane, 0, 0, 1, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, 1.0, 1.0);
		if (getPropertyBean().isEnableRefreshButton()) {
			ViewUtil.setGridBagConstraints(this, getRefreshButton(), 0, 1, 1, 1, null, null, null, INSETS, GridBagConstraints.FIRST_LINE_START, null, null);
		}

		getMainWindow().setExtendedState(getMainWindow().getExtendedState() | java.awt.Frame.MAXIMIZED_BOTH);
		getProductIdTextField().requestFocusInWindow();
	}

	@Override
	public void clearTab() {
		super.clearTab();
		getProductIdTextField().setText(null);
		getProductIdButton().setEnabled(true);
		if (!getPropertyBean().isDisplayOnly()) {
			getProductIdTextField().setEditable(true);
			getProductIdTextField().getCaret().setVisible(true);
		}
		if (getPropertyBean().isShowPreviousProduct()) {
			getPreviousProductIdTextField().setText(null);
			getPreviousProductSpecTextField().setText(null);
		}
		requestFocusOnProductId();
	}

	@Override
	public void onTabSelected() {
		if (!getPropertyBean().isAutoConfirmDeviceData() && !isErrorMessage()) {
			clearTab();
		}
		getController().requestTrigger();
	}

	public void clearCurrentProduct() {
		super.clearTab();
		getProductIdTextField().setText(null);
		getProductIdButton().setEnabled(true);
		if (!getPropertyBean().isDisplayOnly()) {
			getProductIdTextField().setEditable(true);
			getProductIdTextField().getCaret().setVisible(true);
		}
		requestFocusOnProductId();
	}

	public void requestFocusOnProductId() {
		if(getProductIdTextField() != null) {
			getProductIdTextField().requestFocusInWindow();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {}

}
