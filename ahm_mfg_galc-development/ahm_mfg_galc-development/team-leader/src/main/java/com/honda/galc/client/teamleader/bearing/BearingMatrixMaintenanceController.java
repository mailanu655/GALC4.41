package com.honda.galc.client.teamleader.bearing;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.dao.product.EngineSpecDao;
import com.honda.galc.dao.product.bearing.BearingMatrixCellDao;
import com.honda.galc.dao.product.bearing.BearingMatrixDao;
import com.honda.galc.dao.product.bearing.BearingPartDao;
import com.honda.galc.entity.bearing.BearingMatrix;
import com.honda.galc.entity.bearing.BearingMatrixCell;
import com.honda.galc.entity.bearing.BearingMatrixId;
import com.honda.galc.entity.bearing.BearingPart;
import com.honda.galc.entity.bearing.BearingPartType;
import com.honda.galc.entity.bearing.BearingType;
import com.honda.galc.property.BearingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * <h3>Class description</h3> <h4>Description</h4>
 * <p>
 * <code>BearingMatrixMaintenanceController</code> is ... .
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
 * @created May 17, 2013
 */
public class BearingMatrixMaintenanceController extends BaseListener<BearingMatrixMaintenancePanel> implements ActionListener, MouseListener {

	public static final String LAST_SELECTION = "LAST_SELECTION";

	private List<BearingPart> bearingParts;
	private BearingPropertyBean properties;

	public BearingMatrixMaintenanceController(BearingMatrixMaintenancePanel view) {
		super(view);
		this.properties = PropertyService.getPropertyBean(BearingPropertyBean.class, getView().getApplicationId());
	}

	// === action mapping === //
	@Override
	protected void executeActionPerformed(ActionEvent ae) {
		getView().getMainWindow().clearMessage();
		if (ae.getSource().equals(getView().getYearModelComboBox())) {
			yearModelSelected();
		} else if (ae.getSource().equals(getView().getModelTypeComboBox())) {
			modelTypeSelected();
		} else if (ae.getSource().equals(getView().getJournalPositionComboBox())) {
			journalPositionSelected();
		} else if (ae.getSource().equals(getView().getBearingTypeComboBox())) {
			bearingTypeSelected();
		} else if (ae.getSource().equals(getView().getResetButton())) {
			resetButtonClicked();
		} else if (ae.getSource().equals(getView().getSaveButton())) {
			saveButtonClicked();
		}
	}

