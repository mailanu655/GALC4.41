package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.honda.galc.client.ClientMainFx;
import com.honda.galc.client.IAccessControlManager;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.client.ui.component.StyleChangingRowFactory;
import com.honda.galc.common.exception.ReflectionException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.conf.PrintAttributeFormatId;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ReflectionUtils;
import com.honda.galc.util.SortedArrayList;
import com.honda.galc.util.StringUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * <h3>Class description</h3>
 * Schedule client controller. 
 * <h4>Description</h4>
 * <h4>Special Notes</h4>
 * <h4>Change History</h4>
 * <TABLE BORDER="1" CELLPADDING="3" CELLSPACING="1" WIDTH="100%">
 * <TR BGCOLOR="#EEEEFF" CLASS="TableSubHeadingColor">
 * <TH>Updated by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */

public class ScheduleClientController implements IProductOnNotification {

	protected ScheduleClientProperty properties;
	protected Logger logger;
	protected Map<String, Object> upcomingPanelProperties;
	protected Map<String, Object> processedPanelProperties;
	protected Map<String, Object> currentPanelProperties;
	protected Map<String, Object> onHoldPanelProperties;
	private Map<String, Object> lastProductSelectionProperties;
	protected String exceptionOnProcessPoint;
	protected String exceptionOnProcessPointColor;
	protected List<String> exceptionOnProcessPointModels;
	protected ScheduleMainPanel scheduleMainPanel;
	protected Date lastUpdateTimeStamp;
	private LastProductSelectionDialog lastLotSelectionScreen;
	private ProductType productType;
	private BuildAttributeCache buildAttributeCache = new BuildAttributeCache();
	//Define Model for Schedule Client
	private ScheduleClientModel model;
	
	public ScheduleClientController(ScheduleMainPanel panel) {
		this.scheduleMainPanel = panel;
		//Set the Schedule Client Model
		this.model=new ScheduleClientModel();
		logger = panel.getLogger();
		initialize();
	}

	private void initialize() {
		EventBusUtil.register(this);
		initializeProperties();
		requestFocusOnProductId();
	}
	
