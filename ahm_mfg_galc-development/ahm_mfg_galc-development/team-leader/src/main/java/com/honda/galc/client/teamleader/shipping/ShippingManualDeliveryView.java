package com.honda.galc.client.teamleader.shipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ui.ApplicationMainPanel;
import com.honda.galc.client.ui.DefaultWindow;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineManifestDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.enumtype.ShippingQuorumDetailStatus;
import com.honda.galc.entity.enumtype.ShippingQuorumStatus;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.EngineShippingHelper;

/**
 * 
 * 
 * <h3>ShippingManualDeliveryView Class description</h3>
 * <p> ShippingManualDeliveryView description </p>
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
 * Feb 2, 2015
 *
 *
 */
public class ShippingManualDeliveryView extends ApplicationMainPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<ShippingVanningSchedule> kdLotPane;
	
	private LabeledTextField engineNumberField = new LabeledTextField("Enter Engine Number To Be Loaded:",false);
	
	private JLabel kdLotLabel = new JLabel("Select KD LOT Your Want To Load In:");
	
	private JButton loadButton = new JButton("Load");
	private JButton refreshButton = new JButton("Refresh");
	private EngineShippingHelper engineShippingHelper;
	
	public ShippingManualDeliveryView(DefaultWindow window) {
		super(window);
		initComponents();
		window.pack();
		refresh();
		addListeners();
	}
	
	private void initComponents(){
		setLayout(new MigLayout("insets 20 200 20 200", "[grow,fill]"));
		
		engineNumberField.setFont(new Font("sansserif", 1, 20));
		kdLotLabel.setFont(new Font("sansserif", 1, 18));
		add(engineNumberField,"gapleft 100,gapright 100,span");
		add(kdLotLabel,"span");
		add(kdLotPane = createKdLotPane(),"span");
		add(createButtonPanel());
	}
	
	private void loadData(){
		List<ShippingVanningSchedule> kdLots = 
			ServiceFactory.getDao(ShippingVanningScheduleDao.class).findAllPartialLoadSchedules();
		
		kdLotPane.reloadData(kdLots);
	}
	
	private JPanel createButtonPanel() {
		refreshButton.setFont(new Font("sansserif", 1, 18));
		loadButton.setFont(new Font("sansserif", 1, 18));
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel,BoxLayout.X_AXIS));
		panel.add(Box.createHorizontalStrut(200));
		panel.add(loadButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(refreshButton);
		panel.add(Box.createHorizontalStrut(200));
		return panel;
	}
	
	
	private void addListeners() {
		refreshButton.addActionListener(this);
		loadButton.addActionListener(this);
		engineNumberField.getComponent().addActionListener(this);
	}
	
	private void refresh(){
		loadData();
		engineNumberField.getComponent().setText("");
		engineNumberField.getComponent().requestFocusInWindow();
	}
	
	private void loadEngine() {
		if(!basicLoadEngine()) {
			engineNumberField.getComponent().selectAll();
			engineNumberField.getComponent().requestFocusInWindow();
		}
	}
	
	private boolean basicLoadEngine() {
		ShippingVanningSchedule schedule = kdLotPane.getSelectedItem();
		if(kdLotPane.getItems().isEmpty()){
			getMainWindow().setErrorMessage("No incompleted KD Lots Found");
			return false;
		}else if(schedule == null) {
			getMainWindow().setErrorMessage("Please select a KD Lot");
			return false;
		}
		String ein = StringUtils.trimToEmpty(engineNumberField.getComponent().getText());
		if(StringUtils.isEmpty(ein)) {
			getMainWindow().setErrorMessage("Please enter engine number:");
			return false;
		}
		getLogger().info("Engine number is scanned : " + ein);
		
		String message = checkEngine(ein,schedule.getYmto());
		if(message != null) {
			setErrorMessage(message);
			return false;
		}
		
		removeEngineFromQuorum(ein);
		
		loadEngineNumber(schedule, ein);
		
		trackEngine(ein);
		
		String msg = "Loaded Engine " + ein + " successfully";
		getLogger().info(msg);
		setMessage(msg);

		refresh();
		
		return true;
	}
	
	private boolean loadEngineNumber(ShippingVanningSchedule schedule,String engineNumber) {
		
		
		ShippingTrailerInfo trailerInfo = ServiceFactory.getDao(ShippingTrailerInfoDao.class).findByKey(schedule.getTrailerId());
		List<ShippingQuorum> quorums = getDao(ShippingQuorumDao.class).findAllByTrailerId(schedule.getTrailerId());
		trailerInfo.setShippingQuorums(quorums);
		for(ShippingQuorum quorum : trailerInfo.getShippingQuorums()) {
			for(ShippingQuorumDetail detail : quorum.getShippingQuorumDetails()) {
				if(StringUtils.isEmpty(detail.getEngineNumber()) && schedule.getYmto().equalsIgnoreCase(detail.getYmto())) {
					detail.setEngineNumber(engineNumber);
					if(quorum.getLoadedCount() == quorum.getActualQuorumSize()){
						ServiceFactory.getDao(ShippingQuorumDao.class).updateStatus(quorum.getId().getQuorumDate(), quorum.getId().getQuorumId(), ShippingQuorumStatus.COMPLETE);
					}
					ServiceFactory.getDao(ShippingQuorumDetailDao.class).save(detail);
					if(schedule.getActQty() < schedule.getSchQty()){
						schedule.setActQty(schedule.getActQty() + 1);
						ServiceFactory.getDao(ShippingVanningScheduleDao.class).save(schedule);
					}
					if(trailerInfo.getActQty() < trailerInfo.getSchQty()){
						trailerInfo.setActQty(trailerInfo.getActQty() + 1);
						ServiceFactory.getDao(ShippingTrailerInfoDao.class).save(trailerInfo);
					}
					EngineManifest engineManifest = getEngineShippingHelper().createEngineManifest(detail, PropertyService.getSystemPropertyBean().getSiteName());
					getDao(EngineManifestDao.class).save(engineManifest);
					
					getEngineShippingHelper().invokeBroadcast(engineManifest.getId().getEngineNo(), engineManifest.getEngineKdLot(), getShippingPPID());

					return true;
				}
			}
		}
		
		return false;

	}
	
	public void removeEngineFromQuorum(String ein) {
		List<ShippingQuorum> quorums = new ArrayList<ShippingQuorum>();
		List<ShippingQuorumDetail> quorumDetails = new ArrayList<ShippingQuorumDetail>();
		
		List<ShippingTrailerInfo> trailerInfos = new ArrayList<ShippingTrailerInfo>();
		List<ShippingVanningSchedule> vanningSchedules = new ArrayList<ShippingVanningSchedule>();
		
		List<ShippingQuorumDetail> loadedQuorums = getDao(ShippingQuorumDetailDao.class).findAllByEngineNumber(ein);
		for(ShippingQuorumDetail loadedQuorumDetail : loadedQuorums) {
			loadedQuorumDetail.setEngineNumber(null);
			loadedQuorumDetail.setStatus(ShippingQuorumDetailStatus.MANUAL_LOAD);
			ShippingQuorum loadedQuorum = findQuorum(loadedQuorumDetail.getId().getQuorumDate(), loadedQuorumDetail.getId().getQuorumId(), quorums); 
			loadedQuorum.setStatus(ShippingQuorumStatus.INCOMPLETE);
			ShippingTrailerInfo trailerInfo = findTrailerInfo(loadedQuorum.getTrailerId(), trailerInfos);
			if(trailerInfo != null)	trailerInfo.setActQty(trailerInfo.getActQty() - 1);
			ShippingVanningSchedule schedule = findSchedule(loadedQuorum.getTrailerId(), loadedQuorumDetail.getKdLot(), vanningSchedules); 
			if(schedule != null) schedule.setActQty(schedule.getActQty() -1);
			quorumDetails.add(loadedQuorumDetail);
		}
		
		getDao(ShippingQuorumDetailDao.class).saveAll(quorumDetails);
		getDao(ShippingQuorumDao.class).saveAll(quorums);
		getDao(ShippingVanningScheduleDao.class).saveAll(vanningSchedules);
		getDao(ShippingTrailerInfoDao.class).saveAll(trailerInfos);
	}
    
	
	private ShippingVanningSchedule findSchedule(int trailerId, String kdLot, List<ShippingVanningSchedule> schedules) {
		ShippingVanningSchedule schedule = findScheduleToRemove(trailerId, kdLot);
		if(schedule == null ) return schedule;
		int index = schedules.indexOf(schedule);
		if(index >=0 ) schedule = schedules.get(index);
		else schedules.add(schedule);
		return schedule; 
	}
	
	private ShippingVanningSchedule findScheduleToRemove(int trailerId,String kdLot) {
		List<ShippingVanningSchedule> schedules = getDao(ShippingVanningScheduleDao.class).findVanningSchedules(trailerId,kdLot);
		for(int i=schedules.size()-1;i>=0;i--) {
			ShippingVanningSchedule schedule = schedules.get(i);
			if(schedule != null && schedule.getActQty() > 0) return schedule;
		}
		return null;
	}
	
	private ShippingTrailerInfo findTrailerInfo(int trailerId, List<ShippingTrailerInfo> trailerInfos) {
		ShippingTrailerInfo trailerInfo = new ShippingTrailerInfo();
		trailerInfo.setTrailerId(trailerId);
		int index = trailerInfos.indexOf(trailerInfo);
		if(index < 0){
			trailerInfo = getDao(ShippingTrailerInfoDao.class).findByKey(trailerId);
			if(trailerInfo != null) trailerInfos.add(trailerInfo);
		}else trailerInfo = trailerInfos.get(index);
		return trailerInfo;
	}
	
	private ShippingQuorum findQuorum(Date quorumDate, int quorumId, List<ShippingQuorum> quorums) {
		ShippingQuorum loadedQuorum = new ShippingQuorum(quorumDate,quorumId);
		int index = quorums.indexOf(loadedQuorum);
		if(index <0 ){
			loadedQuorum = getDao(ShippingQuorumDao.class).findByKey(loadedQuorum.getId());
			quorums.add(loadedQuorum);
		}else loadedQuorum = quorums.get(index);
		return loadedQuorum; 
	}
	
	private void trackEngine(String ein) {
			ServiceFactory.getService(TrackingService.class)
				.track(ProductType.ENGINE, ein, getShippingPPID());
	}
	
	private String checkEngine(String ein,String ymto) {
		
		if(!ProductNumberDef.EIN.isNumberValid(ein)) return  "Invalid Engine Number received :" + ein;
		
		Engine engine = ServiceFactory.getDao(EngineDao.class).findByKey(ein);
		if(engine == null) return "Engine does not exist:" + ein;
		
		if(!engine.isDirectPassStatus() && !engine.isRepairedStatus())
			return "Engine has defects, cannot be loaded";
		
		if(engine.getAutoHoldStatus() == 1) return "Engine " + ein + " is on hold!";
		
		if(engine.getLastPassingProcessPointId().equals(getShippingPPID())) 
			return "Engine " + ein + " is shipped!";
		
		if(!engine.getProductSpecCode().equals(ymto))
			return "engine's spec code " + engine.getProductSpecCode() + " does not match the required " + ymto;
		
		List<String> missingParts = getDao(RequiredPartDao.class).findMissingRequiredParts(engine.getProductSpecCode(),
				getProcessPointId(), engine.getProductType(), engine.getProductId(), null);
		if(!missingParts.isEmpty()) return "Engine has missing required parts : " + missingParts.get(0);	
		
		return null;
		
	}
	
	private ObjectTablePane<ShippingVanningSchedule> createKdLotPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("KD LOT","kdLot").put("YMTO","ymto").put("TRAILER NO","trailerNumber")
		.put("SCH_QTY","schQty").put("ACT_QTY","actQty");
	
		ObjectTablePane<ShippingVanningSchedule> pane = new ObjectTablePane<ShippingVanningSchedule>(clumnMappings.get(),false);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		return pane;
	}

	public void actionPerformed(ActionEvent e) {
		getMainWindow().clearMessage();
		try{
			if(e.getSource().equals(refreshButton)) refresh();
			else if(e.getSource().equals(loadButton) || e.getSource().equals(engineNumberField.getComponent())){
				setCursor(new Cursor(Cursor.WAIT_CURSOR));	
				loadEngine();
			}
		}catch(Exception ex) {
			handleException(ex);
		}finally{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));	
		}
	}
	
	private String getShippingPPID() {
		return getProperty("SHIPPING_PPID", "AE0EN16601");
	}
	
	public EngineShippingHelper getEngineShippingHelper() {
		if(engineShippingHelper == null)
			engineShippingHelper = new EngineShippingHelper(getPropertyBean());
		return engineShippingHelper;
	}

	private EngineShippingPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(EngineShippingPropertyBean.class, getShippingPPID());
	}
}
