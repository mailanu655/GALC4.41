/**
 * 
 */
package com.honda.galc.client.schedule.mbpn;

import java.util.Calendar;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.audio.ClientAudioManager;
import com.honda.galc.client.property.AudioPropertyBean;
import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.client.schedule.ScheduleClient3ListPanel;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.schedule.property.ScheduleClientPropertyBean;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.common.message.MessageType;
import com.honda.galc.dao.conf.ApplicationDao;
import com.honda.galc.dao.conf.TerminalDao;
import com.honda.galc.dao.product.MbpnProductDao;
import com.honda.galc.dao.product.OrderDao;
import com.honda.galc.dao.product.ProductPriorityPlanDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.DeviceDataConverter;
import com.honda.galc.device.dataformat.ProductId;
import com.honda.galc.entity.enumtype.OrderStatus;
import com.honda.galc.entity.enumtype.PlanStatus;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.Order;
import com.honda.galc.entity.product.ProductPriorityPlan;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.tracking.MbpnProductHelper;
import com.honda.galc.service.tracking.MbpnProductProcessor;
import com.honda.galc.util.ReflectionUtils;

/**
 * @author Subu Kathiresan
 * @date Jan 17, 2013
 */
public class ScheduleClientController implements EventSubscriber<SchedulingEvent>, DataContainerListener {

	private ApplicationDao _applicationDao;
	private TerminalDao _terminalDao;
	private OrderDao _orderDao;
	private ProductPriorityPlanDao _productPriorityPlanDao;
	private MbpnProductDao _mbpnProductDao;
	private Logger _logger;
	private ApplicationContext _appContext;
	private volatile MbpnProductProcessor<?> _productProcessor;
	// private volatile PlasticsMbpnProductProcessor _productProcessor;
	private ScheduleClient3ListPanel _scheduleListPanel;
	private DeviceDataConverter _deviceDataConverter;
	private ScheduleClientPropertyBean property;
	private  ClientAudioManager audioManager=null;

	public ScheduleClientController(ScheduleClient3ListPanel listPanel, ApplicationContext appContext) {
		_appContext = appContext;
		_scheduleListPanel = listPanel;
		setProperty(PropertyService.getPropertyBean(ScheduleClientPropertyBean.class, appContext.getProcessPointId()));
		if(getProperty().isSoundsEnabled())
			audioManager = new ClientAudioManager(PropertyService.getPropertyBean(AudioPropertyBean.class,appContext.getProcessPointId()));
		try {
			String className = getProperty().getProductProcessor();
			Class<? extends MbpnProductProcessor> clazz = (Class<? extends MbpnProductProcessor>) Class.forName(className);
			// Here we only support Plastics and Paint product processor.
			if (PaintMbpnProductProcessor.class.isAssignableFrom(clazz)) {
				setProductProcessor(ReflectionUtils.createInstance(clazz, appContext.getProcessPointId()));
			} else if (PlasticsMbpnProductProcessor.class.isAssignableFrom(clazz)) {
				setProductProcessor(ReflectionUtils.createInstance(clazz, appContext));
			}
			registerListener();
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Unable to start PlasticsMbpnProductProcessor");
			playNGSound();
		}
		EventBus.subscribe(SchedulingEvent.class, this);
	}

	public void onEvent(SchedulingEvent event) {
		if (event.getEventType() == SchedulingEventType.PROCESS_PRODUCT) {
			processProduct(event);
		} else if (event.getEventType() == SchedulingEventType.CURRENT_ORDER_CHANGED) {
			changeCurrentOrder(event);
		} else if (event.getEventType() == SchedulingEventType.ORDER_COMPLETED) {
			completeOrder(event);
		} else if (event.getEventType() == SchedulingEventType.GENERATE_SN) {
			generateSN(event);
		}
		getScheduleListPanel().getExpectedProductPanel().getProductIdTextField().requestFocus();
	}

