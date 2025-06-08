package com.honda.galc.service.recipe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.honda.galc.dao.product.MbpnDao;
import com.honda.galc.data.ProductType;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.entity.product.BaseProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.service.ServiceFactory;
import com.honda.galc.util.CommonUtil;

/**
 * 
 * <h3>RecipeDownloadMBPNInFrameService</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> RecipeDownloadMBPNInFrameService description </p>
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
 * <TD>R. Kowal</TD>
 * <TD>June 19, 2019</TD>
 * <TD>0.1</TD>
 * <TD>none</TD>
 * <TD>Initial Version</TD> 
 * </TR>  
 *
 * </TABLE>
 *    
 * @see
 * @version 0.1
 * @author Rich Kowal
 * @since June 19, 2019
 */

public class RecipeDownloadMBPNInFrameServiceImpl extends RecipeDownloadFrameServiceImpl implements RecipeDownloadMBPNInFrameService {

	protected void getLotControlRules(BaseProduct nextProduct) {
		if(null==nextProduct)
			return;
		//Check if the Rules already loaded. Do nothing if already loaded. 
		if(areRulesAlreadyLoadedFor(nextProduct)) 
		    return;

		super.getLotControlRules(nextProduct);
		
		if(rulesMap == null)
			rulesMap = new LinkedHashMap<String, List<LotControlRule>>();
		
		List<String> mbpnProcessPoints = CommonUtil.splitStringList(getPropertyBean().getRecipeMbpnProcessPointChildIds());
		if(mbpnProcessPoints == null || mbpnProcessPoints.isEmpty())
			return;
		
		List<String> consumerProcessPointIds = CommonUtil.splitStringList(getPropertyBean().getRecipeProcessPointIds());

		if (consumerProcessPointIds.isEmpty())
			getMbpnLotControlRules(mbpnProcessPoints, processPointId);
		else {
			for (String eachConsumerProcessPointId : consumerProcessPointIds) {
				getMbpnLotControlRules(mbpnProcessPoints, eachConsumerProcessPointId);
			}
		}
	}

	private void getMbpnLotControlRules(List<String> mbpnProcessPoints, String parentProcessPointId) {
		if(!rulesMap.containsKey(parentProcessPointId))
			return;
	
		List<Mbpn> mbpns = getMBPNProductSpecCodes(rulesMap.get(parentProcessPointId));
		
		for(Mbpn eachMBPN :mbpns) {
			for(String eachMBPNProcesPointId :mbpnProcessPoints) {
				if(!rulesMap.containsKey(eachMBPNProcesPointId)) {
					List<LotControlRule> processPointRules = LotControlRuleCache.getOrLoadLotControlRule(eachMBPN, eachMBPNProcesPointId);
					if(processPointRules != null && !processPointRules.isEmpty()) {
						rulesMap.put(eachMBPNProcesPointId, processPointRules);
						getMbpnLotControlRules(mbpnProcessPoints, eachMBPNProcesPointId);
					}
				}
			}
		}
	}	

	private List<Mbpn> getMBPNProductSpecCodes(List<LotControlRule> lotControlRules) {
		List<Mbpn> mbpns = new ArrayList<Mbpn>();

		for(LotControlRule eachLotControlRule : lotControlRules) {
			if (isMBPNSubProductType(eachLotControlRule)) {
				Mbpn mbpn = getMBPNProductSpecCode(eachLotControlRule);
				if(mbpn != null)
					mbpns.add(mbpn);
			}
		}
		return mbpns;
	}

	private boolean isMBPNSubProductType(LotControlRule lotControlRule) {
		return lotControlRule.getPartName().getSubProductType() != null
				&& lotControlRule.getPartName().getSubProductType().trim().equals(ProductType.MBPN.name());
	}


	private Mbpn getMBPNProductSpecCode(LotControlRule rule) {
		if (!isMBPNSubProductType(rule))
			return null;
		for (PartSpec eachPartSpec : rule.getParts()) {
			String possibleProductSpecCode = eachPartSpec.getPartNumber(); 
			if (!StringUtils.isEmpty(possibleProductSpecCode)) {
				Mbpn mbpn = ServiceFactory.getDao(MbpnDao.class).findByKey(possibleProductSpecCode.trim());
				if(mbpn != null)
					return mbpn;
			}
		}
		return null;
	}
	
	private boolean areRulesAlreadyLoadedFor(BaseProduct product) {
		return getPropertyBean().isOneRuleForSubIds() && rulesMap != null && rulesMap.size() > 0 &&
			productSpec != null && productSpec.getProductSpecCode().equals(product.getProductSpecCode());
	}

}
