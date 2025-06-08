package com.honda.galc.client.linesidemonitor.controller;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventTopicSubscriber;

import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.device.DeviceManager;
import com.honda.galc.client.device.ei.EiDevice;
import com.honda.galc.client.linesidemonitor.LineSideMonitorData;
import com.honda.galc.client.linesidemonitor.property.LineSideMonitorPropertyBean;
import com.honda.galc.client.linesidemonitor.view.LineSideMonitorWindow;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.ui.MainWindow;
import com.honda.galc.client.ui.event.Event;
import com.honda.galc.client.ui.event.EventType;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.PrintAttributeFormatDao;
import com.honda.galc.dao.product.BlockDao;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.ProductionLotDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.device.DeviceListener;
import com.honda.galc.device.IDeviceData;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.device.dataformat.ProductIdHighlight;
import com.honda.galc.device.dataformat.ProductIdRefresh;
import com.honda.galc.entity.EntityCache;
import com.honda.galc.entity.conf.PrintAttributeFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductionLot;
import com.honda.galc.net.Request;
import com.honda.galc.notification.service.IProductPassedNotification;
import com.honda.galc.property.FrameLinePropertyBean;
import com.honda.galc.service.printing.PrintAttributeConvertor;
import com.honda.galc.service.printing.PrintAttributeServiceUtil;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * <h3>LineSideMonitorController Class description</h3>
 * <p> LineSideMonitorController description </p>
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
 * Mar 30, 2011
 *
 *
 */
public class LineSideMonitorController implements IProductPassedNotification, DeviceListener{

	public enum SpecialColorType { NONE, REBUILD, NEW_LOT, STRAGGLER, CUT_LOT };

	private MainWindow window;

	private List<PrintAttributeFormat> attributeFormats;

	private List<? extends BaseProduct> products = new ArrayList<Product>();

	private PrintAttributeConvertor convertor;

	private EntityCache<ProductionLot,String> productionLotCache;

	private String lastProcessedProduct = null;

	private Timer checkLastProductTimer = null;

	private ClientAudioManager audioManager;


