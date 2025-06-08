package com.honda.galc.client.schedule;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.ClientMain;
import com.honda.galc.client.IAccessControlManager;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.data.ProductSpecData;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.GpcsDivisionDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.conf.ProductStampingSequenceDao;
import com.honda.galc.dao.product.DailyDepartmentScheduleDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dao.product.ProductDao;
import com.honda.galc.dao.product.ProductResultDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.dao.product.SubProductShippingDao;
import com.honda.galc.dao.product.ProductionLotMbpnSequenceDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.DailyDepartmentSchedule;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.ProductResult;
import com.honda.galc.entity.product.ProductStampingSequence;
import com.honda.galc.entity.product.ProductionLotMbpnSequence;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.entity.product.SubProductShipping;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductOnNotification;
import com.honda.galc.notification.service.IStampedCountChangedNotification;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.ReflectionUtils;

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
 * <TD>Dylan Yang</TD>
 * <TD>Jan 22, 2013</TD>
 * <TD>1.0</TD>
 * <TD>GY 20130122</TD>
 * <TD>Initial Realease</TD>
 * </TR>
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date March 15 , 2016
 * Product stamping sequence screen changes
 */

public class ScheduleClientController implements EventSubscriber<SchedulingEvent>, IProductOnNotification, IStampedCountChangedNotification {

	public static final String UPCOMING = "Upcoming";
	public static final String CURRENT = "Current";
	public static final String PROCESSED = "Processed";
	public static final String ATTR_DESC = "ATTR.DESC-";
	public static final String MBPN_DESC = "MBPN_DESC";
	
	protected ScheduleClientProperty properties;
	protected Logger logger;
	protected Map<String, Object> upcomingPanelProperties;
	protected Map<String, Object> upcomingProductStampingSeqPanelProperties;
	protected Map<String, Object> processedPanelProperties;
	protected Map<String, Object> processedProductStampingSeqPanelProperties;
	protected Map<String, Object> currentPanelProperties;
	protected Map<String, Object> onHoldPanelProperties;
	protected ScheduleMainPanel scheduleMainPanel;
	protected Date lastUpdateTimeStamp;
	
	private LastProductSelectionDialog lastLotSelectionScreen;
	private String replicatedLocation = "";
	private ProductType productType;
	private boolean ProductStampingInfoEnabled = false;
	private BaseProduct expectedProduct;
	private ClientAudioManager audioManager = null;
	
	protected List<PreProductionLot> upcomingLots;
	protected List<PreProductionLot> processedLots;
	protected List<PreProductionLot> waitingLots;
	protected List<PreProductionLot> inProgressLots;
	
	protected List<ProductStampingSequence> waitingProductStampingSeqList = new ArrayList<ProductStampingSequence>();
	protected List<ProductStampingSequence> inProgressProductStampingSeqList = new ArrayList<ProductStampingSequence>();
	
	protected ProductSpecData productSpecData;

	private BuildAttributeCache buildAttributeCache = new BuildAttributeCache();
	private boolean isInitialized = false;
	private MbpnDao mbpnDao;
	private ProductionLotMbpnSequenceDao productionLotMbpnSequenceDao;

