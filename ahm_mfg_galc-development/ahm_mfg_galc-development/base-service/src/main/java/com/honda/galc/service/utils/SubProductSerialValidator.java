package com.honda.galc.service.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.Frame;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.MbpnProduct;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.property.ProductCheckPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.service.PartSerialNumberErrorCode;
import com.honda.galc.service.ServiceErrorCode;
import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.ProductCheckType;
import com.honda.galc.util.ProductCheckUtil;
import com.honda.galc.util.SubproductUtil;

public class SubProductSerialValidator {

	public static final String ADDITIONAL_MESSAGE = "ADDITIONAL_MESSAGE";
	private HeadlessDataCollectionContext context = new HeadlessDataCollectionContext();
	private SubproductUtil subproductUtil;
	private SubproductPropertyBean subProductProperty;
	private ProductCheckUtil productCheckUtil;
	private EngineUtil engineUtil;
	private PartSpec partSpec;
	private LotControlRule rule;
	private PartSerialNumber serialNumber = null;

	private BaseProduct subProduct;

	private String installProcessPointId = null;

	public SubProductSerialValidator(HeadlessDataCollectionContext context, PartSpec spec, LotControlRule rule, PartSerialNumber serialNumber) {
		this.context = context;
		this.partSpec = spec;
		this.rule = rule;
		this.serialNumber = serialNumber;
	}

	public boolean performSubProductChecks() {

		confirmSubproductType();

		subProduct = getSubProductUtil().findSubproduct();
		if(subProduct==null)
		{
			context.put(ADDITIONAL_MESSAGE, serialNumber.getPartSn());
			context.setErrorCode(PartSerialNumberErrorCode.INVALID_REF);
			context.getLogger().error(PartSerialNumberErrorCode.INVALID_REF.getMessage(serialNumber.getPartSn()));
			return false;
		}

		if(subProduct instanceof MbpnProduct) {
			return(validateMbpn(subProduct));

		} else if(subProduct instanceof Engine) {
			return(validateEngine((Engine)subProduct));
		}
		return true;
	}

	public boolean validateEngine(Engine subProduct) {
		context.getLogger().info("Begin validating Engine sub product");

		installProcessPointId = getSubproductProperty().getInstallProcessPointMap().get(getSubProductName());

		//check if engine is already assigned to a frame
		if(getProductCheckUtil().duplicateEngineAssignmentCheck()) {
			context.put(ADDITIONAL_MESSAGE, subProduct.getProductId());
			context.setErrorCode(PartSerialNumberErrorCode.DUPLICATE_REF);
			return false;
		}

		//check Engine type mismatch	
		if(context.getProduct() instanceof Frame) {
			Frame frame = (Frame)context.getProduct();
			ProductCheckPropertyBean productCheckProperty = PropertyService.getPropertyBean(ProductCheckPropertyBean.class,
					context.getProcessPointId());
			boolean useAltEngineMto = productCheckProperty.isUseAltEngineMto();
			boolean engineTypeCheck = getProductCheckUtil().checkEngineTypeForEngineAssignment(frame, subProduct, useAltEngineMto);

			if (!engineTypeCheck) {
				context.setErrorCode(ServiceErrorCode.INCORRECT_SPEC_CODE);
				return false;
			}
			context.getLogger().info("Engine has correct spec code for Frame: " + context.getProduct().getProductId());
		}

		//check if engine is on hold
		List<String> holdList = getProductCheckUtil().engineOnHoldCheck();
		if(holdList != null && !holdList.isEmpty()) {
			context.setErrorCode(PartSerialNumberErrorCode.ON_HOLD);
			return false;
		}
		
		context.getLogger().info("Engine passed on hold check");

		//check if engine came from a valid previous line only if configured to do so.
		if(!getEngineUtil().checkValidPreviousEngineLine(subProduct)){
			context.setErrorCode(PartSerialNumberErrorCode.INVALID_LINE);
			return false;
		}

		List<String> checkTypesList = new ArrayList<String>();

		if(!doChecks(checkTypesList, subProduct)) {
			return false;
		}
		context.getLogger().info("Passed all sub product checks");
		context.setErrorCode(PartSerialNumberErrorCode.NORMAL_REPLY);
		return true; 
	}

