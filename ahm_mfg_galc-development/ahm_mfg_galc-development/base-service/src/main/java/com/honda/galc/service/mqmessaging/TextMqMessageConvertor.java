package com.honda.galc.service.mqmessaging;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.honda.galc.common.exception.TaskException;
import com.honda.galc.data.DataContainer;
import com.honda.galc.data.DataContainerTag;
import com.honda.galc.service.mq.IMqMessageConvertor;

public class TextMqMessageConvertor implements IMqMessageConvertor {

	@Override
	public String convert(DataContainer dc) throws Exception {
		String text ="";
		
		List<String> attributes = getAttributes(dc);
		
		boolean isFirst = true;
		// Write ATTRIBUTE_SEPARATOR separated attribute values
		for (String attrName : attributes) {
			if(isFirst) isFirst = false;
			else text += getDelimiter(dc);
			String attrValue = getAttributeValue(dc,attrName);
			text += attrValue;
		}
		
		return text;
	}
	
	public String getAttributeValue(DataContainer dc, String attributeName) {
		return String.valueOf(dc.get(attributeName));
	}
	
	public String getDelimiter(DataContainer dc) {
		return StringUtils.trimToEmpty(dc.getString(DataContainerTag.DELIMITER));
	}
	
	@SuppressWarnings("unchecked")
	protected List<String> getAttributes(DataContainer dataContainer) {
		Object orderObject = dataContainer.get(DataContainerTag.TAG_LIST);
		if(orderObject == null) {
			throw new TaskException("Cannot find " + DataContainerTag.TAG_LIST + " tag in datacontainer");
		}
		
		List<String> order = (List<String>) orderObject;
		return order;
	}
}
