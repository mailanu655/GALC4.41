package com.honda.galc.service.recipe;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.service.ServiceFactory;
/**
 * 
 * @author vec15809
 *
 */
public class RecipeDownloadFrameFillsServiceImpl extends RecipeDownloadFrameServiceImpl implements RecipeDownloadFrameFillsService{
	private Engine engine;
	
	protected void getNextProduct(Device device) {
		super.getNextProduct(device);
		getEngineFiringFlagForFills();
	}
	
	protected void getCurrentProduct(Device device) {
		super.getCurrentProduct(device);
		getEngineFiringFlagForFills();
	}
	
	private void getEngineFiringFlagForFills() {
		try {
			engine = ServiceFactory.getDao(EngineDao.class).findByKey(((Frame)product).getEngineSerialNo());
			if(engine == null){
				Logger.getLogger().warn("Failed to find Engine for Fills");
			}
			
			/**
			 * if there is no engine associated with VIN then use NOT fired rule
			 */
			short engineFiringFlag = (engine==null)? 0 : engine.getEngineFiringFlag();
			populatePartSerialnumberInfo(engineFiringFlag);
		} catch(Exception e) {
			populatePartSerialnumberInfo((short)0);
			getLogger().warn("Exception to get Engine information:"+ e.getCause());
			
		}

	}

	private void populatePartSerialnumberInfo(short engineFiringFlag) {
		context.put(TagNames.ENGINE_FIRING_FLAG.name(), engineFiringFlag);

		/**
		 * Fill firing flag Part Sn format
		 * <PART SERIAL NUMBER for Not Fired>,<PART SERIAL NUMBER for fired>[, PREFIX]
		 * for example: NOT_FIRED,FIRED  OR NOT_FIRED,FIRED,1
		 */
		String[] split = getFramePropertyBean().getEngineFiringFillPartSn().split(Delimiter.COMMA);
		String currentTag = null;
		if(split.length == 2) {
			currentTag = TagNames.PART_SERIAL_NUMBER.name();
			context.put(currentTag, split[engineFiringFlag]);
			
		}
		if(split.length > 2) {
			currentTag = split[2] + Delimiter.DOT+ TagNames.PART_SERIAL_NUMBER.name();
			context.put(currentTag, split[engineFiringFlag]);
		}
		
		if(!StringUtils.isEmpty(currentTag)) {
			DeviceFormat deviceFormat = device.getDeviceFormat(currentTag);
			if(deviceFormat != null)
				deviceFormat.setValue(split[engineFiringFlag]);
		}
	}
}

	

