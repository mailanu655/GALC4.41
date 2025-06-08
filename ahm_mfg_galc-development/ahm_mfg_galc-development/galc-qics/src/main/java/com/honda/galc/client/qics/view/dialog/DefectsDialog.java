package com.honda.galc.client.qics.view.dialog;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import com.honda.galc.client.qics.controller.QicsController;
import com.honda.galc.client.qics.view.constants.ActionId;
import com.honda.galc.client.qics.view.fragments.ButtonPanel;
import com.honda.galc.client.qics.view.frame.QicsFrame;
import com.honda.galc.client.qics.view.util.DefectTableCellRenderer;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.entity.qics.DefectResult;

/**
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>DefectsDialog</code> is ...
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
 * <TD>Apr 9, 2008</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Karol Wozniak
 * @deprecated  Replaced by functionality of <code>DefectRepairPanel</code>.
 */
public class DefectsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel dialogPane;
	private ObjectTablePane<DefectResult>  defectPane;
	private ButtonPanel buttonPanel;
	private QicsFrame qicsFrame;
	private ActionId[] actions;

	public DefectsDialog(QicsFrame frame, String title, ActionId[] actions) {
		super(frame, title);
		this.qicsFrame = frame;
		this.actions = actions;
		initialize();
	}

	protected void initialize() {
		try {
			setSize(980, 600);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setFont(Fonts.DIALOG_BOLD_12);
			setModal(true);
			setContentPane(getDialogPane());
			setResizable(false);
			mapActions();
			mapHandlers();
			resetButtons();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected JPanel getDialogPane() {
		if (dialogPane == null) {
			dialogPane = new JPanel();
			dialogPane.setLayout(null);

			dialogPane.add(getDefectPane());
			dialogPane.add(getButtonPanel());

		}
		return dialogPane;
	}

	public ObjectTablePane<DefectResult> getDefectPane() {
		if (defectPane == null) {
			int width = getWidth() - 40;
			int height = getHeight() - 130;
			ColumnMappings columnMappings = 
				ColumnMappings.with("Status","defectStatus").put("Defect","defectTypeName")
							  .put("part","inspectPart").put("Location","inspectionPartLocation")
							  .put("Secondary Part","secondaryPart").put("TWO PART PAIR","twoPartPair")
							  .put("Two Pair Part Loc'n","twoPairPartLocation")
				              .put("Responsible Department","responsibleDepartment")
				              .put("Entry Station","entryStation");
				              
			defectPane = new ObjectTablePane<DefectResult> (columnMappings.get());
//			defectPane.setHeaders(new String[] { STATUS.getColumnName(), DEFECT.getColumnName(), PART.getColumnName(), LOCATION.getColumnName(), SECONDARY_PART.getColumnName(),
//					TWO_PAIR_PART.getColumnName(), TWO_PAIR_PART_LOCATION.getColumnName(), RESPONSIBLE_DEPARTMENT.getColumnName(), ENTRY_STATION.getColumnName() });
			defectPane.setSize(width, height);
			defectPane.setLocation(20, 30);
			defectPane.getTable().setDefaultRenderer(Object.class, new DefectTableCellRenderer(getQicsController().getClientConfig()));
			defectPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			defectPane.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			defectPane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setColumnSize(defectPane.getTable());
		}
		return defectPane;
	}

	protected void setColumnSize(JTable table) {
		DefaultTableColumnModel cModel = (DefaultTableColumnModel) table.getColumnModel();
		double totalWidth = 1200;
		double[] width = { 8, 18, 14, 10, 10, 10, 10, 10, 10 };
		for (int i = 0; i < width.length; i++) {
			TableColumn column = cModel.getColumn(i);
			int w = (int) (totalWidth * (width[i] / 100));
			column.setPreferredWidth(w);
		}
	}

	protected QicsController getQicsController() {
		return ((QicsFrame) qicsFrame).getQicsController();
	}

	protected QicsFrame getQicsFrame() {
		return qicsFrame;
	}

	protected void setQicsFrame(QicsFrame qicsFrame) {
		this.qicsFrame = qicsFrame;
	}

	protected ButtonPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new ButtonPanel(Arrays.asList(this.actions)) {
				private static final long serialVersionUID = 1L;

				@Override
				public void setSize() {
					setHorizontalSize();
				}

				@Override
				public void setLayout() {
					setHorizontalLayout();
				}
			};
			buttonPanel.setLocation(getDefectPane().getX() + getDefectPane().getWidth() / 2 - buttonPanel.getWidth() / 2, getDefectPane().getY() + getDefectPane().getHeight() + 10);
		}
		return buttonPanel;
	}

	public void loadData(List<DefectResult> data) {
		getDefectPane().removeData();
		if (data == null) {
			return;
		}
		getDefectPane().reloadData(data);
		resetButtons();
	}

	protected void mapActions() {
		if (getButtonPanel() != null) {
			mapButtonActions(getButtonPanel().getButtons());
		}
	}

	protected void mapHandlers() {
		getDefectPane().getTable().getSelectionModel().addListSelectionListener(new DefectSelectionHandler());
	}

	protected void mapButtonActions(Map<ActionId, JButton> buttons) {
		if (buttons == null || buttons.isEmpty()) {
			return;
		}
		for (Entry<ActionId, JButton> entry : buttons.entrySet()) {
			ActionId actionId = entry.getKey();
			JButton button = entry.getValue();
			if (button != null) {
				Action action = createAction(actionId);// getActionFactory().createAction(actionId);
				button.setAction(action);
			}
		}
	}

	public void resetButtons() {

		if (getButtonPanel().getButtons().size() == 1) {
			return;
		}

		boolean selectedItems = !getDefectPane().getSelectedItems().isEmpty();
		boolean tableEmtpy = getDefectPane().getTable().getRowCount() == 0;

		JButton voidAll = getButtonPanel().getButton(ActionId.VOID_ALL);
		JButton voidSelected = getButtonPanel().getButton(ActionId.VOID_SELECTED);
		JButton changeResponsible = getButtonPanel().getButton(ActionId.CHANGE_RESPONSIBLE);

		if (voidAll != null) {
			voidAll.setEnabled(!selectedItems && !tableEmtpy);
		}
		if (voidSelected != null) {
			voidSelected.setEnabled(selectedItems);
		}
		if (changeResponsible != null) {
			changeResponsible.setEnabled(selectedItems);
		}
	}

	public void changeResponsible() {
		final DefectResult repairData = getDefectPane().getSelectedItem();
		final OrgUnitSelectDialog responsibleDialog = new OrgUnitSelectDialog(this, getQicsController(), "Change Responsible Data");
		responsibleDialog.setSelectedDepartment(repairData.getResponsibleDept());
		responsibleDialog.setSelectedLine(repairData.getResponsibleLine());
		responsibleDialog.setSelectedZone(repairData.getResponsibleZone());
		responsibleDialog.getOkButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Use Department, Line and Zone Name instead of ID
				String department = responsibleDialog.getSelectedDepartmentName();
				String line = responsibleDialog.getSelectedLineName();
				String zone = responsibleDialog.getSelectedZoneName();
				repairData.setResponsibleDept(department);
				repairData.setResponsibleLine(line);
				repairData.setResponsibleZone(zone);
				responsibleDialog.dispose();
			}
		});
		responsibleDialog.setLocationRelativeTo(qicsFrame);
		responsibleDialog.setVisible(true);
	}

	protected Action createAction(ActionId actionId) {
		if (ActionId.VOID_ALL.equals(actionId)) {
			return new VoidAllAction();
		} else if (ActionId.VOID_SELECTED.equals(actionId)) {
			return new VoidSelectedAction();
		} else if (ActionId.CHANGE_RESPONSIBLE.equals(actionId)) {
			return new ChangeResponsibleAction();
		} else {
			return new ReturnAction();
		}
	}

	class BaseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public BaseAction(String actionName, int keyEvent) {
			putValue(Action.NAME, actionName);
			putValue(Action.MNEMONIC_KEY, keyEvent);
		}

		public void execute(ActionEvent e) {
		}

		public void actionPerformed(ActionEvent e) {
			try {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				execute(e);
			} finally {
				resetButtons();
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}

	class VoidAllAction extends BaseAction {
		private static final long serialVersionUID = 1L;

		public VoidAllAction() {
			super("Void All", KeyEvent.VK_A);
		}

		@Override
		public void execute(ActionEvent e) {
			qicsFrame.getQicsController().getProductModel().clearNewDefects();
			getDefectPane().removeData();
		}
	}

	class VoidSelectedAction extends BaseAction {
		private static final long serialVersionUID = 1L;

		public VoidSelectedAction() {
			super("Void Selected", KeyEvent.VK_S);
		}

		@Override
		public void execute(ActionEvent e) {
			List<DefectResult> selectedObjects = getDefectPane().getSelectedItems();
			for (DefectResult item : selectedObjects) {
				qicsFrame.getQicsController().getProductModel().removeDefect(item);
			}
			loadData(qicsFrame.getQicsController().getProductModel().getNewDefects());
		}
	}

	class ChangeResponsibleAction extends BaseAction {
		private static final long serialVersionUID = 1L;

		public ChangeResponsibleAction() {
			super("Change Resp", KeyEvent.VK_C);
		}

		@Override
		public void execute(ActionEvent e) {
			changeResponsible();
			getDefectPane().clearSelection();
			getDefectPane().repaint();
		}
	}

	class ReturnAction extends BaseAction {
		private static final long serialVersionUID = 1L;

		public ReturnAction() {
			super("Return", KeyEvent.VK_R);
		}

		@Override
		public void execute(ActionEvent e) {
			DefectsDialog.this.dispose();
		}
	}

	class DefectSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			try {
				getQicsFrame().setWaitCursor();
				resetButtons();
			} finally {
				getQicsFrame().setDefaultCursor();
			}
		}
	}
}
