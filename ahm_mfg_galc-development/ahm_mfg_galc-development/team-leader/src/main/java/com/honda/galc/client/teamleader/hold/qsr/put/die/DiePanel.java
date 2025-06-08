package com.honda.galc.client.teamleader.hold.qsr.put.die;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.QsrMaintenanceFrame;
import com.honda.galc.client.teamleader.hold.qsr.put.die.listener.DieListener;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PropertiesMapping;
import com.honda.galc.entity.product.HoldParm;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DiePanel</code> is ... .
 * </p>
 * <h4>Usage and Example</h4> <h4>Special Notes</h4>
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
public class DiePanel extends TabbedPanel {

	private static final long serialVersionUID = 1L;

	public static final String SET_HOLD_COMMAND = "set";
	public static final String RELEASE_HOLD_COMMAND = "release";
	public static final String SELECT_ACTIVE_HOLDS_COMMAND = "active";
	public static final String SELECT_INACTIVE_HOLDS_COMMAND = "inactive";

	private InputPanel inputPanel;
	private ObjectTablePane<HoldParm> holdDataTable;
	private JPanel holdDataPanel;

	private DieListener controller;

	boolean refreshData;

	public DiePanel(QsrMaintenanceFrame mainWindow) {
		super("Die Hold", KeyEvent.VK_D, mainWindow);
		this.controller = new DieListener(this);
		initView();
		mapActions();
		initData();
	}

	@Override
	public void onTabSelected() {
		if (isRefreshData()) {
			getController().actionPerformed(new ActionEvent(getInputPanel().getAssociateIdTextField(), ActionEvent.ACTION_PERFORMED, getInputPanel().getAssociateIdTextField().getText()));
			getInputPanel().getSelectHoldsButton().doClick();
			setRefreshData(false);
		}
	}

	public void actionPerformed(ActionEvent arg0) {
	}

	protected void initView() {
		setName("DieHoldPanel");
		setLayout(new MigLayout("insets 0 0 0 2, gap 0", "[max,fill]", ""));
		this.inputPanel = createInputPanel();
		this.holdDataTable = createHoldDataTable();
		this.holdDataPanel = createHoldDataPanel();
		add(getInputPanel(), "wrap");
		add(getHoldDataPanel(), "height 540");
	}

	protected void mapActions() {
		getInputPanel().getDepartmentComboBox().addActionListener(getController());
		getInputPanel().getProductTypeComboBox().addActionListener(getController());
		getInputPanel().getAssociateIdTextField().addActionListener(getController());
		getInputPanel().getHoldButton().addActionListener(getController());
		getInputPanel().getSelectHoldsButton().addActionListener(getController());
		getHoldDataTable().getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (isActiveHoldsDisplayed() && getHoldDataTable().getTable().getSelectedRowCount() > 0) {
					getInputPanel().getHoldButton().setActionCommand(RELEASE_HOLD_COMMAND);
					getInputPanel().getHoldButton().setText(InputPanel.RELEASE_HOLD_LABEL);
				} else {
					getInputPanel().getHoldButton().setActionCommand(SET_HOLD_COMMAND);
					getInputPanel().getHoldButton().setText(InputPanel.SET_HOLD_LABEL);
				}
			}
		});
	}

	protected void initData() {

		String[] reasons = Config.getProperty().getDieHoldReasons();
		for (String str : reasons) {
			getInputPanel().getHoldReasonComboBox().addItem(str);
		}
		getInputPanel().getAssociateIdTextField().setText(getMainWindow().getUserId());
		setRefreshData(true);
		if (getInputPanel().getDepartmentComboBox().getItemCount() > 0) {
			getInputPanel().getDepartmentComboBox().setSelectedIndex(0);
		}
	}

	protected boolean isActiveHoldsDisplayed() {
		if (SELECT_INACTIVE_HOLDS_COMMAND.equals(getInputPanel().getSelectHoldsButton().getActionCommand())) {
			return true;
		}
		return false;
	}

	public static TitledBorder createTitledBorder(String title) {
		return new TitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), title);
	}

	public static String getDateFormat() {
		return "yyyy-MM-dd";
	}

	// === factory methods === //
	public InputPanel createInputPanel() {
		return new InputPanel(Config.getProperty().getDieIds(), Config.getProperty().getCoreIds());
	}

	public JPanel createHoldDataPanel() {
		JPanel panel = new JPanel();
		panel.setName("holdDataPanel");
		panel.setBorder(createTitledBorder(InputPanel.ACTIVE_HOLDS_LABEL));
		panel.setLayout(new GridLayout(1, 1));
		panel.add(getHoldDataTable());
		return panel;
	}

	protected ObjectTablePane<HoldParm> createHoldDataTable() {

		SimpleDateFormat timestampFormat = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
		timestampFormat.applyPattern("yyyy-MM-dd HH:mm:ss");

		PropertiesMapping mapping = new PropertiesMapping();
		mapping.put("ID", "holdId");
		mapping.put("QSR", "qsrId");
		mapping.put("Dept", "department");
		mapping.put("Model", "modelCode");
		mapping.put("Machine", "machineNumber");
		mapping.put("Die", "dieNumber");
		mapping.put("Core", "coreNumber");
		mapping.put("Hold Reason", "holdReason");
		mapping.put("Hold Start", "startDate");
		mapping.put("Hold Stop", "stopDate");
		mapping.put("Associate ID", "holdAssociateId");
		mapping.put("Associate Name", "holdAssociateName");
		mapping.put("Timestamp", "actualTimestamp", timestampFormat);

		ObjectTablePane<HoldParm> dataTable = new ObjectTablePane<HoldParm>(mapping.get(), true, true);
		dataTable.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return dataTable;
	}

	// === get/set === //
	protected DieListener getController() {
		return controller;
	}

	public InputPanel getInputPanel() {
		return inputPanel;
	}

	public ObjectTablePane<HoldParm> getHoldDataTable() {
		return holdDataTable;
	}

	public JPanel getHoldDataPanel() {
		return holdDataPanel;
	}

	protected boolean isRefreshData() {
		return refreshData;
	}

	protected void setRefreshData(boolean refreshData) {
		this.refreshData = refreshData;
	}
}
