package com.honda.galc.service.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.conf.ProcessPointDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DefaultDataContainer;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.ProductTypeCatalog;
import com.honda.galc.data.TagNames;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.device.dataformat.PartSerialScanData;
import com.honda.galc.entity.conf.ProcessPoint;
import com.honda.galc.entity.enumtype.ParseStrategyType;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PartSnNoValidate;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.property.SystemPropertyBean;
import com.honda.galc.service.IErrorCode;
import com.honda.galc.service.PartSerialNumberErrorCode;
import com.honda.galc.service.PartSerialValidatorService;
import com.honda.galc.service.ServiceErrorCode;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.ServiceUtil;
import com.honda.galc.service.utils.SubProductSerialValidator;
import com.honda.galc.util.CommonPartUtility;

public class PartSerialValidatorServiceImpl implements PartSerialValidatorService {

	public static final String ADDITIONAL_MESSAGE = "ADDITIONAL_MESSAGE";
	private static final String CLASS_NAME = "PartSerialValidatorServiceImpl";

	protected String processPointId = null;

	protected BaseProduct product;
	protected LotControlRule rule;
	protected DataContainer retDC = new DefaultDataContainer();
	protected Logger logger;
	protected PartSpec partSpec;
	protected BaseProductSpec productSpec = null;
	protected ProcessPoint processPoint;
	protected PartSerialScanData partSerialScanData;
	protected PartSerialNumber partSerialNumber = new PartSerialNumber();
	protected String sequenceNumber;

	protected HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();

	@Autowired
	protected LotControlRuleDao lotControlRuleDao;

	@Autowired
	protected ProcessPointDao processPointDao;
	
	public static final String STATUS_NOT_GOOD = "Invalid Part";

	public void init(DataContainer data) {
		try {
			initDCData(data);
			initReturnDC(); 
		} catch (NullPointerException e) {
			addError(ServiceErrorCode.MISSING_DC_DATA, null);
			throw new TaskException(ServiceErrorCode.MISSING_DC_DATA.getDescription(), CLASS_NAME, e);
		} catch (Exception e) {
			context.setErrorCode(ServiceErrorCode.INITIALIZATION_ERROR);
			addError(ServiceErrorCode.INITIALIZATION_ERROR, null);
			throw new TaskException(ServiceErrorCode.INITIALIZATION_ERROR.getDescription(), CLASS_NAME, e);
		}
	}

	public void initDCData(DataContainer data) {
		if((String) data.get(TagNames.PROCESS_POINT_ID.name()) == null|| (String) StringUtils.trim((String) data.get(TagNames.PRODUCT_ID.name())) == null ||
				(String) data.get(TagNames.PRODUCT_TYPE.name()) == null || (String) data.get(TagNames.PART_SERIAL_NUMBER.name()) == null ||
				(String) data.get(TagNames.SEQUENCE.name()) == null) {
			context.setErrorCode(ServiceErrorCode.MISSING_DC_DATA);
			throw new NullPointerException(ServiceErrorCode.MISSING_DC_DATA.getDescription());
		}
		context.putAll(data);
		context.setLogger(getLogger());

		partSerialScanData = new PartSerialScanData();
		partSerialScanData.setProductId((String) StringUtils.trim((String) data.get(TagNames.PRODUCT_ID.name())));
		partSerialScanData.setProductType((String)  StringUtils.trim((String) data.get(TagNames.PRODUCT_TYPE.name())));
		partSerialScanData.setSerialNumber((String) StringUtils.trim((String) data.get(TagNames.PART_SERIAL_NUMBER.name())));
		processPointId = (String) StringUtils.trim((String) data.get(TagNames.PROCESS_POINT_ID.name()));
		sequenceNumber = (String) StringUtils.trim((String) data.get(TagNames.SEQUENCE.name()));
		partSerialNumber.setPartSn((String) StringUtils.trim((String) data.get(TagNames.PART_SERIAL_NUMBER.name())));

		if ((String) data.get(TagNames.PART_NAME.name().trim()) != null) {
			partSerialScanData.setPartName((String) StringUtils.trim((String) data.get(TagNames.PART_NAME.name())));
		}
		getLogger().info("Part Serial Validator received data :" +  getProcessPointId());
	}

	public void initReturnDC() {
		retDC.clear();
	}

