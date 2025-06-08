package com.honda.galc.service.engine;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.TagNames;
import com.honda.galc.data.ProductNumberDef.NumberType;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.DiecastUtil;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.util.CommonPartUtility;
/**
 * 
 * <h3>MachiningOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MachiningOn description </p>
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
 * <TD>Jun 27, 2012</TD>
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
 * @since Jun 27, 2012
 */
public class MachiningOn<T extends DieCast> extends DcMcOnBase implements McOnService{

	public final static String department = "MC";
	protected String mcNumber;
    protected boolean isHomeProduct = true;
	
	public void collectData() {
		try {
			dcNumber = (String)context.get(TagNames.PRODUCT_ID.name());
			mcNumber = (String)context.get(TagNames.MC_NUMBER.name());
			barcodeGrade = (String)context.get(TagNames.BARCODE_READ_GRADE.name());
			context.setProduct(getDao().findBySn(dcNumber));
			
			if(!isInputValid()) {
				context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
				context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);
				throw new TaskException("Invalid input data.");
			}
				
			
		    associateDcMcNumber();
		    processBarCode();
			
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);
			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_OK);

		} catch(TaskException te){
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);
			context.put(TagNames.EXCEPTION.name(), te.getMessage());
			getLogger().warn(te.getMessage(), " ", this.getClass().getSimpleName());
		} catch (Exception e) {
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);
			context.put(TagNames.EXCEPTION.name(),e.getMessage());
			getLogger().error(e, " Exception to execute ", this.getClass().getSimpleName());
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void associateDcMcNumber() {
		T diecast = (T)context.getProduct();
		if(diecast != null){
			if (diecast.getMcSerialNumber() != null && diecast.getMcSerialNumber().trim().length() > 0 && !getProperty().isAllowOverwriteMc()) {
				throw new TaskException(context.getProductType().toString() + " DC:" + dcNumber + " MC:" + mcNumber + ", existing MC:" + diecast.getMcSerialNumber());
			} else {
				if (diecast.getMcSerialNumber() != null) {
					getLogger().info(context.getProductType().toString(), " DC: " + dcNumber + "Existing MC: ", diecast.getMcSerialNumber()+ " replaced by New MC:",mcNumber);
				}
				diecast.setMcSerialNumber(mcNumber);
				diecast.setDunnage(null);
				String modelCode = super.getModelCode(); //Plc input
				if(!StringUtils.isEmpty(modelCode)) diecast.setModelCode(modelCode);
				getDao().save(diecast);
			}
		} else {
			if(isHomeProduct)
				new TaskException("DCB:" + dcNumber + " MCB:" + mcNumber + ", DCH does not exist in database.");
			
			diecast = (T)ProductTypeUtil.createProduct(context.getProductType().getProductName(), dcNumber);
			diecast.setModelCode(getModelCode());
			diecast.setMcSerialNumber(mcNumber);
			context.setProduct(diecast);
			getDao().save(diecast);
			
		}
	}
	
	@Override
	protected String getModelCode() {
		// take model code from MC number if not input from PLC 
		String modelCode = super.getModelCode();
		if (StringUtils.isNotBlank(modelCode)) {
			return modelCode;
		}
		ProductNumberDef pnd = DiecastUtil.getProductNumberDef(context.getProductType(), NumberType.MC, mcNumber);
		if (pnd == null) {
			throw new TaskException(String.format("There is no ProductNumberDef defined for number type MC,  product type %s", context.getProductType()));			
		}
		return pnd.getToken(TokenType.MODEL.name(), mcNumber); 
	}

	protected boolean validateHomePlantNumber() {
		boolean validDc = getDao().findByKey(dcNumber) != null;
		if(!validDc) context.put(TagNames.EXCEPTION.name(), context.getProductType() + " dc:" + dcNumber + " does not exist.");
		return validDc;
	}
	
	protected boolean isCreateDefect(boolean barCodeStatus){
		return !barCodeStatus;
	}
	
	public boolean isInputValid() {
		
		boolean validDc = true;
		boolean mcMask = true;
		boolean validCheckDigit = true;

		if(getProperty().isValidateDcNumberFormat()) {		
			validDc = DiecastUtil.validateProductDcNumber(context.getProductType().name(), dcNumber, context, null) && checkDcNumber();
			context.put(TagNames.VALID_DC_NUMBER.name(), validDc);
			logContext();
		}
		
		if(getProperty().isValidateMcNumberFormat()) {
			mcMask = DiecastUtil.validateProductMcNumber(context.getProductType().name(), mcNumber, context, null) && checkMcNumberMask();
			context.put(TagNames.VALID_MC_NUMBER.name(), mcMask);
			logContext();
		}
		
		if(!isValidCheckDigit()) {
			validCheckDigit = false;
			 context.put(TagNames.VALID_CHECK_DIGIT.name(), validCheckDigit);
			 logContext();
		}
		
		return validDc && mcMask && validCheckDigit;
	}
	
	protected boolean checkDcNumber() {
		isHomeProduct = context.isHomeProduct(dcNumber);
		getLogger().info(dcNumber, (isHomeProduct ?  " is from home plant." : " is from other plant."));
		return isHomeProduct ?  validateHomePlantNumber() : true;
	}	
	
	public boolean checkMcNumberMask() {
		String maskStr = getMaskProperty();
		String[] masks = maskStr == null ? null : maskStr.split(",");
		if(masks == null) return true;
		
		for(String token : masks)
			if(CommonPartUtility.verification(mcNumber, token, PropertyService.getPartMaskWildcardFormat())) return true;
		
		return false;
	}
	
	public boolean isValidCheckDigit() {
		ProductNumberDef def = DiecastUtil.getProductNumberDef(context.getProductType(), ProductNumberDef.NumberType.MC, mcNumber);
		if(def == null) {
			context.put(TagNames.MESSAGE.name(),LineSideContainerValue.CHECK_NG);
			throw new TaskException("No product number definition set up for type " + ProductNumberDef.NumberType.MC.name() + " ");
		}
		return def.isCheckDigit() ?
				DiecastUtil.validateCheckDigit(context.getProductType().name(),mcNumber,context,null) : true;
	}

	public String getDepartment(){
		return department;
	}

	@SuppressWarnings("unchecked")
	protected DiecastDao<T> getDao() {
		return (DiecastDao<T>) ProductTypeUtil.getProductDao(context.getProductType());
	}

	protected String getMaskProperty() {
		return PropertyService.getProperty(context.getProcessPointId(), "MC_NUMBER_MASK");
	}
}
