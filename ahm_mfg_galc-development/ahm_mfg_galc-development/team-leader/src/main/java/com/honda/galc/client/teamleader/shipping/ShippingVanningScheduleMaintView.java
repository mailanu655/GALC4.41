package com.honda.galc.client.teamleader.shipping;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>ShippingVanningScheduleMaintView Class description</h3>
 * <p> ShippingVanningScheduleMaintView description </p>
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
 * @author Jeffray Huang<br>
 * Mar 16, 2015
 *
 *
 */
public class ShippingVanningScheduleMaintView extends ApplicationMainPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<ShippingVanningSchedule> schedulePane;
	private JButton refreshButton = new JButton("Refresh");
	private JButton moveUpButton = new JButton("Move Up");
	private JButton moveDownButton = new JButton("Move Down");
	
	public ShippingVanningScheduleMaintView(DefaultWindow window) {
		super(window);
		initComponents();
		window.pack();
		refresh();
		addListeners();
	}
	
	private void initComponents(){
		setLayout(new BorderLayout(10,10));
		setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));
		add(new JLabel("Vanning Schedule Maintenance"),BorderLayout.NORTH);
		add(schedulePane = createSchedulePane());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalGlue());
		panel.add(refreshButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(moveUpButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(moveDownButton);
		panel.add(Box.createHorizontalGlue());
				add(panel,BorderLayout.SOUTH);
	}
	
	private void loadData(){
		
		List<ShippingVanningSchedule> vanningSchedules = ServiceFactory.getDao(ShippingVanningScheduleDao.class).findAllActiveVanningSchedules();
		
		for(ShippingVanningSchedule schedule :vanningSchedules) {
			schedule.setPlantCode(getPropertyBean().getShippingPlants().get(schedule.getPlant()));
		}
		schedulePane.clearSelection();
		schedulePane.reloadData(vanningSchedules);
	}
	
	private void addListeners() {
		refreshButton.addActionListener(this);
		moveUpButton.addActionListener(this);
		moveDownButton.addActionListener(this);
	}
	
	private void refresh(){
		ServiceFactory.getDao(ShippingVanningScheduleDao.class).syncVanningSchedule();
		
		loadData();
	}
	
	private void moveUp() {
		int[] rows = this.schedulePane.getTable().getSelectedRows();
		if(rows.length == 0) return;
		int firstRow = rows[0];
		
		List<ShippingVanningSchedule> allSchedules = schedulePane.getItems();
    	
        if(firstRow == 0){
        	
        }else if(allSchedules.get(firstRow -1).getTrailerId()!=null) {
            // not allow to move before an assigned vanning schedule
        	setErrorMessage("Cannot move the schedules because kd lot " + allSchedules.get(firstRow -1).getKdLot() + 
        				" has assigned trailer id " + allSchedules.get(firstRow -1).getTrailerId());
        }else {
        	if(!checkAssignedSchedules(schedulePane.getSelectedItems())) return;
        	
            List<ShippingVanningSchedule> schedules = new ArrayList<ShippingVanningSchedule>();
            ShippingVanningSchedule lastSchedule = allSchedules.get(firstRow -1);
            
            ShippingVanningSchedule tempSchedule = lastSchedule.clone();
            this.replaceSchedule(allSchedules.get(rows[0]),lastSchedule);
            
            schedules.add(lastSchedule);
            
            for (int i= 1; i< rows.length; i++) {
                this.replaceSchedule(allSchedules.get(rows[i]),allSchedules.get(rows[i-1]));
                schedules.add(allSchedules.get(rows[i-1]));
            }
            
            this.replaceSchedule(tempSchedule,allSchedules.get(rows[rows.length -1]));
            schedules.add(allSchedules.get(rows[rows.length -1]));
            
            ServiceFactory.getDao(ShippingVanningScheduleDao.class).saveAll(schedules);
            loadData();
            int rowNum = findRowIndex(schedules.get(0));
            schedulePane.getTable().getSelectionModel().setSelectionInterval(rowNum, rowNum + rows.length -1);
            setMessage("Move Schedules successfully");
        }
	}
	
	private void moveDown() {
		int[] rows = this.schedulePane.getTable().getSelectedRows();
		if(rows.length == 0) return;
		int lastRow = rows[rows.length -1];
		
		List<ShippingVanningSchedule> allSchedules = schedulePane.getItems();
    	
        if(lastRow == allSchedules.size() -1) {
            // not allow to move before an assigned vanning schedule
        }else {
        	if(!checkAssignedSchedules(schedulePane.getSelectedItems())) return;
        	
            List<ShippingVanningSchedule> schedules = new ArrayList<ShippingVanningSchedule>();
            ShippingVanningSchedule nextSchedule = allSchedules.get(lastRow +1);
            
            ShippingVanningSchedule tempSchedule = nextSchedule.clone();
            
            this.replaceSchedule(allSchedules.get(rows[rows.length -1]),nextSchedule);
            schedules.add(nextSchedule);
      
            for (int i= rows.length -1; i > 0; i--) {
                this.replaceSchedule(allSchedules.get(rows[i-1]), allSchedules.get(rows[i]));
                schedules.add(allSchedules.get(rows[i])); 
            }
            
            this.replaceSchedule(tempSchedule,allSchedules.get(rows[0]));
            schedules.add(allSchedules.get(rows[0]));
           
            ServiceFactory.getDao(ShippingVanningScheduleDao.class).saveAll(schedules);
            loadData();
            int rowNum = findRowIndex(schedules.get(schedules.size() -2));
            schedulePane.getTable().getSelectionModel().setSelectionInterval(rowNum, rowNum + rows.length -1);
            
            setMessage("Move Schedules successfully");
        }
	}
	
	private int findRowIndex(ShippingVanningSchedule schedule) {
		List<ShippingVanningSchedule> allSchedules = schedulePane.getItems();
    	
		for(int i = 0; i < allSchedules.size();i++) {
			if(schedule.getId().equals(allSchedules.get(i).getId())) return i; 
		}
		return -1;
	}
	
	private boolean checkAssignedSchedules(List<ShippingVanningSchedule> schedules) {
		for(ShippingVanningSchedule schedule : schedules) {
			ShippingVanningSchedule tmpSchedule = ServiceFactory.getDao(ShippingVanningScheduleDao.class).findByKey(schedule.getId());
			if(tmpSchedule != null && tmpSchedule.getTrailerId() != null) {
				setErrorMessage("Cannot move the schedules because kd lot " + tmpSchedule.getKdLot() + " has assigned trailer id " + tmpSchedule.getTrailerId());
				return false;
			}
		}
		return true;
	}
	
	private void replaceSchedule(ShippingVanningSchedule fromSchedule, ShippingVanningSchedule toSchedule){
        toSchedule.setProductionLot(fromSchedule.getProductionLot());
        toSchedule.setKdLot(fromSchedule.getKdLot());
        toSchedule.setSchQty(fromSchedule.getSchQty());
        toSchedule.setYmto(fromSchedule.getYmto());
        toSchedule.setActQty(fromSchedule.getActQty());
    }
	
	private ObjectTablePane<ShippingVanningSchedule> createSchedulePane() {
		ColumnMappings clumnMappings = ColumnMappings
			.with("Plant","plantCode").put("KD Order Number","kdLot")
			.put("YMTO","ymto").put("SchQty","schQty")
			.put("ActQty","actQty").put("Trailer #","trailerNumber");
	
		ObjectTablePane<ShippingVanningSchedule> pane = new ObjectTablePane<ShippingVanningSchedule>(clumnMappings.get(),false);
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 16));
		pane.getTable().setRowHeight(22);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}

	public void actionPerformed(ActionEvent e) {
		clearErrorMessage();
		if(e.getSource().equals(refreshButton)) refresh();
		else if(e.getSource().equals(moveUpButton)) moveUp();
		else if(e.getSource().equals(moveDownButton)) moveDown();
	}
	
	public EngineShippingPropertyBean getPropertyBean() {
		String ppid = getProperty("SHIPPING_PPID");
		if(StringUtils.isEmpty(ppid)) throw new TaskException("Property SHIPPING_PPID is not configured");
		
		return PropertyService.getPropertyBean(EngineShippingPropertyBean.class, ppid);
	}
}