	private void processProduct(SchedulingEvent event) {
		String productId = StringUtils.trimToEmpty((String) event.getTargetObject());
		String errMsg = null;
		if (StringUtils.isBlank(productId)) {
			errMsg = "Scanned product is blank or null";
			EventBus.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
		} else if (isProductAlreadyAssigned(productId)) {
			errMsg = "Duplicate product scanned";
			EventBus.publish(new DisplayMessageEvent(errMsg, MessageType.ERROR));
		} else {
			if(processProduct(productId))
				playOKSound();
			EventBus.publish(new SchedulingEvent("",
					SchedulingEventType.UPDATE_VIEW));
		}
	}

	protected boolean isProductAlreadyAssigned(String productId) {
		try {
			MbpnProduct mbpnProduct = getMbpnProductDao().findByKey(productId);
			if (mbpnProduct != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().info("Could not check if MbpnProduct " + productId + " was already created");
			playNGSound();
			return false;
		}
		return false;
	}
	
	public MbpnProductDao getMbpnProductDao() {
		if (_mbpnProductDao == null){
			_mbpnProductDao = ServiceFactory.getDao(MbpnProductDao.class);
		}
		return _mbpnProductDao;
	}
	
	private boolean processProduct(String productId) {
		try {
			EventBus.publish(new DisplayMessageEvent(""));
			MbpnProductProcessor<?> productProcessor = getProductProcessor();
			// Here we only support Plastics and Paint product processor.
			if (productProcessor instanceof PlasticsMbpnProductProcessor) {
				return ((PlasticsMbpnProductProcessor) getProductProcessor()).processItem(productId);
			} else if (productProcessor instanceof PaintMbpnProductProcessor) {
				return ((PaintMbpnProductProcessor) getProductProcessor()).processItem(productId);
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			getLogger().error("Could not handle PROCESS_PRODUCT event " + productId);
			playNGSound();
			return false;
		}
	}

	private void changeCurrentOrder(SchedulingEvent event) {
		// set current order status back to Scheduled
		updateOrderStatus(getCurrentOrder(), OrderStatus.SCHEDULED);

		// set new order status to current
		Order newCurrentOrder = (Order) event.getTargetObject();
		updateOrderStatus(newCurrentOrder, OrderStatus.CURRENT);
		// moveProductsToTop(newCurrentOrder);

		EventBus.publish(new SchedulingEvent("", SchedulingEventType.UPDATE_VIEW));
		playOrderChangedSound();
	}

	private void moveProductsToTop(Order currOrder) {
		List<ProductPriorityPlan> pppList = getProductPriorityPlanDao().getProductsByOrderNumber(currOrder.getId().getOrderNo());

	}

	private void completeOrder(SchedulingEvent event) {
		// set selected order's status to Assigned
		Order order = (Order) event.getTargetObject();
		updateOrderStatus(order, OrderStatus.ASSIGNED);

		List<ProductPriorityPlan> pppList = getProductPriorityPlanDao().getProductsByOrderNumber(order.getId().getOrderNo());
		for (ProductPriorityPlan ppp : pppList) {
			if (ppp.getPlanStatus() == PlanStatus.SCHEDULED) {
				ppp.setPlanStatus(PlanStatus.ASSIGNED);
				getProductPriorityPlanDao().save(ppp);
				getLogger().error("Updated status to  " + PlanStatus.ASSIGNED.toString() + " product " + ppp.getProductId());
			}
		}

		EventBus.publish(new SchedulingEvent("", SchedulingEventType.UPDATE_VIEW));
		playOrderChangedSound();
	}

	private void generateSN(SchedulingEvent event) {
		final StringBuilder sb = new StringBuilder("SP");
		sb.append(DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss"));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getScheduleListPanel().getExpectedProductPanel().getProductIdTextField().setText(sb.toString());
			}
		});
	}

