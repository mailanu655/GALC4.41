package com.honda.galc.client.teamleader.bearing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.product.process.engine.bearing.pick.model.BearingPickModel;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.dao.product.bearing.BearingPartByYearModelDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingPartByYearModel;
import com.honda.galc.entity.bearing.BearingPartByYearModelId;
import com.honda.galc.property.BearingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.StringUtil;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingModelMaintenanceController</code> is ... .
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
 * @created May 15, 2013
 */
public class BearingModelMaintenanceController extends BaseListener<BearingModelMaintenancePanel> implements ActionListener {

	private List<BearingPart> bearingParts;
	private BearingMatrix bearingMatrix;

	private BearingPropertyBean properties;

	public BearingModelMaintenanceController(BearingModelMaintenancePanel view) {
		super(view);
		this.properties = PropertyService.getPropertyBean(BearingPropertyBean.class, getView().getApplicationId());
	}

	@Override
	protected void executeActionPerformed(ActionEvent ae) {

		getView().getMainWindow().clearMessage();
		if (ae.getSource().equals(getView().getYearModelComboBox())) {
			selectBearingsByYearModel();
		} else if (ae.getSource().equals(getView().getBearingTypeComboBox())) {
			filterBearings();
		} else if (ae.getSource().equals(getView().getBearingPanelPopupMenu().getSubElements()[0].getComponent())) {
			assignBearingToYearModel();
		} else if (ae.getSource().equals(getView().getBearingPanelPopupMenu().getSubElements()[1].getComponent())) {
			displayUpdateBearingDialog();
		} else if (ae.getSource().equals(getView().getBearingPanelPopupMenu().getSubElements()[2].getComponent())) {
			displayCreateBearingDialog();
		} else if (ae.getSource().equals(getView().getBearingPanelPopupMenu().getSubElements()[3].getComponent())) {
			deleteBearing();
		} else if (ae.getSource().equals(getView().getAssignedBearingPanelPopupMenu().getSubElements()[0].getComponent())) {
			removeBearingAssignment();
		} else if (ae.getSource().equals(getView().getMainBearingCountComboBox())) {
			bearingCountSelected();
		} else if (ae.getSource().equals(getView().getConrodBearingCountComboBox())) {
			bearingCountSelected();
		} else if (ae.getSource().equals(getView().getSaveBearingCountConfigButton())) {
			saveBearingsCount();
		}else if (ae.getSource().equals(getView().getSaveConCrankConfigButton())) {
			saveConrodAndCrankShaftData();
		}
	}

