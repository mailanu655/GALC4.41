package com.honda.galc.client.datacollection.control.headless;

import java.util.ArrayList;
import java.util.List;

import com.honda.galc.client.datacollection.ClientContext;
import com.honda.galc.common.exception.TaskException;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.data.ProductType;
import com.honda.galc.data.cache.PersistentCache;
import com.honda.galc.entity.product.InstalledPart;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.PartSpec;
import com.honda.galc.entity.product.ProductSpec;
import com.honda.galc.service.property.PropertyService;
import com.honda.galc.util.CommonPartUtility;
import com.honda.galc.util.LotControlPartUtil;

/**
 * 
 * <h3>LotControlRuleValidator</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HeadLessLotControlRuleValidator description </p>
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
 * Mar 8, 2011
 *
 */

public class LotControlRuleManager extends PersistentCache{
	private ClientContext context;
    private String processPointId;
    private String productSpecCode;
    private LotControlRule currentRule;
    private List<LotControlRule> rules;
    private PartSpec partSpec;
    
    public LotControlRuleManager(ClientContext context, String processPointId) {
    	this.context = context;
    	this.processPointId = processPointId;
    	loadAllRules();
    }

	public boolean validate(String productId,  String productSpecCode, 
    		String partName, String partSerialNumber) {
    	
        getRulesForProductSpec(productSpecCode);
        
        boolean result = true;
        
        //If can not find Lot control rule for any reason, take the part serial number as OK
        if(rules.size() == 0){ 
        	Logger.getLogger().warn("No Lot Control Rule defined for ", processPointId, 
        			productSpecCode, " ", partName, ":", partSerialNumber, " is processed as valid.");
            return true;
        }
        
        currentRule = findLotControlRuleForPart(partName);
        if(currentRule == null)
        {
            Logger.getLogger().warn("Can't find lot control rule for part:" + partName,
            		" so ", partSerialNumber, " is processed as valid.");
            return true;
        }
        
        if(currentRule.isVerify())
            result = result & verifyPartSerialNumberMask(partSerialNumber);
        if(currentRule.isUnique())
            result = result & verifyPartSerialNumberUniq(productId, partName, partSerialNumber);
        
        return result;
    }

    private LotControlRule findLotControlRuleForPart(String partName) {
        for(LotControlRule rule : rules){
            if(partName.trim().equals(rule.getPartName().getPartName()))
                return rule;
        }
        return null;
    }

    private boolean verifyPartSerialNumberUniq( String productId, String partName, String partSerialNumber) {
    	if(context.isOnLine()){
    		List<InstalledPart> duplicatedParts = context.getDbManager().findDuplicatePartsByPartName(partName, partSerialNumber);

    		for(InstalledPart part: duplicatedParts){
    			if(productId.equals(part.getId().getProductId())){
    				String msg = partName + " " + partSerialNumber + " is already installed on " + part.getId().getProductId();
    				Logger.getLogger().warn(msg);
    				throw new TaskException(msg);
    			}
    		}
    	}
    	return true;
    }
    
    private boolean verifyPartSerialNumberMask(String partSerialNumber){
    	
    		List<PartSpec> parts = currentRule.getParts();
    		PartSpec result = CommonPartUtility.verify(partSerialNumber, parts, PropertyService.getPartMaskWildcardFormat());
    		
    		if(result == null){
    			String msg = "Invalid Part Serial number:" + partSerialNumber + " mask:" + getPartMasks(parts);
    			Logger.getLogger().warn(msg);
    			throw new TaskException(msg);
    		} else {
    			partSpec = result;
    			return true;
    		}
    }
  
    private List<String> getPartMasks(List<PartSpec> parts) {
		ArrayList<String> list = new ArrayList<String>();
		for(PartSpec spec : parts){
			list.add(spec.getPartSerialNumberMask());
		}
		return list;
	}

	private void getRulesForProductSpec(String productSpecCode){
       
        if(processPointId == null)
        {
            String msg = "Error: Configuration error. Process point is null.";
            Logger.getLogger().error(msg);
            throw new TaskException(msg);
        }
        
        if(!productSpecCode.equals(this.productSpecCode)){
        	rules = LotControlPartUtil.getLotControlRuleByProductSpec(findProductSpec(productSpecCode), getAllRules());
        	this.productSpecCode = productSpecCode;
        } 
        
    }


	private ProductSpec findProductSpec(String spec) {
		if(context.getProperty().getProductType().equals(ProductType.ENGINE.toString()))
			return ProductSpec.findProductSpec(spec, context.getEngineSpecs());		
		else if(context.getProperty().getProductType().equals(ProductType.FRAME.toString()))
			return context.getFrameSpec(spec);
		else 
			Logger.getLogger().error("Product type:", context.getProperty().getProductType(), " is not support.");


		throw new TaskException("Invalid product spec code:" + productSpecCode );
	}

	private List<LotControlRule> getAllRules() {
		return getList("LotControlRules",LotControlRule.class);
	}

	private void loadAllRules() {
		List<LotControlRule> allRules =  context.getDbManager().loadLotControlRules(processPointId);

		if(allRules != null) {
			put("LotControlRules",allRules);
		}
	}

	public PartSpec getPartSpec() {
		return partSpec;
	}

	public void setPartSpec(PartSpec partSpec) {
		this.partSpec = partSpec;
	}
}
