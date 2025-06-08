package com.honda.galc.service.gts;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.message.Message;
import com.honda.galc.entity.gts.GtsArea;
import com.honda.galc.entity.gts.GtsDecisionPoint;
import com.honda.galc.entity.gts.GtsDecisionPointCondition;
import com.honda.galc.entity.gts.GtsIndicator;
import com.honda.galc.entity.gts.GtsMove;
import com.honda.galc.entity.gts.GtsNode;
import com.honda.galc.entity.enumtype.GtsIndicatorType;


/**
 * 
 * 
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p>
 * <code>AbstractDecisionController</code> is ...
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
 * <TD>Mar 28, 2008</TD>
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

public abstract class AbstractDecisionController {
    
    protected AbstractBodyTrackingService handler;
    
    private boolean isRunning = false;
    
    public AbstractDecisionController(AbstractBodyTrackingService handler){
        this.handler = handler;
    }
    
    
    public boolean isRunning() {
		return isRunning;
	}


	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	public List<AbstractDecisionRule> getDecisionRules(GtsArea area) {
        
    	List<AbstractDecisionRule> decisionRules = new ArrayList<AbstractDecisionRule>();
    	for(GtsDecisionPoint dp : area.getDecisionPoints()) {
            if(!dp.isEnabled()) continue;
            AbstractDecisionRule rule = this.createRuleClass(dp);
            if(rule != null) decisionRules.add(rule);
        }
        return decisionRules;
    }
	
	protected List<AbstractDecisionRule> getDecisionRules(GtsArea area, List<GtsMove> expiredMoves,String sourceLane, String destinationLane) {
        Map<Integer,AbstractDecisionRule> rules = new HashMap<Integer,AbstractDecisionRule>();
        
		for(GtsMove move : area.getMoves()) {
			if(expiredMoves.contains(move) || move.getSource().equals(sourceLane) || move.getSource().equals(destinationLane) ||
			   move.getDestination().equals(sourceLane) || move.getDestination().equals(destinationLane)) {
				if(!rules.containsKey(move.getDecisionPointId())) {
					AbstractDecisionRule rule = getDecisionRule(area, move.getDecisionPointId());
					if(rule != null) rules.put(move.getDecisionPointId(),rule);
				}
			}
		}
		
		return new ArrayList<AbstractDecisionRule>(rules.values());
	}
	
	protected AbstractDecisionRule getDecisionRule(GtsArea area, int decisionPointId) {
		for(GtsDecisionPoint dp : area.getDecisionPoints()) {
            if(dp.isEnabled() && dp.getId().getDecisionPointId() == decisionPointId) {
            	return createRuleClass(dp);
            }
        }
		return null;
	}
	
	protected AbstractDecisionRule getDecisionRule(GtsArea area, Class<?> clazz) {
		for(GtsDecisionPoint dp : area.getDecisionPoints()) {
            if(dp.isEnabled() && dp.getRuleClass().equals(clazz.getName())) {
            	return createRuleClass(dp);
            }
        }
		return null;
	}
    
    protected AbstractDecisionRule createRuleClass(GtsDecisionPoint dp) {
        
        try {
            Class<?> c = Class.forName(dp.getRuleClass());
            
            try {
                Constructor<?> ct = c.getConstructor();
                try {
                    AbstractDecisionRule rule = (AbstractDecisionRule)ct.newInstance(new Object[]{});
                    rule.setHandler(handler);
                    rule.setDecisionPoint(dp);
                    return rule;
                } catch (Exception e) {
                    handler.getLogger().emergency("Create Rule Class Failed. Class name : " + dp.getRuleClass() + " Exception : " + e.getMessage());
                }
            } catch (Exception e) {
                handler.getLogger().emergency("Create Rule Class Failed. Class name : "  + dp.getRuleClass() + " Exception : " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            handler.getLogger().emergency("Create Rule Class Failed. Class not found : " + dp.getRuleClass());
        }
        
        return null;
        
    }
     
    protected void basicExecuteRules() {
    	GtsArea area = handler.fetchArea();
    	
    	basicExecuteRules(area, getDecisionRules(area));
       
    }
    
    protected void basicExecuteRules(List<GtsMove> expiredMoves,String sourceLane, String destLane) {
    	GtsArea area = handler.fetchArea();
    	basicExecuteRules(area,getDecisionRules(area, expiredMoves,sourceLane, destLane));
    }
    
    protected void basicExecuteRules(GtsArea area, List<AbstractDecisionRule> rules) {
    	 for(AbstractDecisionRule rule: rules){
             
    		 try {
    			 basicExecuteRule(area, rule);
    		 }catch(Exception ex) {
    			 handler.getLogger().error(ex, "Exception occured when executing decision rule " + rule.getDecisionPoint().getDecisionPointDescription());
    		 }
          }
    }
    
    private void basicExecuteRule(GtsArea area, AbstractDecisionRule rule) {
 
    	if(!rule.getDecisionPoint().toBeExecuted()) return;
        
        // update current execution time stamp for this decision point
        updateActualTimestamp(rule.getDecisionPoint());
       
        if(checkDecisionPointConditions(rule.getDecisionPoint(),area) == null){
            
            handler.getLogger().debug("Execute decision point rule - Decision Point : " + rule.getDecisionPoint().getDecisionPointDescription());
            rule.executeRule(area);
            
        }

    }
    
    private void updateActualTimestamp(GtsDecisionPoint decisionPoint) {
        decisionPoint.setCurrentTimestamp();
        handler.updateDecisionPoint(decisionPoint);        
    }
    
    public Message checkMovePossible(GtsMove move){
        
    	GtsArea area = handler.fetchArea();
        AbstractDecisionRule rule = this.findDecisionRule(area,move.getDecisionPointId());
        if(rule != null) {
            Message message = checkDecisionPointConditions(rule.getDecisionPoint(),area);
            if(message != null) return message;
            
            return rule.checkMovePossible(move.getSource(), move.getDestination(),area);
            
        }
        
        return null;
        
    }
    
    private AbstractDecisionRule findDecisionRule(GtsArea area,int dpId) {
        
        for(AbstractDecisionRule rule : getDecisionRules(area)) {
            if(rule.getDecisionPoint().getDecisionPointId() == dpId) return rule;
        }
        
        return null;
    }
    
    private List<AbstractDecisionRule> findRulesByDecisionPointCondition(GtsArea area, List<GtsMove> expiredMoves, GtsIndicator indicator) {
    	 
    	Map<Integer,AbstractDecisionRule> rules = new HashMap<Integer,AbstractDecisionRule>();
        
    	for(GtsDecisionPointCondition condition :area.getDecisionPointConditions()) {
    		if(condition.getId().getIndicatorId().equalsIgnoreCase(indicator.getIndicatorName())) {
    			AbstractDecisionRule rule = getDecisionRule(area, condition.getDecisionPointId());
				if(rule != null) rules.put(condition.getDecisionPointId(),rule);
			}
    	}
    	
    	for(GtsMove move : expiredMoves) {
    		if(!rules.containsKey(move.getDecisionPointId())) {
    			AbstractDecisionRule rule = getDecisionRule(area, move.getDecisionPointId());
				if(rule != null) rules.put(move.getDecisionPointId(),rule);
    		}
    	}
    	
    	return new ArrayList<AbstractDecisionRule>(rules.values());
         
    }

    protected Message checkDecisionPointConditions(GtsArea area, String fromLane, String toLane) {
    	
    	int decisionPointId = 0;
    	
    	for(GtsMove move : area.getMoves()) {
    		if (move.getSource().equals(fromLane) && move.getDestination().equals(toLane))
    		{
    			decisionPointId = move.getDecisionPointId();
    			break;	
    		}
    	}
        			
        List<GtsDecisionPointCondition> decisionPointConditions = area.getDecisionPointConditions();
        
        if(decisionPointConditions.isEmpty())return null;
        
//        handler.getLogger().debug("Start checking decision point conditions.Decision point : " + dp.getDecisionPointName());

        for(GtsDecisionPointCondition condition : decisionPointConditions){
            
            if(condition.getDecisionPointId() != decisionPointId) continue;
            
            GtsIndicator indicator = area.findIndicator(condition.getId().getIndicatorId());
            
            if(indicator == null) {
                
                // error
                
                handler.getLogger().error("Invalid Configuration. Possible invalid indicator name [ " +  
                                condition.getId().getIndicatorId() + 
                                "] was defined for decision point condition. ");
                
            }else {
                if(condition.getRequiredValue() != indicator.getStatus()) {
                    
                    String msg = "Indicator " + indicator.getIndicatorName()+ 
                    "'s status is " + indicator.getStatus() +
                    ",but the required status is " + condition.getRequiredValue();
                    handler.getLogger().error("Decision point condition does not meet " + msg);
                    
                    return Message.logError(msg);
                    
                } else {
                    
                    handler.getLogger().debug("Decision point condition is ok , Indicator " + indicator.getIndicatorName()+ 
                                    "'s status is " + indicator.getStatus() +
                                    ", and the required status is " + condition.getRequiredValue());
                }
            }
        }
        
        return null;   	
   
    }

    protected Message checkDecisionPointConditions(GtsDecisionPoint dp , GtsArea area) {
        
        List<GtsDecisionPointCondition> decisionPointConditions = area.getDecisionPointConditions();
        
        if(decisionPointConditions.isEmpty())return null;
        
        handler.getLogger().debug("Start checking decision point conditions.Decision point : " + dp.getDecisionPointName());

        for(GtsDecisionPointCondition condition : decisionPointConditions){
            
            if(condition.getDecisionPointId() != dp.getDecisionPointId()) continue;
            
            GtsIndicator indicator = area.findIndicator(condition.getId().getIndicatorId());
            
            if(indicator == null) {
                
                // error
                
                handler.getLogger().error("Invalid Configuration. Possible invalid indicator name [ " +  
                                condition.getId().getIndicatorId() + 
                                "] was defined for decision point condition. ");
                
            }else {
                if(condition.getRequiredValue() != indicator.getStatus()) {
                    
                    String msg = "Indicator " + indicator.getIndicatorName()+ 
                    "'s status is " + indicator.getStatus() +
                    ",but the required status is " + condition.getRequiredValue();
                    handler.getLogger().error("Decision point condition does not meet " + msg);
                    
                    return Message.logError(msg);
                    
                } else {
                    
                    handler.getLogger().debug("Decision point condition is ok , Indicator " + indicator.getIndicatorName()+ 
                                    "'s status is " + indicator.getStatus() +
                                    ", and the required status is " + condition.getRequiredValue());
                }
            }
        }
        
        return null; 
    }
    
    
    // fire all rules
    /**
     *  fire all decision rules
     *  for each decision rule, check the decision point conditions like(Converyor status etc)
     *  
     *  the decision rule is invoked only when the decison point conditions are all good.
     */
 
    // execute rules based on the indicator change
    public void executeRules(GtsIndicator indicator, List<GtsMove> expiredMoves){
     	if(isRunning()) {
    		handler.getLogger().warn("Decision Controller is running. Exit now");
    		return;
    	}
    	
    	setRunning(true);
        
        try {
        	if(indicator.isMoveInProgress() || 
  	    	   GtsIndicatorType.CARRIER_PRESENT == indicator.getIndicatorType() ||
   	    	   GtsIndicatorType.CPLF == indicator.getIndicatorType()||
   	    	   GtsIndicatorType.LINE_FULL == indicator.getIndicatorType()) { 
        		basicExecuteRules(expiredMoves,indicator.getSourceLaneName(),indicator.getDestLaneName());
        	}else if( GtsIndicatorType.CONVEYOR_STATUS == indicator.getIndicatorType() ||
  	    	   GtsIndicatorType.CONTROL_BOX == indicator.getIndicatorType()) {
    	       GtsArea area = handler.fetchArea();
    	       basicExecuteRules(area,findRulesByDecisionPointCondition(area,expiredMoves,indicator));
    	    }else if(GtsIndicatorType.HEART_BEAT == indicator.getIndicatorType()) {
    	       basicExecuteRules();
    	    }
        }
        finally {
        	setRunning(false);
        }

    	
    }
    
    // execute rules based on open/close gate
    public void executeRules(GtsNode gate, List<GtsMove> expiredMoves){
    	if(isRunning()) {
    		handler.getLogger().warn("Decision Controller is running. Exit now");
    		return;
    	}
    	
    	setRunning(true);
        
        try {
        	String laneName = gate.getLaneName();
        	if(!StringUtils.isEmpty(laneName)) {
        		basicExecuteRules(expiredMoves,laneName, "");
        	}
        }
        finally {
        	setRunning(false);
        }
    	
    }
    
    abstract protected Map<String,AbstractDecisionRule> defineDecisionRules();


}