	private void saveConrodAndCrankShaftData() {
		
		Map<?, ?> map = (Map<?, ?>) getView().getYearModelComboBox().getSelectedItem();
		String modelYearCode = (String) map.get("modelYearCode");
		String modelCode = (String) map.get("modelCode");
		
		
		String conrodRankIndex = getView().getConrodRankIndex().getText();
		String conrodWeightIndex = getView().getConrodWeightIndex().getText();
		String crankConIndex = getView().getCrankConIndex().getText();
		String crankMainIndex = getView().getCrankMainIndex().getText();

		

		
		String message = validateConrodCrankIndexes(conrodRankIndex,conrodWeightIndex, crankConIndex, crankMainIndex);
		if(!StringUtil.isNullOrEmpty(message)) {
			JOptionPane.showMessageDialog(getView(), message);
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(getView(), "Do you want to Save the details ?", "Save Details", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		
		BearingMatrix bm = getBearingMatrix();
		if (bm == null) {
			BearingMatrixId bmId = new BearingMatrixId();
			bmId.setModelYearCode(modelYearCode);
			bmId.setModelCode(modelCode);
			bm = new BearingMatrix();
			bm.setId(bmId);
			bm.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		} else {
			bm.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		}
		bm.setConrodRankIndex(conrodRankIndex);
		bm.setConrodWeightIndex(conrodWeightIndex);
		bm.setCrankConIndex(crankConIndex);
		bm.setCrankMainIndex(crankMainIndex);
		bm = ServiceFactory.getDao(BearingMatrixDao.class).save(bm);
		
	}

	private String validateConrodCrankIndexes(String conrodRankIndex, String conrodWeightIndex, String crankConIndex,
			String crankMainIndex) {
		
		if (StringUtil.isNullOrEmpty(conrodRankIndex) && StringUtil.isNullOrEmpty(conrodWeightIndex)
				&& StringUtil.isNullOrEmpty(crankConIndex) && StringUtil.isNullOrEmpty(crankMainIndex)) {
			return "All text fields can not be blank.";
		} 
		
		if(!validateFormParams(conrodRankIndex)) {
			return "Conrod Rank Index should be numbers seperated by comma.";
		}
		
		if(!validateFormParams(conrodWeightIndex)) {
			return "Conrod Weight Index should be numbers seperated by comma.";
		}
		if(!validateFormParams(crankConIndex)) {
			return "Crankshaft Conrod Index should be numbers seperated by comma.";
		}
		if(!validateFormParams(crankMainIndex)) {
			return "Crankshaft Main Index should be numbers seperated by comma.";
		}
		
		return "";
	}

	private boolean validateFormParams(String index) {
		
		if(!StringUtil.isNullOrEmpty(index)) {
		
			String[] indexArray = index.split(Delimiter.COMMA);
	
			if(!(indexArray.length == 2)) {
				return false;
			}
				
			if(!StringUtil.isNullOrEmpty(indexArray[0]) && !StringUtil.isNumeric(indexArray[0])) {
				return false;
			}
			
			if(!StringUtil.isNullOrEmpty(indexArray[1]) && !StringUtil.isNumeric(indexArray[1])) {
				return false;
			}
		}
		
		return true;
	}

	// === bl api === //
	public List<BearingPart> selectBearings() {
		setBearingParts(ServiceFactory.getDao(BearingPartDao.class).findAll());
		getView().getBearingPanel().getTable().clearSelection();
		getView().getBearingPanel().reloadData(getBearingParts());
		return getBearingParts();
	}

	protected void filterBearings() {
		getView().getBearingPanel().removeData();
		Object bearingType = getView().getBearingTypeComboBox().getSelectedItem();
		List<BearingPart> list = null;
		if (bearingType == null) {
			list = getBearingParts();
		} else {
			list = new ArrayList<BearingPart>();
			for (BearingPart bp : getBearingParts()) {
				if (bp == null) {
					continue;
				}
				if (bp.getType().equals(bearingType)) {
					list.add(bp);
				}
			}
		}
		getView().getBearingPanel().reloadData(list);
	}

	public void selectBearingsByYearModel() {

		Map<?, ?> map = (Map<?, ?>) getView().getYearModelComboBox().getSelectedItem();

		getView().getConrodRankIndex().setText("");
		getView().getConrodWeightIndex().setText("");
		getView().getCrankConIndex().setText("");
		getView().getCrankMainIndex().setText("");
		
		getView().getAssignedBearingPanel().removeData();
		getView().getMainBearingCountComboBox().setSelectedIndex(-1);
		getView().getMainBearingCountComboBox().setEnabled(false);
		getView().getConrodBearingCountComboBox().setSelectedIndex(-1);
		getView().getConrodBearingCountComboBox().setEnabled(false);
		getView().getSaveBearingCountConfigButton().setEnabled(false);
		getView().getSaveConCrankConfigButton().setEnabled(true);
		setBearingMatrix(null);

		if (map == null) {
			return;
		}

		String modelYearCode = (String) map.get("modelYearCode");
		String modelCode = (String) map.get("modelCode");

		List<BearingPart> list = ServiceFactory.getDao(BearingPartDao.class).findAllByYearModel(modelYearCode, modelCode);
		getView().getAssignedBearingPanel().getTable().clearSelection();
		getView().getAssignedBearingPanel().reloadData(list);

		BearingMatrixId bmId = new BearingMatrixId();
		bmId.setModelYearCode(modelYearCode);
		bmId.setModelCode(modelCode);
		BearingMatrix bm = ServiceFactory.getDao(BearingMatrixDao.class).findByKey(bmId);

		setBearingMatrix(bm);
		getView().getMainBearingCountComboBox().setEnabled(true);
		getView().getConrodBearingCountComboBox().setEnabled(true);
		if (bm != null) {
			getView().getMainBearingCountComboBox().setSelectedItem(bm.getNumberOfMainBearings());
			getView().getConrodBearingCountComboBox().setSelectedItem(bm.getNumberOfConrods());
			getView().getConrodRankIndex().setText(bm.getConrodRankIndex());
			getView().getConrodWeightIndex().setText(bm.getConrodWeightIndex());
			getView().getCrankConIndex().setText(bm.getCrankConIndex());
			getView().getCrankMainIndex().setText(bm.getCrankMainIndex());
		}
	}

	public void bearingCountSelected() {
		Integer mainCount = (Integer) getView().getMainBearingCountComboBox().getSelectedItem();
		Integer conrodCount = (Integer) getView().getConrodBearingCountComboBox().getSelectedItem();

		if (mainCount == null || conrodCount == null) {
			getView().getSaveBearingCountConfigButton().setEnabled(false);
			return;
		}

		if (getBearingMatrix() == null) {
			getView().getSaveBearingCountConfigButton().setEnabled(true);
			return;
		}

		if (!mainCount.equals(getBearingMatrix().getNumberOfMainBearings())) {
			getView().getSaveBearingCountConfigButton().setEnabled(true);
			return;
		}

		if (!conrodCount.equals(getBearingMatrix().getNumberOfConrods())) {
			getView().getSaveBearingCountConfigButton().setEnabled(true);
			return;
		}
		getView().getSaveBearingCountConfigButton().setEnabled(false);
	}

	public void saveBearingsCount() {

		Map<?, ?> map = (Map<?, ?>) getView().getYearModelComboBox().getSelectedItem();

		String modelYearCode = (String) map.get("modelYearCode");
		String modelCode = (String) map.get("modelCode");

		Integer mainCount = (Integer) getView().getMainBearingCountComboBox().getSelectedItem();
		Integer conrodCount = (Integer) getView().getConrodBearingCountComboBox().getSelectedItem();

		if (modelYearCode == null || modelCode == null || mainCount == null || conrodCount == null) {
			String msg = "Some data might be missing, please refresh screen and try again.";
			JOptionPane.showMessageDialog(getView(), msg);
			getView().getSaveBearingCountConfigButton().setEnabled(false);
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Save Bearing Counts", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		BearingMatrix bm = getBearingMatrix();
		if (bm == null) {
			BearingMatrixId bmId = new BearingMatrixId();
			bmId.setModelYearCode(modelYearCode);
			bmId.setModelCode(modelCode);
			bm = new BearingMatrix();
			bm.setId(bmId);
			bm.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));
		} else {
			bm.setUpdateTimestamp(new Timestamp(System.currentTimeMillis()));
		}
		bm.setNumberOfMainBearings(mainCount);
		bm.setNumberOfConrods(conrodCount);
		bm = ServiceFactory.getDao(BearingMatrixDao.class).save(bm);
		
		
		logUserAction(SAVED, bm);
		setBearingMatrix(bm);
		bearingCountSelected();
	}

	public List<Map<String, String>> selectYearModelCodes() {
		List<Map<String, String>> list = ServiceFactory.getDao(EngineSpecDao.class).findAllYearModelCodes();
		if (list != null) {
			list.add(0, null);
		}
		return list;
	}

	public void assignBearingToYearModel() {

		Map<?, ?> ymCode = (Map<?, ?>) getView().getYearModelComboBox().getSelectedItem();
		if (ymCode == null) {
			return;
		}

		BearingPart selectedBearing = getView().getBearingPanel().getSelectedItem();
		
		boolean mainSelected = getView().getMainBearingCountComboBox().getSelectedItem() != null;
		boolean conSelected = getView().getConrodBearingCountComboBox().getSelectedItem() != null;
		if (getView().getSaveBearingCountConfigButton().isEnabled() || !mainSelected || !conSelected ) {
			String msg = "Please select and save appropriate bearing count";
			JOptionPane.showMessageDialog(getView(), msg, "Assign to Year Model", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String msg = "";
		List<BearingPart> assignedBearings = getView().getAssignedBearingPanel().getItems();
		if (assignedBearings.contains(selectedBearing)) {
			msg = "Selected bearing is already assigned.";
			JOptionPane.showMessageDialog(getView(), msg, "Assign to Year Model", JOptionPane.WARNING_MESSAGE);
			return;
		}

		for (BearingPart bp : assignedBearings) {
			if (bp.getColor().equals(selectedBearing.getColor())
					&& bp.getType().equals(selectedBearing.getType())) {
				msg = String
						.format("%s Bearing with color %s is already assigned. Do you want to continue adding new Bearing with same color?",
								selectedBearing.getType(),
								selectedBearing.getColor());
				// @KM :RGALCPROD-2824 - Allow multiple bearings of the same
				// color to be assigned to the same Year Model combination
				int response = JOptionPane.showConfirmDialog(getView(), msg,
						"Confirm", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.NO_OPTION) {
					return;
				} else if (response == JOptionPane.CLOSED_OPTION) {
					return;
				}
			}
		}

		msg = "Are You sure ?";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Assign to Year Model", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION != retCode) {
			return;
		}

		String modelYearCode = (String) ymCode.get("modelYearCode");
		String modelCode = (String) ymCode.get("modelCode");

		BearingPartByYearModel bpym = new BearingPartByYearModel();
		BearingPartByYearModelId id = new BearingPartByYearModelId();
		bpym.setId(id);
		id.setModelYearCode(modelYearCode);
		id.setModelCode(modelCode);
		id.setBearingSerialNumber(selectedBearing.getId());
		bpym.setCreateTimestamp(new Timestamp(System.currentTimeMillis()));

		ServiceFactory.getDao(BearingPartByYearModelDao.class).save(bpym);
		logUserAction(SAVED, bpym);
		selectBearingsByYearModel();
	}

	public void removeBearingAssignment() {

		Map<?, ?> ymCode = (Map<?, ?>) getView().getYearModelComboBox().getSelectedItem();
		if (ymCode == null) {
			return;
		}

		String modelYearCode = (String) ymCode.get("modelYearCode");
		String modelCode = (String) ymCode.get("modelCode");

		BearingPart selectedBearing = getView().getAssignedBearingPanel().getSelectedItem();
		if (selectedBearing == null) {
			return;
		}

		List<BearingMatrixCell> usedInMatrixBearings = ServiceFactory.getDao(BearingMatrixCellDao.class).findAllByBearingNumber(modelYearCode, modelCode, selectedBearing.getId());
		if (usedInMatrixBearings != null && !usedInMatrixBearings.isEmpty()) {
			String msg = "This bearing is already used in Matrix. \nTo delete it please remove it first from Matrix.";
			JOptionPane.showMessageDialog(getView(), msg, "Delete assignment", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Remove from Year Model", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == retCode) {

			BearingPartByYearModel bpym = new BearingPartByYearModel();
			BearingPartByYearModelId id = new BearingPartByYearModelId();
			bpym.setId(id);
			id.setModelYearCode(modelYearCode);
			id.setModelCode(modelCode);
			id.setBearingSerialNumber(selectedBearing.getId());

			ServiceFactory.getDao(BearingPartByYearModelDao.class).remove(bpym);
			logUserAction(REMOVED, bpym);
			selectBearingsByYearModel();
		}
	}

	public void displayUpdateBearingDialog() {

		BearingPart bearingPart = getView().getBearingPanel().getSelectedItem();
		if (bearingPart == null) {
			return;
		}
		BearingPickModel model = new BearingPickModel(getView().getMainWindow().getApplicationContext());
		BearingDialog dialog = new BearingDialog(bearingPart, getBearingTypes(), model.getColors());

		dialog.setLocationRelativeTo(getView());
		dialog.setModal(true);
		dialog.setVisible(true);

		if (dialog.isUpdated()) {
			selectBearings();
			if (getView().getBearingTypeComboBox().getSelectedItem() != null) {
				filterBearings();
			}
			if (getView().getYearModelComboBox().getSelectedItem() != null) {
				selectBearingsByYearModel();
			}
		}
	}

	public void displayCreateBearingDialog() {
		BearingPickModel model = new BearingPickModel(getView().getMainWindow().getApplicationContext());
		BearingDialog dialog = new BearingDialog(getBearingTypes(), model.getColors());

		dialog.setLocationRelativeTo(getView());
		dialog.setModal(true);
		dialog.setVisible(true);

		if (dialog.isUpdated()) {
			selectBearings();
			if (getView().getBearingTypeComboBox().getSelectedItem() != null) {
				filterBearings();
			}
		}
	}

	public void deleteBearing() {
		BearingPart bearingPart = getView().getBearingPanel().getSelectedItem();

		if (bearingPart == null) {
			selectBearings();
			return;
		}

		long count = ServiceFactory.getDao(BearingPartByYearModelDao.class).selectCountByBearingNumber(bearingPart.getId());
		if (count > 0) {
			String msg = "This bearing is assigned to Year Model, please remove all existing associations before deleting bearing.";
			JOptionPane.showMessageDialog(getView(), msg, "Delete Bearing", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int retCode = JOptionPane.showConfirmDialog(getView(), "Are You sure ?", "Delete Bearing", JOptionPane.YES_NO_OPTION);
		if (JOptionPane.YES_OPTION == retCode) {
			ServiceFactory.getDao(BearingPartDao.class).remove(bearingPart);
			logUserAction(REMOVED, bearingPart);
			selectBearings();
			if (getView().getBearingTypeComboBox().getSelectedItem() != null) {
				filterBearings();
			}
		}
	}

	// === get/set === //
	public List<BearingPart> getBearingParts() {
		return bearingParts;
	}

	public void setBearingParts(List<BearingPart> bearingParts) {
		this.bearingParts = bearingParts;
	}

	public String[] getBearingTypes() {
		return getProperties().getBearingTypes();
	}

	public Integer[] getMainBearingCountValues() {
		return getProperties().getMainBearingCountValues();
	}

	public Integer[] getConrodCountValues() {
		return getProperties().getConrodCountValues();
	}

	protected BearingMatrix getBearingMatrix() {
		return bearingMatrix;
	}

	protected void setBearingMatrix(BearingMatrix bearingMatrix) {
		this.bearingMatrix = bearingMatrix;
	}

	protected BearingPropertyBean getProperties() {
		return properties;
	}
}