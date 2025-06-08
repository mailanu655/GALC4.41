package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.DeviceDao;
import com.honda.galc.dao.product.MeasurementSpecDao;
import com.honda.galc.dao.product.PartSpecDao;
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
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.MeasurementSpecId;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PartSpecId;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.ApplicationPropertyBean;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;

/**
 * 
 * <h3>RecipeDownloadAllRulesService</h3>
 * <h3> Class description</h3>
 * <h4> this Service provides Lot Control Rules for a product and a process point. </h4>
 * <p> RecipeDownloadBase description </p>
 * 
 * <h4>Change History</h4>
 * <Table border="1" Cellpadding="3" Cellspacing="0" width="100%">
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH> Input JSON </TH>
 * </TR>
 * <TR>
 * <TD> [ {"com.honda.galc.data.DataContainer" : </TD>
 * <TD>    {  </TD>
 * <TD>     "PROCESS_POINT_ID": "RCP-DWNLD-TST-2", </TD>
 * <TD>     "PRODUCT_ID": "5KBRL6889KB700441", </TD>
 * <TD>     "PRODUCT_TYPE": "FRAME", </TD>
 * <TD>     "CLIENT_ID": "RCP-DWLD" </TD>
 * <TD>   } }  </TD>
]* <TD> ] </TD>
 * <TR bgcolor="#EEEEFF" Class="TableSubHeadingColor">
 * <TH>Update by</TH>
 * <TH>Update date</TH>
 * <TH>Version</TH>
 * <TH>Mark of Update</TH>
 * <TH>Reason</TH>
 * </TR>
 * <TR>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * <TD></TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * 
 * @author vfc91343
 * @date Aug 20, 2018
 * Recipe Download All Rules Service
 */

public class RecipeDownloadAllRulesServiceImpl extends RecipeDownloadServiceImpl implements RecipeDownloadService{

	
	public Device execute(Device device) {
		try{
			init(device);
			prepareSingleProductRecipe(device);
		} catch (Throwable te){
			getLogger().error(te, " Exception to collect data for", this.getClass().getSimpleName());
			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		}
		
		populateReply(device); 
		
		return device;
	}

	public DataContainer execute(DataContainer data) {
		Device device = getDao(DeviceDao.class).findByKey(data.getClientID());
		if(device == null){
			getLogger().error("Can not find the Device info ", this.getClass().getSimpleName());
			data.put(TagNames.DATA_COLLECTION_COMPLETE, LineSideContainerValue.NOT_COMPLETE);
			data.put(TagNames.ERROR_CODE, RecipeErrorCode.Device_NOT_FOUND.getCode());
			return data;
		}
		device.populate(data);
		
		execute(device);
		
		return populateDataToDataContainer(device);
	}
	
	protected void getLotControlRules(BaseProduct baseProduct,String requestPPID, String requestProductSpec ) {
		if(null==baseProduct){
			return;
		}
		
		rulesMap = new LinkedHashMap<String, List<LotControlRule>>();
		
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

		if(rules!=null&&rules.size()!= 0){
			List<Map<String, String>> partInfoMapp = new ArrayList<Map<String, String>>();
			for(LotControlRule r: rules){
				List<PartByProductSpecCode> partByProductSpecCodeList = r.getPartByProductSpecs();
				for(PartByProductSpecCode partByProductSpecCode : partByProductSpecCodeList){
					Map<String, String> partInfoMap = generatePartInfo(r, partByProductSpecCode, requestPPID);
					partInfoMapp.add(partInfoMap);
				}
			}
			dc.put(DataContainerTag.PART_INFO_LIST,partInfoMapp);
		}
		
		for(DeviceFormat format: devise.getReplyDeviceDataFormats()){
			dc.put(format.getTag(), getTagValue(true, format));
		}
		String productSpecCode = ProductTypeUtil.getProductDao(product.getProductType()).findByKey(product.getProductId()).getProductSpecCode();
		dc.put(TagNames.PRODUCT_ID.name(), product.getProductId());
		dc.put(TagNames.PRODUCT_SPEC_CODE, productSpecCode);
		dc.put(TagNames.PROCESS_POINT_ID, requestPPID);
		return dc;
	}
	
	protected Object getTagValue(boolean isString, DeviceFormat format) {
		
		 if(isString)
			 return format.getValue() == null ? "" : format.getValue().toString();
	     else 
	    	 return format.getValue() == null ? "" : format.getValue();
	}
	
	public Map<String, String> generatePartInfo(LotControlRule lotControlRule,PartByProductSpecCode partByProductSpecCode, String requestPPID){
		
		String partId = partByProductSpecCode.getId().getPartId();
		String partName = partByProductSpecCode.getId().getPartName(); 
		
		PartSpecId partSpecId=new PartSpecId();
		partSpecId.setPartName(partName);
		partSpecId.setPartId(partId);
		PartSpec partSpec = getDao(PartSpecDao.class).findByKey(partSpecId);
		
		Map<String, String> partInfoMap = new HashMap<String, String>();
		partInfoMap.put(TagNames.COMMAND.name(), lotControlRule.getInstructionCode());
		partInfoMap.put(TagNames.SCAN.name(), String.valueOf(lotControlRule.getSerialNumberScanFlag()));
		partInfoMap.put(TagNames.UNIQUE.name(), String.valueOf(lotControlRule.getSerialNumberUniqueFlag()));
		if(partSpec!=null){
			partInfoMap.put(TagNames.PART_ID.name(), partId);
			partInfoMap.put(TagNames.PART_NAME.name(), partName);
			partInfoMap.put(TagNames.MEASUREMENT_COUNT.name(), String.valueOf(partSpec.getMeasurementCount()));
			partInfoMap.put(TagNames.SERIAL_NUMBAR_MASK.name(), CommonPartUtility.parsePartMask(partSpec.getPartSerialNumberMask()));
		}
		for(int i=1; i<(partSpec.getMeasurementCount()+1); i++)	{
			MeasurementSpecId measurementSpecId =new MeasurementSpecId();
			measurementSpecId.setPartName(partName);
			measurementSpecId.setPartId(partId);
			measurementSpecId.setMeasurementSeqNum(i);
			MeasurementSpec measurementSpec = getDao(MeasurementSpecDao.class).findByKey(measurementSpecId);
			partInfoMap.put(TagNames.TORQUE_MAX.name()+"_"+(i-1), Double.toString(measurementSpec.getMaximumLimit()));
			partInfoMap.put(TagNames.TORQUE_MIN.name()+"_"+(i-1), Double.toString(measurementSpec.getMinimumLimit()));
		}
		return partInfoMap;
	}
	
	protected void getProduct(Device device) {
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();
		ApplicationPropertyBean bean = PropertyService.getPropertyBean(ApplicationPropertyBean.class, device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString());
		String productType = bean.getProductType();
		product = ProductTypeUtil.findProduct(productType, requestProductId);
		if(product == null){
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);
		}
	}
	
	@Override
	protected void prepareSingleProductRecipe(Device device) {
		try{
			
			getProduct(device);
			
			String requestPPID = device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString();
			String requestProductSpec = ProductTypeUtil.getProductDao(product.getProductType()).findByKey(product.getProductId()).getProductSpecCode();
			
			getLotControlRules(product, requestPPID, requestProductSpec);
			
			if(!hasLotControlRule()){
				getLogger().info("There is no Lot Control Rule defined!");
				addError(RecipeErrorCode.No_Recipe_Data, product.getProductId());
			}

			contextPut(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			contextPut(TagNames.ERROR_CODE.name(),RecipeErrorCode.Recipe_Download_Normal_Reply.getCode());
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
