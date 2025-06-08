package com.honda.galc.test.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.honda.galc.dao.product.EngineDao;
import com.honda.galc.dao.product.InProcessProductDao;
import com.honda.galc.entity.product.Engine;
import com.honda.galc.entity.product.InProcessProduct;
import com.honda.galc.entity.product.LotControlRule;
import com.honda.galc.entity.product.LotControlRuleId;

import static com.honda.galc.service.ServiceFactory.getDao;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>ProductionManager</code> is ...
 * </p>
 * <h4>Usage and Example</h4>
 * <h4>Special Notes</h4>
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
 * <TD>Jeffray Huang</TD>
 * <TD>Mar 10, 2009</TD>
 * <TD>0.1</TD>
 * <TD>(none)</TD>
 * <TD>Initial Realse</TD>
 * </TR>
 * </TABLE>
 * 
 * @see
 * @ver 0.1
 * @author Jeffray Huang
 */

public class ProductionManager {
    
    public static ProductionManager productionManager;
    
    private List<LotControlRule> rules;
    
    public static ProductionManager getInstance() {
        if(productionManager == null) productionManager = new ProductionManager();
        return productionManager;
    }
    
    
    public ProductionManager() {
        
        
    }
    
    /**
     * get last processed engine in current process point
     * This applies to any data collection process points
     * @return
     */
    
    public String getLastProcessedProductId() {
         
        
        List<LotControlRule> currentRules = this.getLotControlRule();
        if(currentRules.isEmpty()) return null;
        
        return getLastProcessedProduct(currentRules.get(0).getId().getPartName());
        
    }
    
    @SuppressWarnings("unchecked")
    private String getLastProcessedProduct(String partName) {
        Vector vector = new Vector();
        vector.add(partName);
        String productId = (String)QueryHelper.getInstance().querySingleResult("TEST_SEL001", vector);
        if(productId == null) return null;
        else return productId.trim();
    }
    
    /**
     * get last processed Product Id
     * @param ppId process point Id
     * @return
     */
    
    public String getLastProcessedProductId(String ppId) {
    	List<LotControlRule> currentRules = this.getLotControlRule(ppId);
        if(currentRules.isEmpty()) return null;
        
        return getLastProcessedProduct(currentRules.get(0).getId().getPartName());
	}
    

	/**
     * get next expected production Id to be processed for the current process point id
     * @return product Id
     */
    
    public String getExpectedProductId() {
        
        String productId = this.getLastProcessedProductId();
        if(productId == null) return null;
        
        InProcessProduct product = getDao(InProcessProductDao.class).findByKey(productId);
        
        if(product == null) return null;
        return product.getNextProductId().trim();
        
    }
    
    /**
     * get next expected product Id to be processed for a process point id
     * @param ppId process point Id
     * @return
     */
    
    public String getExpectedProductId(String ppId) {
        
        String productId = this.getLastProcessedProductId(ppId);
        System.out.println("-----productId " + productId);
        if(productId == null) return null;
        
        InProcessProduct product = getDao(InProcessProductDao.class).findByKey(productId);
        System.out.println("-----inprocessProduct: " + product.getProductId());
        
        if(product == null) return null;
        return product.getNextProductId().trim();
        
    }
    
    /**
     * get next expected engine to be processed for the current process point id
     * @return Engine object
     */
    
	public Engine getNextExpectedEngine() {
        String productId = this.getExpectedProductId();
        if(productId == null) return null;
        return getDao(EngineDao.class).findByKey(productId);
    }
    
     private List<LotControlRule> getLotControlRule() {
        
        return getLotControlRule(ProcessManager.getInstance().getProcessPointId());

    }
    
    private List<LotControlRule> getLotControlRule(String ppId) {
    	 
        List<LotControlRule> rules = new ArrayList<LotControlRule>();
         
        for(LotControlRule rule : this.getLotControlRules()) { 
        
             if(ppId.equalsIgnoreCase(rule.getId().getProcessPointId())) {

                 rules.add(rule);
             }
         }
         
         return rules;
                
	}
    
    @SuppressWarnings("unchecked")
    private List<LotControlRule> getLotControlRules() {
        if(rules == null) {
            rules = new ArrayList<LotControlRule>();
            List<Object[]> objs= QueryHelper.getInstance().query("SEL3039");

            for(Object[] object :objs) {
                LotControlRuleId ruleId = new LotControlRuleId();
                ruleId.setPartName((String)object[0]);
                ruleId.setProcessPointId((String)object[1]);
                LotControlRule rule = new LotControlRule();
                rule.setId(ruleId);
                rules.add(rule);
            }
        }
        return rules;
    }
    
    /**
     * find an engine without defects on a specific line
     * @param trackingStatus -- line
     * @return product id of the engine
     */
    
    @SuppressWarnings("unchecked")
    public String findEngineWithoutDefects(String trackingStatus) {
        
        Vector vector = new Vector();
        vector.add(trackingStatus);
        String productId = (String)QueryHelper.getInstance().querySingleResult("TEST_SEL003", vector);
        if(productId == null) return null;
        else return productId.trim();
    }
    
    /**
     * find an engine with defects on a specific line
     * @param trackingStatus -- line
     * @return product id of the engine
     */
    
    @SuppressWarnings("unchecked")
    public String findEngineWithDefects(String trackingStatus) {
        
        Vector vector = new Vector();
        vector.add(trackingStatus);
        String productId = (String)QueryHelper.getInstance().querySingleResult("TEST_SEL004", vector);
        if(productId == null) return null;
        else return productId.trim();
    }
    
}