	public boolean validateMbpn(BaseProduct subProduct) {

		List<String> checkTypesList = new ArrayList<String>();

		context.getLogger().info("Begin validating MBPN sub product");

		MbpnProduct product = ((MbpnProduct) subProduct);

		if (!getSubProductUtil().isValidSpecCode(getSubProductName(), subProduct, rule.getProductSpecCode())) {
			context.setErrorCode(ServiceErrorCode.INCORRECT_SPEC_CODE);
			return false;
		}

		context.getLogger().info("MBPN sub product contains valid spec code");

		//mandatory checks for Installed part of MBPN product type 
		if(product.getExternalBuild() != 1){
			checkTypesList.add(ProductCheckType.RECURSIVE_INSTALLED_PART_CHECK.name());
			checkTypesList.add(ProductCheckType.OUTSTANDING_PARTS_CHECK.name());
		}
		checkTypesList.add(ProductCheckType.OUTSTANDING_DEFECTS_CHECK.name());
		checkTypesList.add(ProductCheckType.CHECK_SCRAPPED_EXCEPTIONAL_OUT.name());
		checkTypesList.add(ProductCheckType.PRODUCT_ON_HOLD_CHECK.name());	

		installProcessPointId = getInstallProcessPoint();
		if(!doChecks(checkTypesList, subProduct)) {
			return false;
		}

		context.getLogger().info("Passed all sub product checks");
		context.setErrorCode(PartSerialNumberErrorCode.NORMAL_REPLY);
		return true;
	}

	private boolean doChecks(List<String> checkTypesList, BaseProduct subProduct) {
		boolean isPassed = true;
		try {
			List<String> failedProductCheckList = new ArrayList<String>();

			if(checkTypesList != null && !checkTypesList.isEmpty()) {
				String[] checkTypes = checkTypesList.toArray(new String[0]);	
				failedProductCheckList.addAll(getSubProductUtil().performSubProductChecks(getSubProductName(), subProduct, context.getProcessPointId(), checkTypes));
			}
			failedProductCheckList.addAll(getSubProductUtil().performSubProductChecks(getSubProductName(), 
					subProduct, installProcessPointId));
			if(!failedProductCheckList.isEmpty()) {
				isPassed = false;
				StringBuilder msg = new StringBuilder();
				msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
				for (int i = 0; i < failedProductCheckList.size(); i++) {
					msg.append(failedProductCheckList.get(i));
					if (i != (failedProductCheckList.size() - 1)) {
						msg.append(", ");
					}
				}
				context.getLogger().info(msg.toString());
				context.put(ADDITIONAL_MESSAGE, failedProductCheckList);
				context.setErrorCode(PartSerialNumberErrorCode.PART_CHECK_FAILED);
			}
		} catch (Exception e) {
			context.getLogger().error(PartSerialNumberErrorCode.PART_CHECK_ERROR.getDescription());
			context.setErrorCode(PartSerialNumberErrorCode.PART_CHECK_ERROR);
			throw new TaskException(PartSerialNumberErrorCode.PART_CHECK_ERROR.getDescription(), e);
		}
		return isPassed;
	}

	private String getInstallProcessPoint() {
		String processPoint = "";
		try {
			if(!getSubproductProperty().isUseMainNoFromPartSpec())
				processPoint = getSubproductProperty().getInstallProcessPointMap().get(getSubProductName());
			else{
				processPoint = getSubproductProperty().getInstallProcessPointMap().get(getMainNo(rule.getProductSpecCode()));
			}
		}catch(NullPointerException e) {
			String msg = PartSerialNumberErrorCode.INSTALL_PP_MISSING.getMessage(context.getProcessPointId()) + " for product type of " + getSubProductName();
			context.getLogger().error(msg);
			context.put(ADDITIONAL_MESSAGE, context.getProcessPointId());
			context.setErrorCode(PartSerialNumberErrorCode.INSTALL_PP_MISSING);
			throw new TaskException(msg, e);
		}		
		return processPoint;
	}

	public void confirmSubproductType() {
		if(StringUtils.isBlank(getSubProductName())) {
			context.setErrorCode(PartSerialNumberErrorCode.MISSING_SUB_PROODUCT_TYPE);
			throw new TaskException(PartSerialNumberErrorCode.MISSING_SUB_PROODUCT_TYPE.getDescription());
		}		
	}

	public boolean isSubProduct() {	
		return !StringUtils.isBlank(getSubProductName());
	}

	public SubproductPropertyBean getSubproductProperty() {
		return subProductProperty == null ? subProductProperty = PropertyService.getPropertyBean(
				SubproductPropertyBean.class,context.getProcessPointId()) : subProductProperty;
	}

	public SubproductUtil getSubProductUtil() {
		return subproductUtil == null ? subproductUtil = new SubproductUtil(serialNumber, rule, partSpec) : subproductUtil;
	}

	private String getSubProductName() {
		return rule.getPartName().getSubProductType() == null ? null : rule.getPartName().getSubProductType(); 
	}

	private ProductCheckUtil getProductCheckUtil() {
		return productCheckUtil == null ? productCheckUtil = new ProductCheckUtil(subProduct, context.getProcessPoint()) : productCheckUtil;
	}

	public EngineUtil getEngineUtil() {
		return (engineUtil == null) ? engineUtil = new EngineUtil(installProcessPointId) : engineUtil;
	}

	private String getMainNo(String spec){
		return MbpnDef.MAIN_NO.getValue(spec);
	}

	public String getInstallProcessPointId() {
		return installProcessPointId;
	}

	public HeadlessDataCollectionContext getContext() {
		return context;
	}
}
