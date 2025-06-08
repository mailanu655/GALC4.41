package com.honda.galc.service.datacollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.dao.product.InstalledPartDao;
import com.honda.galc.dao.product.MeasurementAttemptDao;
import com.honda.galc.dao.product.MeasurementDao;
import com.honda.galc.data.LineSideContainerValue;
import com.honda.galc.data.MbpnDef;
import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.device.dataformat.PartSerialNumber;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.enumtype.MeasurementStatus;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.Measurement;
import com.honda.galc.entity.product.MeasurementId;
import com.honda.galc.entity.product.MeasurementSpec;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.property.HeadLessPropertyBean;
import com.honda.galc.property.SubproductPropertyBean;
import com.honda.galc.script.IScriptInterpreter;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.QiHeadlessHelper;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.LotControlRuleUtil;
import com.honda.galc.util.SubproductUtil;

/**
 * 
 * <h3>ProductDataCollector</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProductDataCollector description </p>
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
 * @author Paul Chou
 * Mar 21, 2011
 *
 * @param <T>
 */

public class ProductDataCollector extends ProductDataCollectorBase{
	
	protected IScriptInterpreter processor;
	List<String> scanParts=new ArrayList<String>();
	
	boolean duplicatePartsFlag;
	protected SubproductUtil subproductUtil;
	protected SubproductPropertyBean subProductProperty = null;
	protected List<InstalledPart> processedParts = new ArrayList<InstalledPart>();
	
	public void loadLotControlRules(){
		
		if(context.getProductSpec() == null)
			throw new TaskException("Failed to find product spec code.", this.getClass().getSimpleName());
		
		if(StringUtils.isEmpty(context.getProcessPointId()))
			throw new TaskException("Process Point Id is empty.", this.getClass().getSimpleName());
		
		if(getProperty().isInlineRepair() && !StringUtils.isEmpty(getProperty().getDefectProcessPointId())){
			String defectProcessPointId = getProperty().getDefectProcessPointId();
			List<LotControlRule> allRules = getLotControlRuleFromCache(defectProcessPointId);
			
			rules = new ArrayList<LotControlRule>();
			for(LotControlRule rule : allRules){
				if(getProperty().isInlineRepairQuickFix()){
					//for repair only part status is passed in, so remove measurements
					LotControlRuleId ruleId = rule.getId().clone();
					ruleId.setProcessPointId(context.getProcessPointId());
					LotControlRule aRule = new LotControlRule();
					aRule.setId(ruleId);

					rules.add(aRule);
				} else {
					rule.getId().setProcessPointId(defectProcessPointId);
					rules.add(rule);
				}
			}
			
		} else if(getProperty().isInlineRepair() && getProperty().isPdaRecipeDownload()){ //RecipeDownloadPDAService
			String defectProcessPointId = context.get(TagNames.PROCESS_POINT_ID.name()).toString().trim();
			List<LotControlRule> allRules = getLotControlRuleFromCache(defectProcessPointId);
			
			rules = new ArrayList<LotControlRule>();
			for(LotControlRule rule : allRules){
				rule.getId().setProcessPointId(defectProcessPointId);
				rules.add(rule);
			}
			filterUpdatePart();
		}else{
			rules = getLotControlRuleFromCache(context.getProcessPointId());
			
			if((rules == null) && getProperty().isDeviceDriven()){
				rules = LotControlRuleUtil.deduceLotControlRules(context.getDevice(), context.getProductType().toString(), getFirstProductName(), getLogger());
				LotControlRuleCache.addToCache(LotControlRuleCache.getKey(null, context.getProcessPointId()), rules);
				
			}
		}
		
		if(getProperty().isInlineRepair() && !StringUtils.isEmpty(getProperty().getRepairPartNames()))
			filterRepairPart();
		
		//check ignored parts
		if(getProperty().isSupportIgnoredParts()){
			//If lot control rules come from LotControlRuleCache and some rules are removed, the lot control rule cache is changed too.
			//Clone the rule list and make changes to the cloned rule list so that lot control rule cache isn't changed.
			rules = new ArrayList<LotControlRule>(rules);
			filterIgnoredParts();
		}
		
		context.setLotControlRules(rules);
		getLogger().info("Current Control Rules(" + (rules == null ? "null" : rules.size()), "): ", (rules == null ? "null" : rules.toString()));
	}

