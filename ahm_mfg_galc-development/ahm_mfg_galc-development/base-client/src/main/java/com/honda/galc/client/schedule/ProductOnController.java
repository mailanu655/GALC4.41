package com.honda.galc.client.schedule;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.client.common.HttpDataContainerInvoker;
import com.honda.galc.client.ui.MessageDialog;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.conf.ApplicationTaskDao;
import com.honda.galc.dao.product.PreProductionLotDao;
import com.honda.galc.data.BuildAttributeTag;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerListener;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.net.HttpServiceProvider;
import com.honda.galc.property.ProductOnHlPropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonUtil;


/**
 * 
 * <h3>ProductOnController Class description</h3>
 * <p> ProductOnController description </p>
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
 * @author Paul Chou<br>
 * May 8, 2014
 *
 *
 */
public class ProductOnController extends ScheduleClientControllerSeq implements DataContainerListener  {
	
	private static final String NO_ERROR = "01";
	ProductOnHlPropertyBean onPropertyBean;
	BuildAttributeCache buildAttributeCache;
	
	
	
	@Autowired
	PreProductionLotDao preProductionLotDao;

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
  		scheduleMainPanel.getMainWindow().getApplicationContext().getRequestDispatcher().registerListner(this);
	}
  	
	public DataContainer received(DataContainer dc) {
		getLogger().info("received from device:" + dc);
		
		return invokeService(prepareData(dc));
	
	}
	
	private void processProductOn(String productId) {
		if(checkExpectedProduct(productId)) {
			DataContainer dc = new DefaultDataContainer();
			dc.put(TagNames.PRODUCT_ID.name(), productId);

			invokeService(prepareData(dc));
			
		} else {
			if(getExpectedProduct() != null) {
				publishDataChanged(getExpectedProduct(), SchedulingEventType.EXPECTED_PRODUCT_CHANGED);
			}
		}
	}
	
	protected boolean checkExpectedProduct(String productId) {
		if(getProperties().isCheckExpectedProductId() && getExpectedProduct() != null && !getExpectedProduct().getProductId().equals(productId)) {
			return MessageDialog.confirm(this.scheduleMainPanel, "The product ID entered is not expected product ID. Do you want to continue?", false);
		}
		return true;
	}

	private DataContainer prepareData(DataContainer dc) {
		dc.put(TagNames.PROCESS_POINT_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.APPLICATION_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.CLIENT_ID.name(), scheduleMainPanel.getProcessPointId());
		dc.put(TagNames.PRODUCTION_LOT.name(), getCurrentOrNextLot());
		return dc;
	}
	
	protected String getCurrentOrNextLot() {
		String prodLot = null;
		List<PreProductionLot> lots = scheduleMainPanel.getCurrentLotPanel().getCurrentLots();
		int i = 0;
		while(prodLot == null && i < lots.size()) {
			PreProductionLot tempLot = lots.get(i++);
			if(tempLot.getStampedCount() < getLotSize(tempLot)) {
				prodLot = tempLot.getProductionLot();
			}
		}
		
		if(prodLot == null) {
			if(scheduleMainPanel.getUpcomingLotPanel().getItems().size() > 0) {
				prodLot = scheduleMainPanel.getUpcomingLotPanel().getItems().get(0).getKeyObject().getProductionLot();
			} else {
				getLogger().error("There is no upcoming lot.");
			}
		}
		return prodLot;
	}

	private synchronized DataContainer invokeService(DataContainer dc)  {
		try {
			String taskName = ServiceFactory.getDao(ApplicationTaskDao.class).findHeadlessTaskName(scheduleMainPanel.getProcessPointId());
			if (StringUtils.isEmpty(taskName))getLogger().warn("Error, failed to find the On service task configuration.");
			String urlStr = HttpServiceProvider.url.replace("HttpServiceHandler", "HttpDeviceHandler");
			DataContainer returnDc = new HttpDataContainerInvoker(urlStr).invoke(dc);
			if(returnDc.get(TagNames.ERROR_CODE.name())!=null && returnDc.get(TagNames.ERROR_CODE.name()).toString().trim().equals(NO_ERROR))
				playOKSound();
			else
				playNGSound();
			getLogger().info("reply to device:" + returnDc);
		} catch (Exception e) {
			getLogger().warn(e, "Exception to invoke service.");
			scheduleMainPanel.getMainWindow().setErrorMessage("Failed to invoke Service.");
		}
		return null;
	}

	private Logger getLogger() {
		return scheduleMainPanel.getLogger();
	}

	protected ProductOnHlPropertyBean getOnPropertyBean(){
		if(onPropertyBean == null)
			onPropertyBean = PropertyService.getPropertyBean(ProductOnHlPropertyBean.class, scheduleMainPanel.getMainWindow().getApplicationContext().getProcessPointId());
		
		return onPropertyBean;
			
	}
	
	public PreProductionLotDao getPreProductionLotDao() {
		if(preProductionLotDao == null)
			preProductionLotDao = ServiceFactory.getDao(PreProductionLotDao.class);
		
		return preProductionLotDao;
	}
	
	public void onEvent(SchedulingEvent event) {
	    
	     switch(event.getEventType()) {
			case PROCESS_PRODUCT :
				scheduleMainPanel.getMainWindow().setMessage("");
				processProductOn(event);
				break;
			default:
	     }
	     
	     super.onEvent(event);
	}

	private void processProductOn(SchedulingEvent event) {
		
		String productId = StringUtils.trimToEmpty((String) event.getTargetObject());
		processProductOn(StringUtils.upperCase(productId));
		
	}
	
	private String[] getSubIds(PreProductionLot preProductionLot)  {
		 String subIdStr = getBuildAttributesCache().findAttributeValue(preProductionLot.getProductSpecCode(),BuildAttributeTag.SUB_IDS,"", getProductType());
		 String[] subIds =new String[]{};
		 if(StringUtils.isEmpty(subIdStr)){
			 logger.info("Invalid Build Attribute SUB_IDS.");
		 }else
			 subIds = subIdStr.split(Delimiter.COMMA);
		return subIds;
	}
	
	private BuildAttributeCache getBuildAttributesCache() {
		if(buildAttributeCache == null){
			buildAttributeCache = new BuildAttributeCache();
			buildAttributeCache.loadAttribute(BuildAttributeTag.SUB_IDS);
	
		}
		return buildAttributeCache;
	}
	
	private int getLotSize(PreProductionLot preProductionLot) {

		if(getProductType() == ProductType.KNUCKLE) {
			return preProductionLot.getLotSize()*2;
		} else if(ProductTypeUtil.isSubProduct(getProductType())){
			String[] subIds = getSubIds(preProductionLot);
			return preProductionLot.getLotSize()*(subIds.length == 0 ? 1 : subIds.length);
		} else if(ProductTypeUtil.isMbpnProduct(getProductType())){
			return StringUtils.isEmpty(getOnPropertyBean().getMbpnProductTypes()) ? preProductionLot.getLotSize() :
				preProductionLot.getLotSize() * CommonUtil.toList(getOnPropertyBean().getMbpnProductTypes()).size();
		} else {
			return preProductionLot.getLotSize();
		}
	}

}

