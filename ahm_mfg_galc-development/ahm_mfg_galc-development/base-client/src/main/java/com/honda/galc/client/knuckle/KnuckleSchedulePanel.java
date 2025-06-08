package com.honda.galc.client.knuckle;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.ui.MainPanel;
import com.honda.galc.client.ui.component.Fonts;
import com.honda.galc.client.ui.component.LabeledTextField;
import com.honda.galc.client.ui.component.TablePane;
import com.honda.galc.client.ui.tablemodel.KnucklePreProductionLotTableModel;
import com.honda.galc.client.utils.ViewUtil;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.SubProductDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.SubProduct;
import com.honda.galc.notification.service.IKnuckleLoadNotification;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.KeyValue;
import com.honda.galc.util.SortedArrayList;

/**
 * 
 * <h3>KnuckleSchedulePanel Class description</h3>
 * <p> KnuckleSchedulePanel description </p>
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
 * Dec 16, 2010
 *
 *
 */
/**
 * 
 *    
 * @version 0.2
 * @author Gangadhararao Gadde
 * @since Aug 09, 2012
 */
public class KnuckleSchedulePanel extends MainPanel implements ListSelectionListener, ActionListener, IKnuckleLoadNotification {

	private static final long serialVersionUID = 1L;
	
	private static final String PROCESS_LOCATION_KN ="KN";
	
	private ApplicationContext context;
	private TablePane currentKdLotPane;
	private TablePane preProductionLotPane;
	
	private KnucklePreProductionLotTableModel preProdLotTableModel;
	private KnucklePreProductionLotTableModel currentProdLotTableModel;
	
	private List<PreProductionLot> currentProductionLots = new ArrayList<PreProductionLot>();
	private List<PreProductionLot> preProductionLots = new ArrayList<PreProductionLot>();
	
	private BuildAttributeCache buildAttributeCache;
	
	private JButton refreshButton = new JButton("Refresh");
	private JButton moveUpButton = new JButton("Move Up");
	private JButton moveDownButton = new JButton("Move Down");
	
	private LabeledTextField lotPositionField = null;
	private boolean lockFirstPreproductionLot;
	
	private Date lastUpdateTime = null;
	
	public KnuckleSchedulePanel(ApplicationContext context) {
		
		this.context = context;
		lockFirstPreproductionLot = initLockFirstPreproductionLot();
		initComponents();
		buildAttributeCache = new BuildAttributeCache(
				BuildAttributeTag.KNUCKLE_LEFT_SIDE,
				BuildAttributeTag.KNUCKLE_RIGHT_SIDE,
				BuildAttributeTag.KNUCKLE_PART_MARK_LEFT,
				BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT);
		
		refreshAllData();
		addListeners();
	}
	
	private boolean initLockFirstPreproductionLot() {
		try {
			String property = PropertyService.getProperty(context.getProcessPointId(), "LOCK_FIRST_PREPRODUCTION_LOT");
			return Boolean.parseBoolean(property);
		} catch (Exception e) {
			return false;
		}		
	}

	private void loadPreProductionLots() {
		
		// find all pre production lots with sendStatus = 0
		preProductionLots = getDao(PreProductionLotDao.class).findAllPreProductionLotsByProcessLocation(PreProductionLot.PROCESS_LOCATION_KNUCKLE); 
		
		setKnucklePartData(preProductionLots);
		
		loadCurrentProductionLots();
			
	}
	
	private void loadCurrentProductionLots() {
		if(!preProductionLots.isEmpty()) {
			loadCurrentPreproductionLots();
			
			if(currentProductionLots.isEmpty()) return;
			if(isFinishingCurrentLots()) moveFirstWaitingPreproductionLotsToCurrentLots();
			
			List<PreProductionLot> tmpList = new ArrayList<PreProductionLot>();
			for(PreProductionLot item : preProductionLots) {
				if(item.isSameKdLot(currentProductionLots.get(0))){
					currentProductionLots.add(item);
					tmpList.add(item);
				}else break;
			}
			preProductionLots.removeAll(tmpList);
			
			setKnucklePartData(currentProductionLots);
		}
	}

	private void loadCurrentPreproductionLots() {
		List<PreProductionLot> lotList = getDao(PreProductionLotDao.class)
		           .findAllWithSameKdLotCurrentlyProcessed(preProductionLots.get(0).getProductionLot());
		
		currentProductionLots.clear();
		PreProductionLot currentLot = getTheFirstLotOnList(lotList);
		do{
			currentProductionLots.add(currentLot);
			currentLot = findNextLot(currentLot.getNextProductionLot(), lotList);
		} while(currentLot != null);
	}