	private String getFirstProductName() {
		if(!StringUtils.isEmpty(getProperty().getProducts()))
			return getProducts().get(0);
		else 
			return null;
	}

	private void filterIgnoredParts() {
		String ignoredParts = (String) context.getDevice().getInputObjectValue(
				TagNames.IGNORED_PARTS.name());
		if (StringUtils.isBlank(ignoredParts)) {
			// No ignored parts in the request
			getLogger().debug("No ignored part defined.");
			return;
		}
		String[] parts = ignoredParts.split(",");
		Set<String> partSet = new HashSet<String>(parts.length);
		for (String part : parts) {
			partSet.add(part);
		}
		Iterator<LotControlRule> iterator = rules.iterator();
		while (iterator.hasNext()) {
			LotControlRule rule = iterator.next();
			if (rule != null && rule.getPartName() != null
					&& partSet.contains(rule.getPartNameString())) {
				// The Lot control rule is included in the ignored part list. so
				// remove the lot control rule
				iterator.remove();
				getLogger().debug("The lot control rule is ignored: "+rule);
			}
		}
	}

	private void filterRepairPart() {
		String[] names = getProperty().getRepairPartNames().split(",");
		List<String> partNameList = Arrays.asList(names);
		Iterator<LotControlRule> iterator = rules.iterator();
		
		while(iterator.hasNext()){
			if(!partNameList.contains(iterator.next().getPartNameString()))
				iterator.remove();
		}
	}

	
	@Override
	public void collectData(){
		
		context.extractResults(rules);
		logPerformance("collectData- after getBuildReulsts:");
			
		validate();
		
		for(ProductBuildResult b: getBuildResults()){
			if(b.getInstalledPartStatus() == InstalledPartStatus.REPAIRED){
				b.setInstalledPartStatus(InstalledPartStatus.OK);
				((InstalledPart)b).setInstalledPartReason(InstalledPartStatus.REPAIRED.name().toLowerCase());
			}
			if(getProperty().isInlineRepair() && getProperty().isPdaRecipeDownload() &&
					b.getInstalledPartStatus() == InstalledPartStatus.OK)
			{
				((InstalledPart)b).setInstalledPartReason(getProperty().getRepairReason());
			}
		}
		
		logPerformance("collectData- after validate:");
		
	}
	
