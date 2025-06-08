package com.honda.galc.service.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.dao.product.RequiredPartDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.LotControlRuleInfo;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.BasePartResult;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.PartByProductSpecCode;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.entity.product.RequiredPart;
import com.honda.galc.property.RecipeDownloadPDAServicePropertyBean;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.DataCollectionService;
import com.honda.galc.service.RecipeDownloadPDAService;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonUtil;
import com.honda.galc.util.LotControlPartUtil;
/**
 * 
 * <h3>RecipeDownloadPDAServiceImpl</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadPDAServiceImpl description </p>
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
 * <TD>Jackie</TD>
 * <TD>May 30, 2014</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR> 
 * <TR>
 * <TD>Kamlesh Maharjan</TD>
 * <TD>March 3rd, 2016</TD>
 * <TD>0.2</TD>
 * <TD>none</TD>
 * <TD>Acceptable for All Product</TD> 
 * </TR> 
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Jackie
 * @since May 30, 2014
 */
public class RecipeDownloadPDAServiceImpl extends RecipeDownloadServiceRockwellImpl implements RecipeDownloadPDAService {
	
	protected void getMeasurementsForInstalledParts(BaseProduct product,List<ProductBuildResult> installedPartList) {
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		List<Measurement> allMeasurements = measurementDao.findAllByProductId(product.getProductId());
		for(ProductBuildResult part : installedPartList){
			List<Measurement> measuremnts = new ArrayList<Measurement>();
			for(Measurement measurement : allMeasurements){
				if(((InstalledPart) part).getId().getPartName().equals(measurement.getId().getPartName()))
					measuremnts.add(measurement);
			}
			((InstalledPart) part).setMeasurements(measuremnts);
		}
		
	}
	
	protected void assembleLotControl(BaseProduct product, BaseProductSpec productSpec, List<Object[]> rulesAndProcessPoints,  String lineId, List<BasePartResult> partResults) {
		for(Object[] objects : rulesAndProcessPoints){
			LotControlRule rule = (LotControlRule)objects[0];
			ProcessPoint pp = (ProcessPoint)objects[1];
			if(!StringUtils.isEmpty(rule.getSubId()) && !rule.getSubId().equals(product.getSubId())) continue;
			if(StringUtils.isNotBlank(lineId) && "0".equals(lineId.trim()) == false && lineId.compareToIgnoreCase(pp.getLineId())!=0){
				//filter out line id
				continue;
			}
			
			BasePartResult existingResult = null;
			for(BasePartResult result : partResults){
				if(result.getProcessPointId().equals(rule.getId().getProcessPointId()) &&
						result.getPartName().equals(rule.getId().getPartName())){
					existingResult = result;
						break;
					}
			}
			if(existingResult == null)
				partResults.add(new BasePartResult(rule, pp));
			else 
				existingResult.add(rule);
			
		}
		
		for(BasePartResult partResult : partResults){
			List<LotControlRule> theMostMatchedRules = LotControlPartUtil.getLotControlRuleByProductSpec(productSpec, partResult.getLotControlRules());
			LotControlPartUtil.sortRulesBySequence(theMostMatchedRules);
			if(theMostMatchedRules.size() > 0 ){
				partResult.setLotControlRule(theMostMatchedRules.get(0));
			}
		}
	}
	
	private String getExcludedToSaveParts() {
		return PropertyService.getProperty("Default_LotControl", "EXCLUDE_PARTS_TO_SAVE");
	}
	

