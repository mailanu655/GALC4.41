package com.honda.galc.service.datacollection;

import java.util.List;

import com.honda.galc.data.TagNames;
import com.honda.galc.datacollection.LotControlRuleCache;
import com.honda.galc.device.dataformat.DataCollectionComplete;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.Mbpn;

/**
 * 
 * <h3>MbpnDataCollector</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> MbpnDataCollector description </p>
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
 * <TD>Mar 18, 2014</TD>
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
 * @since Mar 18, 2014
 */
public class MbpnDataCollector extends ProductDataCollector {
	
	@Override
	protected List<LotControlRule> getLotControlRuleFromCache(String processPointId) {
		List<LotControlRule> allRules = null;
		try {
			allRules = LotControlRuleCache.getOrLoadLotControlRule(getProperty().isDeviceDriven() ? null : (Mbpn) context
							.getProductSpec(), processPointId);
		} catch (Exception e) {
			getLogger().error(e, " Excetpion to get Lot Control Rule on ", processPointId, " Product Spec:",
					context.getProductSpec().getProductSpecCode());
			context.put(TagNames.DATA_COLLECTION_COMPLETE, DataCollectionComplete.NG);
			return null;
		}
		return allRules;
	}

	public Mbpn getMbpn(){
		return (Mbpn) context.getProductSpec();
	}
	
}