	public ScheduleClientController(ScheduleMainPanel panel) {
		this.scheduleMainPanel = panel;
		logger = panel.getLogger();
		initialize();
		if(properties.isSoundsEnabled())
			audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class,scheduleMainPanel.getProcessPointId()));
	}

	private void initialize() {
		AnnotationProcessor.process(this);
		EventBus.subscribe(SchedulingEvent.class, this);
		initializeProperties();
		requestFocusOnProductId();
	}
	
	public void refreshLots() {
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				retrievePreProductionLots();
			}
		});
	}

	public void retrievePreProductionLots() {

		String processLocation = properties.getProcessLocation();

		upcomingLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllUpcomingLots(processLocation));
		processedLots = populatePreProductionLotWithProdDate(getDao(PreProductionLotDao.class).findAllPreviousLots(upcomingLots.get(0).getProductionLot(), properties.getProcessedRowCount()));
		waitingLots = new ArrayList<PreProductionLot>();
		inProgressLots = new ArrayList<PreProductionLot>();
		
		waitingProductStampingSeqList = new ArrayList<ProductStampingSequence>();
		inProgressProductStampingSeqList = new ArrayList<ProductStampingSequence>();
		
		if(isProductStampingInfoEnabled()) {	
			if(StringUtils.isBlank(getProperties().getPlanCode())) {
				scheduleMainPanel.getMainWindow().setErrorMessage("Plan Code Property not configured");
			    playNGSound();
				return;
			}

			Set<String> inProgressLotsSet = new HashSet<String>();
			Set<String> waitingLotsSet = new HashSet<String>();
			
			for(PreProductionLot preProductionLot : upcomingLots) {
				if(preProductionLot.getPlanCode() != null && getProperties().getPlanCode().trim().equals(preProductionLot.getPlanCode().trim())) {
					if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.WAITING) {
						waitingLots.add(preProductionLot);
						waitingLotsSet.add(preProductionLot.getProductionLot());
					} else if(preProductionLot.getSendStatus() == PreProductionLotSendStatus.INPROGRESS) {
						inProgressLots.add(preProductionLot);
						inProgressLotsSet.add(preProductionLot.getProductionLot());
					}
				}
			}
					
			updateStampingSeqLinkedList(retrieveProductStampSeq(), waitingLotsSet, inProgressLotsSet);
		}
		
		List<PreProductionLot> onHoldLots = getDao(PreProductionLotDao.class).findAllOnHoldLots(processLocation);
		
		List<PreProductionLot> currentLots = getCurrentProductionLots(); 
		if(isMultipleActiveLots()) {
			List<PreProductionLot> previousLots = getPreviousActiveLots(); 
			currentLots.addAll(0, previousLots);
		}

		if(!isProcessedProductOrLot()){
			publishDataChanged(prepareData(processedLots,processedPanelProperties),SchedulingEventType.PROCESSED_ORDER_CHANGED);
		} else {
			retrieveProcessedProducts();
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct(currentLots, upcomingLots);
			}
		}
		
		if(isProductStampingInfoEnabled()) {
			publishDataChanged(prepareData(waitingProductStampingSeqList,upcomingProductStampingSeqPanelProperties),SchedulingEventType.UPCOMING_PRODUCT_STAMPING_SEQ_CHANGED);
			publishDataChanged(prepareData(inProgressProductStampingSeqList,processedProductStampingSeqPanelProperties),SchedulingEventType.PROCESSED_PRODUCT_STAMPING_SEQ_CHANGED);
			publishDataChanged(prepareData(inProgressLots,processedPanelProperties),SchedulingEventType.PROCESSED_ORDER_CHANGED);
			publishDataChanged(prepareData(waitingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
		}else {
			publishDataChanged(prepareData(upcomingLots,upcomingPanelProperties),SchedulingEventType.UPCOMING_ORDER_CHANGED);
		}
		
		publishDataChanged(prepareData(currentLots,currentPanelProperties),SchedulingEventType.CURRENT_ORDER_CHANGED);
		publishDataChanged(prepareData(onHoldLots,onHoldPanelProperties),SchedulingEventType.ON_HOLD_ORDER_CHANGED);

		lastUpdateTimeStamp = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(properties.getProcessLocation());
	}
	
	private List<PreProductionLot> getCurrentProductionLots() {
		List<PreProductionLot> currentLots = new ArrayList<PreProductionLot>();
		
		PreProductionLot lot = null;
		PreProductionLot previousLot = null;
		
		if (processedLots.size() > 0) {
			lot = processedLots.get(processedLots.size() -1);

			if (lot.getSendStatus() == PreProductionLotSendStatus.DONE && upcomingLots.size() > 0)
				lot = upcomingLots.get(0);
		}
		
		Iterator<PreProductionLot> upcomingIterator = upcomingLots.iterator();

		while(upcomingIterator.hasNext()){
			PreProductionLot currentLot = upcomingIterator.next();
			if(currentLot.isSameKdLot(lot)){
				currentLots.add(currentLot);
				upcomingIterator.remove();
			} else break;	
		}

		ListIterator<PreProductionLot> processedIterator = processedLots.listIterator(processedLots.size());
		while(processedIterator.hasPrevious()){
			PreProductionLot currentLot = processedIterator.previous();
			if(currentLot.getSendStatus() != PreProductionLotSendStatus.DONE &&  currentLot.isSameKdLot(lot)){
				currentLots.add(0,currentLot);
				processedIterator.remove();
			} else break;	
		}
		
		return currentLots;
	}
	
	private List<PreProductionLot> getPreviousActiveLots() {
		List<PreProductionLot> activeLots = new ArrayList<PreProductionLot>();
		PreProductionLot previousLot = null; 
		
		ListIterator<PreProductionLot> processedIterator = processedLots.listIterator(processedLots.size());
		while(processedIterator.hasPrevious()){
			PreProductionLot lot = processedIterator.previous();
			if(lot.getStampedCount() < lot.getLotSize()) {
				if(previousLot == null) {
					previousLot = lot;
				}else if(!lot.isSameKdLot(previousLot)){
					break;
				}
				activeLots.add(0,lot);
				processedIterator.remove();
			}
		}	
		
		return activeLots;
	}	

	private List<ProductStampingSequence> retrieveProductStampSeq() {

		// get products ordered by product stamping sequence
		long startTime = System.currentTimeMillis();		
		List<ProductStampingSequence> productStampSeqLinkedList = 
				getDao(ProductStampingSequenceDao.class).getLinkedProducts(getProperties().getProcessLocation().trim(), getProperties().getUpcomingProductsMaxLots());
		
		logger.info((System.currentTimeMillis() - startTime), "Retrieved linked products for " + getProperties().getUpcomingProductsMaxLots() + " lots");
		return productStampSeqLinkedList;
	}

	/**
	 * Retreive the expected product.
	 *
	 * @param currentLots the current lots
	 * @param upcomingLots the upcoming lots
	 */
	protected void retreiveExpectedProduct(List<PreProductionLot> currentLots, List<PreProductionLot> upcomingLots){
		List<PreProductionLot> lots = new ArrayList<PreProductionLot>();
		expectedProduct = null;
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
				
				playNGSound();
				return;
			}
			String expectedProductId = seqList.get(0).getId().getProductID();
			expectedProduct = productDao.findByKey(expectedProductId);
			if (expectedProduct == null) {
				scheduleMainPanel.getMainWindow().setErrorMessage(
						"The next product(" + expectedProductId
						+ ") doesn't exist.");
				
				playNGSound();
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

	protected void retrieveProcessedProducts(){
		String productType = properties.getProductType();
		ProductDao<? extends BaseProduct> productDao = ProductTypeUtil.getProductDao(productType);
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

		for(T entry  : objects) {
			T item = (T)((entry instanceof Object[]) ? ((Object[])entry)[0] : entry); 
			Object secondItem = (entry instanceof Object[]) ? ((Object[])entry)[1] : null;
			List<Object> values = new ArrayList<Object>();
			for(String methodName : (String[])properties.get(DefaultScheduleClientProperty.METHOD_NAMES)) {
				if (StringUtils.equalsIgnoreCase(methodName, MBPN_DESC)) {
					String productionLot = (String) ReflectionUtils.invoke(item, "getProductionLot", new Object[0]);
					List<String> expectedMbpns = getExpectedMbpns(productionLot);
					if (!expectedMbpns.isEmpty()) {
						values.add(getMbpnDescriptions(expectedMbpns));
					}else values.add(null);
				} else {
					String buildAttributeName = parseBuildAttributeName(methodName);
					String buildAttributeDescName = parseBuildAttributeDescriptionName(methodName);

					if (!StringUtils.isEmpty(buildAttributeName)) {
						String productSpecCode = (String) ReflectionUtils.invoke(item, "getProductSpecCode",
								new Object[0]);
						String attribute = buildAttributeCache.findAttributeValue(productSpecCode, buildAttributeName, getProductType());
						values.add(attribute);
					} else if (!StringUtils.isEmpty(buildAttributeDescName)) {
						String productSpecCode = (String) ReflectionUtils.invoke(item, "getProductSpecCode",
								new Object[0]);
						BuildAttribute ba = buildAttributeCache.findById(productSpecCode, buildAttributeDescName);
						values.add((ba == null) ? "null" : ba.getAttributeDescription());
					} else {
						String historyMethodName = parseProductHistoryMethodName(methodName);
						if (!StringUtils.isEmpty(historyMethodName)) {
							values.add(ReflectionUtils.invoke(secondItem, historyMethodName, new Object[0]));
						} else
							values.add(ReflectionUtils.invoke(item, methodName, new Object[0]));
					}
				}
			}
			MultiValueObject<T> multiValueObject = new MultiValueObject<T>(item,values);
			list.add(multiValueObject);
		}
		return list;
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
		else if(StringUtils.startsWith(methodName, "MBPN")) return StringUtils.trim(StringUtils.substring(methodName, 4));
		else return null;
	}
	
	private String parseBuildAttributeDescriptionName(String methodName) {
		if(StringUtils.startsWith(methodName, ATTR_DESC)) return StringUtils.trim(StringUtils.substring(methodName, 10));
		else return null;
	}

	private String parseProductHistoryMethodName(String methodName) {
		if(StringUtils.startsWith(methodName, "HIST-")) return StringUtils.trim(StringUtils.substring(methodName, 5));
		else return null;
	}

	public void moveUp(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;

		int[] selectedRows = tablePane.getTable().getSelectedRows();

		List<MultiValueObject<PreProductionLot>> selectedItems = tablePane.getSelectedItems();
		//selected rows are the top of the list, can not move up
		if(selectedRows[0] == 0) return;

		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tablePane.getItems(),
				getProperties().isMoveByKdLot(),getProperties().lockFirstLot());

		List<PreProductionLot> changedLots = 
				lotUtils.moveUp(selectedRows[0], selectedRows[selectedRows.length - 1]);

		logger.info("move the lots up. number of changed lots: " + changedLots.size() + 
				" start lot: " + changedLots.get(0).getProductionLot() + 
				" end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);

		updateTablePane(tablePane,selectedItems);

		updateLastCurrentLot(tablePane.getItems().get(0).getKeyObject().getProductionLot());
	}

	public void moveDown(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;

		int[] selectedRows = tablePane.getTable().getSelectedRows();
		//selected rows are the top of the list, can not move up
		if(selectedRows[0] == 0) return;

		List<MultiValueObject<PreProductionLot>> selectedItems = tablePane.getSelectedItems();

		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tablePane.getItems(),
				getProperties().isMoveByKdLot(),getProperties().lockFirstLot());

		List<PreProductionLot> changedLots = 
				lotUtils.moveDown(selectedRows[0], selectedRows[selectedRows.length - 1]);

		logger.info("move the lots down. number of changed lots: " + changedLots.size() + 
				" start lot: " + changedLots.get(0).getProductionLot() + 
				" end lot: " + changedLots.get(changedLots.size()-1).getProductionLot());

		updateChangedLots(changedLots);

		updateTablePane(tablePane,selectedItems);

		updateLastCurrentLot(tablePane.getItems().get(0).getKeyObject().getProductionLot());
	}

	protected void updateTablePane(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane,
			List<MultiValueObject<PreProductionLot>> selectedLots){
		if(tablePane == scheduleMainPanel.getUpcomingLotPanel())
			updateNextProductionLot(tablePane.getItems(), 
					(String[])upcomingPanelProperties.get(DefaultScheduleClientProperty.METHOD_NAMES));
		tablePane.reloadData(tablePane.getItems());
		if(tablePane == scheduleMainPanel.getUpcomingLotPanel()){
			scheduleMainPanel.assignUpcomingLotColors();
			if(properties.isAutoLoadExpectedProductId()){
				retreiveExpectedProduct();
			}
		}
		tablePane.clearSelection();
		tablePane.select(selectedLots);
	}

	protected void updateLastCurrentLot(String nextProductionLot) {
		List<PreProductionLot> currentLots = scheduleMainPanel.getCurrentLotPanel().getCurrentLots();
		if(currentLots.isEmpty()) return;
		PreProductionLot preProdLot = (currentLots.get(currentLots.size()-1));
		if(nextProductionLot.equals(preProdLot.getNextProductionLot())) return;
		preProdLot.setNextProductionLot(nextProductionLot);
		
		updateReplicatedLot(preProdLot);
		getDao(PreProductionLotDao.class).update(preProdLot);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
	}

	protected void updateChangedLots(List<PreProductionLot> changedLots) {

		updateReplicatedLots(changedLots);
		getDao(PreProductionLotDao.class).updateAll(changedLots);
		lastUpdateTimeStamp = findLastUpdateTimestamp();
	}

	protected void updateReplicatedLots(List<PreProductionLot> changedLots) {

		if ( properties.isUpdateReplicatedSchedule() ) {
			String[] targetProcessLocations = properties.getTargetProcessLocation();
			replicatedLocation = "";
			for(String processlocation : (String[])targetProcessLocations) {
				if (compareLots(changedLots,processlocation)){
					for(PreProductionLot lot : changedLots) {
						PreProductionLot targetLot = getTargetLot(lot,processlocation);
						if ( null != targetLot ) getDao(PreProductionLotDao.class).update(targetLot);
					}
					logger.info("The Sub Assemble schedule:"+processlocation+" has be updated.");
				} else {
					replicatedLocation = replicatedLocation + processlocation + " "; 
				}
			}
			if (replicatedLocation.length() > 0 ) { 
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						scheduleMainPanel.getMainWindow().setErrorMessage("The Sub Assemble schedule:"+replicatedLocation+" can not be updated because it was changed from other app.");
					}
				});
				
				logger.info("The Sub Assemble schedule:"+replicatedLocation+" can not be updated because it was changed from other app.");
			}
		}
	}
	
	protected boolean updateReplicatedLot(PreProductionLot changedLot) {
		if ( properties.isUpdateReplicatedSchedule() ) {
			String[] targetProcessLocations = properties.getTargetProcessLocation();
			for(String processlocation : (String[])targetProcessLocations) {
				PreProductionLot targetLot = getTargetLot(changedLot,processlocation);
				if ( null != targetLot ) {
					getDao(PreProductionLotDao.class).update(targetLot);
					logger.info("The Sub Assemble schedule:"+processlocation+" has be updated.");
				}
			}
		}
		return true;
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

	private boolean compareLots(List<PreProductionLot> changedLots,String targetProcessLocation) {
		for(PreProductionLot lot : changedLots) {
			String sourceProductionLot = lot.getProductionLot();
			PreProductionLot sourceLot = getDao(PreProductionLotDao.class).findByKey(sourceProductionLot);
			String id = lot.getProductionLot().replaceFirst(lot.getProcessLocation(), targetProcessLocation);
			PreProductionLot targetLot = getDao(PreProductionLotDao.class).findByKey(id);
			if (null == targetLot) continue;
			boolean useNextProductionLot = properties.getUseNextProductionLot();
			if(useNextProductionLot) {
				String sourcenextLot = sourceLot.getNextProductionLot();
				String targetnextLot = targetLot.getNextProductionLot();
				if ( sourcenextLot == null && targetnextLot != null) return false;
				if (sourcenextLot != null && !sourcenextLot.replaceFirst(sourceLot.getProcessLocation(), targetProcessLocation).equals(targetnextLot)) return false;
			} else  {
				double sourceSeq = sourceLot.getSequence();
				double targetSeq = targetLot.getSequence();
				if (targetSeq != sourceSeq ) return false;
			}
		}
		return true;
	}

	
	protected Date findLastUpdateTimestamp(){
		return getDao(PreProductionLotDao.class).findLastUpdateTimestamp(properties.getProcessLocation());
	}

	private void initializeProperties() {
		properties = PropertyService.getPropertyBean(ScheduleClientProperty.class, scheduleMainPanel.getProcessPointId());
        setProductStampingInfoEnabled(getProperties().isProductStampingInfoEnabled());
        
		processedPanelProperties = new HashMap<String, Object>();
		processedPanelProperties.put(DefaultScheduleClientProperty.IS_PROCESSED_PRODUCT_OR_LOT, properties.isProcessedProductOrLot());
		processedPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getProcessedColumnHeadings());
		processedPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getProcessedMethodNames());
		processedPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getProcessedRowHeight());
		processedPanelProperties.put(DefaultScheduleClientProperty.ROW_COUNT, properties.getProcessedRowCount());
		processedPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getProcessedPanelName());
		processedPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getProcessedFontName(), properties.getProcessedFontStyle(), properties.getProcessedFontSize()));
		processedPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getProcessedPanelSize());
		processedPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getProcessedMenuItems(), properties.getProcessedMenuUserGroups()));

		currentPanelProperties = new HashMap<String, Object>();
		currentPanelProperties.put(DefaultScheduleClientProperty.PANEL_HIGHT, properties.getCurrentPanelPreferedHight());
		currentPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getCurrentRowHeight());
		currentPanelProperties.put(DefaultScheduleClientProperty.IS_MOVE_BY_KD_LOT, properties.isMoveByKdLot());
		currentPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getCurrentColumnHeadings());
		currentPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getCurrentMethodNames());
		currentPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getCurrentFontName(), properties.getCurrentFontStyle(), properties.getCurrentFontSize()));
		currentPanelProperties.put(DefaultScheduleClientProperty.PROCESS_PRODUCT, properties.isProcessProduct());
		currentPanelProperties.put(DefaultScheduleClientProperty.PROCESS_MULTI_PRODUCT, properties.isProcessMultiProduct());
		currentPanelProperties.put(DefaultScheduleClientProperty.RESET, properties.getReset());
		currentPanelProperties.put(DefaultScheduleClientProperty.SHOW_CURRENT_PRODUCT_LOT, properties.isShowCurrentProductLot());
		currentPanelProperties.put(DefaultScheduleClientProperty.CHANGE_LOT_SIZE, properties.isChangeLotSize());
		currentPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getCurrentMenuItems(), properties.getCurrentMenuUserGroups()));
		currentPanelProperties.put(DefaultScheduleClientProperty.CHECK_DUPLICATE_EXPECTED_PRODUCT_ID, properties.isCheckDuplicateExpectedProductId());
		currentPanelProperties.put(TagNames.PRODUCT_TYPE.name(), properties.getProductType());
		currentPanelProperties.put(TagNames.MBPN_PRODUCT_TYPES.name(), properties.getMbpnProductTypes());
		currentPanelProperties.put(DefaultScheduleClientProperty.IS_MULTIPLE_ACTIVE_LOTS, properties.isMultipleActiveLots());

		upcomingPanelProperties = new HashMap<String, Object>();
		upcomingPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getUpcomingColumnHeadings());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getUpcomingMethodNames());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getUpcomingRowHeight());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getUpcomingPanelName());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.DRAG_AND_DROP_ENABLED, Boolean.valueOf(properties.getUpcomingDragAndDropEnabled()));
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLUMN,properties.getUpcomingHighlightColumn());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_VALUE,properties.getUpcomingHighlightValue());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLOR,properties.getUpcomingHighlightColor());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getUpcomingFontName(), properties.getUpcomingFontStyle(), properties.getUpcomingFontSize()));
		upcomingPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getUpcomingPanelSize());
		upcomingPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getUpcomingMenuItems(), properties.getUpcomingMenuUserGroups()));

		onHoldPanelProperties = new HashMap<String, Object>();
		onHoldPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getOnHoldColumnHeadings());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getOnHoldMethodNames());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getOnHoldRowHeight());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getOnHoldPanelName());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.DRAG_AND_DROP_ENABLED, Boolean.valueOf(properties.getOnHoldDragAndDropEnabled()));
		onHoldPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getOnHoldFontName(), properties.getOnHoldFontStyle(), properties.getOnHoldFontSize()));
		onHoldPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getOnHoldPanelSize());
		onHoldPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
				createAuthroizedMenuItems( properties.getOnHoldMenuItems(), properties.getOnHoldMenuUserGroups()));
		
		if(isProductStampingInfoEnabled())
		{
			processedProductStampingSeqPanelProperties = new HashMap<String, Object>();
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getProcessedProductStampingSeqColumnHeadings());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getProcessedProductStampingSeqMethodNames());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getProcessedRowHeight());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.ROW_COUNT, properties.getProcessedRowCount());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getProcessedProductStampingSeqPanelName());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getProcessedFontName(), properties.getProcessedFontStyle(), properties.getProcessedFontSize()));
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getProcessedPanelSize());
			processedProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
					createAuthroizedMenuItems( properties.getProcessedProductStampingSeqMenuItems(), properties.getProcessedMenuUserGroups()));

			upcomingProductStampingSeqPanelProperties = new HashMap<String, Object>();
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.COLUMN_HEADINGS, properties.getUpcomingProductColumnHeadings());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.METHOD_NAMES, properties.getUpcomingProductMethodNames());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.ROW_HEIGHT, properties.getUpcomingRowHeight());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.PANEL_NAME, properties.getUpcomingProductStampingSeqPanelName());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.DRAG_AND_DROP_ENABLED, Boolean.valueOf(properties.getUpcomingDragAndDropEnabled()));
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLUMN,properties.getUpcomingHighlightColumn());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_VALUE,properties.getUpcomingHighlightValue());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.HIGHLIGHT_COLOR,properties.getUpcomingHighlightColor());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.FONT, getFont(properties.getUpcomingFontName(), properties.getUpcomingFontStyle(), properties.getUpcomingFontSize()));
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.PANEL_SIZE, properties.getUpcomingProductPanelSize());
			upcomingProductStampingSeqPanelProperties.put(DefaultScheduleClientProperty.POPUP_MENU_ITEMS, 
					createAuthroizedMenuItems( properties.getUpcomingProductStampingSeqMenuItems(), properties.getUpcomingMenuUserGroups()));

		}
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
		IAccessControlManager acm = ClientMain.getInstance()
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
		Font aFont = new Font(fontName, style, size);
		return aFont;
	}

	protected void publishDataChanged(Object list,SchedulingEventType eventId) {
		final SchedulingEvent event = new SchedulingEvent(list, eventId);
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				EventBus.publish(event);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public synchronized void onEvent(SchedulingEvent event) {
		if(!(event.getTargetObject() instanceof ObjectTablePane)) return;

		scheduleMainPanel.getMainWindow().clearMessage();

		if(event.getEventType() == SchedulingEventType.SELECT_LAST_PRODUCT) {
			selectLastProduct((ObjectTablePane<MultiValueObject<BaseProduct>>)event.getTargetObject());
			return;
		}
		
		
		if(event.getEventType() == SchedulingEventType.GO_WELD_ON) {
			goWeldOn((ObjectTablePane<MultiValueObject<ProductStampingSequence>>)event.getTargetObject());
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
		case DELETE_LOT:
			deleteLot(tablePane);
			break;
		case UPDATE_LOT_SIZE:
			updateLotSize(tablePane);
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
		case UPDATE_COMMENT:
			updateComment(tablePane);
			break;	
		case CHANGE_TO_UNSENT:
			changeToUnsent(tablePane);
			break;
		case SET_NEXT:
			setNext(tablePane);
			break;
		case FINISH_LOT:
			finishLot(tablePane);
			break;
			
		default :
		}

		requestFocusOnProductId();
	}


	private boolean isEligibleForUnsend(PreProductionLot preProductionLot) {
		return (
				preProductionLot != null &&
				preProductionLot.getSendStatus().getId() == 1 &&
				preProductionLot.getStampedCount() == 0
		);
	}

	
	protected void changeToUnsent(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		try {
			if (checkScheduleChanged()) return;
			List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
			playWarnSound();
			if(MessageDialog.confirm(scheduleMainPanel.getMainWindow(),"Do you want to Unsent the selected Lots?")) {
				String processLocation = properties.getProcessLocation();
				for (MultiValueObject<PreProductionLot> selPreProductionLot: selectedLots) {
					if (isEligibleForUnsend(selPreProductionLot.getKeyObject())) {
						if (getDao(PreProductionLotDao.class).changeToUnsent(selPreProductionLot.getKeyObject().getProductionLot(), processLocation) < 1) {
							MessageDialog.showError("Cannot unsend - lot \'" + selPreProductionLot.getKeyObject().getProductionLot() + "\' status has changed.");
							playNGSound();
							break;
						}
					} else {
						scheduleMainPanel.getMainWindow().setErrorMessage("Lot not eligible for unsend process .");
						playNGSound();
						break;
					}			
				}			
				refreshLots();
			}
		} catch (Exception e) {
			scheduleMainPanel.getMainWindow().setErrorMessage("An Error occurred while Unsending the Production Lot.");
			logger.error(e, "An Error occurred while Unsending the Production Lot.");
			playNGSound();
			e.printStackTrace();
		}
	}

	private void goWeldOn(ObjectTablePane<MultiValueObject<ProductStampingSequence>> tablePane) {
		try {
			ProductStampingSequence productStampingSequence=tablePane.getSelectedItem().getKeyObject();
			String selectedVin = productStampingSequence.getProductId();
			playWarnSound();
			if(MessageDialog.confirm(scheduleMainPanel.getMainWindow(),"Do you want to manually send the selected ProductId to WeldON?"))
			{
				FrameDao frameDao=getDao(FrameDao.class);
				ProcessPointDao processPointDao=getDao(ProcessPointDao.class);
				ProductionLotDao productionLotDao=getDao(ProductionLotDao.class);
				PreProductionLotDao preProductionLotDao=getDao(PreProductionLotDao.class);

				String weldOnProcessPointId=getProperties().getOnProcessPoint();
				if(StringUtils.isBlank(weldOnProcessPointId))
				{
					scheduleMainPanel.getMainWindow().setErrorMessage("ON_PROCESS_POINT Property not configured");
					playNGSound();
					return;
				}
				List<ProductResult> productResultList=getDao(ProductResultDao.class).findAllByProductAndProcessPoint(selectedVin, weldOnProcessPointId);
				if(productResultList.size()>0)
				{
					scheduleMainPanel.getMainWindow().setErrorMessage("ProductId:" + selectedVin + " has already passed through WELD ON Process Point.");
					playNGSound();
					return;
				}
				Frame frame=frameDao.findByKey(selectedVin);       
				java.sql.Date productStartDate = new java.sql.Date(System.currentTimeMillis());
				String divisionId =processPointDao.findById(weldOnProcessPointId).getDivisionId();
				String gpcsProcessLocation =getDao(GpcsDivisionDao.class).findByDivision(divisionId).getGpcsProcessLocation();
				ProductionLot productionLot =productionLotDao.findByKey(frame.getProductionLot());
				if (gpcsProcessLocation.equals("WE"))
				{
					String lineNo =productionLot.getWeLineNo();
					String strProcessLocation =productionLot.getWeProcessLocation();
					DailyDepartmentSchedule schedule = getDao(DailyDepartmentScheduleDao.class).findByActualTime(lineNo, strProcessLocation,productionLot.getPlantCode(),new Timestamp(Calendar.getInstance().getTimeInMillis()) );
					if(schedule!=null)
						productStartDate = schedule.getId().getProductionDate();
				}
				frame.setProductStartDate(productStartDate);
				frameDao.save(frame);      
				if (productionLot.getLotStatus() ==PreProductionLotSendStatus.WAITING.getId())
				{
					productionLot.setLotStatus(PreProductionLotSendStatus.INPROGRESS.getId());
					productionLotDao.save(productionLot);
				}
				productStampingSequence.setSendStatus(PreProductionLotSendStatus.DONE.getId());
				getDao(ProductStampingSequenceDao.class).save(productStampingSequence);
				PreProductionLot preProductionLot=preProductionLotDao.findByKey(frame.getProductionLot());
				int iStampCount = preProductionLot.getStampedCount();
				if (preProductionLot.getLotSize() >= iStampCount + 1)
				{
					preProductionLot.setStampedCount( iStampCount + 1);
					if (preProductionLot.getLotSize() == iStampCount + 1)
					{
						preProductionLot.setSendStatus(PreProductionLotSendStatus.DONE);
					}
					preProductionLotDao.save(preProductionLot);
				}
				ServiceFactory.getService(TrackingService.class).track(frame,weldOnProcessPointId);
				refreshLots();
			}
		} catch (Exception e) {
			scheduleMainPanel.getMainWindow().setErrorMessage("An Error occurred while sending the Product to Weldon.");
			logger.error(e, "An Error occurred while sending the Product to Weldon.");
			playNGSound();
			e.printStackTrace();
		}
	}

	

	private void requestFocusOnProductId() {
		if(scheduleMainPanel != null)
			scheduleMainPanel.requestFocusOnProductId();

	}

	protected void completeLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		//tbd - this function currently only required on Preproduction Lot sequence
		MultiValueObject<PreProductionLot> lot = scheduleMainPanel.getCurrentLotPanel().getCurrentLot();
		PreProductionLot preProdLot=lot.getKeyObject();
		preProdLot.setSendStatus(PreProductionLotSendStatus.DONE);
		getDao(PreProductionLotDao.class).save(preProdLot);
		refreshLots();
	}
	
	protected void finishLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
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
		case DELETE_LOT :
			canEnable = isSingleWaitingLotSelected(tablePane);
			break;
		case UPDATE_LOT_SIZE :
			canEnable = isSingleWaitingLotSelected(tablePane);
			break;
		case SELECT_LAST_ORDER:
			canEnable = isRowSelected(tablePane);
			break;
		case CREATE_SHIPPING_LOT:
			canEnable = canCreateShippingLot(tablePane);
			break;
		case HOLD :
			canEnable = canHold(tablePane);
		case RELEASE :
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

	private boolean canMoveUp(ObjectTablePane<?> tablePane){
		int[] rows = tablePane.getTable().getSelectedRows();
		if(rows.length == 0 || rows[0] == 0) return false;
		// check to see if the selected rows are the second kd lot in the list
		if(!isSecondKdLot(tablePane)) return true;
		return !properties.lockFirstLot();
	}

	private boolean isSecondKdLot(ObjectTablePane<?> tablePane) {
		int[] rows = tablePane.getTable().getSelectedRows();
		if(!properties.isMoveByKdLot()) return rows[0] == 1;
		PreProductionLot lot0 = getPreProductionLot(tablePane.getItems().get(0));
		PreProductionLot lot1 = getPreProductionLot(tablePane.getItems().get(rows[0] -1));
		return lot0.isSameKdLot(lot1);
	}

	private boolean isRowSelected(ObjectTablePane<?> tablePane) {
		return tablePane.getTable().getSelectedRow() != -1;
	}

	private boolean canCreateShippingLot(ObjectTablePane<?> tablePane){
		if(tablePane.getSelectedItem() == null) return false;
		PreProductionLot lot = getPreProductionLot(tablePane.getSelectedItem());
		if(lot == null) return false;

		SubProductShippingDao shippingDao = getDao(SubProductShippingDao.class);
		List<SubProductShipping> shippingLots = shippingDao.findAllWithSameKdLot(lot.getKdLot(),lot.getProductionLot());
		return shippingLots.isEmpty();
	}

	private PreProductionLot getPreProductionLot(Object obj) {
		if(!(obj instanceof MultiValueObject<?>)) return null;
		MultiValueObject<?> valueObject = (MultiValueObject<?>) obj;
		if(!(valueObject.getKeyObject() instanceof PreProductionLot)) return null;
		return (PreProductionLot)valueObject.getKeyObject();
	}

	private boolean canMoveDown(ObjectTablePane<?> tablePane){
		int[] rows = tablePane.getTable().getSelectedRows();
		if(rows.length == 0) return false;
		if(rows[0] == 0) return !properties.lockFirstLot();
		return rows[rows.length -1] < tablePane.getTable().getRowCount() -1;
	}

	protected void holdAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;

		List<MultiValueObject<PreProductionLot>> tableItems = tablePane.getItems();
		int[] selectedRows = tablePane.getTable().getSelectedRows();
		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();
		int firstRow = selectedRows[0];
		int lastRow = selectedRows[selectedRows.length - 1];
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
		refreshLots();
	}

	protected void realeaseAndMove(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;

		List<MultiValueObject<PreProductionLot>> selectedLots = tablePane.getSelectedItems();

		if(selectedLots.isEmpty()) return;
		List<MultiValueObject<PreProductionLot>> upcomingLots = scheduleMainPanel.getUpcomingLotPanel().getItems();
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

		refreshLots();
		ObjectTablePane<MultiValueObject<PreProductionLot>> upcomingLotPane = scheduleMainPanel.getUpcomingLotPanel();
		upcomingLotPane.scrollToBottom();
	}

	private void selectLastLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		getLastLotSelectionScreen().open(tablePane.getSelectedItems());
	}

	private void selectLastProduct(ObjectTablePane<MultiValueObject<BaseProduct>> tablePane) {
		LastProductSelectionDialog.sendToPlc(scheduleMainPanel.getMainWindow(),properties.getLastLotDeviceId(),tablePane.getSelectedItems());
	}

	protected void addLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		playWarnSound();
		HashMap<String, List<PreProductionLot>> lotsMap = new HashMap<String, List<PreProductionLot>>();
		lotsMap.put(UPCOMING, getLotList(scheduleMainPanel.getUpcomingLotPanel().getItems()));
		lotsMap.put(CURRENT, getLotList(scheduleMainPanel.getCurrentLotPanel().getLotPanel().getItems()));
		lotsMap.put(PROCESSED, getLotList(scheduleMainPanel.getProcessedLotPanel().getItems()));
		
		new AddLotDialog(scheduleMainPanel.getController(), lotsMap);
		refreshLots();
	}
	
	private boolean isSingleWaitingLotSelected(ObjectTablePane<?> tablePane) {
		int[] rows = tablePane.getTable().getSelectedRows();
		if (rows.length != 1) {
			return false;
		}
		PreProductionLot selectedLot = getPreProductionLot(tablePane.getSelectedItem());
		if (selectedLot.getSendStatus() != PreProductionLotSendStatus.WAITING) {
			return false;
		}
		return true;
	}
	
	protected void deleteLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if (MessageDialog.confirm(scheduleMainPanel.getMainWindow(),"Do you want to Delete the selected Lot?")) {
			PreProductionLot selectedLot = getPreProductionLot(tablePane.getSelectedItem());
			PreProductionLot previousLot = getDao(PreProductionLotDao.class).findParent(selectedLot.getProductionLot());
			if (previousLot != null) {
				String nextProductionLot = selectedLot.getNextProductionLot();
				previousLot.setNextProductionLot(nextProductionLot);
				getDao(PreProductionLotDao.class).save(previousLot);
			}
			getDao(PreProductionLotDao.class).remove(selectedLot);
			getDao(ProductionLotMbpnSequenceDao.class).deleteByProductionLot(selectedLot.getId()); 
			refreshLots();
		}
	}
	
	protected void updateLotSize(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		String input = MessageDialog.showInputDialog(scheduleMainPanel.getMainWindow(), SchedulingEventType.UPDATE_LOT_SIZE.getMessage(), "Please enter the new lot size", 4, false);
		while (input != null && (input.equals("") || input.equals("0") || !StringUtils.isNumeric(input))) {
			MessageDialog.showError(scheduleMainPanel.getMainWindow(), "Please enter a valid lot size");
			input = MessageDialog.showInputDialog(scheduleMainPanel.getMainWindow(), SchedulingEventType.UPDATE_LOT_SIZE.getMessage(), "Please enter the new lot size", 4, false);
		}
		if (input == null) {
			return; // user canceled
		}
		int newLotSize = Integer.valueOf(input);
		PreProductionLot selectedLot = getPreProductionLot(tablePane.getSelectedItem());
		selectedLot.setLotSize(newLotSize);
		getDao(PreProductionLotDao.class).save(selectedLot);
		refreshLots();
	}
	
	protected void updateComment(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		PreProductionLot selectedLot = getPreProductionLot(tablePane.getSelectedItem());
		String input = MessageDialog.showInputDialog(scheduleMainPanel.getMainWindow(),
				SchedulingEventType.UPDATE_COMMENT.getMessage(), "Please enter the new comment", selectedLot.getNotes(),
				60, false);
		if (input == null) {
			return;
		}
		selectedLot.setNotes(input);
		getDao(PreProductionLotDao.class).save(selectedLot);
		refreshLots();
	}
	
	public String getProductName() {
		return scheduleMainPanel.getMainWindow().getProductType().getProductName();
	}

	public ProductSpecData getProductSpecData() {
		if (productSpecData == null) {
			String productType = ProductType.getType(getLotProductType()).name();
			productSpecData = new ProductSpecData(productType);
		}
		return productSpecData;
	}

	private String getLotProductType() {
		return StringUtils.isEmpty(getProperties().getAddLotDialogProductType()) ? getProperties().getProductType() : getProperties().getAddLotDialogProductType();
	}

	private List<PreProductionLot> getLotList(List<MultiValueObject<PreProductionLot>> mvObjects) {
		List<PreProductionLot> lotList = new ArrayList<PreProductionLot>();
		for(MultiValueObject<PreProductionLot> item: mvObjects) {
			lotList.add(item.getKeyObject());
		}
		return lotList;
	}
	
	private void createShippingLot(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane){
		MultiValueObject<PreProductionLot> item = tablePane.getSelectedItem();
		if(item == null) return;

		SubProductShippingDao shippingDao = getDao(SubProductShippingDao.class);
		List<SubProductShipping> shippingLots = shippingDao.findAllWithSameKdLot(item.getKeyObject().getKdLot(),item.getKeyObject().getProductionLot());
		if(!shippingLots.isEmpty()) return;
		playWarnSound();
		if(MessageDialog.confirm(scheduleMainPanel.getMainWindow(),"Are you sure to create shipping lots for " + item.getKeyObject().getProductionLot())){
			shippingLots = shippingDao.createKnuckleShippingLots(item.getKeyObject().getProductionLot());
			logger.info("created shipping lots for lot number " + item.getKeyObject().getProductionLot());
		}
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
			lastUpdateTimeKN = getDao(PreProductionLotDao.class).findLastUpdateTimestamp(properties.getProcessLocation());
		} catch (Exception e) {
			;//ok keep going if did not find lastUpdateTimeKN
		}
		if(lastUpdateTimeKN == null || lastUpdateTimeStamp == null) return false;
		return lastUpdateTimeKN.after(lastUpdateTimeStamp);
	}

	protected boolean checkScheduleChanged() {
		if(isScheduleChanged()){ 
			refreshLots();
			scheduleMainPanel.getMainWindow().setErrorMessage("The schedule was refreshed because it was changed from other app.Please redo your operation");
			return true;
		}
		return false;
	}
	
	@EventTopicSubscriber(topic="IProductOnNotification")
	public void productOnEvent(String event, Request request) {
        try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
			playNGSound();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			playNGSound();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			playNGSound();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			playNGSound();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			playNGSound();
		}
   }

	@EventTopicSubscriber(topic="IStampedCountChangedNotification")
	public void stampedCountChangedEvent(String event, Request request) {
		try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
			playNGSound();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			playNGSound();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			playNGSound();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			playNGSound();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			playNGSound();
		}
	}

	@Override
	public void stampedCountChanged(String productionLot, int stampedCount) {
		logger.info(IStampedCountChangedNotification.class.getSimpleName() + ": Stamped count changed to " + stampedCount + " for production lot " + productionLot);
		refreshLots();
	}

	/**
	 * processed product stamped and update the current production lot
	 * @param productionLot
	 * @param processLocation
	 * @param stampedCount
	 */
	public void execute(String productionLot, String processLocation,	int stampedCount) {
		if(!isInitialized()){
			logger.info("Received product on event before system initialized: Production Lot:",
					productionLot, " Process Location:", processLocation, " stampedCount:" + stampedCount);
			return;
		}

		if(!properties.getProcessLocation().equalsIgnoreCase(processLocation)) return;

		logger.info("Product Loaded:", productionLot, " processLocation:", processLocation, " stampedCount:" + stampedCount);
		updateLots(productionLot, stampedCount);
	}

	protected void updateLots(String productionLot, int stampedCount) {
		MultiValueObject<PreProductionLot> lot = scheduleMainPanel.getCurrentLotPanel().getCurrentLot();

		if(isScheduleChanged() || isLotChanged(productionLot, lot)){
			refreshLots();
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
				.getUpcomingLotPanel().getItems();
		List<PreProductionLot> upcomingLots = new ArrayList<PreProductionLot>(
				items.size());
		for (MultiValueObject<PreProductionLot> item : items) {
			upcomingLots.add(item.getKeyObject());
		}
		items = scheduleMainPanel.getCurrentLotPanel().getLotPanel()
				.getItems();
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
		{			
			lastLotSelectionScreen = new LastProductSelectionDialog(scheduleMainPanel.getMainWindow(), getProperties());
		}
		playWarnSound();
		return lastLotSelectionScreen;
	}

	public boolean isProcessedProductOrLot() {
		return (Boolean)processedPanelProperties.get(DefaultScheduleClientProperty.IS_PROCESSED_PRODUCT_OR_LOT);
	}
	
	public boolean isMultipleActiveLots() {
		return properties.isMultipleActiveLots();
	}

	protected ProductType getProductType() {
		if(productType == null)
			productType =  ProductTypeCatalog.getProductType(getProperties().getProductType());

		return productType;
	}

	public void execute(String productId, String message, String planCode, MessageType type) {

		if(StringUtils.isEmpty(properties.getPlanCode()) || !getProperties().getPlanCode().contains(planCode)) return;

		if(type.ordinal() >= MessageType.ERROR.ordinal()){
			scheduleMainPanel.getMainWindow().setErrorMessage(message);
			playNGSound();
		} else {
			scheduleMainPanel.getMainWindow().setMessage(message);
		} 

		if(!StringUtils.isEmpty(productId)) setCurrentProductId(productId);

	}

	protected void setCurrentProductId(String productId) {
		if(scheduleMainPanel.getCurrentLotPanel() != null && scheduleMainPanel.getCurrentLotPanel().getProductPane() != null){
			JTextField prodIdTextField = scheduleMainPanel.getCurrentLotPanel().getProductPane().getProductIdTextField();
			prodIdTextField.setText(productId);
			prodIdTextField.select(0, productId.length());
		}
	}

	protected void clearCurrentProductId() {
		if(scheduleMainPanel.getCurrentLotPanel() != null && scheduleMainPanel.getCurrentLotPanel().getProductPane() != null)
			scheduleMainPanel.getCurrentLotPanel().getProductPane().getProductIdTextField().setText("");

		scheduleMainPanel.getMainWindow().clearMessage();
	}

	@Override
	public void execute(String productId, String productionLot,
			String processLocation, int stampedCount) {
		// TODO Auto-generated method stub
		
	}
	
	public Map<String, Object> getUpcomingProductStampingSeqPanelProperties() {
		return upcomingProductStampingSeqPanelProperties;
	}

	public void setUpcomingProductPanelProperties(
			Map<String, Object> upcomingProductPanelProperties) {
		this.upcomingProductStampingSeqPanelProperties = upcomingProductPanelProperties;
	}
	
	public Map<String, Object> getProcessedProductStampingSeqPanelProperties() {
		return processedProductStampingSeqPanelProperties;
	}

	public void setProcessedProductStampingSeqPanelProperties(
			Map<String, Object> processedProductStampingSeqPanelProperties) {
		this.processedProductStampingSeqPanelProperties = processedProductStampingSeqPanelProperties;
	}
		
	public boolean isProductStampingInfoEnabled() {
		return ProductStampingInfoEnabled;
	}

	public void setProductStampingInfoEnabled(boolean productStampingInfoEnabled) {
		ProductStampingInfoEnabled = productStampingInfoEnabled;
	}

	public BaseProduct getExpectedProduct() {
		return expectedProduct;
	}

	public void setExpectedProduct(BaseProduct expectedProduct) {
		this.expectedProduct = expectedProduct;
	}

	public void setInitialized(boolean initialized) {
		this.isInitialized  = initialized;

	}

	public boolean isInitialized() {
		return isInitialized;
	}


	public BuildAttributeCache getBuildAttributeCache() {
		return buildAttributeCache;
	}
	
	public void updateStampingSeqLinkedList(List<ProductStampingSequence> linkedProductStampSeq, Set<String> waitingLotsSet, Set<String> inProgressLotsSet)	{
		for(ProductStampingSequence productStampingSequence: linkedProductStampSeq) {
			if(waitingLotsSet.contains(productStampingSequence.getProductionLot())) {
				waitingProductStampingSeqList.add(productStampingSequence);
			} else if (inProgressLotsSet.contains(productStampingSequence.getProductionLot())) {
				inProgressProductStampingSeqList.add(productStampingSequence);
			}
		}
	}
	
	private ClientAudioManager getAudioManager() 
	{ 
		 return audioManager; 
     }
	
	public void playNGSound()
	{
		if(properties.isSoundsEnabled())
			getAudioManager().playNGSound();
	}
	
	public void playOKSound()
	{
		if(properties.isSoundsEnabled())
			getAudioManager().playOKSound();
	}
	
	public void playWarnSound()
	{
		if(properties.isSoundsEnabled())
			getAudioManager().playWarnSound();
	}
	
	public void setNext(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if(checkScheduleChanged()) return;
		int[] selectedRows = tablePane.getTable().getSelectedRows();
		List<MultiValueObject<PreProductionLot>> selectedItems = tablePane.getSelectedItems();
		if(selectedRows[0] == 0) 
		{
			scheduleMainPanel.getMainWindow().setErrorMessage("Selected row is at the top of the list, can not move up.");
			return;
		}
		if(selectedItems.size()>1)
		{
			scheduleMainPanel.getMainWindow().setErrorMessage("Please select one row only");
			return;
		}
		PreProductionLotUtils lotUtils = new PreProductionLotUtils(tablePane.getItems(),getProperties().isMoveByKdLot(),getProperties().lockFirstLot());
		List<PreProductionLot> changedLots = lotUtils.moveTop(selectedRows[0]);
		updateChangedLots(changedLots);
		updateTablePane(tablePane,selectedItems);
		updateLastCurrentLot(tablePane.getItems().get(0).getKeyObject().getProductionLot());
		logger.info("Moved the lot:"+tablePane.getSelectedItems().get(0)+ " to top of list");
		refreshLots();
	}
	
	private List<String> getExpectedMbpns(String prodLot) {

		List<String> expectedMbpnsForLot = new ArrayList<String>();

		List<ProductionLotMbpnSequence> productionLotMbpnSequences = getProductionLotMbpnSequenceDao()
				.findAllByProductionLot(prodLot);

		for (ProductionLotMbpnSequence productionLotMbpnSequence : productionLotMbpnSequences) {

			expectedMbpnsForLot.add(productionLotMbpnSequence.getMbpn());
		}

		return expectedMbpnsForLot;
	}
	
	private String getMbpnDescriptions(List<String> expectedMbpns){
		StringBuilder mbpnDesc = new StringBuilder();
		for(String mbpnSpec:expectedMbpns) {
			String desc = mbpnSpec;
			Mbpn mbpn = getMbpnDao().findByKey(mbpnSpec);
			if(mbpn != null)desc = StringUtils.isEmpty(mbpn.getDescription())?mbpn.getMbpn():mbpn.getDescription();		
			if(StringUtils.isNotBlank(mbpnDesc.toString())) mbpnDesc.append(",");
			mbpnDesc.append(desc);
		}
		
		return mbpnDesc.toString();
	}
	
	protected MbpnDao getMbpnDao() {
		if (mbpnDao == null) {
			mbpnDao = ServiceFactory.getDao(MbpnDao.class);
		}
		return mbpnDao;
	}
	
	private ProductionLotMbpnSequenceDao getProductionLotMbpnSequenceDao() {
		if (productionLotMbpnSequenceDao == null) {
			productionLotMbpnSequenceDao = ServiceFactory.getDao(ProductionLotMbpnSequenceDao.class);
		}
		return productionLotMbpnSequenceDao;
	}
	
	public boolean updateComment() {
		boolean updateComment = false;
		String[] values  = properties.getUpcomingMenuItems();
		for(String value : values) {
			if(value.equalsIgnoreCase("Update Comment")) {
				updateComment = true;
			}
		}
		return updateComment;
	}
	
	protected List<PreProductionLot> populatePreProductionLotWithProdDate(List<PreProductionLot> lots) {
		for(PreProductionLot lot : lots) {
			ProductionLot prodLot = getDao(ProductionLotDao.class).findByKey(lot.getProductionLot());
			String prodDate = prodLot != null ? prodLot.getProductionDate().toString() : "";
			lot.setProductionDate(prodDate);
		}
		return lots;
	}
}
