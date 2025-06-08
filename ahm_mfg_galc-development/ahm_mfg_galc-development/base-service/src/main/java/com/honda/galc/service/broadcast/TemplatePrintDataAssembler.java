package com.honda.galc.service.broadcast;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerUtil;
import com.honda.galc.entity.product.Template;

public class TemplatePrintDataAssembler implements IPrintDataAssembler{
	
	private static final String DEFAULT_VAR_MARKER = "@";
	
	Template template;
	public TemplatePrintDataAssembler(Template template) {
		this.template = template;
	}

	public byte[] assembleData(DataContainer dc) {
		
		List<String> attributes = DataContainerUtil.getAttributes(dc);
		if(attributes.isEmpty()) return template.getTemplateDataBytes();
		
		String[] attrValues = getAttributeValues(attributes, dc);
		
		String printData = StringUtils.replaceEach(
				template.getTemplateDataString(), addMarkerForAttributes(attributes), attrValues); 
		
		return printData.getBytes();
	}
	
	
	private String[] addMarkerForAttributes(List<String> variables) {
		List<String> list = new ArrayList<String>();
		String marker = getVarMarker();
		for(String var : variables) {
			list.add(marker + var + marker);
		}
		return list.toArray(new String[list.size()]);
	}
	
	private String getVarMarker(){
		return DEFAULT_VAR_MARKER;
	}
	
	private String[] getAttributeValues(List<String> attributes, DataContainer dc) {
		List<String> values = new ArrayList<String>();
		for(String attr : attributes) {
			values.add(dc.getString(attr));
		}
		return values.toArray(new String[values.size()]);
	}
}
