package com.honda.galc.service.datacollection.work;

import java.util.Map;

import com.honda.galc.service.datacollection.HeadlessDataCollectionContext;
import com.honda.galc.service.datacollection.ProductDataCollectorBase;
import com.honda.galc.service.datacollection.script.CollectorScriptInterpreter;
/**
 * 
 * <h3>ProcessScripts</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ProcessScripts description </p>
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
 * <TD>Mar 12, 2014</TD>
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
 * @since Mar 12, 2014
 */
public class Script extends CollectorWork{
	CollectorScriptInterpreter scriptInterpreter;
	Map<String, String> scripts;
	
	public Script(HeadlessDataCollectionContext context, ProductDataCollectorBase collector, Map<String, String> scripts) {
		super(context, null);
		this.collector = collector;
		this.scripts = scripts;
	}
	
	@Override
	void doWork() throws Exception{
		getScriptInterpreter().process(scripts);
	}
	

	private CollectorScriptInterpreter getScriptInterpreter() {
		if(scriptInterpreter == null)
			scriptInterpreter = new CollectorScriptInterpreter(collector);
		
		return scriptInterpreter;
	}

}