	public DataContainer getNGPart(DefaultDataContainer data) {
		DataContainer dc = new DefaultDataContainer();
		SystemPropertyBean propertyBean = PropertyService.getPropertyBean(SystemPropertyBean.class);
		List<BasePartResult> partResults = new ArrayList<BasePartResult>();
		
		String productId = data.getString(TagNames.PRODUCT_ID.name());
		String lineId = data.getString(TagNames.LINE_NUMBER.name());
		String filterProcessPoints=data.getString(TagNames.FILTER_BY_PROCESS_POINTS.name());
		String requiredParts = getRequiredPartLists(filterProcessPoints);
		ProductType productType = ProductType.getType(data.getString(TagNames.PRODUCT_TYPE.name()));
		if(productType==null) productType = ProductType.getType(propertyBean.getProductType());
		if(productType==null){
			getLogger().error("Invalid product Type");
			dc.put(TagNames.ERROR_CODE, RecipeErrorCode.Invalid_Ref.getCode());
			return dc;
		}
		
		BaseProduct product=(BaseProduct) ServiceUtil.getProductFromDataBase(productType.name(), productId);
		//Check if product or productSpec is null;
		if(product==null){
			getLogger().error("Invalid product: the product doesn't exist.");
			dc.put(TagNames.ERROR_CODE, RecipeErrorCode.Invalid_Ref.getCode());
			return dc;
		}

		BaseProductSpec productSpec=(BaseProductSpec)ProductTypeUtil.getProductSpecDao(productType).findByProductSpecCode(product.getProductSpecCode(),productType.name());
		if(productSpec==null){
			getLogger().error("Invalid product: the product spec doesn't exist.");
			dc.put(TagNames.ERROR_CODE, RecipeErrorCode.Invalid_Ref.getCode());
			return dc;
		}
		ProductBuildResultDao pb = ProductTypeUtil.getProductBuildResultDao(productType);
		List<ProductBuildResult> installedPartList = pb.findAllByProductId(product.getProductId());
		MeasurementDao measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		if(!ProductTypeUtil.isDieCast(productType)){
			getMeasurementsForInstalledParts(product, installedPartList);
		}
		boolean isMbpnProduct = ProductTypeUtil.isMbpnProduct(productType);
		LotControlRuleId ruleId;
		LotControlRuleDao lotControlRuleDao = ServiceFactory.getDao(LotControlRuleDao.class);
		List<Object[]> rulesAndProcessPoints;
		if(isMbpnProduct){
			ruleId = new LotControlRuleId((Mbpn)productSpec);
			rulesAndProcessPoints = lotControlRuleDao.findAllRulesAndProcessPointsForMbpn(ruleId, productType.name());
		}
		else {
			ruleId = new LotControlRuleId((ProductSpec)productSpec);
			rulesAndProcessPoints = lotControlRuleDao.findAllRulesAndProcessPoints(ruleId, productType.name());
		}
		
		assembleLotControl(product,productSpec,rulesAndProcessPoints,lineId, partResults);
		
		List<BasePartResult> filterOutList = new ArrayList<BasePartResult>();
		for(BasePartResult partResult: partResults){
			
			if(partResult.isPartMark() ||
				LotControlPartUtil.isExcludedToSave(partResult.getPartName(), getExcludedToSaveParts())){
				filterOutList.add(partResult);
				continue;
			}
			
			ProductBuildResult installedPart = null;
			for(ProductBuildResult part : installedPartList){
				if(part.getPartName().equals(partResult.getLotControlRule().getId().getPartName())){
					installedPart= part;
				}
			}
			partResult.setBuildResult(installedPart);
			if(!(partResult.getStatus()==InstalledPartStatus.NG 
					|| partResult.getStatus()==InstalledPartStatus.NC
					|| partResult.getStatusMeasure()==MeasurementStatus.NG)){
				filterOutList.add(partResult);
				continue;
			}
			
			if(partResult.getStatus()==InstalledPartStatus.NC){
				//There is no record in 185TBX. so check if there is some record in gal198tbx and look for NG measurements
				List<Measurement> measurements = measurementDao.findAll(productId, partResult.getPartName());
				boolean shouldFilterOut = true;
				if(measurements.size()>0 && partResult.getMeasurementCount()> measurements.size()){
					//No enough measurements, comparing with lot control rule
					shouldFilterOut = false;
				}
				else if(measurements.size()>0 && partResult.getMeasurementCount() <= measurements.size()){
					//There is enough measurements. check if there is any NG measurement.
					for(Measurement m : measurements){
						if(m.getMeasurementStatus() == MeasurementStatus.NG){
							//NG measurement detected
							shouldFilterOut = false;
							break;
						}
					}
				}
				if(shouldFilterOut){
					if(!CommonUtil.isInList(partResult.getPartName(),requiredParts))
						filterOutList.add(partResult);
					continue;
				}else{
					InstalledPart fakePart = new InstalledPart();
					fakePart.setMeasurements(measurements);
					partResult.setBuildResult(fakePart);
				}
			}
			
		}
		partResults.removeAll(filterOutList);
		
		List<NGPartInfo> ngParts = new ArrayList<NGPartInfo>(partResults.size());
		for(BasePartResult partResult: partResults){
			NGPartInfo ngPart = new NGPartInfo();
			if(CommonUtil.isInList(partResult.getPartName(),requiredParts) || requiredParts.equalsIgnoreCase("")){
				ngPart.setPartName(partResult.getPartName());
				ngPart.setPartSerialNumber(partResult.getPartSerialNumber());
				ngPart.setProcessPointId(partResult.getProcessPointId());
				ngPart.setProcessPointName(partResult.getProcessPoint().getProcessPointName());
				ngPart.setMeasurementCount(partResult.getMeasurementCount());
				ProductBuildResult installedPart= partResult.getBuildResult();
				if(installedPart!=null && installedPart.getMeasurements()!=null){
					for(Measurement m : installedPart.getMeasurements()){
						setTorqueValue(ngPart, m.getId().getMeasurementSequenceNumber(), m.getMeasurementValue());
					}
				}
				ngParts.add(ngPart);
			}
		}

		dc.put(DataContainerTag.NO_GOOD_PARTS_LIST,ngParts);
		dc.put(DataContainerTag.PART_COUNT, ngParts.size());
		return dc;
	}
	private void setTorqueValue(NGPartInfo partInfo ,int index, Object value){
		switch (index-1) {
		case 0:
			partInfo.setTorque0(value==null?"":value.toString());
			break;
		case 1:
			partInfo.setTorque1(value==null?"":value.toString());
			break;
		case 2:
			partInfo.setTorque2(value==null?"":value.toString());
			break;
		case 3:
			partInfo.setTorque3(value==null?"":value.toString());
			break;
		case 4:
			partInfo.setTorque4(value==null?"":value.toString());
			break;
		case 5:
			partInfo.setTorque5(value==null?"":value.toString());
			break;
		case 6:
			partInfo.setTorque6(value==null?"":value.toString());
			break;
		case 7:
			partInfo.setTorque7(value==null?"":value.toString());
			break;
		case 8:
			partInfo.setTorque8(value==null?"":value.toString());
			break;
		case 9:
			partInfo.setTorque9(value==null?"":value.toString());
			break;
		default:
			break;
		}
	}
	