	private PreProductionLot findNextLot(String nextProductionLot, List<PreProductionLot> lotList) {
		for(PreProductionLot lot : lotList){
			if(lot.getProductionLot().equals(nextProductionLot))
				return lot;
		}
		return null;
	}

	private PreProductionLot getTheFirstLotOnList(List<PreProductionLot> lotList) {
		PreProductionLot firstLot = lotList.get(0);
		for(PreProductionLot lot : lotList){
			if(findParent(lotList, lot) == null){
				firstLot = lot;
				break;
			}
		}
		return firstLot;
	}

	private PreProductionLot findParent(List<PreProductionLot> lotList, PreProductionLot lot) {
		for(PreProductionLot alot : lotList){
			if(lot.getProductionLot().equals(alot.getNextProductionLot()))
				return alot;
		}
		return null;
	}

	private List<PreProductionLot> moveFirstWaitingPreproductionLotsToCurrentLots() {
		if(!currentProductionLots.get(0).isSameKdLot(preProductionLots.get(0))) currentProductionLots.clear();
		for(PreProductionLot lot : preProductionLots){
			if(lot.isSameKdLot(preProductionLots.get(0)))
				currentProductionLots.add(lot);
		}
		preProductionLots.removeAll(currentProductionLots);
		return currentProductionLots;
	}

