package com.honda.galc.client.teamleader.shipping;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.ui.LoginDialog;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.IPopupMenu;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.PopupMenuMouseAdapter;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.EngineManifestPlant;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.EngineManifestDao;
import com.honda.galc.dao.product.ShippingQuorumDao;
import com.honda.galc.dao.product.ShippingQuorumDetailDao;
import com.honda.galc.dao.product.ShippingTrailerInfoDao;
import com.honda.galc.dao.product.ShippingTrailerRackDao;
import com.honda.galc.dao.product.ShippingVanningScheduleDao;
import com.honda.galc.dto.ShippingQuorumDetailDto;
import com.honda.galc.entity.enumtype.ShippedStatus;
import com.honda.galc.entity.enumtype.ShippingTrailerInfoStatus;
import com.honda.galc.entity.product.EngineManifest;
import com.honda.galc.entity.product.ShippingQuorum;
import com.honda.galc.entity.product.ShippingQuorumDetail;
import com.honda.galc.entity.product.ShippingTrailerInfo;
import com.honda.galc.entity.product.ShippingTrailerRack;
import com.honda.galc.entity.product.ShippingVanningSchedule;
import com.honda.galc.enumtype.LoginStatus;
import com.honda.galc.property.EngineShippingPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.EngineShippingHelper;

import net.miginfocom.swing.MigLayout;

/**
 * 
 * 
 * <h3>ShippingTrailerQueryPanel Class description</h3>
 * <p> ShippingTrailerQueryPanel description </p>
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
 * Apr 2, 2015
 *
 *
 */
