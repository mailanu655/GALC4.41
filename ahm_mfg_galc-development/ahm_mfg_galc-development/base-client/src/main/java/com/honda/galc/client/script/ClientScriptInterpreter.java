package com.honda.galc.client.script;

import com.honda.galc.client.ApplicationContext;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.script.BeanShellInterpreter;
import com.honda.galc.script.ScriptHelper;

/**
 * 
 * <h3>ClientScriptInterpreter</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> ClientScriptInterpreter description </p>
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
 * <TD>Apr 12, 2012</TD>
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
 * @since Apr 12, 2012
 */
public class ClientScriptInterpreter extends BeanShellInterpreter{
    ApplicationContext appContext;
	public ClientScriptInterpreter(ApplicationContext applicationContext, Logger logger) {
		super(logger);
		this.appContext = applicationContext;
		initialize();
	}

	private void initialize() {
		set(HELPER, new ScriptHelper(getProcessPointId()) );
	}

	@Override
	protected void createInterpreter() {
		super.createInterpreter();
		interpreter.getNameSpace().importCommands("com.honda.galc.client.script.commands");

	}
	
	private String getProcessPointId() {
		return appContext == null ? "" : appContext.getProcessPointId();

	}

}
