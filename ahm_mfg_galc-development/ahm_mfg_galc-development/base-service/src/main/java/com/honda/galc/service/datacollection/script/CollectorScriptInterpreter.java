package com.honda.galc.service.datacollection.script;

import org.apache.commons.lang.StringUtils;

import bsh.EvalError;

import com.honda.galc.script.BeanShellInterpreter;
import com.honda.galc.script.ScriptHelper;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
/**
 * 
 * <h3>CollectorScriptInterpreter</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorScriptInterpreter description </p>
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
 * <TD>May 3, 2011</TD>
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
 * @since May 3, 2011
 */
public class CollectorScriptInterpreter extends BeanShellInterpreter{

	
	private ProductDataCollectorBase collector;
	public CollectorScriptInterpreter(ProductDataCollectorBase dataCollector) {
		super(dataCollector.getContext(), dataCollector.getLogger());
		
		this.collector = dataCollector;
	}
	
	/**
	 * preparation before the script evaluation
	 * 
	 * @throws EvalError
	 */
	protected void beforeProcess() throws EvalError {
		// set internal variables to interpreter
		setContext(PRODUCT, collector.getProduct());
		setContext(PRODUCTSPEC, collector.getProductSpec());
		
		set(COLLECTOR, collector);
		set(DAO, collector.getDeviceFormatDao());
		set(INTERPRETER, this);
		set(LOGGER, collector.getLogger());
		set(HELPER, new ScriptHelper(this.collector.getProcessPointId()));
		set(CONTEXT, collector.getContext());
		
		set("OK", "OK"); 
		set("NG", "NG");
		
		super.beforeProcess();
		
	}

	public void put(String key, Object value) {
		context.put(key, value);
		if(!StringUtils.isEmpty(collector.getProductName())){
			context.put(collector.getProductName()+"." + key, value);
		}
	}
	
	
	
	
	//---------- delegate method ----------
	
	@Override
	protected void setResultsToContext(String var, Object value) {
		this.put(var, value);
	}


}