	public void validate() {
		
		if(!getProperty().isLotControl()){  
			context.put(TagNames.OVERALL_STATUS.name(), updateStatusForInstalledParts());
			return;
		}
		
		if(getLotControlRules() == null || getLotControlRules().size() == 0) return;
		
		int overalStatus = 1;
		processedParts.clear();
		for(int idx = 0; idx < rules.size(); idx++){
			currentPartSpec = null;
			LotControlRule rule = rules.get(idx);
			String partName = rules.get(idx).getId().getPartName();
			InstalledPart part = getInstalledPart(partName);
			if(part == null) {
				if (getProperty().isInlineRepair()) {
					continue;
				} else {
					throw new TaskException("Failed to validate data collection for part " + partName, this.getClass().getSimpleName());
				}
			}
			if(rule.getParts() != null && rule.getParts().size() > 0) {
				currentPartSpec = rule.getParts().get(0);
			}
			if (null != currentPartSpec && StringUtils.isEmpty(part.getPartId()))
			{
				part.setPartId(currentPartSpec.getId().getPartId());
			}
			
			if(!getProperty().isValidateBuildResults()){
				updateInstalledPartStatus(part);
				continue;
			}
		
			duplicatePartsFlag=false;
			// validate Part SN
			if(rule.isVerify()){ 
				boolean verifyPartSerialNumber = verifyPartSerialNumber(part, rule);
				if(duplicatePartsFlag){
					putErrorMessage("Same Part "+part.getPartSerialNumber()+" scanned more than once.");
				}
				else if(!verifyPartSerialNumber){
					putErrorMessage("Invalid part serial number "+part.getPartSerialNumber());
				}
			}else {
				currentPartSpec = (rule.getParts() != null && rule.getParts().size() > 0) ? rule.getParts().get(0) : null;
				part.setValidPartSerialNumber(true);
			}
			
			if(null != currentPartSpec && rule.isVerify()){
				//Validate the Subproduct if part is subproduct
				subproductUtil = new SubproductUtil(new PartSerialNumber(part.getPartSerialNumber()), rule, currentPartSpec);
				subProductProperty = PropertyService.getPropertyBean(SubproductPropertyBean.class, context.getProcessPointId());
				validateSubproduct(rule, part);			
			}
			if(rule.isUnique() && part.getInstalledPartStatus() == InstalledPartStatus.OK){ 
				boolean verifyPartSerialNumberUniq = verifyPartSerialNumberUniq(part, rule);
				if(!verifyPartSerialNumberUniq){
					putErrorMessage("Duplicate part SN.");
				}
			}
			
			if(getProperty().isIncludeRequiredPartCheckStatus() && isMissingRequiredPart()){
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				getLogger().info("part:" + part.getPartName() + " status set to NG because missing required parts.");
			}
			
			try {
				validateMeasurements(part);
			} catch (Exception e) {
				getLogger().error(e, "Exception to validate measurement for product:", context.getProduct().getProductId());
			}
			
			//chek measurement max attempt
			try{
				if(getProperty().isCheckMeasurementMaxAttempts()){
					validateMeasurementMaxAttempts(part);
				}
			} catch (Exception e){
				getLogger().error(e, "Exception to validate measurement max attempts for product:", context.getProduct().getProductId());
			}			
			overalStatus = part.getInstalledPartStatusId() == 0 ? 0 : overalStatus;
			
		}
		
		context.put(TagNames.JUDGEMENT.name(), overalStatus);
		context.put(TagNames.OVERALL_STATUS.name(), overalStatus);
		
	}

	/**
	 * Validate and confirm the correct Subproduct
	 * Perform ProductCheck for the Subproduct if any
	 * @param rule
	 * @param part
	 */
	private void validateSubproduct(LotControlRule rule, InstalledPart part) {
		if (subproductUtil.isPartSubproduct()) {
			BaseProduct subProduct = subproductUtil.findSubproduct();
			String installProcessPoint="";
			if (subproductUtil.findSubproduct() == null) {
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				getLogger().info("part:" + part.getPartName() + " status set to NG because invalid Subproduct");
				putErrorMessage("part:" + part.getPartName() + " status set to NG because invalid Subproduct");
			}

			if (!subproductUtil.isValidSpecCode(rule.getPartName().getSubProductType(), subProduct, context.getProductSpecCode())) {
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				getLogger().info("part:" + part.getPartName() + " status set to NG because Spec Code of Subproduct does not match expected Spec Code.");
				putErrorMessage("part:" + part.getPartName() + " status set to NG because Spec Code of Subproduct does not match expected Spec Code.");
			}else {
				if(null != subproductUtil && null != subproductUtil.getMatchedPartSpec()) {
					currentPartSpec = subproductUtil.getMatchedPartSpec();
					part.setPartId(currentPartSpec.getId().getPartId());
				}
			}
			
			try {				
				try{
					if(!subProductProperty.isUseMainNoFromPartSpec())
						installProcessPoint =subProductProperty.getInstallProcessPointMap().get(rule.getPartName().getSubProductType());
					else{
						installProcessPoint =subProductProperty.getInstallProcessPointMap().get(getMainNo(subProduct.getProductSpecCode().trim()));
					}
				}catch(Exception e){
					installProcessPoint = "";
					getLogger().info("Property not define for INSTALL_PROCESS_POINT_MAP");
				}
				List<String> failedProductCheckList = subproductUtil.performSubProductChecks(rule.getPartName().getSubProductType(), subProduct, installProcessPoint);
				if(failedProductCheckList.size() > 0) {
					StringBuffer msg = new StringBuffer();
					msg.append(subProduct.getProductId() + " failed the following Product Checks : ");
					for (int i = 0; i < failedProductCheckList.size(); i++) {
						msg.append(failedProductCheckList.get(i));
						if (i != failedProductCheckList.size() - 1) {
							msg.append(", ");
						}
					}
					getLogger().info(msg.toString());
					part.setInstalledPartStatus(InstalledPartStatus.NG);
					putErrorMessage(msg.toString());
				}
			} catch (Exception e) {
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				getLogger().info("part:" + part.getPartName() + " status set to NG because "+e.getMessage());
			}
			try {
				subproductUtil.performSubproductTracking(rule.getPartName().getSubProductType(), subProduct, installProcessPoint, context.getProcessPointId());
			} catch (Exception e) {
				Logger.getLogger().info("Could not perform tracking on subproduct.");
			}
		}		
	}

