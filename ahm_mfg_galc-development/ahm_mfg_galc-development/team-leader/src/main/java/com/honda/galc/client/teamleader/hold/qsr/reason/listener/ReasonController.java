package com.honda.galc.client.teamleader.hold.qsr.reason.listener;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.product.controller.listener.BaseListener;
import com.honda.galc.client.teamleader.hold.config.Config;
import com.honda.galc.client.teamleader.hold.config.QsrMaintenancePropertyBean;
import com.honda.galc.client.teamleader.hold.qsr.reason.ReasonPanel;
import com.honda.galc.client.utils.ComboBoxUtils;
import com.honda.galc.dao.conf.DivisionDao;
import com.honda.galc.dao.conf.LineDao;
import com.honda.galc.dao.product.HoldReasonDao;
import com.honda.galc.dao.product.HoldReasonMappingDao;
import com.honda.galc.dto.HoldReasonMappingDto;
import com.honda.galc.entity.conf.Division;
import com.honda.galc.entity.conf.Line;
import com.honda.galc.entity.conf.Plant;
import com.honda.galc.entity.enumtype.QCAction;
import com.honda.galc.entity.product.HoldReason;
import com.honda.galc.entity.product.HoldReasonMapping;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.PropertyComparator;

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
public class ReasonController extends BaseListener<ReasonPanel> implements ActionListener,ListSelectionListener {

	/**
	 * 
	 */
	private static final String USER_DEFINED = "USER_DEFINED";
	public static final String SELECT_PLANT = "Select Plant";
	public static final String SELECT_LINE = "Select Line";
	public static final String SELECT_DIVISION = "Select Division";
	public static final String SELECT_QCACTION = "Select QC Action";
	private QsrMaintenancePropertyBean propertyBean=null;

	public ReasonController(ReasonPanel parentPanel) {
		super(parentPanel);
	}

	@Override
	protected void executeActionPerformed(ActionEvent e) {
		getView().getMainWindow().clearStatusMessage();
		
		if (e.getSource() == getView().getPlantComboBox()) {
			handlePlantSelected();
			return;
		}
		if (e.getSource() == getView().getDivisonComboBox()) {
			handleDivisonSelected();
			return;
		}
		if (e.getSource() == getView().getLineComboBox()) {
			handleLineSelected();
			return;
		}
		if (e.getSource() == getView().getQCActionComboBox()) {
			handleQCActionSelected();
			return;
		}
		
		if (e.getSource() == getView().getAddButton()) {
			handleAddReason();
			return;
		}
		
		if (e.getSource() == getView().getNewButton()) {
			handleNewReason();
			return;
		}
		
		if (e.getSource() == getView().getRemoveButton()) {
			handleDeleteReason();
			return;
		}

	}

