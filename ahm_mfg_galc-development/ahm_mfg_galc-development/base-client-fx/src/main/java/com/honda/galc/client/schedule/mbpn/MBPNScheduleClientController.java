package com.honda.galc.client.schedule.mbpn;

import java.util.Calendar;

import javafx.collections.ObservableList;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import com.honda.galc.client.schedule.DisplayMessageEvent;
import com.honda.galc.client.schedule.ScheduleClientControllerSeq;
import com.honda.galc.client.schedule.ScheduleMainPanel;
import com.honda.galc.client.schedule.SchedulingEvent;
import com.honda.galc.client.schedule.SchedulingEventType;
import com.honda.galc.client.ui.EventBusUtil;
import com.honda.galc.client.ui.component.MultiValueObject;
import com.honda.galc.client.ui.component.ObjectTablePane;
import com.honda.galc.dao.conf.ComponentPropertyDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.dto.rest.ProductTrackDTO;
import com.honda.galc.entity.enumtype.PreProductionLotSendStatus;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.tracking.MbpnProductHelper;
import com.honda.galc.service.tracking.MbpnProductProcessor;
import com.honda.galc.util.ReflectionUtils;

public class MBPNScheduleClientController extends ScheduleClientControllerSeq {
	public static String COMPONENT_ID_FOR_PLANT_CODE_MAPPING = "PLANT_CODE_MAPPING";
	private volatile MbpnProductProcessor<?> productProcessor;
	private ScheduleClientPropertyBean property;
	private PreProductionLotDao preProductionLotDao;
	private ComponentPropertyDao componentPropertyDao;
	private String plantLocCode;
	private String deptCode;

