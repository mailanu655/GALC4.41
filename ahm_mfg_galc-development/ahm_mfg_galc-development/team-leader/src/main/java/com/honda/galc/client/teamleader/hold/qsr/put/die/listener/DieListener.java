package com.honda.galc.client.teamleader.hold.qsr.put.die.listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.view.UiUtils;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrCreatedEvent;
import com.honda.galc.client.teamleader.hold.qsr.event.QsrEvent;
import com.honda.galc.client.teamleader.hold.qsr.put.die.DiePanel;
import com.honda.galc.client.teamleader.hold.qsr.put.die.InputPanel;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.User;
import com.honda.galc.entity.enumtype.QsrStatus;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.HoldParm;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.Qsr;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>DieListener</code> is ... .
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
public class DieListener extends BaseListener<DiePanel> implements ActionListener {

	public DieListener(DiePanel parentPanel) {
		super(parentPanel);
	}

	@Override
	protected void executeActionPerformed(ActionEvent e) {

		if (e.getSource() == getView().getInputPanel().getDepartmentComboBox()) {
			handleDepartmentSelected();
			return;
		}

		if (e.getSource() == getView().getInputPanel().getProductTypeComboBox()) {
			handleProductTypeSelected();
			return;
		}

		if (e.getSource() == getView().getInputPanel().getAssociateIdTextField()) {
			handleAssociateIdUpdated();
			return;
		}
		if (e.getSource() == getView().getInputPanel().getHoldButton()) {
			String cmd = getView().getInputPanel().getHoldButton().getActionCommand();
			if (DiePanel.SET_HOLD_COMMAND.equals(cmd)) {
				handleCreateDieHold();
			} else if (DiePanel.RELEASE_HOLD_COMMAND.equals(cmd)) {
				handleReleaseDieHold();
			}
			return;
		}

		if (e.getSource() == getView().getInputPanel().getSelectHoldsButton()) {
			String cmd = getView().getInputPanel().getSelectHoldsButton().getActionCommand();
			if (DiePanel.SELECT_ACTIVE_HOLDS_COMMAND.equals(cmd)) {
				selectActiveHoldsParams();
			} else if (DiePanel.SELECT_INACTIVE_HOLDS_COMMAND.equals(cmd)) {
				selectInactiveHoldParams();
			}
			return;
		}
	}

	// === handlers === //
	protected void handleDepartmentSelected() {

		getView().getInputPanel().getProductTypeComboBox().removeAllItems();

		Division division = (Division) getView().getInputPanel().getDepartmentComboBox().getSelectedItem();
		if (division == null) {
			String msg = "There is no defined DIVISION_MACHINE_ID_MAPPING property, please review configuration";
			getView().getMainWindow().setErrorMessage(msg);
			return;
		}

		List<ProductType> productTypes = Config.getInstance().getDiecastProductTypes(division);
		if (productTypes != null && !productTypes.isEmpty()) {
			getView().getInputPanel().getProductTypeComboBox().setModel(new DefaultComboBoxModel(new Vector<ProductType>(productTypes)));
			getView().getInputPanel().getProductTypeComboBox().setSelectedIndex(0);
		}

		String[] ids = Config.getMachineIds(division);
		for (int i = 0; i < getView().getInputPanel().getMachineSelectionList().size(); ++i) {
			JRadioButton button = getView().getInputPanel().getMachineSelectionList().get(i);
			if (ids != null && i < ids.length) {
				button.setText(ids[i]);
				button.setEnabled(true);
				button.setVisible(true);
			} else {
				button.setText("");
				button.setEnabled(false);
				button.setVisible(false);
			}
		}
	}