	private void setKnucklePartData(List<PreProductionLot> prodLots) {
		
		for(PreProductionLot item : prodLots) {
			
			String partNumberLeft  = buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_LEFT_SIDE);
			String partNumberRight =  buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_RIGHT_SIDE);
			String partMarkLeft  = buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_PART_MARK_LEFT);
			String partMarkRight =  buildAttributeCache.findAttributeValue(item.getProductSpecCode(), BuildAttributeTag.KNUCKLE_PART_MARK_RIGHT);
			
			item.setPartNumber(partNumberLeft + " , " + partNumberRight);
			item.setPartMark(partMarkLeft + " , " + partMarkRight);
		}
		
		
	}
	
	private void initComponents() {
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		add(Box.createVerticalStrut(10));
		add(createLotPositionField());
		add(Box.createVerticalStrut(10));
		add(createCurrentKdLotPane());
		add(Box.createVerticalStrut(10));
		add(createLowerPanel());
		add(Box.createVerticalStrut(10));
	}
	
	private void addListeners() {
		preProductionLotPane.getTable().getSelectionModel().addListSelectionListener(this);
		refreshButton.addActionListener(this);
		moveUpButton.addActionListener(this);
		moveDownButton.addActionListener(this);
		
	}
	
	
	private TablePane createCurrentKdLotPane() {
		currentKdLotPane = new TablePane();
		currentProdLotTableModel = new KnucklePreProductionLotTableModel(currentKdLotPane.getTable(),true,currentProductionLots);
		currentKdLotPane.setPreferredHeight(80);
		currentKdLotPane.setMaxHeight(80);
		return currentKdLotPane;
	}
	
	private JPanel createLowerPanel() {
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(createPreProductionLotPane(),BorderLayout.CENTER);
		lowerPanel.add(createButtonPanel(),BorderLayout.SOUTH);
		return lowerPanel;
		
	}
	private Component createPreProductionLotPane() {
		preProductionLotPane = new TablePane("Pre Production Schedule",ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		preProdLotTableModel = new KnucklePreProductionLotTableModel(preProductionLotPane.getTable(),preProductionLots);
		return preProductionLotPane;
	}
	
	private JPanel createButtonPanel() {
		
		JPanel lowerRightPanel = new JPanel();
		lowerRightPanel.setLayout(new BoxLayout(lowerRightPanel,BoxLayout.X_AXIS));
		ViewUtil.setInsets(lowerRightPanel, 0, 10, 0, 10);
		lowerRightPanel.add(Box.createHorizontalStrut(400));
		JPanel buttonPanel = new JPanel();
		
		refreshButton.setFont(Fonts.DIALOG_BOLD_14);
		moveUpButton.setFont(Fonts.DIALOG_BOLD_14);
		moveDownButton.setFont(Fonts.DIALOG_BOLD_14);
		
		buttonPanel.setLayout(new GridLayout(1,0,10,10));
		buttonPanel.add(refreshButton);
		buttonPanel.add(moveUpButton);
		buttonPanel.add(moveDownButton);
		
		lowerRightPanel.add(buttonPanel);
		return lowerRightPanel;
	}

	private LabeledTextField createLotPositionField() {
		
		lotPositionField =  new LabeledTextField("Current Production KD Lot");
		lotPositionField.getComponent().setText(getCurrentLotPositionString());
		lotPositionField.setFont(new java.awt.Font("dialog", 0, 32));
		lotPositionField.getComponent().setHorizontalAlignment(JLabel.CENTER);
		lotPositionField.getComponent().setEditable(false);
		return lotPositionField;
	}
	
	private void refreshAllData() {
		
		loadPreProductionLots();
		
		PreProductionLot preProdLot = currentProductionLots.isEmpty()? null : currentProductionLots.get(currentProductionLots.size()-1);
		preProdLotTableModel.setCurrentProductLot(preProdLot);
		
		preProdLotTableModel.refresh(preProductionLots);
		currentProdLotTableModel.refresh(currentProductionLots);
		refreshCurrentLot();
		refreshSchduleChanged();

	}
	
	private String getCurrentLotPositionString() {
		
		KeyValue<Integer,Integer> pair = getCurrentLotPosition();
		return "" + pair.getKey() + "/" + pair.getValue();
		
	}
	private KeyValue<Integer, Integer> getCurrentLotPosition() {
		int size = 0;
		int position = 0;
		
		for(PreProductionLot preProductionLot : currentProductionLots) {
			size += preProductionLot.getLotSize() * 2;
			position += preProductionLot.getStampedCount();
		}
		
		return new KeyValue<Integer,Integer>(position,size);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		clearErrorMessage();
		if(e.getSource() == refreshButton) doRefresh();
		if(e.getSource() == moveUpButton) doMoveUp();
		else if(e.getSource() == moveDownButton) doMoveDown();
		
	}


	private void doMoveDown() {
		
		if(checkScheduleChanged()){
			setErrorMessage("Could not move the schedules because schedules were changed on other clients. Please refresh.");
			return;
		};
		
		List<PreProductionLot> prodLots = preProdLotTableModel.getSelectedProductionLots();
		if(prodLots.isEmpty()) return;
		getLogger().info("move production lots from " + prodLots.get(0) + " to " + prodLots.get(prodLots.size() - 1)+ " down");
		
		List<PreProductionLot> changedLots = preProdLotTableModel.moveDown();
		preProductionLotPane.getTable().repaint();
		
		int startIndex = preProdLotTableModel.getIndex(prodLots.get(0));
		int endIndex = preProdLotTableModel.getIndex(prodLots.get(prodLots.size() -1));
		
		preProductionLotPane.getTable().getSelectionModel().setSelectionInterval(startIndex, endIndex);
		
		try{
			doSave(changedLots);
		}catch(Exception ex) {
			handleException(ex);
			refreshAllData();
		}
		
	}

	private void doMoveUp() {
		
		if(checkScheduleChanged()){
			setErrorMessage("Could not move the schedules because schedules were changed on other clients. Please refresh.");
			return;
		};
		List<PreProductionLot> prodLots = preProdLotTableModel.getSelectedProductionLots();
		if(prodLots.isEmpty()) return;
		getLogger().info("move production lot from " + prodLots.get(0) + " to " + prodLots.get(prodLots.size() - 1)+ " up");
		
		List<PreProductionLot> changedLots = preProdLotTableModel.moveUp();
		preProductionLotPane.getTable().repaint();
		
		
		int startIndex = preProdLotTableModel.getIndex(prodLots.get(0));
		int endIndex = preProdLotTableModel.getIndex(prodLots.get(prodLots.size() -1));
		
		preProductionLotPane.getTable().getSelectionModel().setSelectionInterval(startIndex, endIndex);
		
		try{
			doSave(changedLots);
		}catch(Exception ex) {
			handleException(ex);
			refreshAllData();
		}
		
	}
	
	private void doSave(List<PreProductionLot> changedProdLots) {
		
		if(changedProdLots.isEmpty()) return ;
		getDao(PreProductionLotDao.class).updateAllNextProductionLots(changedProdLots);
		
		for(PreProductionLot item : changedProdLots) {
			getLogger().info("update production lot " + item.getProductionLot() + "'s next production lot to " + item.getNextProductionLot());
		}
		refreshSchduleChanged();
		
	}
	
	private void doRefresh() {

		refreshAllData();
		
	}

	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting()) {
			return;
		}
		if(e.getSource() == preProductionLotPane.getTable().getSelectionModel()) {
			
			if(lockFirstPreproductionLot){
				moveDownButton.setEnabled(!preProdLotTableModel.isFirstKdLotSelected());
				moveUpButton.setEnabled(!preProdLotTableModel.isFirstKdLotSelected() && !preProdLotTableModel.isSecondKdLotSelected());
			}
			
			preProductionLotPane.getTable().repaint();
		}
	}

	public void knuckleLoaded(String productionLot, int stampedCount) {
		
		getLogger().info("knuckle loaded " + productionLot + " count : " + stampedCount);

		PreProductionLot currentLot = findCurrentPreProductionLot(productionLot);
		if(currentLot == null ) 
			refreshAllData();
		else {
			currentLot.setStampedCount(stampedCount);
			
			if(isFinishingCurrentLots())
				refreshAllData();
			else
				refreshCurrentLot();
		}
		
		printNextKdLots();
	}
	
	
	private boolean isFinishingCurrentLots() {
		KeyValue<Integer, Integer> currentLotPosition = getCurrentLotPosition();
		return currentLotPosition.getKey() == currentLotPosition.getValue() && 
		       !currentProductionLots.get(0).isSameKdLot(preProductionLots.get(0));
	}

	private void printNextKdLots() {
		
		if(preProductionLots.isEmpty()) return;
		
		int lotOffset = KnuckleLabelPrintingUtil.getPrintLotTriggerOffset();
		KeyValue<Integer,Integer> lotPositionPair = getCurrentLotPosition();
		int lotPosition = lotPositionPair.getKey();
		int size = lotPositionPair.getValue();
		if(lotOffset == size - lotPosition || ((size - lotOffset) < 0 && lotPosition == 1)) {
			System.out.println("PRINT next KD LOt");
			new KnuckleLabelPrintingUtil().print(findAllNextKdLotSubProducts());
		}
	}
	
	
	private List<SubProduct> findSubProducts(String productionLot) {
		
		List<SubProduct> allSubProducts = getDao(SubProductDao.class).findAllByProductionLot(productionLot);
		List<SubProduct> leftItems = new SortedArrayList<SubProduct>("getProductId");
		List<SubProduct> rightItems = new SortedArrayList<SubProduct>("getProductId");
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		for(SubProduct item : allSubProducts) {
			if(item.getSubId().equals(Product.SUB_ID_LEFT))
				leftItems.add(item);
			else rightItems.add(item);
		}
		
		int count = leftItems.size()>= rightItems.size() ? leftItems.size() : rightItems.size();
		
		for(int i = 0; i<count;i++) {
			
			if(i<leftItems.size())
				subProducts.add(leftItems.get(i));
			if(i<rightItems.size())
				subProducts.add(rightItems.get(i));
		}
		return subProducts;
	}

	
	private List<SubProduct> findAllNextKdLotSubProducts() {
		
		List<SubProduct> subProducts = new ArrayList<SubProduct>();
		if(preProductionLots.isEmpty()) return subProducts;
		
		PreProductionLot prodLot = preProductionLots.get(0);
		for(PreProductionLot item : preProductionLots) {
			if(item.isSameKdLot(prodLot))
				subProducts.addAll(findSubProducts(item.getProductionLot()));
			else break;
		}
		return subProducts;
	}
	
	
	
	private void refreshCurrentLot() {
		currentProdLotTableModel.refresh(currentProductionLots);
		lotPositionField.getComponent().setText(getCurrentLotPositionString());
	
		currentProdLotTableModel.selectItem(getCurrentLot());

	}
	
	private PreProductionLot getCurrentLot() {
		for(PreProductionLot item : currentProductionLots) {
			if(item.getStampedCount()!= item.getLotSize() * 2) {
				return item;
			}
		}
		return null;
	}

	public PreProductionLot findCurrentPreProductionLot(String productionLot) {
		
		for(PreProductionLot item : currentProductionLots) 
			if(item.getProductionLot().equals(productionLot)) return item;
			
		return null;
	}
	
	private boolean checkScheduleChanged(){
		Date updateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KN);
		return updateTimeKN != null && (lastUpdateTime == null || updateTimeKN.after(lastUpdateTime));
	}
	
	private void refreshSchduleChanged() {
		lastUpdateTime = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(PROCESS_LOCATION_KN);
	}

}