public class ShippingTrailerQueryPanel extends TabbedPanel implements ActionListener, ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private ObjectTablePane<ShippingTrailerInfo> trailerInfoTablePane; 
	
	private ObjectTablePane<ShippingQuorumDetailDto> quorumDetailTablePane;
	
	private ObjectTablePane<ShippingTrailerRack> trailerRackTablePane;
	
	private boolean isHistory = false;
	
	ShippingTrailerInfo trailerInfo;
	
	private static final String Unship ="Unship";
	
	private EngineShippingPropertyBean shippingPropertyBean;

	private String authorizedUser; 
	
	public ShippingTrailerQueryPanel(MainWindow mainWindow,boolean isHistory){
		super("",0,mainWindow);
		this.isHistory = isHistory;
		initComponents();
		trailerInfoTablePane.addListSelectionListener(this);
		quorumDetailTablePane.getTable().addMouseListener(createPrintAttributeFormatListener());
	}

	@Override
	public void onTabSelected() {
		if(!isInitialized) {
			loadData();
			isInitialized = true;
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JMenuItem) {
			Exception exception = null;
			try{
				JMenuItem menuItem = (JMenuItem)e.getSource();
				if(menuItem.getName().equals(Unship)){
					if(getShippingPropertyBean().isNeedAuthorizedUserToUnship() && !login()) return;
					unship();
				}
			}catch(Exception ex) {
				exception = ex;
			}
			handleException(exception);
		}
	}
	
	private boolean login() {
		if(LoginDialog.login() != LoginStatus.OK) return false;

		if (!ClientMain.getInstance().getAccessControlManager().isAuthorized(getShippingPropertyBean().getAuthorizationGroup())) {
			JOptionPane.showMessageDialog(null, "You have no access permission to unship.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void unship() {
		List<ShippingQuorumDetailDto> selectedItems = quorumDetailTablePane.getSelectedItems();
		Map<String, List<ShippingQuorumDetail>> unshipQuorumMap = new HashMap<String, List<ShippingQuorumDetail>>();
		List<String> unshipEngines = new ArrayList<String>();
		prepareUnship(selectedItems, unshipQuorumMap, unshipEngines);
		
		getLogger().info("Unship: ", getUnshipInfo(unshipQuorumMap));
		
		//update Product tracking status
		if(unshipEngines != null && !unshipEngines.isEmpty()){
			String[] unshipEngineArray = new String[unshipEngines.size()];
			unshipEngines.toArray(unshipEngineArray);
			getDao(EngineDao.class).updateTrackStatusByProductIds(unshipEngineArray, getShippingPropertyBean().getUnshipTrackingStatus());
			getDao(EngineDao.class).updateLastPassingProcessPointIdByProductIds(unshipEngineArray, getShippingPropertyBean().getUnshipProcessPoint());
		}
		
		updateUnshipEngines(unshipQuorumMap, unshipEngines);
		
		
		quorumDetailTablePane.reloadData(getDao(ShippingQuorumDetailDao.class).findAllDetails(trailerInfo.getTrailerId()));
		
		for(ShippingQuorumDetailDto sqdDto: selectedItems)
			sqdDto.setEngineNumber(null);
		quorumDetailTablePane.setSelectedItems(selectedItems);

	}

	private List<ShippingQuorumDetail> updateUnshipEngines(	Map<String, List<ShippingQuorumDetail>> unshipQuorumMap, List<String> unshipEngines) {
		//remove from Shipping Quorum
		List<ShippingQuorumDetail> shippingQuorumDetails = new ArrayList<ShippingQuorumDetail>();
		ShippingVanningScheduleDao vanningSchDao = getDao(ShippingVanningScheduleDao.class);
		for(Map.Entry<String, List<ShippingQuorumDetail>> entry : unshipQuorumMap.entrySet()){
			
			updateVanningSchedule(vanningSchDao, entry);
			
			//update engine manifest to unshipped
			EngineManifestDao emdDao = getDao(EngineManifestDao.class);
			unshipEngines.clear();
			for(ShippingQuorumDetail detail : entry.getValue())
				unshipEngines.add(detail.getEngineNumber());
				
			EngineManifestPlant emp = EngineManifestPlant.getById(Integer.parseInt(entry.getKey().substring(4,6)));
			List<EngineManifest> allUnshippedEmList = emdDao.findAllByEngineList(emp.getPlant(),unshipEngines);
			
			for(EngineManifest em : allUnshippedEmList)
				em.setUnshipStatus(ShippedStatus.UNSHIPPED);
			emdDao.saveAll(allUnshippedEmList);
			EngineShippingHelper engineShippingHelper = getEngineShippingHelper();
			
			for(EngineManifest em : allUnshippedEmList) {
				engineShippingHelper.invokeBroadcast(em.getId().getEngineNo(), em.getEngineKdLot(), getShippingPPID());
			} 
			
			shippingQuorumDetails.clear();
			for(ShippingQuorumDetail sqd : entry.getValue()){
				sqd.setEngineNumber(null);
				shippingQuorumDetails.add(sqd);
			}
			getDao(ShippingQuorumDetailDao.class).updateAll(shippingQuorumDetails);
			
			//update Trailer Info
			if(shippingQuorumDetails.size() > trailerInfo.getActQty()){
				getLogger().warn("Trailer:" + trailerInfo.getTrailerId() + " ActQty:" + trailerInfo.getActQty() + " is less than unship size:" + shippingQuorumDetails.size());
				trailerInfo.setActQty(0);
			} else 
				trailerInfo.setActQty(trailerInfo.getActQty() - shippingQuorumDetails.size());
			
			if(trailerInfo.getActQty() <= 0)
				trailerInfo.setStatus(ShippingTrailerInfoStatus.HOLD);
			getDao(ShippingTrailerInfoDao.class).update(trailerInfo);
		}
		
		return shippingQuorumDetails;
	}

	private void updateVanningSchedule(ShippingVanningScheduleDao vanningSchDao, Map.Entry<String, List<ShippingQuorumDetail>> entry) {
		int size = entry.getValue().size();
		while(size > 0){
			ShippingVanningSchedule vanningSchd = findVanningSchedule(vanningSchDao, entry.getKey());
			if(vanningSchd != null) {
				if(vanningSchd.getActQty() >= size){
					vanningSchd.setActQty(vanningSchd.getActQty() - size);
					vanningSchDao.update(vanningSchd);
					size = 0;
				} else {
					size -= vanningSchd.getActQty();
					vanningSchd.setActQty(0);
					vanningSchDao.update(vanningSchd);
					
				}
			} else 
				getLogger().warn("Failed to find vanning schedule for: " + trailerInfo.getTrailerId(), entry.getKey() + " Unship size:" + size);
		}
	}

	private void prepareUnship(List<ShippingQuorumDetailDto> selectedItems,
			Map<String, List<ShippingQuorumDetail>> unshipQuorumMap,
			List<String> unshipEngines) {
		List<ShippingQuorum> quorums = getDao(ShippingQuorumDao.class).findAllByTrailerId(trailerInfo.getTrailerId());
		trailerInfo.setShippingQuorums(quorums);
		
		for(ShippingQuorumDetailDto dt : selectedItems){
			
			if(StringUtils.isBlank(dt.getEngineNumber())) continue;
			
			unshipEngines.add(dt.getEngineNumber()); 
			for(ShippingQuorum sq : trailerInfo.getShippingQuorums()){
				for (ShippingQuorumDetail sd : sq.getShippingQuorumDetails()){
					if(sd.getEngineNumber() != null && (sd.getEngineNumber().equals(dt.getEngineNumber()))){
						if(!unshipQuorumMap.containsKey(sd.getKdLot())){
						List<ShippingQuorumDetail> newList = new ArrayList<ShippingQuorumDetail>();
							unshipQuorumMap.put(sd.getKdLot(), newList);
						}
						unshipQuorumMap.get(sd.getKdLot()).add(sd);
					}
				}
			}
		}
	}

	private ShippingVanningSchedule findVanningSchedule(ShippingVanningScheduleDao vanningSchDao, String kdLot) {
		ShippingVanningSchedule schedule =  null;
		List<ShippingVanningSchedule> findVanningSchedules = vanningSchDao.findVanningSchedules(trailerInfo.getTrailerId(), kdLot);
		for(ShippingVanningSchedule svs : findVanningSchedules)
			if(svs.getActQty() != 0){
				if(schedule == null) 
					schedule = svs;
				else if (schedule.getId().getVanningSeq() < svs.getId().getVanningSeq())
						schedule = svs;
			}
		 
		 
		 return schedule;
	}

	private String getUnshipInfo(Map<String, List<ShippingQuorumDetail>> unshipQuorumMap) {
		StringBuilder sb = new StringBuilder();
		for(String lot : unshipQuorumMap.keySet())
			sb.append("KD-LOT:[").append(lot).append(unshipQuorumMap.get(lot)).append("]; ");
		
		return sb.toString();

	}

	private void initComponents() {
		setLayout(new MigLayout("insets 20 20 20 20", "[grow,fill]"));
		add(trailerInfoTablePane = createTrailerInfoTablePane(),"dock west");
		add(quorumDetailTablePane = createQuorumDetailTablePane(),"dock center");
		if(!isHistory) add(trailerRackTablePane = createTrailerRackPane(),"gapleft 50,gapright 50,dock south");
		
	}
	
	private void loadData() {
		List<ShippingTrailerInfo> trailerInfos = isHistory?
			getDao(ShippingTrailerInfoDao.class).findAll(): getDao(ShippingTrailerInfoDao.class).findLatestTrailers();
		trailerInfoTablePane.reloadData(trailerInfos);
	}
	
	
	private ObjectTablePane<ShippingTrailerInfo> createTrailerInfoTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("TRAILER #","trailerNumber").put("TRAILER ID","trailerId").
		put("SCH_QTY","schQty").put("ACT_QTY","actQty").put("STATUS","status").put("Date","assignDate");
	
		ObjectTablePane<ShippingTrailerInfo> pane = 
			new ObjectTablePane<ShippingTrailerInfo>("Select Trailer Number",clumnMappings.get(),false,true);
		pane.getTable().setFont(new Font("sansserif", 1, 14));
		pane.getTable().setRowHeight(24);
		pane.setAlignment(SwingConstants.CENTER);
		ViewUtil.setPreferredWidth(pane, 450);
		return pane;
	}
	
	private ObjectTablePane<ShippingQuorumDetailDto> createQuorumDetailTablePane() {
		ColumnMappings clumnMappings = ColumnMappings.with("#").put("ROW","trailerRow").put("SEQ","quorumSeq")
		.put("KD LOT","kdLot").put("YMTO","ymto").put("ENGINE #","engineNumber");
	
		ObjectTablePane<ShippingQuorumDetailDto> pane = 
			new ObjectTablePane<ShippingQuorumDetailDto>("Shipping Detail",clumnMappings.get(),false,false);
		pane.getTable().setFont(new Font("sansserif", 1, 16));
		pane.getTable().setRowHeight(28);
		pane.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<ShippingTrailerRack> createTrailerRackPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("RACK","rackType").put("QUANTITY","Quantity");
		ObjectTablePane<ShippingTrailerRack> pane = 
			new ObjectTablePane<ShippingTrailerRack>("Shipping Rack",clumnMappings.get(),false,false);
		pane.getTable().setFont(new Font("sansserif", 1, 16));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		ViewUtil.setPreferredHeight(pane, 130);
		return pane;
	}

	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()) return;
		trailerInfo = trailerInfoTablePane.getSelectedItem();
		if(trailerInfo == null) return;
		List<ShippingQuorumDetailDto> details = 
			getDao(ShippingQuorumDetailDao.class).findAllDetails(trailerInfo.getTrailerId());
		quorumDetailTablePane.reloadData(details);
		if(!isHistory) {
			List<ShippingTrailerRack> racks = getDao(ShippingTrailerRackDao.class).findAllByTrailerNumber(trailerInfo.getTrailerNumber());
			trailerRackTablePane.reloadData(racks);
		}
	}
	
	
	private MouseListener createPrintAttributeFormatListener(){
    	return new PopupMenuMouseAdapter(new IPopupMenu() {
			public void showPopupMenu(MouseEvent e) {
				showPopupSubMenu(e);
			}
		 });  
	}
	
	private void showPopupSubMenu(MouseEvent e) {
    	Logger.getLogger().info("Shipping Trailer History Query PopupMenu");
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(createMenuItem(Unship, isUnshipEnabled()));
		
		popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
	
	private boolean isUnshipEnabled(){
		return trailerInfo.getStatus() == ShippingTrailerInfoStatus.COMPLETE && isAllUnshipped();
	}
	
	private boolean isAllUnshipped() {
		boolean result = false;
		List<ShippingQuorumDetailDto> selectedItems = quorumDetailTablePane.getSelectedItems();
		for(ShippingQuorumDetailDto dto : selectedItems)
			if(!StringUtils.isBlank(dto.getEngineNumber()))
				result = true;
		return result;
	}

	public EngineShippingPropertyBean getShippingPropertyBean() {
		if(shippingPropertyBean == null)
			shippingPropertyBean = PropertyService.getPropertyBean(EngineShippingPropertyBean.class, getMainWindow().getApplication().getApplicationId());
		
		return shippingPropertyBean;
	}
	
	private String getShippingPPID() {
		return getProperty("SHIPPING_PPID", "AE0EN16601");
	}
	
	public EngineShippingHelper getEngineShippingHelper() {
		EngineShippingPropertyBean propertyBean =  PropertyService.getPropertyBean(EngineShippingPropertyBean.class, getShippingPPID());
		
		return  new EngineShippingHelper(propertyBean);
	}

}
