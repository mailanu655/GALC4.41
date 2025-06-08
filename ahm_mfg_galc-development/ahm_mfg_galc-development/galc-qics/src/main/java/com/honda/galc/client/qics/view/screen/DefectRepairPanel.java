package com.honda.galc.client.qics.view.screen;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.qics.view.action.CancelAllNewDefectsAction;
import com.honda.galc.client.qics.view.action.CancelSelectedNewDefectAction;
import com.honda.galc.client.qics.view.action.ChangeResponsibleAction;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.constants.QicsViewId;
import com.honda.galc.client.qics.view.fragments.ActionButtonsPanel;
import com.honda.galc.client.qics.view.fragments.DefectRepairInputPanel;
import com.honda.galc.client.qics.view.fragments.DefectStatusPanel;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.exception.SystemException;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.qics.DefectActualProblem;
import com.honda.galc.entity.qics.DefectResult;

public class DefectRepairPanel extends QicsPanel implements ListSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;

	protected DefectStatusPanel defectStatusPanel;
	protected ActionButtonsPanel actionButtonsPanel;
	protected ObjectTablePane<DefectResult> defectPane;
	protected DefectRepairInputPanel inputPane;
	protected JPopupMenu defectTablePopupMenu;

	public DefectRepairPanel(QicsFrame frame) {
		super(frame);
		initialize();
	}

	@Override
	public QicsViewId getQicsViewId() {
		return QicsViewId.REPAIR;
	}

	protected void initialize() {
		setLayout(null);
		setSize(getTabPaneWidth(), getTabPaneHeight());

		defectPane = createDefectPane();
		inputPane = createInputPane();

		defectStatusPanel = createDefectStatusPanel();
		actionButtonsPanel = createActionButtonsPanel();

		defectTablePopupMenu = createDefectTablePopupMenu();

		add(getDefectPane());
		add(getInputPane());

		add(getDefectStatusPanel());
		add(getActionButtonsPanel());

		setComponentZOrder(getInputPane(), 0);

		mapActions();
		mapEventHandlers();
	}

	// === controlling api ===//
	@Override
	public void startPanel() {
		resetDefectTable();
		resetInput();

		getDefectStatusPanel().setEnabled(false);
		getInputPane().setEnabled(false);
		setButtonsState();

	}

	@Override
	public void setButtonsState() {
		super.setButtonsState();
		getQicsFrame().getMainPanel().setButtonsState();
		setAcceptButtonEnabled(false);
	}

	public void setAcceptButtonEnabled(boolean enabled) {
		getAcceptButton().setEnabled(enabled);
	}

	public void setAcceptButtonEnabled() {
		boolean inputUpdated = isInputDirty();
		getAcceptButton().setEnabled(inputUpdated);
	}

	public void setDefectStatusEnabled(boolean enabled) {
		getDefectStatusPanel().setEnabled(enabled);
	}

	public boolean isDefectSelected() {
		boolean selected = getDefectPane().getTable().getSelectedRowCount() > 0;
		return selected;
	}

	public void setInput() {
		DefectResult repairResultData = getSelectedItem();
		if (repairResultData == null) {
			return;
		}
		getDefectStatusPanel().setSelectedDefectStatus(repairResultData.getDefectStatusValue());
		getInputPane().setInput(repairResultData);
	}

	public void resetInput() {
		getDefectStatusPanel().resetInput();
		getInputPane().resetInput();
	}

	public boolean isInputDirty() {
		DefectResult repairResultData = getSelectedItem();
		if (repairResultData == null) {
			return false;
		}

		boolean repairInputUpdated = false;
		if (getDefectStatusPanel().getSelectedStatus() == DefectStatus.REPAIRED.getId()) {
			repairInputUpdated = getInputPane().isInputDirty(repairResultData);
		}
		boolean defectStatusUpdated = getDefectStatusPanel().getSelectedStatus() != repairResultData.getDefectStatusValue();
		boolean dirty = repairInputUpdated || defectStatusUpdated;
		return dirty;
	}

	public void resetDefectTable() {

		List<DefectResult> totalDefects = new ArrayList<DefectResult>();

		if (getProductModel().getExistingDefects() != null && getProductModel().getExistingDefects().size() > 0) {
			totalDefects.addAll(getProductModel().getExistingDefects());
		}

		if (getProductModel().getNewDefects() != null && getProductModel().getNewDefects().size() > 0) {
			totalDefects.addAll(getProductModel().getNewDefects());
		}

		getDefectPane().reloadData(totalDefects);
		getDefectPane().clearSelection();
	}

	public void initializeInputData(DefectResult repairResultData) {
		refreshAssociateNumbers();

		getInputPane().getActualProblemComboBox().setModel(new DefaultComboBoxModel());
		if(getQicsPropertyBean().isDefectActualProbRepairMethodEnabled())
		{
			List<DefectActualProblem> problems = null;
			try {
				problems = getQicsController().selectActualProblems(repairResultData);
			} catch (SystemException ex) {
				//			String messageId = ex.getMessageId();
				//			getQicsFrame().setErrorMessage(messageId);
			}

			if (problems == null || problems.isEmpty()) {
				return;
			}

			getInputPane().getActualProblemComboBox().setModel(new DefaultComboBoxModel(problems.toArray()));
			getInputPane().getActualProblemComboBox().setSelectedIndex(-1);
		}
	}

	// === get/set === //
	public ObjectTablePane<DefectResult> getDefectPane() {
		return defectPane;
	}

	public DefectRepairInputPanel getInputPane() {
		return inputPane;
	}

	public DefectStatusPanel getDefectStatusPanel() {
		return defectStatusPanel;
	}

	protected JPopupMenu getDefectTablePopupMenu() {
		return defectTablePopupMenu;
	}

	protected void setDefectTablePopupMenu(JPopupMenu defectTablePopupMenu) {
		this.defectTablePopupMenu = defectTablePopupMenu;
	}

	@Override
	public ActionButtonsPanel getActionButtonsPanel() {
		return actionButtonsPanel;
	}

	// ============== factory methods for ui elements

	protected ObjectTablePane<DefectResult> createDefectPane() {
		int width = 1000;
		int height = 335;
		
		ColumnMappings columnMappings = 
			ColumnMappings.with("#", "row").put("Defect Status", "defectStatus")
						  .put("New", Boolean.class,"newDefect").put("Defect", "defectTypeName")
						  .put("Part", "inspectionPartName").put("Loc'n", "inspectionPartLocationName")
						  .put("Secondary Part", "secondaryPartName").put("Resp. Dept.", "responsibleDept")
						  .put("Resp. Line", "responsibleLine").put("Resp. Zone", "responsibleZone")
						  .put("Entry Station", "entryStation");

		ObjectTablePane<DefectResult> pane = new ObjectTablePane<DefectResult>(columnMappings.get(),true);
		pane.setSize(width, height);
		pane.setTitle("Defect Result");
		pane.setLocation(0, 0);
		pane.setCellRenderer(1,createDefectTableCellRenderer());
		pane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		if (getClientConfig().isScreenTouch()) {
			pane.getTable().setRowHeight((int) (pane.getTable().getRowHeight() * getClientConfig().getScreenTouchFactor()));
		}

		setColumnSize(pane);
		return pane;
	}

	protected void setColumnSize(ObjectTablePane<DefectResult> pane) {
		Map<String, Integer> columnWidths = new HashMap<String, Integer>();
		columnWidths.put("#", 30);
		columnWidths.put("Defect Status", 100);
		columnWidths.put("New", 40);
		columnWidths.put("Defect", 200);
		columnWidths.put("Part", 200);
		columnWidths.put("Resp. Line", 200);
		columnWidths.put("Entry Station", 220);
//		pane.setColumnSize(columnWidths, defaultWidth);
	}

	protected JPopupMenu createDefectTablePopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Void");
		menuItem.setName("Void");
		menuItem.addActionListener(new CancelSelectedNewDefectAction(this));
		popup.add(menuItem);

		menuItem = new JMenuItem("Void All");
		menuItem.setName("Void All");
		menuItem.addActionListener(new CancelAllNewDefectsAction(this));
		popup.add(menuItem);
		if (!(getQicsController().getQicsPropertyBean().isChangeResponsibleDeptDisabled()))
		{
			popup.addSeparator();
			menuItem = new JMenuItem("Change Responsible");
			menuItem.setName("Change Responsible");
			menuItem.addActionListener(new ChangeResponsibleAction(this));
			popup.add(menuItem);
		}
		return popup;
	}

	public DefectRepairInputPanel createInputPane() {
		DefectRepairInputPanel panel = new DefectRepairInputPanel(this,765, 165);
		panel.setLocation(0, getDefectPane().getY() + getDefectPane().getHeight());
		panel.getAssociateNumberComboBox().setEditable(getQicsPropertyBean().isOverrideRepairAssociateEnabled());
		return panel;
	}

	public DefectStatusPanel createDefectStatusPanel() {
		DefectStatusPanel panel = new DefectStatusPanel(getClientConfig(), 230);
		panel.setLocation(getInputPane().getX() + getInputPane().getWidth(), getInputPane().getY() + 23);
		return panel;
	}

	public ActionButtonsPanel createActionButtonsPanel() {

		ActionButtonsPanel panel = new ActionButtonsPanel(getActionButtonCodes());
		panel.setLocation(getDefectStatusPanel().getX(), getDefectStatusPanel().getY() + getDefectStatusPanel().getHeight() + 2);
		panel.setSize(getDefectStatusPanel().getWidth(), getHeight() - panel.getY() - 1);

		return panel;
	}

	// === panel buttons === //
	@Override
	protected Collection<ActionId> getActionButtonCodes() {
		return Arrays.asList(new ActionId[] { ActionId.ACCEPT_REPAIR });
	}


	
	public DefectResult getSelectedItem() {
		
		return getDefectPane().getSelectedItem();
		
	}

	public JButton getAcceptButton() {
		return getActionButtonsPanel().getButton(ActionId.ACCEPT_REPAIR);
	}

	// ================= event handlers mappings ========================//
	@Override
	protected void mapEventHandlers() {
		getDefectPane().getTable().getSelectionModel().addListSelectionListener(this);
		getDefectPane().getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				showPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				showPopupMenu(e);
			}

			protected void showPopupMenu(MouseEvent e) {
				if (e.isPopupTrigger()) {
					if (!getQicsController().isProductProcessable()) {
						return;
					}
					DefectResult selectedValue = getDefectPane().getSelectedItem();
					if (selectedValue == null) {
						return;
					}
					getDefectTablePopupMenu().getSubElements()[0].getComponent().setEnabled(selectedValue.isNewDefect());
					getDefectTablePopupMenu().getSubElements()[1].getComponent().setEnabled(selectedValue.isNewDefect());
					getDefectTablePopupMenu().show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});

		getDefectStatusPanel().addActionListener(this);
	}
	
	protected void deselected(ListSelectionModel model) {
		if (!getQicsController().isProductProcessable()) {
			return;
		}
		resetInput();
		setDefectStatusEnabled(false);
		getInputPane().setEnabled(false);
		setButtonsState();
	}
	
	protected void selected(ListSelectionModel model) {
		if (!getQicsController().isProductProcessable()) {
			return;
		}

		DefectResult repairResultData = getSelectedItem();
		if (repairResultData == null) {
			return;
		}

		boolean defectUpdatable = getQicsController().isDefectUpdatable(repairResultData);
		boolean repairInputEnabled = defectUpdatable && repairResultData.isRepairedStatus();

		getInputPane().setEnabled(repairInputEnabled);

		initializeInputData(repairResultData);
		setInput();
		setDefectStatusEnabled(defectUpdatable);
		getInputPane().setEnabled(repairInputEnabled);
		getLogger().info("Defect "+repairResultData.getDefectTypeName()+" is selected");
		 
		setButtonsState();
	}

	public void actionPerformed(ActionEvent e) {
		try {

			getQicsFrame().setWaitCursor();
			getQicsFrame().clearMessage();

			DefectResult repairResultData = getSelectedItem();
			if (repairResultData == null) return;

			DefectStatus selectedDefectStatus = DefectStatus.getType(getDefectStatusPanel().getSelectedStatus());

			boolean repairInputEnabled = selectedDefectStatus.isRepaired();

			getInputPane().setEnabled(repairInputEnabled);
			if (repairInputEnabled) {
				if (getInputPane().getActualProblemComboBox().getItemCount() > 0) {
					getInputPane().getActualProblemComboBox().setSelectedIndex(0);
				}
			} else {
				getInputPane().getActualProblemComboBox().setSelectedIndex(-1);
			}
			
			setAcceptButtonEnabled();

		} finally {
			getQicsFrame().setDefaultCursor();
		}
	}

	public void refreshAssociateNumbers() {
		List<String> associateNumbers = getQicsController().getAssociateNumbers();
		getInputPane().getAssociateNumberComboBox().setModel(new DefaultComboBoxModel(associateNumbers.toArray()));
		if (getInputPane().getAssociateNumberComboBox().getModel().getSize() > 0) {
			getInputPane().getAssociateNumberComboBox().setSelectedItem(getQicsFrame().getUserId());
		}
	}
}