	// === handlers === //
	@SuppressWarnings({ "unchecked" })
	protected void handlePlantSelected() {
		
		getView().getDivisonComboBox().removeAllItems();
		Plant plant = (Plant) getView().getPlantComboBox().getSelectedItem();
		if (plant== null) {
			String msg = "There is no defined Plant property, please review configuration";
			getView().getMainWindow().setErrorMessage(msg);
			return;
		}
		try {
			List<Division> divisions = new ArrayList<Division>();
			if (!plant.getPlantName().trim().equals(getView().SELECT_PLANT)) {
				divisions = ServiceFactory.getDao(DivisionDao.class).findById(plant.getId().getSiteName(), plant.getPlantName());
			} 
			Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "divisionName"));
			divisions.add(0, createDummyDivision());
			ComboBoxUtils.loadComboBox(getView().getDivisonComboBox(), divisions);
			enableAddQueryButtons();	
		} catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
	
	}

	// === handlers === //
	@SuppressWarnings({ "unchecked", "rawtypes", "null" })
	protected void handleDivisonSelected() {

		getView().getLineComboBox().removeAllItems();
		Division division = (Division)getView().getDivisonComboBox().getSelectedItem();
		if(division == null) {
			getView().getMainWindow().setErrorMessage("There is no defined Division property");
			return;
		}
		try {
			List<Line> lines = new ArrayList<Line>();
			if (!division.getDivisionName().trim().equals(SELECT_DIVISION)) {
				lines = ServiceFactory.getDao(LineDao.class).findAllByDivisionId(division, false);
				getView().getNewButton().setEnabled(true);
			} else {
				getView().getNewButton().setEnabled(false);
			}
			Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
			lines.add(0, createDummyLine());
			ComboBoxUtils.loadComboBox(getView().getLineComboBox(), lines);

			loadHoldReasonPane();
			loadHoldReasonMappingPane();
			enableAddQueryButtons();
		}catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void handleLineSelected() {

		Line line = (Line)getView().getLineComboBox().getSelectedItem();
		if(line == null) {
			getView().getMainWindow().setErrorMessage("There is no defined Line property");
			return;
		}
		try {
			loadHoldReasonMappingPane();
			enableAddQueryButtons();
		}catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}		
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void handleQCActionSelected() {

		String action = (String)getView().getQCActionComboBox().getSelectedItem();
		if(action == null) {
			getView().getMainWindow().setErrorMessage("There is no defined Action property");
			return;
		}
		try {
			loadHoldReasonMappingPane();
			enableAddQueryButtons();
		} catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
		
	}
	protected void handleAddReason() {
		String msg = validateAdd();
		getView().getMainWindow().setErrorMessage(msg);
		if (msg != null && msg.trim().length() > 0) {
			JOptionPane.showMessageDialog(getView(), msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int retCode = JOptionPane.showConfirmDialog(getMainWindow(), "Are you sure You want to add reason under LineId ?", "Add Reason", JOptionPane.YES_NO_OPTION);

		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			createHoldReasonMappings();
		} catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
	}

	protected void handleNewReason() {
		String reason = JOptionPane.showInputDialog("Enter the new Reason", "");
		if (StringUtils.isEmpty(reason)) return;
		String msg = validateNew(reason);
		if (msg != null && msg.trim().length() > 0) {
			JOptionPane.showMessageDialog(getView(), msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int retCode = JOptionPane.showConfirmDialog(getView(), "Are you sure You want to add a reason ?", "Save Reason", JOptionPane.YES_NO_OPTION);
		if (retCode != JOptionPane.YES_OPTION) {
			return;
		}
		try {
			addReason(reason);
		} catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
			
	}
	
	protected void handleDeleteReason() {
		List<HoldReasonMappingDto> selectedValues = getView().getHoldReasonMappingPane().getSelectedItems();
		if (selectedValues == null || selectedValues.isEmpty()) return;
		List<HoldReasonMapping> reasonMappings = new ArrayList<HoldReasonMapping>();
		try {
			for (HoldReasonMappingDto mapping : selectedValues) {
				reasonMappings.add((HoldReasonMapping)ServiceFactory.getDao(HoldReasonMappingDao.class).findByKey(Integer.valueOf(mapping.getReasonMappingId())));
			}
			getDao(HoldReasonMappingDao.class).removeAll(reasonMappings);
			logUserAction(REMOVED, reasonMappings);
			getView().getHoldReasonMappingPane().clearSelection();
			enableAddQueryButtons();
			loadHoldReasonMappingPane();
		}  catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
		
	}

	// === utility api === //
	protected String validateAdd() {
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}

	// === utility api === //
	protected String validateNew(String reason) {
		StringBuilder sb = new StringBuilder();
		
		Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
		List<HoldReason> records = ServiceFactory.getDao(HoldReasonDao.class).findAllByDivisionId(division.getDivisionId());
		for (HoldReason record : records) {
			if (StringUtils.equals(reason, record.getHoldReason())) {
				sb.append("Reason exists");
				return sb.toString();
			}
		}
		
		return sb.toString();
	}
	
	// === utility api === //
	protected String validateDelete() {
		StringBuilder sb = new StringBuilder();
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
	
	public void addReason(String reasonName) {
		//TODO implement
		Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
		try {
			if (division.getDivisionName().equals("") || division.getDivisionName().equals(SELECT_DIVISION)) {
				return;
			} else {
				
				HoldReason reason = new HoldReason();
				reason.setDivisionId(division.getDivisionId());
				reason.setHoldReason(reasonName);
				getDao(HoldReasonDao.class).insert(reason);
				logUserAction(INSERTED, reason);
			}
			
			loadHoldReasonPane();
			enableAddQueryButtons();
		}  catch(Exception ex) {
			getView().getMainWindow().setErrorMessage("Exception occured:"+ex.getMessage());
		}
	}
	
	public Plant createDummyPlant() {
		Plant dummy = new Plant();
		dummy.setPlantName(SELECT_PLANT);
		return dummy;
	}
	public Line createDummyLine() {
		Line dummy = new Line();
		dummy.setLineName(SELECT_LINE);
		return dummy;
	}
	
	public Division createDummyDivision() {
		Division dummy = new Division();
		dummy.setDivisionName(SELECT_DIVISION);
		return dummy;
	}
	
	private void enableAddQueryButtons(){
		String divisionName = "";
		String plantName = "";
		String lineName = "";
		String actionName = "";

		
		if (getView().getPlantComboBox().getSelectedItem() != null) {
			Plant plant = (Plant) getView().getPlantComboBox().getSelectedItem();
			plantName = plant.getPlantName().trim();
		}
		
		if (getView().getDivisonComboBox().getSelectedItem() != null) {
			Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
			divisionName = division.getDivisionName().trim();
		}

		if (!(divisionName.equals("")) && !(divisionName.equals(SELECT_DIVISION))) {
			getView().getNewButton().setEnabled(true);
		} else {
			getView().getNewButton().setEnabled(false);
		}
		
		if (getView().getLineComboBox().getSelectedItem() != null) {
			Line line = (Line) getView().getLineComboBox().getSelectedItem();
			lineName = line.getLineName().trim();
		}
		if (getView().getQCActionComboBox().getSelectedItem() != null) {
			actionName = (String) getView().getQCActionComboBox().getSelectedItem();
		}
		List<HoldReason> reasons = getView().getHoldReasonPane().getSelectedItems();
		List<HoldReasonMappingDto> reasonMappings = getView().getHoldReasonMappingPane().getSelectedItems();

		if (divisionName.equals("") || divisionName.equals(SELECT_DIVISION)
				|| plantName.equals("") || plantName.equals(SELECT_PLANT)
				|| lineName.equals("") || lineName.equals(SELECT_LINE) 
				|| actionName.equals("") || actionName.equals(SELECT_QCACTION)
				|| reasons == null || reasons.isEmpty()) {
			getView().getAddButton().setEnabled(false);
		} else {
			getView().getAddButton().setEnabled(true);
		}
		
		if (reasonMappings == null || reasonMappings.isEmpty()) {
			getView().getRemoveButton().setEnabled(false);
		} else {
			getView().getRemoveButton().setEnabled(true);
		}
	}

	private void createHoldReasonMappings() {
		List<HoldReason> reasons = getView().getHoldReasonPane().getSelectedItems();
		if (reasons == null || reasons.isEmpty()) return;
		
		List<HoldReasonMappingDto> currentReasonMappings = getView().getHoldReasonMappingPane().getItems();
		List<HoldReasonMapping> reasonMappings = new ArrayList<HoldReasonMapping>();
		
		Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
		Line line = (Line) getView().getLineComboBox().getSelectedItem();
		String action = (String) getView().getQCActionComboBox().getSelectedItem();
		int existingCount = 0;
		if (division.getDivisionName().equals("") || division.getDivisionName().equals(SELECT_DIVISION)
				|| line.getLineName().equals("") || line.getLineName().equals(SELECT_LINE) 
				|| action.equals("") || action.equals(SELECT_QCACTION)) {
			getView().getMainWindow().setErrorMessage("Check that division line and action are selected");
			return;
		} else {
			for (HoldReason reason : reasons) {
				int count =  ServiceFactory.getDao(HoldReasonMappingDao.class).findAllByLineReasonAndAction(line.getLineId(), 
						reason.getReasonId(),QCAction.getQCAction(action).getQcActionId()).size();
				if ((count <= 0)) {
					HoldReasonMapping reasonMap = new HoldReasonMapping();
					reasonMap.setAssociateId("");
					reasonMap.setAssociateName("");
					reasonMap.setDivisionId(reason.getDivisionId());
					reasonMap.setLineId(line.getLineId());
					reasonMap.setQcActionId(QCAction.getQCAction(action).getQcActionId());
					reasonMap.setReasonId(reason.getReasonId());
					if (!currentReasonMappings.contains(reasonMap)) {
						reasonMappings.add(reasonMap);
					} else {
						getMainWindow().setErrorMessage("Reason already added");
					}
				} else {
					existingCount++;
				}
			}
		}

		if (!reasonMappings.isEmpty()) {
			getDao(HoldReasonMappingDao.class).saveAll(reasonMappings);
			logUserAction(SAVED, reasonMappings);
		} else
			getView().getMainWindow().setErrorMessage("There are no reasons to add");
		
		if (existingCount >1){
			getView().getMainWindow().setErrorMessage(existingCount + " of the reasons already added");
		}
		
		getView().getHoldReasonPane().clearSelection();
		loadHoldReasonMappingPane();
	}
	
	private void loadHoldReasonPane()
	{
		List<HoldReason> reasons = new ArrayList<HoldReason>();
		propertyBean=PropertyService.getPropertyBean(QsrMaintenancePropertyBean.class, getView().getApplicationId());
		Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
		String[] holdReasons = propertyBean.getHoldReasons();
		int id = 0;
		if (holdReasons !=null && !(holdReasons[0].equalsIgnoreCase(USER_DEFINED)) ){
			for (String s : holdReasons) {
				HoldReason reason = new HoldReason();
				reason.setDivisionId(division.getDivisionId());
				reason.setHoldReason(s);
				reason.setReasonId(++id);
				reasons.add(reason);
			}
		} else {
			reasons = ServiceFactory.getDao(HoldReasonDao.class).findAllByDivisionId(division.getDivisionId());
		}
	
		getView().getHoldReasonPane().reloadData(reasons);
	}
	
	private void loadHoldReasonMappingPane() {
		List<HoldReasonMappingDto> reasonMappings = new ArrayList<HoldReasonMappingDto>();
		String divisionName = "";
		String lineName = "";
		String actionName = "";

		if (getView().getQCActionComboBox().getSelectedItem() != null) {
			String action = (String) getView().getQCActionComboBox().getSelectedItem();

			if (getView().getLineComboBox().getSelectedItem() != null) {
				Line line = (Line) getView().getLineComboBox().getSelectedItem();
				lineName = line.getLineName().trim();
				Division division = (Division) getView().getDivisonComboBox().getSelectedItem();
				divisionName = division.getDivisionName().trim();
				
				if (!actionName.equals(SELECT_QCACTION)) {					
					if ((!lineName.equals("")) && !(lineName.equals(SELECT_LINE))) {
							reasonMappings = getDao(HoldReasonMappingDao.class).findAllByLineAndAction(line.getLineId(),QCAction.getQCAction(action).getQcActionId());
					} else if (!(divisionName.equals("")) && !(divisionName.equals(SELECT_DIVISION))) {
							reasonMappings = getDao(HoldReasonMappingDao.class).findAllByDivisionAndAction(division.getDivisionId(),QCAction.getQCAction(action).getQcActionId());
					} else {
						reasonMappings = getDao(HoldReasonMappingDao.class).findAllByAction(QCAction.getQCAction(action).getQcActionId());
					}
				} else {
					if ((!lineName.equals("")) && !(lineName.equals(SELECT_LINE))) {
						reasonMappings = getDao(HoldReasonMappingDao.class).findAllByLine(line.getLineId());
					} else if (!(divisionName.equals("")) && !(divisionName.equals(SELECT_DIVISION))) {
							reasonMappings = getDao(HoldReasonMappingDao.class).findAllByDivision(division.getDivisionId());
					}
				}
			}

		}

		getView().getHoldReasonMappingPane().reloadData(reasonMappings);
	}
	
	public List<Plant> getPlants() {
		List<Plant> plants = Config.getPlants();
		Collections.sort(plants, new PropertyComparator<Plant>(Plant.class, "plantName"));
		return plants;
	}
	
	public List<Division> getDivisions() {
		List<Division> divisions = new ArrayList<Division>(); 
		Plant plant = (Plant) getView().getPlantComboBox().getSelectedItem();
		if (!plant.getPlantName().trim().equals(SELECT_PLANT)) {
			divisions = ServiceFactory.getDao(DivisionDao.class).findById(plant.getId().getSiteName(), plant.getPlantName());
		} 
		Collections.sort(divisions, new PropertyComparator<Division>(Division.class, "divisionName"));
		divisions.add(0,createDummyDivision());
		return divisions;
	}
	
	public List<Line> getLines() {
		List<Line> lines = new ArrayList<Line>(); 
		Collections.sort(lines, new PropertyComparator<Line>(Line.class, "lineName"));
		lines.add(0,createDummyLine());
		return lines;
	}
	
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		for(String action : QCAction.getQCActionNames().keySet()) {
			actions.add(action);
		}

		return actions;
	}
	
	// === ListSelectionListener === //
	public void valueChanged(ListSelectionEvent lse) {
		try {
			if (lse.getSource().equals(getView().getHoldReasonPane().getTable().getSelectionModel())){
				getView().getHoldReasonMappingPane().clearSelection();
				
			} else if (lse.getSource().equals(getView().getHoldReasonMappingPane().getTable().getSelectionModel())){
				getView().getHoldReasonPane().clearSelection();
			}
			enableAddQueryButtons();
		} catch (Exception e) {
			handleException(e, lse);
		}
	}

}