	/**
	 * get NG part lot control rule
	 */
	@Override
	public DataContainer execute(DataContainer data) {
		RecipeDownloadPDAServicePropertyBean propertyBean = PropertyService.getPropertyBean(RecipeDownloadPDAServicePropertyBean.class);
		String ngPartClientId = propertyBean.getNGPartLotControlRuleClientId();
		data.put(TagNames.CLIENT_ID.name(), ngPartClientId);
		return super.execute(data);
	}

	@Override
	protected void prepareSingleProductRecipe(Device device) {
		try{
			getProduct(device);
			
			String requestPPID = device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString();
			String requestProductSpec = product.getProductSpecCode();
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

	@Override
	protected DataContainer populateDataToDataContainer(Device device) {
		DataContainer dc = new DefaultDataContainer();
		String requestPPID = device.getDeviceFormat(getFullTag(TagNames.PROCESS_POINT_ID.name())).getValue().toString();
		String partName = device.getDeviceFormat(getFullTag(TagNames.PART_NAME.name())).getValue().toString();
		List<LotControlRule> rules = rulesMap.get(requestPPID);

		if(rules!=null && rules.size()!=0){
			List<LotControlRuleInfo> partInfoList = new ArrayList<LotControlRuleInfo>();
			for(LotControlRule r: rules){
				if(!partName.equals(r.getPartNameString())){
					continue;
				}
				List<PartByProductSpecCode> findByProductSpec = r.getPartByProductSpecs();
				//List<PartByProductSpecCode> findByProductSpec = partByProductSpecCodeDao.findByProductSpec(productSpec);
				for(PartByProductSpecCode partByProductSpecCode :findByProductSpec){
					
					if(!partName.equals(partByProductSpecCode.getId().getPartName())){
						continue;
					}
					
					LotControlRuleInfo ruleInfo= new LotControlRuleInfo();
					ruleInfo.setSerialNumberScanFlag(r.getSerialNumberScanFlag());
					ruleInfo.setSerialNumberUniqueFlag(r.getSerialNumberUniqueFlag());
					ruleInfo.setMeasurementCount(partByProductSpecCode.getPartSpec().getMeasurementCount());
					ruleInfo.setPartSerialNumberMask(CommonPartUtility.parsePartMask(partByProductSpecCode.getPartSpec().getPartSerialNumberMask()));
					ruleInfo.setPartId(partByProductSpecCode.getId().getPartId());
					ruleInfo.setPartName(r.getPartNameString());
					ruleInfo.setSequenceNumber(r.getSequenceNumber());
					ruleInfo.setPartMaxAttempts(partByProductSpecCode.getPartSpec().getPartMaxAttempts());
					
					List<MeasurementSpec> measurementSpecs = partByProductSpecCode.getPartSpec().getMeasurementSpecs();
					setTorgueValue(ruleInfo, measurementSpecs);
					
					partInfoList.add(ruleInfo);
				}
			}
			dc.put(DataContainerTag.PART_INFO_LIST,partInfoList);
		}
		
		for(DeviceFormat format: device.getReplyDeviceDataFormats()){
			dc.put(format.getTag(), getTagValue(true, format));
		}
		
		return dc;
	}

	private void setTorgueValue(LotControlRuleInfo ruleInfo,List<MeasurementSpec> measurementSpecs){
		for(MeasurementSpec measurementSpec:measurementSpecs){
			setMaxAndMinTorgueValue(ruleInfo, measurementSpec.getId().getMeasurementSeqNum(), measurementSpec.getMaximumLimit(), measurementSpec.getMinimumLimit());
		}
	}
	
	private void setMaxAndMinTorgueValue(LotControlRuleInfo ruleInfo,int index,Double maxValue,Double minValue){
		switch (index) {
		case 1:
			ruleInfo.setTorqueMax0(maxValue.toString());
			ruleInfo.setTorqueMin0(minValue.toString());
			break;
		case 2:
			ruleInfo.setTorqueMax1(maxValue.toString());
			ruleInfo.setTorqueMin1(minValue.toString());
			break;
		case 3:
			ruleInfo.setTorqueMax2(maxValue.toString());
			ruleInfo.setTorqueMin2(minValue.toString());
			break;
		case 4:
			ruleInfo.setTorqueMax3(maxValue.toString());
			ruleInfo.setTorqueMin3(minValue.toString());
			break;
		case 5:
			ruleInfo.setTorqueMax4(maxValue.toString());
			ruleInfo.setTorqueMin4(minValue.toString());
			break;
		case 6:
			ruleInfo.setTorqueMax5(maxValue.toString());
			ruleInfo.setTorqueMin5(minValue.toString());
			break;
		case 7:
			ruleInfo.setTorqueMax6(maxValue.toString());
			ruleInfo.setTorqueMin6(minValue.toString());
			break;
		case 8:
			ruleInfo.setTorqueMax7(maxValue.toString());
			ruleInfo.setTorqueMin7(minValue.toString());
			break;
		case 9:
			ruleInfo.setTorqueMax8(maxValue.toString());
			ruleInfo.setTorqueMin8(minValue.toString());
			break;
		case 10:
			ruleInfo.setTorqueMax9(maxValue.toString());
			ruleInfo.setTorqueMin9(minValue.toString());
			break;
		default:
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.honda.galc.service.recipe.RecipeDownloadPDAService1#updateNGPart(com.honda.galc.data.DataContainer)
	 */
	@Override
	public DataContainer updateNGPart(DataContainer data) {
		RecipeDownloadPDAServicePropertyBean propertyBean = PropertyService.getPropertyBean(RecipeDownloadPDAServicePropertyBean.class);
		String ngPartClientId = propertyBean.getUpdateNGPartClientId();
		data.put(TagNames.CLIENT_ID.name(), ngPartClientId);
		processEmptyTorque(data);
		String productTypeSend = data.getString(DataContainerTag.PRODUCT_TYPE);
		String ppid = data.getString(DataContainerTag.PROCESS_POINT_ID);
		if(productTypeSend==null || StringUtils.isEmpty(productTypeSend)){
			productTypeSend = PropertyService.getPropertyBean(SystemPropertyBean.class,ppid).getProductType();
			data.put(DataContainerTag.PRODUCT_TYPE, productTypeSend);
		}
		if(productTypeSend==null){
			getLogger().error("Invalid product Type");
			data.put(TagNames.ERROR_CODE, RecipeErrorCode.Invalid_Ref.getCode());
			return data;
		}
		DataContainer dc =  ServiceFactory.getService(DataCollectionService.class).execute(data);
		DataContainer result = new DefaultDataContainer();
		if(StringUtils.isNotBlank(dc.getString(TagNames.ERROR_MESSAGE.name()))){
			result.put(TagNames.ERROR_MESSAGE.name(), dc.get(TagNames.ERROR_MESSAGE.name()));	
		}
		else if(StringUtils.isNotBlank(dc.getString(TagNames.EXCEPTION.name()))){
			//Exception as error
			result.put(TagNames.ERROR_MESSAGE.name(), dc.get(TagNames.EXCEPTION.name()));
		}
		else if(StringUtils.isNotBlank(dc.getString(TagNames.VALID_PRODUCT_ID.name()))){
			//Invalid product id as error
			if(!Boolean.parseBoolean(dc.getString(TagNames.VALID_PRODUCT_ID.name()))){
				result.put(TagNames.ERROR_MESSAGE.name(), "Invalid Prouct Id");
			}
		}else{
			//No error
			result.put(TagNames.ERROR_MESSAGE.name(), "");
		}
		result.put(TagNames.JUDGEMENT.name(), dc.get(TagNames.JUDGEMENT.name()));
		result.put(TagNames.DUPLICATED_PRODUCT_ID.name(), dc.get(TagNames.DUPLICATED_PRODUCT_ID.name())==null?"":dc.get(TagNames.DUPLICATED_PRODUCT_ID.name()));
		result.put(TagNames.DATA_COLLECTION_COMPLETE.name(), dc.get(TagNames.DATA_COLLECTION_COMPLETE.name()));
		return result;
	}
	
	private void processEmptyTorque(DataContainer data){
		Set<Object> keySet = data.keySet();
		for(Object key: keySet){
			if(data.get(key).equals("")){
				data.put(key, "0");
			}
		}
	}
	
	private String getRequiredPartLists(String filterProcessPoints){
		StringBuilder requiredParts=new StringBuilder();
		String[] getFilterByRequiredParts = filterProcessPoints.split(",");
		if(getFilterByRequiredParts.length>0){
			boolean firstString=true;				
			for(String requiredPartProcessPoint :getFilterByRequiredParts){
				if(isValidProcesPoint(requiredPartProcessPoint)){
					List<RequiredPart> requiredPartsList = ServiceFactory.getDao(RequiredPartDao.class).findAllByProcessPoint(requiredPartProcessPoint);
					for(RequiredPart rp:requiredPartsList){
						if(!firstString){
							requiredParts.append(",");
						}
						requiredParts.append(rp.getId().getPartName());
						firstString=false;
						}
					}
				}
			}
		return requiredParts.toString();
	}
	
	private boolean isValidProcesPoint(String processPoint){
		if(StringUtils.isEmpty(processPoint)) return false;
		ProcessPoint pp=ServiceFactory.getDao(ProcessPointDao.class).findById(processPoint);
		return pp==null?false:true;
	}
	
}