	public DataContainer validateSerialNumber(DefaultDataContainer data) {
		try {
			init(data);

			processPoint = getProcessPoint();

			product = validateProduct();
			if(product == null) 
			{
				return retDC; 
			}

			getLogger().info("Processing product : " + product.getProductId());
			context.setProduct(product);

			productSpec = findByProductSpecCode();

			getLogger().info("Scanned serial number is : " + partSerialScanData.getSerialNumber());
			context.setProductSpec(productSpec);
			
			getLogger().info("Product Spec Code for: " + product.getProductId() + " is " + productSpec.getProductSpecCode());
			rule = getLotControlRule(productSpec);
			if (rule == null) {
				prepareNGReply(PartSerialNumberErrorCode.NO_RULE, null);
				getLogger().warn(PartSerialNumberErrorCode.NO_RULE.getDescription());
				return retDC;
			}

			getLogger().info("Current Lot Control Rule: ", getRule().toString());
			partSerialScanData.setMask(getRule().getPartMasks());

			if(!validate()) {
				return retDC;
			}

			SubProductSerialValidator subProductProcessor = new SubProductSerialValidator(context, getPartSpec(), getRule(), partSerialNumber);
			Boolean result = true;
			if(subProductProcessor.isSubProduct()) {
				getLogger().info("Passed all serial number checks, performing sub product checks.");
				result = subProductProcessor.performSubProductChecks();
			}
			if(result) {
				prepareOkReply();
				getLogger().info(PartSerialNumberErrorCode.NORMAL_REPLY.getMessage(partSerialNumber.getPartSn()));
			} else {
				prepareNGReply(context.getErrorCode(), context.containsKey(ADDITIONAL_MESSAGE) ? context.get(ADDITIONAL_MESSAGE).toString() : null);
			}

		} catch (TaskException e) {
			addError(context.getErrorCode(), context.containsKey(ADDITIONAL_MESSAGE) ? context.get(ADDITIONAL_MESSAGE).toString() : null);
			getLogger().error(e, context.containsKey(ADDITIONAL_MESSAGE) ? context.get(ADDITIONAL_MESSAGE).toString() : null);
			return retDC;

		} catch (Exception e) {
			addError(ServiceErrorCode.SYSTEM_ERR, null);
			getLogger().error(e, ServiceErrorCode.SYSTEM_ERR.getDescription());
			return retDC;
		}
		return retDC;
	}

	private ProcessPoint getProcessPoint() {
		ProcessPoint processPt = getProcessPoint(processPointId);
		if(processPt == null) {
			context.setErrorCode(ServiceErrorCode.INVALID_PP);
			context.put(ADDITIONAL_MESSAGE, processPointId);
			addError(ServiceErrorCode.INVALID_PP, processPointId);
			throw new TaskException(ServiceErrorCode.INVALID_PP.getMessage(processPointId), CLASS_NAME);
		}	
		return processPt;
	}

	private boolean validate() {
		// validate that the part name sent from PLC is the same as part name from rule.
		// This check only happens when the part name is sent from PLC, otherwise ignored.
		if (!confirmPartName()) {
			prepareNGReply(PartSerialNumberErrorCode.INVALID_PART, partSerialScanData.getPartName() + " expected " +  StringUtils.trim(getRule().getPartNameString()));
			getLogger().warn(PartSerialNumberErrorCode.INVALID_PART.getMessage(partSerialScanData.getPartName() + " expected " + StringUtils.trim(getRule().getPartNameString())));
			return false;
		}

		SystemPropertyBean sysBean = PropertyService.getPropertyBean(SystemPropertyBean.class, getRule().getId().getProcessPointId());

		if(getRule().isVerify()) {
			partSpec = CommonPartUtility.verify(partSerialScanData.getSerialNumber(), getRule().getParts(), PropertyService.getPartMaskWildcardFormat(), 
					getRule().isDateScan(), sysBean.getExpirationDays(), product, sysBean.isUseParsedDataCheckPartMask());
			if(partSpec == null) {
				prepareNGReply(PartSerialNumberErrorCode.MASK_CHECK_FAILED, partSerialScanData.getSerialNumber());
				getLogger().info(PartSerialNumberErrorCode.MASK_CHECK_FAILED.getMessage(partSerialScanData.getSerialNumber())
						+ " part Masks: " + getPartSnMaskList(getRule()));
				return false;
			} else {
				getLogger().info("Passed serial number mask check.");
			}
		}
		if(getRule().isUnique() && isDuplicatePart(partSerialScanData.getSerialNumber())) {
			return false;
		} else {
			getLogger().info("Passed duplicate part check.");
		}
		return true;
	}

	private BaseProductSpec findByProductSpecCode() {
		BaseProductSpec prodSpec = findByProductSpecCode(product.getProductSpecCode());
		if(prodSpec == null ) {
			context.setErrorCode(ServiceErrorCode.NO_SPEC_CODE);
			context.put(ADDITIONAL_MESSAGE, product.getProductSpecCode());
			addError(ServiceErrorCode.NO_SPEC_CODE, product.getProductSpecCode());
			throw new TaskException(ServiceErrorCode.NO_SPEC_CODE.getMessage(product.getProductSpecCode()), CLASS_NAME);
		}
		return prodSpec;
	}

