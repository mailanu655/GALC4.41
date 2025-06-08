package com.honda.galc.client.collector;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.logging.Logger;

/**
 * 
 * <h3>HtmlTag</h3>
 * <h3> Class description</h3>
 * <h4> Description </h4>
 * <p> HtmlTag description </p>
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
 * <TD>Dec 13, 2011</TD>
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
 * @since Dec 13, 2011
 */

public class HtmlTag {
	
	private static final String NAME = "name";
	private static final Object VALUE = "value";
	private static final Object CLOSE_TAG = "closeTag";
	private String name;
	private String value;
	private Map<String, String> attributes = new HashMap<String, String>();
	private Logger logger;
	private boolean closeTag;
	
	public HtmlTag(Logger logger, String[] args) {
		super();
		this.logger = logger;
		init(args);
	}

	private void init(String[] args) {
		for(int i = 0; i < args.length; i++){
			String[] split = args[i].split("=");
			if(split.length != 2){
				logger.error("Html tag invalid attribute:" + i);
				return;
			}
			
			attributes.put(split[0], split[1]);
		}
		
		if(!attributes.containsKey(NAME)){
			logger.error("Html tag missing tag name.");
			return;
		} else {
			name = attributes.remove(NAME);
		}
		
		if(attributes.containsKey(VALUE))
			value = attributes.remove(VALUE);
		
		if(attributes.containsKey(CLOSE_TAG)){
			String tmp = attributes.remove(CLOSE_TAG);
			closeTag = Boolean.parseBoolean(tmp);
		}
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public void setAttribute(String key, String value){
		attributes.put(key, value);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(name).append(" ");
		for(String key : attributes.keySet()){
			sb.append(key).append("=");
			String tmp = attributes.get(key);
			if(tmp.matches("-?\\d+(.\\d+)?"))
				sb.append(tmp);
			else
				sb.append("\"").append(tmp).append("\"");
			
			sb.append(" ");
		}
		
		if(!StringUtils.isEmpty(value)){
			sb.append(">").append(value).append("<");
		} 

		if(closeTag) sb.append("/");
		if(!StringUtils.isEmpty(value)) sb.append(name);
		sb.append(">");
		
		return sb.toString();
	}

}
