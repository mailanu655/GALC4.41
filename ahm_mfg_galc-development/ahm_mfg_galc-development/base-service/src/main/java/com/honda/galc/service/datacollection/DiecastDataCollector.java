package com.honda.galc.service.datacollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.dao.product.ProductBuildResultDao;
import com.honda.galc.data.TagNames;
import com.honda.galc.entity.conf.Device;
import com.honda.galc.entity.enumtype.InstalledPartStatus;
import com.honda.galc.entity.product.DieCast;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;
import com.honda.galc.entity.product.ProductBuildResult;
import com.honda.galc.script.IScriptInterpreter;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.service.utils.QiHeadlessHelper;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;
import com.honda.galc.util.ProductResultUtil;

/**
 * 
 * <h3>DiecastDataCollector</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> DiecastDataCollector description </p>
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
 * <TD>May 11, 2011</TD>
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
 * @since May 11, 2011
 * @param <T>
 */

public class DiecastDataCollector extends ProductDataCollectorBase{
	
	protected IScriptInterpreter processor;
	protected String partName;
	protected List<ProductBuildResult> processedParts = new ArrayList<ProductBuildResult>();
	
	@Override
	protected void init(Device device){
		super.init(device);
		partName = getProperty().getOverallStatusPartName();
		context.setProductName(null);
		
		getLogger().info("start to collect data for ", partName, " at process point:", context.getProcessPointId());
		
	}
	
	public void collectData() {
		
		context.extractResults(rules);
		validate();

	}
	
	@Override
	public void loadLotControlRules() {
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
		}else {
			rules = getLotControlRuleFromCache(context.getProcessPointId());
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
		getLogger().info("Current Control Rules(" + rules.size(), "): ", rules.toString());
	}
	
	
	public void validate() {
		if(rules == null || rules.size() == 0) return;
		for(int idx = 0; idx < rules.size(); idx++){
			LotControlRule rule = rules.get(idx);
			ProductBuildResult headResult = getBuildResults().get(idx);
			
			// validate Part SN
			if(rule.isVerify()) 
				verifyPartSerialNumber(headResult, rule);
			
			if(rule.isUnique() && headResult.getInstalledPartStatus() == InstalledPartStatus.OK) 
				verifyPartSerialNumberUniq(headResult, rule);
			
		}
		
		if(getProperty().isIncludeRequiredPartCheckStatus() && isMissingRequiredPart()){
			for(ProductBuildResult b : context.getBuildResults()){
				if(getPartName().equals(b.getPartName())){
					b.setInstalledPartStatus(InstalledPartStatus.NG);
					getLogger().info("part:" + b.getPartName() + " status set to NG because missing required parts.");
				}
			}
		}
		
	}
	
	
	protected boolean verifyPartSerialNumber(ProductBuildResult buildResult, LotControlRule rule) {
		boolean result = false;
		if(buildResult.getResultValue() == null){
			getLogger().info("Failed to validate part serical number against lot control rule. Result Value is null, ", 
					"product id:", buildResult.getProductId(), " part name:", buildResult.getPartName());
			result = false;
		} else {
			currentPartSpec = CommonPartUtility.verify(buildResult.getResultValue(), rule.getParts(), PropertyService.getPartMaskWildcardFormat());
			getLogger().info("Part SN mask:", rule.getPartMasks());
			if(currentPartSpec != null){
				result = true;
			} else {
				result = false;
			}
		}
		
		if(buildResult.getInstalledPartStatus() == InstalledPartStatus.OK)
			buildResult.setInstalledPartStatus(result ? InstalledPartStatus.OK :InstalledPartStatus.NG);
		
		getLogger().info("Part SN:" + buildResult.getResultValue(), " mask check result:" +  result);
		
		return result;
		
	}

	protected boolean verifyPartSerialNumberUniq(ProductBuildResult productResult, LotControlRule rule) {

		boolean result = true;
		
		List<String> duplicatedList = new ArrayList<String>();
		
		//get all the parts with same partNumeer	
		List<String> partNames = LotControlPartUtil.getPartNamesOfSamePartNumber(currentPartSpec);
		List<? extends ProductBuildResult> list = context.getProductTypeUtil().getProductBuildResultDao().findAllByPartNameAndSerialNumber(partNames, productResult.getResultValue());
		updateDuplicateParts(productResult, duplicatedList, list);
		updateDuplicateParts(productResult, duplicatedList, processedParts, partNames);
		
		if(duplicatedList.size() > 0){
			
			getLogger().warn("Part name:", productResult.getPartName(), " part Sn:", productResult.getResultValue(),
					" is already installed on ", duplicatedList.toString());
			result = false;
			productResult.setInstalledPartStatus(InstalledPartStatus.NG);
		} else {
			getLogger().info("passed duplicated part check!");
		}
		
		processedParts.add(productResult);
		return result;
	}

