package com.honda.galc.service.datacollection;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.Map;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.service.IoService;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ServiceUtil;

/**
 * 
 * <h3>IoServiceBase Class description</h3>
 * <p> IoServiceBase description </p>
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
 * Jan 2, 2013
 *
 *
 */
public abstract class IoServiceBase implements IoService{
	
	protected ThreadLocal<DataContainer> dataCache = new ThreadLocal<DataContainer>();
	
	public Logger getLogger() {
		return  ServiceUtil.getLogger(getProcessPointId()); 
	}
	
	protected void initData(DataContainer data) {
		this.dataCache.set(data);
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		populateData(device,data);
		data.put("DEVICE", device);
	}
	
	protected void populateData(Device device, DataContainer data) {
		device.populate(data);
	}
	
	public Device execute(Device device) {
		return null;
	}
	
	public DataContainer execute(DataContainer data){
		try{
			initData(data);
			getLogger().info("Received Device Data." + data.toString());
			return processData();
		}catch(Exception ex) {
			getLogger().error(ex,"Could not finish processing due to the exception");
			return dataCollectionInComplete();
		}finally{
			// clean up data container cache
			dataCache.remove();
		}
	}
	
	public void asyncAllExecute(DataContainer data) {
		try {
			initData(data);
			getLogger().info("Received Device Data." + data.toString());
			asyncAllProcessData();
		} catch(Exception ex) {
			getLogger().error(ex, "Could not finish processing due to the exception");
		} finally {
			// clean up data container cache
			dataCache.remove();
		}
	}
	
	public void asyncExecute(DataContainer data){
		try{
			initData(data);
			getLogger().info("Received Device Data." + data.toString());
			asyncProcessData();
		}catch(Exception ex) {
			getLogger().error(ex,"Could not finish processing due to the exception");
		}finally{
			// clean up data container cache
			dataCache.remove();
		}
	}
	
	public void asyncAllProcessData() {}
	
	public void asyncProcessData() {}
	
	public DataContainer getData() {
		return dataCache.get();
	}
	
	public Device getDevice() {
		return (Device) getData().get("DEVICE");
	}
	
	public String getProcessPointId() {
		return getDevice().getIoProcessPointId();
	}
	
	public ProcessPoint getProcessPoint() {
		return getDao(ProcessPointDao.class).findByKey(getProcessPointId());
	}
	public DataContainer dataCollectionComplete(boolean aFlag){
		if(aFlag) getDevice().dataCollectionStatusOk();
		else getDevice().dataCollectionStatusNg();
		return getDevice().toReplyDataContainer(true);
	}
	
	public DataContainer dataCollectionInComplete(){
		return dataCollectionComplete(false);
	}
	
	protected void checkContext(Map<Object, Object> context) {
		if(context.containsKey(TagNames.EXCEPTION.name()))
			throw new TaskException((String)context.get(TagNames.EXCEPTION.name()));

		if(context.containsKey(TagNames.MESSAGE.name()))
			getLogger().info((String)context.get(TagNames.MESSAGE.name()));
	}
	
	public String getProductType() {
		return PropertyService.getProperty(getProcessPointId(), "PRODUCT_TYPE" , PropertyService.getProductType());
	}
	
	public String getProperty(String propertyName){
		return PropertyService.getProperty(getProcessPointId(),propertyName);
	}
	
	public String getProperty(String propertyName,String defaultValue){
		return PropertyService.getProperty(getProcessPointId(),propertyName,defaultValue);
	}

	public abstract DataContainer processData();
	

}
