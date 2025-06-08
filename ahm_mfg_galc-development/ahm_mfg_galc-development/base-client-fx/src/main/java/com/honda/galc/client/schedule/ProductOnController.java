package com.honda.galc.client.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.honda.galc.checkers.CheckPointsRegistry;
import com.honda.galc.client.ApplicationContext;
import com.honda.galc.client.common.HttpDataContainerInvoker;
import com.honda.galc.client.product.action.ProductionLotSelectedAction;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.conf.BroadcastDestinationDao;
import com.honda.galc.dao.product.FrameDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.BaseScheduleCheckerData;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.BroadcastDestination;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.BroadcastService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.vios.dto.PddaPlatformDto;

import javafx.collections.ObservableList;

/**
 * 
 * <h3>Product On Controller Class description</h3>
 * <p>
 * ProductOnController description
 * </p>
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
 * <TR>
 * <TD>Janak Bhalla & Alok Ghode</TD>
 * <TD>March 05, 2015</TD>
 * <TD>1.0</TD>
 * <TD>GY 20150305</TD>
 * <TD>Initial Release</TD>
 * </TR>
 * </TABLE>
 * 
 */
public class ProductOnController extends ScheduleClientControllerSeq implements
		DataContainerListener {

	ProductOnHlPropertyBean onPropertyBean;
	BuildAttributeCache buildAttributeCache;

	@Autowired
	PreProductionLotDao preProductionLotDao;
	
	@Autowired
	FrameDao frameDao;

	public ProductOnController(ScheduleMainPanel scheduleMainPanel) {
		super(scheduleMainPanel);

		initialize();
	}

	private void initialize() {
		registerListener();

		buildAttributeCache = new BuildAttributeCache();
		buildAttributeCache.loadAttribute(BuildAttributeTag.SUB_IDS);

	}

	private void registerListener() {
		scheduleMainPanel.getMainWindow().getApplicationContext()
		.getRequestDispatcher().registerListner(this);
	}

	public DataContainer received(DataContainer dc) {
		getLogger().info("received from device:" + dc);

		return invokeService(prepareData(dc));

	}

	private void processProductOn(String productId) {

		DataContainer dc = new DefaultDataContainer();
		dc.put(TagNames.PRODUCT_ID.name(), productId);

		invokeService(prepareData(dc));

	}

	private DataContainer prepareData(DataContainer dc) {
		dc.put(TagNames.PROCESS_POINT_ID.name(),
				scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.APPLICATION_ID.name(),
				scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.CLIENT_ID.name(), scheduleMainPanel.getProcessPointId());
		if(getModel().getPddaPlatform() != null) {
			dc.put(TagNames.PDDA_PLATFORM.name(), new Gson().toJson(getModel().getPddaPlatform()));
			getModel().setPddaPlatform(null);
		}
		return dc;
	}

	private synchronized DataContainer invokeService(DataContainer dc) {
		try {
			String taskName = ServiceFactory
					.getDao(ApplicationTaskDao.class)
					.findHeadlessTaskName(scheduleMainPanel.getProcessPointId());
			if (StringUtils.isEmpty(taskName))
				getLogger()
				.warn("Error, failed to find the On service task configuration.");
			String urlStr = HttpServiceProvider.url.replace(
					"HttpServiceHandler", "HttpDeviceHandler");
			DataContainer returnDc = new HttpDataContainerInvoker(urlStr)
					.invoke(dc);

			getLogger().info("reply to device:" + returnDc);
			retrievePreProductionLots();
		} catch (Exception e) {
			getLogger().warn(e, "Exception to invoke service.");
			scheduleMainPanel.getMainWindow().setErrorMessage(
					"Failed to invoke Service.");
		}
		return null;
	}

	private Logger getLogger() {
		return scheduleMainPanel.getLogger();
	}

	protected ProductOnHlPropertyBean getOnPropertyBean() {
		if (onPropertyBean == null)
			onPropertyBean = PropertyService.getPropertyBean(
					ProductOnHlPropertyBean.class, scheduleMainPanel
					.getMainWindow().getApplicationContext()
					.getProcessPointId());

		return onPropertyBean;

	}

	public PreProductionLotDao getPreProductionLotDao() {
		if (preProductionLotDao == null)
			preProductionLotDao = ServiceFactory
			.getDao(PreProductionLotDao.class);

		return preProductionLotDao;
	}
	
	public FrameDao getFrameDao() {
		if (frameDao == null)
			frameDao = ServiceFactory.getDao(FrameDao.class);

		return frameDao;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(SchedulingEvent event) {

		switch (event.getEventType()) {
		case PROCESS_PRODUCT:
			processProductOn(event);
			break;
		case COMPLETE_LOT:
			ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane = (ObjectTablePane<MultiValueObject<PreProductionLot>>) event
			.getTargetObject();
			completeLot(tablePane);
			if (properties.isBroadcastWhenComplete() || properties.isProcessProductOnWhenComplete()) {
				doBroadcast(tablePane.getTable().getItems().get(0)
						.getKeyObject());
			}
			break;
		case REFRESH_SCHEDULE_CLIENT:
			retrievePreProductionLots();
			break;

		case SET_CURRENT_LOT:
			ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane1 = (ObjectTablePane<MultiValueObject<PreProductionLot>>) event
			.getTargetObject();
			ObservableList<Integer> selectedRows = tablePane1.getTable()
					.getSelectionModel().getSelectedIndices();
			MultiValueObject<PreProductionLot> selectedRow = tablePane1
					.getTable().getItems().get(selectedRows.get(0));
			if (processSelectedPreProductionLot(selectedRow)) {

				super.setCurrentLot(tablePane1);
			}
			break;

		case PROCESS_PRODUCT_WITH_PLATFORM:
			Map<String, Object> dc = (Map<String, Object>) event.getTargetObject();
			PddaPlatformDto pddaPlatform = (PddaPlatformDto) dc.get(TagNames.PDDA_PLATFORM.name());
			if(pddaPlatform != null) {
				getModel().setPddaPlatform(pddaPlatform);
			}
			EventBusUtil.publishAndWait(new SchedulingEvent(dc.get(TagNames.PRODUCT_ID.name()), SchedulingEventType.PRODUCT_ID_INPUT));

		default:
			super.onEvent(event);
		}

	}

	private boolean processSelectedPreProductionLot(
			MultiValueObject<PreProductionLot> selectedRow) {

		String currentLot = selectedRow.getKeyObject()
				.getProductionLot();
		String processPoint = scheduleMainPanel.getProcessPointId();
		ProductionLotSelectedAction action = new ProductionLotSelectedAction(this.getScheduleMainPanel());
		if (CheckPointsRegistry.getInstance().isCheckPointConfigured(action)) {
			getLogger().info("At CheckPoint: " + action.getCheckPointName());
			if(!action.executeCheckers(new BaseScheduleCheckerData(currentLot,
					processPoint))){
				return false;
			}
			CheckPointsRegistry.getInstance().unregister(action);
		}
		return true;
	}

	private void doBroadcast(PreProductionLot productionLot) {
		if (null == productionLot) {
			return;
		}
		List<? extends BaseProduct> products = ProductTypeUtil.getProductDao(
				getProductType()).findAllByProductionLot(
						productionLot.getProductionLot());

		List<Frame> frameRecords = new ArrayList<Frame>();

		if (null == products || products.isEmpty()) {
			PreProductionLot prodLot = getPreProductionLotDao().findByKey(productionLot.getProductionLot());

			if (prodLot != null) {
				String kdLotNumber = prodLot.getKdLotNumber();
				if (kdLotNumber != null && !kdLotNumber.isEmpty()) {
					frameRecords = getFrameDao().findAllByKDLotNumber(kdLotNumber);

					if (!frameRecords.isEmpty()) {
						if (frameRecords.size() > 1) {
							getLogger().info("No existing Frame for the given KD Lot number :" + kdLotNumber);
							return;
						}
					}
				}
			}
		}

		if (!products.isEmpty()) {
			broadCast(products);
		} else if (frameRecords != null) {
			broadCast(frameRecords);
		}
	}

	private void processProductOn(SchedulingEvent event) {

		String productId = StringUtils.trimToEmpty((String) event
				.getTargetObject());
		processProductOn(StringUtils.upperCase(productId));

	}
	
	private void broadCast(List<? extends BaseProduct> products) {

		String processPointId = scheduleMainPanel.getProcessPointId();
		BroadcastDestinationDao broadcastDestinationDao = ServiceFactory.getDao(BroadcastDestinationDao.class);
		List<BroadcastDestination> destinations = broadcastDestinationDao.findAllByProcessPointId(processPointId, true);
		BroadcastService broadcastService = ServiceFactory.getService(BroadcastService.class);
		DataContainer dc = new DefaultDataContainer();
		dc.put(DataContainerTag.USER_ID, ApplicationContext.getInstance().getUserId());
		for (BaseProduct product : products) {
			if (properties.isProcessProductOnWhenComplete()) {
				processProductOn(StringUtils.upperCase(product.getProductId()));
			}
			if (properties.isBroadcastWhenComplete()) {
				if (null == destinations || destinations.isEmpty()) {
					continue;
				}
				broadcastService.broadcast(processPointId, product, dc);
			}

		}
	}
}
