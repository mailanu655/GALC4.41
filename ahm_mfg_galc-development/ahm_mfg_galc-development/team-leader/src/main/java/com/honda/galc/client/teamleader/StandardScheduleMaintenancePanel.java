package com.honda.galc.client.teamleader;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.lang.time.DurationFormatUtils;

import com.honda.galc.client.teamleader.model.ScheduleTableModel;
import com.honda.galc.client.ui.component.ComboBoxModel;
import com.honda.galc.client.ui.component.LabeledComboBox;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.StandardScheduleDao;
import com.honda.galc.entity.product.StandardSchedule;
import com.honda.galc.entity.product.StandardScheduleId;
import com.honda.galc.service.ServiceFactory;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>StandardScheduleMaintenancePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> StandardScheduleMaintenancePanel description </p>
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
 *
 * </TABLE>
 *   
 * @author Paul Chou
 * Mar 2, 2011
 *
 */

public class StandardScheduleMaintenancePanel extends ScheduleMaintenancePanel {
	private static final long serialVersionUID = 1L;
	protected static final String FIRST_SHIFT = "1";
	protected static final String SECOND_SHIFT = "2";
	protected static final String THIRD_SHIFT = "3";
	private LabeledComboBox dateComboBox;
	private HashSet<String>days = new HashSet<String>();
	ArrayList<String> dayOfWeek = new ArrayList<String>();
	List<Object[]> schedules;
	private List<StandardSchedule> allRecords;
	
	
	public StandardScheduleMaintenancePanel() {
		super("Standard Schedule Maintenance", KeyEvent.VK_S);
		initApplication();
	}

	private void initApplication() {
		dao = ServiceFactory.getDao(StandardScheduleDao.class);
		model = new ScheduleTableModel(this, new ArrayList<Object[]>());
		
		dayOfWeek.add("SUN");
		dayOfWeek.add("MON");
		dayOfWeek.add("TUE");
		dayOfWeek.add("WED");
		dayOfWeek.add("THU");
		dayOfWeek.add("FRI");
		dayOfWeek.add("SAT");
		
		getProcessInfo();
	}
	
	@Override
	protected JPanel getDatePanel() {
		if(datePanel == null){
			datePanel = new JPanel(new MigLayout());
			datePanel.add(getDateComboBox(),"gapbottom 4");
		}
		
		return datePanel;
	}

	private LabeledComboBox getDateComboBox() {
		if(dateComboBox == null){
			dateComboBox = new LabeledComboBox("Day", false);
			dateComboBox.setBorder(null);
			dateComboBox.getLabel().setHorizontalAlignment(JTextField.CENTER);
			dateComboBox.getComponent().setPreferredSize(new Dimension(60,25));
			((BorderLayout)dateComboBox.getLayout()).setVgap(3);
		}
		return dateComboBox;
	}
	
	private void setDayProdTotals() {
		ArrayList<StandardSchedule> daySchedules = new ArrayList<StandardSchedule>();
		String day = (String)getDateComboBox().getComponent().getSelectedItem();
		if (this.allRecords != null) {
			for (StandardSchedule schedule : this.allRecords)
				if (schedule.getId().getDayOfWeek().trim().equals(day)) 
					daySchedules.add(schedule);
		}
		this.dayProdTime.getComponent().setText(this.getProdTime(daySchedules));
		this.dayProdCap.getComponent().setText(this.getProdCapacity(daySchedules));
	}
	
	private void setShiftProdTotals(String shift) {
		ArrayList<StandardSchedule> shiftSchedules = new ArrayList<StandardSchedule>();
		String day = (String)getDateComboBox().getComponent().getSelectedItem();
		if (this.allRecords != null) {
			for (StandardSchedule schedule : this.allRecords)
				if (schedule.getId().getDayOfWeek().trim().equals(day) &&
					schedule.getId().getShift().trim().replaceFirst("^0+(?!$)", "").equals(shift))
					shiftSchedules.add(schedule);
		}
		if (shift.equals(FIRST_SHIFT)) {
			this.shiftOneProdTime.getComponent().setText(this.getProdTime(shiftSchedules));
			this.shiftOneProdCap.getComponent().setText(this.getProdCapacity(shiftSchedules));
		} else if (shift.equals(SECOND_SHIFT)) {
			this.shiftTwoProdTime.getComponent().setText(this.getProdTime(shiftSchedules));
			this.shiftTwoProdCap.getComponent().setText(this.getProdCapacity(shiftSchedules));
		} else if (shift.equals(THIRD_SHIFT)) {
			this.shiftThreeProdTime.getComponent().setText(this.getProdTime(shiftSchedules));
			this.shiftThreeProdCap.getComponent().setText(this.getProdCapacity(shiftSchedules));
		}else return;
	}
	