	// === mouse handler ==== //
	public void mouseClicked(MouseEvent e) {
		if (MouseEvent.BUTTON1 != e.getButton()) {
			return;
		}
		BearingPart bearingPart = getView().getAssignedBearingPanel().getSelectedItem();
		if (bearingPart == null) {
			return;
		}

		if (e.getSource() instanceof JTextField) {
			JTextField field = (JTextField) e.getSource();
			BearingType bearingType = getView().getBearingType();
			BearingPartType bearingPartType = getView().getBearingPartType(field);
			if (isMatch(bearingPart, bearingType, bearingPartType)) {
				getView().setBearingPart(field, bearingPart);
				getView().resetButtons();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource() instanceof JTextField) {
			BearingPart bearingPart = getView().getAssignedBearingPanel().getSelectedItem();
			if (bearingPart == null) {
				return;
			}
			JTextField field = (JTextField) e.getSource();
			BearingType bearingType = getView().getBearingType();
			BearingPartType bearingPartType = getView().getBearingPartType(field);
			if (isMatch(bearingPart, bearingType, bearingPartType) && isInputSelected()) {
				field.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof JTextField) {
			JTextField comp = (JTextField) e.getSource();
			if (!comp.getCursor().equals(Cursor.getDefaultCursor())) {
				comp.setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {

	}

	// === actions implementation === //
	protected void yearModelSelected() {
		if (getView().getMatrixComponent().isUpdated()) {
			if (!isDiscardChanges(getView().getYearModelComboBox())) {
				return;
			}
		}
		setSelection(getView().getYearModelComboBox());

		getView().clearMatrix();
		setBearingParts(null);

		loadBearings();
		loadModelTypes();
		loadJournalPositions();
		loadBearingTypes();
		loadMatrix();
	}

	protected void modelTypeSelected() {
		if (getView().getMatrixComponent().isUpdated()) {
			if (!isDiscardChanges(getView().getModelTypeComboBox())) {
				return;
			}
		}
		setSelection(getView().getModelTypeComboBox());
		loadMatrix();
	}

	protected void journalPositionSelected() {
		if (getView().getMatrixComponent().isUpdated()) {
			if (!isDiscardChanges(getView().getJournalPositionComboBox())) {
				return;
			}
		}
		setSelection(getView().getJournalPositionComboBox());
		loadMatrix();
	}

	protected void bearingTypeSelected() {
		getView().getAssignedBearingPanel().clearSelection();
		getView().getAssignedBearingPanel().reloadData(filterBearings());
	}

	protected void resetButtonClicked() {
		if (getView().getMatrixComponent().isUpdated()) {
			if (!isDiscardChanges()) {
				return;
			}
		}
		loadMatrix();
	}

	protected void saveButtonClicked() {
		if (!(getView().getMatrixComponent().isComplete())) {
			JOptionPane.showMessageDialog(getView(), "Please fill out all Matrix Cells .", "Matrix Validation", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String msg = "Are you sure ?";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Bearing Matrix", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}

		List<BearingMatrixCell> list = new ArrayList<BearingMatrixCell>();
		list.addAll(getView().getMatrixComponent().getUpdatedBearingMatrixCells());

		if (list != null && !list.isEmpty()) {
			ServiceFactory.getDao(BearingMatrixCellDao.class).saveAll(list);
		}
		loadMatrix();
	}

	// === ui data api === //
	protected void loadYearModels() {
		List<Map<String, String>> ymCodesList = selectYearModelCodes();
		getView().getYearModelComboBox().setModel(new DefaultComboBoxModel(ymCodesList.toArray()));
		getView().getYearModelComboBox().setSelectedIndex(-1);
	}

	protected void loadBearings() {
		getView().getAssignedBearingPanel().getTable().clearSelection();
		getView().getAssignedBearingPanel().removeData();
		String modelYearCode = getView().getSelectedModelYearCode();
		String modelCode = getView().getSelectedModelCode();
		if (modelYearCode == null || modelCode == null) {
			return;
		}
		List<BearingPart> list = ServiceFactory.getDao(BearingPartDao.class).findAllByYearModel(modelYearCode, modelCode);
		setBearingParts(list);
		getView().getAssignedBearingPanel().reloadData(filterBearings());
	}

	protected void loadModelTypes() {
		getView().getModelTypeComboBox().removeAllItems();
		String modelYearCode = getView().getSelectedModelYearCode();
		String modelCode = getView().getSelectedModelCode();
		if (modelYearCode == null || modelCode == null) {
			return;
		}
		List<String> list = selectModelTypeOptions(modelYearCode, modelCode);
		getView().getModelTypeComboBox().setModel(new DefaultComboBoxModel(list.toArray()));
		if (!list.isEmpty()) {
			getView().getModelTypeComboBox().setSelectedIndex(0);
		}
	}

	protected void loadJournalPositions() {
		getView().getJournalPositionComboBox().removeAllItems();
		String modelYearCode = getView().getSelectedModelYearCode();
		String modelCode = getView().getSelectedModelCode();
		if (modelYearCode == null || modelCode == null) {
			return;
		}
		List<String> list = selectBearingPositions(getView().getBearingType(), modelYearCode, modelCode);
		getView().getJournalPositionComboBox().setModel(new DefaultComboBoxModel(list.toArray()));
		if (!list.isEmpty()) {
			getView().getJournalPositionComboBox().setSelectedIndex(0);
		}
	}

	protected void loadBearingTypes() {
		getView().getBearingTypeComboBox().removeAllItems();
		if (getView().getYearModelComboBox().getSelectedItem() == null) {
			return;
		}
		String[] bearingTypes = getView().getBearingTypeOptionValues();
		getView().getBearingTypeComboBox().setModel(new DefaultComboBoxModel(bearingTypes));
	}

	protected void loadMatrix() {
		getView().clearMatrix();
		getView().getAssignedBearingPanel().clearSelection();
		getView().resetButtons();
		String modelYearCode = getView().getSelectedModelYearCode();
		String modelCode = getView().getSelectedModelCode();
		String modelTypeCode = (String) getView().getModelTypeComboBox().getSelectedItem();
		String journalPosition = (String) getView().getJournalPositionComboBox().getSelectedItem();
		if (modelYearCode == null || modelCode == null || modelTypeCode == null || journalPosition == null) {
			return;
		}
		List<BearingMatrixCell> bearings = ServiceFactory.getDao(BearingMatrixCellDao.class).findAllByYearModelTypePosition(modelYearCode, modelCode, modelTypeCode, journalPosition, getView().getBearingType().name());
		getView().loadMatrix(getBearingParts(), bearings);
	}

	// === model api === //
	public List<Map<String, String>> selectYearModelCodes() {
		List<Map<String, String>> list = ServiceFactory.getDao(EngineSpecDao.class).findAllYearModelCodes();
		if (list != null) {
			list.add(0, null);
		}
		return list;
	}

	public List<String> selectModelTypeOptions(String modelYearCode, String modelCode) {
		List<String> list = new ArrayList<String>();
		list.add("*");
		List<String> types = ServiceFactory.getDao(EngineSpecDao.class).findModelTypeCodes(modelYearCode, modelCode);
		if (types != null) {
			list.addAll(types);
		}
		return list;
	}

	public List<String> selectBearingPositions(BearingType bearingType, String modelYearCode, String modelCode) {
		List<String> list = new ArrayList<String>();
		list.add("*");
		if (BearingType.Main.equals(bearingType)) {
			BearingMatrix bm = new BearingMatrix();
			BearingMatrixId bmId = new BearingMatrixId();
			bmId.setModelYearCode(modelYearCode);
			bmId.setModelCode(modelCode);
			bm = ServiceFactory.getDao(BearingMatrixDao.class).findByKey(bmId);
			if (bm != null) {
				int numOfMains = bm.getNumberOfMainBearings();
				for (int i = 1; i <= numOfMains; i++) {
					list.add(String.valueOf(i));
				}
			}
		}
		return list;
	}

	// === supporting api === //
	protected boolean isDiscardChanges() {
		String msg = "You have updated Matrix, do you want to discard changes ?";
		int retCode = JOptionPane.showConfirmDialog(getView(), msg, "Discard Update", JOptionPane.YES_NO_OPTION);
		if (retCode == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	protected boolean isDiscardChanges(JComboBox comboBox) {
		if (isDiscardChanges()) {
			return true;
		}
		if (comboBox != null) {
			resetSelection(comboBox);
		}
		return false;
	}

	protected boolean isMatch(BearingPart bearingPart, BearingType bearingType, BearingPartType bearingPartType) {
		if (bearingPart == null || bearingType == null || bearingPartType == null) {
			return false;
		}
		if (BearingType.Conrod.name().equals(bearingPart.getType()) && BearingType.Conrod.equals(bearingType)) {
			return true;
		}
		if (bearingPart.getType().equals(String.format("%s %s", bearingType.name(), bearingPartType.name()))) {
			return true;
		}
		return false;
	}

	protected boolean isInputSelected() {
		if (getView().getYearModelComboBox().getSelectedIndex() > -1 && getView().getModelTypeComboBox().getSelectedIndex() > -1 && getView().getJournalPositionComboBox().getSelectedIndex() > -1) {
			return true;
		}
		return false;
	}

	protected List<BearingPart> filterBearings() {
		List<BearingPart> bearingParts = new ArrayList<BearingPart>();
		if (getBearingParts() == null) {
			return bearingParts;
		}
		Object item = getView().getBearingTypeComboBox().getSelectedItem();
		String selectedBearingType = item == null ? null : (String) item;
		if (selectedBearingType != null) {
			bearingParts.addAll(filterBearings(getBearingParts(), selectedBearingType));
		} else {
			String[] filter = getView().getBearingTypeOptionValues();
			bearingParts.addAll(filterBearings(getBearingParts(), filter));
		}
		return bearingParts;
	}

	protected List<BearingPart> filterBearings(List<BearingPart> bearingParts, String... bearingTypes) {
		List<BearingPart> filtered = new ArrayList<BearingPart>();
		if (bearingParts == null) {
			return filtered;
		}
		if (bearingTypes == null) {
			return bearingParts;
		}
		List<String> filter = Arrays.asList(bearingTypes);
		for (BearingPart bp : bearingParts) {
			if (bp == null) {
				continue;
			}
			if (filter.contains(StringUtils.trim(bp.getType()))) {
				filtered.add(bp);
			}
		}
		return filtered;
	}

	// === get/set === //
	protected void setSelection(JComboBox comboBox) {
		if (comboBox == null) {
			return;
		}
		comboBox.putClientProperty(LAST_SELECTION, comboBox.getSelectedItem());
	}

	protected void resetSelection(JComboBox comboBox) {
		if (comboBox == null) {
			return;
		}
		Object lastSelection = comboBox.getClientProperty(LAST_SELECTION);
		comboBox.setSelectedItem(lastSelection);
	}

	protected List<BearingPart> getBearingParts() {
		return bearingParts;
	}

	protected void setBearingParts(List<BearingPart> bearingParts) {
		this.bearingParts = bearingParts;
	}

	protected BearingPropertyBean getProperties() {
		return properties;
	}
}