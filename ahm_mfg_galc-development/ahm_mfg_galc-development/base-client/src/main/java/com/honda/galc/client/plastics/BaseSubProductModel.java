package com.honda.galc.client.plastics;

import static com.honda.galc.service.ServiceFactory.getDao;
import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.SubProductShippingDetailDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.SubId;
import com.honda.galc.entity.product.SubProductShippingDetail;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.property.PropertyService;

/**
 * 
 * 
 * <h3>BaseSubProductModel Class description</h3>
 * <p> BaseSubProductModel description </p>
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
 * Aug 27, 2014
 *
 *
 */
public class BaseSubProductModel {
	
	protected ApplicationContext applicationContext;

	private static final String SUB_IDS ="SUB_IDS";
	private static final String LINE_NUMBER = "LINE_NUMBER";
	private static final String CONTAINER_SIZE = "CONTAINER_SIZE";
	private static final String SOUND_ALARM_ENABLED = "SOUND_ALARM_ENABLED";
	private static final String SHIPPING_PPID_LIST = "SHIPPING_PPID_LIST";

	public BaseSubProductModel(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	public void invokeTracking(String productId) {
		getService(TrackingService.class).track(ProductType.valueOf(getProductType()), productId, applicationContext.getProcessPointId());
	}
	
	public String getProductType() {
		return PropertyService.getProperty(applicationContext.getProcessPointId(),"PRODUCT_TYPE");
	}
	
	public List<String> getSubIdList() {
		return PropertyService.getPropertyList(applicationContext.getProcessPointId(),SUB_IDS);
	}
	
	public List<String> getShippingPPID() {
		return PropertyService.getPropertyList(applicationContext.getProcessPointId(),SHIPPING_PPID_LIST);
	}
	
	
	public List<SubId> getSubIdTypes() {
		List<SubId> subIds = new ArrayList<SubId>();
		for(String name : getSubIdList()) {
			SubId subId = SubId.getType(name);
			if(subId != null) subIds.add(subId);
		}
		return subIds;
	}
	
	public List<String> getSubIds() {
		List<String> subIds = new ArrayList<String>();
		for(String name : getSubIdList()) {
			SubId subId = SubId.getType(name);
			if(subId != null) subIds.add(subId.getId());
		}
		return subIds;
	}
	
	protected List<SubId> getAllSubIds() {
		List<SubId> subIds = new ArrayList<SubId>();
		ProductType type = ProductType.getType(getProductType());
		return type == null ? subIds : type.getSubIds();
	}
	
	protected SubId getSubId(String subId) {
		for(SubId item : getAllSubIds()) {
			if (item.getId().equals(subId)) return item;
		}
		return null;
	}
	
	public boolean isContainerInUse(String containerId) {
		List<SubProductShippingDetail> details = getDao(SubProductShippingDetailDao.class).findAllNotShippedByContainer(getProductType(), containerId);
		return !details.isEmpty();
	}
	
	public List<SubProductShippingDetail> findShippingDetails(String containerId) {
		List<SubProductShippingDetail> details = getDao(SubProductShippingDetailDao.class).findAllNotShippedByContainer(getProductType(), containerId);
		return filterBySubIds(details);
	}
	
	private List<SubProductShippingDetail> filterBySubIds(List<SubProductShippingDetail> details) {
		List<SubProductShippingDetail> results = new ArrayList<SubProductShippingDetail>();
		for(SubProductShippingDetail detail : details) {
			if(isValidLineNumber(detail.getKdLot()) && isValidSubId(detail.getId().getSubId())) results.add(detail);
		}
		//sorting by kdlot / seq no
		Collections.sort(results, new Comparator<SubProductShippingDetail>(){

			public int compare(SubProductShippingDetail o1,	SubProductShippingDetail o2) {
				int compareResult = o1.getId().getKdLotNumber().compareToIgnoreCase(o2.getId().getKdLotNumber());
				if(compareResult != 0) return compareResult;
				return o1.getId().getProductSeqNo() - o2.getId().getProductSeqNo(); 
			}
			
		});
		return results;
	}
	
	private boolean isValidLineNumber(String kdLot){
		return StringUtils.substring(kdLot, 4,6).equalsIgnoreCase(getLineNumber());
	}
	
	private boolean isValidSubId(String subIdStr) {
		for(SubId subId :getSubIdTypes()) {
			if(subId.getId().equalsIgnoreCase(subIdStr)) return true;
		}
		return false;
	}
		
	public String getLineNumber() {
		return PropertyService.getProperty(applicationContext.getProcessPointId(),LINE_NUMBER);
	}
	
	public Integer getContainerSize(){
		return PropertyService.getPropertyInt(applicationContext.getProcessPointId(),CONTAINER_SIZE);
	}
	
	public boolean isSoundAlarmEnabled(){
		return PropertyService.getPropertyBoolean(applicationContext.getProcessPointId(),SOUND_ALARM_ENABLED,false);
	}
	
	public void checkConfigurations() {
		if(StringUtils.isEmpty(getLineNumber()))
			throw new TaskException("Please configure LINE_NUMBER");
		if(getContainerSize()== null || getContainerSize() == 0) 
			throw new TaskException("Please configure CONTAINER_SIZE");
		if(getSubIdList().isEmpty() || getSubIdTypes().isEmpty())
			throw new TaskException("Please configure SUB_IDS properly");
	}
	
}