	private void updateOrderStatus(Order order, OrderStatus status) {
		order.setOrderStatusId(status.getId());
		getOrderDao().save(order);
		getLogger().info("Updated Order status for Order " + getOrderNumber(order) + " to " + status);
	}

	public Order getCurrentOrder() {
		return MbpnProductHelper.getCurrentOrder(getAppContext().getProcessPointId());
		// return ServiceFactory.getDao(OrderDao.class).getCurrentOrder();
	}

	private String getOrderNumber(Order currentOrder) {
		return StringUtils.trimToEmpty(currentOrder.getId().getOrderNo());
	}

	public ApplicationDao getApplicationDao() {
		return _applicationDao;
	}

	public void setApplicationDao(ApplicationDao applicationDao) {
		_applicationDao = applicationDao;
	}

	public TerminalDao getTerminalDao() {
		return _terminalDao;
	}

	public void setTerminalDao(TerminalDao terminalDao) {
		_terminalDao = terminalDao;
	}

	public OrderDao getOrderDao() {
		if (_orderDao == null)
			_orderDao = ServiceFactory.getDao(OrderDao.class);

		return _orderDao;
	}

	public ProductPriorityPlanDao getProductPriorityPlanDao() {
		if (_productPriorityPlanDao == null)
			_productPriorityPlanDao = ServiceFactory.getDao(ProductPriorityPlanDao.class);

		return _productPriorityPlanDao;
	}

	protected ApplicationContext getAppContext() {
		return _appContext;
	}

	public MbpnProductProcessor<?> getProductProcessor() {
		return _productProcessor;
	}

	public void setProductProcessor(MbpnProductProcessor<?> processor) {
		_productProcessor = processor;
	}

	private ScheduleClient3ListPanel getScheduleListPanel() {
		return _scheduleListPanel;
	}

	protected Logger getLogger() {
		if (_logger == null) {
			_logger = Logger.getLogger(getAppContext().getTerminalId() + "_" + getAppContext().getApplicationId());
			_logger.getLogContext().setApplicationInfoNeeded(true);
			_logger.getLogContext().setMultipleLine(false);
			_logger.getLogContext().setCenterLog(false);
		}
		_logger.getLogContext().setThreadName(getAppContext().getApplicationId() + "-" + Thread.currentThread().getName());
		return _logger;
	}

	private void registerListener() {
		getAppContext().getRequestDispatcher().registerListner(this);
	}

	public DataContainer received(DataContainer dc) {
		getLogger().info("received from device:" + dc);
		ProductId productId = (ProductId) getDeviceDataConverter().convert(dc);
		boolean result = processProduct(StringUtils.trimToEmpty(productId.getProductId()));
		dc.put(TagNames.DATA_COLLECTION_COMPLETE.name(), result ? "1" : "0");
		getLogger().info("reply to device:" + dc);
		return dc;
	}

	private DeviceDataConverter getDeviceDataConverter() {
		if (_deviceDataConverter == null) {
			_deviceDataConverter = new DeviceDataConverter(getAppContext().getProcessPointId());
			_deviceDataConverter.registerDeviceData(new ProductId());
		}
		return _deviceDataConverter;
	}

	public ScheduleClientPropertyBean getProperty() {
		return property;
	}

	public void setProperty(ScheduleClientPropertyBean property) {
		this.property = property;
	}
	
	private ClientAudioManager getAudioManager() 
	{ 
		 return audioManager; 
     }
	
	public void playNGSound()
	{
		if(property.isSoundsEnabled())
			getAudioManager().playNGSound();
	}
	
	public void playOKSound()
	{
		if(property.isSoundsEnabled())
			getAudioManager().playOKSound();
	}
	
	public void playWarnSound()
	{
		if(property.isSoundsEnabled())
			getAudioManager().playWarnSound();
	}
	
	public void playOrderChangedSound()
	{
		if(property.isSoundsEnabled())
			getAudioManager().playModelChangedSound();
	}
	

}