	private void validateMeasurements(InstalledPart part) {
		if(currentPartSpec != null && currentPartSpec.getMeasurementCount() > 0 &&  
				currentPartSpec.getMeasurementSpecs().size() > 0)
			validateAgainstMeasurementSpec(part);
		else
			updateInstalledPartStatus(part);		
	}


	private void validateAgainstMeasurementSpec(InstalledPart part) {
		//OK - to specify how many measurement data to collect without specify range of each measurement.
		if(currentPartSpec.getMeasurementSpecs() == null || currentPartSpec.getMeasurementSpecs().size() == 0)
			return;
		
		for(int i = 0; i < currentPartSpec.getMeasurementCount(); i++){
			MeasurementSpec spec = currentPartSpec.getMeasurementSpecs().get(i);
			Measurement measurement = part.getMeasurements().get(i);
			if(measurement.getMeasurementStatus() == MeasurementStatus.ERR){
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				continue;
			}
			
			if(measurement.getMeasurementValue() > spec.getMaximumLimit() ||
					measurement.getMeasurementValue() < spec.getMinimumLimit()){
				
				measurement.setMeasurementStatus(MeasurementStatus.NG);
				part.setInstalledPartStatus(InstalledPartStatus.NG);
			    
				putErrorMessage("Measurment index: "+i+" is not within range.");
				getLogger().info("Measurment index:" + i, " value:" + measurement.getMeasurementValue(), 
					" is not within range:[" +spec.getMinimumLimit(),"," +spec.getMaximumLimit(),"] ", info());
			}
		}
		
		updateInstalledPartStatus(part);
		
	}
	
	private void validateMeasurementMaxAttempts(InstalledPart part){
		if(currentPartSpec.getMeasurementSpecs() == null || currentPartSpec.getMeasurementSpecs().size() == 0)
			return;
		for(int i = 0; i < currentPartSpec.getMeasurementCount(); i++){
			MeasurementSpec spec = currentPartSpec.getMeasurementSpecs().get(i);
			Measurement measurement = part.getMeasurements().get(i);
			
			MeasurementAttemptDao dao = ServiceFactory.getDao(MeasurementAttemptDao.class);
			int maxAttemptForMeasurement = dao.getMaxAttemptForMeasurement(measurement.getId().getProductId(), measurement.getId().getPartName(), measurement.getId().getMeasurementSequenceNumber());
			if(maxAttemptForMeasurement>spec.getMaxAttempts()){
				
				measurement.setMeasurementStatus(MeasurementStatus.NG);
				part.setInstalledPartStatus(InstalledPartStatus.NG);
				
				putErrorMessage("Reached the Measurement max attempts");
				getLogger().info("Measurment index:" + i, " measurement attempt:" + maxAttemptForMeasurement, 
						" is reached the max attempts :[" +spec.getMaxAttempts()+"] ", info());
			}
		}
		
		updateInstalledPartStatus(part);
		
	}

