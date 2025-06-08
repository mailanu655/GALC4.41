package com.honda.galc.client.codebroadcast;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.codebroadcast.CodeBroadcastEmailHandler.CodeBroadcastEmailNotificationLevel;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.service.property.PropertyService;

public abstract class CodeBroadcastPanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;
	public enum CodeBroadcastPanelType { PRODUCT, SPECIAL_TAG, SPECIAL_TAG_BY_COLOR_CODE };
	protected static final Insets INSETS = new Insets(8, 8, 8, 8);
	protected static final Insets INSETS_TEXT_FIELD = new Insets(8, 2, 8, 8);
	protected static final Insets INSETS_TEXT_FIELD_LABEL = new Insets(8, 8, 8, 2);

	protected final Font font;
	private final ClientAudioManager audioManager;
	private final CodeBroadcastPropertyBean propertyBean;
	private final CodeBroadcastEmailHandler emailHandler;
	private CodeBroadcastController controller;
	protected JPanel mainPanel;
	protected JPanel codePanel;
	private Map<String, CodeBroadcastConfirmationField> codePanelFields;
	private Map<String, JTextField> displayFields;
	private JTextField productSpecTextField;
	private JButton confirmButton;
	private JButton refreshButton;

	public CodeBroadcastPanel(CodeBroadcastPanelType type, String screenName, int keyEvent) {
		super(screenName, keyEvent);
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		this.propertyBean = PropertyService.getPropertyBean(CodeBroadcastPropertyBean.class, getMainWindow().getApplicationContext().getProcessPointId());
		this.emailHandler = new CodeBroadcastEmailHandler(this.propertyBean, getMainWindow().getApplicationContext().getProcessPointId());
		this.font = new Font("Dialog", Font.PLAIN, this.propertyBean.getFontSize());
		setController(createClientController(type));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { initComponents(); }
		});
	}

	public CodeBroadcastPanel(CodeBroadcastPanelType type, String screenName, int keyEvent, MainWindow mainWindow) {
		super(screenName, keyEvent, mainWindow);
		this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class));
		this.propertyBean = PropertyService.getPropertyBean(CodeBroadcastPropertyBean.class, getMainWindow().getApplication().getApplicationId());
		this.emailHandler = new CodeBroadcastEmailHandler(this.propertyBean, getMainWindow().getApplication().getApplicationId());
		this.font = new Font("Dialog", Font.PLAIN, this.propertyBean.getFontSize());
		setController(createClientController(type));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() { initComponents(); }
		});
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		getMainPanel().setBackground(bg);
	}

	protected ClientAudioManager getAudioManager() {
		return this.audioManager;
	}
	public CodeBroadcastPropertyBean getPropertyBean() {
		return this.propertyBean;
	}
	public CodeBroadcastEmailHandler getEmailHandler() {
		return this.emailHandler;
	}
	protected CodeBroadcastController getController() {
		return this.controller;
	}
	protected void setController(CodeBroadcastController controller) {
		this.controller = controller;
	}

	public JPanel getMainPanel() {
		if (this.mainPanel == null) {
			this.mainPanel = new JPanel();
			this.mainPanel.setLayout(new GridBagLayout());
		}
		return this.mainPanel;
	}

	public JPanel getCodePanel() {
		if (this.codePanel == null) {
			this.codePanel = initCodePanel("Codes", getPropertyBean().getLabelCodePanel(), getCodePanelFields(), getController().getStationJobCodes());
		}
		return this.codePanel;
	}

	public Map<String, CodeBroadcastConfirmationField> getCodePanelFields() {
		if (this.codePanelFields == null) {
			this.codePanelFields = new HashMap<String, CodeBroadcastConfirmationField>();
		}
		return this.codePanelFields;
	}

	public Map<String, JTextField> getDisplayFields() {
		if (this.displayFields == null) {
			this.displayFields = new HashMap<String, JTextField>();
		}
		return this.displayFields;
	}

	public JTextField getProductSpecTextField() {
		if (this.productSpecTextField == null) {
			this.productSpecTextField = createDisplayTextField();
		}
		return this.productSpecTextField;
	}

	public JButton getConfirmButton() {
		if (this.confirmButton == null) {
			final String functionKey = getPropertyBean().getFunctionKeyConfirm();
			this.confirmButton = new JButton("Confirm" + (StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : ""));
			this.confirmButton.setEnabled(false);
			this.confirmButton.setFocusable(false);
			this.confirmButton.setFont(this.font);
			this.confirmButton.setToolTipText("Confirm the info" + (StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : ""));
		}
		return this.confirmButton;
	}

	protected JLabel getDummyLabel() {
		return new JLabel();
	}

	public JButton getRefreshButton() {
		if (this.refreshButton == null) {
			final String functionKey = getPropertyBean().getFunctionKeyRefresh();
			this.refreshButton = new JButton("Refresh" + (StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : ""));
			this.refreshButton.setFocusable(false);
			this.refreshButton.setFont(this.font);
			this.refreshButton.setToolTipText("Refresh the screen" + (StringUtils.isNotBlank(functionKey) ? " (" + functionKey.toUpperCase() + ")" : ""));
		}
		return this.refreshButton;
	}

	protected JLabel createLabel(final String label, final java.awt.Component labelFor) {
		JLabel productSpecLabel = new JLabel(label.concat(":"));
		productSpecLabel.setFont(this.font);
		productSpecLabel.setLabelFor(labelFor);
		return productSpecLabel;
	}

	protected JTextField createDisplayTextField() {
		JTextField jTextField = new JTextField(getPropertyBean().getTextFieldSize());
		jTextField.setEditable(false);
		jTextField.setFocusable(false);
		jTextField.setFont(this.font);
		return jTextField;
	}

	protected TitledBorder createTitledBorder(final String title) {
		LineBorder lineBorder = new LineBorder(new Color(184, 207, 229));
		TitledBorder titledBorder = new TitledBorder(lineBorder);
		titledBorder.setTitle(title);
		return titledBorder;
	}

	protected JScrollPane createScrollPane(final java.awt.Component view) {
		final JScrollPane scrollPane = new JScrollPane(view);
		scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		scrollPane.getVerticalScrollBar().setUnitIncrement(getPropertyBean().getFontSize()/2);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(getPropertyBean().getFontSize()/2);
		return scrollPane;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" } )
	private CodeBroadcastController createClientController(CodeBroadcastPanelType type) {
		String controllerClz = getPropertyBean().getControllerClass();
		if (StringUtils.isBlank(controllerClz) || controllerClz.equals(CodeBroadcastController.class.getName())) {
			switch (type) {
			case PRODUCT:
				return new CodeBroadcastProductController(this);
			case SPECIAL_TAG:
				return new CodeBroadcastSpecialTagController(this);
			case SPECIAL_TAG_BY_COLOR_CODE:
				return new CodeBroadcastSpecialTagByColorCodeController(this);
			}
		} else {
			try {
				Class<? extends CodeBroadcastController> forName = (Class<? extends CodeBroadcastController>) Class.forName(controllerClz);
				Class[] parameterTypes = { CodeBroadcastPanel.class };
				Object[] parameters = { this };
				Constructor constructor = forName.getConstructor(parameterTypes);
				return (CodeBroadcastController)constructor.newInstance(parameters);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	protected abstract void initComponents();

	protected JPanel initCodePanel(final String title, final String label, final Map<String, CodeBroadcastConfirmationField> codePanelFields, final String[] codes) {
		if (ArrayUtils.isEmpty(codes)) {
			return null;
		}
		JPanel codePanel = new JPanel();
		codePanel.setBorder(createTitledBorder(title));
		codePanel.setLayout(new GridBagLayout());
		codePanel.setVisible(false);

		final int numColumns = getPropertyBean().getColumnCountCodePanel();
		if (numColumns <= 0) throw new RuntimeException("Number of code panel columns must be positive");

		int rowIndex = 0;
		int columnIndex = 0;
		if (StringUtils.isNotBlank(label)) {
			JLabel panelLabel = new JLabel(label);
			panelLabel.setFont(this.font);
			panelLabel.setForeground(Color.RED);
			ViewUtil.setGridBagConstraints(codePanel, panelLabel, rowIndex, columnIndex, numColumns*2, 1, null, null, null, INSETS, null, 1.0, null);
			rowIndex++;
		}
		for (final String stationJobCode : codes) {
			final JLabel codeLabel = new JLabel(getController().getCodeDisplayName(stationJobCode).concat(":"));
			final CodeBroadcastConfirmationField codePanelField = new CodeBroadcastConfirmationField();
			codeLabel.setFont(this.font);
			codeLabel.setLabelFor(codePanelField);
			codePanelField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			codePanelField.setBackground(getPropertyBean().getColorUnconfirmed());
			codePanelField.setFocusable(false);
			codePanelField.setFont(this.font);
			ViewUtil.setGridBagConstraints(codePanel, codeLabel, columnIndex*2, rowIndex, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
			ViewUtil.setGridBagConstraints(codePanel, codePanelField, (columnIndex*2)+1, rowIndex, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
			codePanelFields.put(stationJobCode, codePanelField);
			columnIndex++;
			if (columnIndex == numColumns) {
				rowIndex++;
				columnIndex = 0;
			}
		}
		return codePanel;
	}

	protected String formatTitle(final String title) {
		StringBuilder titleBuilder = new StringBuilder();
		for (int i = 0; i < title.length(); i++) {
			if (i == 0 || !Character.isLetterOrDigit(title.charAt(i-1))) {
				titleBuilder.append(Character.toUpperCase(title.charAt(i)));
			} else {
				titleBuilder.append(Character.toLowerCase(title.charAt(i)));
			}
		}
		return titleBuilder.toString();
	}

	protected int initDisplayComponents(final JPanel panel, final int startRowIndex, final Map<String, JTextField> displayFields) {
		int rowIndex = startRowIndex;
		if (getController().getStationDisplayFormats() != null) {
			for (final PrintAttributeFormat stationDisplayFormat : getController().getStationDisplayFormats()) {
				final JTextField displayField = addDisplayComponent(panel, rowIndex, stationDisplayFormat.getAttribute());
				displayFields.put(stationDisplayFormat.getAttribute(), displayField);
				rowIndex++;
			}
		}
		if (ArrayUtils.isNotEmpty(getPropertyBean().getStationDisplayCodes())) {
			for (final String stationDisplayCode : getPropertyBean().getStationDisplayCodes()) {
				final JTextField displayField = addDisplayComponent(panel, rowIndex, stationDisplayCode);
				displayFields.put(stationDisplayCode, displayField);
				rowIndex++;
			}
		}
		return rowIndex;
	}

	protected JTextField addDisplayComponent(final JPanel panel, final int rowIndex, final String stationDisplayField) {
		final JLabel displayLabel = new JLabel(getController().getCodeDisplayName(stationDisplayField).concat(":"));
		final JTextField displayField = createDisplayTextField();
		displayLabel.setFont(this.font);
		displayLabel.setLabelFor(displayField);
		ViewUtil.setGridBagConstraints(panel, displayLabel, 0, rowIndex, 1, 1, null, null, null, INSETS_TEXT_FIELD_LABEL, GridBagConstraints.LINE_START, null, null);
		ViewUtil.setGridBagConstraints(panel, displayField, 1, rowIndex, 1, 1, null, null, null, INSETS_TEXT_FIELD, GridBagConstraints.LINE_START, 1.0, null);
		return displayField;
	}

	public void populateCodePanel(final JPanel codePanel, final Map<String, CodeBroadcastConfirmationField> codePanelFields, final JButton confirmButton, final List<CodeBroadcastCode> codes, final boolean isDisplay) {
		if (codePanel == null || codes == null || codes.isEmpty()) return;
		codePanel.setVisible(true);
		confirmButton.setEnabled(false);

		for (final CodeBroadcastCode code : codes) {
			CodeBroadcastConfirmationField codePanelField = codePanelFields.get(code.getKey());
			if (codePanelField == null) continue;
			codePanelField.setText(code.getValue());
			codePanelField.setBackground(code.getConfirmed() ? getPropertyBean().getColorConfirmed() : getPropertyBean().getColorUnconfirmed());
			codePanelField.setCode(code);
			codePanelField.setConfirmationEnabled(!isDisplay);
			if (code.getChangeListeners().isEmpty()) {
				code.addChangeListener(getController().getCodePanelConfirmListener(code, codePanelField));
			}
		}
	}

	public void populateDisplayFields(final Map<String, JTextField> displayFields, final List<CodeBroadcastCode> displayCodes) {
		if (displayCodes == null || displayCodes.isEmpty()) return;

		for (final CodeBroadcastCode code : displayCodes) {
			JTextField displayField = displayFields.get(code.getKey());
			if (displayField == null) continue;
			displayField.setText(code.getValue());
		}
	}

	public void clearDisplayFields() {
		if (getDisplayFields() == null || getDisplayFields().isEmpty()) {
			return;
		}
		for (String displayField : getDisplayFields().keySet()) {
			getDisplayFields().get(displayField).setText(null);
		}
	}

	public void clearTab() {
		getController().handleClearTab();
		if (getCodePanel() != null) {
			getCodePanel().setVisible(false);
		}
		getConfirmButton().setEnabled(false);
		getProductSpecTextField().setText(null);
		setBackground(getPropertyBean().getColorNeutral());
		clearDisplayFields();
	}

	@Override
	public void onTabSelected() {
		if (!isErrorMessage()) {
			clearTab();
		}
		getController().requestTrigger();
	}

	public boolean isErrorMessage() {
		return getMainWindow().getStatusMessagePanel().isError();
	}

	public boolean isWarning() {
		return getMainWindow().getStatusMessagePanel().isWarning();
	}

	public void clearMessage() {
		getMainWindow().clearStatusMessage();
		getMainWindow().clearMessage();
	}

	public void displayMessage(String message, boolean popup) {
		Logger.getLogger().info(message);
		if (message == null || (!getController().isErrorState() && message.equals(getMainWindow().getMessage()))) return;
		getMainWindow().setMessage(message);
		if (this.emailHandler.isSendEmail(CodeBroadcastEmailNotificationLevel.MESSAGE)) {
			this.emailHandler.sendEmail(message, CodeBroadcastEmailNotificationLevel.MESSAGE.name());
		}
		if (popup) {
			MessageDialog.showColoredMessageDialog(getMainWindow(), message, "Information", JOptionPane.INFORMATION_MESSAGE, Color.lightGray);
		}
	}
	public void playOKSound() {
		try {
			getAudioManager().playOKSound();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	public void displayWarningMessage(String warningMessage, boolean popup) {
		Logger.getLogger().warn(warningMessage);
		if (warningMessage == null || (!getController().isErrorState() && warningMessage.equals(getMainWindow().getMessage()))) return;
		playWarnSound();
		getMainWindow().setWarningMessage(warningMessage);
		if (this.emailHandler.isSendEmail(CodeBroadcastEmailNotificationLevel.WARNING)) {
			this.emailHandler.sendEmail(warningMessage, CodeBroadcastEmailNotificationLevel.WARNING.name());
		}
		if (popup) {
			MessageDialog.showColoredMessageDialog(getMainWindow(), warningMessage, "Warning", JOptionPane.WARNING_MESSAGE, Color.yellow);
		}
	}
	public void playWarnSound() {
		try {
			getAudioManager().playWarnSound();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

	public void displayErrorMessage(String errorMessage) {
		displayErrorMessage(errorMessage, null, false);
	}
	public void displayErrorMessage(String errorMessage, boolean popup) {
		displayErrorMessage(errorMessage, null, popup);
	}
	public void displayErrorMessage(String errorMessage, Throwable throwable) {
		displayErrorMessage(errorMessage, throwable, true);
	}
	public void displayErrorMessage(String errorMessage, Throwable throwable, boolean popup) {
		if (throwable == null) {
			Logger.getLogger().error(errorMessage);
		}
		else {
			Logger.getLogger().error(throwable, errorMessage);
		}
		if (errorMessage == null || (getController().isErrorState() && errorMessage.equals(getMainWindow().getMessage()))) return;
		playNGSound();
		getMainWindow().setErrorMessage(errorMessage);
		if (this.emailHandler.isSendEmail(CodeBroadcastEmailNotificationLevel.ERROR)) {
			this.emailHandler.sendEmail(errorMessage, CodeBroadcastEmailNotificationLevel.ERROR.name());
		}
		if (popup) {
			MessageDialog.showColoredMessageDialog(getMainWindow(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE, Color.red);
		}
	}
	public void playNGSound() {
		try {
			getAudioManager().playNGSound();
		} catch (Exception e) {
			Logger.getLogger().error(e);
		}
	}

}
