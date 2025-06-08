package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.PartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Part;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

public class RecipeDownloadServiceRockwellImpl extends RecipeDownloadServiceImpl implements RecipeDownloadService{

	@Override
	public Device execute(Device device) {
		getLogger().error("This method can't be used. Please call execute(DataContainer data) ");
		return device;
	}

	@Override
	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		if(device == null){
			getLogger().error("Can not find the Device info ", this.getClass().getSimpleName());
			data.put(TagNames.DATA_COLLECTION_COMPLETE, LineSideContainerValue.NOT_COMPLETE);
			data.put(TagNames.ERROR_CODE, RecipeErrorCode.Rockwell_Device_NOT_FOUND.getCode());
			return data;
		}
		device.populate(data);
		try{
			init(device);
			prepareSingleProductRecipe(device);
		} catch (Throwable te){
			getLogger().error(te, " Exception to collect data for ", this.getClass().getSimpleName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}
		
		populateReply(device); 
		
		return populateDataToDataContainer(device);
	}
	
	//check request ProductSpec matches the value from db
	private void checkMTOC(Device device){
		String requestProductSpec = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_SPEC_CODE.name())).getValue().toString();
		if(StringUtils.isEmpty(requestProductSpec) || !requestProductSpec.equals(product.getProductSpecCode())){
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		}
	}
	
	//check request LineNo matches the value from db
	private void checkLineID(Device device){
		Object value = device.getDeviceFormat(getFullTag(TagNames.LINE_NUMBER.name())).getValue();
		String requestLineNo = value==null?"":value.toString().trim();
		if(StringUtils.isBlank(requestLineNo) || "0".equals(requestLineNo)){
			//Line Id is empty, missing or "0". ignore the line id validation.
			return;
		}
		if(product != null){
			if(!requestLineNo.equals(product.getTrackingStatus())){
				addError(RecipeErrorCode.Invalid_Ref, requestProductId);
			}
		}else{
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		}
	}
	
	protected void getLotControlRules(BaseProduct baseProduct,String requestPPID,String requestProductSpec ) {
		if(null==baseProduct){
			return;
		}
		
		rulesMap = new LinkedHashMap<String, List<LotControlRule>>();
		
		 
		// @RGALCDEV-1628
		// Use service method that has its own transactional context so
		// we don't hang on to data source
		boolean isMbpnProduct = ProductTypeUtil.isMbpnProduct(product.getProductType());		
		if (isMbpnProduct){
			productSpec = (Mbpn)ProductTypeUtil.getProductSpecDao(baseProduct.getProductType()).findByProductSpecCode_NoTxn(requestProductSpec, baseProduct.getProductType().name());  
		}else{
			productSpec = (ProductSpec)ProductTypeUtil.getProductSpecDao(baseProduct.getProductType()).findByProductSpecCode_NoTxn(requestProductSpec, baseProduct.getProductType().name());
		}
		requestPPID = StringUtils.trim(requestPPID);
		List<LotControlRule> rules = LotControlRuleCache.getOrLoadLotControlRule(productSpec, StringUtils.trim(requestPPID));
		
		if(!StringUtils.isEmpty(baseProduct.getSubId()))
			rules = filterLotControlRules(rules, baseProduct.getSubId());
		
		rulesMap.put(requestPPID, rules);
	}

	
	protected DataContainer  populateDataToDataContainer(Device devise){
		
		DataContainer dc = new DefaultDataContainer();
		String requestPPID = devise.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString();
		List<LotControlRule> rules = rulesMap.get(requestPPID);

		if(rules!=null&&rules.size()!=0){
			List<PartInfo> partInfoList = new ArrayList<PartInfo>();
			for(LotControlRule r: rules){
				List<PartByProductSpecCode> findByProductSpec = r.getPartByProductSpecs();
				for(PartByProductSpecCode partByProductSpecCode :findByProductSpec){
					
					partInfoList.add(generatePartInfo(r,partByProductSpecCode));
				}
			}
			dc.put(DataContainerTag.PART_INFO_LIST,partInfoList);
		}
		
		for(DeviceFormat format: devise.getReplyDeviceDataFormats()){
			dc.put(format.getTag(), getTagValue(true, format));
		}
		
		String MTOC = devise.getDeviceFormat(getFullTag(TagNames.PRODUCT_SPEC_CODE.name())).getValue().toString();
		dc.put(TagNames.CONDITION_CHARACTERS.name(),MTOC);
		return dc;
	}
	
	protected Object getTagValue(boolean isString, DeviceFormat format) {
		
		 if(isString)
			 return format.getValue() == null ? "" : format.getValue().toString();
	     else 
	    	 return format.getValue() == null ? "" : format.getValue();
	}
	
	public PartInfo generatePartInfo(LotControlRule lotControlRule ,PartByProductSpecCode partByProductSpecCode){
		
		PartInfo partInfo = new PartInfo();
		String partId = partByProductSpecCode.getId().getPartId();
		String partName = partByProductSpecCode.getId().getPartName();
		PartId id = new PartId();
		id.setPartId(partId);
		id.setPartName(partName);
		
		Part findByKey = getDao(PartDao.class).findByKey(id);
		partInfo.setCommand(lotControlRule.getInstructionCode());
		partInfo.setScan(String.valueOf(lotControlRule.getSerialNumberScanFlag()));
		partInfo.setUnique(String.valueOf(lotControlRule.getSerialNumberUniqueFlag()));
		
		if(findByKey !=null ){
			partInfo.setPartId(partId);
			partInfo.setPartName(partName);
			partInfo.setMeasurementCount(String.valueOf(findByKey.getMeasurementCount()));
			partInfo.setSerialNumberMask(CommonPartUtility.parsePartMask(findByKey.getPartSerialNumberMask()));
			partInfo.setTorgueMax0(String.valueOf(findByKey.getMaximumValue1()));
			partInfo.setTorgueMax1(String.valueOf(findByKey.getMaximumValue2()));
			partInfo.setTorgueMax2(String.valueOf(findByKey.getMaximumValue3()));
			partInfo.setTorgueMax3(String.valueOf(findByKey.getMaximumValue4()));
			partInfo.setTorgueMax4(String.valueOf(findByKey.getMaximumValue5()));
			partInfo.setTorgueMax5(String.valueOf(findByKey.getMaximumValue6()));
			partInfo.setTorgueMax6(String.valueOf(findByKey.getMaximumValue7()));
			partInfo.setTorgueMax7(String.valueOf(findByKey.getMaximumValue8()));
			partInfo.setTorgueMax8(String.valueOf(findByKey.getMaximumValue9()));
			partInfo.setTorgueMax9(String.valueOf(findByKey.getMaximumValue10()));
			
			partInfo.setTorgueMin0(String.valueOf(findByKey.getMinimumValue1()));
			partInfo.setTorgueMin1(String.valueOf(findByKey.getMinimumValue2()));
			partInfo.setTorgueMin2(String.valueOf(findByKey.getMinimumValue3()));
			partInfo.setTorgueMin3(String.valueOf(findByKey.getMinimumValue4()));
			partInfo.setTorgueMin4(String.valueOf(findByKey.getMinimumValue5()));
			partInfo.setTorgueMin5(String.valueOf(findByKey.getMinimumValue6()));
			partInfo.setTorgueMin6(String.valueOf(findByKey.getMinimumValue7()));
			partInfo.setTorgueMin7(String.valueOf(findByKey.getMinimumValue8()));
			partInfo.setTorgueMin8(String.valueOf(findByKey.getMinimumValue9()));
			partInfo.setTorgueMin9(String.valueOf(findByKey.getMinimumValue10()));
			
			partInfo.setSerialNumberMask(findByKey.getPartSerialNumberMask());
		}
		
		return partInfo;
	}
	
	protected void getProduct(Device device) {
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();
		ApplicationPropertyBean bean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString());
		String productName = bean.getProductType();
		// @RGALCDEV-1628
		// Use service method that has its own transactional context so
		// we don't hang on to data source
		product = ProductTypeUtil.findProduct(productName, requestProductId);
		if(product == null){
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		}
	}
	
	@Override
	protected void prepareSingleProductRecipe(Device device) {
		try{
			
			getProduct(device);
			
			//check requestMTOC equals request Product's MTOC
			checkMTOC(device);
			
			//check lineID
			checkLineID(device);
			
			String requestPPID = device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString();
			String requestProductSpec = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_SPEC_CODE.name())).getValue().toString();
			getLotControlRules(product, requestPPID,requestProductSpec);
			
			if(!hasLotControlRule()){
				getLogger().info("There is no Lot Control Rule defined!");
				addError(RecipeErrorCode.No_Recipe_Data, product.getProductId());
			}

			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			contextPut(TagNames.ERROR_CODE.name(),RecipeErrorCode.Rockwell_Normal_Reply.getCode());
			if(NO_ERROR.equals(context.get(TagNames.ERROR_CODE.name())))
				getLogger().info("Ref#", getRequestProductId(), " - Next Ref# OK.");
			
		}catch(TaskException te){
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(te, te.getMessage(), " ",this.getClass().getSimpleName());
		}catch (Exception e){
			contextPut(TagNames.ERROR_CODE.name(), RecipeErrorCode.System_Err.getCode());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(e, RecipeErrorCode.System_Err.getDescription() + getRequestProductId(), " ", this.getClass().getSimpleName());
		}
	}

	
	
}
