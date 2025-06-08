package com.honda.galc.client.teamleader;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import com.honda.galc.client.teamleader.model.ScheduleTableModel;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.DailyDepartmentScheduleId;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * <h3>DailyDeptScheduleMaintenancePanel</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DailyDeptScheduleMaintenancePanel description </p>
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
/**
 * 
 * @author Gangadhararao Gadde
 * @date June 15, 2016
 */
public class DailyDeptScheduleMaintenancePanel extends ScheduleMaintenancePanel {
	private static final long serialVersionUID = 1L;
	static final String DATE_FORMAT = "yyyy-MM-dd";
	protected static final String FIRST_SHIFT = "1";
	protected static final String SECOND_SHIFT = "2";
	protected static final String THIRD_SHIFT = "3";
	private DateBean dateBean;
	private List<DailyDepartmentSchedule> allRecords;
	
	public DailyDeptScheduleMaintenancePanel() {
		super("Daily Department Schedule",  KeyEvent.VK_D);
		initApplication();
	}

	private void initApplication() {
		dao = ServiceFactory.getDao(DailyDepartmentScheduleDao.class);
		model = new ScheduleTableModel(this, new ArrayList<Object[]>());
		setDefaultTimeZone();
		getProcessInfo();
	}
	
	private void setDefaultTimeZone() {
		try {
			String defaultTimeZoneId=PropertyService.getPropertyBean(SystemPropertyBean.class).getDefaultTimeZoneId();
			if(!StringUtils.isBlank(defaultTimeZoneId))
			{
				TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZoneId.trim()));
				getLogger().info("Successfully set the default Time Zone to "+defaultTimeZoneId.trim());
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	@Override
	protected JPanel getDatePanel() {
		if(datePanel == null){
			datePanel = new JPanel();
			datePanel.setLayout(new MigLayout("insets 0 0 0 0, center"));
			datePanel.setBorder(new TitledBorder("Date"));
			datePanel.add(getDateBean());
		}
		
		return datePanel;
	}

	private DateBean getDateBean() {
		if(dateBean == null){
			dateBean = new DateBean();
			dateBean.setName("DateBean");
			dateBean.setMinimumSize(new Dimension(186,55));
		}
		return dateBean;
	}
	
	private void setDayProdTime() {
		ArrayList<DailyDepartmentSchedule> daySchedules = new ArrayList<DailyDepartmentSchedule>();
		String day = getDateBean().getDate(DATE_FORMAT);
		if (this.allRecords != null) {
			for (DailyDepartmentSchedule schedule : this.allRecords)
				if (schedule.getId().getProductionDate().toString().equals(day)) daySchedules.add(schedule);
		}
		this.dayProdTime.getComponent().setText(this.getProdTime(daySchedules));
		this.dayProdCap.getComponent().setText(this.getProdCapacity(daySchedules));
	}
	
	private void setShiftProdTime(String shift) {
		ArrayList<DailyDepartmentSchedule> shiftSchedules = new ArrayList<DailyDepartmentSchedule>();
		String day = getDateBean().getDate(DATE_FORMAT);
		if (this.allRecords != null) {
			for (DailyDepartmentSchedule schedule : this.allRecords)
				if (schedule.getId().getProductionDate().toString().equals(day) &&
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
		} else return;
	}
	
	private String getProdTime(ArrayList<DailyDepartmentSchedule> scheduleList){
		int msPerDay = 24 * 60 * 60 * 1000;
		long total = 0;
		for (DailyDepartmentSchedule schedule : scheduleList) {
			if (!schedule.isPlan()) continue;
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
	
	private String getProdCapacity(ArrayList<DailyDepartmentSchedule> scheduleList){
		int total = 0;
		for (DailyDepartmentSchedule schedule : scheduleList) {	
			if (!schedule.isPlan()) continue;
			total = total + schedule.getCapacity();
		}
		return Integer.toString(total);
	}

	@Override
	protected void doQuery() {
		try{
			setErrorMessage("");
		    List<Object[]> schedules = dao.getSchedule((String)departmentSelectionPanel.getPlantComboBox().getComponent().getSelectedItem(),
				(String)departmentSelectionPanel.getLineComboBox().getComponent().getSelectedItem(),
				(String)departmentSelectionPanel.getDepartmentComboBox().getComponent().getSelectedItem(),
				getDateBean().getDate(DATE_FORMAT));
		
		     model.refresh(schedules);
		     this.updateProdTimes();
		} catch(Exception e){
			handleException(e);
		}
		
	}
	
	protected void updateProdTimes() {
		DailyDepartmentScheduleId id = new DailyDepartmentScheduleId();
		id.setPlantCode((String)departmentSelectionPanel.getPlantComboBox().getComponent().getSelectedItem());
		id.setLineNo((String)departmentSelectionPanel.getLineComboBox().getComponent().getSelectedItem());
		id.setProcessLocation((String)departmentSelectionPanel.getDepartmentComboBox().getComponent().getSelectedItem());
		id.setProductionDate(new java.sql.Date(getDateBean().currentDate.getTime()));
		id.setShift("");
	     
		this.allRecords = ServiceFactory.getDao(DailyDepartmentScheduleDao.class).findAllById(id);
		this.setDayProdTime();
		this.setShiftProdTime(FIRST_SHIFT);
		this.setShiftProdTime(SECOND_SHIFT);
		this.setShiftProdTime(THIRD_SHIFT);
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
					schedule[9] = getDateBean().getDate(DATE_FORMAT);
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
