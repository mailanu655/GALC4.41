package com.honda.galc.service.engine;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.message.Message;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.property.RepairDecisionProperyBean;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.IoServiceBase;
import com.honda.galc.service.gts.GtsEngineBodyTrackingService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.GtsHttpServiceProvider;
import com.honda.galc.util.ProductCheckUtil;

/**
 * 
 * 
 * <h3>RepairDecisionServiceImpl Class description</h3>
 * <p> RepairDecisionServiceImpl description </p>
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
 * Jul 13, 2015
 *
 *
 */
public class EngineRepairDecisionServiceImpl extends IoServiceBase implements EngineRepairDecisionService{
	
	private static int REPAIR_NOT_REQUIRED = 1;
	private static int REPAIR_REQUIRED = 2;
	
	@Autowired
	EngineDao engineDao;
	
	@Autowired
	TrackingService trackingService;
	
	@Autowired
	GtsEngineBodyTrackingService engineBodyTrackingService;
	
	@Override
	public DataContainer processData() {
		String carrierId=(String) getDevice().getInputValue(TagNames.CARRIER_ID.name());
		String productId=(String) getDevice().getInputValue(TagNames.PRODUCT_ID.name());		
		String locationStatus=(String) getDevice().getInputValue("LOCATION_STATUS");		
		
		Engine engine = engineDao.findByKey(productId);
		if(engine == null) {
			getLogger().error("Invalid Product ID : " + productId);
		}
		
		if(!StringUtils.isEmpty(locationStatus)){
			
			Message message=null;
			
			if(getPropertyBean().useGTSService()) {
				// invoke GTS Server
				message = GtsHttpServiceProvider.getService(GtsEngineBodyTrackingService.class).changeAssociation(carrierId, productId);
			}else {
				message = engineBodyTrackingService.changeAssociation(carrierId, productId);
			}
			if(message != null) {
				getLogger().error("Failed to assocaite product with carrier - " + message);
			}
			return track(engine,locationStatus);
		}else 
			return checkProduct(engine);
		
	}
	
	private DataContainer checkProduct(Engine engine) {
		getLogger().info("Check Product " + engine);
		Map<String,Object> checkResults = ProductCheckUtil.check(engine, getProcessPoint(), getPropertyBean().getProductCheckTypes());
		int repairStatus = REPAIR_REQUIRED;

		if(checkResults == null || checkResults.isEmpty()) {
			getLogger().info("Product Check Passed");
			repairStatus = REPAIR_NOT_REQUIRED;
		}else {
			getLogger().error("Product Check Failed - " + ProductCheckUtil.toErrorString(checkResults));
			repairStatus = REPAIR_REQUIRED;
		}
		getLogger().info("return REPAIR_STATUS = " + 
				(repairStatus == REPAIR_REQUIRED ?  "REPAIR_REQUIRED" : "REPAIR_NOT_REQUIRED")); 
		
		getDevice().setReplyValue("REPAIR_STATUS", repairStatus);
		return getDevice().toReplyDataContainer(false);
	}
	
	private DataContainer track(Engine engine,String locationStatus) {
		String processPointId = getPropertyBean().getLocationProcessPointIdMapping().get(locationStatus);
		
		boolean aFlag = false;
		
		if(StringUtils.isEmpty(processPointId)) {
			getLogger().error("Could not get Process Point ID . Maybe incorrect Locatus Status " + locationStatus);
		}else {
			if(engine != null){
				trackingService.track(engine, processPointId);
				aFlag = true;
			}
		}
		getLogger().info("return DATA COLLECTION COMPLETE  = " + aFlag );
		
		return dataCollectionComplete(aFlag);
	}
	
	
	private RepairDecisionProperyBean getPropertyBean() {
		return PropertyService.getPropertyBean(RepairDecisionProperyBean.class, getProcessPointId());
	}

}
