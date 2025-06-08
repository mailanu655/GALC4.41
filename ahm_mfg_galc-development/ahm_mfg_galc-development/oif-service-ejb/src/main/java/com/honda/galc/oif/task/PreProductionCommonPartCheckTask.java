/**
 * 
 */
package com.honda.galc.oif.task;

import java.util.Arrays;
import java.util.List;

import com.honda.galc.dao.product.ExtRequiredPartSpecDao;
import com.honda.galc.dao.product.FrameSpecDao;
import com.honda.galc.dao.product.LotControlRuleDao;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.dao.product.PartNameDao;
import com.honda.galc.dao.product.RuleExclusionDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.entity.product.BaseProductSpec;
import com.honda.galc.entity.product.BaseProductSpecDao;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.RuleExclusion;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.service.utils.ProductTypeUtil;
import com.honda.galc.system.oif.svc.common.IEventTaskExecutable;
import com.honda.galc.util.ProductSpecUtil;

/**
 * @author VF031824
 *
 */
public class PreProductionCommonPartCheckTask extends OifTask<Object> 
implements IEventTaskExecutable {

	private List<String> base5 = null;

	public PreProductionCommonPartCheckTask(String name) {
		super(name);
	}

	public void execute(Object[] args) {
		logger.info("Beginning pre production common part check.");
		List<String> productTypes = ServiceFactory.getDao(PartNameDao.class).findAllProductTypesWithExtRequired();
		for(String productType : productTypes){
			checkCommonParts(productType.trim());
		}
		errorsCollector.sendEmail();
		logger.info("Completed pre production common part check.");
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private <T> void checkCommonParts(String productType) {

		List<T> activeFrameSpecs = null;
		boolean matchFound = false;

		if(!ProductTypeUtil.isMbpnProduct(productType)) {
			activeFrameSpecs = (List<T>) getActiveSpecs(productType);
		}

		List<String> extRequiredParts = ServiceFactory.getDao(ExtRequiredPartSpecDao.class).
				findDistinctActiveRequiredSpecsByProductType(productType);

		for(String requiredPart : extRequiredParts) {
			logger.info("Checking part: " + requiredPart);
			matchFound = false;
			List<LotControlRule> currentPartRules = ServiceFactory.getDao(LotControlRuleDao.class)
					.findAllByPartName(requiredPart);
			List<RuleExclusion> excludedRules = ServiceFactory.getDao(RuleExclusionDao.class).findAllByPartName(requiredPart);
			if(ProductTypeUtil.isMbpnProduct(productType)) {
				List<String> Base5 = getMbpnSpecs(requiredPart.trim());
				activeFrameSpecs= (List<T>) getActiveSpecs(productType);
			}

			for(T activeFrameSpec : activeFrameSpecs) {
				for(LotControlRule currentPartRule : currentPartRules) {
					matchFound = ProductSpecUtil.
							match(((BaseProductSpec) activeFrameSpec).getProductSpecCode(), currentPartRule.getId().getProductSpecCode(),
									ProductTypeUtil.isMbpnProduct(productType));
					if(matchFound){
						break;
					}
				}
				if(!matchFound){
					boolean isRuleExcluded = checkForExcludedRule(requiredPart, productType, excludedRules, activeFrameSpec);
					if(!isRuleExcluded) addMissingRule(requiredPart, activeFrameSpec);
				}
			}
		}		
	}

	private List<String> getMbpnSpecs(String requiredPart) {

		try{
			base5 = Arrays.asList(getProperty("Base5ForPart{" + requiredPart + "}").split(","));
		}catch(Exception e) {
			logger.error("No base 5 configured for " + requiredPart + " in OIF properties");
			errorsCollector.error("Missing configuration for MBPN Part " + requiredPart + ". Contact GALC team to fix.");
			return null;
		}
		return base5;
	}

	@SuppressWarnings({ "rawtypes", "unused" })
	private List<?> getActiveSpecs(String productType) {
		BaseProductSpecDao productSpecDao =	ProductTypeUtil.getProductSpecDao(productType);

		if(ProductTypeUtil.isMbpnProduct(productType))
			return ServiceFactory.getDao(MbpnDao.class).findAllByMainNo(base5);
		else
			return ServiceFactory.getDao(FrameSpecDao.class).findAllActiveProductSpecCodesOnly(productType);
	}

	private <T> boolean checkForExcludedRule(String requiredPart, String productType, 
			List<RuleExclusion> excludedRules, T activeFrameSpec) {
		List<RuleExclusion> rule = null;
		if(ProductTypeUtil.isMbpnProduct(productType)){
			rule =	ProductSpecUtil.getMatchedRuleList(((Mbpn) activeFrameSpec).getProductSpecCode(),
					ProductType.MBPN.name(), excludedRules, RuleExclusion.class);
		}else{
			rule =	ProductSpecUtil.getMatchedRuleList(((BaseProductSpec) activeFrameSpec).getProductSpecCode(),
					productType, excludedRules, RuleExclusion.class);	
		}
		if(rule.size() > 0) {
			return true;
		}
		return false;		
	}

	private <T> void addMissingRule(String requiredPart, T activeFrameSpec) {
		logger.info("Missing Rule for " + requiredPart.trim() + "   " +activeFrameSpec.toString());	
		errorsCollector.error("Missing Rule for " + requiredPart.trim() + "   " +activeFrameSpec.toString());
	}
}