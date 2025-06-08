package com.honda.galc.client.collector;

import com.honda.galc.client.script.ClientScriptInterpreter;
import com.honda.galc.common.exception.TaskException;

/**
 * 
 * <h3>CollectorClientRenderFactory</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorClientRenderFactory description </p>
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
public class CollectorClientRenderFactory {
	private static CollectorClientRenderFactory instance;
	
	private CollectorClientRenderFactory() {
		super();
	}

	public static CollectorClientRenderFactory getInstance(){
		if(instance == null)
			instance = new CollectorClientRenderFactory();
		
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public CollectorRenderBase createRender(String type, ClientScriptInterpreter interpreper){
		
		if(type.equals("html")){
			 return new CollectorHtmlRender(interpreper);
		} else 
			throw new TaskException("Invalid render type.");
	}
}