	private BaseProduct validateProduct() {
		BaseProduct baseProduct = ServiceUtil.validateProductId(partSerialScanData.getProductType(), partSerialScanData.getProductId(), retDC, null);
		if (baseProduct == null) {
			prepareNGReply(ServiceErrorCode.INVALID_REF,  partSerialScanData.getProductId());
			getLogger().error(ServiceErrorCode.INVALID_REF.getMessage(partSerialScanData.getProductId()));
		}	
		return baseProduct;
	}

	protected boolean confirmPartName() {
		boolean correctPartName = false;
		if (partSerialScanData.getPartName() == null || StringUtils.isEmpty(partSerialScanData.getPartName()) || 
				partSerialScanData.getPartName().equalsIgnoreCase(StringUtils.trim(getRule().getPartNameString()))) {
			correctPartName = true;
		}
		return correctPartName;
	}

	@SuppressWarnings("unchecked")
	protected boolean isDuplicatePart(String partSn) {
		boolean isDuplicate = false;
		ProductType productType = ProductTypeCatalog.getProductType(partSerialScanData.getProductType());
		List<ProductBuildResult> list = (List<ProductBuildResult>)ProductTypeUtil.getProductBuildResultDao(productType)
				.findAllByPartNameAndSerialNumber(StringUtils.trim(getRule().getPartNameString()), partSn);

		if(list == null || list.isEmpty()) {
			return isDuplicate;
		}

		for(ProductBuildResult result : list){
			if(!result.getProductId().equals(partSerialScanData.getProductId())) {
				isDuplicate = true;
				prepareNGReply(PartSerialNumberErrorCode.DUPLICATE_REF, result.getProductId());
				getLogger().warn(PartSerialNumberErrorCode.DUPLICATE_REF.getMessage(partSerialScanData.getProductId()));
				break;
			}
		}
		return isDuplicate;
	}

	public void prepareOkReply() {
		retDC.put(TagNames.OVERALL_STATUS.name(), true);
		retDC.put(TagNames.ERROR_CODE.name(), PartSerialNumberErrorCode.NORMAL_REPLY.getCode());
		retDC.put(TagNames.ERROR_MESSAGE.name(),
				PartSerialNumberErrorCode.NORMAL_REPLY.getMessage(partSerialScanData.getSerialNumber()));
		retDC.put(TagNames.PROCESS_COMPLETE.name(), LineSideContainerValue.COMPLETE);
	}

	public void prepareNGReply(IErrorCode error, String additionalMsg) {
		retDC.put(TagNames.OVERALL_STATUS.name(), false);
		retDC.put(TagNames.ERROR_CODE.name(), error.getCode());
		retDC.put(TagNames.ERROR_MESSAGE.name(), error.getMessage(additionalMsg));
		retDC.put(TagNames.PROCESS_COMPLETE.name(), LineSideContainerValue.COMPLETE);
	}

