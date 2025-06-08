package com.honda.galc.service.recipe;

import static com.honda.galc.service.ServiceFactory.getService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.constant.Delimiter;
import com.honda.galc.dao.product.ExpectedProductDao;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.HeadlessDataMapping;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.BuildAttributeCache;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.conf.DeviceFormat;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BuildAttribute;
import com.honda.galc.entity.product.ExpectedProduct;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.PreProductionLot;
import com.honda.galc.entity.product.Product;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.RecipePropertyBean;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.TrackingService;
import com.honda.galc.service.datacollection.InstalledPartHelper;
import com.honda.galc.service.on.ProductOnServiceBase;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>RecipeDownloadBase</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadBase description </p>
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
 * <TD>P.Chou</TD>
 * <TD>Jan 2, 2013</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Paul Chou
 * @since Jan 2, 2013
 */
/**
 * 
 * @author Gangadhararao Gadde
 * @date Aug 10, 2015
 * Recipe Download Service
 */
public class RecipeDownloadBase extends ProductOnServiceBase{
	
	private static final int MAX_MASK_LENGTH = 30;//max length from the recipe
	
	private TrackingService trackingService;
	private int lastRuleSeqOfPreviousPpid;
	
	protected BaseProductSpec productSpec;
	protected String requestProductId;
	protected Map<String,List<LotControlRule>> rulesMap;
	protected RecipeProductSequenceHelper helper;
	protected InstalledPartHelper installedPartHelper;
	protected InstalledPartDao installedPartDao;
	
	@Autowired
	private ExpectedProductDao expectedProductDao;
	private MeasurementDao measurementDao;
	
	public RecipeDownloadBase() {
		super();
	}
	
	@Override
	protected void init(Device device) {
		super.init(device);
		context.putAll(device.getInputMap());
		helper = null;
		this.installedPartHelper = new InstalledPartHelper(device, getPropertyBean(), processPointId, logger, null);
		if(rulesMap != null) {
			rulesMap.clear();
		} else {
			rulesMap = new HashMap<String,List<LotControlRule>>();
		}
		productSpec = null;
	}

	protected void populateRules(Device device) {
		lastRuleSeqOfPreviousPpid = 0;
		for(List<LotControlRule> rlist : rulesMap.values()){
			populateRules(device, rlist);
			lastRuleSeqOfPreviousPpid += rlist.get(rlist.size() -1).getSequenceNumber();
		}
		
	}
	
	protected void populateRules(Device device, List<LotControlRule> rules) {
		for(LotControlRule r: rules){
			populateRuleContext(r);			
		}
	}

	private void populateRuleContext(LotControlRule r) {
		contextPut(getMappingTagName(r, TagNames.SEQUENCE.name()), getSequenceNumber(r));
		String instructionCode = StringUtils.isEmpty(r.getInstructionCode()) ? "  " : r.getInstructionCode();
		contextPut(getMappingTagName(r,TagNames.INSTRUCTION_CODE.name()), instructionCode);
		
		String masks = getPartMasks(r);
		
		if(masks.length() > MAX_MASK_LENGTH)
			addError(RecipeErrorCode.Mask_Oversize, product.getProductId() + " mask size:" + masks.length());
		contextPut(getMappingTagName(r,TagNames.PART_MASK.name()), masks);
		
		int measurementCount = 0;
		PartSpec partSpec = null;
		contextPut(getMappingTagName(r,TagNames.TORQUE_COUNT.name()), measurementCount);
		if(r.getParts()!=null && r.getParts().size() > 0){
			partSpec = getPartSpec(r);	
			if(partSpec == null) getLogger().warn("WARN: No part spec matches part serial number.");
			if(partSpec != null ) {
				measurementCount = partSpec.getMeasurementCount();
				contextPut(getMappingTagName(r,TagNames.TORQUE_COUNT.name()), measurementCount);
				contextPut(getMappingTagName(r,TagNames.PART_MARK.name()), partSpec.getPartMark());
				contextPut(getMappingTagName(r,TagNames.PART_NUMBER.name()), partSpec.getPartNumber());
				contextPut(getMappingTagName(r,TagNames.PART_DESCRIPTION.name()), partSpec.getPartDescription());

				if(!StringUtils.isEmpty(getPartSerialNumberFromDevice(r))) // only overwrite the part mask if a specific rule is selected
					contextPut(getMappingTagName(r,TagNames.PART_MASK.name()), stripOffWildCardBracket(partSpec.getPartSerialNumberMask()));
					
				if(measurementCount > 0 && partSpec.getMeasurementSpecs().size() > 0){
					contextPut(getMappingTagName(r,TagNames.MAX_LIMIT.name()), partSpec.getMeasurementSpecs().get(0).getMaximumLimit());
					contextPut(getMappingTagName(r,TagNames.AVERAGE_LIMIT.name()), partSpec.getMeasurementSpecs().get(0).getAverageLimit());
					contextPut(getMappingTagName(r,TagNames.MIN_LIMIT.name()), partSpec.getMeasurementSpecs().get(0).getMinimumLimit());
				}
			} 
		}
		
		// just add extra 
		contextPut(getMappingTagName(r,TagNames.UNIQ.name()), r.getSerialNumberUniqueFlag());
		contextPut(getMappingTagName(r,TagNames.VERIFY.name()), r.getVerificationFlag());
		contextPut(getMappingTagName(r,TagNames.SCAN.name()), r.getSerialNumberScanFlag());
		contextPut(getMappingTagName(r,TagNames.DEVICE_ID.name()), r.getDeviceId());
		contextPut(getMappingTagName(r,TagNames.PART_NAME.name()), r.getPartNameString());
	}
	