	private String getProdTime(ArrayList<StandardSchedule> scheduleList){
		int msPerDay = 24 * 60 * 60 * 1000;
		long total = 0;
		for (StandardSchedule schedule : scheduleList) {	
			if (schedule.getPlan().equals("N")) continue;
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			Date startDate = new Date();
			Date endDate = new Date();
			try {
				startDate = format.parse(schedule.getStartTime().toString());
				endDate = format.parse(schedule.getEndTime().toString());
			} catch (ParseException e) {
				Logger.getLogger().error(e);
			}
			long difference;
			if (startDate.before(endDate)) difference = endDate.getTime() - startDate.getTime();
			else difference = endDate.getTime() - startDate.getTime() + msPerDay;

			total = total+difference;
		}
		return DurationFormatUtils.formatDuration(total, "HH:mm:ss");
	}
	
	private String getProdCapacity(ArrayList<StandardSchedule> scheduleList){
		int total = 0;
		for (StandardSchedule schedule : scheduleList) {	
			if (schedule.getPlan().equals("N")) continue;
			total = total + schedule.getCapacity();
		}
		return Integer.toString(total);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	protected void getProcessInfo() {
		List<Object[]> processInfo = dao.getProcessInfo();
		
		for(Object[]info : processInfo){
			plantCodes.add((String)info[0]);
			lineNos.add((String)info[1]);
			processLocations.add((String)info[2]);
			days.add((String)info[3]);
		}
		
		departmentSelectionPanel.getPlantComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(plantCodes)), 0);
		departmentSelectionPanel.getLineComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(lineNos)), 0);
		departmentSelectionPanel.getDepartmentComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(processLocations)), 0);
		
		if(dayOfWeek.containsAll(days)) getDateComboBox().setModel(new ComboBoxModel<String>(dayOfWeek), 0);
		else getDateComboBox().setModel(new ComboBoxModel<String>(new ArrayList<String>(days)),0);
	}
	
	
	@Override
	protected void doQuery() {
		try{
			setErrorMessage("");
			this.schedules = dao.getSchedule((String)departmentSelectionPanel.getPlantComboBox().getComponent().getSelectedItem(),
			(String)departmentSelectionPanel.getLineComboBox().getComponent().getSelectedItem(),
			(String)departmentSelectionPanel.getDepartmentComboBox().getComponent().getSelectedItem(),
			(String)getDateComboBox().getComponent().getSelectedItem());
		
		    model.refresh(this.schedules);
		    this.updateProdTotals();	     
		} catch(Exception e){
			handleException(e);
		}
	}
	
	protected void updateProdTotals() {
		StandardScheduleId id = new StandardScheduleId();
		id.setPlantCode((String)departmentSelectionPanel.getPlantComboBox().getComponent().getSelectedItem());
		id.setLineNo((String)departmentSelectionPanel.getLineComboBox().getComponent().getSelectedItem());
		id.setProcessLocation((String)departmentSelectionPanel.getDepartmentComboBox().getComponent().getSelectedItem());
		id.setDayOfWeek("");
		id.setShift("");
	     
		this.allRecords = ServiceFactory.getDao(StandardScheduleDao.class).findAllById(id);
		this.setDayProdTotals();
		this.setShiftProdTotals(FIRST_SHIFT);
		this.setShiftProdTotals(SECOND_SHIFT);
		this.setShiftProdTotals(THIRD_SHIFT);
	}

	@Override
	protected void doSave() {
		// First check for row errors
		if (model.getErroredRow() >= 0) {
			setErrorMessage("Cannot <Save> when errors present");
			return;
		}
		
		model.setClearPending(false);
		try {
			setErrorMessage("");
			List<Object[]> schedules = new ArrayList<Object[]>();

			for(int r = 0; r < model.getRowCount(); r++){
				// only save in changed data
				if(model.isRowChanged(r)){
					Object[] schedule = new Object[11];
					for(int c = 5; c < model.getColumnCount(); c++){
						schedule[c - 5] = model.getValueAt(r,c);
					}

					schedule[6] = 
						departmentSelectionPanel.getPlantComboBox().getComponent().getSelectedItem();
					schedule[7] = 
						departmentSelectionPanel.getLineComboBox().getComponent().getSelectedItem();
					schedule[8] = 
						departmentSelectionPanel.getDepartmentComboBox().getComponent().getSelectedItem();
					schedule[9] = getDateComboBox().getComponent().getSelectedItem();
					schedule[10] = model.getValueAt(r,1);

					schedules.add(schedule);
				}
			}

			dao.saveSchedules(schedules);
			clearScreen();
		}
		catch(Exception e){
			handleException(e);
		}
	}
	
	


}