	private boolean verifyPartSerialNumber(InstalledPart part, LotControlRule rule) {
		boolean result = false;
		if(part.getPartSerialNumber() == null){
			getLogger().info("Part Serial Number is null, ", info(), ":", part.getId().getPartName());
			result = false;
		} else {
			HeadLessPropertyBean propertyBean = PropertyService.getPropertyBean(HeadLessPropertyBean.class,rule.getId().getProcessPointId());
			
			currentPartSpec = CommonPartUtility.verify(part.getPartSerialNumber(), rule.getParts(),
					PropertyService.getPartMaskWildcardFormat(), rule.isDateScan(), propertyBean.getExpirationDays(), 
					context.getProduct(), propertyBean.isUseParsedDataCheckPartMask());
			
			getLogger().info("Part SN mask:", rule.getPartMasks());
			if(currentPartSpec != null){
				String parsePartSn = CommonPartUtility.parsePartSerialNumber(currentPartSpec,part.getPartSerialNumber());
				if (parsePartSn != null) part.setPartSerialNumber(parsePartSn);
				part.setPartId(currentPartSpec.getId().getPartId());
				result = true;
				//if VERIFY_DUPLICATE_PARTS TRUE - verify if same part scanned mupliple times
				boolean verifyDuplicatePartScan = false;
				if(getProperty().isVerifyDuplicateParts()){
					verifyDuplicatePartScan = verifyDuplicatePartScan(part,scanParts);
					scanParts.add(part.getPartSerialNumber());
					if(verifyDuplicatePartScan){
						duplicatePartsFlag=true;
						result=false;
					}
				}				
			} else {
				result = false;
			}
		}
		
		//We never update a NG status from Equipment 
		if(part.getInstalledPartStatus() != InstalledPartStatus.NG)
			part.setInstalledPartStatus(result ? InstalledPartStatus.OK :InstalledPartStatus.NG);
		
		part.setValidPartSerialNumber(result);
		if(duplicatePartsFlag){
			getLogger().info("Duplicate Part SN:" + part.getPartSerialNumber(), " scan check result:" +  result);
		}else getLogger().info("Part SN:" + part.getPartSerialNumber(), " mask check result:" +  result);
		return result;
		
	}
	

	private boolean verifyPartSerialNumberUniq(InstalledPart part,	LotControlRule rule) {
		boolean result = true;
		List<String> duplicatedList = new ArrayList<String>();
		
		//set valid part sn to false if blank sn was received.
		if(StringUtils.isEmpty(part.getPartSerialNumber())){
			part.setValidPartSerialNumber(false);
			part.setInstalledPartStatus(InstalledPartStatus.NG);
			getLogger().warn("Part name:", part.getId().getPartName(), " Error:blank part serial number was received for unique check!");
			return false;
		}
		
		List<String> partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(currentPartSpec);
		List<InstalledPart> list = getInstalledPartDao().findAllByPartNameAndSerialNumber(partNames, part.getPartSerialNumber());
		updateDuplicateParts(part, duplicatedList, list);
		updateDuplicateParts(part, duplicatedList,processedParts, partNames);
		
		
		if(duplicatedList.size() > 0){
			
			getLogger().warn("Part name:", part.getId().getPartName(), " part Sn:", part.getPartSerialNumber(),
					" is already installed on ", duplicatedList.toString());
			result = false;
			part.setInstalledPartStatus(InstalledPartStatus.NG);
			part.setValidPartSerialNumber(false);
			if(getProperty().isReturnDuplicatedProductId()){
				context.put(TagNames.DUPLICATED_PRODUCT_ID.name(), duplicatedList);
			}
		} else {
			getLogger().info("passed duplicated part check!");
		}
		
		processedParts.add(part);
		
		return result;
		
	}