	private PartSpec getPartSpec(LotControlRule r) {
		String partSerialNumber = getPartSerialNumberFromDevice(r);
		if(StringUtils.isEmpty(partSerialNumber))
			return r.getParts().get(0);
		else 
			return CommonPartUtility.verify(partSerialNumber, r.getParts(),
					PropertyService.getPartMaskWildcardFormat(), r.isDateScan(), getPropertyBean().getExpirationDays(), 
					null, getPropertyBean().isUseParsedDataCheckPartMask());


	}


	private String getPartSerialNumberFromDevice(LotControlRule r) {
		DeviceFormat partSnDf = device.getDeviceFormat(getFullTag(getMappingTagName(r, TagNames.PART_SERIAL_NUMBER.name())));
		return StringUtils.trimToEmpty(partSnDf == null ? "" : (String)partSnDf.getValue());
	}

	private String getPartMasks(LotControlRule r) {
		StringBuilder sb = new StringBuilder();
		for(PartSpec spec : r.getParts()){
			if(sb.length() > 0) sb.append(",");
			sb.append(stripOffWildCardBracket(spec.getPartSerialNumberMask()));

		}
		return sb.toString();
	}

	private String stripOffWildCardBracket(String specString) {
		return StringUtils.remove(StringUtils.remove(specString, "<<"),">>");
	}
	
	protected void populateCommon() {
		if(null==product){
			return;
		}
		contextPut(TagNames.REF.name(),product.getProductId());
		
		if(product instanceof Frame) {
			contextPut(TagNames.AF_ON_SEQUENCE_NUMBER.name(),((Frame)product).getAfOnSequenceNumber());
		}
		
		contextPut(TagNames.PRODUCT_ID.name(),product.getProductId());
		String productSpecCode = product.getProductSpecCode();
		contextPut(TagNames.PRODUCT_SPEC_CODE.name(),productSpecCode);
		contextPut(TagNames.PRODUCTION_LOT.name(),product.getProductionLot());
		contextPut(TagNames.INSTRUCTION_COUNT.name(),getInstructionCount());
		boolean isMbpnProduct = ProductTypeUtil.isMbpnProduct(productType);
		if(productSpec != null) {
			if (!isMbpnProduct){
			contextPut(TagNames.MODEL_YEAR_CODE.name(), ((ProductSpec)productSpec).getModelYearCode());
			contextPut(TagNames.MODEL_CODE.name(), ((ProductSpec)productSpec).getModelCode());
			contextPut(TagNames.MODEL_OPTION.name(), ((ProductSpec)productSpec).getModelOptionCode());
			contextPut(TagNames.MODEL_TYPE.name(),((ProductSpec)productSpec).getModelTypeCode());
			contextPut(TagNames.EXT_COLOUR.name(), ((ProductSpec)productSpec).getExtColorCode());
			contextPut(TagNames.INT_COLOUR.name(), ((ProductSpec)productSpec).getIntColorCode());
			
			contextPut(TagNames.BOUNDARY_MARK_REQUIRED.name(), ((ProductSpec)productSpec).getBoundaryMarkRequired());
			contextPut(TagNames.PRODUCT_ID_WITH_BOUNDARY.name(),product.getProductIdWithBoundary(((ProductSpec)productSpec).getBoundaryMarkRequired()));
			}
			
		} else 
			getLogger().warn(this.getClass().getSimpleName(), " Product Spec is null.");
		
		if(product instanceof Product)
			contextPut(TagNames.KD_LOT.name(), ((Product)product).getKdLotNumber());
		
		getLogger().info("production lot:" + product.getProductionLot() +  " mtoc:" + product.getProductSpecCode());

		if(!StringUtils.isEmpty(getPropertyBean().getBuildAttributes()))
				populateBuildAttributes(product.getProductSpecCode());
		
		if(getPropertyBean().isDownloadLotInfo()) populateLotInfo();
	}

