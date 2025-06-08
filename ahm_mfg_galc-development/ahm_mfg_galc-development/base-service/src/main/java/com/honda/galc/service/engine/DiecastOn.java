package com.honda.galc.service.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.DiecastDao;
import com.honda.galc.dao.product.ProductTypeDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.ProductNumberDef;
import com.honda.galc.data.ProductNumberDef.TokenType;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.enumtype.DefectStatus;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.entity.product.ProductTypeData;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.DiecastUtil;
import com.honda.galc.service.utils.ProductTypeUtil;

/**
 * 
 * <h3>DiecastOn</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DiecastOn description </p>
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
 * <TD>Apr 27, 2012</TD>
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
 * @since Apr 27, 2012
 */
public class DiecastOn<T extends DieCast> extends DcMcOnBase implements DcOnService {
	public static final String department = "DC";
	public static final String TRUE_VALUE = "1";
	public static final String TRUE_WORD = "TRUE";
	public static final int MAX_ID_LENGTH = 17;
    protected boolean preheat;

    @Override
    public void collectData() {
    	try {
    		dcNumber = (String)context.get(TagNames.PRODUCT_ID.name());
    		preheat = parseBooleanFromContext(TagNames.PREHEAT.name(), false);	
    		barcodeGrade = (String)context.get(TagNames.BARCODE_READ_GRADE.name());

    		context.put(TagNames.VALID_DC_NUMBER.name(), false);
    		context.put(TagNames.VALID_CHECK_DIGIT.name(), false);
    		if(isValidDiecastIdNumber()){
    			if(isValidCheckDigit()){
    				if(saveDiecastInfo()){
    					context.put(TagNames.VALID_DC_NUMBER.name(), true);
    					context.put(TagNames.VALID_CHECK_DIGIT.name(), true);
    					processPreheat();
    					processBarCode();
    					checkForDieHold();
    				}
    			}
    			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_OK);
    		} else 
    			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);			
    		context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.COMPLETE);

		} catch(TaskException te){
			// populate needed tags for notification
			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);
			context.put(TagNames.EXCEPTION.name(),te.getMessage());
			
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().warn(te.getMessage(), this.getClass().getSimpleName());
		} catch (Exception e) {
			// populate needed tags for notification
			context.put(TagNames.MESSAGE.name(), LineSideContainerValue.CHECK_NG);
			context.put(TagNames.EXCEPTION.name(),e.getMessage());
			
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			getLogger().error(e, " Exception to execute ", this.getClass().getSimpleName());
		}
		
	}
	
	public boolean isValidDiecastIdNumber(){
		return getProperty().isValidateDcNumberFormat() ? 
			DiecastUtil.validateProductDcNumber(context.getProductType().name(),dcNumber,context,null) : true;
	}
	
	public boolean isValidCheckDigit() {
		ProductNumberDef def = DiecastUtil.getProductNumberDef(context.getProductType(), ProductNumberDef.NumberType.DC, dcNumber);
		if(def == null) {
			context.put(TagNames.MESSAGE.name(),LineSideContainerValue.CHECK_NG);
			throw new TaskException("No product number definition set up for type " + ProductNumberDef.NumberType.DC.name() + " ");
		}
		return def.isCheckDigit() ?
				DiecastUtil.validateCheckDigit(context.getProductType().name(),dcNumber,context,null) : true;
	}
	
	public void checkForDieHold() {
		if(isPreheat()) return;
		DiecastUtil.processDieHold(context.getProductType(), dcNumber);
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean saveDiecastInfo() {
		T diecast = (T)ProductTypeUtil.getProductDao(context.getProductType()).findBySn(dcNumber);
		if(diecast != null) {
			throw new TaskException(context.getProductType().name() + ":" + dcNumber + " already exists in database.");
		}
		
		diecast = (T)ProductTypeUtil.createProduct(context.getProductType().name(), dcNumber);
		diecast.setDcSerialNumber(dcNumber);
		diecast.setModelCode(getModelCode());
	
		if(isPreheat()){
			diecast.setDefectStatus(DefectStatus.PREHEAT_SCRAP);
			getLogger().info(context.getProductType().name(), ":", dcNumber, " is preheat.");
		}
		
		saveDiecast(diecast);
		context.setProduct(diecast);
		getLogger().info(context.getProductType().toString(), ":", dcNumber, " has been created, process point id:", context.getProcessPointId());
		
		return true;
		
	}
	
	public void saveDiecast(T diecast) {
		getDao().save(diecast);
	}
	
	public void preheatHeat() {
		List<ProductBuildResult> buildResults = new ArrayList<ProductBuildResult>();
		ProductBuildResult preheatResult = ProductTypeUtil.createBuildResult(context.getProductType().name(), dcNumber, "");
		preheatResult.setInstalledPartStatus(InstalledPartStatus.NG);
		preheatResult.setDefectStatus(DefectStatus.PREHEAT_SCRAP.getId());
		buildResults.add(preheatResult);
		qicsService.update(context.getProcessPointId(), context.getProduct(), buildResults);
	}
	
	@Override
	protected String getModelCode() {
		String modelCode = super.getModelCode();
		return modelCode != null ? modelCode : ProductNumberDef.DCB.getToken(TokenType.MODEL.name(), dcNumber);
	}
	
	
	public void processPreheat() {
		if(isPreheat())
			preheatHeat();
	}
	
	public boolean isPreheat() {
		
		return preheat || DiecastUtil.isDcNumberMarkedPreheat(dcNumber);
	}

	public boolean isCreateDefect(boolean barCodeStatus){
		return !isPreheat() && !barCodeStatus;
	}
	
	public String getDepartment(){
		return department;
	}

    protected boolean parseBooleanFromContext(String tagName, boolean defaultValue) {
    	boolean result = defaultValue;
    	if (StringUtils.isEmpty(tagName)) {
    		return result;
    	}
    	Object tagValue = context.get(tagName);
		if (tagValue != null) {
			String value = tagValue.toString().trim();
			result = Boolean.valueOf(TRUE_VALUE.equals(value) || TRUE_WORD.equalsIgnoreCase(value));
		}
    	return result;
    }
	
	@SuppressWarnings("unchecked")
	protected DiecastDao<T> getDao() {
		return (DiecastDao<T>) ProductTypeUtil.getProductDao(context.getProductType());
	}
	
	protected ProductTypeDao getProductTypeDataDao() {
		return ServiceFactory.getDao(ProductTypeDao.class);
	}
}
