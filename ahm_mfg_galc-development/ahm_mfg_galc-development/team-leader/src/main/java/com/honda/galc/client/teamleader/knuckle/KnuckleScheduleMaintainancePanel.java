package com.honda.galc.client.teamleader.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.product.PreProductionLot;

/**
 * 
 * <h3>KnuckleScheduleMaintainanePanel Class description</h3>
 * <p> KnuckleScheduleMaintainanePanel description </p>
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
 * Jan 8, 2013
 *
 *
 */
public class KnuckleScheduleMaintainancePanel extends ApplicationMainPanel implements ListSelectionListener, ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static final String PROCESS_LOCATION_KN ="KN";
	private static final String PROCESS_LOCATION_KR ="KR";
	
	private List<PreProductionLot> knLots;
	private List<PreProductionLot> krLots;
	
	private ObjectTablePane<PreProductionLot> knSchedulePane;
	
	private ObjectTablePane<PreProductionLot> krSchedulePane;
	
	private JButton moveLeftButton = new JButton("Move  To  Left <<");
	
	private JButton moveRightButton = new JButton("Move To Right >>");
	
	private JButton refreshButton = new JButton("   Refresh");
	
	private final int BUTTON_WIDTH = 150;
	
	private Date lastUpdateTimeKN; 
	private Date lastUpdateTimeKR; 
	
	public KnuckleScheduleMaintainancePanel(DefaultWindow window) {
		super(window);
		initComponents();
		window.pack();
		loadData();
		addListeners();
	}
	
	private void initComponents(){
		
		setLayout(new BorderLayout());
		knSchedulePane = createSchedulePane("Existing Knuckle Schedule");
		krSchedulePane = createSchedulePane("Robot Line Schedule");
		
		ViewUtil.setMaxWidth(moveLeftButton, BUTTON_WIDTH);
		ViewUtil.setMaxWidth(moveRightButton, BUTTON_WIDTH);
		ViewUtil.setMaxWidth(refreshButton, BUTTON_WIDTH);
		
		JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new BoxLayout(commandPanel,BoxLayout.Y_AXIS));
		commandPanel.add(moveLeftButton);
		commandPanel.add(Box.createVerticalStrut(20));
		commandPanel.add(moveRightButton);
		commandPanel.add(Box.createVerticalStrut(20));
		commandPanel.add(refreshButton);
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.X_AXIS));
		topPanel.add(knSchedulePane);
		topPanel.add(Box.createHorizontalStrut(10));
		topPanel.add(commandPanel);
		topPanel.add(Box.createHorizontalStrut(10));
		topPanel.add(krSchedulePane);
		
		
		add(topPanel,BorderLayout.CENTER);
	}
	
	private ObjectTablePane<PreProductionLot> createSchedulePane(String title) {
		ColumnMappings clumnMappings = ColumnMappings.with("#", "row")
			.put("Production Lot", "productionLot")
			.put("KD Lot", "kdLot")
			.put("Lot Size", "lotSize");
		
		ObjectTablePane<PreProductionLot> pane = new ObjectTablePane<PreProductionLot>(title,clumnMappings.get());
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		return pane;
	}
	
	private void loadData(){
		knLots = loadPreProductLots(PROCESS_LOCATION_KN);
		krLots = loadPreProductLots(PROCESS_LOCATION_KR);
		knSchedulePane.reloadData(knLots);
		krSchedulePane.reloadData(krLots);
		knSchedulePane.clearSelection();
		krSchedulePane.clearSelection();
		moveLeftButton.setEnabled(false);
		moveRightButton.setEnabled(false);
		knSchedulePane.scrollToBottom();
		krSchedulePane.scrollToBottom();
		lastUpdateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KN);
		lastUpdateTimeKR = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KR);
	}
	
	private List<PreProductionLot> loadPreProductLots(String processLocation){
		List<PreProductionLot> lots = getDao(PreProductionLotDao.class).findAllPreProductionLotsByProcessLocation(processLocation);
		if(lots.isEmpty()) return lots;
		
		List<PreProductionLot> currentLots = getDao(PreProductionLotDao.class)
        .findAllWithSameKdLotCurrentlyProcessed(lots.get(0).getProductionLot());
		if(currentLots.isEmpty()) return lots;
		return removeLotsWithSameKdLot(lots, currentLots.get(0).getKdLot());
	}
	
	private List<PreProductionLot> removeLotsWithSameKdLot(List<PreProductionLot> lots,String kdLotNumber) {
		List<PreProductionLot> removedLots = new ArrayList<PreProductionLot>();
		for(PreProductionLot lot : lots) {
			// only find first a few consecutive lots
			if(lot.isSameKdLot(kdLotNumber)) removedLots.add(lot); 
			else break;
		}
		lots.removeAll(removedLots);
		return lots;
	}
	
	private void addListeners() {
		knSchedulePane.getTable().getSelectionModel().addListSelectionListener(this);
		krSchedulePane.getTable().getSelectionModel().addListSelectionListener(this);
		moveLeftButton.addActionListener(this);
		moveRightButton.addActionListener(this);
		refreshButton.addActionListener(this);
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) return;
		
		clearErrorMessage();
		if(e.getSource() == knSchedulePane.getTable().getSelectionModel()) {
			knuckleScheduleSelected(knSchedulePane,knLots);
			krSchedulePane.clearSelection();
			moveLeftButton.setEnabled(false);
			moveRightButton.setEnabled(true);
		}else if(e.getSource() == krSchedulePane.getTable().getSelectionModel()) {
			knuckleScheduleSelected(krSchedulePane,krLots);
			knSchedulePane.clearSelection();
			moveLeftButton.setEnabled(true);
			moveRightButton.setEnabled(false);
		};
	}
		
	private void knuckleScheduleSelected(ObjectTablePane<PreProductionLot> schedulePane,
			List<PreProductionLot> lots){
		int[] rows = schedulePane.getTable().getSelectedRows();
		if(rows.length == 0) return;
		int index = getFirstSameKdLotIndex(lots, rows[0]);
		if(index != rows[0] || lots.size() -1 != rows[rows.length -1]){
			schedulePane.clearSelection();
			schedulePane.getTable().getSelectionModel().setSelectionInterval(index, lots.size()-1);
		}

	}

	public void actionPerformed(ActionEvent e) {
		clearErrorMessage();
		String message = null;
		try{
			if(e.getSource().equals(moveRightButton)){
				if(movePreProductionLots(knSchedulePane,knLots,PROCESS_LOCATION_KR))
					message = "Schedules have been moved to \"" + PROCESS_LOCATION_KR + "\" successfully";
			}else if(e.getSource().equals(moveLeftButton)){
				if(movePreProductionLots(krSchedulePane,krLots,PROCESS_LOCATION_KN))
					message = "Schedules have been moved to \"" + PROCESS_LOCATION_KN + "\" successfully";
			}else if(e.getSource().equals(refreshButton)){
				message = "Schedule data is refreshed successfully";
			}
			if(message != null){
				loadData();
				setMessage(message);
			}
			
		}catch(Exception ex) {
			handleException(ex);
		}
	}
	
	private boolean movePreProductionLots(ObjectTablePane<PreProductionLot> schedulePane,
			List<PreProductionLot> lots,String processLocation) {
		if(checkScheduleChanged()) {
			setErrorMessage("Could not move the schedules because schedules were changed on other clients. Please refresh.");
			return false;
		}
		
		List<PreProductionLot> selectedLots = schedulePane.getSelectedItems();
		PreProductionLot lot = selectedLots.get(0);
		if(isFirstKdLotSelected(lots, lot)){
			setErrorMessage("First Kd Lot is locked. You cannot move the first lot");
			return false;
		};
		
		if(isCheckProcessedLots() && processLocation.equals(PROCESS_LOCATION_KN)){
			List<PreProductionLot> processedLots = checkProcessedLots(selectedLots,processLocation);
			if(!processedLots.isEmpty()){
				setErrorMessage("Could not move lots due to lots in knuckle automation cells. Lot : " + processedLots.get(0).getProductionLot());
				return false;
			}
		}
		getLogger().info("the start production lot " + lot.getProductionLot() + " will be moved to process location " + processLocation);
		getDao(PreProductionLotDao.class).movePreProductionLots(lot.getProductionLot(), processLocation);
		getLogger().info("production lots are moved ");
		return true;
	}
	
	private int getFirstSameKdLotIndex(List<PreProductionLot> lots, int currentIndex) {
		if(currentIndex == 0) return currentIndex;
		PreProductionLot currentLot = lots.get(currentIndex);
		int index = currentIndex;
		while(index >0 && currentLot.isSameKdLot(lots.get(index -1))){
			index--;
		}
		return index;
	}
	
	private List<PreProductionLot> checkProcessedLots(List<PreProductionLot> lots,String processLocation){
		
		List<PreProductionLot> nonProcessedLots = 
				getDao(PreProductionLotDao.class).findAllNonProcessedLots(PROCESS_LOCATION_KR, "MS0FK16001");
		for(PreProductionLot lot : nonProcessedLots){
			if(lots.contains(lot)) lots.remove(lot);
		}
		return lots;
		
	}
	
	private boolean isFirstKdLotSelected(List<PreProductionLot> lots,PreProductionLot lot){
		if(lots.isEmpty()) return false;
		return lots.get(0).isSameKdLot(lot);
	}
	private boolean checkScheduleChanged(){
		Date updateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KN);
		Date updateTimeKR = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KR);
		
		boolean isChangedKN = updateTimeKN != null && (lastUpdateTimeKN == null || updateTimeKN.after(lastUpdateTimeKN));
		boolean isChangedKR = updateTimeKR != null && (lastUpdateTimeKR == null || updateTimeKR.after(lastUpdateTimeKR));
			
		return isChangedKN || isChangedKR;
	}
	
	private boolean isCheckProcessedLots(){
		return getPropertyBoolean("CHECK_PROCESSED_LOTS", false);
	}
	
}