	public void retrievePreProductionLots() {
		
		String processLocation = scheduleMainPanel.getDropDownStringValue();
		if(!StringUtil.isNullOrEmpty(processLocation)) {
			List<PreProductionLot> preProductionLots = getDao(PreProductionLotDao.class).findAllByProcessLocation(processLocation);
			
			List<PreProductionLot> processedLots = new ArrayList<PreProductionLot>();
			List<PreProductionLot> upcomingLots = new ArrayList<PreProductionLot>();
			List<PreProductionLot> onHoldLots = new SortedArrayList<PreProductionLot>("getProductionLot");
			List<PreProductionLot> currentLots = new ArrayList<PreProductionLot>();
			
			for(PreProductionLot lot : preProductionLots) {
				if(lot.getProcessLocation().equals(processLocation)) {
					if(lot.getHoldStatus() == 0) {
						onHoldLots.add(lot);
					} else {
						if(lot.getSendStatus() == PreProductionLotSendStatus.WAITING 
								|| lot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS) {
							upcomingLots.add(lot);
						} else if (lot.getSendStatus() == PreProductionLotSendStatus.DONE) {
							processedLots.add(lot);
						}
					}
				}
			}
		
			processedLots = PreProductionLotUtils.sortPreProductionLot((processedLots));
			upcomingLots = PreProductionLotUtils.sortPreProductionLot(upcomingLots);
			
			Iterator<PreProductionLot> iterator = upcomingLots.iterator();
			PreProductionLot currentLot = null; 
			PreProductionLot previousLot = upcomingLots.size() == 0 ? null : upcomingLots.get(0);
			
			while(iterator.hasNext() && previousLot != null){
				currentLot = iterator.next();
				if(currentLot.isSameKdLot(previousLot)){
					currentLots.add(currentLot);
					iterator.remove();
					previousLot = currentLot;
					
				} else
					previousLot = null;
			}
			
			if(!isProcessedProductOrLot()){
				publishDataChanged(prepareData(processedLots,processedPanelProperties),SchedulingEventType.PROCESSED_ORDER_CHANGED);
			}
			else {
				retrieveProcessedProducts();
				if(properties.isAutoLoadExpectedProductId()){
					retreiveExpectedProduct(currentLots, upcomingLots);
				}
			}
			
			publishDataChanged(prepareData(currentLots,currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
			publishDataChanged(prepareData(upcomingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
			publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);
			
			lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestampByProcessLocation(processLocation);
		}
	}
	
	/**
	 * Retreive the expected product.
	 *
	 * @param currentLots the current lots
	 * @param upcomingLots the upcoming lots
	 */
	protected void retreiveExpectedProduct(List<PreProductionLot> currentLots, List<PreProductionLot> upcomingLots){
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		BaseProduct expectedProduct = null;
		String productType = properties.getProductType();
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil
				.getProductDao(productType);
		ProductStampingSequenceDao seqDao = ServiceFactory
				.getDao(ProductStampingSequenceDao.class);
		
		if(currentLots!=null){
			lots.addAll(currentLots);
		}
		if(upcomingLots!=null){
			lots.addAll(upcomingLots);
		}
		for(PreProductionLot currentLot: lots){
			if (currentLot == null || currentLot.getStampedCount() >= currentLot.getLotSize()) {
				//all product in the lot has been processed, check the next production lot
				continue;
			}
			List<ProductStampingSequence> seqList = seqDao.findAllNext(
					currentLot.getProductionLot(), currentLot.getStampedCount());
			
			if (seqList == null || seqList.isEmpty()) {
				// no stamping seq list for the production lot, show error.
				scheduleMainPanel.getMainWindow().setErrorMessage(
						"Cannot find a next product from "
								+ currentLot.getProductionLot() + ".");
				return;
			}
			String expectedProductId = seqList.get(0).getId().getProductID();
			expectedProduct = productDao.findByKey(expectedProductId);
			if (expectedProduct == null) {
				scheduleMainPanel.getMainWindow().setErrorMessage(
						"The next product(" + expectedProductId
								+ ") doesn't exist.");
				return;
			}
			if(expectedProduct!=null){
				//The expected product has been found.
				break;
			}
		}
		if (expectedProduct == null) {
			if (logger.isInfoEnabled()) {
				logger.info("No expected product available.");
			}
			return;
		}
		publishDataChanged(expectedProduct,
				SchedulingEventType.EXPECTED_PRODUCT_CHANGED);
	}
	
	@SuppressWarnings("rawtypes")
	protected void retrieveProcessedProducts(){
		String productType = properties.getProductType();
		ProductDao productDao = ProductTypeUtil.getProductDao(productType);
		List<?> list = productDao.findAllLastProcessed(properties.getOnProcessPoint(), properties.getProcessedRowCount());
		//last passed product displayed on the bottom of the list
		Collections.reverse(list);
		publishDataChanged(prepareData(list,processedPanelProperties),SchedulingEventType.PROCESSED_PRODUCT_CHANGED);
	}
	
	/**
	 * This method derives the values from the original list of objects
	 * The type of objects could be PreProduction Lot or (BaseProduct & ProductResult)
	 * @param <T>
	 * @param objects
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> List<MultiValueObject<T>> prepareData(List<T> objects,Map<String, Object> properties) {
		
		List<MultiValueObject<T>> list = new ArrayList<MultiValueObject<T>>();
		PrintAttributeConvertor printAttributeConvertor = new PrintAttributeConvertor(logger);
		DataContainer dc = new DefaultDataContainer();
		
		Map<PrintAttributeFormatId,PrintAttributeFormat> printAttributeCache=new HashMap<PrintAttributeFormatId,PrintAttributeFormat>();
		
		Map<String, ProductionLot> productionLotMap = getProductionLots(objects,properties);
		
		for(T entry  : objects) {
			T item = (T)((entry instanceof Object[]) ? ((Object[])entry)[0] : entry); 
			Object secondItem = (entry instanceof Object[]) ? ((Object[])entry)[1] : null;
			List<Object> values = new ArrayList<Object>();
			for(String methodName : (String[])properties.get(DefaultScheduleClientProperty.METHOD_NAMES)) {
				String buildAttributeName = parseBuildAttributeName(methodName);
				
				if(!StringUtils.isEmpty(buildAttributeName)) {
					String productSpecCode = (String)ReflectionUtils.invoke(item, "getProductSpecCode", new Object[0]);
					String attribute = buildAttributeCache.findAttributeValue(productSpecCode, buildAttributeName, getProductType());
					values.add(attribute);
				}else {
					String historyMethodName = parseProductHistoryMethodName(methodName);
					//Print Attribute to  get Production date
					String printMethodName =parsePrintAttributeName(methodName);
					if(!StringUtils.isEmpty(historyMethodName)) {
						values.add(ReflectionUtils.invoke(secondItem, historyMethodName, new Object[0]));
					}
					//Print Attribute to  get Production date and Production VIN
					else if(!StringUtils.isEmpty(printMethodName)){
						String valueStr = "";
						ProductionLot productionLot = null;
						String preProductionLot = null;
						try{
							preProductionLot = (String)ReflectionUtils.invoke(item, "getProductionLot", new Object[0]);
							productionLot = productionLotMap.get(preProductionLot);
									
						}
						catch(ReflectionException ex){
							logger.error("Ignoring Reflection Exception occured while fetching Production Lot details" + ex.getMessage());
						}
								
						PrintAttributeFormat format =null;
						PrintAttributeFormatId id = new PrintAttributeFormatId();
						id.setFormId(scheduleMainPanel.getProcessPointId());
						id.setAttribute(printMethodName);
						if(printAttributeCache.containsKey(id)){
							format=printAttributeCache.get(id);
						}
						else{
							format = getDao(PrintAttributeFormatDao.class).findByKey(id);
							printAttributeCache.put(id,format);
						}
						dc.put(TagNames.PRODUCTION_LOT.name(), preProductionLot);
						dc.put(ProductionLot.class, productionLot);
						dc.put(TagNames.DATA.name(), objects);
						Object attributeValue=null;
						try{
						attributeValue = printAttributeConvertor.basicConvert(format, dc);
						}
						catch(Exception ex){
							logger.error("Ignoring Exception occured while using print attributes" + ex.getMessage());
						}
						valueStr = (attributeValue == null) ? "" : attributeValue.toString();
						values.add(valueStr);	
					}
					else 	
						values.add(ReflectionUtils.invoke(item, methodName, new Object[0]));
					
				}
			}
			MultiValueObject<T> multiValueObject = new MultiValueObject<T>(item,values);
			list.add(multiValueObject);
		}
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	private  <T> Map<String, ProductionLot> getProductionLots(List<T> objects,Map<String, Object> properties){
		//populate productionLots 
		Map<String, ProductionLot> productionLotMap = new HashMap<String, ProductionLot>();
		List<String> preProductionLotList = new ArrayList<String>();
		//only populate production lots for print attributes
		for(String methodName : (String[])properties.get(DefaultScheduleClientProperty.METHOD_NAMES)) {
			String printMethodName =parsePrintAttributeName(methodName);
			if(!StringUtils.isEmpty(printMethodName)){
				for(T entry : objects){
						T item = (T)((entry instanceof Object[]) ? ((Object[])entry)[0] : entry); 
						preProductionLotList.add((String)ReflectionUtils.invoke(item, "getProductionLot", new Object[0]));			
				}
				productionLotMap =  ServiceFactory.getDao(ProductionLotDao.class).findAllByLotNumber(preProductionLotList);
				break;
			}
		}
		return productionLotMap;
	}
	
	private int getNextProdLotPosition(String[] methodNames){
		if(methodNames == null) return -1;
		for(int i=0;i<methodNames.length;i++)
			if("getNextProductionLot".equalsIgnoreCase(StringUtils.trim(methodNames[i]))) return i;
		return -1;
	}
	
	private void updateNextProductionLot(List<MultiValueObject<PreProductionLot>> items,String[] methodNames){
		int index = getNextProdLotPosition(methodNames);
		if(index < 0) return;
		for(MultiValueObject<PreProductionLot> item : items) {
			item.setValue(index, item.getKeyObject().getNextProductionLot());
		}
	}
	
	private String parseBuildAttributeName(String methodName) {
		if(StringUtils.startsWith(methodName, "ATTR-")) return StringUtils.trim(StringUtils.substring(methodName, 5));
		else return null;
	}
	
	private String parseProductHistoryMethodName(String methodName) {
		if(StringUtils.startsWith(methodName, "HIST-")) return StringUtils.trim(StringUtils.substring(methodName, 5));
		else return null;
	}
	
	//Print Attribute to  get Production date and Production VIN
	private String parsePrintAttributeName(String methodName) {
		if(StringUtils.startsWith(methodName, "PRTATTR-")) return StringUtils.trim(StringUtils.substring(methodName, 8));
		else return null;
	}
	
	public void cut(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		
		//Set Cut Flag
		getModel().setOnCut(true);
		retrievePreProductionLots();
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		//Set the indexes of the rows Cut
		getModel().getCutRowIndices().addAll(selectedRows);
	}
	
	public void paste(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		//Check if no row is selected while performing paste
		if(selectedRows.isEmpty()){
			MessageDialog.showError("Please select a row first, then Paste");
			return;
		}
		else{
			//Set the index of the row which is to be replaced by the rows Cut
			getModel().setPasteRowIndex(selectedRows.get(0));
		
		//Call Move Up or move Down according to rows Cut and row selected to Paste
		if(getModel().getCutRowIndices().get(0) > getModel().getPasteRowIndex()){
			performUp(tablePane, getModel().getCutRowIndices(), getModel().isOnCut());
		}
		else{
			performDown(tablePane, getModel().getCutRowIndices(), getModel().isOnCut());
		}
			//Reset the cut Flag, Cut rows index and Paste Row Index
			getModel().reset();
		}
	}
	
	public void cancelCut(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		//Reload the data
		getModel().reset();
		retrievePreProductionLots();
	}
	
	public void moveUp(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
	if(checkScheduleChanged()) return;
	
	ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();	
	performUp(tablePane, selectedRows, false);
	}

	public void performUp (ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane, List<Integer> selectedRows, boolean isCut) {
		ArrayList<Integer> selectedRowsNew=new ArrayList<Integer>();
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		final boolean upcomingPanelEnabled = ((scheduleMainPanel.getUpcomingLotTblPane() != null) && ((Boolean) upcomingPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		final boolean currentPanelEnabled = ((scheduleMainPanel.getCurrentLotPanel() != null) && ((Boolean) currentPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));

		//if the new property is set
		//check if table pane is upcoming
		if(upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())) {
			//The table pane is Upcoming
			//Get current panel items
			List<MultiValueObject<PreProductionLot>> inProgressLots = FXCollections.observableArrayList();
			List<MultiValueObject<PreProductionLot>>  currentLots = (currentPanelEnabled ? scheduleMainPanel.getCurrentLotPanel().getLotPanel().getTable().getItems() : null);
			//Get upcoming panel items
			List<MultiValueObject<PreProductionLot>>  upcomingLots = (upcomingPanelEnabled ? scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems() : null);
			//Table pane is upcoming lots, Edit the selected row indexes
			int factor = (currentPanelEnabled ? currentLots.size() : 0);
			
			for (Integer index:selectedRows) {
				index += factor;
				selectedRowsNew.add(index);
			}
			//Combining current and upcoming
			if (currentPanelEnabled) inProgressLots.addAll(currentLots);
			if (upcomingPanelEnabled) inProgressLots.addAll(upcomingLots);
			
			//selected rows are the top of the list, can not move up
			if(selectedRowsNew.get(0) == 0) return;
			
			PreProductionLotUtils lotUtils = new PreProductionLotUtils(inProgressLots,
					getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
			
			
			//Check for Cut or Move Up Event
			if(isCut){
				changedLots=cutMoveUp(selectedRowsNew, lotUtils);
			}
			else{
				changedLots = 
						lotUtils.moveUp(selectedRowsNew.get(0), selectedRowsNew.get(selectedRowsNew.size() - 1));
			}
			
			logger.info("move the lots up. number of changed lots: " + changedLots.size() + 
				    " start lot: " + changedLots.get(0).getProductionLot() + 
				    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());
			
			updateChangedLots(changedLots);
			retrievePreProductionLots();
			
			
		}

	}
	
	public List<PreProductionLot> cutMoveUp (ArrayList<Integer> selectedRows, PreProductionLotUtils lotUtils) {
		
			List<PreProductionLot> changedLots=new ArrayList<PreProductionLot>();
			Map<String,PreProductionLot> changedLotMap=new HashMap<String,PreProductionLot>();
			
			int startRow=selectedRows.get(0);
			int endRow=selectedRows.get(selectedRows.size() - 1);
			
			//Move Up above the row selected to paste
			for(int i=0; i< (getModel().getCutRowIndices().get(0)-getModel().getPasteRowIndex());i++){
				
				List<PreProductionLot> movedLots=
						lotUtils.moveUp(startRow, endRow);
				startRow--;
				endRow--;
				for(PreProductionLot prodLot:movedLots){
					changedLotMap.put(prodLot.getId(), prodLot);
				}
			
			}
			logger.info("Changed Lots after Cut/Paste:  "+changedLotMap.values().toString());
			changedLots.addAll(changedLotMap.values());
			
			return changedLots;
		
	}
	
	
	public void moveDown(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		performDown(tablePane,selectedRows,false );
	}
	
	public void performDown (ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane, List<Integer> selectedRows, boolean isCut){
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		ArrayList<Integer> selectedRowsNew=new ArrayList<Integer>();
		final boolean upcomingPanelEnabled = ((scheduleMainPanel.getUpcomingLotTblPane() != null) && ((Boolean) upcomingPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		final boolean currentPanelEnabled = ((scheduleMainPanel.getCurrentLotPanel() != null) && ((Boolean) currentPanelProperties.get(DefaultScheduleClientProperty.PANEL_ENABLED)));
		
		//New Logic for Move Down
		//check if table pane is upcoming or Current
		if((upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())) ||
				(currentPanelEnabled && tablePane.equals(scheduleMainPanel.getCurrentLotPanel().getLotPanel()))) {
			
			List<MultiValueObject<PreProductionLot>> inProgressLots = FXCollections.observableArrayList();
			//The table pane is Upcoming
			//Get current panel items
			List<MultiValueObject<PreProductionLot>>  currentLots = (currentPanelEnabled ? scheduleMainPanel.getCurrentLotPanel().getLotPanel().getTable().getItems() : null);
			//Get upcoming panel items
			List<MultiValueObject<PreProductionLot>>  upcomingLots = (upcomingPanelEnabled ? scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems() : null);
			
			if(upcomingPanelEnabled && tablePane.equals(scheduleMainPanel.getUpcomingLotTblPane())){
				int factor = (currentPanelEnabled ? currentLots.size() : 0);
				for (Integer index: selectedRows) {
					index += factor;
					selectedRowsNew.add(index);
				}
			}
			else{
				selectedRowsNew.addAll(selectedRows);
			}
			
			//Combining current and upcoming
			if (currentPanelEnabled) inProgressLots.addAll(currentLots);
			if (upcomingPanelEnabled) inProgressLots.addAll(upcomingLots);
			
			PreProductionLotUtils lotUtils = new PreProductionLotUtils(inProgressLots,
					getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
			
			// check for Cut or Move Down Event
			if(isCut){
				
				changedLots=cutMoveDown(selectedRowsNew, lotUtils);
			}
			else{
				changedLots = 
						lotUtils.moveDown(selectedRowsNew.get(0), selectedRowsNew.get(selectedRowsNew.size() - 1));
			}
					
			logger.info("move the lots down. number of changed lots: " + changedLots.size() + 
				    " start lot: " + changedLots.get(0).getProductionLot() + 
				    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());
			
			updateChangedLots(changedLots);
			retrievePreProductionLots();
			
		}
	}
	
	public List<PreProductionLot> cutMoveDown (ArrayList<Integer> selectedRows, PreProductionLotUtils lotUtils) {
		
		List<PreProductionLot> changedLots=new ArrayList<PreProductionLot>();
		Map<String,PreProductionLot> changedLotMap=new HashMap<String,PreProductionLot>();
		
		int startRow=selectedRows.get(0);
		int endRow=selectedRows.get(selectedRows.size() - 1);
		
		//Move Down below the row selected to paste
		for(int i=0; i< (getModel().getPasteRowIndex()-getModel().getCutRowIndices().get(getModel().getCutRowIndices().size()-1));i++){
			
			List<PreProductionLot> movedLots=
					lotUtils.moveDown(startRow, endRow);
			startRow++;
			endRow++;
			for(PreProductionLot prodLot:movedLots){
				changedLotMap.put(prodLot.getId(), prodLot);
			}
		
		}
		logger.info("Changed Lots after Cut/Paste:  "+changedLotMap.values().toString());
		changedLots.addAll(changedLotMap.values());
				
		return changedLots;
	}
	
	protected void updateTablePane(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane,
			List<MultiValueObject<PreProductionLot>> selectedLots){
		if(tablePane == scheduleMainPanel.getUpcomingLotTblPane())
			updateNextProductionLot(tablePane.getTable().getItems(), 
					(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
		tablePane.setData(tablePane.getTable().getItems());
		if(tablePane == scheduleMainPanel.getUpcomingLotTblPane()){
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct();
			}
		}
		tablePane.getTable().getSelectionModel().clearSelection();
		tablePane.selectList(selectedLots);
	}
	
	protected void updateLastCurrentLot(String nextProductionLot) {
		if (scheduleMainPanel.getCurrentLotPanel() == null) return;
		List<PreProductionLot> currentLots = scheduleMainPanel.getCurrentLotPanel().getCurrentLots();
		if(currentLots.isEmpty()) return;
		PreProductionLot preProdLot = (currentLots.get(currentLots.size()-1));
		if(nextProductionLot.equals(preProdLot.getNextProductionLot())) return;
		preProdLot.setNextProductionLot(nextProductionLot);
		getDao(PreProductionLotDao.class).update(preProdLot);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
	}
	
	protected void updateChangedLots(List<PreProductionLot> changedLots) {
		
		getDao(PreProductionLotDao.class).updateAll(changedLots);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void updateProductColorCoding(javafx.scene.control.TableView<MultiValueObject<BaseProduct>> table) {
		if (!StringUtils.isEmpty(exceptionOnProcessPoint)) ((StyleChangingRowFactory) table.getRowFactory()).getRowStyles().setAll(getColorCodingForProducts(table.getItems()));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void updateColorCoding(javafx.scene.control.TableView<MultiValueObject<PreProductionLot>> table) {
		if (!StringUtils.isEmpty(exceptionOnProcessPoint)) ((StyleChangingRowFactory) table.getRowFactory()).getRowStyles().setAll(getColorCodingForPreProductionLots(table.getItems()));
	}
	
	protected Date findLastUpdateTimestamp(){
		if(scheduleMainPanel.isProcessLocationSelected())
			return getDao(PreProductionLotDao.class).findLastUpdateTimestampByProcessLocation(scheduleMainPanel.getDropDownStringValue());
		else
			return getDao(PreProductionLotDao.class).findLastUpdateTimestampByPlanCode(scheduleMainPanel.getDropDownStringValue());
	}
	
	private void initializeProperties() {
		properties = PropertyService.getPropertyBean(ScheduleClientProperty.class, scheduleMainPanel.getProcessPointId());

		processedPanelProperties = new HashMap<String, Object>();
		processedPanelProperties.put(DefaultScheduleClientProperty.IS_PROCESSED_PRODUCT_OR_LOT, properties.isProcessedProductOrLot());
		processedPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getProcessedColumnHeadings());
		processedPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getProcessedMethodNames());
		processedPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getProcessedRowHeight());
		processedPanelProperties.put(DefaultScheduleClientProperty.ROW_COUNT, properties.getProcessedRowCount());
		processedPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getProcessedPanelName());
		processedPanelProperties.put(DefaultScheduleClientProperty.PANEL_ENABLED, properties.isProcessedPanelEnabled());
		processedPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getProcessedFontName(), properties.getProcessedFontStyle(), properties.getProcessedFontSize()));
		processedPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getProcessedPanelSize());
		processedPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getProcessedMenuItems(), properties.getProcessedMenuUserGroups()));
		//Disable Sort Order
		processedPanelProperties.put(DefaultScheduleClientProperty.ENABLE_SORT, properties.isEnableSort());
		
		currentPanelProperties = new HashMap<String, Object>();
		currentPanelProperties.put(DefaultScheduleClientProperty.PANEL_HIGHT, properties.getCurrentPanelPreferedHight());
		currentPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getCurrentPanelSize());
		currentPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getCurrentPanelName());
		currentPanelProperties.put(DefaultScheduleClientProperty.PANEL_ENABLED, properties.isCurrentPanelEnabled());
		currentPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getCurrentRowHeight());
		currentPanelProperties.put(DefaultScheduleClientProperty.IS_MOVE_BY_KD_LOT, properties.isMoveByKdLot());
		currentPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getCurrentColumnHeadings());
		currentPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getCurrentMethodNames());
		currentPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getCurrentFontName(), properties.getCurrentFontStyle(), properties.getCurrentFontSize()));
		currentPanelProperties.put(DefaultScheduleClientProperty.PROCESS_PRODUCT, properties.isProcessProduct());
		currentPanelProperties.put(DefaultScheduleClientProperty.RESET, properties.getReset());
		currentPanelProperties.put(DefaultScheduleClientProperty.SHOW_CURRENT_PRODUCT_LOT, properties.isShowCurrentProductLot());
		currentPanelProperties.put(DefaultScheduleClientProperty.CHANGE_LOT_SIZE, properties.isChangeLotSize());
		currentPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getCurrentMenuItems(), properties.getCurrentMenuUserGroups()));
		currentPanelProperties.put(DefaultScheduleClientProperty.CHECK_DUPLICATE_EXPECTED_PRODUCT_ID, properties.isCheckDuplicateExpectedProductId());
		//Disable Sort Order
		currentPanelProperties.put(DefaultScheduleClientProperty.ENABLE_SORT, properties.isEnableSort());
		//Switch Upcoming or Current Lot
		currentPanelProperties.put(DefaultScheduleClientProperty.SWITCH_UPCOMING_CURRENT_LOT, properties.isSwitchUpcomingCurrentLot());
		
		upcomingPanelProperties = new HashMap<String, Object>();
		upcomingPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getUpcomingColumnHeadings());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getUpcomingMethodNames());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getUpcomingRowHeight());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getUpcomingPanelName());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.PANEL_ENABLED, properties.isUpcomingPanelEnabled());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.DRAG_AND_DROP_ENABLED, Boolean.valueOf(properties.getUpcomingDragAndDropEnabled()));
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLUMN,properties.getUpcomingHighlightColumn());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_VALUE,properties.getUpcomingHighlightValue());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLOR,properties.getUpcomingHighlightColor());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getUpcomingFontName(), properties.getUpcomingFontStyle(), properties.getUpcomingFontSize()));
		upcomingPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getUpcomingPanelSize());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getUpcomingMenuItems(), properties.getUpcomingMenuUserGroups()));
		//Disable Sort Order
		upcomingPanelProperties.put(DefaultScheduleClientProperty.ENABLE_SORT, properties.isEnableSort());
		//Switch Upcoming or Current Lot
		upcomingPanelProperties.put(DefaultScheduleClientProperty.SWITCH_UPCOMING_CURRENT_LOT, properties.isSwitchUpcomingCurrentLot());
		
		onHoldPanelProperties = new HashMap<String, Object>();
		onHoldPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getOnHoldColumnHeadings());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getOnHoldMethodNames());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getOnHoldRowHeight());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getOnHoldPanelName());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.PANEL_ENABLED, properties.isOnHoldPanelEnabled());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.DRAG_AND_DROP_ENABLED, Boolean.valueOf(properties.getOnHoldDragAndDropEnabled()));
		onHoldPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getOnHoldFontName(), properties.getOnHoldFontStyle(), properties.getOnHoldFontSize()));
		onHoldPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getOnHoldPanelSize());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getOnHoldMenuItems(), properties.getOnHoldMenuUserGroups()));
		//Disable Sort Order
		onHoldPanelProperties.put(DefaultScheduleClientProperty.ENABLE_SORT, properties.isEnableSort());
		
		lastProductSelectionProperties = new HashMap<String, Object>();
		lastProductSelectionProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getLastProductSelectionRowHeight());
		lastProductSelectionProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getLastProductSelectionFontName(), properties.getLastProductSelectionFontStyle(), properties.getLastProductSelectionFontSize()));
		lastProductSelectionProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getLastProductSelectionMenuItems(), properties.getLastProductSelectionMenuUserGroups()));
		//Disable Sort Order
		lastProductSelectionProperties.put(DefaultScheduleClientProperty.ENABLE_SORT, properties.isEnableSort());
				
		initializeExceptionProperties();
	}

	private void initializeExceptionProperties() {
		exceptionOnProcessPoint = properties.getExceptionOnProcessPoint();
		if (!StringUtils.isEmpty(exceptionOnProcessPoint)) {
			exceptionOnProcessPointColor = "-fx-background-color: ".concat(properties.getExceptionOnProcessPointColor());
			//exceptionOnProcessPointModels = Arrays.asList(properties.getExceptionOnProcessPointModels());
			exceptionOnProcessPointModels = new ArrayList<String>();
			List<com.honda.galc.entity.conf.ComponentProperty> eoppms = PropertyService.getProperties(scheduleMainPanel.getProcessPointId(), "EXCEPTION_ON_PROCESS_POINT_MODEL\\([0-9]+\\)");
			for (com.honda.galc.entity.conf.ComponentProperty eoppm : eoppms) {
				exceptionOnProcessPointModels.add(eoppm.getPropertyValue());
			}
		} else {
			exceptionOnProcessPointColor = null;
			exceptionOnProcessPointModels = Collections.emptyList();
		}
	}
	
	public List<String> getColorCodingForPreProductionLots(List<MultiValueObject<PreProductionLot>> preProductionLots) {
		if (StringUtils.isEmpty(exceptionOnProcessPoint)) return null;
		List<String> colors = new ArrayList<String>();
		for (MultiValueObject<PreProductionLot> preProductionLot : preProductionLots) {
			String modelCode = getModelCodeForPreProductionLot(preProductionLot.getKeyObject());
			if (StringUtils.isEmpty(modelCode)) {
				colors.add("");
			} else {
				colors.add(exceptionOnProcessPointModels.contains(modelCode) ? exceptionOnProcessPointColor : "");
			}
		}
		return colors;
	}
	
	public List<String> getColorCodingForProducts(List<MultiValueObject<BaseProduct>> products) {
		if (StringUtils.isEmpty(exceptionOnProcessPoint)) return null;
		List<String> colors = new ArrayList<String>();
		for (MultiValueObject<BaseProduct> product : products) {
			String modelCode = product.getKeyObject().getModelCode();
			colors.add(exceptionOnProcessPointModels.contains(modelCode) ? exceptionOnProcessPointColor : "");
		}
		return colors;
	}
	
	protected String getModelCodeForPreProductionLot(PreProductionLot preProductionLot) {
		String productSpecCode = preProductionLot.getProductSpecCode();
		if (StringUtils.isEmpty(productSpecCode) || productSpecCode.length() < 4) return null;
		else return productSpecCode.substring(1,4);
	}

	/**
	 * Creates the authroized menu items for the current user. If there is a group defined for a menu
	 * item, only the users with the specific security groups can access to the
	 * menu item. If there is no group defined for a menu item, the menu item is
	 * available for all users
	 * 
	 * @param menuItems
	 *            the menu items defined in db properties for a table.
	 * @param groups
	 *            the groups the security groups for menu items. The key is the
	 *            menu item. The value is the security groups authorized for the
	 *            menu item. The value can contains multiple security groups,
	 *            spliting by comma
	 * @return the string[] the authroized menu items for the current user
	 */
	protected String[] createAuthroizedMenuItems(String[] menuItems,
			Map<String, String> groups) {
		if(menuItems==null){
			return null;
		}
		if (groups == null) {
			groups = new HashMap<String, String>();
		}
		Map<String, Set<String>> formattedGroups = new HashMap<String, Set<String>>(
				groups.size());
		for (Entry<String, String> entry : groups.entrySet()) {
			Set<String> gs;
			if (entry.getValue() != null) {
				String[] ga = StringUtils.split(entry.getValue(), ",");
				gs = new HashSet<String>(ga.length);
				for (String g : ga) {
					gs.add(StringUtils.trimToEmpty(g));
				}
			} else {
				gs = new HashSet<String>(0);
			}
			formattedGroups.put(entry.getKey().trim(), gs);
		}
		List<String> authorizedMenuItems = new ArrayList<String>(
				menuItems.length);
		IAccessControlManager acm = ClientMainFx.getInstance()
				.getAccessControlManager();
		for (String item : menuItems) {
			item = StringUtils.trimToEmpty(item);
			Set<String> authorizedGroups = formattedGroups.get(item);
			boolean isAuthroized = false;
			if (authorizedGroups == null || authorizedGroups.isEmpty()) {
				// No security group defined for the menu item. we should
				// show the menu popup for any user
				isAuthroized = true;
			}
			else{
				for (String ag : authorizedGroups) {
					if (acm.isAuthorized(ag)) {
						isAuthroized = true;
						break;
					}
				}
			}
			if (isAuthroized) {
				authorizedMenuItems.add(item);
			}
		}
		menuItems = new String[authorizedMenuItems.size()];
		menuItems = authorizedMenuItems.toArray(menuItems);
		return menuItems;
	}
	
	private Font getFont(String fontName, int style, int size) {
		if(fontName.length() == 0) {
			return null;
		}
		Font aFont = Font.font(fontName, FontWeight.findByWeight(style),(double) ((Integer) size).intValue());
		return aFont;
	}
	
	protected void publishDataChanged(Object list,SchedulingEventType eventId) {
		SchedulingEvent event = new SchedulingEvent(list, eventId);
		EventBusUtil.publish(event);
	}

	@Subscribe
	@SuppressWarnings("unchecked")
	public void onEvent(SchedulingEvent event) {
		if(!(event.getTargetObject() instanceof ObjectTablePane)) return;
		
		scheduleMainPanel.getMainWindow().clearMessage();
		
		if(event.getEventType() == SchedulingEventType.SELECT_LAST_PRODUCT) {
			selectLastProduct((ObjectTablePane<MultiValueObject<BaseProduct>>)event.getTargetObject());
			return;
		}
		
		ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane = (ObjectTablePane<MultiValueObject<PreProductionLot>>)event.getTargetObject();
		
		switch(event.getEventType()) {
			case HOLD :
				holdAndMove(tablePane);
				break;
			case MOVE_UP :
				moveUp(tablePane);
				break;
			case MOVE_DOWN :
				moveDown(tablePane);
				break;
			case RELEASE :
				realeaseAndMove(tablePane);
				break;
			case SELECT_LAST_ORDER:
				selectLastLot(tablePane);
				break;
			case ADD_LOT:
				addLot(tablePane);
				break;
			case CREATE_SHIPPING_LOT:
				createShippingLot(tablePane);
				break;
			case SET_CURRENT_LOT:
				setCurrentLot(tablePane);
				break;
			case COMPLETE_LOT:
				completeLot(tablePane);
				break;
			case CHANGE_TO_UNSENT:
				changeToUnsent(tablePane);
				break;
			case CUT:
				cut(tablePane);
				break;
			case PASTE:
				paste(tablePane);
				break;
			case CANCEL:
				cancelCut(tablePane);
				break;
			case EDIT:
				showUpdateDialog(tablePane);
				break;
			default:
				break;
		}
		
		requestFocusOnProductId();
	}
	
	private void showUpdateDialog(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		ScheduleUpdateDialog dialog = new ScheduleUpdateDialog(getScheduleMainPanel().getMainWindow(), tablePane, scheduleMainPanel);
		dialog.showDialog();
		
		
	}

	@Subscribe
	public void refreshScheduleClient(SchedulingEvent event){
		switch(event.getEventType()) {
			case REFRESH_SCHEDULE_CLIENT_ON_TIMEOUT:
				getModel().reset();
				retrievePreProductionLots();
				scheduleMainPanel.getMainWindow().setMessage("Schedule Client is Refreshed ::  " +  scheduleMainPanel.getSystemTime(), Color.YELLOW);
				break;
			default :
				break;
		}
		requestFocusOnProductId();
	}
	
	private void requestFocusOnProductId() {
		if(scheduleMainPanel != null)
			scheduleMainPanel.requestFocusOnProductId();
		
	}

	protected void changeToUnsent(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if (checkScheduleChanged()) return;
		if (StringUtils.isEmpty(exceptionOnProcessPoint)) return;
		
		if (MessageDialog.confirm(ClientMainFx.getInstance().getStage(), "Change to unsent?")) {
			final int startIndex = tablePane.getTable().getSelectionModel().getSelectedIndices().get(0);
			boolean isException = exceptionOnProcessPointModels.contains(getModelCodeForPreProductionLot(getPreProductionLot(tablePane,startIndex)));
			
			String processLocation = scheduleMainPanel.getDropDownStringValue();
			PreProductionLotDao preProductionLotDao = getDao(PreProductionLotDao.class);
			for (int i = startIndex, len = tablePane.getTable().getItems().size(); i < len; i++) {
				PreProductionLot preProductionLot = getPreProductionLot(tablePane,i);
				if ((exceptionOnProcessPointModels.contains(getModelCodeForPreProductionLot(preProductionLot)) == isException)) {
					if (isEligibleForUnsend(preProductionLot)) {
						if (preProductionLotDao.changeToUnsent(preProductionLot.getProductionLot(), processLocation) < 1) {
							MessageDialog.showError("Cannot unsend - lot \'" + preProductionLot.getProductionLot() + "\' status has changed.");
							break;
						}
					} else {
						break;
					}
				}
			}
			
			retrievePreProductionLots();
		}
	}

	protected void completeLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		//tbd - this function currently only required on Preproduction Lot sequence
	}

	protected void setCurrentLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		//tbd - this function currently only required on Preproduction Lot sequence
	}

	public boolean canEnableMenuItem(String menuItem,
			ObjectTablePane<?> tablePane) {
		SchedulingEventType eventType = SchedulingEventType.getType(menuItem);
		boolean canEnable = true;
		switch(eventType){
			case MOVE_UP :
				canEnable = canMoveUp(tablePane);
				break;
			case MOVE_DOWN :
				canEnable = canMoveDown(tablePane);
				break;
			case SELECT_LAST_ORDER:
				canEnable = isRowSelected(tablePane);
				break;
			case CREATE_SHIPPING_LOT:
				canEnable = canCreateShippingLot(tablePane);
				break;
			case CHANGE_TO_UNSENT:
				canEnable = canChangeToUnsent(tablePane);
				break;
			case HOLD :
				canEnable = canHold(tablePane);
			case RELEASE :
				break;
			//Check to enable Cut Event 
			case CUT:
				canEnable = canCut(tablePane);
				break;
			//To enable only after Cut Event is executed
			case PASTE:
				canEnable=false;
				break;
			//To enable only after Cut Event is executed
			case CANCEL:
				canEnable=false;
				break;
			default:
				break;
		}
		return canEnable;
	}
	
	private boolean canHold(ObjectTablePane<?> tablePane) {
		List<?> selectedItems = tablePane.getSelectedItems();
		int numberOfItems = selectedItems.size();
		if(properties.isMoveByKdLot()) {
			PreProductionLot firstLot = getPreProductionLot(selectedItems.get(0));
			PreProductionLot lastLot = getPreProductionLot(selectedItems.get(numberOfItems - 1));
			return firstLot != null && firstLot.isSameKdLot(lastLot);
		} else {
			return tablePane.getSelectedItems().size() == 1;
		}
	}
	
	private boolean canChangeToUnsent(ObjectTablePane<?> tablePane) {
		if (StringUtils.isEmpty(exceptionOnProcessPoint)) return false;
		List<?> selectedItems = tablePane.getSelectedItems();
		if (selectedItems == null || selectedItems.size() != 1) return false;
		return isEligibleForUnsend(getPreProductionLot(selectedItems.get(0)));
	}
	
	private boolean isEligibleForUnsend(PreProductionLot preProductionLot) {
		return (
				preProductionLot != null &&
				preProductionLot.getSendStatus().getId() == 1 &&
				preProductionLot.getStampedCount() == 0
		);
	}
	
	private boolean canMoveUp(ObjectTablePane<?> tablePane){
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		//Switch Upcoming or Current Lot
		if(properties.isSwitchUpcomingCurrentLot()){
			if(rows.size() == 0) return false;
		}
		else{
			if(rows.size() == 0 || rows.get(0) == 0) return false;	
		}
		
		if(properties.isCheckStatus()){
			//check if the lots to be moved are all WAITING lots.
			@SuppressWarnings("unchecked")
			List<MultiValueObject<PreProductionLot>> productionLots = (List<MultiValueObject<PreProductionLot>>) tablePane.getTable().getItems();
			boolean isMoveByKdLot = properties.isMoveByKdLot();
			PreProductionLotUtils lotUtils = new PreProductionLotUtils(productionLots, isMoveByKdLot, properties.lockFirstLot());
			int firstSelectedRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(rows.get(0)) : rows.get(0);
			int lastSelectedRow = isMoveByKdLot ? lotUtils.getLastRowWithSameKdLot(rows.get(rows.size() - 1)) : rows.get(rows.size() - 1);
			int firstRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(firstSelectedRow - 1): firstSelectedRow - 1;
			if(!lotUtils.isAllWaitingLot(firstRow, lastSelectedRow)) {
				return false;
			}
		}
		
		// check to see if the selected rows are the second kd lot in the list
		if(!isSecondKdLot(tablePane)) return true;
		
		return !properties.lockFirstLot();
	}
	
	//Cut Menu to be enabled to disabled
	private boolean canCut(ObjectTablePane<?> tablePane){
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		if(rows.size() == 0) return false;
		if(properties.isCheckStatus()){
			//check if the lots to be moved are all WAITING lots.
			@SuppressWarnings("unchecked")
			
			List<MultiValueObject<PreProductionLot>> productionLots = (List<MultiValueObject<PreProductionLot>>) tablePane.getTable().getItems();
			boolean isMoveByKdLot = properties.isMoveByKdLot();
			PreProductionLotUtils lotUtils = new PreProductionLotUtils(productionLots, isMoveByKdLot, properties.lockFirstLot());
			int firstSelectedRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(rows.get(0)) : rows.get(0);
			int lastSelectedRow = isMoveByKdLot ? lotUtils.getLastRowWithSameKdLot(rows.get(rows.size() - 1)) : rows.get(rows.size() - 1);
			int firstRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(firstSelectedRow - 1): firstSelectedRow - 1;
			
			if(!lotUtils.isAllWaitingLot(firstRow, lastSelectedRow)) {
				return false;
			}
		}
		
		return !properties.lockFirstLot();
	}
	
	private boolean isSecondKdLot(ObjectTablePane<?> tablePane) {
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		if(!properties.isMoveByKdLot()) return rows.get(0) == 1;
		PreProductionLot lot0 = getPreProductionLot(tablePane.getTable().getItems().get(0));
		PreProductionLot lot1 = rows.get(0) == 0 ? getPreProductionLot(tablePane.getTable().getItems().get(0)):
											getPreProductionLot(tablePane.getTable().getItems().get(rows.get(0) -1));
		return lot0.isSameKdLot(lot1);
	}
	
	private boolean isRowSelected(ObjectTablePane<?> tablePane) {
		return tablePane.getTable().getSelectionModel().getSelectedIndex() != -1;
	}
	
	private boolean canCreateShippingLot(ObjectTablePane<?> tablePane){
		if(tablePane.getTable().getSelectionModel().getSelectedItems().get(0) == null) return false;
		PreProductionLot lot = getPreProductionLot(tablePane.getTable().getSelectionModel().getSelectedItems().get(0));
		if(lot == null) return false;
		
		SubProductShippingDao shippingDao = getDao(SubProductShippingDao.class);
		List<SubProductShipping> shippingLots = shippingDao.findAllWithSameKdLot(lot.getKdLot(),lot.getProductionLot());
		return shippingLots.isEmpty();
	}
	
	private PreProductionLot getPreProductionLot(ObjectTablePane<?> tablePane, int index) {
		return getPreProductionLot(tablePane.getTable().getItems().get(index));
	}
	
	private PreProductionLot getPreProductionLot(Object obj) {
		if(!(obj instanceof MultiValueObject<?>)) return null;
		MultiValueObject<?> valueObject = (MultiValueObject<?>) obj;
		if(!(valueObject.getKeyObject() instanceof PreProductionLot)) return null;
		return (PreProductionLot)valueObject.getKeyObject();
	}
	
	private boolean canMoveDown(ObjectTablePane<?> tablePane){
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		if(scheduleMainPanel.getCurrentLotPanel() != null && tablePane.equals(scheduleMainPanel.getCurrentLotPanel().getLotPanel()))
		{
			if(!properties.isSwitchUpcomingCurrentLot()) return false;
		}
		if(rows.size() == 0) return false;
		//added for "Valve Line 4 In House Production Schedule Download"
		if(properties.isCheckStatus()){
			return isInMoveDownStatus(tablePane, rows);
		}
		if(rows.get(0) == 0) return !properties.lockFirstLot();
		
		return rows.get(rows.size() -1) < tablePane.getRowCount() -1;
		
	}
	/**
	 * Check selected production lot send_status.
	 * if status is WAITING, enable Move Down menu item.
	 * if status is not WAITING, disable Move Down menu item. 
	 * @param tablePane
	 * @param rows
	 * @return
	 */
	private boolean isInMoveDownStatus(ObjectTablePane<?> tablePane, ObservableList<Integer> rows){
		boolean isMoveByKdLot = properties.isMoveByKdLot();
		@SuppressWarnings("unchecked")
		List<MultiValueObject<PreProductionLot>> tableItems = (List<MultiValueObject<PreProductionLot>>) tablePane.getTable().getItems();
		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tableItems, isMoveByKdLot , properties.lockFirstLot());
		
		int firstSelectedRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(rows.get(0)) : rows.get(0);
		int lastSelectedRow = isMoveByKdLot ? lotUtils.getLastRowWithSameKdLot(rows.get(rows.size() - 1)) : rows.get(rows.size() - 1);
		
		if(lastSelectedRow >= tablePane.getRowCount() - 1) return false;
		//check selected lot status.
		if(!lotUtils.isAllWaitingLot(firstSelectedRow, lastSelectedRow)){
			return false;
		}
		
		int firstRow = isMoveByKdLot ? lotUtils.getFirstRowWithSameKdLot(lastSelectedRow +1 ) : lastSelectedRow +1;
		int lastRow = isMoveByKdLot ? lotUtils.getLastRowWithSameKdLot(lastSelectedRow +1): lastSelectedRow +1;
		if(!lotUtils.isAllWaitingLot(firstRow, lastRow)){
			return false;
		}
		return true;
	}
	