	protected void handleProductTypeSelected() {
		getView().getInputPanel().getModelCodeComboBox().removeAllItems();
		ProductType productType = (ProductType) getView().getInputPanel().getProductTypeComboBox().getSelectedItem();
		if (productType == null) {
			return;
		}

		List<? extends BaseProductSpec> productSpecs = ProductTypeUtil.getProductSpecDao(productType).findAllProductSpecCodesOnly(productType.name());
		if (productSpecs != null && !productSpecs.isEmpty()) {
			Set<String> set = new TreeSet<String>();
			for (BaseProductSpec spec : productSpecs) {
				if (spec instanceof ProductSpec) {
					String modelCode = ((ProductSpec) spec).getModelCode();
					if (StringUtils.isBlank(modelCode)) {
						continue;
					}
					if (!set.contains(modelCode)) {
						set.add(modelCode);
					}
				}
			}
			getView().getInputPanel().getModelCodeComboBox().setModel(new DefaultComboBoxModel(new Vector<String>(set)));
		}
		getView().getInputPanel().getModelCodeComboBox().setSelectedIndex(-1);
		//getView().getInputPanel().getMachineGroup().clearSelection(); since 1.5 support will be dropped, for now :
		getView().getInputPanel().getMachineNoSelection().setSelected(true);

		UiUtils.setSelected(getView().getInputPanel().getDieSelectionList(), false);
		UiUtils.setSelected(getView().getInputPanel().getCoreSelectionList(), false);
		String[] coreProductTypes = Config.getProperty().getCoreProductTypes();
		boolean coreProductTypeInd = false;
		if (coreProductTypes != null && Arrays.asList(coreProductTypes).contains(productType.name())) {
			coreProductTypeInd = true;
		}
		UiUtils.setEnabled(getView().getInputPanel().getCoreSelectionList(), coreProductTypeInd);
	}