	private void updateDuplicateParts(InstalledPart part, List<String> duplicatedList, List<InstalledPart> list, List<String> partNames) {
		if(partNames.size() == 0) return;
		for(InstalledPart installedPart : list){
			if(installedPart == null || installedPart.getId() == null || !partNames.contains(installedPart.getPartName())) continue;
			if(!part.getPartSerialNumber().equals(installedPart.getPartSerialNumber()))  continue;
			if(!installedPart.getId().getProductId().equals(part.getId().getProductId()))
				duplicatedList.add(installedPart.getId().getProductId());
			else if(!installedPart.getId().getPartName().equals(part.getId().getPartName())) 
				duplicatedList.add(installedPart.getId().getProductId());
		}
		
	}

	private void updateDuplicateParts(InstalledPart part, List<String> duplicatedList, List<InstalledPart> list) {
		if(list == null) return;
		for(InstalledPart installedPart : list){
			if(installedPart == null || installedPart.getId() == null) continue;
			if(!installedPart.getId().getProductId().equals(part.getId().getProductId()))
				duplicatedList.add(installedPart.getId().getProductId());
			else if(!installedPart.getId().getPartName().equals(part.getId().getPartName())) 
				duplicatedList.add(installedPart.getId().getProductId());
		}
	}
	
	//Validate Duplicate Part Scan
	private boolean verifyDuplicatePartScan(InstalledPart part, List<String> scanParts) {
		boolean result = false;
		if(part.getPartSerialNumber() == null){
			getLogger().info("Part Serial Number is null, ", info(), ":", part.getId().getPartName());
			result = false;
		} else {
			if(scanParts.contains(part.getPartSerialNumber())) result=true;
			else result=false;
		}
		
		//We never update a NG status from Equipment 
		if(part.getInstalledPartStatus() != InstalledPartStatus.NG)
			part.setInstalledPartStatus(!result ? InstalledPartStatus.OK :InstalledPartStatus.NG);
		
		part.setValidPartSerialNumber(!result);
		return result;		
	}
	
	
	private String info() {
		StringBuilder sb = new StringBuilder();
		sb.append(context.getProduct().getId()).append(":").append(context.getProcessPointId());
		return sb.toString();
	}
	

	@Override
	public void saveBuildResults() {
		
		if(getBuildResults() == null || getBuildResults().size() == 0){
			getLogger().info("There is no build result to save for product:", context.getProductId());
			return;
		}
		
		//remove part place holders
		List<ProductBuildResult> emptyParts = new ArrayList<ProductBuildResult>();
		for(ProductBuildResult part : getBuildResults())
			if(!((InstalledPart)part).hasData()) emptyParts.add(part);
		
		if(emptyParts.size() > 0) //missing data for some parts 
			context.put(TagNames.DATA_COLLECTION_COMPLETE.name(), LineSideContainerValue.NOT_COMPLETE);
			
		getBuildResults().removeAll(emptyParts);
		
		List<InstalledPart> installedParts = getInstalledParts();
		QiHeadlessHelper qiHelper = new QiHeadlessHelper(getProperty());
		if(qiHelper.isSendToNAQics() && installedParts != null && !installedParts.isEmpty())  {
			for(InstalledPart iPart : installedParts)  {
				InstalledPart p = ServiceFactory.getDao(InstalledPartDao.class).findById(iPart.getProductId(),  iPart.getPartName());
				if(p == null)  continue;
				iPart.setDefectRefId(p.getDefectRefId());
				
		   		if(iPart.getMeasurements() != null && !iPart.getMeasurements().isEmpty()) {
		   			for(Measurement meas : iPart.getMeasurements())  {
		   				Measurement m = ServiceFactory.getDao(MeasurementDao.class).findByKey(meas.getId());
		   				if(m == null)    continue;
		   				meas.setDefectRefId(m.getDefectRefId());
		   			}
				}
				
			}
		}
		saveAllInstalledParts(installedParts);
		
		getLogger().info("save build results:", getBulidResultLogString(getBuildResults()));
	}

	
	private String getMainNo(String spec){
		return MbpnDef.MAIN_NO.getValue(spec);
	}
}