	protected void holdAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		List<MultiValueObject<PreProductionLot>> tableItems = tablePane.getTable().getItems();
		ObservableList<Integer> selectedRows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		int firstRow = selectedRows.get(0);
		int lastRow = selectedRows.get(selectedRows.size() - 1);
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		//Fetching Parent Lot in order to update its next production lot
		PreProductionLot firstSelectedLot = getPreProductionLot(tableItems.get(firstRow));
		if(firstSelectedLot != null) {
			PreProductionLot parentLot = getDao(PreProductionLotDao.class).findParent(firstSelectedLot.getProductionLot());
			if(parentLot != null) {
				//Fetching next production lot to manage linked list for parent lot
				PreProductionLot lastSelectedLot = getPreProductionLot(tableItems.get(lastRow));
				if(lastSelectedLot != null) {
					//Update next production lot of parent lot
					parentLot.setNextProductionLot(lastSelectedLot.getNextProductionLot());
					changedLots.add(parentLot);
				}
			}
		}
		
		for(MultiValueObject<PreProductionLot> lot : selectedLots) {
			lot.getKeyObject().setHoldStatus(0);
			lot.getKeyObject().setNextProductionLot(null);
		}
		
		for(MultiValueObject<PreProductionLot> preProdLotData : selectedLots) {
			changedLots.add(preProdLotData.getKeyObject());
		}
		
