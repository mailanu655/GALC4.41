package com.honda.galc.client.collector;

import java.util.Map;

import com.honda.galc.client.script.ClientScriptInterpreter;
/**
 * 
 * <h3>CollectorHtmlRender</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> CollectorHtmlRender description </p>
 * <p> htmlContent is a reserved property key for html render.
 *     RENDERS are scripts to produce htmlContent, e.g. htmlContent must be the result of RENDERS
 *     for html render type.
 * </p>
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
 * <TD>Dec 5, 2011</TD>
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
 * @since Dec 5, 2011
 */

public class CollectorHtmlRender extends CollectorRenderBase<Map<String, String>> {
       
	private static final Object HTML_CONTENT = "htmlContent";
	private static final String RENDER = "render";


	public CollectorHtmlRender(ClientScriptInterpreter interpreper) {
		this.interpreter = interpreper;
		
	}

	@Override
	public Object render() {
		return processTag();
	}

	private String processTag() {

		try {
			interpreter.setContext(RENDER, this);
			interpreter.process(renderProperty);
		} catch (Exception e) {
			logger.error(e, "Exception to process Renders:" + renderProperty);
		}
		
		String htmlContent = (String)interpreter.getContext().get(HTML_CONTENT);
		htmlContent = htmlContent.replaceAll("&lt;", "<");
		htmlContent = htmlContent.replaceAll("&gt;", ">");
		return htmlContent;
	}


	public void setProperty(Map<String, String> property) {
		renderProperty = property;
	}
	
	//Beanshell is not yet support list arguments ... so array for now!!
	public String htmlTag(String[] args){
		HtmlTag htmlTag = new HtmlTag(logger, args);
		return htmlTag.toString();
	}


}