	private void updateDuplicateParts(ProductBuildResult part, List<String> duplicatedList, List<? extends ProductBuildResult> list) {
		if(list == null) return;
		for(ProductBuildResult installedPart : list){
			if(installedPart == null || installedPart.getId() == null) continue;
			if(!installedPart.getProductId().equals(part.getProductId()) || !installedPart.getPartName().equals(part.getPartName()))
				duplicatedList.add(installedPart.getProductId());
		}
	}
	
	private void updateDuplicateParts(ProductBuildResult part, List<String> duplicatedList, List<? extends ProductBuildResult> list, List<String> partNames) {
		if(partNames.size() == 0) return;
		for(ProductBuildResult installedPart : list){
			if(installedPart == null || installedPart.getId() == null || !partNames.contains(installedPart.getPartName())) continue;
			if(!part.getPartSerialNumber().equals(installedPart.getPartSerialNumber()))  continue;
			if(!installedPart.getProductId().equals(part.getProductId()) || !installedPart.getPartName().equals(part.getPartName()))
				duplicatedList.add(installedPart.getProductId());
		}		
	}
	
	public void updateQics() {
		
		//Qics create defect on the overall status or on the part locations 
		//Should not on both
		if(getProperty().isCreatDefectOnOverallStatus())
		{
			//removes all other build results, i.e., there will be only one build result with overall defect status
			removeOtherBuildResultFromList(getPartName());
		} else  {
			//removes build result corresponding to overall status
			removeBuildResultFromList(getPartName());
		}
		
		prepareQicsUpdate();
		
		super.updateQics();
	}

	private void prepareQicsUpdate() {
		// Diecast part name is really the location
		for(ProductBuildResult result : getBuildResults()){
			result.setDefectLocation(result.getPartName());
		}		
	}


	/**
	 * Remove all of the other build results and keep on the result
	 * for input part name
	 * @param partName
	 */
	private void removeOtherBuildResultFromList(String partName) {
		ProductBuildResult partNameResult = null;
		for(ProductBuildResult b : getBuildResults()){
			if(b.getPartName().trim().equals(partName))  {
				partNameResult = b;
			}
		}
		
		context.getBuildResults().clear(); 
		if(partNameResult != null)  {
			context.getBuildResults().add(partNameResult);
		}
	}

	public ProductBuildResult removeBuildResultFromList(String partName) {
		//Remove the Part build results list
		ProductBuildResult partNameResult = null;
		for(ProductBuildResult b : getBuildResults()){
			if(b != null && b.getPartName().trim().equals(partName))  {
				partNameResult = b;
			}	
		}
		
		if(partNameResult != null)  {
			context.getBuildResults().remove(partNameResult);
		}
		return partNameResult;
	}

	public void saveBuildResults() {
		if(context.getBuildResults() == null || context.getBuildResults().size() == 0){
			getLogger().info("There is no build result to save for product:", context.getProductId());
			return;
		}
		List<ProductBuildResult> listOfBuildResults = context.getBuildResults();
		QiHeadlessHelper qiHelper = new QiHeadlessHelper(getProperty());

		if(qiHelper.isSendToNAQics())  {
			for(ProductBuildResult buildResult : listOfBuildResults)  {
				ProductBuildResultDao<? extends ProductBuildResult, ?> pbrDao = ProductTypeUtil.getProductBuildResultDao(context.getProductType());
				ProductBuildResult pbr = pbrDao.findById(buildResult.getProductId(),  buildResult.getPartName());
				if(pbr == null)  continue;
				buildResult.setDefectRefId(pbr.getDefectRefId());				
			}
		}
		ProductResultUtil.saveBuildResults(context.getProcessPointId(), context.getBuildResults());
	}
	
	public void saveBuildResult(ProductBuildResult result) {
		ProductResultUtil.saveBuildResult(context.getProcessPointId(), result);
	}
	
	
	public void track() {
		super.track();
	}
	

	public String getPartName() {
		return partName;
	}

	protected DieCast getDiecastProduct() {
		return(DieCast) context.getProduct();
		
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
	
	public boolean doQicsUpdate()  {
		boolean retStatus = super.doQicsUpdate();
		if(retStatus)  {
			//if the defect ref id is set, save it
			for(ProductBuildResult result : getBuildResults())  {
				if(result != null && result.getDefectRefId() > 0)  {
					try {
						ProductResultUtil.saveBuildResult(context.getProcessPointId(), result);
					} 	catch(Exception ex)  {
						getLogger().error("Unable to save defect ref id: " + result.toString());
						
					}
				}
			}
		}
		return retStatus;

	}
}