	public LineSideMonitorController(MainWindow window) {

		this.window = window;

		initializeController();

		AnnotationProcessor.process(this);

		if(isAlarmConfigured())
			this.audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class,getProcessPointId()));

	}

	public MainWindow getWindow() {
		return window;
	}

	public LineSideMonitorWindow getLsmWindow() {
		return (LineSideMonitorWindow) window;
	}

	public void setWindow(MainWindow window) {
		this.window = window;
	}

	protected void initializeController() {

		productionLotCache = new EntityCache<ProductionLot,String>(getDao(ProductionLotDao.class));

		if(!StringUtils.isNotBlank(getPropertyBean().getTargetProcessPoint())) {
			window.getLogger().info("Target process point is not set. User can manually update the processed product");
		}else {
			if(getPropertyBean().isProductIdFromPlc() || getPropertyBean().isProductIdRefreshFromPlc() || getPropertyBean().isProductIdHighlightFromPlc()) {
				registerDeviceListeners();
				window.getLogger().info("registered to receive product id update info from PLC");
			}
			if(getPropertyBean().isPollingLastProduct()) {
				createCheckLastProductTimer();
				window.getLogger().info("Started timer to poll last processed product");
			}
			if(getPropertyBean().isSubscribingProcessedProduct()){
				// subscribe to processed product event
				window.getLogger().info("starting to subscribe to IProductPassedNotification");
			}
		}
	}

	private void registerDeviceListeners() {
		EiDevice device = DeviceManager.getInstance().getEiDevice();
		device.registerDeviceListener(this, getProcessData());
	}

	private List<IDeviceData> getProcessData() {
		ArrayList<IDeviceData> list = new ArrayList<IDeviceData>();
		list.add(new ProductId());
		list.add(new ProductIdRefresh());
		list.add(new ProductIdHighlight());
		return list;
	}

	public void cleanUp() {
		// TODO add cleanup code here
	}

	private String getProcessPointId() {

		return window.getApplicationContext().getProcessPointId();

	}

	public List<PrintAttributeFormat> getAttributeFormats() {

		if(attributeFormats == null)
			attributeFormats = getDao(PrintAttributeFormatDao.class).findAllByFormId(getProcessPointId());

		return attributeFormats;

	}

	public String[] getColumnNames() {

		int ColumnCount = getPropertyBean().isHaveCheckBoxColumn() ? getAttributeFormats().size() + 1 : getAttributeFormats().size();
		String[]columnNames = new String[ColumnCount];

		int i = 0;
		if(getPropertyBean().isHaveCheckBoxColumn()) columnNames[i++] = "";

		for(PrintAttributeFormat item : getAttributeFormats()) {
			columnNames[i++] = item.getAttribute();
		}
		return columnNames;
	}

	public boolean autoAdjustColumnSize() {

		for(PrintAttributeFormat item : getAttributeFormats()) {
			if(item.getLength() <= 0) return true;
		}

		return false;

	}

	public boolean isEngine() {
		return "ENGINE".equalsIgnoreCase(getPropertyBean().getProductType());
	}
	
	public boolean isBlock() {
		return "BLOCK".equalsIgnoreCase(getPropertyBean().getProductType());
	}

	public boolean isMbpn() {
		return "MBPN".equalsIgnoreCase(getPropertyBean().getProductType());
	}

	public boolean isFrame() {
		return "FRAME".equalsIgnoreCase(getPropertyBean().getProductType());
	}

	public int[] getColumnSizes() {

		int ColumnCount = getPropertyBean().isHaveCheckBoxColumn() ? getAttributeFormats().size() + 1 : getAttributeFormats().size();

		int[] columnSizes = new int[ColumnCount];
		int i = 0;
		if(getPropertyBean().isHaveCheckBoxColumn()) columnSizes[i++] = 20;

		for(PrintAttributeFormat item : getAttributeFormats()) {
			columnSizes[i++] = item.getLength();
		}	

		return columnSizes;

	}

	public List<? extends BaseProduct> findProducts(String productId,int processedSize,int upcomingSize) {
		if(getPropertyBean().isSequenceFromInProcessProduct()) {
			if(isEngine())
				return getDao(EngineDao.class).findAllByInProcessProduct(productId, processedSize, upcomingSize);
			else if(isBlock()){
				return getDao(BlockDao.class).findAllByInProcessProduct(productId, processedSize, upcomingSize);
			} else if(isMbpn())
				return getDao(MbpnProductDao.class).findAllByInProcessProduct(productId, processedSize, upcomingSize);
			else
			{
				if (getPropertyBean().isProcessEmptyEnabled()) {
					return getDao(FrameDao.class).findAllByProductCarrier(getProductSequenceProcessPoint(), productId,
							processedSize, upcomingSize);
				} else
					return getDao(FrameDao.class).findAllByInProcessProduct(productId, processedSize, upcomingSize);
			}
		}else {
			if(isEngine())
				return getDao(EngineDao.class)
						.findAllByProductSequence(getProductSequenceProcessPoint(), productId, processedSize, upcomingSize);
			else if(isBlock()) {
				 return getDao(BlockDao.class)
						 .findAllByProductSequence(getProductSequenceProcessPoint(), productId, processedSize, upcomingSize);
			} else if(isMbpn()) 
				return getDao(MbpnProductDao.class)
						.findAllByProductSequence(getProductSequenceProcessPoint(), productId, processedSize, upcomingSize);
			else return getDao(FrameDao.class)
					.findAllByProductSequence(getProductSequenceProcessPoint(), productId, processedSize, upcomingSize);
		}	
	}

	public PrintAttributeConvertor getPrintAttributeConvertor() {
		if(convertor == null) convertor = new PrintAttributeConvertor(getAttributeFormats());
		return convertor;
	}

	public LineSideMonitorPropertyBean getPropertyBean() {
		return PropertyService.getPropertyBean(LineSideMonitorPropertyBean.class, getWindow().getApplication().getApplicationId());

	}

	public FrameLinePropertyBean getFrameLinePropertyBean() {
		return PropertyService.getPropertyBean(FrameLinePropertyBean.class, getWindow().getApplication().getApplicationId());
	}

	public int getNumberColumnIndex() {
		String[] columnNames = getColumnNames();
		for(int i = 0 ; i < columnNames.length; i++) {
			if("#".equals(columnNames[i])) return i;
		}
		return -1;
	}

	public List<LineSideMonitorData> findAllDisplayItems(List<LineSideMonitorData> oldLsmData) {
		return findAllDisplayItems(oldLsmData, null);
	}

	public List<LineSideMonitorData> findAllDisplayItems(List<LineSideMonitorData> oldLsmData, String targetProductId) {
		if (!StringUtils.isEmpty(targetProductId)) {
			lastProcessedProduct = targetProductId;
		}

		List<LineSideMonitorData> items = new ArrayList<LineSideMonitorData>();
		int processedSize = getPropertyBean().getProcessedProductNumber();
		if(processedSize < 0) processedSize = 1;

		int upcomingSize = getPropertyBean().getUpcomingProductNumber();

		products = findProducts(lastProcessedProduct, processedSize, upcomingSize);
		boolean flag = true;
		final boolean cacheMode = getPropertyBean().isAllowCacheMode();
		for(BaseProduct product : products) {
			LineSideMonitorData data = cacheMode ? getCachedData(product, oldLsmData) : createData(product);
			data.setChecked(flag);
			items.add(data);
			if(product.getProductId().equals(lastProcessedProduct)) flag = false;
		}
		if (cacheMode) {
			assignColors(items);
		}
		return items;

	}

	private BaseProduct findNextProduct(String productId) {

		if(StringUtils.isEmpty(productId)) return null;

		for(int i = 0; i<products.size() -1;i++) {
			if(products.get(i).getProductId().equals(productId)) return products.get(i+1); 
		}
		return null;
	}

	private DataContainer prepareProductData(BaseProduct product) {

		DefaultDataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_ID, product.getProductId());
		dc.put(DataContainerTag.PRODUCT_SPEC_CODE, product.getProductSpecCode());
		dc.put(DataContainerTag.PRODUCT_TYPE, product.getProductType());
		dc.put(DataContainerTag.PRODUCTION_LOT, product.getProductionLot());
		dc.put(DataContainerTag.PROCESS_POINT_ID, getWindow().getApplication().getApplicationId());
		dc.put(Product.class, product);
		dc.put(product.getClass(), product);
		ProductionLot prodLot = null;
		if(product.getProductionLot() != null) {
			prodLot = productionLotCache.findByKey(product.getProductionLot());
		}
		product.setProdLot(prodLot);
		dc.put(ProductionLot.class, prodLot);
		return dc;
	}

	private LineSideMonitorData getCachedData(BaseProduct product, List<LineSideMonitorData> cachedLsmData) {
		if (product != null && cachedLsmData != null) {
			for (LineSideMonitorData lsmd : cachedLsmData) {
				if (product.equals(lsmd.getProduct())) {
					return lsmd;
				}
			}
		}
		return createData(product);
	}

	private LineSideMonitorData createData(BaseProduct product) {

		LineSideMonitorData data = new LineSideMonitorData(getAttributeFormats());
		data.setProduct(product);
		getPrintAttributeConvertor().make(prepareProductData(product));
		data.setValues(getPrintAttributeConvertor().getValues());
		return data;

	}

	private void assignColors(List<LineSideMonitorData> items) {
		if (items == null || items.isEmpty()) {
			return;
		}

		final int colorChangeColumnIndex = findColorChangeColumnIndex();
		final int referenceColorIndex = findReferenceColorIndex(items);
		if (referenceColorIndex == -1) {
			assignColors(items.get(0), null, colorChangeColumnIndex);
		}

		for (int i = referenceColorIndex-1; i > -1; i--) {
			LineSideMonitorData item = items.get(i);
			if (item.getBackgroundColor() != null) {
				continue;
			}
			LineSideMonitorData referenceItem = items.get(i+1);
			assignColors(item, referenceItem, colorChangeColumnIndex);
		}

		for(int i = referenceColorIndex+1; i < items.size(); i++) {
			LineSideMonitorData item = items.get(i);
			if (item.getBackgroundColor() != null) {
				continue;
			}
			LineSideMonitorData referenceItem = items.get(i-1);
			assignColors(item, referenceItem, colorChangeColumnIndex);
		}
	}

	private void assignColors(LineSideMonitorData item, LineSideMonitorData referenceItem, int colorChangeColumnIndex) {
		if (item == null) {
			return;
		}
		if (referenceItem == null) {
			item.setBackgroundColor(getPropertyBean().getBackgroundColor());
			item.setForegroundColor(getPropertyBean().getForegroundColor());
			assignSpecialColors(item);
		}
		else {
			Object itemColorChangeColumnValue = item.getValue(colorChangeColumnIndex);
			Object referenceItemColorChangeColumnValue = referenceItem.getValue(colorChangeColumnIndex);
			if (!ObjectUtils.equals(itemColorChangeColumnValue, referenceItemColorChangeColumnValue)) {
				item.setBackgroundColor(getAlternateBackgroundColor(referenceItem.getBackgroundColor()));
				item.setForegroundColor(getMatchingForegroundColor(item.getBackgroundColor()));
			} else {
				item.setBackgroundColor(referenceItem.getBackgroundColor());
				item.setForegroundColor(referenceItem.getForegroundColor());
			}
			assignSpecialColors(item);
		}
	}

	private void assignSpecialColors(LineSideMonitorData item) {
		SpecialColorType type = getSpecialColorType(item);
		if (type != SpecialColorType.NONE) { 
			item.setSpecialBackgroundColor(getSpecialBackgroundColor(type));
			item.setSpecialForegroundColor(getSpecialForegroundColor(type));
		}
	}

	public SpecialColorType getSpecialColorType(LineSideMonitorData item) {
		if (getPropertyBean().getRebuildBackgroundColor() != null) {
			if (getPrintAttributeServiceUtil(item.getProduct().getProductId(), getFrameLinePropertyBean().getAfOnProcessPointId()).getRebuildByKDLot().equals(PrintAttributeServiceUtil.Y)) {
				return SpecialColorType.REBUILD;
			}
		}
		if (getPropertyBean().getNewLotBackgroundColor() != null) {
			if (getPrintAttributeServiceUtil(item.getProduct().getProductId(), getFrameLinePropertyBean().getAfOnProcessPointId()).getPassQtyByKDLot().equals("1")) {
				return SpecialColorType.NEW_LOT;
			}
		}
		if (getPropertyBean().getStragglerBackgroundColor() != null) {
			if (getPrintAttributeServiceUtil(item.getProduct().getProductId(), getPropertyBean().getStragglerPpDelayedAt()).getStragglerByKDLot().equals(PrintAttributeServiceUtil.Y)) {
				return SpecialColorType.STRAGGLER;
			}
		}
		if (getPropertyBean().getCutLotBackgroundColor() != null) {
			if (getPrintAttributeServiceUtil(item.getProduct().getProductId(), getFrameLinePropertyBean().getAfOnProcessPointId()).getCutlotByKDLot().equals(PrintAttributeServiceUtil.Y)) {
				return SpecialColorType.CUT_LOT;
			}
		}
		return SpecialColorType.NONE;
	}

	public Color getSpecialBackgroundColor(SpecialColorType type) {
		switch(type) {
		case NONE:
			return null;
		case REBUILD:
			return getPropertyBean().getRebuildBackgroundColor();
		case NEW_LOT:
			return getPropertyBean().getNewLotBackgroundColor();
		case STRAGGLER:
			return getPropertyBean().getStragglerBackgroundColor();
		case CUT_LOT:
			return getPropertyBean().getCutLotBackgroundColor();
		}
		return null;
	}

	public Color getSpecialForegroundColor(SpecialColorType type) {
		switch(type) {
		case NONE:
			return null;
		case REBUILD:
			return getPropertyBean().getRebuildForegroundColor();
		case NEW_LOT:
			return getPropertyBean().getNewLotForegroundColor();
		case STRAGGLER:
			return getPropertyBean().getStragglerForegroundColor();
		case CUT_LOT:
			return getPropertyBean().getCutLotForegroundColor();
		}
		return null;
	}

	private PrintAttributeServiceUtil getPrintAttributeServiceUtil(String productId, String processPointId) {
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.PRODUCT_ID, productId);
		dc.put(DataContainerTag.PROCESS_POINT_ID, processPointId);
		return new PrintAttributeServiceUtil(dc, window.getLogger());
	}

	public int findColorChangeColumnIndex() {
		int colorChangeColumnIndex = getPropertyBean().getBackgroundColorChangeColumnIndex();
		if (getPropertyBean().isHaveCheckBoxColumn()) {
			colorChangeColumnIndex = colorChangeColumnIndex - 1;
		}
		return colorChangeColumnIndex;
	}

	private int findReferenceColorIndex(List<LineSideMonitorData> items) {
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getBackgroundColor() != null) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the alternate background color to the given background color.
	 */
	public Color getAlternateBackgroundColor(Color currentBackgroundColor) {
		if (currentBackgroundColor == null) {
			return getPropertyBean().getBackgroundColor();
		}
		return ObjectUtils.equals(currentBackgroundColor, getPropertyBean().getBackgroundColor()) ?
				getPropertyBean().getAlternateBackgroundColor() : getPropertyBean().getBackgroundColor();
	}

	/**
	 * Returns the foreground color to match the given background color.
	 */
	public Color getMatchingForegroundColor(Color currentBackgroundColor) {
		if (currentBackgroundColor == null) {
			return getPropertyBean().getForegroundColor();
		}
		return ObjectUtils.equals(currentBackgroundColor, getPropertyBean().getBackgroundColor()) ?
				getPropertyBean().getForegroundColor(): getPropertyBean().getAlternateForegroundColor();
	}

	public void updateLastProcessedProduct(String productId) {

		ExpectedProduct expectedProduct = new ExpectedProduct();
		expectedProduct.setProcessPointId(getPropertyBean().getTargetProcessPoint());
		expectedProduct.setProductId(productId);
		expectedProduct.setLastProcessedProduct(lastProcessedProduct);
		getDao(ExpectedProductDao.class).save(expectedProduct);
		lastProcessedProduct = productId;

	}

	protected String getProductSequenceProcessPoint() {

		return getPropertyBean().getProductSequenceProcessPoint();

	}

	public void initLastProcessedProduct() {
		ExpectedProduct expectedProduct = null;
		if(StringUtils.isNotEmpty(getPropertyBean().getTargetProcessPoint())) {
			expectedProduct = getDao(ExpectedProductDao.class).findByKey(getPropertyBean().getTargetProcessPoint());
		}else {
			 expectedProduct = getDao(ExpectedProductDao.class).findByKey(getProcessPointId());
		}
		if(expectedProduct != null) {
				expectedProduct.setProcessPointId(getProcessPointId());
				getDao(ExpectedProductDao.class).save(expectedProduct);
				lastProcessedProduct = expectedProduct.getProductId();
		}else {
			if (!getPropertyBean().isAllowDataCollection()) {
				throw new TaskException("Could not find last processed product");
			}
		}
	}

	protected void publishDisplayItemsChanged() {

		EventBus.publish(new Event(this,EventType.CHANGED));

	}

	public void expectedProductUpdate() {
		try{
			ExpectedProduct expectedProduct = getDao(ExpectedProductDao.class).findByKey(getPropertyBean().getTargetProcessPoint());
			if (expectedProduct == null || expectedProduct.getProductId().equals(lastProcessedProduct)) { 
				if (!getPropertyBean().isRefreshOnProductChange()) {
					publishDisplayItemsChanged();
				}
			} else {
				saveLastPassedProduct(expectedProduct.getProductId());
			}
		}catch(Exception ex) {
		}
	}

	private void createCheckLastProductTimer() {

		TimerTask timerTask = new TimerTask() {
			public void run() {
				expectedProductUpdate();
			}
		};

		checkLastProductTimer = new Timer();
		int time = getPropertyBean().getPollingLastProductFrequency() * 1000;
		if(time < 1000) time = 1000;
		checkLastProductTimer.scheduleAtFixedRate(timerTask, time, time);

		window.getLogger().info("start to poll the last passed product every " + time / 1000 + "seconds");
	}

	private void saveLastPassedProduct(String productId) {
		ExpectedProduct expectedProduct = new ExpectedProduct();
		expectedProduct.setProcessPointId(getProcessPointId());
		expectedProduct.setProductId(productId);
		expectedProduct.setLastProcessedProduct(lastProcessedProduct);
		getDao(ExpectedProductDao.class).save(expectedProduct);
		lastProcessedProduct = expectedProduct.getProductId();
		window.getLogger().info("updated the last processed product to " + productId);
		publishDisplayItemsChanged();
	}

	/**
	 * notification about last passed product at a process Point
	 * @param event
	 * @param request
	 */
	@EventTopicSubscriber(topic="IProductPassedNotification")
	public void onProductPassedEvent(String event, Request request) {
		try {
			request.invoke(this);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void setAlarm(Boolean alarmFlag) {
		if(!isAlarmConfigured()) return;

		if(alarmFlag)  audioManager.playAlarmSound();
		else audioManager.stopRepeatAlarmSound();
	}

	public int getAlarmIndex(int lastProcessedProductIndex) {
		if(!isAlarmConfigured()) return -1;

		if(getPropertyBean().isAlarmOnOffsetBottom()) return products.size() - 1;
		else if(getPropertyBean().isAlarmOnOffsetCurrent()) return  lastProcessedProductIndex + 1;
		else {
			int offset = getPropertyBean().getAlarmOffset();
			return lastProcessedProductIndex + offset  + 1;
		}
	}

	private boolean isAlarmConfigured() {
		return getPropertyBean().isAlarmOnOffsetBottom()  || 
				getPropertyBean().isAlarmOnOffsetCurrent() ||
				getPropertyBean().getAlarmOffset() > -1000;
	}

	public void execute(String processPointId, String productId) {
		if(!getPropertyBean().getTargetProcessPoint().equals(processPointId) && !isRefreshProcessPoint(processPointId)) return;
		window.getLogger().info("received notification : product " + productId, "passed process point " + processPointId);

		if(isRefreshProcessPoint(processPointId))publishDisplayItemsChanged();
		else saveLastPassedProduct(productId);
	}

	private boolean isRefreshProcessPoint(String processPointId){
		List<String> ppIds = Arrays.asList(getPropertyBean().getRefreshProcessPoints());
		return ppIds.contains(processPointId);
	}

	/**
	 * handler to receive product id from PLC
	 */
	public IDeviceData received(String clientId, IDeviceData deviceData) {
		try {
			if(deviceData instanceof ProductId)
				received(((ProductId)deviceData).getProductId());
			else if(deviceData instanceof ProductIdRefresh) {
				if(((ProductIdRefresh)deviceData).isProductIdRefresh())
					moveToNextExpected();
			} else if(deviceData instanceof ProductIdHighlight) {
				final ProductIdHighlight productIdHighlight = (ProductIdHighlight) deviceData;
				window.getLogger().info("received product id highlight identifier " + productIdHighlight.getIdentifier() + " for product id " + productIdHighlight.getProductId() + " from PLC");
				getLsmWindow().getPanel().highlightProductId(productIdHighlight.getProductId(), productIdHighlight.getIdentifier());
			}
			return DataCollectionComplete.OK();
		} catch (Exception e) {
			window.getLogger().error(e, "Error processing received device data for client id " + clientId + " : " + deviceData);
			return DataCollectionComplete.NG();
		}
	}

	private void received(String productId) {
		window.getLogger().info("received product id " + productId + " from PLC");
		BaseProduct product = findNextProduct(lastProcessedProduct);
		if(product == null || !productId.equalsIgnoreCase(product.getProductId())) {
			window.getLogger().warn("received product id " + productId + 
					" from PLC is different from the expected " + (product == null ? " null " : product.getProductId()));
		}
		saveLastPassedProduct(productId);
	}

	private void moveToNextExpected() {
		window.getLogger().info("received product id refresh signal from PLC");
		if(StringUtils.isEmpty(lastProcessedProduct)) {
			setErrorMessage("Could not update product id from PLC refresh signal because last processed product does not exist");
			return;
		};
		BaseProduct product = findNextProduct(lastProcessedProduct);
		if(product == null) {
			setErrorMessage("Could not update product id from PLC refresh signal because next product does not exist");
			return;
		}

		saveLastPassedProduct(product.getProductId());

	}

	private void setErrorMessage(String errorMsg) {
		window.getLogger().error(errorMsg);
		window.setErrorMessage(errorMsg);
	}

	public String getLastProcessedProduct() {
		return this.lastProcessedProduct;
	}
}