		logger.info("hold the lots. number of changed lots: " + changedLots.size() + 
			    " start lot: " + changedLots.get(0).getProductionLot() + 
			    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);
		retrievePreProductionLots();
	}
	
	protected void realeaseAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		
		if(selectedLots.isEmpty()) return;
		List<MultiValueObject<PreProductionLot>> upcomingLots = scheduleMainPanel.getUpcomingLotTblPane().getTable().getItems();
		MultiValueObject<PreProductionLot> lastLot = upcomingLots.isEmpty()? null : upcomingLots.get(upcomingLots.size() -1);
		
		List<PreProductionLot> changedLots = new ArrayList<PreProductionLot>();
		if(lastLot != null) changedLots.add(lastLot.getKeyObject());
		for(MultiValueObject<PreProductionLot> lot : selectedLots) {
			lot.getKeyObject().setHoldStatus(1);
			if(lastLot != null)
				lastLot.getKeyObject().setNextProductionLot(lot.getKeyObject().getProductionLot());
			lastLot = lot;
		}
		
		for(MultiValueObject<PreProductionLot> preProdLotData : selectedLots) {
			changedLots.add(preProdLotData.getKeyObject());
		}
		
		logger.info("release the lots. number of changed lots: " + changedLots.size() + 
			    " start lot: " + changedLots.get(0).getProductionLot() + 
			    " end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);
		
		retrievePreProductionLots();
		ObjectTablePane<MultiValueObject<PreProductionLot>> upcomingLotPane = scheduleMainPanel.getUpcomingLotTblPane();
		upcomingLotPane.getTable().scrollTo(Integer.MAX_VALUE);
	}

	private void selectLastLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		getLastLotSelectionScreen().open(tablePane.getSelectedItems());
	}
	
	private void selectLastProduct(ObjectTablePane<MultiValueObject<BaseProduct>> tablePane) {
		LastProductSelectionDialog.sendToPlc((Stage)scheduleMainPanel.getScene().getWindow(),properties.getLastLotDeviceId(),tablePane.getSelectedItems());
	}
	
	protected void addLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		AddLotDialog addLotDialog = new AddLotDialog(scheduleMainPanel);
		addLotDialog.showDialog();
	}
	
	private void createShippingLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		MultiValueObject<PreProductionLot> item = tablePane.getTable().getSelectionModel().getSelectedItems().get(0);
		if(item == null) return;
		
		SubProductShippingDao shippingDao = getDao(SubProductShippingDao.class);
		List<SubProductShipping> shippingLots = shippingDao.findAllWithSameKdLot(item.getKeyObject().getKdLot(),item.getKeyObject().getProductionLot());
		if(!shippingLots.isEmpty()) return;
		
		if(MessageDialog.confirm(ClientMainFx.getInstance().getStage(),"Are you sure to create shipping lots for " + item.getKeyObject().getProductionLot())){
			shippingLots = shippingDao.createKnuckleShippingLots(item.getKeyObject().getProductionLot());
			logger.info("created shipping lots for lot number " + item.getKeyObject().getProductionLot());
		}
	}
	
	public String getExceptionOnProcessPoint() {
		return exceptionOnProcessPoint;
	}

	public Map<String, Object> getLastproductSelectionPanelProperties() {
		return lastProductSelectionProperties;
	}

	public void setLastproductSelectionPanelProperties(
			Map<String, Object> lastProductSelectionProperties) {
		this.lastProductSelectionProperties = lastProductSelectionProperties;
	}
	
	public Map<String, Object> getUpcomingPanelProperties() {
		return upcomingPanelProperties;
	}

	public void setUpcomingPanelProperties(
			Map<String, Object> upcomingPanelProperties) {
		this.upcomingPanelProperties = upcomingPanelProperties;
	}

	public Map<String, Object> getProcessedPanelProperties() {
		return processedPanelProperties;
	}

	public void setProcessedPanelProperties(
			Map<String, Object> processedPanelProperties) {
		this.processedPanelProperties = processedPanelProperties;
	}
	
	public Map<String, Object> getCurrentPanelProperties() {
		return currentPanelProperties;
	}

	public void setCurrentPanelProperties(Map<String, Object> currentPanelProperties) {
		this.currentPanelProperties = currentPanelProperties;
	}

	public Map<String, Object> getOnHoldPanelProperties() {
		return onHoldPanelProperties;
	}

	public void setOnHoldPanelProperties(Map<String, Object> onHoldPanelProperties) {
		this.onHoldPanelProperties = onHoldPanelProperties;
	}
	
	public ScheduleClientProperty getProperties() {
		return properties;
	}
	
	protected boolean isScheduleChanged(){
		Date lastUpdateTimeKN = null;
		try {
			lastUpdateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestampByProcessLocation(scheduleMainPanel.getDropDownStringValue());
		} catch (Exception e) {
		  ;//ok keep going if did not find lastUpdateTimeKN
		}
		if(lastUpdateTimeKN == null || lastUpdateTimeStamp == null) return false;
		return lastUpdateTimeKN.after(lastUpdateTimeStamp);
	}
	
	protected boolean checkScheduleChanged() {
		if(isScheduleChanged()){ 
			retrievePreProductionLots();
			scheduleMainPanel.getMainWindow().setErrorMessage("The schedule was refreshed because it was changed from other app.Please redo your operation");
			return true;
		}
		return false;

	}
	
	/**
	 * processed product stamped and update the current production lot
	 * @param productionLot
	 * @param processLocation
	 * @param stampedCount
	 */
	public void execute(String productionLot, String processLocation,	int stampedCount) {
		
		if(!scheduleMainPanel.getDropDownStringValue().equalsIgnoreCase(processLocation)) return;
		
		Logger.getLogger().info("Product Loaded:", productionLot, " processLocation:", processLocation, " stampedCount:" + stampedCount);
		updateLots(productionLot, stampedCount);
	}

	protected void updateLots(String productionLot, int stampedCount) {
		MultiValueObject<PreProductionLot> lot = scheduleMainPanel.getCurrentLotPanel().getCurrentLot();
		
		if(isScheduleChanged() || isLotChanged(productionLot, lot) || properties.isAlwaysRefresh()){
			retrievePreProductionLots();
		}else{
			lot.getKeyObject().setStampedCount(stampedCount);
			publishDataChanged(prepareData(scheduleMainPanel.getCurrentLotPanel().getCurrentLots(),
					currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
			if(isProcessedProductOrLot()) {
				retrieveProcessedProducts();
				if(properties.isAutoLoadExpectedProductId()){
					retreiveExpectedProduct();
				}
			}
		}
	}

	protected void retreiveExpectedProduct() {
		//load expected product
		List<MultiValueObject<PreProductionLot>> items = scheduleMainPanel
				.getUpcomingLotTblPane().getTable().getItems();
		List<PreProductionLot> upcomingLots = new ArrayList<PreProductionLot>(
				items.size());
		for (MultiValueObject<PreProductionLot> item : items) {
			upcomingLots.add(item.getKeyObject());
		}
		items = scheduleMainPanel.getCurrentLotPanel().getLotPanel().getTable().getItems();
		List<PreProductionLot> currentLots = new ArrayList<PreProductionLot>(
				items.size());
		for (MultiValueObject<PreProductionLot> item : items) {
			currentLots.add(item.getKeyObject());
		}
		retreiveExpectedProduct(currentLots, upcomingLots);
	}

	private boolean isLotChanged(String productionLot, MultiValueObject<PreProductionLot> lot) {
		return lot == null || !productionLot.equalsIgnoreCase(lot.getKeyObject().getProductionLot());
	}

	public LastProductSelectionDialog getLastLotSelectionScreen() {
		if(lastLotSelectionScreen == null)
			lastLotSelectionScreen = new LastProductSelectionDialog(scheduleMainPanel);
		return lastLotSelectionScreen;
	}
	
	public boolean isProcessedProductOrLot() {
		return (Boolean)processedPanelProperties.get(DefaultScheduleClientProperty.IS_PROCESSED_PRODUCT_OR_LOT);
	}
	
	protected ProductType getProductType() {
		if(productType == null)
			productType = ProductType.valueOf(getProperties().getProductType());
		
		return productType;
	}

	public void execute(String productId, String message, String planCode, MessageType type) {
		
		List<String> list = Arrays.asList(properties.getPlanCode());
		if(properties.getPlanCode().length < 1 || !list.contains(planCode)) return;
		
		switch(type) {
			case ERROR:
				EventBusUtil.publish(new DisplayMessageEvent("ERROR: "+message, MessageType.ERROR));
				break;
			case WARN:
				String confirmMsg = message + Delimiter.NEW_LINE + Delimiter.NEW_LINE + "Do you want to continue?";
				if(MessageDialog.confirm(ClientMainFx.getInstance().getStage(),confirmMsg)) {
					completeScheduleClient();
				}
				else {
					EventBusUtil.publish(new DisplayMessageEvent(message, MessageType.WARN));
				}
				break;
			default:
				completeScheduleClient();
				break;
		}
		
		if(!StringUtils.isEmpty(productId)) setCurrentProductId(productId);
			
	}
	
	protected String getCurrentProductId() {
		if(scheduleMainPanel.getCurrentLotPanel() != null && scheduleMainPanel.getCurrentLotPanel().getProductPane() != null){
			TextField prodIdTextField = scheduleMainPanel.getCurrentLotPanel().getProductPane().getProductIdTextField();
			return prodIdTextField.getText();
		}
		return null;
	}

	protected void setCurrentProductId(String productId) {
		if(scheduleMainPanel.getCurrentLotPanel() != null && scheduleMainPanel.getCurrentLotPanel().getProductPane() != null){
			TextField prodIdTextField = scheduleMainPanel.getCurrentLotPanel().getProductPane().getProductIdTextField();
			prodIdTextField.setText(productId);
			prodIdTextField.selectRange(0, productId.length());
		}
		
	}
	
	protected void clearCurrentProductId() {
		if(scheduleMainPanel.getCurrentLotPanel() != null && scheduleMainPanel.getCurrentLotPanel().getProductPane() != null)
			scheduleMainPanel.getCurrentLotPanel().getProductPane().getProductIdTextField().setText("");
		
		scheduleMainPanel.getMainWindow().clearMessage();
	}
	
	public void selectionChanged(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		if(rows.size() == 0) return;
		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tablePane.getTable().getItems(),getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		int [] selectedRows = lotUtils.parseSelections(rows.get(0), rows.get(rows.size() - 1));
		if(StringUtils.isEmpty(exceptionOnProcessPoint) && (selectedRows[0] != rows.get(0) || selectedRows[1] != rows.get(rows.size() - 1))){
			tablePane.clearSelection();
			tablePane.getTable().getSelectionModel()
					.selectRange(selectedRows[0], selectedRows[1] + 1);
		}
	}

	@Override
	public void execute(String productId, String productionLot,
			String processLocation, int stampedCount) {
		//tbd - this function currently only required on Preproduction Lot sequence
		
	}
	
	protected void completeScheduleClient(){
		EventBusUtil.publish(new SchedulingEvent(null, SchedulingEventType.SCHEDULE_CLIENT_PROCESSED));
	}

	public ScheduleMainPanel getScheduleMainPanel() {
		return scheduleMainPanel;
	}
	
	//Returns ScheduleClientModel
	public ScheduleClientModel getModel() {
		return model;
	}
			
}