	public void addError(IErrorCode errorCode, String additionalMsg) {
		getLogger().error(errorCode.getMessage(additionalMsg));
		retDC.put(TagNames.ERROR_CODE.name(), errorCode.getCode());
		retDC.put(TagNames.ERROR_MESSAGE.name(), errorCode.getMessage(additionalMsg));
		retDC.put(TagNames.PROCESS_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
		retDC.put(TagNames.OVERALL_STATUS.name(), false);
	}

	private List<String> getPartSnMaskList(LotControlRule rule) {
		List<String> masks = new ArrayList<String>();
		for(PartSpec spec : rule.getParts())
			masks.add(CommonPartUtility.parsePartMask(spec.getPartSerialNumberMask()));

		return masks;
	}

	private BaseProductSpec findByProductSpecCode(String productSpecCode) {
		BaseProductSpec baseproductSpec;
		if(!ProductTypeUtil.isMbpnProduct(partSerialScanData.getProductType())) {
			baseproductSpec = (ProductSpec)ProductTypeUtil.getProductSpecDao(partSerialScanData.getProductType()).findByProductSpecCode(productSpecCode,
					partSerialScanData.getProductType());
		} else {
			baseproductSpec = (Mbpn)ProductTypeUtil.getProductSpecDao(partSerialScanData.getProductType()).findByProductSpecCode_NoTxn(productSpecCode, partSerialScanData.getProductType());}
		return baseproductSpec;
	}

	private LotControlRule getLotControlRule(BaseProductSpec productSpec) {
		try {
			List<LotControlRule> lotControlRule = lotControlRuleDao.getLotControlRuleByProductSpecCodeProcessId(processPointId,
					productSpec.getProductSpecCode(), Integer.parseInt(getSequenceNumber()));
			return lotControlRule == null || lotControlRule.isEmpty() ? null : lotControlRule.get(0);
		} catch(NumberFormatException e) {
			context.setErrorCode(ServiceErrorCode.MISSING_DC_DATA);
			addError(ServiceErrorCode.MISSING_DC_DATA, null);
			throw new TaskException(ServiceErrorCode.MISSING_DC_DATA.getDescription(), CLASS_NAME, e);
		}
	}

	protected ProcessPoint getProcessPoint(String processPointId) {
		try {
			return processPointDao.findById(processPointId);
		} catch (NullPointerException e) {
			context.setErrorCode(ServiceErrorCode.INVALID_PP);
			addError(ServiceErrorCode.INVALID_PP, processPointId);
			throw new TaskException(ServiceErrorCode.INVALID_PP.getMessage(processPointId), CLASS_NAME, e);
		}
	}

	public PartSerialNumber getSerialNumber() {
		return partSerialNumber == null ? partSerialNumber = new PartSerialNumber(partSerialScanData.getSerialNumber()) : partSerialNumber;
	}

	public LotControlRule getRule() {
		return this.rule;
	}

	public String getProcessPointId() {
		return this.processPointId;
	}

	public PartSpec getPartSpec() {
		return this.partSpec;
	}

	public BaseProductSpec getProductSpec() {
		return this.productSpec;
	}

	public BaseProduct getProduct() {
		return this.product;
	}

	public String getSequenceNumber() {
		return this.sequenceNumber;
	}

	public PartSerialScanData getPartSerialScenData() {
		return this.partSerialScanData;
	}

	public DataContainer getRetDC() {
		return this.retDC;
	}

	public Logger getLogger() {
		if (getProcessPointId() == null || getProcessPointId().equals(""))
			return Logger.getLogger(CLASS_NAME);
		else
			return Logger.getLogger(getProcessPointId());
	}
	
	/**
	 * Method verifies the part serial number against part mask and sends response
	 * @param partsnValidate - Input from rest call service 
	 * @return response
	 */
	public String checkPartSerialNumberMask(PartSnNoValidate partsnValidate) {
		int addedDays = PropertyService.getPropertyBean(SystemPropertyBean.class,partsnValidate.getProcessPoint()).getExpirationDays();
		BaseProduct product = ProductTypeUtil.getProductDao(partsnValidate.getProductType()).findBySn(partsnValidate.getProductId());
		String partSn =  parsePartSerialNumber(partsnValidate.getPartSpec(), partsnValidate.getPartSrNo() );
		getLogger().info("checkPartSerialNumberMask : parsePartSerialNumber :: partSn : ", partSn);
		String partSntoCheck = PropertyService.getPropertyBean(SystemPropertyBean.class,partsnValidate.getProcessPoint()).isUseParsedDataCheckPartMask() ?
				partSn : partsnValidate.getPartSrNo();
		getLogger().info("checkPartSerialNumberMask : partSntoCheck : ", partSntoCheck);
		
		if(partsnValidate.isDateScanFlag()){
			//verify date mask if datescan is required
			if(CommonPartUtility.verifyDateMask(partSntoCheck, partsnValidate.getPartSpec().getPartSerialNumberMask(),addedDays)){
				return "";
			} else 
				return "Input Date :"+partSntoCheck +" is not valid.";
		}
		else if(CommonPartUtility.verification(partSntoCheck, partsnValidate.getPartSpec().getPartSerialNumberMask(),
				PropertyService.getPartMaskWildcardFormat(), product))
		{
			//required for fixed length, if the input length is less than minimum length, fail the part check
			if(!StringUtils.isEmpty(partsnValidate.getPartSpec().getParseStrategy()) && 
					ParseStrategyType.valueOf(partsnValidate.getPartSpec().getParseStrategy()) == ParseStrategyType.FIXED_LENGTH) {
				int minLength = CommonPartUtility.getMinPartSnLength(partsnValidate.getPartSpec()); 
				if(partSntoCheck.length() < minLength)
					return STATUS_NOT_GOOD;
			}
			Logger.getLogger().info("Part SN :", partSntoCheck+"Part Mask :",partsnValidate.getPartSpec().getPartSerialNumberMask()+" passed verification!");
			return "";
		}
	  return STATUS_NOT_GOOD;
	}
	/**
	 * Method to parse serial number
	 * @param partSpec
	 * @param partSerialNumber
	 * @return partSerialNumber
	 */
	private String parsePartSerialNumber(PartSpec partSpec, String partSerialNumber) {
		String psn = CommonPartUtility.parsePartSerialNumber(partSpec,partSerialNumber);
        if(psn == null) 
            Logger.getLogger().info("Part serial number :", partSerialNumber +  
                       " parse failed. parseStrategy :", partSpec.getParseStrategy() + ";parseInfo :"+partSpec.getParserInformation()); 
        return psn;
	}
}
