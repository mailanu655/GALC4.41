package com.honda.galc.client.collector;

import com.honda.galc.client.script.ClientScriptInterpreter;
import com.honda.galc.common.logging.Logger;
import com.honda.galc.script.BeanShellInterpreter;


/**
 * 
 * <h3>CollectorRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorRender description </p>
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
 * <TD>Dec 2, 2011</TD>
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
 * @since Dec 2, 2011
 */
public abstract class CollectorRenderBase<T> implements IRender<T> {
	protected ClientScriptInterpreter interpreter;
	protected final static String SCRIPT_DEFAULT_PREFIX="${";
	protected final static String SCRIPT_PREFIX="Cscript{"; //Collector Script
	protected final static String ENDING = "}";
	protected Logger logger;
	protected T renderProperty;

	public abstract Object render();

	protected Logger getLogger() {
		return logger;
	}
	
	//  Getters && Setters
	public BeanShellInterpreter getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(ClientScriptInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
