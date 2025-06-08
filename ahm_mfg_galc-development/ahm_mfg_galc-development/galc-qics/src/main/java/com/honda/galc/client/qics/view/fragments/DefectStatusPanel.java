package com.honda.galc.client.qics.view.fragments;


import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.honda.galc.client.qics.config.QicsClientConfig;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.entity.enumtype.DefectStatus;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * UI component with defect status radio buttons group.
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
public class DefectStatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JPanel radioButtonPanel;
	private ButtonGroup buttonGroup;
	private QicsClientConfig clientConfig;
	private Collection<DefectStatus> defectStatuses;
	private Map<DefectStatus, JRadioButton> radioButtons;

	public DefectStatusPanel() {
		super();
		initialize(220);
	}

	public DefectStatusPanel(QicsClientConfig clientConfig) {
		super();
		this.clientConfig = clientConfig;
		initialize(220);
	}

	public DefectStatusPanel(QicsClientConfig clientConfig, int width) {
		super();
		this.clientConfig = clientConfig;
		initialize(width);
	}

	protected void initialize(int width) {
		radioButtons = new HashMap<DefectStatus, JRadioButton>();
		defectStatuses = Arrays.asList(new DefectStatus[] { DefectStatus.OUTSTANDING, DefectStatus.REPAIRED });

		setSize(width, 30 + 30 * defectStatuses.size());
		setLayout(null);
		add(getRadioButtonPanel());
	}

	@Override
	public void setEnabled(boolean enabled) {
		for (DefectStatus defectStatus : defectStatuses) {
			JRadioButton radioButton = getRadioButtons().get(defectStatus);
			radioButton.setEnabled(enabled);
		}
	}

	public void setSelectedDefectStatus(int intValue) {
		DefectStatus status = DefectStatus.getType(intValue);
		getRadioButton(status).setSelected(true);
	}

	public void resetInput() {
		getDefaultRadioButton().setSelected(true);
	}

	public DefectStatus getDefaultDefectStatus() {
		return getClientConfig().getDefaultDefectStatus();
	}

	public JRadioButton getDefaultRadioButton() {
		return getRadioButton(getDefaultDefectStatus());
	}

	// ========= factory methods for ui elements =============
	protected ButtonGroup getButtonGroup() {
		if (buttonGroup == null) {
			buttonGroup = new ButtonGroup();
		}
		return buttonGroup;
	}

	protected JPanel getRadioButtonPanel() {
		if (radioButtonPanel == null) {
			radioButtonPanel = new JPanel();
			radioButtonPanel.setLayout(new GridLayout(defectStatuses.size(), 1, 5, 5));
			radioButtonPanel.setSize(getWidth(), 30 * defectStatuses.size() + 30);
			radioButtonPanel.setLocation(0, 0);
			for (DefectStatus defectStatus : defectStatuses) {
				JRadioButton radioButton = createRadioButton(defectStatus);
				radioButtonPanel.add(radioButton);
				getRadioButtons().put(defectStatus, radioButton);
				getButtonGroup().add(radioButton);
			}
			TitledBorder border = new TitledBorder("Status");
			border.setTitleFont(Fonts.DIALOG_PLAIN_18);
			radioButtonPanel.setBorder(border);
		}
		return radioButtonPanel;
	}

	protected JRadioButton createRadioButton(DefectStatus defectStatus) {
		JRadioButton button = new JRadioButton();
		if (defectStatus != null) {
			button.setName(defectStatus.getName());
			button.setText(defectStatus.getName());
			button.setMnemonic(getKeyEvent(defectStatus));
			button.setEnabled(true);
		}
		button.setFont(Fonts.DIALOG_PLAIN_18);
		return button;
	}

	protected int getKeyEvent(DefectStatus defectStatus) {
		int keyEvent = 0;

		if (defectStatus == null) {
			return keyEvent;
		}
		switch (defectStatus) {
		case OUTSTANDING:
			keyEvent = KeyEvent.VK_U;
			break;
		case REPAIRED:
			keyEvent = KeyEvent.VK_A;
			break;
		case SCRAP:
			keyEvent = KeyEvent.VK_S;
			break;
		}
		return keyEvent;
	}

	protected JRadioButton getRadioButton(DefectStatus defectStatus) {
		JRadioButton radioButton = getRadioButtons().get(defectStatus);
		if (radioButton == null) {
			radioButton = createRadioButton(defectStatus);
			getRadioButtons().put(defectStatus, radioButton);
		}
		return radioButton;
	}


	public int getSelectedStatus() {
		Set<Entry<DefectStatus, JRadioButton>> entrySet = getRadioButtons().entrySet();
		for (Entry<DefectStatus, JRadioButton> entry : entrySet) {
			DefectStatus defectStatus = entry.getKey();
			JRadioButton radioButton = entry.getValue();
			if (radioButton.isSelected()) {
				return defectStatus.getId();
			}
		}
		return DefectStatus.OUTSTANDING.getId();
	}

	protected Map<DefectStatus, JRadioButton> getRadioButtons() {
		return radioButtons;
	}

	public void addActionListener(ActionListener listener) {
		for (JRadioButton button : getRadioButtons().values()) {
			button.addActionListener(listener);
		}
	}

	protected void addActionListener(DefectStatus defectStatus, ActionListener listener) {
		getRadioButton(defectStatus).addActionListener(listener);
	}

	protected QicsClientConfig getClientConfig() {
		return clientConfig;
	}
}
