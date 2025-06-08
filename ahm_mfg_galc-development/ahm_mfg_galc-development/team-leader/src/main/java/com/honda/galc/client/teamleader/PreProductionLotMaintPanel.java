package com.honda.galc.client.teamleader;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.StringUtils;

import net.miginfocom.swing.MigLayout;

import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.TabbedMainWindow;
import com.honda.galc.client.ui.TabbedPanel;
import com.honda.galc.client.ui.component.ColumnMappings;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.common.exception.PropertyException;
import com.honda.galc.dao.product.DownloadLotSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.enumtype.PreProductionLotStatus;
import com.honda.galc.entity.product.DownloadLotSequence;
import com.honda.galc.entity.product.DownloadLotSequenceId;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * 
 * <h3> PreProductionLotMaintPanel Class description</h3>
 * <h4> PreProductionLotMaintPanel Description </h4>
 * <p>
 * <code>PreProductionLotMaintPanel</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * 
 * @see
 * @ver 0.1
 * @author is08925
 * Nov 16, 2016
 */
public class PreProductionLotMaintPanel extends TabbedPanel implements ListSelectionListener{

	private static final long serialVersionUID = 1L;
	
	private static int PLC_COLUMN_START_INDEX = 5;
	
	protected JButton insertBeforeButton= new JButton("Insert Before");
	protected JButton insertAfterButton = new JButton("Insert After");
	
	protected JButton holdButton= new JButton("Hold");
	protected JButton releaseButton = new JButton("Release");
	
	protected JButton saveButton = new JButton("Save");
	protected JButton refreshButton = new JButton("Reset/Refresh");
	
	protected ObjectTablePane<PreProductionLot> preProductionLotListPane;
	protected ObjectTablePane<MultiValueObject<PreProductionLot>> upcomingLotListPane;
	
	private Map<String,PreProductionLot> changeLots = new LinkedHashMap<String,PreProductionLot>();
	
	private Map<String,PreProductionLot> lastDownloadedLots = new LinkedHashMap<String,PreProductionLot>();
	
	private Date lastUpdateTimeStamp;
	