	public MBPNScheduleClientController(ScheduleMainPanel panel) {
		super(panel);
		String ppid = scheduleMainPanel.getApplicationId();

		property = PropertyService.getPropertyBean(ScheduleClientPropertyBean.class, ppid);
		try {
			plantLocCode = getComponentPropertyDao().findValueForCompIdAndKey(COMPONENT_ID_FOR_PLANT_CODE_MAPPING, property.getPlantCode());
			deptCode = property.getDepartmentCode();
			String className = property.getProductProcessor();
			@SuppressWarnings("unchecked")
			Class<? extends MbpnProductProcessor<?>> clazz = (Class<? extends MbpnProductProcessor<?>>) Class.forName(className);
			// Here we only support Plastics and Paint product processor.
			if (PaintMbpnProductProcessor.class.isAssignableFrom(clazz)) {
				productProcessor = ReflectionUtils.createInstance(clazz, ppid, plantLocCode, deptCode);
			} else if (PlasticsMbpnProductProcessor.class.isAssignableFrom(clazz)) {
				productProcessor = ReflectionUtils.createInstance(clazz, scheduleMainPanel.getMainWindow().getApplicationContext());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Unable to start MbpnProductProcessor");
		}
	}

	private void processProduct(SchedulingEvent event) {
		String productId = StringUtils.trimToEmpty((String) event.getTargetObject());
		processProduct(productId);
		retrievePreProductionLots();
	}

	public MbpnProductProcessor<?> getProductProcessor() {
		return productProcessor;
	}

	private boolean processProduct(String productId) {
		try {
			EventBusUtil.publish(new DisplayMessageEvent(""));
			MbpnProductProcessor<?> productProcessor = getProductProcessor();
			// Here we only support Plastics and Paint product processor.
			if (productProcessor instanceof PlasticsMbpnProductProcessor) {
				return ((PlasticsMbpnProductProcessor) productProcessor).processItem(productId);
			} else if (productProcessor instanceof PaintMbpnProductProcessor) {
				PreProductionLot currentPreProductionLot = getCurrentOrder();
				ProductTrackDTO trackDTO = new ProductTrackDTO();
				trackDTO.setOrderNo(currentPreProductionLot.getProductionLot());
				trackDTO.setProductId(productId);
				trackDTO.setProductSpecCode(currentPreProductionLot.getProductSpecCode());
				return ((PaintMbpnProductProcessor) productProcessor).processItem(trackDTO);
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Could not handle PROCESS_PRODUCT event " + productId);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(SchedulingEvent event) {
		ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane;
		switch (event.getEventType()) {
		case PROCESS_PRODUCT:
			processProduct(event);
			break;
		case GENERATE_SN:
			generateSN();
			break;
		case ORDER_COMPLETED:
			tablePane = (ObjectTablePane<MultiValueObject<PreProductionLot>>) event.getTargetObject();
			completeOrder(tablePane);
			break;
		default:
			super.onEvent(event);
		}

	}

	private void changeCurrentOrder(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		updateOrderStatus(getCurrentOrder(), PreProductionLotSendStatus.WAITING);
		PreProductionLot newCurrentOrder = tablePane.getTable().getItems().get(0).getKeyObject();
		updateOrderStatus(newCurrentOrder, PreProductionLotSendStatus.INPROGRESS);
		retrievePreProductionLots();
	}

	private void completeOrder(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if (property.getProcessProductWhenClickCompleteMenu()) {
			String productId = null;
			if (StringUtils.isBlank(getCurrentProductId())) {
				productId = generateSN();
			} else {
				productId = getCurrentProductId();
			}
			processProduct(productId);
		}
		PreProductionLot order = tablePane.getTable().getItems().get(0).getKeyObject();
		updateOrderStatus(order, PreProductionLotSendStatus.DONE);
		retrievePreProductionLots();
	}

	private void updateOrderStatus(PreProductionLot order, PreProductionLotSendStatus status) {
		order.setSendStatus(status);
		getPreProductionLotDao().save(order);
	}

	public PreProductionLot getCurrentOrder() {
		return MbpnProductHelper.getCurrentPreProductionLot(scheduleMainPanel.getApplicationId());
	}

	public boolean canEnableMenuItem(String menuItem, ObjectTablePane<?> tablePane) {
		SchedulingEventType eventType = SchedulingEventType.getType(menuItem);
		boolean canEnable = true;
		switch (eventType) {
		case GENERATE_SN:
			canEnable = canGenerateSN(tablePane);
			break;
		case ORDER_COMPLETED:
			canEnable = canCompleteOrder((ObjectTablePane<MultiValueObject<PreProductionLot>>)tablePane);
			break;
		default:
			canEnable = super.canEnableMenuItem(menuItem, tablePane);
			break;
		}
		return canEnable;
	}

	protected boolean canGenerateSN(ObjectTablePane<?> tablePane) {
		if (tablePane == null) {
			return false;
		}
		ObservableList<Integer> rows = tablePane.getTable().getSelectionModel().getSelectedIndices();
		if (rows.size() == 0 || rows.size() > 1) {
			// cannot generate SN if no row or multiple rows are selected
			return false;
		}
		Object selectedItem = tablePane.getSelectedItems().get(0);
		if (selectedItem instanceof MultiValueObject<?>) {
			MultiValueObject<?> mvo = (MultiValueObject<?>) selectedItem;
			if (mvo.getKeyObject() instanceof PreProductionLot) {
				PreProductionLot ppl = (PreProductionLot) mvo.getKeyObject();
				String productionLot = ppl.getProductionLot();
				if (isCurrentProductionLot(productionLot)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean canCompleteOrder(ObjectTablePane<MultiValueObject<PreProductionLot>> tablePane) {
		if (tablePane == null) {
			return false;
		}
		PreProductionLot order = tablePane.getTable().getItems().get(0).getKeyObject();
		return null!=order&&order.getLotSize()==1;
	}

	protected boolean isCurrentProductionLot(String productionLot) {
		try {
			PreProductionLot currentPreProductionLot = getCurrentOrder();
			if (currentPreProductionLot != null && productionLot != null && productionLot.equals(currentPreProductionLot.getProductionLot())) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e, "Cannot check if the production lot is the current one.");
		}
		return false;
	}

	protected String generateSN() {
		StringBuilder sb = new StringBuilder(plantLocCode);
		sb.append(deptCode);
		sb.append(DateFormatUtils.format(Calendar.getInstance(), "yyyyMMddHHmmss"));
		String productId = sb.toString();
		setCurrentProductId(productId);
		return productId;
	}

	public PreProductionLotDao getPreProductionLotDao() {
		if (null == preProductionLotDao) {
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		}
		return preProductionLotDao;
	}

	public ComponentPropertyDao getComponentPropertyDao() {
		if (null == componentPropertyDao) {
			componentPropertyDao = ServiceFactory.getDao(ComponentPropertyDao.class);
		}
		return componentPropertyDao;
	}

}