	private void populateBuildAttributes(String specCode) {
		String[] split = getPropertyBean().getBuildAttributes().split(Delimiter.COMMA);
		BuildAttributeCache cache = new BuildAttributeCache();
		
		for(String buildattr : split){
			BuildAttribute buildAttr = cache.findById(specCode, buildattr, product.getSubId());
			
			if(buildAttr != null)
				contextPut(buildAttr.getTag(), buildAttr.getAttributeValue());
			else
				getLogger().warn("Failed to find build attribute for:", buildattr, " product spec code:", specCode);
		}
		
	}

	protected String getMappingTagName(LotControlRule r, String name) {
		return HeadlessDataMapping.MAP_BY_PART_NAME.name().equals(getPropertyBean().getPlcDataMapping())?
				r.getPartNameString() + Delimiter.DOT + name : getSequenceNumber(r) + Delimiter.DOT + name;
	}

	private int getSequenceNumber(LotControlRule r) {
		
		return r.getSequenceNumber() + lastRuleSeqOfPreviousPpid;
	}
	

	protected void getNextProduct(Device device) {
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();

		if(!validateProductId(requestProductId))
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);

		//TODO check if the current product is skipped
		
		product = getHelper().nextProduct(requestProductId);
		if(product == null){
			addError(RecipeErrorCode.No_Next_Ref, requestProductId);
		}
	}
	
	protected void getCurrentProduct(Device device) {
		requestProductId = device.getDeviceFormat(getFullTag(TagNames.PRODUCT_ID.name())).getValue().toString();

		if(!validateProductId(requestProductId))
			addError(RecipeErrorCode.Invalid_Ref, requestProductId);

		product = getHelper().getProductDao().findByKey(requestProductId);
		
		if(product == null){
			addError(RecipeErrorCode.No_Next_Ref, requestProductId);
		}
	}

	protected void getLotControlRules(BaseProduct nextProduct) {
		if(null==nextProduct){
			return;
		}
		
		//Check if the Rules already loaded. Do nothing if already loaded. 
		if( getPropertyBean().isOneRuleForSubIds() && rulesMap != null && rulesMap.size() > 0 &&
			productSpec != null && productSpec.equals(nextProduct.getProductSpecCode())) 
		    return;
		
		rulesMap = new LinkedHashMap<String, List<LotControlRule>>();
		List<String> ppids = CommonUtil.splitStringList(getPropertyBean().getRecipeProcessPointIds());
		
		if(!ProductTypeUtil.isMbpnProduct(productType))
			productSpec = (ProductSpec)ProductTypeUtil.getProductSpecDao(nextProduct.getProductType()).findByProductSpecCode(nextProduct.getProductSpecCode(), productType.toString());
		else
			productSpec = (Mbpn)ProductTypeUtil.getProductSpecDao(nextProduct.getProductType()).findByProductSpecCode_NoTxn(nextProduct.getProductSpecCode(), productType.toString());
		for(String ppid : ppids){
			ppid = StringUtils.trim(ppid);
			List<LotControlRule> rules = LotControlRuleCache.getOrLoadLotControlRule(productSpec, StringUtils.trim(ppid));
			if(!StringUtils.isEmpty(nextProduct.getSubId()) || (!StringUtils.isEmpty(getPropertyBean().getLotControlRuleFilter())))
				rules = filterLotControlRules(rules, nextProduct.getSubId());
			
			rulesMap.put(ppid, rules);
		}

	}

	public TrackingService getTrackingService() {
		if(trackingService == null)
			trackingService = getService(TrackingService.class);
		
		return trackingService;
	}


	protected List<LotControlRule> filterLotControlRules(List<LotControlRule> rules, String subId) {
		List<LotControlRule> newList = new ArrayList<LotControlRule>();
		
		for(LotControlRule rule : rules){

			if(!StringUtils.isEmpty(getPropertyBean().getLotControlRuleFilter())){
				if(!CommonUtil.isInList(rule.getPartNameString(), getPropertyBean().getLotControlRuleFilter()))
					continue;
			}

			if(!StringUtils.isEmpty(subId) && (!StringUtils.isEmpty(rule.getSubId()) && !subId.equals(rule.getSubId())))
					continue;

			newList.add(rule);

		}
		
		return newList;
	}

	protected void handleException(RecipeErrorCode recipeErrorCode) {
		throw new TaskException(recipeErrorCode.getDescription() + requestProductId, recipeErrorCode.getCode());
	}

	public RecipePropertyBean getPropertyBean() {
		if(propertyBean == null)
			propertyBean = PropertyService.getPropertyBean(RecipePropertyBean.class, getProcessPointId());
		return (RecipePropertyBean)propertyBean;
	}
	
	protected boolean hasLotControlRule() {
		if(null==rulesMap){
			return false;
		}
		for(List<LotControlRule> list : rulesMap.values())
			if(list != null && list.size() > 0) return true;
		
		return false;
	}

	protected int getInstructionCount() {
		int instructionCount = 0;
		for(List<LotControlRule> list : rulesMap.values())
			if(list != null && list.size() >  0)
				instructionCount += list.get(list.size() -1).getSequenceNumber();
			
		return instructionCount;
	}


	protected void addError(RecipeErrorCode recipeErrorCode, String productId) {
		RecipeErrorCode fromCode = RecipeErrorCode.fromCode((String)context.get(TagNames.ERROR_CODE.name()));
		if(fromCode == null || recipeErrorCode.getSeverity() > fromCode.getSeverity())
			contextPut(TagNames.ERROR_CODE.name(), recipeErrorCode.getCode());

		if(recipeErrorCode.isWarning())
			getLogger().warn(recipeErrorCode.getDescription(productId));
		else
			throw new TaskException(recipeErrorCode.getDescription(productId));
	}


	private void populateLotInfo() {
		int kdLotPosition = getHelper().getKdLotPosition(product); //position start from 1
		contextPut(TagNames.KD_LOT_SIZE.name(), getKdLotSize());
		if(getPreProductionLot() != null) contextPut(TagNames.PRODUCTION_LOT_SIZE.name(), getPreProductionLot().getLotSize());
		contextPut(TagNames.KD_LOT_POSITION.name(), isKnuckle()? kdLotPosition*2 : kdLotPosition);
		if(getPreProductionLot() != null) contextPut(TagNames.START_PRODUCT_ID.name(), getPreProductionLot().getStartProductId());
		contextPut(TagNames.CART_ID.name(), (kdLotPosition -1)/getPropertyBean().getCartSize() + 1);
	}


	private boolean isKnuckle() {
		return productType == ProductType.KNUCKLE;
	}

	protected int getKdLotSize(){
		return getHelper().getKdLotSize();
	}
	
	@Override
	protected String getProductionLot() {
		return product.getProductionLot();
	}
	
	
	public RecipeProductSequenceHelper getHelper() {
		if(helper == null)
			helper = RecipeProductSequenceHelper.getHelper(getProcessPointId(), getProductType(), getPropertyBean(), getLogger());
		
		return helper;
	}

	protected int getPrdocuctSequence(String productId){
		return getHelper().getProductSequence(productId);
	}
	
	protected PreProductionLot getPreProductionLot() {
		return getHelper().getEffectivePreProductionLot();
	}

	public boolean validateProductId(String  productId){
		return ServiceUtil.isProductIdValid(getProductType().name(),productId);
	}

	public String getRequestProductId() {
		return requestProductId;
	}
	
	public ExpectedProductDao getExpectedProductDao() {
		if(expectedProductDao == null)
			expectedProductDao = ServiceFactory.getDao(ExpectedProductDao.class);
		return expectedProductDao;
	}
	
	protected boolean validateSkippingProduct(String productId) {
		ExpectedProduct expectedProduct = getExpectedProductDao().findByKey(getProcessPointId());
		return expectedProduct == null ? true : expectedProduct.getProductId().equals(productId);
	}


	protected void processAddtionalFeatures(Device device) {
		
	}
	
	protected InstalledPartDao getInstalledPartDao() {
		if(installedPartDao == null)
			installedPartDao = ServiceFactory.getDao(InstalledPartDao.class);
		
		return installedPartDao;
	}
	
	protected void getMeasurementsForInstalledParts(List<InstalledPart> installedParts) {
		for(InstalledPart part : installedParts){
			List<Measurement> measuremnts = getMeasurementDao().findAllOrderBySequence(part.getProductId(), part.getPartName(), true);
			part.setMeasurements(measuremnts);
		}
		
	}

	private MeasurementDao getMeasurementDao() {
		if(measurementDao == null)
			measurementDao = ServiceFactory.getDao(MeasurementDao.class);
		
		return measurementDao;
	}
	

	protected List<String> getPartNames(List<LotControlRule> rules) {
		List<String> list = new ArrayList<String>();
		for(LotControlRule r : rules)
			list.add(r.getPartNameString());
		return list;
	}

	
}