	private String replicatedLocation = "";

	
	public PreProductionLotMaintPanel(TabbedMainWindow mainWindow) {
		super("Hold Release Pre-Production Lot", KeyEvent.VK_P,mainWindow);
		initComponents();
		addListeners();
		loadData();
		insertBeforeButton.setEnabled(false);
		insertAfterButton.setEnabled(false);
	}

	
	public void initComponents() {
		setLayout(new BorderLayout());
		JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				createTopPanel(), createBottomPanel());
		splitPanel.setDividerLocation(180);
		splitPanel.setContinuousLayout(true);
		splitPanel.setOneTouchExpandable(true);
		add(splitPanel,BorderLayout.CENTER);
	}
	
	private JPanel createTopPanel() {
		JPanel panel= new JPanel(new MigLayout("insets 10 20 10 20", "[grow,fill]"));
		TitledBorder border = new TitledBorder("Production Lot List");
		border.setTitleFont(new Font("Arial", Font.BOLD, 20));
		preProductionLotListPane = createPreProductionLotListPane();
		panel.add(preProductionLotListPane,"dock center");
		panel.add(createHoldReleaseButtonPanel(),"dock east");
		panel.setBorder(border);
		return panel;
	}
	
	private JPanel createBottomPanel() {
		JPanel panel= new JPanel(new MigLayout("insets 10 20 10 20", "[grow,fill]"));
		TitledBorder border = new TitledBorder("Upcoming Lots");
		border.setTitleFont(new Font("Arial", Font.BOLD, 20));
		upcomingLotListPane = createUpcomingLotListPane();
		panel.add(upcomingLotListPane,"dock center");
		panel.add(createInsertButtonPanel(),"dock east");
		panel.add(createSaveButtonPanel(),"dock south");
		panel.setBorder(border);
		return panel;
	}
	
	private JPanel createInsertButtonPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 10 0 10 20", "[grow,fill]"));
		
		ViewUtil.setPreferredWidth(150, insertBeforeButton,insertAfterButton);
		
		panel.add(insertBeforeButton,"gaptop 100,wrap");
		panel.add(insertAfterButton,"gapbottom 100");
		return panel;
	}
	
	private JPanel createHoldReleaseButtonPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 10 0 10 20", "[grow,fill]"));
		
		ViewUtil.setPreferredWidth(150, holdButton,releaseButton);
		
		JLabel lable = new JLabel(getSettings());
		panel.add(lable,"wrap");
		
		holdButton.setEnabled(false);
		releaseButton.setEnabled(false);
		
		panel.add(holdButton,"gaptop 50,wrap");
		panel.add(releaseButton,"gapbottom 80");
		return panel;
	}
	
	private JPanel createSaveButtonPanel() {
		JPanel panel = new JPanel(new MigLayout("insets 10 200 10 200", "[grow,fill]"));
		
		panel.add(saveButton);
		panel.add(refreshButton,"gapleft 100");
		return panel;
	}
	
	@Override
	public void onTabSelected() {
	}
	
	private ObjectTablePane<PreProductionLot> createPreProductionLotListPane() {
		ColumnMappings clumnMappings = ColumnMappings.with("Production Lot", "productionLot")
				.put("KD Lot", "kdLot")
				.put("Product Spec Code","productSpecCode")
				.put("Lot Size","lotSize")
				.put("Hold Status", "lotStatus");
		
		ObjectTablePane<PreProductionLot> pane = new ObjectTablePane<PreProductionLot>(clumnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}
	
	private ObjectTablePane<MultiValueObject<PreProductionLot>> createUpcomingLotListPane() {
		ColumnMappings columnMappings = ColumnMappings.with("Production Lot")
				.put("KD Lot")
				.put("Start Product ID")
				.put("Product Spec Code")
				.put("Lot Size");
		
		Map<String,String> shedulePlCMap = getSchedulePLCMap();
		if(shedulePlCMap != null && !shedulePlCMap.isEmpty()) {
			for(String value :shedulePlCMap.values()) {
				columnMappings.put(value);
			}
		}
		
		ObjectTablePane<MultiValueObject<PreProductionLot>> pane = new ObjectTablePane<MultiValueObject<PreProductionLot>>(columnMappings.get(),false);
		
		pane.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pane.getTable().setFont(new Font("sansserif", 1, 18));
		pane.getTable().setRowHeight(28);
		pane.getTable().setEnabled(false);
		pane.setAlignment(SwingConstants.CENTER);
		return pane;
	}

	public void addListeners() {
		preProductionLotListPane.addListSelectionListener(this);
		upcomingLotListPane.addListSelectionListener(this);
		
		holdButton.addActionListener(this);
		releaseButton.addActionListener(this);

		insertBeforeButton.addActionListener(this);
		insertAfterButton.addActionListener(this);
		
		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);
		
	}
	
	public void actionPerformed(ActionEvent event) {
		clearMessage();
		try{
			if(event.getSource().equals(holdButton)) holdButtonClicked();
			else if(event.getSource().equals(releaseButton)) releaseButtonClicked();
			else if(event.getSource().equals(insertBeforeButton)) insertBeforeButtonClicked();
			else if(event.getSource().equals(insertAfterButton)) insertAfterButtonClicked();
			else if(event.getSource().equals(saveButton)) saveButtonClicked();
			else if(event.getSource().equals(refreshButton)) refreshButtonClicked();
		}catch(Exception ex) {
			setErrorMessage("Exception occured : " + ex.getMessage());
		}
		
	}
	
	private void holdButtonClicked() {
		int selectionIndex = upcomingLotListPane.getTable().getSelectedRow();
		if(selectionIndex < 0) return;
		
		MultiValueObject<PreProductionLot> currentLot = upcomingLotListPane.getSelectedItem();
		if(currentLot == null) return;
		
		boolean isInPLC = isLotInPLC(currentLot);
		
		if(isInPLC) {
			boolean isOK = MessageDialog.confirm(this,"Pre Production Lot " + currentLot.getKeyObject().getProductionLot() + " is in Schedule PLC\n " 
		         + "Do you still want to hold the lot? " );
			if(!isOK) return;
		}
		
		if(!isInPLC && !MessageDialog.confirm(this, 
				"Are you sure to hold the pre-production lot " + currentLot.getKeyObject().getProductionLot() + " ?")) 
				return;
		
		PreProductionLot beforeLot = null;
		if(selectionIndex == 0) beforeLot = findParentLot(currentLot.getKeyObject());
		else beforeLot = upcomingLotListPane.getItems().get(selectionIndex -1).getKeyObject();
		
		if(isInPLC) {
			processLotInPLC(beforeLot,selectionIndex);
		}
		
		if(!isBySequence()) {
			PreProductionLot afterLot = null;
			if(selectionIndex < upcomingLotListPane.getItems().size()-1){
				afterLot = upcomingLotListPane.getItems().get(selectionIndex +1).getKeyObject();
			}
			beforeLot.setNextProductionLot(afterLot == null ? null : afterLot.getProductionLot());
			changeLots.put(beforeLot.getProductionLot(), beforeLot);
			
			currentLot.getKeyObject().setNextProductionLot(null);
			
		}
		
		currentLot.getKeyObject().setHoldStatus(PreProductionLotStatus.HOLD.getId());
		changeLots.put(currentLot.getKeyObject().getProductionLot(), currentLot.getKeyObject());
		
		
		upcomingLotListPane.getItems().remove(currentLot);
		upcomingLotListPane.refresh();
		
		preProductionLotListPane.clearSelection();
		preProductionLotListPane.refresh();
		preProductionLotListPane.select(currentLot.getKeyObject());
		
		setMessage("hold production lot - " + currentLot.getKeyObject());
	}
	
	private void releaseButtonClicked() {
		upcomingLotListPane.getTable().setEnabled(true);
		releaseButton.setEnabled(false);
		insertBeforeButton.setEnabled(true);
		insertAfterButton.setEnabled(true);
	}
	
	private void insertBeforeButtonClicked(){
		insertLot(true);
	}	
	
	private void insertAfterButtonClicked(){
		insertLot(false);
	}
	
	private void saveButtonClicked() {
		if(!findLastUpdatedTimestamp().equals(lastUpdateTimeStamp)) {
			setErrorMessage("Pre production lot schedule was changed in database. Please reset / refresh ");
			return;
		}
		
		if(changeLots.isEmpty()) {
			setMessage("There are no changes");
			return;
		}
		
		if(!MessageDialog.confirm(this, "Are you sure that you want to save all the changes?")) return;
		
		List<PreProductionLot> lotList = new ArrayList<PreProductionLot>(changeLots.values());
		getDao(PreProductionLotDao.class).saveAll(lotList);
		logUserAction(SAVED, lotList);
		updateReplicatedLots(lotList);
		
		if(!lastDownloadedLots.isEmpty()) updateDownloadLotSequences();
		
		changeLots.clear();
		loadData();
		
		setMessage("PreProduction Lots saved successfully");
	}
	
	private void updateDownloadLotSequences() {
		List<DownloadLotSequence> downloadLotSequenceList = new ArrayList<DownloadLotSequence>();
		
		for(Entry<String,PreProductionLot> entry:lastDownloadedLots.entrySet()) {
			DownloadLotSequenceId  id = new DownloadLotSequenceId();
			id.setProcessLocation(getProcessLocation());
			id.setProcessPointId(entry.getKey());
			DownloadLotSequence downloadLot = new DownloadLotSequence();
			downloadLot.setId(id);
			downloadLot.setEndProductionLot(entry.getValue().getProductionLot());
			downloadLotSequenceList.add(downloadLot);
			getLogger().info("updating download sequence " + downloadLot);
		}
		
		if(!downloadLotSequenceList.isEmpty()){ 
			getDao(DownloadLotSequenceDao.class).saveAll(downloadLotSequenceList);
			MessageDialog.confirm(this, "Production Lot Schedule in PLCs are Changed. \n " +
					"Please re-download Schedule to PLC immediately!");
			getLogger().info("updated download sequences successfully");
		}
	}
	
	private void refreshButtonClicked() {
		if(!changeLots.isEmpty()) {
			if(!MessageDialog.confirm(this,"There are unsaved changes. Do you still want to refresh the pre production lots ? "))
				return;
		}
		
		changeLots.clear();
		loadData();
		lotSelected();
		
		setMessage("PreProduction Lot list is reset / refreshed successfully");
	}

	public void valueChanged(ListSelectionEvent event) {
		if (event.getValueIsAdjusting()) return;
		if(event.getSource().equals(preProductionLotListPane.getTable().getSelectionModel()))
			lotSelected();
	}
	
	private void lotSelected() {
		upcomingLotListPane.getTable().setEnabled(false);
		PreProductionLot preProdLot = preProductionLotListPane.getSelectedItem();
		if(preProdLot == null) {
			holdButton.setEnabled(false);
			releaseButton.setEnabled(false);
		}else if(preProdLot.getHoldStatus() == 1) {
			holdButton.setEnabled(true);
			releaseButton.setEnabled(false);
			insertBeforeButton.setEnabled(false);
			insertAfterButton.setEnabled(false);
			showLot(preProdLot);
		}else if(preProdLot.getHoldStatus() == 0) {
			holdButton.setEnabled(false);
			releaseButton.setEnabled(true);
			upcomingLotListPane.clearSelection();
			insertBeforeButton.setEnabled(false);
			insertAfterButton.setEnabled(false);
		}	
		
	}
	
		
	protected void updateReplicatedLots(List<PreProductionLot> changedLots) {

		if (isUpdateReplicatedSchedule()) {
			String[] targetProcessLocations = getTargetProcessLocation();
			replicatedLocation = "";
			for(String processlocation : (String[])targetProcessLocations) {
				if (compareLots(changedLots,processlocation)){
					for(PreProductionLot lot : changedLots) {
						PreProductionLot targetLot = getTargetLot(lot,processlocation);
						if ( null != targetLot ) getDao(PreProductionLotDao.class).update(targetLot);
					}
					getLogger().info("The Sub Assemble schedule:"+processlocation+" has be updated.");
				} else {
					replicatedLocation = replicatedLocation + processlocation + " "; 
				}
			}
			if (replicatedLocation.length() > 0 ) { 
				getLogger().info("The Sub Assemble schedule:"+replicatedLocation+" can not be updated because it was changed from other app.");
			}
		}
	}
	
	private boolean compareLots(List<PreProductionLot> changedLots,String targetProcessLocation) {
		for(PreProductionLot lot : changedLots) {
			String sourceProductionLot = lot.getProductionLot();
			PreProductionLot sourceLot = getDao(PreProductionLotDao.class).findByKey(sourceProductionLot);
			String id = lot.getProductionLot().replaceFirst(lot.getProcessLocation(), targetProcessLocation);
			PreProductionLot targetLot = getDao(PreProductionLotDao.class).findByKey(id);
			if (null == targetLot) continue;
			boolean useNextProductionLot = getUseNextProductionLot();
			if(useNextProductionLot) {
				String sourcenextLot = sourceLot.getNextProductionLot();
				String targetnextLot = targetLot.getNextProductionLot();
				if ( sourcenextLot == null && targetnextLot != null) return true;
				if (sourcenextLot != null && !sourcenextLot.replaceFirst(sourceLot.getProcessLocation(), targetProcessLocation).equals(targetnextLot)) return true;
			} else  {
				double sourceSeq = sourceLot.getSequence();
				double targetSeq = targetLot.getSequence();
				if (targetSeq != sourceSeq ) return false;
			}
		}
		return false;
	}
	
	private PreProductionLot getTargetLot(PreProductionLot sourcelot,String targetProcessLocation) {
		String id = sourcelot.getProductionLot().replaceFirst(sourcelot.getProcessLocation(), targetProcessLocation);
		PreProductionLot targetLot = getDao(PreProductionLotDao.class).findByKey(id);
		if ( null == targetLot ) return null;
		targetLot.setSequence(sourcelot.getSequence());
		String sourceNextLot = sourcelot.getNextProductionLot();
		if ( sourceNextLot != null ) targetLot.setNextProductionLot(sourcelot.getNextProductionLot().replaceFirst(sourcelot.getProcessLocation(), targetProcessLocation));
		else targetLot.setNextProductionLot(null);
		if (sourcelot.getHoldStatus() == 0 || sourcelot.getHoldStatus() == 1 )
			targetLot.setHoldStatus(sourcelot.getHoldStatus());
		return targetLot;
	}
	
	private void showLot(PreProductionLot lot) {
		List<MultiValueObject<PreProductionLot>> upcomingLots = upcomingLotListPane.getItems();
		MultiValueObject<PreProductionLot> currentLot = null;
		for(MultiValueObject<PreProductionLot> item : upcomingLots) {
			if(item.getKeyObject().getProductionLot().equalsIgnoreCase(lot.getProductionLot())){
					currentLot = item;
					break;
			}
		}
		if(currentLot != null) {
			upcomingLotListPane.select(currentLot);
			int selectIndex = upcomingLotListPane.getTable().getSelectedRow();
			upcomingLotListPane.scrollToCenter(selectIndex, 0);
		}
	}
	
	protected void loadData() {
		if(isBySequence()) loadDataBySequence();
		else loadDataByLinkList();
		
		lastDownloadedLots.clear();
		
		lastUpdateTimeStamp = findLastUpdatedTimestamp();
		
	}
	
	private void loadDataByLinkList() {
		List<PreProductionLot> allLots = new SortedArrayList<PreProductionLot>("getProductionLot");
		List<PreProductionLot> onHoldLots = getDao(PreProductionLotDao.class).findAllOnHoldLots(getProcessLocation());
		List<PreProductionLot> upcomingLots = getDao(PreProductionLotDao.class).findAllUpcomingLots(getProcessLocation());
		
		allLots.addAll(onHoldLots);
		allLots.addAll(upcomingLots);
		
		preProductionLotListPane.reloadData(allLots);
		upcomingLotListPane.reloadData(prepareUpcomingLots(upcomingLots));
		
	}
	
	private void loadDataBySequence() {
		List<PreProductionLot> preProductionLots = 
				getDao(PreProductionLotDao.class).findAllBySendStatusAndPlanCode(PreProductionLotSendStatus.WAITING.getId(),getPlanCode());
		
		List<PreProductionLot> upcomingLots = new SortedArrayList<PreProductionLot>("getSequence");
		List<PreProductionLot> allLots = new SortedArrayList<PreProductionLot>("getProductionLot");
		allLots.addAll(preProductionLots);
		
		for(PreProductionLot lot : preProductionLots) {
			if(lot.getHoldStatus() == 1) upcomingLots.add(lot);
		}
		
		preProductionLotListPane.reloadData(allLots);
		upcomingLotListPane.reloadData(prepareUpcomingLots(upcomingLots));
	}
	
	private List<MultiValueObject<PreProductionLot>> prepareUpcomingLots(List<PreProductionLot> prodLots) {
		List<MultiValueObject<PreProductionLot>> lots = new ArrayList<MultiValueObject<PreProductionLot>>();
		
		List<Integer> indexList = findLotIndexList(prodLots);
		
		for(int i = 0; i < prodLots.size(); i++) {
			PreProductionLot preProdLot = prodLots.get(i);
			MultiValueObject<PreProductionLot> lot = createMultiValueObject(preProdLot);
			
			for(Integer index : indexList) {
				lot.add(i <= index ? "Yes" : "");
			}

			lots.add(lot);
			
		}
		return lots;
	}
	
	private MultiValueObject<PreProductionLot> createMultiValueObject(PreProductionLot preProdLot) {
		MultiValueObject<PreProductionLot> lot = new MultiValueObject<PreProductionLot>();
		lot.setKeyObject(preProdLot);
		lot.add(preProdLot.getProductionLot());
		lot.add(preProdLot.getKdLot());
		lot.add(preProdLot.getStartProductId());
		lot.add(preProdLot.getProductSpecCode());
		lot.add(preProdLot.getLotSize());
	    return lot;
	}
	
	private void insertLot(boolean isBefore) {
		int selectionIndex = upcomingLotListPane.getTable().getSelectedRow();
		
		if(selectionIndex == -1) {
			setErrorMessage("Please select a production lot to insert before or after");
			return;
		}
		
		if(!isBefore) selectionIndex++;
		
		releaseLot(selectionIndex);
		
		PreProductionLot preProdLot = preProductionLotListPane.getSelectedItem();
		MultiValueObject<PreProductionLot> lot = createMultiValueObject(preProdLot);
		
		upcomingLotListPane.getItems().add(selectionIndex,lot);
		
		upcomingLotListPane.select(lot);
		upcomingLotListPane.refresh();
		upcomingLotListPane.setEnabled(false);
		
		preProductionLotListPane.clearSelection();
		preProductionLotListPane.refresh();
		preProductionLotListPane.select(preProdLot);
		
	}
	
	// release lot before the selectionIndex
	private void releaseLot(int selectionIndex) {
		
		PreProductionLot beforeLot = null;
		MultiValueObject<PreProductionLot> afterLot = null;
		
		if(selectionIndex < upcomingLotListPane.getItems().size())
			afterLot = upcomingLotListPane.getItems().get(selectionIndex);
			
		if(selectionIndex == 0){
			MultiValueObject<PreProductionLot> currentLot = upcomingLotListPane.getItems().get(selectionIndex);
			beforeLot = findParentLot(currentLot.getKeyObject());
		}
		else beforeLot = upcomingLotListPane.getItems().get(selectionIndex -1).getKeyObject();
		
		boolean isInPLC = isLotInPLC(afterLot);
		if(isInPLC) {
			boolean isOK = MessageDialog.confirm(this,"Pre Production Lot " + afterLot.getKeyObject().getProductionLot() + " is in Schedule PLC\n " 
			         + "Do you still want to release lot before it? " );
			if(!isOK) return;
		}
		
		if(isInPLC) {
			processLotInPLC(beforeLot,selectionIndex);
		}
	
		PreProductionLot preProdLot = preProductionLotListPane.getSelectedItem();
				
		if(!isBySequence()){
			beforeLot.setNextProductionLot(preProdLot.getProductionLot());
			if(afterLot != null) {
				preProdLot.setNextProductionLot(afterLot.getKeyObject().getProductionLot());
			}
		}else {
			// by sequence
			if(afterLot == null) preProdLot.setSequence(beforeLot.getSequence() + getSequenceInterval());
			else {
				double space = (afterLot.getKeyObject().getSequence() - beforeLot.getSequence()) / 2.0;
				if(space > 0.0) preProdLot.setSequence(beforeLot.getSequence() + space);
				else {
					// no space between the 2 current lots
					// I think there will be massive moves for pre production lots
					// If this situation does happen, use can create spaces by hold /release a few lots
					MessageDialog.showError(this, "There are no space between lot " + beforeLot.getProductionLot() +
							" and lot " + afterLot.getKeyObject().getProductionLot());
					return;
				}
			}
		}
		
		preProdLot.setHoldStatus(PreProductionLotStatus.RELEASE.getId());
		
		changeLots.put(beforeLot.getProductionLot(), beforeLot);
		changeLots.put(preProdLot.getProductionLot(),preProdLot);
		
		setMessage("Lot " + preProdLot.getProductionLot() + " is released after Lot " + beforeLot.getProductionLot() + " successfully");
		
	}
	
	private boolean isLotInPLC(MultiValueObject<PreProductionLot> lot) {
		if(lot != null && lot.getSize() > PLC_COLUMN_START_INDEX) {
			for (int i = PLC_COLUMN_START_INDEX; i<lot.getSize();i++) {
				if(!StringUtils.isEmpty((String)lot.getValue(i))) return true;
			}
		}
		return false;
	}
	
	private void processLotInPLC(PreProductionLot beforeLot,int currentSelectionIndex) {
		
		Map<String,String> schedulePLCMap = getSchedulePLCMap();
		
		PreProductionLot previousLot = beforeLot;
		for(int i = currentSelectionIndex; i<upcomingLotListPane.getItems().size();i++) {
			MultiValueObject<PreProductionLot> lot = upcomingLotListPane.getItems().get(i);
			boolean isInPLC = false;
			int j = 0;
			for (String processPoint:  schedulePLCMap.keySet()) {
				if(lot.getValue(j + PLC_COLUMN_START_INDEX).equals("Yes")) {
					if(!lastDownloadedLots.containsKey(processPoint) || lastDownloadedLots.get(processPoint).equals(lot.getKeyObject())) {
						lastDownloadedLots.put(processPoint,previousLot);
					}
					lot.setValue(j + PLC_COLUMN_START_INDEX, "");
					isInPLC = true;
				}
				j++;
			}
			if (!isInPLC) break;
			previousLot = lot.getKeyObject();
		}
		
	}
	
	private List<Integer> findLotIndexList(List<PreProductionLot> prodLots) {
		List<Integer> indexList= new ArrayList<Integer>();
		
		Map<String,String> shedulePlCMap = getSchedulePLCMap();
		if(shedulePlCMap == null || shedulePlCMap.isEmpty()) return indexList;
		
		List<DownloadLotSequence> downloadLotSequenceList = getDao(DownloadLotSequenceDao.class).findAll();
		if(downloadLotSequenceList.isEmpty()) return indexList;
		
		for(String processPointId : shedulePlCMap.keySet()) {
			
		   String prodLot = getProductionLot(downloadLotSequenceList, processPointId);
		   indexList.add(getIndex(prodLots,prodLot));
		}
		return indexList;
	}
	
	private int getIndex(List<PreProductionLot> prodLots, String prodLot) {
		if(StringUtils.isEmpty(prodLot)) return -1;
		for(int i=0; i<prodLots.size();i++) {
			PreProductionLot lot = prodLots.get(i);
			if(lot.getProductionLot().equalsIgnoreCase(prodLot)) return i;
		}
		return -1;
	}
	
	private String getProductionLot(List<DownloadLotSequence> downloadLotSequenceList,String processPointId) {
		for(DownloadLotSequence lotSequence : downloadLotSequenceList) {
			if(lotSequence.getProcessPointId().equalsIgnoreCase(processPointId)) return lotSequence.getEndProductionLot();
		}
		return null;
	}
	
	private Date findLastUpdatedTimestamp() {
		return isBySequence() ?
				getDao(PreProductionLotDao.class).findLastUpdateTimestampByPlanCode(getPlanCode()) :
				getDao(PreProductionLotDao.class).findLastUpdateTimestamp(getProcessLocation());
	}
	
	private PreProductionLot findParentLot(PreProductionLot preProductionLot) {
		if(isBySequence()) {
			return getDao(PreProductionLotDao.class).findParentBySequence(preProductionLot.getProductionLot());
		}else {
			return getDao(PreProductionLotDao.class).findParent(preProductionLot.getProductionLot());
		}
	}
	
	public String getSettings() {
		String settings ="";
		if(isBySequence()) {
			settings += "PLAN_CODE : " + getPlanCode();
		}else {
			settings += "PROCESS LOCATION : " + getProcessLocation(); 
		}
		
		return settings;
		
	}
	
	public Map<String,String> getSchedulePLCMap() {
		return PropertyService.getPropertyMap(getApplicationId(), "SCHEDULE_PLC_MAP");
	}
	
	
	private String getProcessLocation() {
		try{
			return getProperty("PROCESS_LOCATION");
		}catch(PropertyException ex) {
			return null;
		}
	}

	private String getPlanCode() {
		try{
			return getProperty("PLAN_CODE");
		}catch(PropertyException ex) {
			return null;
		}
	}
	
	private int getSequenceInterval() {
		return getPropertyInt("SEQUENCE_INTERVAL",1000000);
	}
	
	private boolean isBySequence() {
		return !StringUtils.isEmpty(getPlanCode());
	}
	
	private boolean isUpdateReplicatedSchedule() {
		try{
			return getPropertyBoolean("UPDATE_REPLICATED_SCHEDULE",false);
		}catch(PropertyException ex) {
			return false;
		}
	}
	
	private boolean getUseNextProductionLot() {
		try{
			return getPropertyBoolean("USE_NEXT_PRODUCTION_LOT",true);
		}catch(PropertyException ex) {
			return false;
		}
	}
	
	
	private String[] getTargetProcessLocation() {
		try{
			return getPropertyArray("TARGET_PROCESS_LOCATION");
		}catch(PropertyException ex) {
			return null;
		}
	}
	
	public void clearMessage() {
		getMainWindow().clearMessage();
	}
	
	public void setMessage(String message){
		getMainWindow().setMessage(message);
		getLogger().info(message);
	}
	
	protected void setErrorMessage(String errorMessage){
		getMainWindow().setErrorMessage(errorMessage);
		getLogger().error(errorMessage);
	}
	
}