	protected void handleAssociateIdUpdated() {
		getView().getInputPanel().getAssociateNameTextField().setText("");
		String userId = getView().getInputPanel().getAssociateIdTextField().getText().trim();
		if (userId == null || userId.trim().length() == 0) {
			JOptionPane.showMessageDialog(getView(), "User id can not be empty", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		userId = userId.trim();
		User user = getUserDao().findByKey(userId);
		if (user == null) {
			JOptionPane.showMessageDialog(getView(), String.format("User with id '%s' does not exist", userId), "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		getView().getInputPanel().getAssociateNameTextField().setText(user.getUserName());
	}

	protected void handleCreateDieHold() {
		handleAssociateIdUpdated();
		String msg = validateCreate();
		if (msg != null && msg.trim().length() > 0) {
			JOptionPane.showMessageDialog(getView(), msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int retCode = JOptionPane.showConfirmDialog(getView(), "Are you sure You want to create Die Hold ?", "Create Die Hold", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			saveHoldParamData();
		} finally {
			selectActiveHoldsParams();
		}
	}

	public void handleReleaseDieHold() {
		List<HoldParm> list = getView().getHoldDataTable().getSelectedItems();
		if (list == null || list.isEmpty()) {
			return;
		}
		handleAssociateIdUpdated();
		String msg = validateRelease();
		if (msg != null && msg.trim().length() > 0) {
			JOptionPane.showMessageDialog(getView(), msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		msg = String.format("Are you sure You want to release %s Die Hold%s ?", list.size(), list.size() == 1 ? "" : "s");
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Release Die Hold", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			String associateId = getView().getInputPanel().getAssociateIdTextField().getText().trim();
			String associateName = getView().getInputPanel().getAssociateNameTextField().getText().trim();
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			for (HoldParm hp : list) {
				hp.setReleaseFlag((short) 1);
				hp.setReleaseTimestamp(timestamp);
				hp.setReleaseAssociateId(associateId);
				hp.setReleaseAssociateName(associateName);
				hp.setUpdateTimestamp(timestamp);
			}
			getHoldParamDao().updateAll(list);
		} finally {
			selectActiveHoldsParams();
		}
	}

	public void selectActiveHoldsParams() {

		getView().getHoldDataTable().clearSelection();

		Calendar date = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(DiePanel.getDateFormat());
		Date stopDate = Date.valueOf(format.format(date.getTime()));
		getHoldParamDao().releaseExpiredHolds(stopDate);

		List<HoldParm> list = getHoldParamDao().findAll(0);
		getView().getHoldDataTable().reloadData(list);

		getView().getHoldDataPanel().setBorder(DiePanel.createTitledBorder(InputPanel.ACTIVE_HOLDS_LABEL));
		getView().getHoldDataTable().getTable().setForeground(Color.BLACK);

		getView().getInputPanel().getSelectHoldsButton().setText(InputPanel.INACTIVE_HOLDS_LABEL);
		getView().getInputPanel().getSelectHoldsButton().setActionCommand(DiePanel.SELECT_INACTIVE_HOLDS_COMMAND);

	}

	public void selectInactiveHoldParams() {

		getView().getHoldDataTable().clearSelection();
		List<HoldParm> list = getHoldParamDao().findAll(1);
		getView().getHoldDataTable().reloadData(list);
		getView().getHoldDataPanel().setBorder(DiePanel.createTitledBorder(InputPanel.INACTIVE_HOLDS_LABEL));
		getView().getHoldDataTable().getTable().setForeground(Color.RED);
		getView().getInputPanel().getSelectHoldsButton().setText(InputPanel.ACTIVE_HOLDS_LABEL);
		getView().getInputPanel().getSelectHoldsButton().setActionCommand(DiePanel.SELECT_ACTIVE_HOLDS_COMMAND);
	}

	// === utility api === //
	protected String validateCreate() {
		StringBuilder sb = new StringBuilder();

		ProductType productType = (ProductType) getView().getInputPanel().getProductTypeComboBox().getSelectedItem();
		if (productType == null) {
			append(sb, "No ProductType selected");
		}

		String modelCode = (String) getView().getInputPanel().getModelCodeComboBox().getSelectedItem();
		if (StringUtils.isBlank(modelCode)) {
			append(sb, "No Model selected");
		}

		if (getView().getInputPanel().getSelectedMachines().isEmpty()) {
			append(sb, "No Machine selected");
		}
		if (getView().getInputPanel().getSelectedDies().isEmpty() && getView().getInputPanel().getSelectedCores().isEmpty()) {
			if (!getView().getInputPanel().getDieSelectionList().isEmpty() && !getView().getInputPanel().getCoreSelectionList().isEmpty()) {
				append(sb, "Die or Sand Core must be selected");
			} else if (!getView().getInputPanel().getCoreSelectionList().isEmpty()) {
				append(sb, "No Sand Core selected");
			} else {
				append(sb, "No Die selected");
			}
		}

		Calendar startDateCal = convertDate(getView().getInputPanel().getStartDateBean().getDate());
		Calendar stopDateCal = convertDate(getView().getInputPanel().getStopDateBean().getDate());
		if (startDateCal == null || stopDateCal == null) {
			append(sb, "Invalid Date(s).");
		} else if (startDateCal.after(stopDateCal)) {
			append(sb, "Invalid Date Range");
		}
		String associateId = getView().getInputPanel().getAssociateIdTextField().getText().trim();
		String associateName = getView().getInputPanel().getAssociateNameTextField().getText().trim();
		if (associateId.equals("") || associateName.equals("")) {
			append(sb, "Missing Associate ID/Associate Name");
		}

		String holdReason = (String) getView().getInputPanel().getHoldReasonComboBox().getSelectedItem();
		if (StringUtils.isBlank(holdReason)) {
			append(sb, "No Hold Reason selected");
		}

		return sb.toString();
	}

	protected StringBuilder append(StringBuilder sb, String str) {
		String delimiter = "\n";
		if (sb == null) {
			sb = new StringBuilder();
		}
		if (str == null || str.trim().length() == 0) {
			return sb;
		}
		if (sb.length() > 0 && delimiter != null) {
			sb.append(delimiter);
		}
		sb.append(str.trim());
		return sb;
	}

	protected Calendar convertDate(String aDate) {
		if(aDate != null && aDate.matches("([0-9]{8})")) {
			return new GregorianCalendar(Integer.parseInt(aDate.substring(0, 4)), Integer.parseInt(aDate.substring(4, 6)), Integer.parseInt(aDate.substring(6, 8)));
		} else {
			return null;
		}
	}

	public void saveHoldParamData() {
		Date startDate = Date.valueOf(getView().getInputPanel().getStartDateBean().getDate(DiePanel.getDateFormat()));
		Date stopDate = Date.valueOf(getView().getInputPanel().getStopDateBean().getDate(DiePanel.getDateFormat()));
		ProductType productType = (ProductType) getView().getInputPanel().getProductTypeComboBox().getSelectedItem();
		HoldParm dieHoldParam = new HoldParm();
		Division division = (Division) getView().getInputPanel().getDepartmentComboBox().getSelectedItem();
		String modelCode = (String) getView().getInputPanel().getModelCodeComboBox().getSelectedItem();
		dieHoldParam.setDepartment(division.getDivisionId());
		dieHoldParam.setModelCode(modelCode);
		dieHoldParam.setMachineNumber(getView().getInputPanel().getSelectedMachine());
		if (!getView().getInputPanel().getSelectedDies().isEmpty()) {
			dieHoldParam.setDieNumber(getView().getInputPanel().getSelectedDies().toString());
		}
		if (!getView().getInputPanel().getSelectedCores().isEmpty()) {
			dieHoldParam.setCoreNumber(getView().getInputPanel().getSelectedCores().toString());
		}
		dieHoldParam.setStartDate(startDate);
		dieHoldParam.setStopDate(stopDate);
		dieHoldParam.setHoldReason(getView().getInputPanel().getHoldReasonComboBox().getSelectedItem().toString());
		dieHoldParam.setHoldAssociateId(getView().getInputPanel().getAssociateIdTextField().getText().trim());
		dieHoldParam.setHoldAssociateName(getView().getInputPanel().getAssociateNameTextField().getText().trim());
		dieHoldParam.setReleaseFlag((short) 0);

		StringBuffer qsrDescription = new StringBuffer();
		qsrDescription.append("Die Hold - ");
		qsrDescription.append("Machine{" + dieHoldParam.getMachineNumber() + "} ");
		if (dieHoldParam.getDieNumber() != null) {
			qsrDescription.append("Die{").append(dieHoldParam.getDieNumber()).append("} ");
		}
		if (dieHoldParam.getCoreNumber() != null) {
			qsrDescription.append("Core{" + dieHoldParam.getCoreNumber() + "} ");
		}
		qsrDescription.append("Dates{").append(startDate).append("-").append(stopDate).append("}");

		Qsr qsr = new Qsr();
		qsr.setProcessLocation(dieHoldParam.getDepartment());
		qsr.setProductType(productType.name());
		qsr.setDescription(qsrDescription.toString());
		qsr.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		qsr.setStatus(QsrStatus.ACTIVE.getIntValue());
		qsr.setHoldAccessType(Config.getProperty().getDefaultHoldAccessType());

		Qsr qsrResult = getQsrDao().insert(qsr, dieHoldParam);

		QsrEvent qsrEvent = new QsrCreatedEvent();
		qsrEvent.setQsr(qsrResult);
		EventBus.publish(qsrEvent);
	}

	protected String validateRelease() {
		StringBuilder sb = new StringBuilder();
		String associateId = getView().getInputPanel().getAssociateIdTextField().getText().trim();
		String associateName = getView().getInputPanel().getAssociateNameTextField().getText().trim();
		if (associateId.equals("") || associateName.equals("")) {
			append(sb, "Missing Associate ID/Associate Name");
		}
		return sb.toString();
	}
}
