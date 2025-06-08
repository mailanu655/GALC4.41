package com.honda.galc.client.qics.view.screen;

import javax.swing.JButton;

import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.fragments.ActionButtonsPanel;
import com.honda.galc.client.qics.view.fragments.DefectStatusPanel;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;

/**
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * Absract panel for qics input defect screen.
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
public abstract class DefectInputPanel extends QicsPanel {

	private static final long serialVersionUID = 1L;
	private DefectStatusPanel defectStatusPanel;
	private ActionButtonsPanel actionButtonsPanel;

	public DefectInputPanel(QicsFrame frame) {
		super(frame);
	}

	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());

		defectStatusPanel = createDefectStatusPanel();
		actionButtonsPanel = createActionButtonsPanel();

		add(getDefectStatusPanel());
		add(getActionButtonsPanel());

		mapActions();
		mapEventHandlers();
	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
	}

	protected void setDefaultDefectStatusSelected() {
		getDefectStatusPanel().getDefaultRadioButton().setSelected(true);
	}

	public DefectStatusPanel getDefectStatusPanel() {
		return defectStatusPanel;
	}

	@Override
	protected ActionButtonsPanel getActionButtonsPanel() {
		return actionButtonsPanel;
	}

	// === factory methods === //
	protected DefectStatusPanel createDefectStatusPanel() {
		DefectStatusPanel panel = new DefectStatusPanel(getClientConfig(), 230);
		panel.setLocation(765, 358);
		return panel;
	}

	protected ActionButtonsPanel createActionButtonsPanel() {
		ActionButtonsPanel panel = new ActionButtonsPanel(getActionButtonCodes());
		panel.setLocation(getDefectStatusPanel().getX(), getDefectStatusPanel().getY() + getDefectStatusPanel().getHeight() + 2);
		panel.setSize(getDefectStatusPanel().getWidth(), getHeight() - panel.getY() - 1);
		return panel;
	}

	
	protected ObjectTablePane<String> createTablePane(String title, String columnName,boolean supportUnselect) {
		ObjectTablePane<String> panel = new ObjectTablePane<String>(title, new String[]{columnName},false,supportUnselect);
		if (getClientConfig().isScreenTouch()) {
			panel.getTable().setRowHeight((int) (panel.getTable().getRowHeight() * getClientConfig().getScreenTouchFactor()));
		}
		return panel;
	}
	
	protected <E> ObjectTablePane<E> createTablePane(String title, ColumnMappings columnMappings, boolean supportUnselect) {
		ObjectTablePane<E> panel = new ObjectTablePane<E>(title, columnMappings.get(), false, supportUnselect);
		if (getClientConfig().isScreenTouch()) {
			panel.getTable().setRowHeight((int) (panel.getTable().getRowHeight() * getClientConfig().getScreenTouchFactor()));
		}
		return panel;
	}

	// ================= event handlers mappings ========================//
	@Override
	protected void mapEventHandlers() {
	}

	// ============== end of event handlers mappings ====================//

	public JButton getAcceptButton() {
		return getActionButtonsPanel().getButton(ActionId.ACCEPT_NEW_DEFECT);
	}
}
